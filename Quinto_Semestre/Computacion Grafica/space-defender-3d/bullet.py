# bullet.py — Player and enemy projectiles
from panda3d.core import NodePath, TransparencyAttrib
from config import (
    BULLET_SPEED_PLAYER, BULLET_SPEED_ENEMY, BULLET_RADIUS,
    WORLD_Y_SPAWN, WORLD_Y_KILL,
)

# Colours
_COLOUR_PLAYER = (0.85, 1.00, 1.00, 1.0)   # icy white-cyan
_COLOUR_ENEMY  = (1.00, 0.20, 0.20, 1.0)   # red


class Bullet:
    """
    A single elongated-sphere projectile.
    is_player=True  → fires upward (+Y), cyan
    is_player=False → fires downward (-Y), red
    """

    # Shared sphere template (loaded once by BulletPool).
    _template = None

    def __init__(self, render, loader, x, y, is_player: bool):
        self.is_player = is_player
        self.x         = x
        self.y         = y
        self.active    = True
        self.radius    = BULLET_RADIUS

        speed     = BULLET_SPEED_PLAYER if is_player else BULLET_SPEED_ENEMY
        self._vy  = speed if is_player else -speed
        colour    = _COLOUR_PLAYER if is_player else _COLOUR_ENEMY

        # Build node
        if Bullet._template is None:
            Bullet._template = loader.loadModel('models/misc/sphere')
            Bullet._template.detachNode()

        self.node = Bullet._template.copyTo(render)
        self.node.setLightOff()
        # Elongated capsule feel: thin in X/Z, stretched in Y
        self.node.setScale(0.16, 0.45, 0.16)
        self.node.setColorScale(*colour)
        self.node.setPos(x, y, 0.0)

        # Additive transparency so bullets glow
        self.node.setTransparency(TransparencyAttrib.MDual)

    # ── public ──────────────────────────────────────────────────────────────
    def update(self, dt):
        if not self.active:
            return
        self.y += self._vy * dt
        self.node.setY(self.y)

        # Kill bullet when it leaves the play field
        if self.y > WORLD_Y_SPAWN + 2 or self.y < WORLD_Y_KILL - 1:
            self.deactivate()

    def deactivate(self):
        if self.active:
            self.active = False
            self.node.removeNode()

    def cleanup(self):
        self.deactivate()
