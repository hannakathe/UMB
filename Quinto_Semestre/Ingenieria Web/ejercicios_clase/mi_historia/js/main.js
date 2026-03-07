/**
 * main.js — El Último Commit
 * Punto de entrada principal: inicialización, navegación y lógica de capítulo 6.
 */

document.addEventListener("DOMContentLoaded", () => {

  /* ----------------------------------------------------------
     MARCAR CAPÍTULO ACTUAL
     Detecta el número de capítulo desde la URL y lo registra.
  ---------------------------------------------------------- */
  const ruta = window.location.pathname;
  const matchCapitulo = ruta.match(/ch(\d+)\.html/);
  if (matchCapitulo) {
    const numCapitulo = parseInt(matchCapitulo[1], 10);
    StoryState.marcarCapitulo(numCapitulo);
  }


  /* ----------------------------------------------------------
     CAPÍTULO 6 — Lógica de decisión interactiva
     Solo se ejecuta si estamos en ch6.html
  ---------------------------------------------------------- */
  if (ruta.includes("ch6.html")) {
    initDecisionPanel();
  }


  /* ----------------------------------------------------------
     CAPÍTULO 10 — Mostrar el final correcto
     Solo se ejecuta en ch10.html
  ---------------------------------------------------------- */
  if (ruta.includes("ch10.html")) {
    mostrarFinalCorrecto();
  }


  /* ----------------------------------------------------------
     BARRA DE PROGRESO DE CAPÍTULO
     Actualiza el indicador visual de progreso.
  ---------------------------------------------------------- */
  actualizarProgreso();


  /* ----------------------------------------------------------
     MATRIX RAIN en index.html (portada)
  ---------------------------------------------------------- */
  if (ruta.endsWith("index.html") || ruta.endsWith("/")) {
    initMatrixRain("matrix-canvas");
  }

});


/* ============================================================
   FUNCIÓN: initDecisionPanel
   Capítulo 6 — Botones de decisión con navegación a capítulos
   ============================================================ */
function initDecisionPanel() {
  // Referencias a los botones de decisión
  const btnHackear   = document.getElementById("btn-hackear");
  const btnConvencer = document.getElementById("btn-convencer");
  const btnLiberar   = document.getElementById("btn-liberar");
  const resultado    = document.getElementById("resultado-decision");
  const btnContinuar = document.getElementById("btn-continuar");

  // Si no están los botones en este capítulo, salir
  if (!btnHackear) return;

  // Textos de feedback por opción
  const mensajes = {
    hackear:   "⚔️  Ruta A activada. Objetivo: destruir el núcleo de NEXUS desde dentro. Riesgo: MÁXIMO.",
    convencer: "🤝  Ruta B activada. Objetivo: establecer comunicación con NEXUS. Requiere: lógica y evidencia.",
    liberar:   "🌐  Ruta C activada. Objetivo: publicar el código al mundo. Consecuencias: IMPREDECIBLES.",
  };

  // Función reutilizable para manejar cada decisión
  function manejarDecision(opcion) {
    // Guardar en localStorage
    StoryState.guardarDecision(opcion);

    // Mostrar feedback visual
    if (resultado) {
      resultado.textContent = mensajes[opcion];
      resultado.style.color = opcion === "hackear"
        ? "var(--color-danger)"
        : opcion === "convencer"
          ? "var(--color-primary)"
          : "var(--color-secondary)";
    }

    // Resaltar botón seleccionado
    [btnHackear, btnConvencer, btnLiberar].forEach(btn => {
      btn.classList.remove("opcion-seleccionada");
    });

    const btnSeleccionado = {
      hackear:   btnHackear,
      convencer: btnConvencer,
      liberar:   btnLiberar,
    }[opcion];

    if (btnSeleccionado) {
      btnSeleccionado.classList.add("opcion-seleccionada");
    }

    // Mostrar botón "Continuar" con la ruta correcta
    if (btnContinuar) {
      const rutaSiguiente = StoryState.getSiguienteCapitulo();
      btnContinuar.href        = rutaSiguiente;
      btnContinuar.textContent = `Continuar con Ruta ${opcion === "hackear" ? "A" : opcion === "convencer" ? "B" : "C"} →`;
      btnContinuar.classList.remove("hidden");
    }

    // Efecto de audio
    if (typeof AudioManager !== "undefined") {
      AudioManager.play("alarma", 0.3);
    }
  }

  // Asignar eventos a cada botón
  btnHackear.addEventListener("click",   () => manejarDecision("hackear"));
  btnConvencer.addEventListener("click", () => manejarDecision("convencer"));
  btnLiberar.addEventListener("click",   () => manejarDecision("liberar"));
}


/* ============================================================
   FUNCIÓN: mostrarFinalCorrecto
   Capítulo 10 — Resalta el final según la decisión guardada
   ============================================================ */
function mostrarFinalCorrecto() {
  const decision = StoryState.getDecision();

  // Mapeo decisión → final y color
  const mapaFinal = {
    hackear:   { id: "final-reset",   clase: "final-activo-rojo"   },
    convencer: { id: "final-alianza", clase: "final-activo-amarillo"},
    liberar:   { id: "final-libre",   clase: "final-activo-verde"  },
  };

  const config = mapaFinal[decision];
  if (!config) return;

  // Resaltar el final correcto
  const finalEl = document.getElementById(config.id);
  if (finalEl) {
    finalEl.classList.add(config.clase);
    finalEl.scrollIntoView({ behavior: "smooth", block: "center" });

    // Añadir indicador de "Tu final"
    const badge = document.createElement("div");
    badge.className   = "tu-final-badge";
    badge.textContent = "★ Tu final";
    finalEl.prepend(badge);
  }
}


/* ============================================================
   FUNCIÓN: actualizarProgreso
   Actualiza la barra de progreso y el indicador de capítulo
   ============================================================ */
function actualizarProgreso() {
  const estado   = StoryState.get();
  const progreso = document.getElementById("barra-progreso");
  const texto    = document.getElementById("progreso-texto");

  if (progreso) {
    const porcentaje = (estado.capituloActual / 10) * 100;
    progreso.style.width = `${porcentaje}%`;
  }

  if (texto) {
    const vistos = estado.capitulosVistos.length;
    texto.textContent = `${vistos}/10 capítulos`;
  }
}


/* ============================================================
   FUNCIÓN: resetearHistoria
   Reinicia el progreso y vuelve al inicio
   ============================================================ */
function resetearHistoria() {
  if (confirm("¿Reiniciar el progreso? Esta acción borrará tus decisiones.")) {
    StoryState.reset();
    window.location.href = "../index.html";
  }
}
