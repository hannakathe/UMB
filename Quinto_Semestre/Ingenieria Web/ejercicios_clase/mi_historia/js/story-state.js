/**
 * story-state.js — El Último Commit
 * Gestión del estado de la historia usando localStorage.
 */

const StoryState = {

  STORAGE_KEY: "nexus_story_state",

  defaultState: {
    decision:           null,   // ID del nodo final elegido
    currentNode:        null,   // ID del nodo activo (para reanudar en SPA)
    capituloActual:     1,
    capitulosVistos:    [],
    tiempoInicio:       null,   // timestamp cuando inició
    endingDesbloqueado: null,
    // Ramas de capítulos (para las páginas HTML individuales)
    branch_ch2: null,   // "orden_silicio" | "algoritmo" | "autoridades"
    branch_ch3: null,   // "infiltrar_orden" | "sellar_servidor"
    branch_ch4: null,   // "dialogo_ia" | "hack_nucleo" | "exponer_orden"
    branch_ch6: null,   // "confiar" | "rechazar" | "convencer"
  },

  get() {
    try {
      const raw = localStorage.getItem(this.STORAGE_KEY);
      if (!raw) return { ...this.defaultState };
      return { ...this.defaultState, ...JSON.parse(raw) };
    } catch (e) {
      console.warn("StoryState: No se pudo leer el estado.", e);
      return { ...this.defaultState };
    }
  },

  save(estado) {
    try {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(estado));
    } catch (e) {
      console.warn("StoryState: No se pudo guardar el estado.", e);
    }
  },

  update(cambios) {
    const nuevoEstado = { ...this.get(), ...cambios };
    this.save(nuevoEstado);
    return nuevoEstado;
  },

  /** Guarda la decisión del jugador (acepta cualquier valor). */
  guardarDecision(nodoFinal) {
    this.update({ decision: nodoFinal });
  },

  getDecision() {
    return this.get().decision;
  },

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

  /** Tiempo transcurrido como string "Xh Ym". */
  getTiempoTranscurrido() {
    const estado = this.get();
    if (!estado.tiempoInicio) return '0h 0m';
    const seg = Math.floor((Date.now() - estado.tiempoInicio) / 1000);
    const h   = Math.floor(seg / 3600);
    const m   = Math.floor((seg % 3600) / 60);
    return `${h}h ${m}m`;
  },

  /** Contador regresivo de NEXUS: 72h → 0. Formato "HH:MM:SS". */
  getContadorNexus() {
    const estado      = this.get();
    const maxSegundos = 72 * 3600;
    if (!estado.tiempoInicio) return "72:00:00";
    const transcurrido = Math.floor((Date.now() - estado.tiempoInicio) / 1000);
    const restante     = Math.max(0, maxSegundos - transcurrido);
    const h = Math.floor(restante / 3600);
    const m = Math.floor((restante % 3600) / 60);
    const s = restante % 60;
    const pad = n => String(n).padStart(2, "0");
    return `${pad(h)}:${pad(m)}:${pad(s)}`;
  },

  reset() {
    localStorage.removeItem(this.STORAGE_KEY);
  },
};
