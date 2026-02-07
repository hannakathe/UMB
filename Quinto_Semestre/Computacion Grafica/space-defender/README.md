# 🚀 SPACE DEFENDER

<div align="center">

![Space Defender Banner](https://img.shields.io/badge/Space-Defender-00d4ff?style=for-the-badge&logo=rocket)
![Version](https://img.shields.io/badge/version-5.1-success?style=for-the-badge)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![License](https://img.shields.io/badge/license-MIT-blue?style=for-the-badge)

**Un juego arcade 2D moderno desarrollado con HTML5 Canvas puro**

[Demo en Vivo](#) • [Reportar Bug](../../issues) • [Solicitar Feature](../../issues)

</div>

---

## 📋 Tabla de Contenidos

- [Acerca del Proyecto](#-acerca-del-proyecto)
- [Características](#-características)
- [Capturas de Pantalla](#-capturas-de-pantalla)
- [Instalación](#-instalación)
- [Uso](#-uso)
- [Controles](#-controles)
- [Arquitectura](#-arquitectura)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Tecnologías](#-tecnologías)
- [Configuración](#-configuración)
- [Sistema de Audio](#-sistema-de-audio)
- [Desarrollo](#-desarrollo)
- [Roadmap](#-roadmap)
- [Contribución](#-contribución)
- [Licencia](#-licencia)
- [Contacto](#-contacto)
- [Agradecimientos](#-agradecimientos)

---

## 🎮 Acerca del Proyecto

**Space Defender** es un juego arcade 2D tipo shoot 'em up desarrollado completamente con tecnologías web nativas (HTML5, CSS3, JavaScript ES6+). El proyecto demuestra el uso de Canvas API para gráficos 2D, implementación de game loops, sistemas de colisiones, y diseño responsive.

### Contexto

Desarrollado como proyecto de laboratorio de Computación Gráfica, Space Defender combina conceptos de programación orientada a objetos, matemáticas aplicadas (trigonometría, vectores), y diseño de interfaces para crear una experiencia de juego completa y profesional.

### Objetivo

Crear un videojuego 2D completamente funcional que demuestre:
- Dominio de Canvas API 2D
- Implementación de patrones de diseño
- Sistemas de física y colisiones
- Arquitectura modular escalable
- Diseño responsive multiplataforma

---

## ✨ Características

### Gameplay

- 🎯 **Sistema de Spawn Individual** - Enemigos aparecen progresivamente uno por uno
- 🌊 **5 Patrones de Movimiento Únicos** - Cada nivel introduce un nuevo patrón de movimiento enemigo
- 🎨 **3 Tipos de Enemigos** - Velocidades y puntuaciones diferenciadas por color
- 💥 **Sistema de Colisiones Mejorado** - Detección continua con hitboxes expandidas (+123% área)
- ⭐ **Sistema de Niveles Progresivos** - Dificultad incremental con nuevos patrones
- 💎 **Sistema de Puntuación** - High score persistente con LocalStorage
- ❤️ **Sistema de Vidas** - 3 vidas con invencibilidad temporal al recibir daño

### Técnicas

- 📱 **100% Responsive** - Adaptable a desktop, tablet y móvil
- 🎵 **Sistema de Audio Completo** - Música de fondo y efectos de sonido
- 🎚️ **Controles de Volumen en Tiempo Real** - Ajusta Master, SFX y Music independientemente
- 🔊 **Sistema de Audio No Bloqueante** - Audio completamente asíncrono que nunca afecta el gameplay
- ⚡ **Optimizado para 60 FPS** - Game loop con requestAnimationFrame
- 🎨 **Efectos Visuales** - Partículas, sombras, campo de estrellas animado
- 🐛 **Modo Debug** - Visualización de hitboxes, FPS, y estado del juego
- 💾 **Persistencia de Datos** - High score guardado localmente

### Patrones de Movimiento

| Nivel | Patrón | Descripción |
|-------|--------|-------------|
| **1** | Clásico | Descenso vertical simple |
| **2** | Ondas | Movimiento sinusoidal horizontal |
| **3** | Zigzag | Oscilación rápida en zigzag |
| **4** | Circular | Órbitas circulares alrededor de un punto base |
| **5+** | Errático | Movimiento aleatorio impredecible |

---

## 📸 Capturas de Pantalla

### Pantalla de Inicio
![Intro Screen](./screenshots/01-intro.png)
*Pantalla cinematográfica de introducción con historia del juego*

### Gameplay
<div align="center">
  <img src="./screenshots/02-nivel1-clasico.png" width="400" alt="Nivel 1"/>
  <img src="./screenshots/03-nivel2-ondas.png" width="400" alt="Nivel 2"/>
</div>

*Nivel 1 (Clásico) y Nivel 2 (Ondas)*

<div align="center">
  <img src="./screenshots/05-nivel4-circular.png" width="400" alt="Nivel 4"/>
  <img src="./screenshots/06-nivel5-erratico.png" width="400" alt="Nivel 5"/>
</div>

*Nivel 4 (Circular) y Nivel 5 (Errático)*

### Modo Debug
![Debug Mode](./screenshots/07-debug-hitboxes.png)
*Modo debug mostrando hitboxes expandidas y puntos de colisión*



---

## 🚀 Instalación

### Requisitos Previos

- Navegador moderno (Chrome, Firefox, Safari, Edge)
- Servidor web local (opcional, recomendado)

### Opción 1: Servidor Local con Python

```bash
# Clona el repositorio
git clone https://github.com/tuusuario/space-defender.git

# Navega al directorio
cd space-defender-v5.1

# Inicia servidor HTTP con Python 3
python -m http.server 8000

# O con Python 2
python -m SimpleHTTPServer 8000

# Abre en navegador
# http://localhost:8000
```

### Opción 2: Servidor Local con Node.js

```bash
# Instala http-server globalmente
npm install -g http-server

# Navega al directorio
cd space-defender-v5.1

# Inicia el servidor
http-server -p 8000

# Abre en navegador
# http://localhost:8000
```

### Opción 3: Live Server (VS Code)

```bash
1. Instala la extensión "Live Server" en VS Code
2. Abre el proyecto en VS Code
3. Click derecho en index.html
4. Selecciona "Open with Live Server"
```

### Opción 4: Abrir Directamente

```bash
# Simplemente abre el archivo en tu navegador
# Doble click en: index.html

# Nota: Algunas features pueden no funcionar sin servidor
```

---

## 🎯 Uso

### Inicio Rápido

1. **Abre el juego** en tu navegador
2. **Lee la historia** en la pantalla de introducción
3. **Presiona ENTER** para comenzar
4. **Usa las teclas** para controlar tu nave
5. **¡Defiende la Tierra!**

### Flujo del Juego

```
INTRO → ENTER → GAMEPLAY → PAUSE (P) → RESUME (P)
                    ↓
              GAME OVER → RESTART (R) → GAMEPLAY
```

---

## 🕹️ Controles

### Teclado

| Tecla | Acción |
|-------|--------|
| `←` o `A` | Mover nave a la izquierda |
| `→` o `D` | Mover nave a la derecha |
| `SPACE` | Disparar |
| `P` | Pausar/Reanudar juego |
| `R` | Reiniciar (en Game Over) |
| `ENTER` | Iniciar juego (en Intro) |

### Controles de Volumen

Los controles de volumen están disponibles en el **panel derecho** durante el juego:

- **Master**: Volumen general (afecta todo)
- **Efectos**: Solo efectos de sonido
- **Música**: Solo música de fondo
- **Botón MUTE**: Silenciar/activar todo

---

## 🏗️ Arquitectura

### Patrón de Diseño

Space Defender utiliza una arquitectura **modular basada en componentes** con separación clara de responsabilidades:

```
┌─────────────────────────────────────────┐
│           GAME (Orquestador)            │
│  - Gestiona estados del juego           │
│  - Coordina entidades y sistemas        │
│  - Maneja input del usuario             │
└──────────────┬──────────────────────────┘
               │
    ┌──────────┼──────────┬──────────┐
    │          │          │          │
┌───▼───┐  ┌──▼───┐  ┌───▼────┐  ┌──▼──────┐
│Player │  │Enemy │  │Bullet  │  │Systems  │
│       │  │      │  │        │  │         │
└───────┘  └──┬───┘  └────────┘  └──┬──────┘
              │                      │
         ┌────▼─────┐           ┌───▼──────┐
         │  Enemy   │           │Collision │
         │ Spawner  │           │  Audio   │
         └──────────┘           └──────────┘
```

### Sistemas Principales

#### 1. Game Loop

```javascript
function gameLoop(currentTime) {
    // 1. Actualizar lógica
    game.update();
    
    // 2. Renderizar
    game.draw();
    
    // 3. Siguiente frame
    requestAnimationFrame(gameLoop);
}
```

#### 2. Sistema de Estados

```javascript
STATES = {
    INTRO: 'intro',       // Pantalla de introducción
    PLAYING: 'playing',   // Jugando activamente
    PAUSED: 'paused',     // Juego pausado
    GAME_OVER: 'gameOver' // Fin del juego
}
```

#### 3. Sistema de Colisiones

- **AABB (Axis-Aligned Bounding Box)**: Detección básica
- **Continuous Collision Detection**: Previene tunneling
- **Hitboxes Expandidas**: +8px de margen para mejor gameplay

#### 4. Sistema de Audio

- **Pool de Sonidos**: Para efectos que se repiten frecuentemente
- **Ejecución Asíncrona**: No bloquea el game loop
- **Manejo de Errores**: Funciona sin archivos de audio

---

## 📁 Estructura del Proyecto

```
space-defender-v5.1/
│
├── index.html                    # Punto de entrada HTML
│
├── css/
│   └── style.css                # Estilos responsive completos
│
├── js/
│   ├── config.js                # Configuración centralizada
│   │
│   ├── core/                    # Lógica principal del juego
│   │   ├── main.js             # Inicialización y game loop
│   │   └── game.js             # Clase principal del juego
│   │
│   ├── entities/                # Objetos del juego
│   │   ├── player.js           # Nave del jugador
│   │   ├── enemy.js            # Enemigos y EnemySpawner
│   │   └── bullet.js           # Proyectiles
│   │
│   └── systems/                 # Sistemas reutilizables
│       ├── collision.js        # Detección de colisiones
│       └── audio.js            # Sistema de audio
│
├── assets/                      # Recursos multimedia
│   └── sounds/
│       ├── sfx/                # Efectos de sonido
│       └── music/              # Música de fondo
│
├── screenshots/                 # Capturas de pantalla
│
├── docs/                        # Documentación adicional
│   ├── Modelado y Juegos 2D Lab 1.docs          # Game Design Document
│
└── README.md                    # Este archivo

```

### Archivos Clave

| Archivo | Descripción | LOC |
|---------|-------------|-----|
| `config.js` | Configuración global del juego | ~265 |
| `game.js` | Lógica principal y orquestación | ~600 |
| `player.js` | Nave del jugador | ~260 |
| `enemy.js` | Enemigos y sistema de spawn | ~480 |
| `collision.js` | Sistema de colisiones | ~250 |
| `audio.js` | Sistema de audio | ~285 |
| **Total** | | **~2,500** |

---

## 🛠️ Tecnologías

### Frontend

- **HTML5** - Estructura semántica
- **CSS3** - Estilos responsive con Flexbox y Grid
- **JavaScript ES6+** - Lógica del juego

### APIs Utilizadas

- **Canvas API** - Renderización 2D
- **Web Audio API** (indirecto vía HTML5 Audio) - Sistema de audio
- **LocalStorage API** - Persistencia de high score
- **requestAnimationFrame** - Game loop optimizado

### Características de JavaScript

- ✅ Clases (ES6)
- ✅ Arrow Functions
- ✅ Template Literals
- ✅ Destructuring
- ✅ Promises
- ✅ Async/Await patterns
- ✅ Modules pattern

### CSS Moderno

- ✅ CSS Variables (Custom Properties)
- ✅ Flexbox
- ✅ Media Queries
- ✅ clamp() para responsive
- ✅ Animaciones CSS
- ✅ Transforms y Transitions

---

## ⚙️ Configuración

### Archivo config.js

Toda la configuración del juego está centralizada en `js/config.js`:

```javascript
const CONFIG = {
    // Canvas
    CANVAS: {
        BASE_WIDTH: 800,
        BASE_HEIGHT: 600,
        ASPECT_RATIO: 4/3
    },
    
    // Jugador
    PLAYER: {
        SPEED: 6,
        LIVES: 3,
        SHOOT_COOLDOWN: 200
    },
    
    // Enemigos
    ENEMY: {
        HITBOX_EXPANSION: 8,
        TYPES: {
            1: { SPEED: 1.5, COLOR: '#00ff41', POINTS: 10 },
            2: { SPEED: 2.5, COLOR: '#ffff00', POINTS: 20 },
            3: { SPEED: 4.0, COLOR: '#ff0000', POINTS: 30 }
        },
        SPAWN: {
            BASE_INTERVAL: 1500,
            MAX_ACTIVE: 15
        }
    },
    
    // Audio
    AUDIO: {
        ENABLED: true,
        VOLUME: {
            MASTER: 0.7,
            SFX: 0.8,
            MUSIC: 0.5
        }
    },
    
    // Debug
    DEBUG: {
        ENABLED: false,
        SHOW_HITBOXES: false,
        SHOW_FPS: false
    }
};
```

### Opciones Configurables

#### Dificultad

```javascript
// Fácil
ENEMY: {
    HITBOX_EXPANSION: 12,    // Hitboxes más grandes
    SPAWN: {
        BASE_INTERVAL: 2000,  // Spawn más lento
        MAX_ACTIVE: 10        // Menos enemigos
    }
}

// Difícil
ENEMY: {
    HITBOX_EXPANSION: 4,
    SPAWN: {
        BASE_INTERVAL: 1000,
        MAX_ACTIVE: 20
    }
}
```

#### Velocidad del Juego

```javascript
PLAYER: {
    SPEED: 8           // Más rápido
},
BULLET: {
    PLAYER_SPEED: -10  // Balas más rápidas
}
```

#### Debug Mode

```javascript
DEBUG: {
    ENABLED: true,
    SHOW_HITBOXES: true,     // Ver cajas de colisión
    SHOW_FPS: true,          // Mostrar FPS
    SHOW_PATTERN_INFO: true, // Mostrar patrón actual
    GOD_MODE: false          // Invencibilidad
}
```

---

## 🎵 Sistema de Audio

### Estructura

```
assets/sounds/
├── sfx/                     # Efectos de sonido (8 archivos)
│   ├── player-shoot.mp3
│   ├── enemy-shoot.mp3
│   ├── explosion.mp3
│   ├── player-hit.mp3
│   ├── game-over.mp3
│   ├── level-up.mp3
│   └── game-start.mp3
│
└── music/                   # Música de fondo (1 archivo)
    └── gameplay.mp3
```


#### Sitios Recomendados

1. **Freesound.org** - Efectos de sonido gratuitos
2. **Incompetech.com** - Música libre de regalías (Kevin MacLeod)
3. **OpenGameArt.org** - Assets específicos para juegos
4. **Kenney.nl** - Packs de sonido completos

### Desactivar Audio

Si no quieres usar audio:

```javascript
// En js/config.js
AUDIO: {
    ENABLED: false  // ← Cambia a false
}
```

El juego funcionará perfectamente sin archivos de audio.

---

## 👨‍💻 Desarrollo

### Configuración del Entorno

```bash
# Clona el repositorio
git clone https://github.com/tuusuario/space-defender.git
cd space-defender-v5.1

# No requiere instalación de dependencias
# Todo está en JavaScript vanilla
```

### Herramientas Recomendadas

- **Editor**: Visual Studio Code
- **Extensiones**:
  - Live Server
  - Prettier
  - ESLint
- **Navegador**: Chrome (mejores DevTools)

### Modo Debug

Activa el modo debug en `config.js`:

```javascript
DEBUG: {
    ENABLED: true,
    SHOW_HITBOXES: true,
    SHOW_FPS: true,
    SHOW_PATTERN_INFO: true,
    GOD_MODE: true  // Invencibilidad para testing
}
```

### Testing

```bash
# Prueba en diferentes navegadores
- Chrome/Edge (Chromium)
- Firefox
- Safari

# Prueba en diferentes dispositivos
- Desktop (1920x1080, 1366x768)
- Tablet (768x1024)
- Móvil (375x667, 414x896)
```

### Performance Profiling

```javascript
// En consola del navegador
console.time('gameLoop');
game.update();
game.draw();
console.timeEnd('gameLoop');

// Objetivo: < 16.67ms (60 FPS)
```



## 🤝 Contribución

Las contribuciones son bienvenidas y apreciadas. Para contribuir:

### Proceso

1. **Fork** el proyecto
2. **Crea** una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abre** un Pull Request

### Guías de Estilo

#### JavaScript

```javascript
// Usa camelCase para variables y funciones
const myVariable = 10;
function myFunction() { }

// Usa PascalCase para clases
class MyClass { }

// Comenta funciones complejas
/**
 * Descripción de la función
 * @param {type} param - Descripción
 * @returns {type} Descripción
 */
```

#### Commits

```bash
# Formato: tipo(ámbito): descripción

feat(player): add double jump ability
fix(collision): resolve tunneling issue
docs(readme): update installation guide
style(css): improve responsive layout
refactor(audio): simplify playSound method
```

### Reportar Bugs

Usa el sistema de [Issues](../../issues) con la plantilla:

```markdown
**Descripción del bug**
Descripción clara y concisa

**Pasos para reproducir**
1. Ir a '...'
2. Click en '...'
3. Ver error

**Comportamiento esperado**
Lo que debería suceder

**Capturas**
Si aplica, añade capturas

**Entorno**
- SO: [Windows 10]
- Navegador: [Chrome 120]
- Versión: [5.1]
```

-

## 📞 Contacto

**[Tu Nombre]** - [albrilhanna6@gmail.com](albrilhanna6@gmail.com)

**GitHub**: [@hannakathe](https://github.com/hannakathe)

**Proyecto**: [https://github.com/tuusuario/space-defender](https://github.com/hannakathe/UMB/tree/main/Quinto_Semestre/Computacion%20Grafica/space-defender)


## 🎓 Uso Educativo

Este proyecto es ideal para:

- 📚 Aprender Canvas API
- 🎮 Introducción al desarrollo de juegos
- 💻 Practicar JavaScript ES6+
- 🏗️ Entender arquitecturas modulares
- 📐 Aplicar matemáticas (vectores, trigonometría)
- 🎨 Diseño responsive

### Conceptos Demostrados

- ✅ Game loops con requestAnimationFrame
- ✅ Detección de colisiones AABB
- ✅ Continuous collision detection
- ✅ Programación orientada a objetos
- ✅ Patrones de diseño (Strategy, Pool)
- ✅ Event handling
- ✅ State management
- ✅ Asset loading
- ✅ LocalStorage API
- ✅ Responsive design

---

## 📝 Changelog

### [5.1] - 2026-02-06

#### Added
- Sistema de audio completamente no bloqueante
- Controles de volumen en tiempo real (Master, SFX, Music)
- Botón MUTE global
- Protección multicapa contra errores de audio
- Documentación exhaustiva

#### Fixed
- Bug crítico: juego se congelaba al disparar
- Bug: nave se salía del borde derecho
- Bug: mensajes de pausa/game over no aparecían
- Bug: reinicio no funcionaba correctamente

#### Changed
- playSound() reescrito con requestAnimationFrame
- Todas las llamadas de audio usan Promise.resolve()
- Hitbox margin aumentado de 5px a 10px

### [5.0] - 2026-02-05

#### Added
- 5 patrones de movimiento por nivel
- Sistema responsive completo
- Canvas escalable con aspect ratio
- UI adaptable con clamp()

### [4.0] - 2026-02-04

#### Added
- Sistema de spawn individual
- Velocidades diferenciadas por color
- Spawn continuo durante partida

#### Removed
- Movimiento en bloque (estilo Space Invaders clásico)

### Versiones anteriores
Ver documentación adicional para historia completa


---

<div align="center">

**Hecho con ❤️ y ☕**

[Volver arriba ⬆️](#-space-defender)

</div>