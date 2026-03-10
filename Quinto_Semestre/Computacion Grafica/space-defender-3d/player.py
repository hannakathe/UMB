# player.py — Player ship
import math
from panda3d.core import NodePath, Point3, TransparencyAttrib
from config import (
    WORLD_X_MIN, WORLD_X_MAX,
    WORLD_Y_PLAYER,
    PLAYER_SPEED, PLAYER_LIVES,
    PLAYER_INVINCIBILITY, PLAYER_SHOOT_COOLDOWN,
    PLAYER_COLLISION_RADIUS,
)


class Player:
    """
    3-D player ship built from primitive sphere models.
    Ship geometry (all in local space, +Y forward, +Z up):
      · Main body   — wide flat ellipsoid, cyan
      · Left wing   — swept back, blue-cyan
      · Right wing  — mirror of left
      · Cockpit     — small bright sphere on top-front
      · Engine glow — orange sphere at the back
    """

    def __init__(self, render, loader):
        self.render  = render
        self.loader  = loader

        self.lives          = PLAYER_LIVES
        self.shoot_timer    = 0.0
        self.invincible_timer = 0.0
        self._blink_timer   = 0.0
        self.alive          = True

        self.x = 0.0
        self.y = WORLD_Y_PLAYER
        self.radius = PLAYER_COLLISION_RADIUS

        self.node = self._build_ship()
        self.node.setPos(self.x, self.y, 0.0)

    # ── geometry ────────────────────────────────────────────────────────────
    def _build_ship(self) -> NodePath:
        root = self.render.attachNewNode('player')

        sphere = self.loader.loadModel('models/misc/sphere')
        sphere.detachNode()

        def add(sx, sy, sz, px, py, pz, r, g, b):
            m = sphere.copyTo(root)
            m.setScale(sx, sy, sz)
            m.setPos(px, py, pz)
            m.setColorScale(r, g, b, 1.0)
            m.setLightOff(1)   # lower priority so main lights still apply
            return m

        # Main body
        add(1.40, 0.80, 0.28,  0,  0.10, 0,    0.10, 0.70, 1.00)
        # Left wing
        add(0.75, 0.40, 0.12, -1.55, -0.10, 0,  0.00, 0.45, 0.90)
        # Right wing
        add(0.75, 0.40, 0.12,  1.55, -0.10, 0,  0.00, 0.45, 0.90)
        # Cockpit
        add(0.30, 0.30, 0.30,  0,   0.40, 0.28,  0.75, 1.00, 1.00)
        # Engine exhaust glow (back)
        engine = add(0.25, 0.22, 0.22,  0, -0.75, 0,    1.00, 0.50, 0.05)
        engine.setTransparency(TransparencyAttrib.MDual)
        self._engine_node = engine

        return root

    # ── update ──────────────────────────────────────────────────────────────
    def update(self, dt: float, keys: dict):
        if not self.alive:
            return

        # Horizontal movement
        dx = 0.0
        if keys.get('left'):
            dx -= PLAYER_SPEED
        if keys.get('right'):
            dx += PLAYER_SPEED

        self.x = max(WORLD_X_MIN + 1.6, min(WORLD_X_MAX - 1.6, self.x + dx * dt))

        # Timers
        self.shoot_timer = max(0.0, self.shoot_timer - dt)

        if self.invincible_timer > 0.0:
            self.invincible_timer -= dt
            self._blink_timer += dt
            if self._blink_timer >= 0.12:
                self._blink_timer = 0.0
                if self.node.isHidden():
                    self.node.show()
                else:
                    self.node.hide()
            if self.invincible_timer <= 0.0:
                self.node.show()

        # Engine pulse — slight scale oscillation to suggest thrust
        pulse = 0.22 + 0.06 * math.sin(self.shoot_timer * 20)
        self._engine_node.setScale(pulse, pulse * 0.9, pulse)

        self.node.setX(self.x)

    # ── queries ─────────────────────────────────────────────────────────────
    def can_shoot(self) -> bool:
        return self.alive and self.shoot_timer <= 0.0

    def register_shot(self):
        self.shoot_timer = PLAYER_SHOOT_COOLDOWN

    def get_pos(self) -> Point3:
        return Point3(self.x, self.y, 0.0)

    # ── damage ──────────────────────────────────────────────────────────────
    def take_damage(self) -> bool:
        """Returns True if damage was actually applied."""
        if self.invincible_timer > 0.0:
            return False
        self.lives -= 1
        self.invincible_timer = PLAYER_INVINCIBILITY
        self._blink_timer     = 0.0
        if self.lives <= 0:
            self.alive = False
            self.node.hide()
        return True

    # ── cleanup ─────────────────────────────────────────────────────────────
    def cleanup(self):
        self.node.removeNode()
