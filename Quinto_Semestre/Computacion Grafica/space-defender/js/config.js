/* ===================================
   CONFIG.JS - Constantes del Juego
   ===================================
   Configuración centralizada para fácil balance y ajustes.
*/

const CONFIG = {
    // Dimensiones del Canvas
    CANVAS: {
        WIDTH: 800,
        HEIGHT: 600
    },

    // Configuración del Jugador
    PLAYER: {
        WIDTH: 40,
        HEIGHT: 30,
        SPEED: 5,
        INITIAL_LIVES: 3,
        SHOOT_COOLDOWN: 250,        // milisegundos entre disparos
        INVINCIBLE_TIME: 2000,      // milisegundos de invencibilidad tras daño
        START_X: 400,               // posición inicial X (centro)
        START_Y: 540,               // posición inicial Y (cerca del fondo)
        COLOR: '#00d4ff'            // color azul brillante
    },

    // Configuración de Balas
    BULLET: {
        WIDTH: 4,
        HEIGHT: 15,
        PLAYER_SPEED: -7,           // velocidad hacia arriba (negativa)
        ENEMY_SPEED: 4,             // velocidad hacia abajo (positiva)
        PLAYER_COLOR: '#ffffff',    // blanco
        ENEMY_COLOR: '#ff0000'      // rojo
    },

    // Configuración de Enemigos
    ENEMY: {
        WIDTH: 35,
        HEIGHT: 30,
        BASE_SPEED: 1,              // velocidad base horizontal
        SPEED_INCREMENT: 0.2,       // incremento por nivel
        DROP_DISTANCE: 20,          // píxeles que bajan al tocar borde
        
        // Grid inicial de enemigos
        GRID: {
            ROWS: 3,                // filas iniciales
            COLS: 8,                // columnas
            SPACING_X: 70,          // espacio horizontal
            SPACING_Y: 60,          // espacio vertical
            START_X: 150,           // posición inicial X
            START_Y: 80,            // posición inicial Y
            ROWS_PER_LEVEL: 0.5     // filas adicionales cada 2 niveles
        },

        // Tipos de enemigos (fila determina tipo)
        TYPES: {
            1: { POINTS: 10, COLOR: '#00ff41', SHOOT_CHANCE: 0.0005 },  // Verde
            2: { POINTS: 20, COLOR: '#ffff00', SHOOT_CHANCE: 0.001 },   // Amarillo
            3: { POINTS: 30, COLOR: '#ff0000', SHOOT_CHANCE: 0.0015 }   // Rojo
        },

        // Animación
        ANIMATION_SPEED: 30         // frames entre cambios de sprite
    },

    // Sistema de Puntuación
    SCORING: {
        ENEMY_TYPE_1: 10,
        ENEMY_TYPE_2: 20,
        ENEMY_TYPE_3: 30
    },

    // Estados del Juego
    STATES: {
        MENU: 'menu',
        PLAYING: 'playing',
        PAUSED: 'paused',
        GAME_OVER: 'gameOver',
        LEVEL_COMPLETE: 'levelComplete'
    },

    // Efectos Visuales
    VISUAL: {
        STAR_COUNT: 100,            // número de estrellas de fondo
        SHADOW_BLUR: 15,            // blur de las sombras
        PARTICLE_COUNT: 12,         // partículas por explosión
        PARTICLE_LIFE: 30           // frames de vida de partícula
    },

    // Física y Colisiones
    PHYSICS: {
        INVASION_LINE: 500          // línea Y donde los enemigos invaden
    },

    // Audio (para futuras implementaciones)
    AUDIO: {
        ENABLED: false,
        VOLUME: {
            MASTER: 0.7,
            SFX: 0.8,
            MUSIC: 0.5
        }
    },

    // Almacenamiento Local
    STORAGE: {
        HIGH_SCORE_KEY: 'spaceDefenderHighScore'
    },

    // Mensajes del Juego
    MESSAGES: {
        START: {
            TITLE: 'SPACE DEFENDER',
            TEXT: `← → o A/D: Mover nave<br>
                   ESPACIO: Disparar<br>
                   P: Pausar<br><br>
                   Presiona ENTER para comenzar`
        },
        PAUSED: {
            TITLE: 'PAUSA',
            TEXT: 'Presiona P para continuar'
        },
        GAME_OVER: {
            TITLE: 'GAME OVER',
            TEXT: (score, highScore) => 
                `Puntuación Final: ${score}<br>
                 High Score: ${highScore}<br><br>
                 Presiona R para reiniciar`
        },
        LEVEL_COMPLETE: {
            TITLE: (level) => `¡NIVEL ${level}!`,
            TEXT: 'Prepárate...'
        }
    },

    // Debug y Desarrollo
    DEBUG: {
        ENABLED: false,             // mostrar información de debug
        SHOW_HITBOXES: false,       // mostrar cajas de colisión
        SHOW_FPS: false,            // mostrar FPS
        GOD_MODE: false             // invencibilidad permanente
    }
};

// Hacer CONFIG global y de solo lectura
if (typeof window !== 'undefined') {
    window.CONFIG = Object.freeze(CONFIG);
}

/* ===================================
   NOTAS DE USO:
   ===================================
   
   Para cambiar la dificultad:
   - Aumenta PLAYER.SPEED para nave más rápida
   - Reduce BULLET.PLAYER_SPEED para balas más lentas
   - Aumenta ENEMY.BASE_SPEED para enemigos más rápidos
   - Reduce PLAYER.SHOOT_COOLDOWN para disparar más rápido
   
   Para cambiar el balance:
   - Modifica SCORING para dar más/menos puntos
   - Ajusta ENEMY.TYPES.SHOOT_CHANCE para más/menos disparos enemigos
   - Cambia PLAYER.INITIAL_LIVES para más/menos vidas
   
   Para debugging:
   - Activa DEBUG.ENABLED para ver información
   - Activa DEBUG.SHOW_HITBOXES para ver colisiones
   - Activa DEBUG.GOD_MODE para no recibir daño
*/