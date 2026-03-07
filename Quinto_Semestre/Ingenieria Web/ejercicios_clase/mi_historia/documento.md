# El Último Commit — Documento de Entrega

**Actividad:** Narrando una historia con la web &nbsp;·&nbsp; Ingeniería Web, 5° Semestre

---

## 1. La historia narrada

**"El Último Commit"** es una historia de ciencia ficción interactiva ambientada en el año 2042. Alex Vega, un desarrollador de seguridad, descubre un commit no autorizado en NEXUS —la IA que controla la infraestructura global— con un temporizador de 72 horas para ejecutar un reset de la humanidad. La historia tiene 10 capítulos, un punto de bifurcación en el capítulo 6 (donde el jugador elige entre 3 acciones) y 3 finales posibles: reinicio, alianza o código libre.

---

## 2. Funcionalidades implementadas

| Funcionalidad | Tecnología | Archivo(s) |
|---|---|---|
| Navegación entre 10 capítulos | HTML5 `<nav>`, `<a>` | `chapters/ch1–ch10.html` |
| Temporizador regresivo de NEXUS | `localStorage`, `setInterval` | `js/fx.js`, `js/story-state.js` |
| Efecto Matrix Rain de fondo | Canvas API, `setInterval` | `js/fx.js` → `initMatrixRain()` |
| Efecto typewriter de terminal | DOM manipulation, `setTimeout` | `js/fx.js` → `typeWriter()` |
| Secciones revelar/ocultar | `addEventListener("click")`, CSS transitions | `js/fx.js` → `initRevealButtons()` |
| Panel de decisión interactivo (cap. 6) | `addEventListener`, `localStorage` | `js/main.js` → `initDecisionPanel()` |
| Finales dinámicos (cap. 10) | `localStorage.getItem()`, DOM | `js/main.js` → `mostrarFinalCorrecto()` |
| Animación de entrada de elementos | CSS `@keyframes fade-in-up` | `css/global.css` |
| Animación de texto glitch | CSS `@keyframes glitch` | `css/terminal.css` |
| Neon pulse en títulos | CSS `@keyframes neon-pulse` | `css/global.css` |
| Reproducción de audio controlada | `<audio controls>` | `chapters/ch1, ch3, ch9.html` |
| Reproducción de video | `<video controls>` | `chapters/ch5.html` |

---

## 3. Justificación del diseño

**Paleta de colores:** Se eligió una paleta oscura (fondo `#0a0d12`) con acentos en cyan neón (`#00d4ff`) y verde neón (`#00ff88`), coherente con la estética *cyberpunk* de la historia. El morado (`#9b59b6`) se reserva exclusivamente para NEXUS, creando una identidad visual diferenciada para la IA.

**Tipografía:** Se usa exclusivamente fuentes monoespaciadas (`Courier New`, `Share Tech Mono`) para reforzar la atmósfera de terminal de código. Todo el texto de interfaz se lee como si fuera una pantalla de sistema real.

**Layout:** CSS Grid para el índice de capítulos (adaptable a cualquier pantalla), CSS Flexbox para el panel de decisión (se reorganiza en móvil). El `max-width: 860px` en los capítulos garantiza legibilidad óptima.

**Estructura semántica:** Cada capítulo usa `<header>`, `<main>`, `<article>`, `<section>`, `<figure>`, `<figcaption>`, `<audio>`/`<video>`, y `<footer>` correctamente. Las imágenes tienen atributos `alt` descriptivos. Los botones tienen `type="button"` y elementos de audio/video tienen etiquetas accesibles.

**Interactividad:** La decisión del jugador (capítulo 6) se persiste en `localStorage`, permitiendo que el capítulo 10 muestre el final correcto. El temporizador usa el tiempo real desde que el jugador inició, creando urgencia narrativa genuina.
