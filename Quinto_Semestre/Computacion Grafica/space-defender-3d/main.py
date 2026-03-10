# -*- coding: utf-8 -*-
# main.py — Space Defender 3D  (entry point)
# Run:  python main.py   (from the space-defender-3d/ directory)
#
# Controls
#   A / ←   left    D / →   right    W / ↑   forward    S / ↓   backward
#   SPACE   shoot   P       pause    M       mute        R       restart   ESC quit

import sys, os
from direct.showbase.ShowBase   import ShowBase
from direct.gui.OnscreenText    import OnscreenText
from direct.task                import Task
from panda3d.core import (
    WindowProperties, AmbientLight, DirectionalLight, PointLight,
    Vec4, TextNode, NodePath, loadPrcFileData,
)

from config import *

# Sub-packages
from entities.player   import Player
from entities.enemy    import Enemy, EnemySpawner
from entities.bullet   import Bullet
from systems.starfield import StarField
from systems.particle  import ParticleSystem
from systems.audio     import AudioManager

# ── Pre-window settings ───────────────────────────────────────────────────────
loadPrcFileData('', f'window-title {WIN_TITLE}')
loadPrcFileData('', f'win-size {WIN_W} {WIN_H}')
loadPrcFileData('', 'sync-video 1')
loadPrcFileData('', 'show-frame-rate-meter 0')


class SpaceDefender3D(ShowBase):

    INTRO, PLAYING, PAUSED, GAME_OVER = 'INTRO', 'PLAYING', 'PAUSED', 'GAME_OVER'

    def __init__(self):
        super().__init__()
        self.disableMouse()

        # ── Camera ────────────────────────────────────────────────────────────
        # Nearly top-down (like the 2-D original), high enough to see full field.
        self.camera.setPos(*CAM_POS)
        self.camera.lookAt(*CAM_TARGET)

        # ── Background ────────────────────────────────────────────────────────
        self.setBackgroundColor(0.01, 0.01, 0.06, 1)

        # ── Lighting ──────────────────────────────────────────────────────────
        self._setup_lighting()

        # ── Font for Unicode hearts ────────────────────────────────────────────
        self._hud_font = self._load_unicode_font()

        # ── Input ─────────────────────────────────────────────────────────────
        self.keys = {k: False for k in ('left','right','up','down','shoot')}
        self._bind_keys()

        # ── Persistent state ──────────────────────────────────────────────────
        self.high_score = 0

        # ── Game objects ──────────────────────────────────────────────────────
        self.state    : str                  = ''
        self.score    : int                  = 0
        self.level    : int                  = 1
        self.kills    : int                  = 0
        self.player   : Player | None        = None
        self.enemies  : list[Enemy]          = []
        self.bullets  : list[Bullet]         = []
        self.particles: ParticleSystem|None  = None
        self.starfield: StarField|None       = None
        self.spawner  : EnemySpawner|None    = None
        self.audio    : AudioManager|None    = None
        self._hud     : dict[str, OnscreenText] = {}

        # ── Main loop ─────────────────────────────────────────────────────────
        self.taskMgr.add(self._loop, 'main_loop')

        # ── Audio ─────────────────────────────────────────────────────────────
        self.audio = AudioManager(self)

        # ── Intro ─────────────────────────────────────────────────────────────
        self._show_intro()

    # ══════════════════════════════════════════════════════════════════════════
    # LIGHTING
    # ══════════════════════════════════════════════════════════════════════════
    def _setup_lighting(self):
        # Neutral ambient (slight cool tint)
        al = AmbientLight('amb')
        al.setColor(Vec4(0.32, 0.32, 0.38, 1))
        self._al_np = self.render.attachNewNode(al)
        self.render.setLight(self._al_np)

        # Main directional light (warm white, from above)
        dl = DirectionalLight('sun')
        dl.setColor(Vec4(1.0, 0.96, 0.82, 1))
        self._dl_np = self.render.attachNewNode(dl)
        self._dl_np.setHpr(15, -70, 0)   # mostly down, slight angle
        self.render.setLight(self._dl_np)

        # Fill light (opposite side, cool)
        fl = DirectionalLight('fill')
        fl.setColor(Vec4(0.18, 0.20, 0.35, 1))
        self._fl_np = self.render.attachNewNode(fl)
        self._fl_np.setHpr(195, -30, 0)
        self.render.setLight(self._fl_np)

        # Dynamic point-light that follows the player (cyan engine glow)
        pl = PointLight('player_glow')
        pl.setColor(Vec4(0.0, 0.5, 1.0, 1))
        pl.setAttenuation((1, 0.0, 0.10))
        self._pl_np = self.render.attachNewNode(pl)
        self._pl_np.setPos(0, WORLD_Y_PLAYER, 2)
        self.render.setLight(self._pl_np)

    # ══════════════════════════════════════════════════════════════════════════
    # FONT
    # ══════════════════════════════════════════════════════════════════════════
    def _load_unicode_font(self):
        """Try to load a system font that contains ♥; fall back gracefully."""
        candidates = [
            'C:/Windows/Fonts/seguisym.ttf',    # Segoe UI Symbol  (Windows)
            'C:/Windows/Fonts/segoeui.ttf',     # Segoe UI         (Windows)
            'C:/Windows/Fonts/arial.ttf',       # Arial
            '/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf',  # Linux
            '/System/Library/Fonts/Arial Unicode.ttf',           # macOS
        ]
        for path in candidates:
            if os.path.isfile(path):
                try:
                    return self.loader.loadFont(path)
                except Exception:
                    pass
        return None

    def _lives_text(self, n: int) -> str:
        if self._hud_font:
            return '♥' * max(0, n)
        return str(max(0, n)) + ' vidas'

    # ══════════════════════════════════════════════════════════════════════════
    # INPUT
    # ══════════════════════════════════════════════════════════════════════════
    def _bind_keys(self):
        mapping = [
            ('arrow_left',  'left'),  ('a', 'left'),
            ('arrow_right', 'right'), ('d', 'right'),
            ('arrow_up',    'up'),    ('w', 'up'),
            ('arrow_down',  'down'),  ('s', 'down'),
            ('space', 'shoot'),
        ]
        for key, action in mapping:
            self.accept(key,         self._set_key, [action, True])
            self.accept(f'{key}-up', self._set_key, [action, False])

        self.accept('p',      self._toggle_pause)
        self.accept('m',      self._toggle_mute)
        self.accept('r',      self._on_r)
        self.accept('enter',  self._on_enter)
        self.accept('escape', sys.exit)

    def _set_key(self, action, val):
        self.keys[action] = val

    def _on_enter(self):
        if self.state == self.INTRO:
            self._start_game()

    def _on_r(self):
        if self.state == self.GAME_OVER:
            self._start_game()

    def _toggle_pause(self):
        if self.state == self.PLAYING:
            self.state = self.PAUSED
            if self.audio:
                self.audio.pause_music()
            self._show_pause_overlay()
        elif self.state == self.PAUSED:
            self.state = self.PLAYING
            if self.audio:
                self.audio.resume_music()
            self._hide_pause_overlay()

    def _toggle_mute(self):
        if self.audio:
            muted = self.audio.toggle_mute()
            if 'mute' in self._hud:
                self._hud['mute'].setText('M: SIN SONIDO' if muted else 'M: SONIDO')

    # ══════════════════════════════════════════════════════════════════════════
    # MAIN LOOP
    # ══════════════════════════════════════════════════════════════════════════
    def _loop(self, task):
        dt = min(globalClock.getDt(), 0.05)
        if self.starfield:
            self.starfield.update(dt)
        if self.state == self.PLAYING:
            self._update_game(dt)
        return Task.cont

    def _update_game(self, dt: float):
        # ── Player ────────────────────────────────────────────────────────────
        if self.player:
            self.player.update(dt, self.keys)
            self._pl_np.setPos(self.player.x, self.player.y, 2.0)
            if self.keys['shoot'] and self.player.can_shoot():
                self._fire_player_bullet()

        # ── Enemies ───────────────────────────────────────────────────────────
        for e in self.enemies:
            if e.alive:
                e.update(dt)
                if e.can_shoot(dt):
                    self._fire_enemy_bullet(e)

        # ── Bullets ───────────────────────────────────────────────────────────
        for b in self.bullets:
            if b.active:
                b.update(dt)

        # ── Particles ─────────────────────────────────────────────────────────
        if self.particles:
            self.particles.update(dt)

        # ── Spawner ───────────────────────────────────────────────────────────
        if self.spawner:
            if self.spawner.update(dt, sum(1 for e in self.enemies if e.alive)):
                self.enemies.append(
                    self.spawner.create_enemy(self.render, self.loader, self.level)
                )

        # ── Physics / collisions ──────────────────────────────────────────────
        self._check_collisions()
        self._check_game_state()

        # ── HUD ───────────────────────────────────────────────────────────────
        self._refresh_hud()

        # ── Prune dead objects ────────────────────────────────────────────────
        self.enemies = [e for e in self.enemies if e.alive]
        self.bullets = [b for b in self.bullets if b.active]

    # ── Shooting ──────────────────────────────────────────────────────────────
    def _fire_player_bullet(self):
        pos = self.player.get_pos()
        self.bullets.append(Bullet(self.render, self.loader, pos.x, pos.y, True))
        self.player.register_shot()
        if self.audio:
            self.audio.play('player_shoot')

    def _fire_enemy_bullet(self, e: Enemy):
        self.bullets.append(Bullet(self.render, self.loader, e.x, e.y, False))
        if self.audio:
            self.audio.play('enemy_shoot')

    # ── Collisions ────────────────────────────────────────────────────────────
    def _check_collisions(self):
        if not self.player or not self.player.alive:
            return
        px, py = self.player.x, self.player.y
        pr     = self.player.radius

        for b in self.bullets:
            if not b.active:
                continue
            if b.is_player:
                for e in self.enemies:
                    if not e.alive:
                        continue
                    dx, dy = b.x - e.x, b.y - e.y
                    if dx*dx + dy*dy < (b.radius + e.radius) ** 2:
                        self._kill_enemy(e)
                        b.deactivate()
                        break
            else:
                dx, dy = b.x - px, b.y - py
                if dx*dx + dy*dy < (b.radius + pr) ** 2:
                    self._hit_player(px, py)
                    b.deactivate()

        # Enemy body contact
        for e in self.enemies:
            if not e.alive:
                continue
            dx, dy = e.x - px, e.y - py
            if dx*dx + dy*dy < (e.radius + pr) ** 2:
                self._kill_enemy(e)
                self._hit_player(px, py)

    def _kill_enemy(self, e: Enemy):
        self.score += e.points
        self.kills += 1
        if self.particles:
            self.particles.spawn_explosion(e.x, e.y, e.enemy_type)
        if self.audio:
            self.audio.play('explosion')
        e.destroy()

    def _hit_player(self, px, py):
        if self.player and self.player.take_damage():
            if self.particles:
                self.particles.spawn_explosion(px, py, 0)
            if self.audio:
                self.audio.play('player_hit')

    # ── Win / lose ─────────────────────────────────────────────────────────────
    def _check_game_state(self):
        if self.player and not self.player.alive:
            self._do_game_over()
            return
        for e in self.enemies:
            if e.has_passed_bottom():
                self._do_game_over()
                return
        if (self.kills >= KILLS_PER_LEVEL and
                self.score >= SCORE_PER_LEVEL_MULTIPLIER * self.level):
            self.kills = 0
            self._do_level_up()

    def _do_level_up(self):
        self.level += 1
        if self.spawner:
            self.spawner.next_level(self.level)
        if self.audio:
            self.audio.play('level_up')

        banner = self._txt(f'  NIVEL {self.level}!  ', (0, 0.52), 0.10, (1, 1, 0, 1))

        def _rm(task):
            banner.destroy()
            return Task.done

        self.taskMgr.doMethodLater(2.0, _rm, 'rm_banner')

    def _do_game_over(self):
        self.state = self.GAME_OVER
        if self.score > self.high_score:
            self.high_score = self.score
        if self.audio:
            self.audio.stop_music()
        self._clear_hud()
        self._show_game_over()

    # ══════════════════════════════════════════════════════════════════════════
    # SCREENS / HUD
    # ══════════════════════════════════════════════════════════════════════════

    def _show_intro(self):
        self.state = self.INTRO
        if not self.starfield:
            self.starfield = StarField(self.render, self.loader)

        self._itr: list[OnscreenText] = []
        C = TextNode.ACenter
        self._itr += [
            self._txt('SPACE DEFENDER 3D',
                      (0, 0.60), 0.115, (0.0, 1.0, 1.0, 1.0), C),
            self._txt(
                'Año 2156.  Una armada alienígena se aproxima a la Tierra.\n'
                'Eres la última esperanza de la humanidad.\n'
                '¡Destrúyelos antes de que lleguen a nuestro planeta!',
                (0, 0.22), 0.053, (0.80, 0.90, 1.0, 1.0), C),
            self._txt('Presiona  ENTER  para comenzar',
                      (0, -0.18), 0.065, (1.0, 1.0, 0.0, 1.0), C),
            self._txt(
                'A/D ←/→  Mover     W/S ↑/↓  Acercarse     ESPACIO  Disparar\n'
                'P  Pausa       M  Mute       ESC  Salir',
                (0, -0.42), 0.043, (0.65, 0.65, 0.65, 1.0), C),
            self._txt(
                '▶  Verde = 10 pts     ▶  Amarillo = 20 pts     ▶  Rojo = 30 pts',
                (0, -0.60), 0.040, (0.65, 0.80, 0.65, 1.0), C),
        ]

    def _hide_intro(self):
        for n in getattr(self, '_itr', []):
            n.destroy()
        self._itr = []

    # ── Game init / cleanup ───────────────────────────────────────────────────
    def _start_game(self):
        self._hide_intro()
        self._clear_hud()
        self._cleanup_game()

        self.score = 0
        self.level = 1
        self.kills = 0
        self.state = self.PLAYING

        if not self.starfield:
            self.starfield = StarField(self.render, self.loader)

        self.player    = Player(self.render, self.loader)
        self.enemies   = []
        self.bullets   = []
        self.particles = ParticleSystem(self.render, self.loader)
        self.spawner   = EnemySpawner(self.level)

        if self.audio:
            self.audio.play_music()

        self._build_hud()
        self._refresh_hud()

    def _cleanup_game(self):
        if self.player:
            self.player.cleanup()
            self.player = None
        for e in self.enemies:
            e.cleanup()
        self.enemies.clear()
        for b in self.bullets:
            b.cleanup()
        self.bullets.clear()
        if self.particles:
            self.particles.cleanup()
            self.particles = None
        self.spawner = None

    # ── HUD ───────────────────────────────────────────────────────────────────
    def _build_hud(self):
        self._clear_hud()
        L, C, R = TextNode.ALeft, TextNode.ACenter, TextNode.ARight
        accent = (0.45, 0.80, 1.0, 1.0)
        white  = (1.0,  1.0,  1.0, 1.0)

        self._hud['sc_lbl'] = self._txt('PUNTOS',   (-1.28,  0.91), 0.048, accent, L)
        self._hud['score']  = self._txt('0',         (-1.28,  0.82), 0.068, white,  L)
        self._hud['lv_lbl'] = self._txt('NIVEL',     ( 0.00,  0.91), 0.048, accent, C)
        self._hud['level']  = self._txt('1',          ( 0.00,  0.82), 0.068, white,  C)
        self._hud['li_lbl'] = self._txt('VIDAS',     ( 1.28,  0.91), 0.048, accent, R)
        self._hud['lives']  = self._txt(
            self._lives_text(PLAYER_LIVES), (1.28, 0.82), 0.068,
            (1.0, 0.25, 0.25, 1.0), R)
        self._hud['hi']     = self._txt(
            f'MEJOR: {self.high_score}', (1.28, -0.88), 0.042,
            (1.0, 0.80, 0.0, 1.0), R)
        self._hud['mute']   = self._txt('M: SONIDO', (-1.28, -0.88), 0.036,
                                        (0.55, 0.55, 0.55, 1.0), L)

        # Apply unicode font to lives counter if available
        if self._hud_font:
            self._hud['lives'].setFont(self._hud_font)

    def _refresh_hud(self):
        if 'score'  in self._hud: self._hud['score'].setText(str(self.score))
        if 'level'  in self._hud: self._hud['level'].setText(str(self.level))
        if 'lives'  in self._hud and self.player:
            self._hud['lives'].setText(self._lives_text(self.player.lives))
        if 'hi'     in self._hud:
            self._hud['hi'].setText(f'MEJOR: {self.high_score}')

    def _clear_hud(self):
        for n in self._hud.values():
            n.destroy()
        self._hud.clear()

    def _show_pause_overlay(self):
        self._hud['pause'] = self._txt(
            'PAUSA\n\nP — Continuar', (0, 0.10), 0.10,
            (1.0, 1.0, 0.0, 1.0), TextNode.ACenter)

    def _hide_pause_overlay(self):
        if 'pause' in self._hud:
            self._hud.pop('pause').destroy()

    def _show_game_over(self):
        C = TextNode.ACenter
        self._hud['go1'] = self._txt('GAME OVER',
                                     (0,  0.35), 0.130, (1.0, 0.15, 0.05, 1.0), C)
        self._hud['go2'] = self._txt(f'Puntuación:  {self.score}',
                                     (0,  0.13), 0.068, (1.0, 1.0,  1.0,  1.0), C)
        self._hud['go3'] = self._txt(f'Mejor puntuación:  {self.high_score}',
                                     (0,  0.01), 0.058, (1.0, 0.80, 0.0,  1.0), C)
        self._hud['go4'] = self._txt(f'Nivel alcanzado:  {self.level}',
                                     (0, -0.11), 0.053, (0.80, 0.80, 1.0, 1.0), C)
        self._hud['go5'] = self._txt('Presiona  R  para reiniciar',
                                     (0, -0.32), 0.063, (0.20, 1.0,  0.50, 1.0), C)

    # ── Text helper ───────────────────────────────────────────────────────────
    def _txt(self, text: str, pos, scale: float, fg,
             align=TextNode.ACenter) -> OnscreenText:
        t = OnscreenText(text=text, pos=pos, scale=scale, fg=fg,
                         align=align, mayChange=True)
        return t


# ── Entry point ───────────────────────────────────────────────────────────────
if __name__ == '__main__':
    game = SpaceDefender3D()
    game.run()
