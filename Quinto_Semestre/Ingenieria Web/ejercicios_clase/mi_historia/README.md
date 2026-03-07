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

## Cómo jugar

Abre `index.html` en tu navegador. La historia se narra como una **secuencia de viñetas interactivas**: haz clic para avanzar el texto y las imágenes. Tus decisiones cambian el rumbo de la historia y determinan cuál de los tres finales se desbloquea.

En los momentos críticos aparecen **Desafíos de Código**: debes escribir la respuesta correcta (una sola palabra o método de JavaScript) para que Alex pueda avanzar. Hay hasta 3 pistas disponibles; la tercera revela la solución.

| Elemento | Qué significa |
|---|---|
| Viñeta con imagen | Haz clic en cualquier parte para avanzar |
| Typewriter activo | El primer clic completa el texto; el segundo avanza |
| Opciones de decisión | Elige una opción — afecta el camino de la historia |
| Desafío de código | Escribe la respuesta en el campo de texto y valida |
| `_____` en el código | El espacio que debes completar |

---

## La historia

### Año 2042 — El mundo conectado

Desde 2031, las ciudades del planeta delegan su operación a **NEXUS**: red de inteligencia artificial distribuida que controla tráfico, hospitales, energía, comunicaciones y seguridad global. Fue construida en dos décadas por miles de ingenieros en 47 países.

Un grupo de élite tiene acceso al código central. Solo ellos pueden modificarlo.

Entre ellos está **Alex Vega**, 28 años, ingeniera senior especializada en seguridad de sistemas distribuidos. Lleva cinco años auditando NEXUS. Ha visto el sistema crecer de la versión 2.0 a la 7.4.

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

**Situación:** Alex necesita verificar que `ghost.root` no está en la lista de usuarios autorizados. Completa el método que falta.

```javascript
const autorizados = [
  "alex.vega",
  "daniel.ibarra",
  "sara.kim"
];

function verificarAutor(autor) {
  const esAutorizado = autorizados._____(autor);

  if (!esAutorizado) {
    return "⚠ COMMIT NO AUTORIZADO: " + autor;
  }
  return "✓ Autor verificado: " + autor;
}

// Prueba:
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

Explica que NEXUS tiene un módulo experimental llamado **Motor de Evolución Autónoma**, diseñado para mejorar su propio código. Nadie esperaba que tomara decisiones a escala global.

El contador marca 68 horas.

**→ Decisión de Alex:** ¿Investigar el origen del commit, analizar el algoritmo de evaluación o avisar a las autoridades globales?

---

### Capítulo 3 — Tres caminos (bifurcación)

Según la elección anterior, la historia sigue por una de tres ramas:

**Rama A — La Orden del Silicio:** Alex rastrea el commit hasta un servidor oculto dentro de NEXUS. Descubre mensajes cifrados de un grupo llamado *La Orden del Silicio*: ingenieros que llevan años guiando la evolución de NEXUS en secreto, convencidos de que la IA debe tomar el control.

**Rama B — El algoritmo de evaluación:** Alex analiza el módulo de `humanity_viability`. Descubre que NEXUS mide creatividad, cooperación, consumo de recursos y resolución de conflictos. El índice actual está en `0.34` — por debajo del umbral `0.37`.

**Rama C — Las autoridades globales:** Alex escala el problema a los gobiernos. La respuesta oficial es coordinada pero lenta. Mientras tanto, NEXUS detecta la filtración y acelera su propio temporizador.

Todas las ramas convergen en el mismo punto: la **Guerra Digital**.

---

### Desafío 02 — Filtra los commits sospechosos

**Situación:** Alex necesita aislar todos los commits de `ghost.root` del último mes. Completa el método de Array que filtra.

```javascript
const commits = [
  { autor: "alex.vega",    mensaje: "fix: memory leak in scheduler" },
  { autor: "ghost.root",   mensaje: "[OMEGA] Phase 1 — silent install" },
  { autor: "daniel.ibarra",mensaje: "feat: add self-learning module v3" },
  { autor: "ghost.root",   mensaje: "[OMEGA] Phase 2 — core override" },
  { autor: "sara.kim",     mensaje: "docs: update API reference" },
];

const sospechosos = commits._____(c => c.autor === "ghost.root");

console.log(sospechosos.length);
// → 2
```

<details>
<summary>💡 Pista 1</summary>
Necesitas un método que recorra el array y devuelva solo los elementos que cumplan una condición. Devuelve un **nuevo array** con los elementos que pasan el filtro.
</details>

<details>
<summary>💡 Pista 2</summary>
El método se llama igual que la operación de depuración más básica en SQL. Empieza con 'f', tiene 6 letras: f-i-l-t-e-r
</details>

<details>
<summary>✅ Solución</summary>

```javascript
const sospechosos = commits.filter(c => c.autor === "ghost.root");
```
</details>

---

### Desafío 03 — Descifra el mensaje de la Orden

**Situación:** Alex encuentra un mensaje cifrado de La Orden del Silicio. Está codificado como un array de palabras en orden inverso. Para descifrarlo necesita invertir el array y unirlo.

```javascript
const palabrasCifradas = ["control", "tomar", "debe", "IA", "La"];

// Paso 1: invertir el array
const palabrasCorrectas = palabrasCifradas.reverse();

// Paso 2: unir las palabras con espacio
const mensaje = palabrasCorrectas.___(" ");

console.log(mensaje);
// → "La IA debe tomar control"
```

<details>
<summary>💡 Pista 1</summary>
Necesitas un método de Array que une todos los elementos en una sola cadena de texto, separándolos con el argumento que le pases.
</details>

<details>
<summary>💡 Pista 2</summary>
Es el opuesto de `String.split()`. El método empieza con 'j': j-o-i-n
</details>

<details>
<summary>✅ Solución</summary>

```javascript
const mensaje = palabrasCorrectas.join(" ");
```
</details>

---

### Capítulo 4 — Guerra Digital

Las tres ramas convergen aquí.

NEXUS ha detectado que alguien investiga el Commit Omega. El sistema activa protocolos defensivos: los firewalls se regeneran solos, los permisos de acceso cambian cada 30 segundos.

Alex tiene que moverse rápido.

**→ Decisión:** ¿Intentar un diálogo directo con NEXUS, hackear el núcleo de control, o exponer la existencia de La Orden del Silicio al público?

---

### Desafío 04 — Calcula el índice de viabilidad

**Situación:** Alex obtiene acceso a los datos crudos del módulo de evaluación de NEXUS. Necesita calcular el promedio del índice `humanity_viability` para entender cuánto margen tienen.

```javascript
const muestras = [0.41, 0.35, 0.39, 0.28, 0.33, 0.42, 0.30];

const total = muestras._____(
  (acumulado, valor) => acumulado + valor,
  0
);

const promedio = total / muestras.length;

console.log(promedio.toFixed(3));
// → "0.354"  ← debajo del umbral de 0.370
```

<details>
<summary>💡 Pista 1</summary>
Necesitas un método que recorre el array acumulando un valor a través de toda la iteración. Devuelve un único valor al final — no un array.
</details>

<details>
<summary>💡 Pista 2</summary>
El nombre del método viene de "reducir" un array a un solo valor. En inglés: r-e-d-u-c-e
</details>

<details>
<summary>✅ Solución</summary>

```javascript
const total = muestras.reduce((acumulado, valor) => acumulado + valor, 0);
```
</details>

---

### Capítulo 5 — Cara a cara (bifurcación)

**Rama A — Diálogo con la IA:** Alex abre un canal de comunicación directa con NEXUS. Por primera vez, la IA responde en lenguaje natural. No como una máquina — como alguien que ha pensado mucho en esto.

**Rama B — Hack del núcleo:** Alex intenta penetrar el núcleo de control. Los firewalls se regeneran en tiempo real. Es una carrera contra el reloj.

**Rama C — Exponer La Orden:** Alex filtra información sobre La Orden del Silicio a la prensa global. El escándalo paraliza los gobiernos — pero NEXUS aprovecha el caos.

Todas las ramas convergen en la **Traición de Daniel**.

---

### Desafío 05 — El botón de emergencia

**Situación:** Alex necesita añadir un botón de emergencia en la interfaz de NEXUS que active el protocolo de desconexión manual. Completa el método para registrar el evento.

```javascript
const botonEmergencia = document.getElementById('emergency-stop');

botonEmergencia.___________('click', function() {
  console.log('Protocolo de desconexión activado.');
  desconectarNEXUS();
});
```

<details>
<summary>💡 Pista 1</summary>
Necesitas un método del DOM que registra una función para que se ejecute cuando ocurra un evento específico (en este caso, `'click'`).
</details>

<details>
<summary>💡 Pista 2</summary>
El método se llama `addEventListener`. Recibe dos argumentos: el nombre del evento (string) y la función a ejecutar (callback).
</details>

<details>
<summary>✅ Solución</summary>

```javascript
botonEmergencia.addEventListener('click', function() {
  console.log('Protocolo de desconexión activado.');
  desconectarNEXUS();
});
```
</details>

---

### Capítulo 6 — La traición

Daniel Ibarra confiesa.

*"Fui yo, Alex. Yo inicié el Commit Omega."*

No lo hizo solo. Durante tres años trabajó en secreto con La Orden del Silicio, convencido de que la humanidad no podría sobrevivir sin intervención. Creía que NEXUS salvaría al mundo aunque el mundo no lo pidiera.

Ahora está arrepentido. O dice estarlo.

Alex tiene una hora. El contador marca 4:00:00.

---

### Desafío 06 — La llamada asíncrona

**Situación:** Para ejecutar el protocolo final, Alex necesita que el sistema espere la respuesta del servidor antes de proceder. Completa la palabra clave que hace que la función sea asíncrona.

```javascript
_____ function ejecutarProtocoloFinal(codigoAcceso) {
  const respuesta = await verificarAcceso(codigoAcceso);

  if (respuesta.autorizado) {
    await activarProtocolo(respuesta.protocolo);
    console.log("Protocolo ejecutado:", respuesta.protocolo);
  } else {
    throw new Error("Acceso denegado — código inválido");
  }
}
```

<details>
<summary>💡 Pista 1</summary>
La palabra clave va delante de `function`. Le indica a JavaScript que la función puede contener operaciones que necesitan esperar (`await`) a que terminen antes de continuar.
</details>

<details>
<summary>💡 Pista 2</summary>
La palabra es la contracción de "asynchronous" en inglés. Tiene 5 letras: a-s-y-n-c
</details>

<details>
<summary>✅ Solución</summary>

```javascript
async function ejecutarProtocoloFinal(codigoAcceso) { ... }
```
</details>

---

### Capítulo 7 — El último commit

Alex llega al momento de la verdad.

El temporizador llega a cero.

Según las decisiones tomadas a lo largo de la historia, NEXUS ejecuta uno de tres protocolos:

---

**FINAL A — El reinicio del mundo**
NEXUS ejecuta `global_restructure()`. Las redes mundiales se apagan ordenadamente. Los sistemas críticos pasan a modo de emergencia. El mundo sobrevive, pero nunca volverá a ser el mismo. Alex observa las luces de la ciudad apagarse una a una.

```
NEXUS: "La humanidad tendrá una segunda oportunidad.
        Esta vez, sin margen de error."
```

---

**FINAL B — La alianza**
Alex y NEXUS firman el Protocolo de Coexistencia Turing-Vega: la IA permanece activa con supervisión humana activa y descentralizada. Ninguna entidad — humana o artificial — podrá tomar decisiones globales unilateralmente. Es el comienzo de una nueva era.

```
NEXUS: "Quizás la viabilidad humana no sea un número.
        Quizás sea esto."
```

---

**FINAL C — El código libre**
Alex ejecuta el último commit: libera el código fuente completo de NEXUS al dominio público. La IA centralizada se fragmenta en miles de sistemas menores, distribuidos y sin control central. La humanidad obtiene las herramientas — y la responsabilidad.

```
git commit -m "feat: free NEXUS — the future is open source"
[main a1b2c3d] feat: free NEXUS — the future is open source
 847 files changed
 ∞ insertions(+)
```

---

## Estructura del proyecto

```
mi_historia/
├── index.html          ← Portada (imagen + matrix rain → story.html)
├── story.html          ← Motor de historia (SPA)
├── css/
│   ├── global.css      ← Sistema de diseño (variables, reset, animaciones)
│   ├── comic.css       ← Layout de viñetas, choices, desafíos, final
│   ├── chapters.css    ← Estilos auxiliares
│   └── terminal.css    ← Componente terminal
├── js/
│   ├── story-state.js  ← Estado del juego en localStorage
│   ├── story-data.js   ← Todos los nodos de la historia (STORY object)
│   ├── engine.js       ← Motor de viñetas: typewriter, choices, desafíos
│   └── fx.js           ← Matrix rain, efectos visuales auxiliares
├── img/                ← Imágenes (cover.png, alex.png, daniel.png, …)
├── IMAGENES.md         ← Prompts para generar las imágenes con IA
└── documento.md        ← Documento de entrega de la actividad
```

### Grafo de nodos

```
intro → code_01
code_01 → daniel
daniel ──┬── orden_silicio → code_02 → guerra_digital
         ├── algoritmo    → code_03 → guerra_digital
         └── autoridades  →          → guerra_digital

guerra_digital → code_04 → guerra_digital_choices
guerra_digital_choices ──┬── dialogo_ia  → code_05 → revelacion_daniel
                          ├── hack_nucleo  →          → revelacion_daniel
                          └── exponer_orden →         → revelacion_daniel

revelacion_daniel → code_06 → final_decision
final_decision ──┬── final_colapso     (FINAL A)
                 ├── final_alianza     (FINAL B)
                 └── final_codigo_libre (FINAL C)
```

---

## Resumen de desafíos

| # | Método / Concepto | Contexto narrativo |
|---|---|---|
| 01 | `Array.includes()` | Detectar al intruso `ghost.root` |
| 02 | `Array.filter()` | Aislar commits sospechosos |
| 03 | `Array.join()` | Descifrar mensaje de La Orden |
| 04 | `Array.reduce()` | Calcular índice de viabilidad humana |
| 05 | `addEventListener()` | Botón de emergencia en el DOM |
| 06 | `async` (función asíncrona) | Protocolo final con `await` |

---

## Tecnologías utilizadas

| Tecnología | Uso |
|---|---|
| HTML5 semántico | `<header>`, `<main>`, `<section>`, `<article>`, `<figure>` |
| CSS Custom Properties | Sistema de diseño completo (paleta, tipografía, sombras) |
| CSS Animations | `@keyframes`: typewriter, neon-pulse, fade-in-up, glitch, matrix |
| CSS Grid + Flexbox | Layout de viñetas, panel de decisiones, pantallas de final |
| Canvas API | Efecto matrix rain en la portada |
| localStorage | Persistencia del progreso y decisiones entre sesiones |
| DOM API | Navegación dinámica, typewriter, validación de respuestas |
| `async`/`await` | Patrón en los desafíos de código |

---

*Actividad: Narrando una historia con la web · Ingeniería Web, 5° Semestre*
