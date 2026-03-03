import random
import math

from direct.task import Task
from panda3d.core import Vec3

HIT_RADIUS = 0.8   # distancia bala-enemigo para registrar impacto
SPAWN_MIN  = 10    # distancia mínima de spawn desde el jugador
SPAWN_MAX  = 15    # distancia máxima de spawn


class Enemy:
    HP     = 50
    SPEED  = 3
    DAMAGE = 10   # daño que hace cada bala al impactar

    def __init__(self, base, player):
        self.base       = base
        self.player     = player
        self.hp         = self.HP
        self._task_name = f"enemy_{id(self)}"

        # Posición de spawn aleatoria lejos del jugador
        px, py = player.get_pos().x, player.get_pos().y
        angle  = random.uniform(0, 2 * math.pi)
        dist   = random.uniform(SPAWN_MIN, SPAWN_MAX)

        # Cubo rojo (mismo modelo base que el jugador)
        self.node = base.loader.loadModel("models/misc/rgbCube")
        self.node.reparentTo(base.render)
        self.node.setScale(0.5)
        self.node.setPos(
            px + math.cos(angle) * dist,
            py + math.sin(angle) * dist,
            0.5,
        )
        self.node.setColorScale(1.0, 0.08, 0.08, 1)   # rojo

        base.taskMgr.add(self.update, self._task_name)

    # ── update (tarea Panda3D) ─────────────────────────────────────────────────
    def update(self, task):
        if self.node.isEmpty():
            return Task.done

        dt    = globalClock.getDt()
        e_pos = self.node.getPos()

        # Moverse hacia el jugador
        direction = Vec3(self.player.get_pos() - e_pos)
        if direction.length() > 0.1:
            direction.normalize()
            self.node.setPos(e_pos + direction * self.SPEED * dt)

        # Colisión con balas
        if hasattr(self.base, "bullets"):
            for bullet in list(self.base.bullets):
                if not bullet.is_alive():
                    continue
                if Vec3(e_pos - bullet.get_pos()).length() < HIT_RADIUS:
                    bullet.destroy()
                    self.base.bullets.remove(bullet)
                    if self.take_damage(self.DAMAGE):
                        self.destroy()
                        return Task.done

        return Task.cont

    # ── helpers ────────────────────────────────────────────────────────────────
    def take_damage(self, amount):
        """Aplica daño. Devuelve True si el enemigo murio."""
        self.hp -= amount
        return self.hp <= 0

    def is_alive(self):
        return not self.node.isEmpty()

    def get_pos(self):
        return self.node.getPos()

    def destroy(self):
        self.base.taskMgr.remove(self._task_name)
        if not self.node.isEmpty():
            self.node.removeNode()
