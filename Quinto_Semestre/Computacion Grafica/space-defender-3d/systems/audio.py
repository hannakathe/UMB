# -*- coding: utf-8 -*-
# systems/audio.py — Audio manager (graceful degradation when files missing)
import os
from config import SFX_PATH, MUSIC_PATH, SFX_FILES, MUSIC_FILE


class AudioManager:
    """
    Loads SFX and music from assets/sounds/.
    All operations are safe: missing files are skipped silently.

    Expected layout:
        assets/sounds/sfx/playerShoot.wav
        assets/sounds/sfx/enemyShoot.wav
        assets/sounds/sfx/explosion.wav
        assets/sounds/sfx/levelUp.wav
        assets/sounds/sfx/playerHit.wav
        assets/sounds/music/background.ogg
    """

    def __init__(self, base):
        self._base    = base
        self._sfx     = {}       # name → AudioSound
        self._music   = None
        self._muted   = False
        self._sfx_vol   = 0.80
        self._music_vol = 0.45

        self._load_sfx()
        self._load_music()

    # ── Loading ───────────────────────────────────────────────────────────────
    def _load_sfx(self):
        for name, filename in SFX_FILES.items():
            path = os.path.join(SFX_PATH, filename)
            if os.path.isfile(path):
                try:
                    snd = self._base.loader.loadSfx(path)
                    if snd:
                        snd.setVolume(self._sfx_vol)
                        self._sfx[name] = snd
                except Exception:
                    pass

    def _load_music(self):
        path = os.path.join(MUSIC_PATH, MUSIC_FILE)
        if os.path.isfile(path):
            try:
                snd = self._base.loader.loadSfx(path)
                if snd:
                    snd.setLoop(True)
                    snd.setVolume(self._music_vol)
                    self._music = snd
            except Exception:
                pass

    # ── Playback ──────────────────────────────────────────────────────────────
    def play(self, name: str):
        if self._muted:
            return
        snd = self._sfx.get(name)
        if snd:
            snd.setVolume(self._sfx_vol)
            snd.play()

    def play_music(self):
        if self._music and not self._muted:
            self._music.play()

    def stop_music(self):
        if self._music:
            self._music.stop()

    def pause_music(self):
        if self._music:
            self._music.setPlayRate(0.0)   # freeze

    def resume_music(self):
        if self._music and not self._muted:
            self._music.setPlayRate(1.0)

    # ── Volume / mute ─────────────────────────────────────────────────────────
    def toggle_mute(self) -> bool:
        """Returns new muted state."""
        self._muted = not self._muted
        if self._muted:
            self.stop_music()
        else:
            self.play_music()
        return self._muted

    @property
    def muted(self) -> bool:
        return self._muted

    def set_sfx_volume(self, v: float):
        self._sfx_vol = max(0.0, min(1.0, v))
        for snd in self._sfx.values():
            snd.setVolume(self._sfx_vol)

    def set_music_volume(self, v: float):
        self._music_vol = max(0.0, min(1.0, v))
        if self._music:
            self._music.setVolume(self._music_vol)
