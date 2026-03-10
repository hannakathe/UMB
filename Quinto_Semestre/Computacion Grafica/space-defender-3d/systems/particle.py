# -*- coding: utf-8 -*-
# systems/particle.py — Manual explosion particle system
import random, math
from panda3d.core import Material, Vec4, TransparencyAttrib
from config import (
    EXPLOSION_PARTICLES, EXPLOSION_SPEED_MIN,
    EXPLOSION_SPEED_MAX, EXPLOSION_LIFETIME,
)

# Colour palettes per origin type
_PALETTES = {
    0: [(1.0, 0.4, 0.0), (1.0, 0.7, 0.0), (1.0, 1.0, 0.5)],   # player hit
    1: [(0.2, 1.0, 0.3), (0.5, 1.0, 0.5), (1.0, 1.0, 0.9)],   # green enemy
    2: [(1.0, 1.0, 0.1), (1.0, 0.8, 0.0), (1.0, 1.0, 0.8)],   # yellow enemy
    3: [(1.0, 0.2, 0.0), (1.0, 0.5, 0.1), (1.0, 0.9, 0.5)],   # red enemy
}


class _Particle:
    __slots__ = ('node', 'vx', 'vy', 'vz', 'lifetime', 'max_life',
                 'base_scale', 'active')

    def __init__(self, tmpl, render, x, y, colour):
        self.node = tmpl.copyTo(render)
        self.node.setLightOff()
        self.node.setTransparency(TransparencyAttrib.MAlpha)

        r, g, b = colour
        mat = Material()
        mat.setDiffuse(Vec4(r, g, b, 1))
        mat.setEmission(Vec4(r * 0.5, g * 0.5, b * 0.5, 1))
        self.node.setMaterial(mat)

        self.base_scale = random.uniform(0.10, 0.24)
        self.node.setScale(self.base_scale)
        self.node.setPos(x, y, 0.0)

        angle = random.uniform(0, 2 * math.pi)
        elev  = random.uniform(-0.3, 0.8)
        speed = random.uniform(EXPLOSION_SPEED_MIN, EXPLOSION_SPEED_MAX)
        self.vx = math.cos(angle) * speed
        self.vy = math.sin(angle) * speed
        self.vz = elev * speed * 0.5

        self.lifetime = EXPLOSION_LIFETIME * random.uniform(0.6, 1.0)
        self.max_life = self.lifetime
        self.active   = True

    def update(self, dt: float):
        self.lifetime -= dt
        if self.lifetime <= 0.0:
            self.active = False
            self.node.removeNode()
            return
        self.node.setX(self.node.getX() + self.vx * dt)
        self.node.setY(self.node.getY() + self.vy * dt)
        self.node.setZ(self.node.getZ() + self.vz * dt)
        # Fade and shrink
        frac = max(0.0, self.lifetime / self.max_life)
        self.node.setAlphaScale(frac)
        s = self.base_scale * frac * 0.85 + 0.02
        self.node.setScale(s)


class ParticleSystem:
    def __init__(self, render, loader):
        self._particles = []
        self._tmpl = loader.loadModel('models/misc/sphere')
        self._tmpl.detachNode()
        self._render = render

    def spawn_explosion(self, x: float, y: float, origin_type: int):
        palette = _PALETTES.get(origin_type, _PALETTES[1])
        count   = EXPLOSION_PARTICLES + random.randint(-2, 2)
        for _ in range(count):
            p = _Particle(self._tmpl, self._render, x, y, random.choice(palette))
            self._particles.append(p)

    def update(self, dt: float):
        for p in self._particles:
            if p.active:
                p.update(dt)
        self._particles = [p for p in self._particles if p.active]

    def cleanup(self):
        for p in self._particles:
            if p.active:
                p.node.removeNode()
        self._particles.clear()
