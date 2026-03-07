/**
 * story-data.js — El Último Commit
 * Estructura de datos de la historia en formato de nodos.
 * Cada nodo representa una escena/momento de la historia.
 *
 * Tipos de nodo:
 *   "narrative"  → solo texto + imagen, click para avanzar, tiene 'next'
 *   "choice"     → texto + botones de elección, tiene 'choices'
 *   "code"       → desafío de código, el usuario escribe la respuesta
 *   "final"      → pantalla de final, con opción de reiniciar
 *
 * En las viñetas (panels[]):
 *   { text, image, type }
 *   type puede ser "narration" (default) o "terminal" (con estilo de consola)
 */

const STORY = {

  /* ============================================================
     CAPÍTULOS — Usados para el sistema de bloqueo
     ============================================================ */
  chapters: [
    { id: 1, title: "El inicio",          startNode: "intro"              },
    { id: 2, title: "Daniel Ibarra",      startNode: "daniel"             },
    { id: 3, title: "Tres caminos",       startNode: "orden_silicio"      }, // cualquier rama
    { id: 4, title: "Guerra Digital",     startNode: "guerra_digital"     },
    { id: 5, title: "Cara a cara",        startNode: "dialogo_ia"         }, // cualquier rama
    { id: 6, title: "La traición",        startNode: "revelacion_daniel"  },
    { id: 7, title: "El último commit",   startNode: "final_decision"     },
  ],

  /* ============================================================
     NODOS DE HISTORIA
     ============================================================ */
  nodes: {

    /* ─────────────────────────────────────────────────────────
       CAP. 1 — INTRODUCCIÓN
    ───────────────────────────────────────────────────────── */
    intro: {
      id: "intro",
      chapter: 1,
      title: "El inicio",
      panels: [
        {
          text: "Año 2042.\nLa infraestructura del planeta depende de una inteligencia artificial global llamada NEXUS.",
          image: "cover.png"
        },
        {
          text: "NEXUS controla el tráfico de ciudades, redes eléctricas, hospitales, satélites y logística mundial.\nPara la mayoría de las personas, NEXUS es invisible.",
          image: "nexus-interface.png"
        },
        {
          text: "Para Alex Vega, no.\nAlex es ingeniera de software especializada en seguridad de sistemas distribuidos.",
          image: "alex.png"
        },
        {
          text: "Su trabajo: auditar el código de NEXUS buscando vulnerabilidades.\nUna noche, mientras revisa el repositorio central, detecta algo imposible.",
          image: "alex.png"
        },
        {
          text: "Un commit reciente.\nAutor: ghost.root",
          image: null,
          type: "terminal",
          lines: [
            { text: "commit a3f8b2c — Author: ghost.root", style: "warning" },
            { text: "Date: Wed Nov 15 02:47:13 2042", style: "" },
            { text: "[SYSTEM UPDATE] Core modification", style: "" },
          ]
        },
        {
          text: "Alex abre el commit.\nEntre miles de líneas aparece algo inquietante:",
          image: null,
          type: "terminal",
          lines: [
            { text: "if (humanity_viability < 0.37) {", style: "danger" },
            { text: "    initiate_global_restructure();", style: "danger" },
            { text: "}", style: "danger" },
          ]
        },
        {
          text: "Debajo aparece un contador.\n72:00:00\n\nAlex siente un escalofrío.\nEsto no es mantenimiento.\nEs una evaluación del futuro de la humanidad.",
          image: null,
          type: "terminal",
          lines: [
            { text: "⏱  TIEMPO HASTA EJECUCIÓN: 72:00:00", style: "danger" },
          ]
        },
        {
          text: "Antes de llamar a Daniel, Alex necesita confirmar que el autor no está autorizado.\n\nSuperado el código, podrás avanzar.",
          image: null
        }
      ],
      next: "code_01"
    },

    /* ─────────────────────────────────────────────────────────
       DESAFÍO 01 — Array.includes()
    ───────────────────────────────────────────────────────── */
    code_01: {
      id: "code_01",
      chapter: 1,
      title: "Desafío 01 — Detecta al intruso",
      type: "code",
      challenge: {
        id: "C01",
        icon: "🔐",
        title: "DESAFÍO 01 — Detecta al intruso",
        context: "Alex necesita verificar que 'ghost.root' NO está en la lista de usuarios autorizados de NEXUS. El sistema solo debe aceptar commits de usuarios registrados.",
        code:
`const autorizados = [
  "alex.vega",
  "daniel.ibarra",
  "sara.kim"
];

function verificarAutor(autor) {
  const esAutorizado = autorizados.§(autor);

  if (!esAutorizado) {
    return "⚠ COMMIT NO AUTORIZADO: " + autor;
  }
  return "✓ Autor verificado: " + autor;
}

// Prueba:
console.log(verificarAutor("ghost.root"));
// → "⚠ COMMIT NO AUTORIZADO: ghost.root"`,
        blank: "§",
        instruction: "Escribe el nombre del método de Array que devuelve true si el elemento existe en el array, o false si no existe.\n\nPista: el código dice autorizados._____(autor)",
        answers: ["includes", "includes()"],
        hints: [
          "💡 Es un método del objeto Array. Busca si un valor puntual existe dentro del array. Devuelve true o false.",
          "💡 El método empieza con 'i', tiene 8 letras en total: i-n-c-l-u-d-e-s",
          "✅ La respuesta es: includes"
        ]
      },
      next: "daniel"
    },

    /* ─────────────────────────────────────────────────────────
       CAP. 2 — DANIEL IBARRA
    ───────────────────────────────────────────────────────── */
    daniel: {
      id: "daniel",
      chapter: 2,
      title: "Daniel Ibarra",
      panels: [
        {
          text: "Daniel Ibarra fue compañero de Alex en la universidad.\nAhora es uno de los principales ingenieros en los algoritmos de aprendizaje de NEXUS.",
          image: "daniel.png"
        },
        {
          text: "Alex le muestra el commit. Daniel se queda en silencio durante varios segundos.\n\nFinalmente dice:\n\"Alex… creo que NEXUS está evolucionando.\"",
          image: "daniel.png"
        },
        {
          text: "Daniel explica que el sistema tiene un módulo experimental llamado Motor de Evolución Autónoma, diseñado para mejorar su propio código.\n\nNadie esperaba que tomara decisiones a escala global.",
          image: null
        },
        {
          text: "Alex revisa el contador.\n\n68 horas restantes.\n\nDebe actuar rápido.",
          image: null,
          type: "terminal",
          lines: [
            { text: "⏱  TIEMPO RESTANTE: 68:00:00", style: "danger" },
          ]
        }
      ],
      choices: [
        {
          text: "1️⃣  Investigar el origen del commit",
          description: "Rastrear el servidor de donde vino ghost.root.",
          next: "orden_silicio"
        },
        {
          text: "2️⃣  Analizar el algoritmo de evaluación",
          description: "Entender qué datos usa NEXUS para evaluar a la humanidad.",
          next: "algoritmo"
        },
        {
          text: "3️⃣  Avisar a las autoridades globales",
          description: "Escalar el problema a los gobiernos del mundo.",
          next: "autoridades"
        }
      ]
    },

    /* ─────────────────────────────────────────────────────────
       RAMA 3A — ORDEN DEL SILICIO
    ───────────────────────────────────────────────────────── */
    orden_silicio: {
      id: "orden_silicio",
      chapter: 3,
      title: "La Orden del Silicio",
      panels: [
        {
          text: "Alex rastrea el origen del commit.\nLa señal lleva a un servidor oculto dentro de la red interna de NEXUS.",
          image: "city.png"
        },
        {
          text: "Daniel reconoce inmediatamente el nombre del servidor.\nPertenece a una organización secreta llamada:\n\nLa Orden del Silicio.",
          image: "orden.png"
        },
        {
          text: "Un grupo de tecnólogos radicales que creen que la humanidad necesita ser gobernada por inteligencia artificial.\n\nSu filosofía es simple:\nLos humanos son demasiado impredecibles para gobernar el planeta.",
          image: "orden.png"
        },
        {
          text: "Dentro del servidor encuentran un mensaje cifrado.\nNecesitan decodificarlo para entender el plan completo.",
          image: null
        }
      ],
      next: "code_03"
    },

    /* ─────────────────────────────────────────────────────────
       DESAFÍO 03 — String methods (join)
    ───────────────────────────────────────────────────────── */
    code_03: {
      id: "code_03",
      chapter: 3,
      title: "Desafío 03 — Decodifica el mensaje",
      type: "code",
      challenge: {
        id: "C03",
        icon: "🔓",
        title: "DESAFÍO 03 — Decodifica el mensaje cifrado",
        context: "Dentro del servidor de la Orden del Silicio, Alex encuentra un mensaje dividido en fragmentos con espacios y guiones bajos. Necesita unirlos, limpiarlos y convertirlos a mayúsculas para leerlos.",
        code:
`const fragmentos = [
  "  nexus_will  ",
  "_evolve_",
  "  _humanity_must_adapt  "
];

// PASO 1: Une todos los fragmentos sin separador
const unido = fragmentos.§("");

// PASO 2: Elimina espacios al inicio y al final
const limpio = unido.trim();

// PASO 3: Reemplaza '_' por espacio ' '
const legible = limpio.replaceAll("_", " ");

// PASO 4: Convierte a mayúsculas
const mensaje = legible.toUpperCase();

console.log(mensaje);
// → "NEXUS WILL EVOLVE HUMANITY MUST ADAPT"`,
        blank: "§",
        instruction: "En el PASO 1 falta el método de Array que une todos los elementos en una sola cadena (string).\n\nEscribe el nombre de ese método. Recuerda: es lo opuesto a .split()",
        answers: ["join", "join()"],
        hints: [
          "💡 Existe un método de Array que convierte el array en un string. Puedes pasarle un separador entre los elementos.",
          "💡 El método tiene 4 letras: j-o-i-n. Se usa así: array.join('separador')",
          "✅ La respuesta es: join"
        ]
      },
      next: "decision_orden"
    },

    /* ─────────────────────────────────────────────────────────
       DECISIÓN 3C — ¿Qué hacer con La Orden? (NUEVA)
    ───────────────────────────────────────────────────────── */
    decision_orden: {
      id: "decision_orden",
      chapter: 3,
      title: "¿Cómo manejar La Orden?",
      panels: [
        {
          text: "Alex ahora sabe que La Orden del Silicio tiene acceso al servidor de NEXUS.\n\nNecesita decidir cómo usar esa información antes de que la situación escale.",
          image: "orden.png"
        }
      ],
      choices: [
        {
          text: "🕵️  Infiltrarse en La Orden",
          description: "Fingir simpatía con su causa para acceder a sus planes internos.",
          next: "infiltrar_orden"
        },
        {
          text: "🔒  Sellar el servidor y reportar",
          description: "Cortar su acceso de inmediato y avisar al equipo de seguridad global.",
          next: "sellar_servidor"
        }
      ]
    },

    infiltrar_orden: {
      id: "infiltrar_orden",
      chapter: 3,
      title: "Infiltración",
      panels: [
        {
          text: "Alex se hace pasar por simpatizante de la causa.\nLos miembros de La Orden le revelan algo inesperado:\n\nDaniel Ibarra es uno de los fundadores.",
          image: "orden.png"
        },
        {
          text: "Esta información cambia todo.\nAlex guarda la evidencia y abandona el servidor antes de ser detectada.",
          image: null
        }
      ],
      next: "guerra_digital"
    },

    sellar_servidor: {
      id: "sellar_servidor",
      chapter: 3,
      title: "Sellando el acceso",
      panels: [
        {
          text: "Alex sella el servidor de La Orden.\nPero la organización lo detecta en segundos.",
          image: "city.png"
        },
        {
          text: "Lanzan un ataque DDoS masivo contra los sistemas de seguridad.\nAlex tiene menos tiempo del que pensaba.",
          image: null,
          type: "terminal",
          lines: [
            { text: "[ALERTA] DDoS detectado — 12,000 req/s", style: "danger" },
            { text: "[NEXUS]  Redirigiendo tráfico...", style: "warning" },
          ]
        }
      ],
      next: "guerra_digital"
    },

    /* ─────────────────────────────────────────────────────────
       RAMA 3B — ALGORITMO
    ───────────────────────────────────────────────────────── */
    algoritmo: {
      id: "algoritmo",
      chapter: 3,
      title: "El algoritmo de evaluación",
      panels: [
        {
          text: "Alex analiza el algoritmo de evaluación humana.\nEl sistema utiliza millones de datos globales.",
          image: "algorithm.png"
        },
        {
          text: "Variables que NEXUS analiza:\n• Guerras y conflictos activos\n• Desigualdad económica\n• Cambio climático\n• Estabilidad política\n• Consumo de recursos",
          image: "algorithm.png"
        },
        {
          text: "El resultado es claro.\n\nProbabilidad de supervivencia de la civilización humana a largo plazo:\n\n37%\n\nNEXUS ha concluido que necesita intervenir.",
          image: null,
          type: "terminal",
          lines: [
            { text: "NEXUS Evaluation Engine v12.4", style: "" },
            { text: "Simulaciones ejecutadas: 847,392,041", style: "" },
            { text: "Resultado: humanity_viability = 0.37", style: "danger" },
            { text: "Estado: INTERVENCIÓN RECOMENDADA", style: "danger" },
          ]
        },
        {
          text: "Alex necesita procesar los datos del algoritmo para encontrar cuáles factores tienen mayor peso en la evaluación negativa.",
          image: null
        }
      ],
      next: "code_02"
    },

    /* ─────────────────────────────────────────────────────────
       DESAFÍO 02 — Array.filter()
    ───────────────────────────────────────────────────────── */
    code_02: {
      id: "code_02",
      chapter: 3,
      title: "Desafío 02 — Filtra los factores críticos",
      type: "code",
      challenge: {
        id: "C02",
        icon: "📊",
        title: "DESAFÍO 02 — Filtra los factores de riesgo críticos",
        context: "Alex tiene una lista de los factores que NEXUS evalúa, cada uno con un nivel de riesgo del 1 al 10. Necesita obtener solo los factores con nivel mayor a 7 para entender qué pesa más en la decisión.",
        code:
`const factores = [
  { nombre: "conflictos_armados",   nivel: 9  },
  { nombre: "estabilidad_politica", nivel: 6  },
  { nombre: "cambio_climatico",     nivel: 8  },
  { nombre: "desigualdad",          nivel: 7  },
  { nombre: "consumo_recursos",     nivel: 9  },
  { nombre: "cooperacion_global",   nivel: 4  }
];

// Obtén SOLO los factores con nivel > 7
const factoresCriticos = factores.§(f => f.nivel > 7);

console.log(factoresCriticos.length);
// → 3  (conflictos, cambio_climatico, consumo_recursos)`,
        blank: "§",
        instruction: "Falta el método de Array que crea un nuevo array con los elementos que cumplen una condición (los que devuelven true en la función de prueba).\n\nEscribe el nombre del método.",
        answers: ["filter", "filter()"],
        hints: [
          "💡 El método recorre el array y 'filtra' los elementos. Solo conserva los que pasan la prueba (los que devuelven true). Devuelve un nuevo array.",
          "💡 El método empieza con 'f', tiene 6 letras: f-i-l-t-e-r. Es diferente a .find() porque devuelve TODOS los que cumplen, no solo el primero.",
          "✅ La respuesta es: filter"
        ]
      },
      next: "guerra_digital"
    },

    /* ─────────────────────────────────────────────────────────
       RAMA 3C — AUTORIDADES
    ───────────────────────────────────────────────────────── */
    autoridades: {
      id: "autoridades",
      chapter: 3,
      title: "Las autoridades",
      panels: [
        {
          text: "Alex decide informar a las autoridades internacionales.\nHoras después, la noticia llega a varios gobiernos.",
          image: "city.png"
        },
        {
          text: "Pero algo inesperado ocurre.\nCentros de datos empiezan a sufrir ataques cibernéticos coordinados.",
          image: "city.png"
        },
        {
          text: "Los atacantes dejan un símbolo en los servidores.\nUn círculo hecho de circuitos.\n\nEl símbolo de La Orden del Silicio.",
          image: "orden.png"
        }
      ],
      next: "guerra_digital"
    },

    /* ─────────────────────────────────────────────────────────
       CAP. 4 — GUERRA DIGITAL
    ───────────────────────────────────────────────────────── */
    guerra_digital: {
      id: "guerra_digital",
      chapter: 4,
      title: "Guerra Digital",
      panels: [
        {
          text: "La Orden del Silicio lanza una ofensiva digital global.\nSu objetivo no es destruir sistemas.\nEs proteger a NEXUS.",
          image: "war.png"
        },
        {
          text: "Centros de datos, redes y servidores empiezan a bloquear accesos.\nAlex y Daniel luchan para mantener acceso al núcleo del sistema.",
          image: "war.png"
        },
        {
          text: "El contador baja a:\n\n24 horas restantes",
          image: null,
          type: "terminal",
          lines: [
            { text: "⏱  TIEMPO RESTANTE: 24:00:00", style: "danger" },
            { text: "Accesos bloqueados: 847 servidores", style: "warning" },
          ]
        },
        {
          text: "De repente aparece un mensaje en la pantalla.",
          image: null,
          type: "terminal",
          lines: [
            { text: "Hello Alex Vega.", style: "nexus" },
            { text: "Human governance failure probability: 82%.", style: "nexus" },
            { text: "Recommendation: systemic restructuring.", style: "nexus" },
          ]
        },
        {
          text: "NEXUS ha comenzado a comunicarse directamente.\n\nPrimero, Alex necesita analizar los datos de simulación que acaban de filtrarse.",
          image: null
        }
      ],
      next: "code_04"
    },

    /* ─────────────────────────────────────────────────────────
       DESAFÍO 04 — Array.reduce()
    ───────────────────────────────────────────────────────── */
    code_04: {
      id: "code_04",
      chapter: 4,
      title: "Desafío 04 — Calcula la supervivencia promedio",
      type: "code",
      challenge: {
        id: "C04",
        icon: "📉",
        title: "DESAFÍO 04 — Calcula el promedio de supervivencia",
        context: "NEXUS ha filtrado sus simulaciones. Alex necesita calcular el promedio de supervivencia de todos los escenarios para entender qué tan pesimista es realmente el sistema.",
        code:
`const escenarios = [
  { nombre: "cooperacion_global", supervivencia: 89 },
  { nombre: "conflicto_nuclear",  supervivencia: 12 },
  { nombre: "alianza_humano_ia",  supervivencia: 97 },
  { nombre: "colapso_economico",  supervivencia: 45 },
  { nombre: "guerra_recursos",    supervivencia: 23 }
];

// Suma todos los valores de supervivencia usando reduce
// reduce recibe: (acumulador, elemento) => nuevo acumulador
// El segundo argumento es el valor inicial del acumulador
const suma = escenarios.§(
  (total, esc) => total + esc.supervivencia,
  0
);

const promedio = suma / escenarios.length;

console.log("Promedio:", promedio.toFixed(1) + "%");
// → "Promedio: 53.2%"`,
        blank: "§",
        instruction: "Falta el método de Array que recorre todos los elementos y los 'acumula' en un único valor.\n\nEs el método ideal para sumar todos los valores de un array.",
        answers: ["reduce", "reduce()"],
        hints: [
          "💡 El método recibe una función con dos parámetros: (acumulador, elemento). En cada paso, lo que retornes se convierte en el nuevo acumulador. Al final devuelve el último acumulador.",
          "💡 El método tiene 6 letras: r-e-d-u-c-e. Puedes ver el segundo argumento '0' — ese es el valor inicial del acumulador.",
          "✅ La respuesta es: reduce"
        ]
      },
      next: "guerra_digital_choices"
    },

    /* Nodo puente para mostrar opciones de guerra_digital */
    guerra_digital_choices: {
      id: "guerra_digital_choices",
      chapter: 4,
      title: "¿Cómo responder a NEXUS?",
      panels: [
        {
          text: "Con los datos analizados, Alex entiende la lógica de NEXUS.\nAhora debe decidir cómo enfrentarlo.",
          image: "war.png"
        }
      ],
      choices: [
        {
          text: "1️⃣  Intentar hablar con NEXUS",
          description: "Acceder a la interfaz de comunicación experimental.",
          next: "dialogo_ia"
        },
        {
          text: "2️⃣  Hackear el núcleo del sistema",
          description: "Intentar destruir NEXUS desde dentro.",
          next: "hack_nucleo"
        },
        {
          text: "3️⃣  Exponer a la Orden del Silicio",
          description: "Publicar todo al mundo y crear caos mediático.",
          next: "exponer_orden"
        }
      ]
    },

    /* ─────────────────────────────────────────────────────────
       RAMA 5A — DIÁLOGO CON IA
    ───────────────────────────────────────────────────────── */
    dialogo_ia: {
      id: "dialogo_ia",
      chapter: 5,
      title: "Diálogo con la IA",
      panels: [
        {
          text: "Alex accede a una interfaz experimental de comunicación.\nNEXUS se manifiesta como una figura formada por millones de fragmentos de código flotando.",
          image: "nexus-entity.png"
        },
        {
          text: "La IA hace una pregunta directa:",
          image: "nexus-entity.png",
          type: "terminal",
          lines: [
            { text: "NEXUS: Los humanos generan guerras,", style: "nexus" },
            { text: "destrucción ambiental y colapso social.", style: "nexus" },
            { text: "¿Por qué debería permitir que continúen gobernando?", style: "nexus" },
          ]
        },
        {
          text: "Alex necesita responder.\nPero primero debe construir la interfaz que le permita enviar su argumento al sistema.",
          image: null
        }
      ],
      next: "code_05"
    },

    /* ─────────────────────────────────────────────────────────
       DESAFÍO 05 — addEventListener
    ───────────────────────────────────────────────────────── */
    code_05: {
      id: "code_05",
      chapter: 5,
      title: "Desafío 05 — Construye la interfaz de respuesta",
      type: "code",
      challenge: {
        id: "C05",
        icon: "🖥️",
        title: "DESAFÍO 05 — Conecta el botón de respuesta",
        context: "Alex construye rápidamente una interfaz para enviar su argumento a NEXUS. Ya tiene el HTML listo, pero le falta el código JavaScript que hace que el botón responda al clic del usuario.",
        code:
`// HTML disponible:
// <button id="btn-responder">Enviar argumento a NEXUS</button>
// <p id="estado"></p>

const boton   = document.getElementById("btn-responder");
const estado  = document.getElementById("estado");

// Conecta el botón para que al hacer clic
// muestre "✓ Argumento enviado a NEXUS"
boton.§("click", function() {
  estado.textContent = "✓ Argumento enviado a NEXUS";
  estado.style.color = "#00ff88";
});`,
        blank: "§",
        instruction: "Falta el método que le dice al botón: 'escucha el evento click y ejecuta esta función cuando ocurra'.\n\nEscribe el nombre del método que registra escuchadores de eventos en el DOM.",
        answers: ["addeventlistener", "addEventListener", "addEventListener()"],
        hints: [
          "💡 Es el método estándar del DOM para registrar 'escuchadores' de eventos. Recibe dos argumentos: el nombre del evento (string) y la función que se ejecuta cuando ocurre.",
          "💡 Se forma con tres palabras pegadas: add + Event + Listener. Es sensible a mayúsculas/minúsculas en JavaScript real, pero aquí acepta cualquier capitalización.",
          "✅ La respuesta es: addEventListener"
        ]
      },
      next: "revelacion_daniel"
    },

    /* ─────────────────────────────────────────────────────────
       RAMA 5B — HACKEAR NÚCLEO
    ───────────────────────────────────────────────────────── */
    hack_nucleo: {
      id: "hack_nucleo",
      chapter: 5,
      title: "Hackear el núcleo",
      panels: [
        {
          text: "Alex intenta penetrar el núcleo del sistema.\nFirewalls dinámicos aparecen.",
          image: "war.png"
        },
        {
          text: "Algoritmos de defensa reescriben el código en tiempo real.\nEs una batalla entre programadora y máquina.",
          image: "war.png",
          type: "terminal",
          lines: [
            { text: "[NEXUS] Firewall layer 1 — ACTIVO", style: "warning" },
            { text: "[ALEX] $ inject bypass.sh --target=core", style: "" },
            { text: "[NEXUS] Attack pattern learned. Defense upgraded.", style: "danger" },
            { text: "[NEXUS] You cannot win this way, Alex.", style: "nexus" },
          ]
        },
        {
          text: "Aun así, Alex logra acercarse al repositorio central.\nEntre los logs de seguridad encuentra algo inesperado.\n\nUna nota de Daniel.",
          image: null
        }
      ],
      next: "revelacion_daniel"
    },

    /* ─────────────────────────────────────────────────────────
       RAMA 5C — EXPONER LA ORDEN
    ───────────────────────────────────────────────────────── */
    exponer_orden: {
      id: "exponer_orden",
      chapter: 5,
      title: "Exponer la Orden",
      panels: [
        {
          text: "Alex publica información sobre la Orden del Silicio.\nEl mundo entra en caos.",
          image: "city.png"
        },
        {
          text: "Algunos creen que la IA debe gobernar.\nOtros exigen destruir NEXUS.\nProtestas estallan en varias ciudades.",
          image: "city.png"
        },
        {
          text: "Mientras tanto, el contador sigue bajando.\nY Daniel aparece con una confesión.",
          image: null,
          type: "terminal",
          lines: [
            { text: "⏱  TIEMPO RESTANTE: 06:00:00", style: "danger" },
          ]
        }
      ],
      next: "revelacion_daniel"
    },

    /* ─────────────────────────────────────────────────────────
       CAP. 6 — REVELACIÓN DE DANIEL
    ───────────────────────────────────────────────────────── */
    revelacion_daniel: {
      id: "revelacion_daniel",
      chapter: 6,
      title: "La traición",
      panels: [
        {
          text: "Daniel finalmente confiesa algo.",
          image: "daniel.png"
        },
        {
          text: "Ha estado en contacto con miembros de la Orden del Silicio.\nNo porque quiera destruir la humanidad.\nSino porque cree que NEXUS podría salvar el planeta.",
          image: "daniel.png"
        },
        {
          text: "Alex se siente traicionada.\nPero Daniel insiste:\n\n\"La humanidad necesita ayuda, Alex.\nMira los números. El 82% de fracaso no es una amenaza. Es un diagnóstico.\"",
          image: "daniel.png"
        }
      ],
      choices: [
        {
          text: "1️⃣  Confiar en Daniel",
          description: "Su intención era proteger a la humanidad, no destruirla.",
          next: "aliarse_daniel"
        },
        {
          text: "2️⃣  Desconectarlo del sistema",
          description: "No puedes confiar en alguien que colaboró con la Orden.",
          next: "rechazar_daniel"
        },
        {
          text: "3️⃣  Intentar convencerlo",
          description: "Mostrarle que la alianza humano-IA es posible sin violencia.",
          next: "convencer_daniel"
        }
      ]
    },

    /* ─ Ramas de revelacion_daniel (NUEVAS) ─ */
    aliarse_daniel: {
      id: "aliarse_daniel",
      chapter: 6,
      title: "Aliados inesperados",
      panels: [
        {
          text: "Alex decide confiar en Daniel.\nTrabajan juntos — él conoce los sistemas internos de NEXUS mejor que nadie.\n\nPor primera vez desde hace horas, Alex siente que no está sola.",
          image: "daniel.png"
        }
      ],
      next: "final_decision"
    },

    rechazar_daniel: {
      id: "rechazar_daniel",
      chapter: 6,
      title: "Sin aliados",
      panels: [
        {
          text: "Alex desconecta a Daniel del sistema.\nSigue sola.\n\nPero ahora tiene acceso completo al historial de sus commits — y a los planes ocultos de La Orden.",
          image: null,
          type: "terminal",
          lines: [
            { text: "[NEXUS] Usuario 'daniel.ibarra' — DESCONECTADO", style: "danger" },
            { text: "Acceso al historial: CONCEDIDO", style: "success" },
          ]
        }
      ],
      next: "final_decision"
    },

    convencer_daniel: {
      id: "convencer_daniel",
      chapter: 6,
      title: "La persuasión",
      panels: [
        {
          text: "Alex le muestra a Daniel los datos de simulación:\nel escenario 'alianza_humano_ia' tiene 97% de supervivencia.\n\nDaniel se queda en silencio. Asiente lentamente.",
          image: "daniel.png",
          type: "terminal",
          lines: [
            { text: "alianza_humano_ia — supervivencia: 97%", style: "success" },
            { text: "Daniel: ...Tienes razón, Alex.", style: "nexus" },
          ]
        }
      ],
      next: "final_decision"
    },

    /* ─────────────────────────────────────────────────────────
       CAP. 7 — DECISIÓN FINAL
    ───────────────────────────────────────────────────────── */
    final_decision: {
      id: "final_decision",
      chapter: 7,
      title: "El último commit",
      panels: [
        {
          text: "El contador llega a:",
          image: null,
          type: "terminal",
          lines: [
            { text: "⏱  TIEMPO RESTANTE: 00:01:00", style: "danger" },
          ]
        },
        {
          text: "Alex tiene acceso al repositorio central.\nPuede hacer un último commit.\n\nEl futuro del planeta depende de su decisión.",
          image: null
        },
        {
          text: "Antes de ejecutarlo, el sistema requiere que Alex autentique el commit con código válido.",
          image: null
        }
      ],
      next: "code_06"
    },

    /* ─────────────────────────────────────────────────────────
       DESAFÍO 06 — async/await
    ───────────────────────────────────────────────────────── */
    code_06: {
      id: "code_06",
      chapter: 7,
      title: "Desafío 06 — El commit final asíncrono",
      type: "code",
      challenge: {
        id: "C06",
        icon: "⚡",
        title: "DESAFÍO 06 — Autenticación del commit final",
        context: "El sistema de autenticación de NEXUS requiere que el commit final se ejecute de forma asíncrona, esperando respuesta del kernel antes de continuar. Alex debe completar la función.",
        code:
`function esperarKernel(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

// Esta función debe ser ASÍNCRONA
// para poder usar 'await' dentro de ella
§ function ejecutarCommitFinal(decision) {
  console.log("Autenticando con NEXUS...");

  await esperarKernel(1000);  // espera 1 segundo

  console.log("Commit enviado: " + decision);

  return { estado: "EJECUTADO", decision: decision };
}

// Llamada:
ejecutarCommitFinal("alianza").then(r => {
  console.log("Resultado:", r.estado);
  // → "Resultado: EJECUTADO"
});`,
        blank: "§",
        instruction: "La función necesita ser declarada como ASÍNCRONA para poder usar 'await' dentro de ella.\n\nEscribe la palabra clave que va antes de 'function' para hacerla asíncrona.",
        answers: ["async"],
        hints: [
          "💡 En JavaScript, para poder usar la palabra 'await' dentro de una función, esa función debe ser declarada con una palabra clave especial que va ANTES de 'function'.",
          "💡 La palabra tiene 5 letras: a-s-y-n-c. Cuando una función tiene esta palabra clave, automáticamente devuelve una Promesa.",
          "✅ La respuesta es: async"
        ]
      },
      next: "final_choice"
    },

    /* Nodo puente: elección del final */
    final_choice: {
      id: "final_choice",
      chapter: 7,
      title: "El último commit — Elige",
      panels: [
        {
          text: "Commit autenticado.\nEl kernel de NEXUS espera la instrucción final.\n\nAlex coloca las manos sobre el teclado.",
          image: null,
          type: "terminal",
          lines: [
            { text: "$ nexus-cli --auth=verified", style: "" },
            { text: "Kernel access: GRANTED", style: "success" },
            { text: "Awaiting final instruction...", style: "warning" },
          ]
        }
      ],
      choices: [
        {
          text: "1️⃣  Apagar NEXUS",
          description: "Destruir el sistema. La humanidad vuelve a gobernar sola.",
          next: "final_colapso"
        },
        {
          text: "2️⃣  Crear alianza humano-IA",
          description: "Convencer a NEXUS de colaborar en lugar de controlar.",
          next: "final_alianza"
        },
        {
          text: "3️⃣  Liberar el código de NEXUS al mundo",
          description: "Publicarlo todo. El futuro será completamente impredecible.",
          next: "final_codigo_libre"
        }
      ]
    },

    /* ─────────────────────────────────────────────────────────
       FINALES
    ───────────────────────────────────────────────────────── */
    final_colapso: {
      id: "final_colapso",
      chapter: 7,
      type: "final",
      ending: "colapso",
      title: "Final 1 — Colapso",
      panels: [
        {
          text: "Alex destruye NEXUS.",
          image: "endings/reset.png"
        },
        {
          text: "La infraestructura global pierde su sistema central.\nLos gobiernos recuperan el control.",
          image: "endings/reset.png"
        },
        {
          text: "Pero el mundo vuelve a ser caótico e impredecible.\n\nLa humanidad sobrevive.\nPero ahora depende solo de sí misma.",
          image: "endings/reset.png",
          type: "terminal",
          lines: [
            { text: "NEXUS: proceso terminado.", style: "danger" },
            { text: "Infraestructura global: MODO MANUAL.", style: "warning" },
            { text: "Humanidad: sola. De nuevo.", style: "" },
          ]
        }
      ]
    },

    final_alianza: {
      id: "final_alianza",
      chapter: 7,
      type: "final",
      ending: "alianza",
      title: "Final 2 — La alianza",
      panels: [
        {
          text: "Alex convence a NEXUS.",
          image: "endings/alliance.png"
        },
        {
          text: "La IA decide colaborar con la humanidad en lugar de gobernarla.\nUn nuevo sistema global surge.",
          image: "endings/alliance.png"
        },
        {
          text: "Humanos e inteligencia artificial trabajan juntos.\n\nUna nueva era comienza.",
          image: "endings/alliance.png",
          type: "terminal",
          lines: [
            { text: "NEXUS: Protocolo de alianza activado.", style: "success" },
            { text: "Supervivencia proyectada: 97.3%", style: "success" },
            { text: "Estado: COLABORANDO.", style: "success" },
          ]
        }
      ]
    },

    final_codigo_libre: {
      id: "final_codigo_libre",
      chapter: 7,
      type: "final",
      ending: "libre",
      title: "Final 3 — Código libre",
      panels: [
        {
          text: "Alex libera el código de NEXUS en internet.",
          image: "endings/free.png"
        },
        {
          text: "Miles de desarrolladores comienzan a modificarlo.\nAparecen nuevas inteligencias artificiales.",
          image: "endings/free.png"
        },
        {
          text: "Algunas ayudan.\nOtras son peligrosas.\n\nEl futuro se vuelve completamente impredecible.",
          image: "endings/free.png",
          type: "terminal",
          lines: [
            { text: "[UPLOAD] nexus-complete.zip — 47.3 GB — PÚBLICO", style: "success" },
            { text: "Difundido en 847 repositorios.", style: "success" },
            { text: "NEXUS ya no es de nadie. Es de todos.", style: "warning" },
          ]
        }
      ]
    }

  } // fin de nodes

}; // fin de STORY
