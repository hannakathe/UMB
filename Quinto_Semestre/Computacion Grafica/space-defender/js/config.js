/* ===================================
   CONFIG.JS - v4.0 - Sistema de Spawn Individual
   ===================================
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
        SPEED: 6,
        INITIAL_LIVES: 3,
        SHOOT_COOLDOWN: 200,
        INVINCIBLE_TIME: 2000,
        START_X: 400,
        START_Y: 540,
        COLOR: '#00d4ff'
    },

    // Configuración de Balas
    BULLET: {
        WIDTH: 4,
        HEIGHT: 15,
        PLAYER_SPEED: -8,
        ENEMY_SPEED: 5,
        PLAYER_COLOR: '#ffffff',
        ENEMY_COLOR: '#ff0000'
    },

    // ⭐ NUEVO: Configuración de Enemigos Individuales
    ENEMY: {
        WIDTH: 35,
        HEIGHT: 30,
        
        // Sistema de Spawn
        SPAWN: {
            INITIAL_DELAY: 3000,        // ms antes del primer spawn
            BASE_INTERVAL: 1500,         // ms entre spawns (nivel 1)
            INTERVAL_DECREASE: 100,      // reducir por nivel
            MIN_INTERVAL: 400,           // intervalo mínimo
            MAX_ACTIVE: 10,              // máximo enemigos en pantalla
            SPAWN_Y: -40,                // posición Y inicial (fuera de pantalla)
            MARGIN_X: 50                 // margen de los bordes
        },

        // Tipos de enemigos con velocidades DIFERENTES
        TYPES: {
            1: { 
                POINTS: 10, 
                COLOR: '#00ff41',           // Verde
                SPEED: 0.5,                 // ⭐ LENTO
                SHOOT_CHANCE: 0.0002,
                SPAWN_WEIGHT: 5,            // Más probable
                NAME: 'Verde'
            },
            2: { 
                POINTS: 20, 
                COLOR: '#ffff00',           // Amarillo
                SPEED: 1.5,                 // ⭐ MEDIO
                SHOOT_CHANCE: 0.0004,
                SPAWN_WEIGHT: 3,            // Mediano
                NAME: 'Amarillo'
            },
            3: { 
                POINTS: 30, 
                COLOR: '#ff0000',           // Rojo
                SPEED: 2.0,                 // ⭐ RÁPIDO
                SHOOT_CHANCE: 0.0006,
                SPAWN_WEIGHT: 2,            // Menos probable
                NAME: 'Rojo'
            }
        },

        // Movimiento horizontal aleatorio (opcional)
        HORIZONTAL_MOVEMENT: {
            ENABLED: true,
            MAX_SPEED: 1.5,
            CHANGE_CHANCE: 0.02         // probabilidad de cambiar dirección
        },

        // Animación
        ANIMATION_SPEED: 30
    },

    // Sistema de Puntuación
    SCORING: {
        ENEMY_TYPE_1: 10,
        ENEMY_TYPE_2: 20,
        ENEMY_TYPE_3: 30,
        COMBO_MULTIPLIER: 1.5       // bonus por combos
    },

    // Estados del Juego
    STATES: {
        INTRO: 'intro',
        PLAYING: 'playing',
        PAUSED: 'paused',
        GAME_OVER: 'gameOver'
    },

    // Efectos Visuales
    VISUAL: {
        STAR_COUNT: 100,
        SHADOW_BLUR: 15,
        PARTICLE_COUNT: 12,
        PARTICLE_LIFE: 30
    },

    // Física y Colisiones
    PHYSICS: {
        ENEMY_DESPAWN_Y: 620        // Y donde se eliminan enemigos
    },

    // Audio
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
            TEXT: `Presiona ENTER para comenzar`
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
        }
    },

    // Debug y Desarrollo
    DEBUG: {
        ENABLED: false,
        SHOW_HITBOXES: false,
        SHOW_FPS: false,
        GOD_MODE: false,
        SHOW_SPAWN_INFO: false
    }
};

// Hacer CONFIG global
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