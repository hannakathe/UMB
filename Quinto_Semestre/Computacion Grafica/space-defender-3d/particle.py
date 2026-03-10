# particle.py — Manual explosion particle system
import random, math
from panda3d.core import NodePath, TransparencyAttrib
from config import (
    EXPLOSION_PARTICLES, EXPLOSION_SPEED_MIN, EXPLOSION_SPEED_MAX,
    EXPLOSION_LIFETIME,
)

# Palette per origin type (0 = player hit, 1/2/3 = enemy type)
_PALETTES = {
    0: [(1.0, 0.4, 0.0), (1.0, 0.7, 0.0), (1.0, 1.0, 0.5)],   # orange — player
    1: [(0.3, 1.0, 0.3), (0.6, 1.0, 0.6), (1.0, 1.0, 1.0)],   # green  — type 1
    2: [(1.0, 1.0, 0.2), (1.0, 0.8, 0.0), (1.0, 1.0, 1.0)],   # yellow — type 2
    3: [(1.0, 0.2, 0.0), (1.0, 0.5, 0.2), (1.0, 1.0, 0.5)],   # red    — type 3
}


class _Particle:
    __slots__ = ('node', 'vx', 'vy', 'vz', 'lifetime', 'max_life', 'active')

    def __init__(self, template, render, x, y, colour):
        self.node = template.copyTo(render)
        self.node.setLightOff()
        self.node.setTransparency(TransparencyAttrib.MAlpha)

        size = random.uniform(0.12, 0.28)
        self.node.setScale(size)
        self.node.setPos(x, y, 0.0)
        self.node.setColorScale(*colour, 1.0)

        angle      = random.uniform(0, 2 * math.pi)
        elev       = random.uniform(-0.3, 0.8)   # slight upward bias
        speed      = random.uniform(EXPLOSION_SPEED_MIN, EXPLOSION_SPEED_MAX)
        self.vx    = math.cos(angle) * speed
        self.vy    = math.sin(angle) * speed
        self.vz    = elev * speed * 0.5

        self.lifetime = EXPLOSION_LIFETIME * random.uniform(0.6, 1.0)
        self.max_life = self.lifetime
        self.active   = True

    def update(self, dt):
        if not self.active:
            return
        self.lifetime -= dt
        if self.lifetime <= 0:
            self.active = False
            self.node.removeNode()
            return
        self.node.setX(self.node.getX() + self.vx * dt)
        self.node.setY(self.node.getY() + self.vy * dt)
        self.node.setZ(self.node.getZ() + self.vz * dt)
        # Fade out
        alpha = max(0.0, self.lifetime / self.max_life)
        self.node.setAlphaScale(alpha)
        # Shrink slightly as they fade
        s = alpha * 0.9 + 0.1
        self.node.setScale(self.node.getSx() * (1 - dt * 0.5) + 0.001)


class ParticleSystem:
    """Manages all live explosion particles."""

    def __init__(self, render, loader):
        self.render   = render
        self._particles = []
        self._template  = loader.loadModel('models/misc/sphere')
        self._template.detachNode()

    # ── public ──────────────────────────────────────────────────────────────
    def spawn_explosion(self, x: float, y: float, origin_type: int):
        palette = _PALETTES.get(origin_type, _PALETTES[1])
        count   = EXPLOSION_PARTICLES + random.randint(-2, 2)
        for _ in range(count):
            colour = random.choice(palette)
            p = _Particle(self._template, self.render, x, y, colour)
            self._particles.append(p)

    def update(self, dt):
        for p in self._particles:
            if p.active:
                p.update(dt)
        self._particles = [p for p in self._particles if p.active]

    def cleanup(self):
        for p in self._particles:
            if p.active:
                p.node.removeNode()
        self._particles.clear()
