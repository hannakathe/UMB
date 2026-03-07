/**
 * fx.js — El Último Commit
 * Efectos visuales: matrix rain, typewriter, countdown timer, transiciones.
 */

/* ============================================================
   EFECTO MATRIX RAIN
   Lluvia de caracteres estilo Matrix en un canvas de fondo
   ============================================================ */

/**
 * Inicializa y ejecuta el efecto matrix rain en un canvas.
 * @param {string} canvasId - ID del elemento <canvas>
 */
function initMatrixRain(canvasId = "matrix-canvas") {
  const canvas = document.getElementById(canvasId);
  if (!canvas) return;

  const ctx    = canvas.getContext("2d");
  const chars  = "アイウエオカキクケコサシスセソタチツテトナニヌネノ01001010NEXUS█▓░╔╗╚╝║═";
  const fontSize = 14;

  let columns, drops;

  // Ajusta el canvas al tamaño de la ventana
  function resize() {
    canvas.width  = window.innerWidth;
    canvas.height = window.innerHeight;
    columns       = Math.floor(canvas.width / fontSize);
    drops         = new Array(columns).fill(1);
  }

  function draw() {
    // Fondo semitransparente para efecto de rastro
    ctx.fillStyle = "rgba(10, 13, 18, 0.05)";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    ctx.fillStyle = "#00d4ff";
    ctx.font      = `${fontSize}px Courier New`;

    drops.forEach((y, i) => {
      // Caracter aleatorio
      const char = chars[Math.floor(Math.random() * chars.length)];
      ctx.fillText(char, i * fontSize, y * fontSize);

      // Reinicia la columna cuando llega al fondo (con probabilidad)
      if (y * fontSize > canvas.height && Math.random() > 0.975) {
        drops[i] = 0;
      }
      drops[i]++;
    });
  }

  resize();
  window.addEventListener("resize", resize);

  // Ejecuta el efecto a 20fps para ser eficiente
  setInterval(draw, 50);
}


/* ============================================================
   EFECTO TYPEWRITER
   Escribe texto caracter por caracter como una terminal
   ============================================================ */

/**
 * Escribe texto en un elemento HTML caracter por caracter.
 * @param {HTMLElement} element   - Elemento donde escribir
 * @param {string}      text      - Texto a escribir
 * @param {number}      speed     - Milisegundos entre caracteres (default: 35)
 * @param {Function}    callback  - Función a llamar cuando termine
 */
function typeWriter(element, text, speed = 35, callback = null) {
  if (!element) return;

  element.textContent = "";
  let index = 0;

  function escribirCaracter() {
    if (index < text.length) {
      element.textContent += text.charAt(index);
      index++;
      setTimeout(escribirCaracter, speed);
    } else {
      // Terminó de escribir
      if (typeof callback === "function") callback();
    }
  }

  escribirCaracter();
}

/**
 * Escribe múltiples líneas en una terminal, una por una.
 * @param {HTMLElement} container  - Contenedor de la terminal (terminal-body)
 * @param {Array}       lineas     - Array de objetos { texto, tipo, delay }
 *                                   tipo: "command"|"output"|"error"|"nexus"|""
 *                                   delay: ms de espera antes de esta línea
 */
function typeTerminal(container, lineas) {
  if (!container || !lineas.length) return;

  let indexLinea = 0;

  function escribirLinea() {
    if (indexLinea >= lineas.length) return;

    const { texto, tipo = "output", delay = 0 } = lineas[indexLinea];
    indexLinea++;

    setTimeout(() => {
      // Crear elemento de línea
      const lineEl = document.createElement("div");
      lineEl.classList.add("terminal-line");

      if (tipo === "command") {
        // Línea con prompt
        lineEl.innerHTML = `
          <span class="terminal-prompt">alex@nexus:~$</span>
          <span class="terminal-command"></span>
        `;
        container.appendChild(lineEl);
        const cmdEl = lineEl.querySelector(".terminal-command");
        typeWriter(cmdEl, texto, 50, escribirLinea);
      } else {
        // Línea de output
        lineEl.innerHTML = `<span class="terminal-output ${tipo}"></span>`;
        container.appendChild(lineEl);
        const outEl = lineEl.querySelector(".terminal-output");
        typeWriter(outEl, texto, 15, escribirLinea);
      }

      // Scroll automático al fondo
      container.scrollTop = container.scrollHeight;
    }, delay);
  }

  escribirLinea();
}


/* ============================================================
   COUNTDOWN TIMER
   Actualiza el contador regresivo del sistema NEXUS
   ============================================================ */

/**
 * Inicia el contador regresivo de NEXUS y lo muestra en el elemento dado.
 * @param {string} elementId - ID del elemento donde mostrar el tiempo
 */
function initCountdown(elementId = "countdown") {
  const el = document.getElementById(elementId);
  if (!el) return;

  function actualizar() {
    const tiempo = StoryState.getContadorNexus();
    el.textContent = `⏱ ${tiempo}`;

    // Cambio de color cuando queda poco tiempo
    const [h] = tiempo.split(":").map(Number);
    if (h < 12) {
      el.style.color = "var(--color-danger)";
      el.style.animation = "neon-pulse 0.8s ease-in-out infinite";
    } else if (h < 24) {
      el.style.color = "var(--color-warning)";
    }
  }

  actualizar();
  setInterval(actualizar, 1000);
}


/* ============================================================
   TRANSICIONES DE PÁGINA
   Animación de entrada al cargar un capítulo nuevo
   ============================================================ */

/**
 * Anima la entrada de los elementos principales de un capítulo.
 * Añade la clase .fade-in-up a elementos con data-animate.
 */
function animatePageEntrance() {
  const elementos = document.querySelectorAll("[data-animate]");

  elementos.forEach((el, i) => {
    el.style.opacity    = "0";
    el.style.transform  = "translateY(20px)";

    setTimeout(() => {
      el.style.transition = "opacity 0.5s ease, transform 0.5s ease";
      el.style.opacity    = "1";
      el.style.transform  = "translateY(0)";
    }, i * 120);
  });
}


/* ============================================================
   REVEAL SECTIONS
   Mostrar/ocultar secciones de texto con botones
   ============================================================ */

/**
 * Inicializa todos los botones de reveal/hide del capítulo.
 * Busca botones con clase .btn-reveal y controla la sección target.
 */
function initRevealButtons() {
  const botones = document.querySelectorAll(".btn-reveal");

  botones.forEach(btn => {
    const targetId = btn.getAttribute("data-target");
    const target   = document.getElementById(targetId);
    if (!target) return;

    // Estado inicial: oculto
    target.classList.add("hidden");

    btn.addEventListener("click", () => {
      const estaOculto = target.classList.contains("hidden");

      if (estaOculto) {
        target.classList.remove("hidden");
        target.classList.add("visible");
        btn.classList.add("active");
        btn.textContent = btn.getAttribute("data-text-hide") || "Ocultar";
      } else {
        target.classList.remove("visible");
        target.classList.add("hidden");
        btn.classList.remove("active");
        btn.textContent = btn.getAttribute("data-text-show") || "Mostrar";
      }
    });
  });
}


/* ============================================================
   AUDIO MANAGER
   Control de reproducción de audio del sistema
   ============================================================ */

const AudioManager = {
  _instancias: {},

  /**
   * Registra un elemento de audio con un nombre.
   * @param {string} nombre   - Identificador del audio
   * @param {string} src      - Ruta del archivo
   * @param {boolean} loop    - Si debe repetirse
   */
  registrar(nombre, src, loop = false) {
    const audio = new Audio(src);
    audio.loop  = loop;
    this._instancias[nombre] = audio;
  },

  /**
   * Reproduce un audio registrado.
   * @param {string} nombre
   * @param {number} volumen - 0 a 1
   */
  play(nombre, volumen = 0.5) {
    const audio = this._instancias[nombre];
    if (!audio) return;
    audio.volume = volumen;
    // Vuelve al inicio si ya estaba reproduciéndose
    audio.currentTime = 0;
    audio.play().catch(e => console.warn("Audio no pudo reproducirse:", e));
  },

  /**
   * Pausa un audio.
   * @param {string} nombre
   */
  pause(nombre) {
    const audio = this._instancias[nombre];
    if (audio) audio.pause();
  },

  /**
   * Pausa todos los audios.
   */
  pauseAll() {
    Object.values(this._instancias).forEach(a => a.pause());
  },
};


/* ============================================================
   INICIALIZACIÓN AUTOMÁTICA
   Se ejecuta cuando el DOM está listo
   ============================================================ */
document.addEventListener("DOMContentLoaded", () => {
  // Anima los elementos de entrada
  animatePageEntrance();

  // Inicia el countdown si hay un elemento con id="countdown"
  initCountdown("countdown");

  // Inicia botones de reveal
  initRevealButtons();

  // Registra audios si existen los archivos
  AudioManager.registrar("alarma",  "../assets/media/alarm.mp3",   false);
  AudioManager.registrar("typing",  "../assets/media/typing.mp3",  true);
});
