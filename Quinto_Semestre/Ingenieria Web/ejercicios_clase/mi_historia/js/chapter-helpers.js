/**
 * chapter-helpers.js — El Último Commit
 * Utilidades compartidas para las páginas HTML de capítulos.
 */

/* ─── Countdown HUD ──────────────────────────────────────────── */
function updateCountdown() {
  const el = document.getElementById('hud-countdown');
  if (el && typeof StoryState !== 'undefined') {
    el.textContent = '⏱ ' + StoryState.getContadorNexus();
  }
}

/* ─── Escape HTML ────────────────────────────────────────────── */
function escapeHTML(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}

/**
 * showBranch(prefix, savedValue, allValues)
 * Muestra solo la sección con id="${prefix}-${savedValue}",
 * oculta todas las demás.
 *
 * @param {string}   prefix     - Prefijo del id, ej: "rama-ch2"
 * @param {string}   savedValue - Valor guardado en StoryState
 * @param {string[]} allValues  - Todos los posibles valores
 */
function showBranch(prefix, savedValue, allValues) {
  allValues.forEach(val => {
    const el = document.getElementById(prefix + '-' + val);
    if (el) el.hidden = (val !== savedValue);
  });
}

/**
 * initChallenge(cfg)
 * Monta un desafío de código interactivo en la página de capítulo.
 *
 * cfg = {
 *   containerId : 'challenge-card',   // id del contenedor padre
 *   code        : '...',              // código con § como hueco
 *   blank       : '§',               // marcador del hueco (default '§')
 *   answers     : ['includes', ...], // respuestas válidas
 *   hints       : ['...', '...'],    // array de hasta 3 pistas
 *   onSuccess   : fn                 // callback opcional al resolver
 * }
 */
function initChallenge(cfg) {
  const container = document.getElementById(cfg.containerId);
  if (!container) return;

  const blank   = cfg.blank || '§';
  const answers = (cfg.answers || []).map(a =>
    a.replace(/[();]/g, '').trim().toLowerCase()
  );
  let hintIdx = 0;

  /* ── Renderizar código con el hueco resaltado ── */
  const pre = container.querySelector('.challenge-code-pre');
  if (pre) {
    const safe = escapeHTML(cfg.code).replace(
      escapeHTML(blank),
      '<span class="code-blank" id="code-blank-display">____</span>'
    );
    pre.innerHTML = safe;
  }

  /* ── Referencias a elementos ── */
  const input     = container.querySelector('.challenge-input');
  const submitBtn = container.querySelector('.challenge-submit-btn');
  const feedback  = container.querySelector('.challenge-feedback');
  const successEl = container.querySelector('.challenge-success');
  const blankEl   = () => container.querySelector('.code-blank');

  /* ── Función de validación ── */
  function validate() {
    if (!input) return;
    const val = input.value.replace(/[();]/g, '').trim().toLowerCase();
    if (answers.includes(val)) {
      if (feedback) {
        feedback.textContent = '✅ ¡Correcto! La respuesta es: ' + input.value.trim();
        feedback.className   = 'challenge-feedback success';
      }
      /* Actualizar el hueco con la respuesta correcta */
      const b = blankEl();
      if (b) {
        b.textContent = input.value.trim();
        b.classList.add('solved');
      }
      if (successEl) successEl.hidden = false;
      if (submitBtn) submitBtn.disabled = true;
      if (input) input.disabled = true;
      /* Marcar capítulo como visitado */
      if (typeof StoryState !== 'undefined' && cfg.chapter) {
        StoryState.marcarCapitulo(cfg.chapter);
      }
      if (typeof cfg.onSuccess === 'function') cfg.onSuccess();
    } else {
      if (feedback) {
        feedback.textContent = '❌ Incorrecto. Intenta de nuevo.';
        feedback.className   = 'challenge-feedback error';
      }
      if (input) {
        input.classList.add('shake');
        setTimeout(() => input.classList.remove('shake'), 500);
      }
    }
  }

  if (submitBtn) submitBtn.addEventListener('click', validate);
  if (input) input.addEventListener('keydown', e => {
    if (e.key === 'Enter') validate();
  });

  /* ── Botón "Pedir pista" ── */
  const hintBtn  = container.querySelector('.hint-btn');
  const hintList = container.querySelector('.hints-list');
  if (hintBtn && hintList && cfg.hints) {
    hintBtn.addEventListener('click', () => {
      if (hintIdx < cfg.hints.length) {
        const p = document.createElement('p');
        p.className   = 'hint-item';
        p.textContent = cfg.hints[hintIdx++];
        hintList.appendChild(p);
      }
      if (hintIdx >= cfg.hints.length) hintBtn.disabled = true;
    });
  }
}

/**
 * initDecision(cfg)
 * Inicializa un panel de decisión en una página de capítulo.
 *
 * cfg = {
 *   containerId : 'decision-section',
 *   stateKey    : 'branch_ch2',      // clave en StoryState
 *   choices     : [
 *     { id: 'orden_silicio', resultText: 'Elegiste investigar...' },
 *     ...
 *   ],
 *   onChoice    : fn(choiceId)       // callback opcional
 * }
 */
function initDecision(cfg) {
  const container = document.getElementById(cfg.containerId);
  if (!container) return;

  const state = typeof StoryState !== 'undefined' ? StoryState.get() : {};
  const saved = state[cfg.stateKey] || null;

  /* Si ya hay elección guardada, mostrarla */
  if (saved) {
    _applyDecisionUI(container, cfg, saved);
  }

  cfg.choices.forEach(choice => {
    const btn = container.querySelector(`[data-choice="${choice.id}"]`);
    if (!btn) return;

    btn.addEventListener('click', () => {
      /* Guardar en localStorage */
      if (typeof StoryState !== 'undefined') {
        const upd = {};
        upd[cfg.stateKey] = choice.id;
        StoryState.update(upd);
      }
      _applyDecisionUI(container, cfg, choice.id);
      if (typeof cfg.onChoice === 'function') cfg.onChoice(choice.id);
    });
  });
}

function _applyDecisionUI(container, cfg, choiceId) {
  /* Marcar botón elegido */
  container.querySelectorAll('[data-choice]').forEach(b => {
    b.classList.toggle('chosen', b.dataset.choice === choiceId);
  });
  /* Mostrar texto de resultado */
  const resultEl = container.querySelector('.resultado-texto');
  if (resultEl) {
    const choice = cfg.choices.find(c => c.id === choiceId);
    if (choice && choice.resultText) {
      resultEl.textContent = choice.resultText;
    }
  }
}

/* ─── Init automático del countdown ─────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
  if (typeof StoryState !== 'undefined') {
    updateCountdown();
    setInterval(updateCountdown, 1000);
  }
});
