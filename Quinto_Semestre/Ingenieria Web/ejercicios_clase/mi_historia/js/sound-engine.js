/**
 * sound-engine.js — El Último Commit
 * Motor de sonido procedural usando Web Audio API.
 * Todos los efectos se generan en tiempo real — sin archivos externos.
 *
 * Uso:
 *   SoundEngine.init()         → llamar una vez (primer gesto de usuario)
 *   SoundEngine.play('typing') → reproducir un efecto
 *   SoundEngine.startAmbient('nexus_hum')  → sonido de fondo en loop
 *   SoundEngine.stopAmbient()              → detener fondo
 */

const SoundEngine = (() => {
  /* ================================================================
     CONTEXTO DE AUDIO
     ================================================================ */
  let ctx = null;
  let masterGain = null;
  let ambientNode = null;
  let ambientGain = null;
  let _initialized = false;
  let _muted = false;

  // Volúmenes globales
  const VOL = {
    master:  0.7,
    ambient: 0.18,
    sfx:     0.55,
    typing:  0.09,
    alarm:   0.65,
    ui:      0.35,
  };

  /* ================================================================
     INICIALIZACIÓN
     ================================================================ */
  function init() {
    if (_initialized) return;
    try {
      ctx = new (window.AudioContext || window.webkitAudioContext)();
      masterGain = ctx.createGain();
      masterGain.gain.value = VOL.master;
      masterGain.connect(ctx.destination);
      _initialized = true;
    } catch(e) {
      console.warn('[SoundEngine] Web Audio API no disponible:', e);
    }
  }

  function ensureCtx() {
    if (!ctx) init();
    if (ctx && ctx.state === 'suspended') ctx.resume();
  }

  /* ================================================================
     UTILIDADES DE SÍNTESIS
     ================================================================ */

  /** Crea un oscilador simple y lo conecta al master */
  function osc(type, freq, vol, start, dur, dest) {
    const g = dest || masterGain;
    const gainNode = ctx.createGain();
    gainNode.gain.value = vol;
    gainNode.connect(g);

    const o = ctx.createOscillator();
    o.type = type;
    o.frequency.value = freq;
    o.connect(gainNode);
    o.start(start);
    o.stop(start + dur);
    return { osc: o, gain: gainNode };
  }

  /** Curva de ataque-sustain-decay */
  function ramp(param, startVal, endVal, startTime, endTime) {
    param.setValueAtTime(startVal, startTime);
    param.linearRampToValueAtTime(endVal, endTime);
  }

  /** Ruido blanco */
  function noiseBuffer(dur) {
    const samples = Math.ceil(ctx.sampleRate * dur);
    const buf = ctx.createBuffer(1, samples, ctx.sampleRate);
    const data = buf.getChannelData(0);
    for (let i = 0; i < samples; i++) data[i] = Math.random() * 2 - 1;
    return buf;
  }

  /** Ruido marrón (más grave) */
  function brownNoiseBuffer(dur) {
    const samples = Math.ceil(ctx.sampleRate * dur);
    const buf = ctx.createBuffer(1, samples, ctx.sampleRate);
    const data = buf.getChannelData(0);
    let last = 0;
    for (let i = 0; i < samples; i++) {
      const w = Math.random() * 2 - 1;
      data[i] = (last + 0.02 * w) / 1.02;
      last = data[i];
      data[i] *= 3.5;
    }
    return buf;
  }

  /** Toca un buffer de ruido con envolvente ADSR */
  function playNoise(buf, vol, attack, decay, start) {
    const bs = ctx.createBufferSource();
    bs.buffer = buf;
    const gain = ctx.createGain();
    gain.gain.setValueAtTime(0, start);
    gain.gain.linearRampToValueAtTime(vol, start + attack);
    gain.gain.linearRampToValueAtTime(0, start + attack + decay);
    bs.connect(gain);
    gain.connect(masterGain);
    bs.start(start);
    bs.stop(start + attack + decay + 0.05);
  }

  /* ================================================================
     EFECTOS DE SONIDO (SFX)
     ================================================================ */

  const SFX = {

    /* ──────────────────────────────────────────────
       TYPING — tecleo de teclado mecánico
    ────────────────────────────────────────────── */
    typing() {
      const now = ctx.currentTime;
      // Click mecánico corto (ruido + tono bajo)
      const buf = noiseBuffer(0.04);
      playNoise(buf, VOL.typing * 0.9, 0.001, 0.035, now);

      // Tono breve de tecla (varía aleatoriamente)
      const freq = 180 + Math.random() * 120;
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.typing * 0.4, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.06);
      g.connect(masterGain);
      const o = ctx.createOscillator();
      o.type = 'triangle';
      o.frequency.value = freq;
      o.connect(g);
      o.start(now);
      o.stop(now + 0.07);
    },

    /* ──────────────────────────────────────────────
       TERMINAL LINE — aparición de línea en terminal
    ────────────────────────────────────────────── */
    terminalLine() {
      const now = ctx.currentTime;
      // Beep de terminal corto
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.ui * 0.5, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.12);
      g.connect(masterGain);

      const o = ctx.createOscillator();
      o.type = 'square';
      o.frequency.setValueAtTime(660, now);
      o.frequency.linearRampToValueAtTime(440, now + 0.08);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.13);
    },

    /* ──────────────────────────────────────────────
       PANEL ADVANCE — avanzar viñeta (click suave)
    ────────────────────────────────────────────── */
    panelAdvance() {
      const now = ctx.currentTime;
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.ui, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.1);
      g.connect(masterGain);

      const o = ctx.createOscillator();
      o.type = 'sine';
      o.frequency.setValueAtTime(320, now);
      o.frequency.linearRampToValueAtTime(640, now + 0.05);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.11);
    },

    /* ──────────────────────────────────────────────
       CHAPTER START — inicio de capítulo
    ────────────────────────────────────────────── */
    chapterStart() {
      const now = ctx.currentTime;
      // Tres tonos ascendentes tipo "boot sequence"
      const freqs = [220, 330, 550];
      freqs.forEach((f, i) => {
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, now + i * 0.14);
        g.gain.linearRampToValueAtTime(VOL.ui * 0.7, now + i * 0.14 + 0.03);
        g.gain.exponentialRampToValueAtTime(0.0001, now + i * 0.14 + 0.22);
        g.connect(masterGain);

        const o = ctx.createOscillator();
        o.type = 'square';
        o.frequency.value = f;
        o.connect(g);
        o.start(now + i * 0.14);
        o.stop(now + i * 0.14 + 0.23);
      });
    },

    /* ──────────────────────────────────────────────
       CODE CHALLENGE — activar desafío de código
    ────────────────────────────────────────────── */
    codeChallenge() {
      const now = ctx.currentTime;
      // Sonido "acceso a sistema" — glide descendente
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.sfx * 0.6, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.5);
      g.connect(masterGain);

      const o = ctx.createOscillator();
      o.type = 'sawtooth';
      o.frequency.setValueAtTime(880, now);
      o.frequency.exponentialRampToValueAtTime(110, now + 0.45);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.5);
    },

    /* ──────────────────────────────────────────────
       CODE CORRECT — respuesta correcta
    ────────────────────────────────────────────── */
    codeCorrect() {
      const now = ctx.currentTime;
      // Acorde triunfante: Do-Mi-Sol en secuencia
      const notes = [523.25, 659.25, 783.99];
      notes.forEach((f, i) => {
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, now + i * 0.1);
        g.gain.linearRampToValueAtTime(VOL.sfx * 0.5, now + i * 0.1 + 0.02);
        g.gain.exponentialRampToValueAtTime(0.0001, now + i * 0.1 + 0.35);
        g.connect(masterGain);

        const o = ctx.createOscillator();
        o.type = 'triangle';
        o.frequency.value = f;
        o.connect(g);
        o.start(now + i * 0.1);
        o.stop(now + i * 0.1 + 0.36);
      });
    },

    /* ──────────────────────────────────────────────
       CODE ERROR — respuesta incorrecta (shake)
    ────────────────────────────────────────────── */
    codeError() {
      const now = ctx.currentTime;
      // Buzz de error
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.sfx * 0.55, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.25);
      g.connect(masterGain);

      const o = ctx.createOscillator();
      o.type = 'sawtooth';
      o.frequency.setValueAtTime(120, now);
      o.frequency.linearRampToValueAtTime(80, now + 0.25);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.26);

      // Segundo pulso
      const g2 = ctx.createGain();
      g2.gain.setValueAtTime(VOL.sfx * 0.35, now + 0.1);
      g2.gain.exponentialRampToValueAtTime(0.0001, now + 0.28);
      g2.connect(masterGain);
      const o2 = ctx.createOscillator();
      o2.type = 'square';
      o2.frequency.value = 100;
      o2.connect(g2);
      o2.start(now + 0.1);
      o2.stop(now + 0.29);
    },

    /* ──────────────────────────────────────────────
       HINT — mostrar pista
    ────────────────────────────────────────────── */
    hint() {
      const now = ctx.currentTime;
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.ui * 0.6, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.25);
      g.connect(masterGain);

      const o = ctx.createOscillator();
      o.type = 'sine';
      o.frequency.setValueAtTime(500, now);
      o.frequency.linearRampToValueAtTime(750, now + 0.12);
      o.frequency.linearRampToValueAtTime(620, now + 0.24);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.26);
    },

    /* ──────────────────────────────────────────────
       CHOICE SELECT — seleccionar decisión
    ────────────────────────────────────────────── */
    choiceSelect() {
      const now = ctx.currentTime;
      // Tono ciberpunk de confirmación
      [440, 550].forEach((f, i) => {
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, now + i * 0.08);
        g.gain.linearRampToValueAtTime(VOL.ui * 0.65, now + i * 0.08 + 0.01);
        g.gain.exponentialRampToValueAtTime(0.0001, now + i * 0.08 + 0.2);
        g.connect(masterGain);
        const o = ctx.createOscillator();
        o.type = 'square';
        o.frequency.value = f;
        o.connect(g);
        o.start(now + i * 0.08);
        o.stop(now + i * 0.08 + 0.21);
      });
    },

    /* ──────────────────────────────────────────────
       NEXUS MESSAGE — NEXUS se comunica
    ────────────────────────────────────────────── */
    nexusMessage() {
      const now = ctx.currentTime;
      // Síntesis de voz sintética profunda
      const g = ctx.createGain();
      g.gain.setValueAtTime(0, now);
      g.gain.linearRampToValueAtTime(VOL.sfx * 0.7, now + 0.05);
      g.gain.linearRampToValueAtTime(VOL.sfx * 0.5, now + 0.6);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 1.0);
      g.connect(masterGain);

      // Oscilador principal modulado (efecto voz IA)
      const carrier = ctx.createOscillator();
      carrier.type = 'sawtooth';
      carrier.frequency.value = 80;

      const modGain = ctx.createGain();
      modGain.gain.value = 15;

      const modulator = ctx.createOscillator();
      modulator.type = 'sine';
      modulator.frequency.value = 5.5;
      modulator.connect(modGain);
      modGain.connect(carrier.frequency);

      // Filtro para dar calidez
      const filter = ctx.createBiquadFilter();
      filter.type = 'bandpass';
      filter.frequency.value = 800;
      filter.Q.value = 2;

      carrier.connect(filter);
      filter.connect(g);

      carrier.start(now);
      modulator.start(now);
      carrier.stop(now + 1.05);
      modulator.stop(now + 1.05);
    },

    /* ──────────────────────────────────────────────
       ALARM — alarma de tiempo restante
    ────────────────────────────────────────────── */
    alarm() {
      const now = ctx.currentTime;
      // Sirena alternante en 3 pulsos
      for (let i = 0; i < 3; i++) {
        const t = now + i * 0.45;
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, t);
        g.gain.linearRampToValueAtTime(VOL.alarm, t + 0.04);
        g.gain.linearRampToValueAtTime(VOL.alarm * 0.8, t + 0.2);
        g.gain.exponentialRampToValueAtTime(0.0001, t + 0.42);
        g.connect(masterGain);

        const o = ctx.createOscillator();
        o.type = 'sawtooth';
        o.frequency.setValueAtTime(440, t);
        o.frequency.setValueAtTime(550, t + 0.22);
        o.connect(g);
        o.start(t);
        o.stop(t + 0.44);
      }
    },

    /* ──────────────────────────────────────────────
       GLITCH — efecto de glitch
    ────────────────────────────────────────────── */
    glitch() {
      const now = ctx.currentTime;
      // Ráfagas aleatorias de ruido digital
      for (let i = 0; i < 6; i++) {
        const t = now + Math.random() * 0.3;
        const buf = noiseBuffer(0.02 + Math.random() * 0.04);
        playNoise(buf, VOL.sfx * (0.3 + Math.random() * 0.4), 0.001, 0.03, t);

        // Tono distorsionado aleatorio
        const g = ctx.createGain();
        g.gain.setValueAtTime(VOL.ui * 0.3, t);
        g.gain.exponentialRampToValueAtTime(0.0001, t + 0.05);
        g.connect(masterGain);
        const o = ctx.createOscillator();
        o.type = 'square';
        o.frequency.value = 200 + Math.random() * 800;
        o.connect(g);
        o.start(t);
        o.stop(t + 0.06);
      }
    },

    /* ──────────────────────────────────────────────
       DDoS ATTACK — ataque digital
    ────────────────────────────────────────────── */
    ddosAttack() {
      const now = ctx.currentTime;
      // Ráfaga caótica más intensa
      for (let i = 0; i < 12; i++) {
        const t = now + i * 0.06;
        const buf = noiseBuffer(0.05);
        playNoise(buf, VOL.sfx * 0.5, 0.002, 0.045, t);
      }
      // Glide descendente de fondo
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.sfx * 0.6, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.9);
      g.connect(masterGain);
      const o = ctx.createOscillator();
      o.type = 'sawtooth';
      o.frequency.setValueAtTime(1200, now);
      o.frequency.exponentialRampToValueAtTime(60, now + 0.85);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.9);
    },

    /* ──────────────────────────────────────────────
       HACK ATTEMPT — intentar hackear
    ────────────────────────────────────────────── */
    hackAttempt() {
      const now = ctx.currentTime;
      // Secuencia de tecleo rápido + beep
      for (let i = 0; i < 8; i++) {
        const t = now + i * 0.07;
        const buf = noiseBuffer(0.03);
        playNoise(buf, VOL.typing * 1.4, 0.001, 0.025, t);
      }
      // Tono final de confirmación (o falla)
      const g2 = ctx.createGain();
      g2.gain.setValueAtTime(VOL.sfx * 0.5, now + 0.6);
      g2.gain.exponentialRampToValueAtTime(0.0001, now + 1.1);
      g2.connect(masterGain);
      const o2 = ctx.createOscillator();
      o2.type = 'square';
      o2.frequency.setValueAtTime(300, now + 0.6);
      o2.frequency.linearRampToValueAtTime(150, now + 1.05);
      o2.connect(g2);
      o2.start(now + 0.6);
      o2.stop(now + 1.1);
    },

    /* ──────────────────────────────────────────────
       FINAL COLAPSO — fin oscuro
    ────────────────────────────────────────────── */
    finalColapso() {
      const now = ctx.currentTime;
      // Tono grave y pesado + explosión de ruido
      const g = ctx.createGain();
      g.gain.setValueAtTime(0, now);
      g.gain.linearRampToValueAtTime(VOL.sfx * 0.8, now + 0.1);
      g.gain.linearRampToValueAtTime(VOL.sfx * 0.6, now + 1.5);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 3.0);
      g.connect(masterGain);
      const o = ctx.createOscillator();
      o.type = 'sawtooth';
      o.frequency.setValueAtTime(55, now);
      o.frequency.linearRampToValueAtTime(27.5, now + 2.5);
      o.connect(g);
      o.start(now);
      o.stop(now + 3.0);

      // Ruido de colapso
      const buf = brownNoiseBuffer(2.5);
      playNoise(buf, VOL.sfx * 0.7, 0.05, 2.3, now);
    },

    /* ──────────────────────────────────────────────
       FINAL ALIANZA — fin esperanzador
    ────────────────────────────────────────────── */
    finalAlianza() {
      const now = ctx.currentTime;
      // Acorde mayor ascendente + sustain
      const melody = [261.63, 329.63, 392, 523.25, 659.25];
      melody.forEach((f, i) => {
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, now + i * 0.18);
        g.gain.linearRampToValueAtTime(VOL.sfx * 0.45, now + i * 0.18 + 0.05);
        g.gain.exponentialRampToValueAtTime(0.0001, now + i * 0.18 + 1.2);
        g.connect(masterGain);
        const o = ctx.createOscillator();
        o.type = 'triangle';
        o.frequency.value = f;
        o.connect(g);
        o.start(now + i * 0.18);
        o.stop(now + i * 0.18 + 1.25);
      });
    },

    /* ──────────────────────────────────────────────
       FINAL CÓDIGO LIBRE — fin ambiguo
    ────────────────────────────────────────────── */
    finalCodigoLibre() {
      const now = ctx.currentTime;
      // Glitches + notas ascendentes que se dispersan
      SFX.glitch();
      const freqs = [220, 277, 370, 440, 554, 739];
      freqs.forEach((f, i) => {
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, now + 0.4 + i * 0.15);
        g.gain.linearRampToValueAtTime(VOL.sfx * 0.4, now + 0.4 + i * 0.15 + 0.04);
        g.gain.exponentialRampToValueAtTime(0.0001, now + 0.4 + i * 0.15 + 0.9);
        g.connect(masterGain);
        const o = ctx.createOscillator();
        o.type = 'square';
        o.frequency.value = f;
        o.connect(g);
        o.start(now + 0.4 + i * 0.15);
        o.stop(now + 0.4 + i * 0.15 + 0.95);
      });
    },

    /* ──────────────────────────────────────────────
       COUNTDOWN TICK — tick urgente del temporizador
    ────────────────────────────────────────────── */
    countdownTick() {
      const now = ctx.currentTime;
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.ui * 0.45, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.08);
      g.connect(masterGain);
      const o = ctx.createOscillator();
      o.type = 'square';
      o.frequency.value = 880;
      o.connect(g);
      o.start(now);
      o.stop(now + 0.09);
    },

    /* ──────────────────────────────────────────────
       BUTTON HOVER — hover sobre botón
    ────────────────────────────────────────────── */
    buttonHover() {
      const now = ctx.currentTime;
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.ui * 0.2, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.07);
      g.connect(masterGain);
      const o = ctx.createOscillator();
      o.type = 'sine';
      o.frequency.value = 750;
      o.connect(g);
      o.start(now);
      o.stop(now + 0.08);
    },

    /* ──────────────────────────────────────────────
       RESET / MENU — limpiar estado
    ────────────────────────────────────────────── */
    reset() {
      const now = ctx.currentTime;
      // Borrado: tono descendente
      const g = ctx.createGain();
      g.gain.setValueAtTime(VOL.ui, now);
      g.gain.exponentialRampToValueAtTime(0.0001, now + 0.4);
      g.connect(masterGain);
      const o = ctx.createOscillator();
      o.type = 'sine';
      o.frequency.setValueAtTime(440, now);
      o.frequency.exponentialRampToValueAtTime(110, now + 0.38);
      o.connect(g);
      o.start(now);
      o.stop(now + 0.4);
    },

  }; // fin SFX

  /* ================================================================
     SONIDOS AMBIENTALES (LOOPS)
     ================================================================ */

  const AMBIENTS = {

    /* ── Portada: zumbido electrónico ── */
    coverHum(dest) {
      const nodes = [];

      // Tono base LFO (hum profundo)
      const osc1 = ctx.createOscillator();
      osc1.type = 'sine';
      osc1.frequency.value = 45;

      const osc2 = ctx.createOscillator();
      osc2.type = 'sine';
      osc2.frequency.value = 50.3; // ligero desafinado → batimiento

      const osc3 = ctx.createOscillator();
      osc3.type = 'triangle';
      osc3.frequency.value = 90;

      const g1 = ctx.createGain(); g1.gain.value = 0.4;
      const g2 = ctx.createGain(); g2.gain.value = 0.35;
      const g3 = ctx.createGain(); g3.gain.value = 0.2;

      // LFO para modulación sutil de volumen
      const lfo = ctx.createOscillator();
      lfo.type = 'sine';
      lfo.frequency.value = 0.2;
      const lfoGain = ctx.createGain();
      lfoGain.gain.value = 0.07;
      lfo.connect(lfoGain);
      lfoGain.connect(dest.gain);

      // Filtro pasa-bajos
      const filter = ctx.createBiquadFilter();
      filter.type = 'lowpass';
      filter.frequency.value = 300;
      filter.Q.value = 3;

      osc1.connect(g1); g1.connect(filter);
      osc2.connect(g2); g2.connect(filter);
      osc3.connect(g3); g3.connect(filter);
      filter.connect(dest);

      [osc1, osc2, osc3, lfo].forEach(n => n.start());
      nodes.push(osc1, osc2, osc3, lfo, lfoGain, g1, g2, g3, filter);

      return nodes;
    },

    /* ── Historia: sala de servidores ── */
    serverRoom(dest) {
      const nodes = [];

      // Fan blanco suave
      const buf = noiseBuffer(3);
      const fanLoop = ctx.createBufferSource();
      fanLoop.buffer = buf;
      fanLoop.loop = true;
      const fanFilter = ctx.createBiquadFilter();
      fanFilter.type = 'bandpass';
      fanFilter.frequency.value = 600;
      fanFilter.Q.value = 0.4;
      const fanGain = ctx.createGain();
      fanGain.gain.value = 0.35;
      fanLoop.connect(fanFilter);
      fanFilter.connect(fanGain);
      fanGain.connect(dest);
      fanLoop.start();
      nodes.push(fanLoop, fanFilter, fanGain);

      // Pitidos de servidor periódicos
      function serverBeep() {
        if (!_ambientNodes || !_ambientNodes.length) return;
        const now = ctx.currentTime;
        const g = ctx.createGain();
        g.gain.setValueAtTime(0, now);
        g.gain.linearRampToValueAtTime(0.04, now + 0.01);
        g.gain.exponentialRampToValueAtTime(0.0001, now + 0.18);
        g.connect(dest);
        const o = ctx.createOscillator();
        o.type = 'square';
        o.frequency.value = 880 + Math.random() * 440;
        o.connect(g);
        o.start(now);
        o.stop(now + 0.2);

        const nextIn = 3000 + Math.random() * 5000;
        setTimeout(serverBeep, nextIn);
      }
      setTimeout(serverBeep, 2000);

      // Zumbido de ventiladores (tono grave)
      const fanHum = ctx.createOscillator();
      fanHum.type = 'sawtooth';
      fanHum.frequency.value = 60;
      const fanHumGain = ctx.createGain();
      fanHumGain.gain.value = 0.06;
      fanHum.connect(fanHumGain);
      fanHumGain.connect(dest);
      fanHum.start();
      nodes.push(fanHum, fanHumGain);

      return nodes;
    },

    /* ── Cap. 4 — Guerra Digital: ambiente de crisis ── */
    warZone(dest) {
      const nodes = [];

      // Alarma de fondo grave
      const alarmOsc = ctx.createOscillator();
      alarmOsc.type = 'sawtooth';
      alarmOsc.frequency.value = 110;

      const lfo = ctx.createOscillator();
      lfo.type = 'square';
      lfo.frequency.value = 0.8;
      const lfoG = ctx.createGain();
      lfoG.gain.value = 40;
      lfo.connect(lfoG);
      lfoG.connect(alarmOsc.frequency);

      const alarmFilter = ctx.createBiquadFilter();
      alarmFilter.type = 'lowpass';
      alarmFilter.frequency.value = 500;
      const alarmGain = ctx.createGain();
      alarmGain.gain.value = 0.15;

      alarmOsc.connect(alarmFilter);
      alarmFilter.connect(alarmGain);
      alarmGain.connect(dest);
      alarmOsc.start();
      lfo.start();
      nodes.push(alarmOsc, lfo, lfoG, alarmFilter, alarmGain);

      // Ruido de fondo (estático)
      const buf = noiseBuffer(2);
      const noiseLoop = ctx.createBufferSource();
      noiseLoop.buffer = buf;
      noiseLoop.loop = true;
      const noiseFilter = ctx.createBiquadFilter();
      noiseFilter.type = 'highpass';
      noiseFilter.frequency.value = 2000;
      const noiseGain = ctx.createGain();
      noiseGain.gain.value = 0.12;
      noiseLoop.connect(noiseFilter);
      noiseFilter.connect(noiseGain);
      noiseGain.connect(dest);
      noiseLoop.start();
      nodes.push(noiseLoop, noiseFilter, noiseGain);

      return nodes;
    },

    /* ── Cap. 5 — Diálogo con NEXUS: ambiente alienígena ── */
    nexusPresence(dest) {
      const nodes = [];

      // Tono místico de IA
      [55, 82.4, 110].forEach(freq => {
        const o = ctx.createOscillator();
        o.type = 'sine';
        o.frequency.value = freq;

        const lfo = ctx.createOscillator();
        lfo.type = 'sine';
        lfo.frequency.value = 0.07 + Math.random() * 0.05;
        const lfoG = ctx.createGain();
        lfoG.gain.value = freq * 0.01;
        lfo.connect(lfoG);
        lfoG.connect(o.frequency);

        const g = ctx.createGain();
        g.gain.value = 0.12 + Math.random() * 0.06;
        o.connect(g);
        g.connect(dest);
        o.start();
        lfo.start();
        nodes.push(o, lfo, lfoG, g);
      });

      // Capa de ruido suave (espacio digital)
      const buf = brownNoiseBuffer(4);
      const noiseLoop = ctx.createBufferSource();
      noiseLoop.buffer = buf;
      noiseLoop.loop = true;
      const filter = ctx.createBiquadFilter();
      filter.type = 'lowpass';
      filter.frequency.value = 120;
      const g = ctx.createGain();
      g.gain.value = 0.25;
      noiseLoop.connect(filter);
      filter.connect(g);
      g.connect(dest);
      noiseLoop.start();
      nodes.push(noiseLoop, filter, g);

      return nodes;
    },

    /* ── Cap. 7 — Final: silencio tenso ── */
    finalTension(dest) {
      const nodes = [];

      // Latido electrónico (heartbeat)
      function beat() {
        if (!_ambientNodes || !_ambientNodes.length) return;
        const now = ctx.currentTime;
        [0, 0.12].forEach((offset) => {
          const g = ctx.createGain();
          g.gain.setValueAtTime(0, now + offset);
          g.gain.linearRampToValueAtTime(0.35, now + offset + 0.02);
          g.gain.exponentialRampToValueAtTime(0.0001, now + offset + 0.18);
          g.connect(dest);

          const buf = brownNoiseBuffer(0.2);
          const bs = ctx.createBufferSource();
          bs.buffer = buf;
          const filter = ctx.createBiquadFilter();
          filter.type = 'lowpass';
          filter.frequency.value = 200;
          bs.connect(filter);
          filter.connect(g);
          bs.start(now + offset);
          bs.stop(now + offset + 0.22);
        });
        setTimeout(beat, 800 + Math.random() * 200);
      }
      setTimeout(beat, 500);

      // Tono tenso sostenido
      const o = ctx.createOscillator();
      o.type = 'triangle';
      o.frequency.value = 220;
      const g = ctx.createGain();
      g.gain.value = 0.08;
      o.connect(g);
      g.connect(dest);
      o.start();
      nodes.push(o, g);

      return nodes;
    },

  }; // fin AMBIENTS

  /* ================================================================
     GESTOR DE AMBIENTES (solo uno activo a la vez)
     ================================================================ */
  let _currentAmbient = null;
  let _ambientNodes = [];

  function startAmbient(name, vol) {
    if (!ctx) ensureCtx();
    if (_muted) return;
    stopAmbient();

    ambientGain = ctx.createGain();
    ambientGain.gain.value = 0;
    ambientGain.connect(masterGain);

    const creator = AMBIENTS[name];
    if (!creator) return;

    _ambientNodes = creator(ambientGain);
    _currentAmbient = name;

    // Fade in
    const targetVol = vol !== undefined ? vol : VOL.ambient;
    ambientGain.gain.setValueAtTime(0, ctx.currentTime);
    ambientGain.gain.linearRampToValueAtTime(targetVol, ctx.currentTime + 2.0);
  }

  function stopAmbient(fadeTime = 1.5) {
    if (!ambientGain) return;

    const g = ambientGain;
    g.gain.setValueAtTime(g.gain.value, ctx.currentTime);
    g.gain.linearRampToValueAtTime(0, ctx.currentTime + fadeTime);

    setTimeout(() => {
      _ambientNodes.forEach(n => {
        try { if (n.stop) n.stop(); } catch(e) {}
        try { n.disconnect(); } catch(e) {}
      });
      _ambientNodes = [];
      g.disconnect();
    }, (fadeTime + 0.2) * 1000);

    _currentAmbient = null;
    ambientGain = null;
  }

  function crossfadeAmbient(name, vol) {
    if (_currentAmbient === name) return;
    stopAmbient(1.2);
    setTimeout(() => startAmbient(name, vol), 1300);
  }

  /* ================================================================
     API PÚBLICA
     ================================================================ */
  function play(sfxName) {
    if (!_initialized) init();
    if (!ctx) return;
    if (_muted) return;
    ensureCtx();
    if (SFX[sfxName]) {
      try { SFX[sfxName](); } catch(e) { console.warn('[SoundEngine] Error en SFX:', sfxName, e); }
    } else {
      console.warn('[SoundEngine] Efecto no encontrado:', sfxName);
    }
  }

  function mute() {
    _muted = true;
    if (masterGain) masterGain.gain.value = 0;
    stopAmbient(0.3);
  }

  function unmute() {
    _muted = false;
    if (masterGain) masterGain.gain.value = VOL.master;
  }

  function toggle() {
    if (_muted) unmute(); else mute();
    return !_muted;
  }

  function isMuted() { return _muted; }

  // Activar con primer gesto
  function onUserGesture() {
    init();
    if (ctx && ctx.state === 'suspended') ctx.resume();
  }

  return {
    init,
    onUserGesture,
    play,
    startAmbient,
    stopAmbient,
    crossfadeAmbient,
    mute,
    unmute,
    toggle,
    isMuted,
    VOL,
  };
})();
