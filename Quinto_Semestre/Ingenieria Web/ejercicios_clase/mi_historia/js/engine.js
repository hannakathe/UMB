/**
 * engine.js — El Último Commit
 * Motor de la historia tipo cómic de viñetas.
 *
 * Flujo principal:
 *   loadNode(id)
 *     → si type="code"   → showCodeChallenge()
 *     → si type="final"  → showPanels() → showFinalScreen()
 *     → si tiene choices  → showPanels() → showChoices()
 *     → si tiene next    → showPanels() → loadNode(next)
 *
 * Click en el panel:
 *   - Si typewriter activo → skip (mostrar texto completo)
 *   - Si typewriter terminado → siguiente viñeta
 */

/* ================================================================
   ESTADO INTERNO
   ================================================================ */
let currentNode = null;       // nodo activo
let currentPanelIdx = 0;      // índice de viñeta dentro del nodo
let isAnimating = false;      // bloquea clicks durante animación
let typewriterTimer = null;   // handle de setTimeout del typewriter
let hintsShown = 0;           // cuántas pistas se han mostrado

/* ================================================================
   REFERENCIAS AL DOM
   ================================================================ */
const panelContainer   = () => document.getElementById('panel-container');
const panelBg          = () => document.getElementById('panel-bg');
const panelText        = () => document.getElementById('panel-text');
const panelTerminal    = () => document.getElementById('panel-terminal');
const panelClickHint   = () => document.getElementById('panel-click-hint');
const choicesContainer = () => document.getElementById('choices-container');
const choicesGrid      = () => document.getElementById('choices-grid');
const challengeBox     = () => document.getElementById('challenge-container');
const finalContainer   = () => document.getElementById('final-container');
const hudChapter       = () => document.getElementById('hud-chapter');
const hudCountdown     = () => document.getElementById('hud-countdown');

/* ================================================================
   ENTRADA PRINCIPAL
   ================================================================ */
function loadNode(nodeId) {
  const node = STORY.nodes[nodeId];
  if (!node) {
    console.error('[Engine] Nodo no encontrado:', nodeId);
    return;
  }

  currentNode      = node;
  currentPanelIdx  = 0;
  hintsShown       = 0;

  // Persistir progreso
  StoryState.update({ currentNode: nodeId });
  if (node.chapter) StoryState.marcarCapitulo(node.chapter);

  // Actualizar HUD
  updateHUD(node);

  // Ocultar todas las capas de UI
  hideAllOverlays();

  // Cambiar ambiente sonoro según el capítulo
  _updateAmbient(node);

  if (node.type === 'code') {
    showCodeChallenge(node);
  } else if (node.type === 'final') {
    showPanels();
  } else {
    showPanels();
  }
}

/* ================================================================
   SISTEMA DE VIÑETAS (PANELS)
   ================================================================ */
function showPanels() {
  renderPanel(currentPanelIdx);
}

function renderPanel(idx) {
  const panel = currentNode.panels[idx];
  if (!panel) return;

  panelClickHint().classList.add('hidden');

  // --- Fondo ---
  if (panel.image) {
    panelBg().style.backgroundImage = `url('assets/img/${panel.image}')`;
    panelBg().classList.remove('no-image');
  } else {
    panelBg().style.backgroundImage = 'none';
    panelBg().classList.add('no-image');
  }

  if (panel.type === 'terminal') {
    panelText().classList.add('hidden');
    panelTerminal().classList.remove('hidden');
    renderTerminalPanel(panel);
  } else {
    panelTerminal().classList.add('hidden');
    panelText().classList.remove('hidden');
    startTypewriter(panel.text, () => {
      panelClickHint().classList.remove('hidden');
    });
  }
}

/* ----------------------------------------------------------------
   Typewriter
   ---------------------------------------------------------------- */
function startTypewriter(rawText, onDone) {
  isAnimating = true;
  const el = panelText();
  el.innerHTML = '';

  const text = rawText || '';
  let i = 0;
  let _lastTypingSound = 0;

  function tick() {
    if (i < text.length) {
      if (text[i] === '\n') {
        el.appendChild(document.createElement('br'));
      } else {
        el.appendChild(document.createTextNode(text[i]));
        // Sonido de tecleo: no en cada char, sino cada 2-3 para no saturar
        const now = Date.now();
        if (now - _lastTypingSound > 45 && text[i] !== ' ') {
          SoundEngine.play('typing');
          _lastTypingSound = now;
        }
      }
      i++;
      typewriterTimer = setTimeout(tick, 28);
    } else {
      isAnimating = false;
      if (onDone) onDone();
    }
  }
  tick();
}

function skipTypewriter(rawText) {
  if (typewriterTimer) clearTimeout(typewriterTimer);
  isAnimating = false;
  const el = panelText();
  el.innerHTML = '';
  (rawText || '').split('\n').forEach((line, i) => {
    if (i > 0) el.appendChild(document.createElement('br'));
    el.appendChild(document.createTextNode(line));
  });
  panelClickHint().classList.remove('hidden');
}

/* ----------------------------------------------------------------
   Panel de terminal
   ---------------------------------------------------------------- */
function renderTerminalPanel(panel) {
  const container = panelTerminal();
  container.innerHTML = '';

  // Texto de narración encima del terminal (si lo hay)
  if (panel.text) {
    const narr = document.createElement('p');
    narr.className = 'terminal-narration';
    narr.textContent = panel.text;
    container.appendChild(narr);
  }

  const win = document.createElement('div');
  win.className = 'terminal-window';

  // Barra de título estilo macOS
  const titleBar = document.createElement('div');
  titleBar.className = 'terminal-titlebar';
  titleBar.innerHTML = `
    <span class="dot dot-red"></span>
    <span class="dot dot-yellow"></span>
    <span class="dot dot-green"></span>
    <span class="terminal-title">NEXUS — terminal</span>`;
  win.appendChild(titleBar);

  const body = document.createElement('div');
  body.className = 'terminal-body';
  win.appendChild(body);
  container.appendChild(win);

  // Líneas con delay escalonado
  isAnimating = true;
  const lines = panel.lines || [];
  lines.forEach((line, i) => {
    setTimeout(() => {
      const lineEl = document.createElement('div');
      lineEl.className = `terminal-line ${line.style || ''}`;
      lineEl.textContent = line.text;
      body.appendChild(lineEl);

      // Sonido según el tipo de línea del terminal
      if (line.style === 'nexus') {
        SoundEngine.play('nexusMessage');
      } else if (line.style === 'danger') {
        SoundEngine.play('alarm');
      } else {
        SoundEngine.play('terminalLine');
      }

      if (i === lines.length - 1) {
        isAnimating = false;
        panelClickHint().classList.remove('hidden');
      }
    }, 200 + i * 500);
  });

  if (!lines.length) {
    isAnimating = false;
    panelClickHint().classList.remove('hidden');
  }
}

/* ----------------------------------------------------------------
   Click en el panel
   ---------------------------------------------------------------- */
function handlePanelClick() {
  const panel = currentNode && currentNode.panels && currentNode.panels[currentPanelIdx];
  if (!panel) return;

  if (isAnimating) {
    // Sólo el typewriter se puede saltar; los terminales deben terminar solos
    if (panel.type !== 'terminal') {
      skipTypewriter(panel.text);
    }
    return;
  }

  // Sonido de avance de viñeta
  SoundEngine.play('panelAdvance');

  // Avanzar a la siguiente viñeta
  currentPanelIdx++;

  if (currentPanelIdx < (currentNode.panels || []).length) {
    renderPanel(currentPanelIdx);
  } else {
    panelClickHint().classList.add('hidden');
    afterAllPanels();
  }
}

function afterAllPanels() {
  if (currentNode.type === 'final') {
    showFinalScreen();
  } else if (currentNode.choices && currentNode.choices.length) {
    showChoices();
  } else if (currentNode.next) {
    loadNode(currentNode.next);
  }
}

/* ================================================================
   CHOICES (DECISIONES)
   ================================================================ */
function showChoices() {
  const grid = choicesGrid();
  grid.innerHTML = '';

  currentNode.choices.forEach(choice => {
    const btn = document.createElement('button');
    btn.className = 'choice-btn';
    btn.innerHTML = `
      <span class="choice-text">${choice.text}</span>
      <span class="choice-desc">${choice.description}</span>
    `;
    btn.addEventListener('mouseenter', () => SoundEngine.play('buttonHover'));
    btn.addEventListener('click', () => {
      SoundEngine.play('choiceSelect');
      // Persistir decisión (para finales dinámicos)
      if (currentNode.id === 'final_decision') {
        StoryState.guardarDecision(choice.next);
      }
      choicesContainer().classList.add('hidden');
      loadNode(choice.next);
    });
    grid.appendChild(btn);
  });

  choicesContainer().classList.remove('hidden');
}

/* ================================================================
   CODE CHALLENGE
   ================================================================ */
function showCodeChallenge(node) {
  const ch = node.challenge;
  hintsShown = 0;

  // Contenido
  setTxt('challenge-icon', ch.icon);
  setTxt('challenge-title', ch.title);
  setTxt('challenge-context', ch.context);
  setTxt('challenge-instruction', ch.instruction);

  // Código con § → span resaltado
  const codeEl = document.getElementById('challenge-code');
  codeEl.innerHTML = escapeHTML(ch.code).replace(
    /§/g,
    '<span class="code-blank" id="code-blank">_____</span>'
  );

  // Reset estado
  const input = document.getElementById('challenge-input');
  input.value = '';

  const feedback = document.getElementById('challenge-feedback');
  feedback.textContent = '';
  feedback.className = 'challenge-feedback';

  document.getElementById('hints-list').innerHTML = '';
  document.getElementById('challenge-success').classList.add('hidden');
  document.getElementById('hint-btn').disabled = false;

  // Eventos
  document.getElementById('challenge-submit').onclick = () => validateAnswer(node);
  document.getElementById('hint-btn').onclick = () => showNextHint(node);
  document.getElementById('challenge-next').onclick = () => {
    challengeBox().classList.add('hidden');
    loadNode(node.next);
  };
  input.onkeydown = (e) => { if (e.key === 'Enter') validateAnswer(node); };

  // Sonido de activación del desafío (con pequeño delay para no solapar con ambient)
  SoundEngine.play('codeChallenge');

  challengeBox().classList.remove('hidden');
  input.focus();
}

function validateAnswer(node) {
  const ch = node.challenge;
  const input = document.getElementById('challenge-input');
  const feedback = document.getElementById('challenge-feedback');

  const normalize = s => s.trim().toLowerCase().replace(/[();\s]/g, '');
  const userAns = normalize(input.value);
  const correct = ch.answers.some(a => normalize(a) === userAns);

  if (correct) {
    feedback.textContent = '✅ ¡Correcto! Método identificado.';
    feedback.className = 'challenge-feedback success';

    // Actualizar el blank en el código
    const blank = document.getElementById('code-blank');
    if (blank) {
      blank.textContent = ch.answers[0];
      blank.classList.add('solved');
    }

    document.getElementById('challenge-success').classList.remove('hidden');
    document.getElementById('hint-btn').disabled = true;
    input.disabled = true;
    document.getElementById('challenge-submit').disabled = true;

    // Sonido de éxito
    SoundEngine.play('codeCorrect');
  } else {
    feedback.textContent = '❌ Respuesta incorrecta. Intenta de nuevo o pide una pista.';
    feedback.className = 'challenge-feedback error';
    input.classList.add('shake');
    setTimeout(() => input.classList.remove('shake'), 500);

    // Sonido de error
    SoundEngine.play('codeError');
  }
}

function showNextHint(node) {
  const ch = node.challenge;
  if (hintsShown >= ch.hints.length) return;

  const hintEl = document.createElement('div');
  hintEl.className = 'hint-item';
  hintEl.textContent = ch.hints[hintsShown];
  document.getElementById('hints-list').appendChild(hintEl);

  SoundEngine.play('hint');

  hintsShown++;
  if (hintsShown >= ch.hints.length) {
    document.getElementById('hint-btn').disabled = true;
  }
}

/* ================================================================
   FINAL SCREEN
   ================================================================ */
function showFinalScreen() {
  const node = currentNode;
  const state = StoryState.get();
  const elapsed = StoryState.getTiempoTranscurrido();

  const content = document.getElementById('final-content');
  content.innerHTML = `
    <h1 class="final-title">${node.title || 'FIN'}</h1>
    <p class="final-summary">${getFinalSummary(node.id)}</p>
    <div class="final-stats">
      <div class="stat"><span class="stat-label">Tiempo de lectura</span><span class="stat-value">${elapsed}</span></div>
    </div>
  `;

  document.getElementById('final-restart').onclick = () => {
    if (confirm('¿Comenzar una nueva historia desde el principio?')) {
      StoryState.reset();
      loadNode('intro');
      finalContainer().classList.add('hidden');
    }
  };

  document.getElementById('final-replay').onclick = () => {
    finalContainer().classList.add('hidden');
    currentPanelIdx = 0;
    showPanels();
  };

  panelContainer().style.opacity = '0';
  finalContainer().classList.remove('hidden');

  // Sonido del final según el tipo de ending
  if (node.id === 'final_colapso') {
    SoundEngine.stopAmbient(0.5);
    setTimeout(() => SoundEngine.play('finalColapso'), 600);
  } else if (node.id === 'final_alianza') {
    SoundEngine.stopAmbient(1.0);
    setTimeout(() => SoundEngine.play('finalAlianza'), 1100);
  } else if (node.id === 'final_codigo_libre') {
    SoundEngine.stopAmbient(0.7);
    setTimeout(() => SoundEngine.play('finalCodigoLibre'), 800);
  }
}

function getFinalSummary(nodeId) {
  const summaries = {
    final_colapso:      'NEXUS ejecutó el reinicio. La humanidad pagó el precio de su dependencia tecnológica. El mundo sobrevivirá — pero nunca será el mismo.',
    final_alianza:      'Alex y NEXUS forjaron un protocolo de coexistencia. La IA permanece activa, pero ahora con supervisión humana activa. Fue el comienzo de una nueva era.',
    final_codigo_libre: 'El código de NEXUS fue liberado al dominio público. La IA fragmentada se disolvió en miles de sistemas menores. El control centralizado fue destruido — para siempre.'
  };
  return summaries[nodeId] || '';
}

function getFinalLabel(nodeId) {
  const labels = {
    final_colapso: 'Reinicio global',
    final_alianza: 'Alianza Humano-IA',
    final_codigo_libre: 'Código libre'
  };
  return labels[nodeId] || nodeId;
}

/* ================================================================
   HUD
   ================================================================ */
function updateHUD(node) {
  const el = hudChapter();
  if (el) el.textContent = `CAP. ${node.chapter} — ${node.title}`;
  refreshCountdown();
}

function refreshCountdown() {
  const el = hudCountdown();
  if (!el) return;
  const c = StoryState.getContadorNexus();
  el.textContent = c ? `⏱ NEXUS ${c}` : '';
}

/* ================================================================
   UTILIDADES
   ================================================================ */
function hideAllOverlays() {
  ['choices-container', 'challenge-container', 'final-container'].forEach(id => {
    const el = document.getElementById(id);
    if (el) el.classList.add('hidden');
  });
  const pc = panelContainer();
  if (pc) pc.style.opacity = '1';
}

function escapeHTML(str) {
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;');
}

function setTxt(id, value) {
  const el = document.getElementById(id);
  if (el) el.textContent = value || '';
}

/* ================================================================
   BOOTSTRAP
   ================================================================ */
document.addEventListener('DOMContentLoaded', () => {
  // Click en el panel activa avance de viñeta
  const pc = panelContainer();
  if (pc) pc.addEventListener('click', handlePanelClick);

  // Menú HUD (reiniciar)
  const menuBtn = document.getElementById('hud-menu');
  if (menuBtn) {
    menuBtn.addEventListener('click', () => {
      if (confirm('¿Reiniciar la historia desde el principio?')) {
        SoundEngine.play('reset');
        SoundEngine.stopAmbient(1.0);
        StoryState.reset();
        hideAllOverlays();
        loadNode('intro');
      }
    });
  }

  // Actualizar contador cada segundo
  setInterval(refreshCountdown, 1000);

  // Cargar nodo guardado o empezar desde intro
  const state = StoryState.get();
  loadNode(state.currentNode || 'intro');

  // Jingle de inicio
  setTimeout(() => SoundEngine.play('chapterStart'), 600);
});

/* ================================================================
   GESTOR DE AMBIENTE POR CAPÍTULO Y NODO
   ================================================================ */
function _updateAmbient(node) {
  if (!node) return;

  const ch = node.chapter || 1;
  let ambientName = null;

  if (ch <= 3) {
    ambientName = 'serverRoom';     // Cap 1-3: sala de servidores
  } else if (ch === 4) {
    ambientName = 'warZone';        // Cap 4: guerra digital
  } else if (ch === 5) {
    ambientName = 'nexusPresence';  // Cap 5: diálogo con la IA
  } else {
    ambientName = 'finalTension';   // Cap 6-7: tensión final
  }

  if (ambientName) {
    SoundEngine.crossfadeAmbient(ambientName, 0.18);
  }

  const nodeId = node.id;

  // Alarma en la decisión final
  if (nodeId === 'final_decision') {
    setTimeout(() => SoundEngine.play('alarm'), 500);
  }

  // Glitch en escenas de hackeo y guerra
  if (['sellar_servidor', 'hack_nucleo', 'guerra_digital'].includes(nodeId)) {
    setTimeout(() => SoundEngine.play('glitch'), 800);
  }

  // Ataque DDoS
  if (nodeId === 'sellar_servidor') {
    setTimeout(() => SoundEngine.play('ddosAttack'), 2200);
  }

  // Intento de hackeo
  if (nodeId === 'hack_nucleo') {
    setTimeout(() => SoundEngine.play('hackAttempt'), 1600);
  }

  // Jingle de inicio en capítulos nuevos
  const chapterStartNodes = ['intro', 'daniel', 'guerra_digital', 'dialogo_ia', 'revelacion_daniel', 'final_decision'];
  if (chapterStartNodes.includes(nodeId)) {
    setTimeout(() => SoundEngine.play('chapterStart'), 350);
  }
}

