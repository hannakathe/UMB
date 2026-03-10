# starfield.py — Scrolling 3-D star background
import random
from panda3d.core import NodePath
from config import (WORLD_X_MIN, WORLD_X_MAX, STAR_COUNT, STAR_SCROLL_SPEED)


class StarField:
    """
    Creates STAR_COUNT stars scattered across the game field.
    Stars scroll in the -Y direction to simulate forward motion.
    They are placed at varying Z heights to emphasise 3-D depth.
    Each star is a tiny sphere (template copied once from disk).
    """

    # Vertical layers give a subtle parallax effect.
    # Faster = higher Z = "closer" to the camera.
    _LAYERS = [
        {'z_range': (-0.5, 0.5), 'size_range': (0.04, 0.07), 'speed_mult': 0.80, 'count': 50},
        {'z_range': ( 0.5, 2.0), 'size_range': (0.07, 0.11), 'speed_mult': 1.00, 'count': 50},
        {'z_range': ( 2.0, 4.5), 'size_range': (0.10, 0.16), 'speed_mult': 1.25, 'count': 30},
    ]

    def __init__(self, render, loader):
        self.render = render
        self.loader = loader
        self.stars  = []

        # Load sphere model once; all stars are instances of it.
        self._template = loader.loadModel('models/misc/sphere')
        self._template.detachNode()

        for layer in self._LAYERS:
            for _ in range(layer['count']):
                self._spawn(layer, initial=True)

    # ── public ──────────────────────────────────────────────────────────────
    def update(self, dt):
        for s in self.stars:
            s['y'] -= STAR_SCROLL_SPEED * s['speed_mult'] * dt
            s['node'].setY(s['y'])
            if s['y'] < -11.0:
                self._reset_star(s)

    def cleanup(self):
        for s in self.stars:
            s['node'].removeNode()
        self.stars.clear()

    # ── private ─────────────────────────────────────────────────────────────
    def _spawn(self, layer, initial=False):
        node = self._template.copyTo(self.render)
        node.setLightOff()   # stars ignore scene lighting

        x    = random.uniform(WORLD_X_MIN - 2, WORLD_X_MAX + 2)
        y    = random.uniform(-10, 12) if initial else 12.0
        z    = random.uniform(*layer['z_range'])
        size = random.uniform(*layer['size_range'])
        br   = random.uniform(0.55, 1.0)

        node.setScale(size)
        node.setPos(x, y, z)
        node.setColorScale(br, br, br * 1.15, 1.0)

        record = {
            'node':       node,
            'x': x, 'y': y, 'z': z,
            'size':       size,
            'speed_mult': layer['speed_mult'],
            'layer':      layer,
        }
        self.stars.append(record)
        return record

    def _reset_star(self, s):
        s['x'] = random.uniform(WORLD_X_MIN - 2, WORLD_X_MAX + 2)
        s['y'] = 13.0
        s['node'].setPos(s['x'], s['y'], s['z'])
