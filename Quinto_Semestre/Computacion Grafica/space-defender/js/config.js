/* ===================================
   CONFIG.JS - Constantes del Juego v3.0
   ===================================
   Configuración centralizada con patrones de movimiento.
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
        SHOOT_COOLDOWN: 250,
        INVINCIBLE_TIME: 2000,
        START_X: 400,
        START_Y: 540,
        COLOR: '#00d4ff'
    },

    // Configuración de Balas
    BULLET: {
        WIDTH: 4,
        HEIGHT: 15,
        PLAYER_SPEED: -7,
        ENEMY_SPEED: 4,
        PLAYER_COLOR: '#ffffff',
        ENEMY_COLOR: '#ff0000'
    },

    // Configuración de Enemigos
    ENEMY: {
        WIDTH: 35,
        HEIGHT: 30,
        BASE_SPEED: 1,
        SPEED_INCREMENT: 0.15,  // Reducido para mejor control
        DROP_DISTANCE: 20,
        
        // Grid inicial
        GRID: {
            ROWS: 3,
            COLS: 8,
            SPACING_X: 70,
            SPACING_Y: 60,
            START_X: 150,
            START_Y: 80,
            ROWS_PER_LEVEL: 0.5
        },

        // Tipos de enemigos
        TYPES: {
            1: { POINTS: 10, COLOR: '#00ff41', SHOOT_CHANCE: 0.0003 },
            2: { POINTS: 20, COLOR: '#ffff00', SHOOT_CHANCE: 0.0006 },
            3: { POINTS: 30, COLOR: '#ff0000', SHOOT_CHANCE: 0.001 }
        },

        // ⭐ NUEVO: Patrones de movimiento
        MOVEMENT_PATTERNS: {
            // Nivel 1: Movimiento clásico (horizontal + descenso)
            CLASSIC: {
                name: 'classic',
                description: 'Patrón clásico Space Invaders',
                minLevel: 1,
                update: function(enemy, manager, time) {
                    // Lógica manejada por el manager
                }
            },
            
            // Nivel 2+: Movimiento con olas
            WAVE: {
                name: 'wave',
                description: 'Movimiento ondulatorio',
                minLevel: 2,
                amplitude: 15,
                frequency: 0.05,
                update: function(enemy, manager, time) {
                    const wave = Math.sin(time * this.frequency + enemy.gridCol * 0.3) * this.amplitude;
                    enemy.waveOffset = wave;
                }
            },
            
            // Nivel 3+: Movimiento en zigzag
            ZIGZAG: {
                name: 'zigzag',
                description: 'Movimiento en zigzag',
                minLevel: 3,
                zigzagSpeed: 2,
                update: function(enemy, manager, time) {
                    const zigzag = Math.sin(time * 0.1 + enemy.gridRow * 0.5) * this.zigzagSpeed;
                    enemy.zigzagOffset = zigzag;
                }
            },
            
            // Nivel 4+: Movimiento circular
            CIRCULAR: {
                name: 'circular',
                description: 'Órbitas circulares',
                minLevel: 4,
                radius: 10,
                speed: 0.03,
                update: function(enemy, manager, time) {
                    const angle = time * this.speed + enemy.gridIndex * 0.5;
                    enemy.circularOffsetX = Math.cos(angle) * this.radius;
                    enemy.circularOffsetY = Math.sin(angle) * this.radius;
                }
            },
            
            // Nivel 5+: Movimiento errático
            ERRATIC: {
                name: 'erratic',
                description: 'Movimiento impredecible',
                minLevel: 5,
                changeInterval: 60,  // Cambiar cada 60 frames
                maxOffset: 20,
                update: function(enemy, manager, time) {
                    if (!enemy.erraticTarget) {
                        enemy.erraticTarget = { x: 0, y: 0 };
                        enemy.erraticTimer = 0;
                    }
                    
                    enemy.erraticTimer++;
                    if (enemy.erraticTimer >= this.changeInterval) {
                        enemy.erraticTarget.x = (Math.random() - 0.5) * this.maxOffset;
                        enemy.erraticTarget.y = (Math.random() - 0.5) * this.maxOffset;
                        enemy.erraticTimer = 0;
                    }
                    
                    // Interpolar suavemente hacia el objetivo
                    if (!enemy.erraticOffset) enemy.erraticOffset = { x: 0, y: 0 };
                    enemy.erraticOffset.x += (enemy.erraticTarget.x - enemy.erraticOffset.x) * 0.1;
                    enemy.erraticOffset.y += (enemy.erraticTarget.y - enemy.erraticOffset.y) * 0.1;
                }
            }
        },

        // Animación
        ANIMATION_SPEED: 30
    },

    // Sistema de Puntuación
    SCORING: {
        ENEMY_TYPE_1: 10,
        ENEMY_TYPE_2: 20,
        ENEMY_TYPE_3: 30
    },

    // Estados del Juego
    STATES: {
        INTRO: 'intro',
        MENU: 'menu',
        PLAYING: 'playing',
        PAUSED: 'paused',
        GAME_OVER: 'gameOver',
        LEVEL_COMPLETE: 'levelComplete'
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
        INVASION_LINE: 500
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
        },
        LEVEL_COMPLETE: {
            TITLE: (level) => `¡NIVEL ${level}!`,
            TEXT: (patternName) => `Nuevo patrón: ${patternName}<br>Prepárate...`
        }
    },

    // Debug y Desarrollo
    DEBUG: {
        ENABLED: false,
        SHOW_HITBOXES: false,
        SHOW_FPS: false,
        GOD_MODE: false
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