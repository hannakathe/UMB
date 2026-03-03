from direct.gui.OnscreenText import OnscreenText
from direct.task import Task
from panda3d.core import TextNode, Vec3

from enemy import Enemy

SPAWN_INTERVAL = 3     # segundos entre cada spawn de enemigo
CONTACT_DIST   = 0.9   # distancia enemigo-jugador para activar daño
CONTACT_DPS    = 15    # HP por segundo que pierde el jugador al contacto
SCORE_PER_KILL = 10    # puntos por enemigo eliminado


class GameManager:
    def __init__(self, base, player):
        self.base    = base
        self.player  = player
        self.score   = 0
        self.alive   = True
        self.enemies = []

        # HUD: score (esquina superior derecha)
        self.hud_score = OnscreenText(
            text="SCORE: 0",
            pos=(1.28, 0.91), scale=0.055,
            fg=(1.0, 0.85, 0.0, 1), align=TextNode.ARight,
        )

        # Spawn inicial de 3 enemigos
        for _ in range(3):
            self._spawn_one()

        # Tareas
        base.taskMgr.add(self.update, "gm_update")
        base.taskMgr.doMethodLater(SPAWN_INTERVAL, self._spawn_task, "gm_spawn")

    # ── spawn ──────────────────────────────────────────────────────────────────
    def _spawn_one(self):
        self.enemies.append(Enemy(self.base, self.player))

    def _spawn_task(self, task):
        if self.alive:
            self._spawn_one()
        return task.again   # se repite cada SPAWN_INTERVAL segundos

    # ── update principal ───────────────────────────────────────────────────────
    def update(self, task):
        if not self.alive:
            return Task.done

        dt = globalClock.getDt()

        # 1. Detectar enemigos muertos → sumar score
        alive_now = [e for e in self.enemies if e.is_alive()]
        killed    = len(self.enemies) - len(alive_now)
        if killed:
            self.score += killed * SCORE_PER_KILL
            self.hud_score.setText(f"SCORE: {self.score}")
        self.enemies = alive_now

        # 2. Actualizar HUD de HP cada frame
        hp_display = max(0, int(self.player.hp))
        self.base.hud_hp.setText(
            f"HP: {hp_display} / {self.player.max_hp}"
        )

        # 3. Daño al jugador por contacto con enemigo
        p_pos = self.player.get_pos()
        for enemy in self.enemies:
            if Vec3(enemy.get_pos() - p_pos).length() < CONTACT_DIST:
                dead = self.player.take_damage(CONTACT_DPS * dt)
                if dead:
                    self._game_over()
                    return Task.done
                break   # solo un enemigo inflige daño por frame

        return Task.cont

    # ── game over ──────────────────────────────────────────────────────────────
    def _game_over(self):
        self.alive = False

        # Detener spawn y controles
        self.base.taskMgr.remove("gm_spawn")
        self.base.ignore("mouse1")
        for key in ("w", "s", "a", "d", "w-up", "s-up", "a-up", "d-up"):
            self.base.ignore(key)

        # Congelar movimiento del jugador
        for k in self.player.keys:
            self.player.keys[k] = False

        # Limpiar balas y enemigos restantes
        for b in list(self.base.bullets):
            if b.is_alive():
                b.destroy()
        self.base.bullets.clear()

        for e in self.enemies:
            if e.is_alive():
                e.destroy()
        self.enemies.clear()

        # ── pantalla GAME OVER ─────────────────────────────────────────────────
        OnscreenText(
            text="GAME OVER",
            pos=(0, 0.18), scale=0.20,
            fg=(1.0, 0.08, 0.08, 1), align=TextNode.ACenter,
        )
        OnscreenText(
            text=f"Score final: {self.score}",
            pos=(0, -0.06), scale=0.085,
            fg=(1.0, 0.85, 0.0, 1), align=TextNode.ACenter,
        )
        OnscreenText(
            text="Cierra la ventana para salir",
            pos=(0, -0.22), scale=0.046,
            fg=(0.60, 0.60, 0.60, 1), align=TextNode.ACenter,
        )
