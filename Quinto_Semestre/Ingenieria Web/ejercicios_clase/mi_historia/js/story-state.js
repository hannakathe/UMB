/**
 * story-state.js — El Último Commit
 * Gestión del estado de la historia usando localStorage.
 * Guarda las decisiones del jugador para que persistan entre capítulos.
 */

const StoryState = {

  // Clave base para localStorage
  STORAGE_KEY: "nexus_story_state",

  /**
   * Estado inicial por defecto (cuando el jugador empieza de cero)
   */
  defaultState: {
    decision:         null,      // "hackear" | "convencer" | "liberar"
    capituloActual:   1,
    capitulosVistos:  [],
    tiempoInicio:     null,      // timestamp cuando inició
    tiempoRestante:   72 * 3600, // 72 horas en segundos
    endingDesbloqueado: null,    // "reset" | "alianza" | "libre"
  },

  /**
   * Lee el estado completo desde localStorage.
   * Si no existe, devuelve el estado por defecto.
   * @returns {Object} El estado de la historia
   */
  get() {
    try {
      const raw = localStorage.getItem(this.STORAGE_KEY);
      if (!raw) return { ...this.defaultState };
      return JSON.parse(raw);
    } catch (e) {
      console.warn("StoryState: No se pudo leer el estado.", e);
      return { ...this.defaultState };
    }
  },

  /**
   * Guarda el estado completo en localStorage.
   * @param {Object} estado - Objeto con los datos del estado
   */
  save(estado) {
    try {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(estado));
    } catch (e) {
      console.warn("StoryState: No se pudo guardar el estado.", e);
    }
  },

  /**
   * Actualiza una o varias propiedades del estado sin perder el resto.
   * @param {Object} cambios - Propiedades a actualizar
   */
  update(cambios) {
    const estadoActual = this.get();
    const nuevoEstado  = { ...estadoActual, ...cambios };
    this.save(nuevoEstado);
    return nuevoEstado;
  },

  /**
   * Guarda la decisión del jugador en el capítulo 6.
   * @param {"hackear"|"convencer"|"liberar"} opcion
   */
  guardarDecision(opcion) {
    const opciones = ["hackear", "convencer", "liberar"];
    if (!opciones.includes(opcion)) {
      console.error(`StoryState: Decisión inválida: "${opcion}"`);
      return;
    }
    this.update({ decision: opcion });
    console.log(`StoryState: Decisión guardada → ${opcion}`);
  },

  /**
   * Obtiene la decisión actual del jugador.
   * @returns {"hackear"|"convencer"|"liberar"|null}
   */
  getDecision() {
    return this.get().decision;
  },

  /**
   * Marca un capítulo como visitado.
   * @param {number} numero - Número de capítulo (1-10)
   */
  marcarCapitulo(numero) {
    const estado = this.get();
    if (!estado.capitulosVistos.includes(numero)) {
      estado.capitulosVistos.push(numero);
    }
    estado.capituloActual = numero;
    if (!estado.tiempoInicio) {
      estado.tiempoInicio = Date.now();
    }
    this.save(estado);
  },

  /**
   * Determina a qué capítulo debe ir el jugador después del capítulo 6,
   * según su decisión.
   * @returns {string} Ruta relativa del siguiente capítulo HTML
   */
  getSiguienteCapitulo() {
    const decision = this.getDecision();
    const mapa = {
      hackear:   "ch7.html",
      convencer: "ch8.html",
      liberar:   "ch9.html",  // Liberar salta directamente al colapso
    };
    return mapa[decision] || "ch9.html";
  },

  /**
   * Calcula el tiempo transcurrido desde que el jugador inició.
   * @returns {Object} { horas, minutos, segundos }
   */
  getTiempoTranscurrido() {
    const estado = this.get();
    if (!estado.tiempoInicio) return { horas: 0, minutos: 0, segundos: 0 };

    const transcurrido = Math.floor((Date.now() - estado.tiempoInicio) / 1000);
    return {
      horas:    Math.floor(transcurrido / 3600),
      minutos:  Math.floor((transcurrido % 3600) / 60),
      segundos: transcurrido % 60,
    };
  },

  /**
   * Calcula el "tiempo restante" narrativo del temporizador de NEXUS.
   * Empieza en 72h y baja con el tiempo real (dramatismo).
   * @returns {string} Formato "HH:MM:SS"
   */
  getContadorNexus() {
    const estado       = this.get();
    const maxSegundos  = 72 * 3600;

    if (!estado.tiempoInicio) {
      return "72:00:00";
    }

    const transcurrido = Math.floor((Date.now() - estado.tiempoInicio) / 1000);
    const restante     = Math.max(0, maxSegundos - transcurrido);

    const h = Math.floor(restante / 3600);
    const m = Math.floor((restante % 3600) / 60);
    const s = restante % 60;

    const pad = n => String(n).padStart(2, "0");
    return `${pad(h)}:${pad(m)}:${pad(s)}`;
  },

  /**
   * Reinicia el estado (nueva partida).
   */
  reset() {
    localStorage.removeItem(this.STORAGE_KEY);
    console.log("StoryState: Estado reiniciado.");
  },
};

// Exportar para uso en otros scripts (si se usa como módulo)
// En el proyecto usamos scripts clásicos, así que StoryState es global
