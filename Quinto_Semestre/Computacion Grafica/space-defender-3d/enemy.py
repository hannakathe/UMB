# enemy.py — Enemy ships + EnemySpawner
import random, math
from panda3d.core import NodePath, Point3, TransparencyAttrib
from config import (
    WORLD_X_MIN, WORLD_X_MAX,
    WORLD_Y_SPAWN, WORLD_Y_KILL,
    ENEMY_TYPES,
    SPAWN_INTERVAL_START, SPAWN_INTERVAL_MIN,
    MAX_ENEMIES_BASE, MAX_ENEMIES_PER_LEVEL,
    PATTERNS_BY_LEVEL,
)


# ── helpers ──────────────────────────────────────────────────────────────────
def _clamp_x(x: float) -> float:
    return max(WORLD_X_MIN + 1.0, min(WORLD_X_MAX - 1.0, x))


# ── Enemy ────────────────────────────────────────────────────────────────────
class Enemy:
    """
    A single enemy ship.  Three visual types, six movement patterns.

    Type 1 (green)  — flat saucer   — 10 pts
    Type 2 (yellow) — angular fighter — 20 pts
    Type 3 (red)    — heavy cruiser  — 30 pts

    Patterns:
      classic   straight descent
      wave      sinusoidal X oscillation
      zigzag    alternating lateral bursts
      circular  orbital path while descending
      erratic   random direction changes
      any       random choice of the above per frame  (unused in spawner)
    """

    # Shared sphere template loaded on first instantiation
    _sphere_tmpl = None

    def __init__(self, render, loader,
                 enemy_type: int, pattern: str,
                 level: int, spawn_x: float):
        self.render      = render
        self.loader      = loader
        self.enemy_type  = enemy_type
        self.pattern     = pattern
        self.alive       = True

        cfg = ENEMY_TYPES[enemy_type]
        self.speed       = cfg['speed'] + (level - 1) * 0.30
        self.points      = cfg['points']
        self.shoot_prob  = cfg['shoot_prob'] * (1 + (level - 1) * 0.10)
        self.radius      = cfg['radius']

        # Position
        self.x           = spawn_x
        self.y           = WORLD_Y_SPAWN
        self.z           = 0.0

        # Pattern state
        self._time        = random.uniform(0, math.pi * 2)
        self._base_x      = spawn_x
        self._zz_dir      = random.choice([-1, 1])
        self._zz_timer    = 0.0
        self._erratic_vx  = random.uniform(-3, 3)
        self._erratic_t   = 0.0
        self._orb_angle   = random.uniform(0, math.pi * 2)
        self._orb_cx      = spawn_x
        self._orb_cy      = WORLD_Y_SPAWN - 2.0

        # Rotation for visual interest
        self._rot_speed   = random.uniform(40, 90) * random.choice([-1, 1])

        if Enemy._sphere_tmpl is None:
            Enemy._sphere_tmpl = loader.loadModel('models/misc/sphere')
            Enemy._sphere_tmpl.detachNode()

        self.node = self._build(cfg)
        self.node.setPos(self.x, self.y, self.z)

    # ── geometry ─────────────────────────────────────────────────────────────
    def _build(self, cfg) -> NodePath:
        root   = self.render.attachNewNode(f'enemy_{id(self)}')
        tmpl   = Enemy._sphere_tmpl
        colour = cfg['color']

        def add(sx, sy, sz, px, py, pz, r, g, b, a=1.0):
            m = tmpl.copyTo(root)
            m.setScale(sx, sy, sz)
            m.setPos(px, py, pz)
            m.setColorScale(r, g, b, a)
            return m

        if self.enemy_type == 1:
            # Flat saucer
            sx, sy, sz = cfg['scale']
            add(sx, sy, sz,            0,  0,    0,    *colour[:3])
            # Dome on top
            ds = cfg['dome_scale']
            add(ds[0], ds[1], ds[2],   0,  0,  0.40,   0.5, 1.0, 0.5)
            # Rim lights (4 tiny dots)
            for angle in [0, 90, 180, 270]:
                ax = math.cos(math.radians(angle)) * sx * 0.85
                ay = math.sin(math.radians(angle)) * sy * 0.85
                dot = add(0.12, 0.12, 0.12, ax, ay, 0, 0.8, 1.0, 0.8)
                dot.setTransparency(TransparencyAttrib.MDual)

        elif self.enemy_type == 2:
            # Angular fighter
            sx, sy, sz = cfg['scale']
            add(sx, sy, sz,            0,  0,    0,    *colour[:3])
            wc = cfg['wing_color']
            # Swept wings
            add(0.60, 0.28, 0.10, -1.20, -0.10, 0, *wc[:3])
            add(0.60, 0.28, 0.10,  1.20, -0.10, 0, *wc[:3])
            # Cockpit bubble
            add(0.25, 0.25, 0.22,  0,   0.35, 0.22,  1.0, 1.0, 0.6)
            # Twin guns (nose)
            add(0.10, 0.35, 0.10, -0.25,  0.55, 0,  0.9, 0.9, 0.3)
            add(0.10, 0.35, 0.10,  0.25,  0.55, 0,  0.9, 0.9, 0.3)

        else:
            # Heavy cruiser (type 3)
            sx, sy, sz = cfg['scale']
            add(sx, sy, sz,           0,  0,    0,    *colour[:3])
            # Side pods
            add(0.55, 0.55, 0.45, -1.40,  0,    0,    0.8, 0.1, 0.1)
            add(0.55, 0.55, 0.45,  1.40,  0,    0,    0.8, 0.1, 0.1)
            # Energy core (glowing orange)
            cc = cfg['core_color']
            core = add(0.28, 0.28, 0.28, 0, 0, 0.50, *cc[:3])
            core.setTransparency(TransparencyAttrib.MDual)
            # Forward spines
            add(0.10, 0.55, 0.10,  0,  0.80, 0,   0.9, 0.3, 0.3)

        return root

    # ── update ────────────────────────────────────────────────────────────────
    def update(self, dt: float):
        if not self.alive:
            return
        self._time += dt

        p = self.pattern
        spd = self.speed

        if p == 'classic':
            self.y -= spd * dt

        elif p == 'wave':
            self.y -= spd * dt
            self.x  = self._base_x + math.sin(self._time * 2.2) * 3.8

        elif p == 'zigzag':
            self.y       -= spd * dt
            self._zz_timer += dt
            if self._zz_timer >= 0.75:
                self._zz_timer = 0.0
                self._zz_dir  *= -1
            self.x += self._zz_dir * spd * 1.8 * dt

        elif p == 'circular':
            self._orb_angle += 1.6 * dt
            self._orb_cy    -= spd * 0.55 * dt
            self.x = self._orb_cx + math.cos(self._orb_angle) * 3.5
            self.y = self._orb_cy + math.sin(self._orb_angle) * 1.8

        elif p == 'erratic':
            self.y -= spd * dt
            self._erratic_t -= dt
            if self._erratic_t <= 0.0:
                self._erratic_t  = random.uniform(0.25, 0.70)
                self._erratic_vx = random.uniform(-6, 6)
            self.x += self._erratic_vx * dt

        # Clamp X within world
        self.x = _clamp_x(self.x)

        # Slight Z bob for 3-D feel
        self.z = 0.15 * math.sin(self._time * 2.5 + self._base_x)

        self.node.setPos(self.x, self.y, self.z)
        # Continuous rotation
        self.node.setR(self.node.getR() + self._rot_speed * dt)

    # ── queries ───────────────────────────────────────────────────────────────
    def can_shoot(self, dt: float) -> bool:
        """Probabilistic shot trigger (frame-rate independent)."""
        return random.random() < self.shoot_prob * dt * 60

    def has_passed_bottom(self) -> bool:
        return self.y < WORLD_Y_KILL

    def get_pos(self) -> Point3:
        return Point3(self.x, self.y, self.z)

    # ── lifecycle ─────────────────────────────────────────────────────────────
    def destroy(self):
        if self.alive:
            self.alive = False
            self.node.removeNode()

    def cleanup(self):
        self.destroy()


# ── EnemySpawner ─────────────────────────────────────────────────────────────
class EnemySpawner:
    """
    Decides when and what kind of enemy to spawn.
    Spawn rate and enemy difficulty increase with level.
    """

    _TYPE_WEIGHTS = {
        1: {1: 65, 2: 28, 3:  7},
        2: {1: 50, 2: 38, 3: 12},
        3: {1: 40, 2: 40, 3: 20},
        4: {1: 30, 2: 42, 3: 28},
        5: {1: 22, 2: 40, 3: 38},
    }

    def __init__(self, level: int = 1):
        self._level         = level
        self._spawn_timer   = 0.0
        self._interval      = self._calc_interval(level)

    # ── public ───────────────────────────────────────────────────────────────
    def next_level(self, level: int):
        self._level    = level
        self._interval = self._calc_interval(level)

    def update(self, dt: float, active_count: int) -> bool:
        """
        Returns True if it is time to spawn a new enemy (caller should then
        call create_enemy()), False otherwise.
        """
        self._spawn_timer += dt
        max_e = MAX_ENEMIES_BASE + MAX_ENEMIES_PER_LEVEL * (self._level - 1)

        if self._spawn_timer >= self._interval and active_count < max_e:
            self._spawn_timer = 0.0
            return True
        return False

    # ── private ──────────────────────────────────────────────────────────────
    @staticmethod
    def _calc_interval(level: int) -> float:
        return max(SPAWN_INTERVAL_MIN,
                   SPAWN_INTERVAL_START - (level - 1) * 0.20)

    def _make_enemy(self) -> 'Enemy':
        # Weighted type selection
        row      = self._TYPE_WEIGHTS.get(self._level,
                                          self._TYPE_WEIGHTS[5])
        types    = list(row.keys())
        weights  = list(row.values())
        etype    = random.choices(types, weights=weights)[0]

        # Pattern selection — unlock one new pattern per level
        avail_idx = min(self._level - 1, len(PATTERNS_BY_LEVEL) - 2)
        available = PATTERNS_BY_LEVEL[:avail_idx + 1]
        # 'any' meta-pattern is replaced by a real random choice
        real_patterns = [p for p in PATTERNS_BY_LEVEL[:-1]]
        pattern = random.choice(available if 'any' not in available
                                else real_patterns[:avail_idx + 1])

        spawn_x = random.uniform(WORLD_X_MIN + 1.2, WORLD_X_MAX - 1.2)
        # Use a lazy import to avoid circular refs; render/loader supplied by caller
        return (etype, pattern, spawn_x)

    def create_enemy(self, render, loader, level) -> Enemy:
        """Full factory call: spawn params + instantiate Enemy."""
        etype, pattern, spawn_x = self._make_enemy()
        return Enemy(render, loader, etype, pattern, level, spawn_x)
