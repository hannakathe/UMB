# -*- coding: utf-8 -*-
# systems/starfield.py — Scrolling 3-D star background
import random
from panda3d.core import NodePath
from config import WORLD_X_MIN, WORLD_X_MAX, STAR_SCROLL_SPEED

# Three depth layers — closer layers scroll faster (parallax)
_LAYERS = [
    {'z_range': (-0.3,  0.3), 'size_range': (0.04, 0.07), 'speed_mult': 0.80, 'count': 50},
    {'z_range': ( 0.3,  1.5), 'size_range': (0.07, 0.11), 'speed_mult': 1.00, 'count': 50},
    {'z_range': ( 1.5,  3.5), 'size_range': (0.10, 0.16), 'speed_mult': 1.30, 'count': 30},
]


class StarField:
    def __init__(self, render, loader):
        self.render = render
        self.stars  = []

        tmpl = loader.loadModel('models/misc/sphere')
        tmpl.detachNode()
        self._tmpl = tmpl

        for layer in _LAYERS:
            for _ in range(layer['count']):
                self._create(layer, initial=True)

    def update(self, dt: float):
        for s in self.stars:
            s['y'] -= STAR_SCROLL_SPEED * s['spd'] * dt
            s['node'].setY(s['y'])
            if s['y'] < -12.0:
                s['y'] = 13.0
                s['node'].setY(s['y'])

    def cleanup(self):
        for s in self.stars:
            s['node'].removeNode()
        self.stars.clear()

    def _create(self, layer, initial: bool):
        node = self._tmpl.copyTo(self.render)
        node.setLightOff()   # stars are self-luminous

        x    = random.uniform(WORLD_X_MIN - 2, WORLD_X_MAX + 2)
        y    = random.uniform(-11, 13) if initial else 13.0
        z    = random.uniform(*layer['z_range'])
        size = random.uniform(*layer['size_range'])
        br   = random.uniform(0.55, 1.0)

        node.setScale(size)
        node.setPos(x, y, z)
        node.setColorScale(br, br, br * 1.1, 1.0)

        self.stars.append({'node': node, 'y': y, 'spd': layer['speed_mult']})
