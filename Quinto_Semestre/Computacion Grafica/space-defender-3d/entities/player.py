# -*- coding: utf-8 -*-
# entities/player.py — Player ship
import math
from panda3d.core import (
    NodePath, Point3, Material, Vec4, TransparencyAttrib,
)
from config import (
    WORLD_X_MIN, WORLD_X_MAX,
    WORLD_Y_PLAYER, PLAYER_Y_MIN, PLAYER_Y_MAX,
    PLAYER_SPEED_X, PLAYER_SPEED_Y,
    PLAYER_LIVES, PLAYER_INVINCIBILITY,
    PLAYER_SHOOT_COOLDOWN, PLAYER_COLLISION_RADIUS,
)


def _mat(r, g, b, emissive=0.06, shininess=30):
    """Helper: create a Material with the given base colour."""
    m = Material()
    m.setDiffuse(Vec4(r, g, b, 1))
    m.setAmbient(Vec4(r * 0.35, g * 0.35, b * 0.35, 1))
    m.setEmission(Vec4(r * emissive, g * emissive, b * emissive, 1))
    m.setSpecular(Vec4(0.6, 0.6, 0.7, 1))
    m.setShininess(shininess)
    return m


class Player:
    """
    3-D player ship assembled from scaled sphere primitives.

    Controls:
      A / ←   left       D / →   right
      W / ↑   forward    S / ↓   backward
      SPACE   shoot
    """

    _sphere_tmpl = None   # loaded once

    def __init__(self, render, loader):
        self.render  = render
        self.loader  = loader

        self.lives            = PLAYER_LIVES
        self.shoot_timer      = 0.0
        self.invincible_timer = 0.0
        self._blink_timer     = 0.0
        self.alive            = True

        self.x      = 0.0
        self.y      = WORLD_Y_PLAYER
        self.radius = PLAYER_COLLISION_RADIUS

        if Player._sphere_tmpl is None:
            Player._sphere_tmpl = loader.loadModel('models/misc/sphere')
            Player._sphere_tmpl.detachNode()

        self.node, self._engine_node = self._build()
        self.node.setPos(self.x, self.y, 0.0)

    # ── geometry ─────────────────────────────────────────────────────────────
    def _build(self):
        root = self.render.attachNewNode('player')
        tmpl = Player._sphere_tmpl

        def add(sx, sy, sz, px, py, pz, r, g, b, em=0.06):
            m = tmpl.copyTo(root)
            m.setScale(sx, sy, sz)
            m.setPos(px, py, pz)
            m.setMaterial(_mat(r, g, b, em))
            return m

        # Main fuselage — flat, wide, facing +Y (toward enemies)
        add(0.65, 0.48, 0.15,    0,   0.08,  0,    0.10, 0.65, 1.00)
        # Left wing
        add(0.40, 0.20, 0.07,   -0.85, -0.05, 0,   0.00, 0.42, 0.90)
        # Right wing
        add(0.40, 0.20, 0.07,    0.85, -0.05, 0,   0.00, 0.42, 0.90)
        # Cockpit
        add(0.16, 0.16, 0.16,    0,    0.28,  0.15, 0.70, 1.00, 1.00, 0.15)
        # Engine glow (back)
        eng = add(0.13, 0.11, 0.11,  0, -0.50, 0,    1.00, 0.48, 0.05, 0.30)
        eng.setTransparency(TransparencyAttrib.MDual)

        return root, eng

    # ── update ───────────────────────────────────────────────────────────────
    def update(self, dt: float, keys: dict):
        if not self.alive:
            return

        # Movement (X and Y axes)
        dx = 0.0
        dy = 0.0
        if keys.get('left'):   dx -= PLAYER_SPEED_X
        if keys.get('right'):  dx += PLAYER_SPEED_X
        if keys.get('up'):     dy += PLAYER_SPEED_Y
        if keys.get('down'):   dy -= PLAYER_SPEED_Y

        self.x = max(WORLD_X_MIN + 1.5, min(WORLD_X_MAX - 1.5, self.x + dx * dt))
        self.y = max(PLAYER_Y_MIN,       min(PLAYER_Y_MAX,       self.y + dy * dt))

        # Shoot timer
        self.shoot_timer = max(0.0, self.shoot_timer - dt)

        # Invincibility blink
        if self.invincible_timer > 0.0:
            self.invincible_timer -= dt
            self._blink_timer += dt
            if self._blink_timer >= 0.10:
                self._blink_timer = 0.0
                if self.node.isHidden():
                    self.node.show()
                else:
                    self.node.hide()
            if self.invincible_timer <= 0.0:
                self.node.show()

        # Engine pulse
        p = 0.13 + 0.04 * math.sin(self.shoot_timer * 25)
        self._engine_node.setScale(p, p * 0.85, p)

        self.node.setPos(self.x, self.y, 0.0)

    # ── queries / actions ────────────────────────────────────────────────────
    def can_shoot(self) -> bool:
        return self.alive and self.shoot_timer <= 0.0

    def register_shot(self):
        self.shoot_timer = PLAYER_SHOOT_COOLDOWN

    def take_damage(self) -> bool:
        if self.invincible_timer > 0.0:
            return False
        self.lives -= 1
        self.invincible_timer = PLAYER_INVINCIBILITY
        self._blink_timer     = 0.0
        if self.lives <= 0:
            self.alive = False
            self.node.hide()
        return True

    def get_pos(self) -> Point3:
        return Point3(self.x, self.y, 0.0)

    def cleanup(self):
        self.node.removeNode()
