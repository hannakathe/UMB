# -*- coding: utf-8 -*-
# entities/bullet.py
from panda3d.core import Material, Vec4, TransparencyAttrib
from config import (
    BULLET_SPEED_PLAYER, BULLET_SPEED_ENEMY, BULLET_RADIUS,
    WORLD_Y_SPAWN, WORLD_Y_KILL,
)


class Bullet:
    """
    Elongated sphere travelling in +Y (player) or -Y (enemy).
    Player bullets are cyan; enemy bullets are red.
    """

    _tmpl = None

    def __init__(self, render, loader, x: float, y: float, is_player: bool):
        self.is_player = is_player
        self.x         = x
        self.y         = y
        self.active    = True
        self.radius    = BULLET_RADIUS

        self._vy = BULLET_SPEED_PLAYER if is_player else -BULLET_SPEED_ENEMY

        if Bullet._tmpl is None:
            Bullet._tmpl = loader.loadModel('models/misc/sphere')
            Bullet._tmpl.detachNode()

        self.node = Bullet._tmpl.copyTo(render)
        self.node.setScale(0.12, 0.42, 0.12)
        self.node.setPos(x, y, 0.0)

        # Material
        m = Material()
        if is_player:
            m.setDiffuse(Vec4(0.6, 1.0, 1.0, 1))
            m.setAmbient(Vec4(0.2, 0.4, 0.4, 1))
            m.setEmission(Vec4(0.1, 0.4, 0.4, 1))
        else:
            m.setDiffuse(Vec4(1.0, 0.15, 0.15, 1))
            m.setAmbient(Vec4(0.4, 0.05, 0.05, 1))
            m.setEmission(Vec4(0.4, 0.05, 0.05, 1))
        m.setShininess(50)
        self.node.setMaterial(m)
        self.node.setTransparency(TransparencyAttrib.MDual)

    def update(self, dt: float):
        if not self.active:
            return
        self.y += self._vy * dt
        self.node.setY(self.y)
        if self.y > WORLD_Y_SPAWN + 2 or self.y < WORLD_Y_KILL - 1:
            self.deactivate()

    def deactivate(self):
        if self.active:
            self.active = False
            self.node.removeNode()

    def cleanup(self):
        self.deactivate()
