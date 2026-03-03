from direct.task import Task

# ── Datos de personajes ────────────────────────────────────────────────────────
CHARACTER_DATA = {
    "rex": {
        "name":    "Rex",
        "role":    "Soldado",
        "speed":   10,
        "hp":      100,
        "ability": "Escudo Temporal",
        "desc":    "+defensa por 5 seg",
        "color":   (0.2, 0.6, 1.0, 1),
        "image":   "alex-soldado.png",
        "bio":     "Veterano de elite especializado\nen combate urbano y defensa tactica.",
        "abilities": [
            {
                "tag":  "PASIVA",
                "name": "Blindaje",
                "desc": "Reduce el dano recibido\nen un 15% de forma permanente.",
            },
            {
                "tag":  "HABILIDAD  [Q]",
                "name": "Escudo Temporal",
                "desc": "Genera un escudo que absorbe\n30 de dano durante 5 segundos.",
            },
            {
                "tag":  "ULTIMATE  [R]",
                "name": "Posicion Defensiva",
                "desc": "Velocidad -50% pero defensa\n+60% durante 8 segundos.",
            },
        ],
    },

    "viper": {
        "name":    "Viper",
        "role":    "Francotirador",
        "speed":   7,
        "hp":      80,
        "ability": "Disparo Preciso",
        "desc":    "x3 dano, menor cadencia",
        "color":   (1.0, 0.55, 0.1, 1),
        "image":   "hanna-francotirador.png",
        "bio":     "Especialista en eliminaciones\nde largo alcance y alto valor.",
        "abilities": [
            {
                "tag":  "PASIVA",
                "name": "Punto Debil",
                "desc": "El primer disparo a un enemigo\nhace x2 de dano.",
            },
            {
                "tag":  "HABILIDAD  [Q]",
                "name": "Disparo Preciso",
                "desc": "Bala con x3 de dano que\nperfora hasta 2 enemigos.",
            },
            {
                "tag":  "ULTIMATE  [R]",
                "name": "Vision Termica",
                "desc": "Marca todos los enemigos\nen pantalla por 10 segundos.",
            },
        ],
    },

    "blaze": {
        "name":    "Blaze",
        "role":    "Asalto",
        "speed":   14,
        "hp":      70,
        "ability": "Rafaga Rapida",
        "desc":    "3 balas por disparo",
        "color":   (1.0, 0.15, 0.3, 1),
        "image":   "negro-operador.png",
        "bio":     "Agente de choque con maxima\nvelocidad y potencia de fuego.",
        "abilities": [
            {
                "tag":  "PASIVA",
                "name": "Adrenalina",
                "desc": "Al eliminar un enemigo\nrecupera 5 HP.",
            },
            {
                "tag":  "HABILIDAD  [Q]",
                "name": "Rafaga Rapida",
                "desc": "Dispara 3 balas en cono\npor cada click del mouse.",
            },
            {
                "tag":  "ULTIMATE  [R]",
                "name": "Modo Berserker",
                "desc": "x1.8 velocidad y cadencia\ndurante 6 segundos.",
            },
        ],
    },
}


# ── Clase jugador ──────────────────────────────────────────────────────────────
class Player:
    def __init__(self, base, character_key="rex"):
        self.base   = base
        self.data   = CHARACTER_DATA[character_key]
        self.speed  = self.data["speed"]
        self.hp     = self.data["hp"]
        self.max_hp = self.hp

        # Cubo 3D con modelo built-in de Panda3D
        self.node = base.loader.loadModel("models/misc/rgbCube")
        self.node.reparentTo(base.render)
        self.node.setScale(0.5)
        self.node.setPos(0, 0, 0.5)
        self.node.setColorScale(*self.data["color"])

        # Mapa de teclas
        self.keys = {"w": False, "s": False, "a": False, "d": False}
        self._setup_controls()

        base.taskMgr.add(self.update, "player_update")

    # ── controles ──────────────────────────────────────────────────────────────
    def _setup_controls(self):
        for key in ("w", "s", "a", "d"):
            self.base.accept(key,         self._set_key, [key, True])
            self.base.accept(f"{key}-up", self._set_key, [key, False])

    def _set_key(self, key, value):
        self.keys[key] = value

    # ── update (tarea Panda3D) ─────────────────────────────────────────────────
    def update(self, task):
        dt = globalClock.getDt()
        dx, dy = 0, 0

        if self.keys["w"]: dy += 1
        if self.keys["s"]: dy -= 1
        if self.keys["a"]: dx -= 1
        if self.keys["d"]: dx += 1

        # Normalizar movimiento diagonal
        if dx != 0 and dy != 0:
            dx *= 0.707
            dy *= 0.707

        pos = self.node.getPos()
        self.node.setPos(
            pos.x + dx * self.speed * dt,
            pos.y + dy * self.speed * dt,
            pos.z,
        )
        return Task.cont

    # ── helpers ────────────────────────────────────────────────────────────────
    def take_damage(self, amount):
        """Recibe daño. Devuelve True si el jugador murio."""
        self.hp -= amount
        return self.hp <= 0

    def get_pos(self):
        return self.node.getPos()

    def destroy(self):
        self.base.taskMgr.remove("player_update")
        self.node.removeNode()
