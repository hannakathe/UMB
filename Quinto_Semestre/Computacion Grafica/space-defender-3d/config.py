# -*- coding: utf-8 -*-
# config.py — Space Defender 3D — all tunable constants

# ── Window ─────────────────────────────────────────────────────────────────
WIN_TITLE = 'Space Defender 3D'
WIN_W     = 1280
WIN_H     = 720

# ── Camera — nearly top-down, like the 2-D original ────────────────────────
# Position slightly behind (–Y) and high (Z). LookAt center of play field.
CAM_POS    = (0, -5, 34)
CAM_TARGET = (0,  0,  0)

# ── World bounds (game board on the XY plane) ──────────────────────────────
WORLD_X_MIN    = -12.0
WORLD_X_MAX    =  12.0
WORLD_Y_PLAYER =  -8.0   # default player Y (bottom of field)
WORLD_Y_SPAWN  =  10.5   # enemies appear here (top of field)
WORLD_Y_KILL   = -10.0   # enemy passing here → game over

# ── Player ──────────────────────────────────────────────────────────────────
PLAYER_SPEED_X          = 12.0   # horizontal speed  (units/s)
PLAYER_SPEED_Y          =  8.0   # forward/backward speed (units/s)
PLAYER_Y_MIN            = -10.0  # how far back player can retreat
PLAYER_Y_MAX            =  -3.5  # how far forward player can advance
PLAYER_LIVES            = 4
PLAYER_INVINCIBILITY    = 2.5    # seconds
PLAYER_SHOOT_COOLDOWN   = 0.18   # seconds between shots
PLAYER_COLLISION_RADIUS = 0.50

# ── Bullets ─────────────────────────────────────────────────────────────────
BULLET_SPEED_PLAYER = 22.0
BULLET_SPEED_ENEMY  =  9.0
BULLET_RADIUS       =  0.15

# ── Enemy types ──────────────────────────────────────────────────────────────
#   All scales roughly half of original — visually sized like the 2-D game.
#   Colors are (r,g,b) passed to Material.setDiffuse().
ENEMY_TYPES = {
    1: {                            # green saucer
        'diffuse':     (0.10, 0.90, 0.20),
        'ambient':     (0.04, 0.35, 0.08),
        'emissive':    (0.02, 0.12, 0.03),
        'speed':       2.5,
        'points':      10,
        'shoot_prob':  0.0030,      # per-frame, at 60 fps
        'radius':      0.55,
        'body_scale':  (0.70, 0.70, 0.22),
        'dome_scale':  (0.30, 0.30, 0.42),
        'dome_color':  (0.30, 1.00, 0.30),
    },
    2: {                            # yellow fighter
        'diffuse':     (1.00, 0.85, 0.05),
        'ambient':     (0.40, 0.34, 0.02),
        'emissive':    (0.10, 0.08, 0.00),
        'speed':       3.5,
        'points':      20,
        'shoot_prob':  0.0060,
        'radius':      0.55,
        'body_scale':  (0.50, 0.50, 0.50),
        'wing_scale':  (0.32, 0.16, 0.06),
        'wing_color':  (0.80, 0.65, 0.00),
    },
    3: {                            # red cruiser
        'diffuse':     (0.90, 0.10, 0.10),
        'ambient':     (0.36, 0.04, 0.04),
        'emissive':    (0.12, 0.01, 0.01),
        'speed':       4.5,
        'points':      30,
        'shoot_prob':  0.0100,
        'radius':      0.65,
        'body_scale':  (0.65, 0.65, 0.65),
        'pod_scale':   (0.30, 0.30, 0.24),
        'core_color':  (1.00, 0.50, 0.05),
    },
}

# Movement patterns – unlocked one per level (index = level – 1, clamped)
PATTERNS_BY_LEVEL = ['classic', 'wave', 'zigzag', 'circular', 'erratic', 'any']

# ── Spawning ─────────────────────────────────────────────────────────────────
SPAWN_INTERVAL_START  = 1.8
SPAWN_INTERVAL_MIN    = 0.40
MAX_ENEMIES_BASE      = 5
MAX_ENEMIES_PER_LEVEL = 2

# ── Level progression ─────────────────────────────────────────────────────────
KILLS_PER_LEVEL            = 10
SCORE_PER_LEVEL_MULTIPLIER = 150

# ── Starfield ─────────────────────────────────────────────────────────────────
STAR_COUNT        = 130
STAR_SCROLL_SPEED = 5.0

# ── Particles ─────────────────────────────────────────────────────────────────
EXPLOSION_PARTICLES = 14
EXPLOSION_SPEED_MIN = 2.0
EXPLOSION_SPEED_MAX = 6.0
EXPLOSION_LIFETIME  = 0.80

# ── Audio — expected file names (drop into assets/sounds/) ────────────────────
SFX_PATH = 'assets/sounds/sfx'
MUSIC_PATH = 'assets/sounds/music'
SFX_FILES = {
    'player_shoot': 'playerShoot.wav',
    'enemy_shoot':  'enemyShoot.wav',
    'explosion':    'explosion.wav',
    'level_up':     'levelUp.wav',
    'player_hit':   'playerHit.wav',
}
MUSIC_FILE = 'background.ogg'
