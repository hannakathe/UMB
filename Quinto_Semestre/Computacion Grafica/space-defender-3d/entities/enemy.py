# -*- coding: utf-8 -*-
# entities/enemy.py — Enemy ships + EnemySpawner
import random, math
from panda3d.core import (
    NodePath, Point3, Material, Vec4, TransparencyAttrib,
)
from config import (
    WORLD_X_MIN, WORLD_X_MAX,
    WORLD_Y_SPAWN, WORLD_Y_KILL,
    ENEMY_TYPES,
    SPAWN_INTERVAL_START, SPAWN_INTERVAL_MIN,
    MAX_ENEMIES_BASE, MAX_ENEMIES_PER_LEVEL,
    PATTERNS_BY_LEVEL,
)


# ── Material helper ──────────────────────────────────────────────────────────
def _mat(r, g, b, em=0.05, shininess=25):
    m = Material()
    m.setDiffuse(Vec4(r, g, b, 1))
    m.setAmbient(Vec4(r * 0.35, g * 0.35, b * 0.35, 1))
    m.setEmission(Vec4(r * em, g * em, b * em, 1))
    m.setSpecular(Vec4(0.5, 0.5, 0.5, 1))
    m.setShininess(shininess)
    return m


def _clamp_x(x: float) -> float:
    return max(WORLD_X_MIN + 1.0, min(WORLD_X_MAX - 1.0, x))


# ── Enemy ────────────────────────────────────────────────────────────────────
class Enemy:
    """
    Three visual types, six movement patterns.

    Type 1 (green)  flat saucer     10 pts
    Type 2 (yellow) angular fighter 20 pts
    Type 3 (red)    heavy cruiser   30 pts
    """

    _sphere_tmpl = None

    def __init__(self, render, loader,
                 enemy_type: int, pattern: str,
                 level: int, spawn_x: float):

        self.render     = render
        self.loader     = loader
        self.enemy_type = enemy_type
        self.pattern    = pattern
        self.alive      = True

        cfg = ENEMY_TYPES[enemy_type]
        self.speed      = cfg['speed'] + (level - 1) * 0.30
        self.points     = cfg['points']
        self.shoot_prob = cfg['shoot_prob'] * (1 + (level - 1) * 0.10)
        self.radius     = cfg['radius']

        self.x, self.y, self.z = spawn_x, WORLD_Y_SPAWN, 0.0

        # Pattern state
        self._time      = random.uniform(0, math.pi * 2)
        self._base_x    = spawn_x
        self._zz_dir    = random.choice([-1, 1])
        self._zz_timer  = 0.0
        self._err_vx    = random.uniform(-3, 3)
        self._err_t     = 0.0
        self._orb_angle = random.uniform(0, math.pi * 2)
        self._orb_cx    = spawn_x
        self._orb_cy    = WORLD_Y_SPAWN - 2.0
        self._rot_spd   = random.uniform(40, 90) * random.choice([-1, 1])

        if Enemy._sphere_tmpl is None:
            Enemy._sphere_tmpl = loader.loadModel('models/misc/sphere')
            Enemy._sphere_tmpl.detachNode()

        self.node = self._build(cfg)
        self.node.setPos(self.x, self.y, self.z)

    # ── geometry ─────────────────────────────────────────────────────────────
    def _build(self, cfg) -> NodePath:
        root = self.render.attachNewNode(f'enemy_{id(self)}')
        tmpl = Enemy._sphere_tmpl

        def add(sx, sy, sz, px, py, pz, r, g, b, em=0.05, tr=False):
            m = tmpl.copyTo(root)
            m.setScale(sx, sy, sz)
            m.setPos(px, py, pz)
            m.setMaterial(_mat(r, g, b, em))
            if tr:
                m.setTransparency(TransparencyAttrib.MDual)
            return m

        if self.enemy_type == 1:
            # ── Flat saucer ─────────────────────────────────────────────────
            r, g, b = cfg['diffuse']
            bs = cfg['body_scale']
            add(*bs,  0,  0,    0,    r, g, b)
            # dome (lighter)
            dr, dg, db = cfg['dome_color']
            ds = cfg['dome_scale']
            add(*ds,  0,  0, 0.30, dr, dg, db, em=0.12)
            # 4 rim lights
            for ang in (0, 90, 180, 270):
                ax = math.cos(math.radians(ang)) * bs[0] * 0.82
                ay = math.sin(math.radians(ang)) * bs[1] * 0.82
                dot = add(0.10, 0.10, 0.10, ax, ay, 0.04,
                          0.6, 1.0, 0.6, em=0.40, tr=True)

        elif self.enemy_type == 2:
            # ── Angular fighter ──────────────────────────────────────────────
            r, g, b = cfg['diffuse']
            bs = cfg['body_scale']
            add(*bs,  0, 0, 0,  r, g, b)
            # swept wings
            wr, wg, wb = cfg['wing_color']
            ws = cfg['wing_scale']
            add(*ws, -0.78, -0.08, 0,  wr, wg, wb)
            add(*ws,  0.78, -0.08, 0,  wr, wg, wb)
            # cockpit
            add(0.18, 0.18, 0.16,  0, 0.28, 0.20,  1.0, 1.0, 0.6, em=0.18)
            # twin gun barrels (nose)
            add(0.07, 0.28, 0.07, -0.20, 0.45, 0,  0.9, 0.8, 0.2)
            add(0.07, 0.28, 0.07,  0.20, 0.45, 0,  0.9, 0.8, 0.2)

        else:
            # ── Heavy cruiser ────────────────────────────────────────────────
            r, g, b = cfg['diffuse']
            bs = cfg['body_scale']
            add(*bs,  0, 0, 0,  r, g, b)
            # side pods
            ps = cfg['pod_scale']
            add(*ps, -1.10, 0, 0,  r * 0.9, g * 0.9, b * 0.9)
            add(*ps,  1.10, 0, 0,  r * 0.9, g * 0.9, b * 0.9)
            # energy core (orange glow)
            cr, cg, cb = cfg['core_color']
            add(0.22, 0.22, 0.22,  0, 0, 0.42,  cr, cg, cb, em=0.45, tr=True)
            # forward spine
            add(0.08, 0.50, 0.08,  0, 0.68, 0,  r, g * 0.6, b * 0.6)

        return root

    # ── update ────────────────────────────────────────────────────────────────
    def update(self, dt: float):
        if not self.alive:
            return
        self._time += dt
        spd = self.speed

        if self.pattern == 'classic':
            self.y -= spd * dt

        elif self.pattern == 'wave':
            self.y -= spd * dt
            self.x  = self._base_x + math.sin(self._time * 2.2) * 3.5

        elif self.pattern == 'zigzag':
            self.y -= spd * dt
            self._zz_timer += dt
            if self._zz_timer >= 0.75:
                self._zz_timer = 0.0
                self._zz_dir  *= -1
            self.x += self._zz_dir * spd * 1.8 * dt

        elif self.pattern == 'circular':
            self._orb_angle += 1.6 * dt
            self._orb_cy    -= spd * 0.55 * dt
            self.x = self._orb_cx + math.cos(self._orb_angle) * 3.2
            self.y = self._orb_cy + math.sin(self._orb_angle) * 1.6

        elif self.pattern == 'erratic':
            self.y -= spd * dt
            self._err_t -= dt
            if self._err_t <= 0.0:
                self._err_t  = random.uniform(0.25, 0.65)
                self._err_vx = random.uniform(-6, 6)
            self.x += self._err_vx * dt

        self.x = _clamp_x(self.x)
        # Slight Z bob for 3-D feel
        self.z = 0.12 * math.sin(self._time * 2.8 + self._base_x)
        self.node.setPos(self.x, self.y, self.z)
        self.node.setR(self.node.getR() + self._rot_spd * dt)

    # ── queries ───────────────────────────────────────────────────────────────
    def can_shoot(self, dt: float) -> bool:
        return random.random() < self.shoot_prob * dt * 60

    def has_passed_bottom(self) -> bool:
        return self.y < WORLD_Y_KILL

    def get_pos(self) -> Point3:
        return Point3(self.x, self.y, self.z)

    def destroy(self):
        if self.alive:
            self.alive = False
            self.node.removeNode()

    def cleanup(self):
        self.destroy()


# ── EnemySpawner ─────────────────────────────────────────────────────────────
class EnemySpawner:
    _TYPE_WEIGHTS = {
        1: {1: 65, 2: 28, 3:  7},
        2: {1: 50, 2: 38, 3: 12},
        3: {1: 40, 2: 40, 3: 20},
        4: {1: 30, 2: 42, 3: 28},
        5: {1: 22, 2: 40, 3: 38},
    }

    def __init__(self, level: int = 1):
        self._level       = level
        self._spawn_timer = 0.0
        self._interval    = self._calc_interval(level)

    def next_level(self, level: int):
        self._level    = level
        self._interval = self._calc_interval(level)

    def update(self, dt: float, active_count: int) -> bool:
        """Returns True when it is time to spawn; caller calls create_enemy()."""
        self._spawn_timer += dt
        max_e = MAX_ENEMIES_BASE + MAX_ENEMIES_PER_LEVEL * (self._level - 1)
        if self._spawn_timer >= self._interval and active_count < max_e:
            self._spawn_timer = 0.0
            return True
        return False

    def create_enemy(self, render, loader, level: int) -> Enemy:
        row     = self._TYPE_WEIGHTS.get(self._level, self._TYPE_WEIGHTS[5])
        etype   = random.choices(list(row.keys()), weights=list(row.values()))[0]

        avail_idx = min(self._level - 1, len(PATTERNS_BY_LEVEL) - 2)
        available = PATTERNS_BY_LEVEL[:avail_idx + 1]
        if 'any' in available:
            available = PATTERNS_BY_LEVEL[:avail_idx]   # real patterns only
        pattern = random.choice(available)

        spawn_x = random.uniform(WORLD_X_MIN + 1.2, WORLD_X_MAX - 1.2)
        return Enemy(render, loader, etype, pattern, level, spawn_x)

    @staticmethod
    def _calc_interval(level: int) -> float:
        return max(SPAWN_INTERVAL_MIN,
                   SPAWN_INTERVAL_START - (level - 1) * 0.20)
