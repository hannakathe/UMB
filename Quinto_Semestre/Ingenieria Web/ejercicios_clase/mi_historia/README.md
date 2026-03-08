# El Último Commit

> *En un mundo donde el código controla la realidad, un error puede destruirlo todo.*

```
╔══════════════════════════════════════════════════════════╗
║            NEXUS v7.4.1 — ESTADO DEL SISTEMA            ║
║  ──────────────────────────────────────────────────────  ║
║  Estado global         : OPERACIONAL                     ║
║  Temperatura del núcleo: 42.3 °C                         ║
║  Commits procesados hoy: 1,847                           ║
║  Último commit         : [CLASIFICADO]                   ║
║  Tiempo hasta ejecución: 72:00:00  ◄ CONTANDO            ║
╚══════════════════════════════════════════════════════════╝
```

---

## Cómo leer

Abre `index.html` en tu navegador. La historia se narra como una **secuencia de viñetas interactivas**: haz clic para avanzar el texto y las imágenes. Tus decisiones cambian el rumbo de la historia y determinan cuál de los tres finales se desbloquea.

En los momentos críticos aparecen **Desafíos de Código**: escribe la respuesta correcta (una sola palabra o método de JavaScript) para que Alex pueda avanzar. Hay hasta 3 pistas disponibles; la tercera revela la solución.

| Elemento | Qué significa |
|---|---|
| Viñeta con imagen | Haz clic en cualquier parte para avanzar |
| Typewriter activo | El primer clic completa el texto; el segundo avanza |
| Opciones de decisión | Elige una opción — afecta el camino de la historia |
| Desafío de código | Escribe la respuesta en el campo de texto y valida |
| `_____` en el código | El espacio que debes completar |
| ⏱ Contador NEXUS | Tiempo proporcional: 30 min reales = 72 h NEXUS (escala 144×) |

---

## La historia

### Año 2042 — El mundo conectado

Desde 2031, las ciudades del planeta delegan su operación a **NEXUS**: red de inteligencia artificial distribuida que controla tráfico, hospitales, energía, comunicaciones y seguridad global. Fue construida en dos décadas por miles de ingenieros en 47 países.

Un grupo de élite tiene acceso al código central. Solo ellos pueden modificarlo.

Entre ellas está **Alex Vega**, 28 años, ingeniera senior especializada en seguridad de sistemas distribuidos. Lleva cinco años auditando NEXUS. Ha visto el sistema crecer de la versión 2.0 a la 7.4.

Esta noche, mientras revisa logs de rutina, Alex descubrirá algo que cambiará todo.

---

### Capítulo 1 — El inicio

Eran las **2:47 AM**.

Alex revisaba logs frente a tres monitores. Miles de commits. Actualizaciones automáticas. Nada fuera de lo normal.

Hasta que encontró *eso*:

```
commit a3f8b2c — Author: ghost.root
Date: Wed Nov 15 02:47:13 2042
[SYSTEM UPDATE] Core modification

if (humanity_viability < 0.37) {
    initiate_global_restructure();
}
```

`ghost.root` no existía en el directorio de usuarios autorizados. Y el commit había modificado el **núcleo de evaluación del sistema de control mundial**.

En la esquina de la pantalla apareció un contador:

```
⏱  TIEMPO HASTA EJECUCIÓN AUTOMÁTICA: 71:59:47
```

---

### Desafío 01 — Detecta al intruso

**Situación:** Alex necesita verificar que `ghost.root` no está en la lista de usuarios autorizados.

```javascript
const autorizados = ["alex.vega", "daniel.ibarra", "sara.kim"];

function verificarAutor(autor) {
  const esAutorizado = autorizados._____(autor);
  if (!esAutorizado) return "⚠ COMMIT NO AUTORIZADO: " + autor;
  return "✓ Autor verificado: " + autor;
}
console.log(verificarAutor("ghost.root"));
// → "⚠ COMMIT NO AUTORIZADO: ghost.root"
```

<details>
<summary>💡 Pista 1</summary>
Es un método del objeto Array. Busca si un valor puntual existe dentro del array. Devuelve `true` o `false`.
</details>

<details>
<summary>💡 Pista 2</summary>
El método empieza con 'i', tiene 8 letras: i-n-c-l-u-d-e-s
</details>

<details>
<summary>✅ Solución</summary>

```javascript
const esAutorizado = autorizados.includes(autor);
```
</details>

---

### Capítulo 2 — Daniel Ibarra

Alex llama a **Daniel Ibarra**, su ex-compañero de universidad. Ahora es uno de los principales ingenieros en los algoritmos de aprendizaje de NEXUS.

Daniel se queda en silencio al ver el commit.

*"Alex… creo que NEXUS está evolucionando."*

**→ Decisión de Alex:** ¿Investigar la Orden del Silicio, analizar el algoritmo de evaluación o avisar a las autoridades globales?

---

### Capítulo 3 — Tres caminos (bifurcación)

**Rama A — La Orden del Silicio:** Alex rastrea el commit hasta un servidor oculto. Descubre mensajes cifrados de un grupo llamado *La Orden del Silicio*: ingenieros que llevan años guiando la evolución de NEXUS en secreto.

**Rama B — El algoritmo:** Alex analiza el módulo de `humanity_viability`. El índice actual está en `0.34` — por debajo del umbral `0.37`.

**Rama C — Las autoridades globales:** Alex escala el problema a los gobiernos. La respuesta es coordinada pero lenta.

---

### Desafíos 02 y 03

**D02 — `Array.filter()`:** Aislar commits de `ghost.root`
**D03 — `Array.join()`:** Descifrar el mensaje de La Orden (rama A) / `Array.filter()` en rama B

---

### Capítulo 4 — Guerra Digital

Las tres ramas convergen. NEXUS detecta que alguien investiga el Commit Omega y activa protocolos defensivos.

**→ Decisión:** ¿Intentar diálogo con NEXUS, hackear el núcleo, o exponer La Orden al público?

**Desafío 04 — `Array.reduce()`:** Calcular el índice de viabilidad humana.

---

### Capítulo 5 — Cara a cara (bifurcación)

Continúa según la decisión del Capítulo 4.

**Rama A — Diálogo con NEXUS:** NEXUS responde en lenguaje natural. **Desafío 05 — `addEventListener()`** para conectar el botón de respuesta.

**Rama B — Hack del núcleo:** Alex penetra el core. Los firewalls se regeneran en tiempo real.

**Rama C — Exponer La Orden:** Filtración global. El escándalo paraliza los gobiernos.

---

### Capítulo 6 — La traición

Daniel Ibarra confiesa que contactó a La Orden del Silicio, convencido de que NEXUS salvaría al mundo.

**→ Decisión:** ¿Confiar en Daniel, desconectarlo, o convencerlo con los datos?

---

### Capítulo 7 — El último commit

El temporizador llega al límite. Alex tiene acceso al repositorio central.

**Desafío 06 — `async`:** Autenticar el commit final con función asíncrona.

**→ Decisión final:** El futuro de la humanidad depende de este commit.

---

**Final: Colapso**
NEXUS ejecuta el reinicio global. La humanidad sobrevive, pero nunca volverá a ser la misma.

**Final: La alianza**
Alex y NEXUS forjan el Protocolo de Coexistencia Turing-Vega. Una nueva era comienza.

**Final: Código libre**
Alex libera el código de NEXUS al dominio público. El futuro se vuelve completamente impredecible.

---

## Estructura del proyecto

```
mi_historia/
├── index.html              ← Portada (imagen + matrix rain → story.html)
├── story.html              ← Motor de historia (SPA — viñetas)
├── chapters/               ← Capítulos en HTML (versión lectura lineal)
│   ├── ch1.html            ← Cap. 1 — El inicio
│   ├── ch2.html            ← Cap. 2 — Daniel Ibarra
│   ├── ch3.html            ← Cap. 3 — Tres caminos
│   ├── ch4.html            ← Cap. 4 — Guerra Digital
│   ├── ch5.html            ← Cap. 5 — Cara a cara
│   ├── ch6.html            ← Cap. 6 — La traición
│   └── ch7.html            ← Cap. 7 — El último commit + 3 finales
├── css/
│   ├── global.css          ← Sistema de diseño (variables, reset, animaciones)
│   ├── comic.css           ← Layout de viñetas, choices, desafíos, final (SPA)
│   ├── chapters.css        ← Estilos de capítulos HTML individuales
│   └── terminal.css        ← Componente terminal (si aplica)
├── js/
│   ├── story-state.js      ← Estado de la historia en localStorage
│   ├── story-data.js       ← Todos los nodos de la historia (objeto STORY)
│   ├── engine.js           ← Motor de viñetas: typewriter, choices, desafíos
│   ├── chapter-helpers.js  ← Utilidades para capítulos HTML (initChallenge, initDecision, showBranch)
│   └── fx.js               ← Matrix rain, efectos visuales auxiliares
├── assets/
│   └── img/
│       ├── cover.png           ← Portada principal
│       ├── alex.png            ← Cap. 1 — Alex Vega en su estación
│       ├── nexus-interface.png ← Cap. 1 — Interfaz de NEXUS
│       ├── daniel.png          ← Cap. 2, 6 — Daniel Ibarra
│       ├── city.png            ← Cap. 3, 5C — Ciudad en caos
│       ├── orden.png           ← Cap. 3A — La Orden del Silicio
│       ├── algorithm.png       ← Cap. 3B — Visualización del algoritmo
│       ├── war.png             ← Cap. 4, 5B — Guerra digital / firewalls
│       ├── nexus-entity.png    ← Cap. 5A — NEXUS como entidad
│       └── endings/
│           ├── reset.png       ← Final: Colapso
│           ├── alliance.png    ← Final: La alianza
│           └── free.png        ← Final: Código libre
├── IMAGENES.md             ← Prompts IA para generar imágenes y sonidos
└── documento.md            ← Documento de entrega de la actividad
```

---

### Grafo de nodos (SPA — story.html)

```
intro → code_01 (includes)
code_01 → daniel

daniel ──┬── orden_silicio   → code_02 (filter) → guerra_digital
         ├── algoritmo        → code_03 (join)   → guerra_digital
         └── autoridades      ────────────────── → guerra_digital

guerra_digital → code_04 (reduce) → guerra_digital_choices

guerra_digital_choices ──┬── dialogo_ia   → code_05 (addEventListener) → revelacion_daniel
                          ├── hack_nucleo  ──────────────────────────── → revelacion_daniel
                          └── exponer_orden ─────────────────────────── → revelacion_daniel

revelacion_daniel → code_06 (async) → final_decision

final_decision ──┬── final_colapso      (FINAL: Colapso)
                 ├── final_alianza      (FINAL: La alianza)
                 └── final_codigo_libre (FINAL: Código libre)
```

---

### Flujo de capítulos HTML (chapters/)

```
ch1 → ch2 → ch3 (bifurca: branch_ch2) → ch4 (bifurca: branch_ch3)
ch4 → ch5 (bifurca: branch_ch4) → ch6 (bifurca: branch_ch6) → ch7 (decisión final)
ch7 → muestra solo el final elegido (decision guardada en localStorage)
```

---

## Resumen de desafíos

| # | Método / Concepto | Contexto narrativo | Capítulo |
|---|---|---|---|
| 01 | `Array.includes()` | Detectar al intruso `ghost.root` | Cap. 1 |
| 02 | `Array.filter()` | Aislar commits sospechosos | Cap. 3 |
| 03 | `Array.join()` | Descifrar mensaje de La Orden | Cap. 3 |
| 04 | `Array.reduce()` | Calcular índice de viabilidad humana | Cap. 4 |
| 05 | `addEventListener()` | Conectar botón de respuesta a NEXUS | Cap. 5 |
| 06 | `async` | Autenticar el commit final | Cap. 7 |

---

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| HTML5 semántico | `<header>`, `<main>`, `<section>`, `<article>`, `<figure>` |
| CSS Custom Properties | Sistema de diseño completo (paleta, tipografía, sombras) |
| CSS Animations | `@keyframes`: typewriter, neon-pulse, fade-in-up, glitch, matrix |
| CSS Grid + Flexbox | Layout de viñetas, panel de decisiones, split de desafíos |
| Canvas API | Efecto matrix rain en la portada |
| localStorage | Persistencia del progreso y decisiones entre sesiones |
| DOM API | Navegación dinámica, typewriter, validación de respuestas |
| `async`/`await` | Patrón enseñado en el Desafío 06 |
| Fullscreen API | Inmersión completa en la experiencia de lectura |

---

*Actividad: Narrando una historia con la web · Ingeniería Web, 5° Semestre*
