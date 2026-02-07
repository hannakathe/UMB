/* ===================================
   CONFIG.JS - v5.0
   ===================================
   Con patrones de movimiento individuales + Responsive
*/

const CONFIG = {
    // Dimensiones Base del Canvas (se escala automáticamente)
    CANVAS: {
        BASE_WIDTH: 800,
        BASE_HEIGHT: 600,
        ASPECT_RATIO: 800 / 600  // 4:3
    },

    // Configuración del Jugador
    PLAYER: {
        WIDTH: 40,
        HEIGHT: 30,
        SPEED: 6,
        INITIAL_LIVES: 8,
        SHOOT_COOLDOWN: 200,
        INVINCIBLE_TIME: 2000,
        START_X_PERCENT: 0.5,  // 50% del ancho
        START_Y_PERCENT: 0.9,  // 90% del alto
        COLOR: '#00d4ff'
    },

    // Configuración de Balas
    BULLET: {
        WIDTH: 4,
        HEIGHT: 15,
        PLAYER_SPEED: -6,          // Reducido de -8 a -6 (menos tunneling)
        ENEMY_SPEED: 4,             // Reducido de 5 a 4
        PLAYER_COLOR: '#ffffff',
        ENEMY_COLOR: '#ff0000'
    },

    // Configuración de Enemigos
    ENEMY: {
        WIDTH: 35,
        HEIGHT: 30,
        HITBOX_EXPANSION: 8,        // Píxeles extra en cada lado
        
        // Sistema de Spawn
        SPAWN: {
            INITIAL_DELAY: 1000,
            BASE_INTERVAL: 2000,
            INTERVAL_DECREASE: 100,
            MIN_INTERVAL: 400,
            MAX_ACTIVE: 10,
            SPAWN_Y: -40,
            MARGIN_X_PERCENT: 0.06  // 6% del ancho como margen
        },

        // Tipos de enemigos
        TYPES: {
            1: { 
                POINTS: 10, 
                COLOR: '#00ff41',
                SPEED: 0.2,
                SHOOT_CHANCE: 0.002,  // Aumentado
                SPAWN_WEIGHT: 5,
                NAME: 'Verde'
            },
            2: { 
                POINTS: 20, 
                COLOR: '#ffff00',
                SPEED: 0.8,
                SHOOT_CHANCE: 0.004,  // Aumentado
                SPAWN_WEIGHT: 3,
                NAME: 'Amarillo'
            },
            3: { 
                POINTS: 30, 
                COLOR: '#ff0000',
                SPEED: 1.2,
                SHOOT_CHANCE: 0.007,  // Aumentado
                SPAWN_WEIGHT: 2,
                NAME: 'Rojo'
            }
        },

        // PATRONES DE MOVIMIENTO INDIVIDUALES
        MOVEMENT_PATTERNS: {
            // Nivel 1: Solo vertical (clásico)
            CLASSIC: {
                name: 'classic',
                description: 'Descenso Directo',
                minLevel: 1,
                apply: function(enemy, time) {
                    // Solo movimiento vertical (ya manejado en update)
                    return { offsetX: 0, offsetY: 0 };
                }
            },
            
            // Nivel 2: Movimiento ondulatorio
            WAVE: {
                name: 'wave',
                description: 'Ondas Sinusoidales',
                minLevel: 2,
                amplitude: 30,
                frequency: 0.04,
                apply: function(enemy, time) {
                    const wave = Math.sin(time * this.frequency + enemy.spawnTime * 0.3) * this.amplitude; // Agregado spawnTime para variar fases
                    return { offsetX: wave, offsetY: 0 }; // Solo afecta el desplazamiento horizontal
                }
            },
            
            // Nivel 3: Zigzag
            ZIGZAG: {
                name: 'zigzag',
                description: 'Movimiento en Zigzag',
                minLevel: 3,
                amplitude: 2.5,
                frequency: 0.08,
                apply: function(enemy, time) {
                    const zigzag = Math.sin(time * this.frequency + enemy.id) * this.amplitude; // Agregado id para variar fases
                    return { offsetX: zigzag, offsetY: 0 }; // Solo afecta el desplazamiento horizontal
                }
            },
            
            // Nivel 4: Circular/Espiral
            CIRCULAR: {
                name: 'circular',
                description: 'Órbitas Circulares',
                minLevel: 4,
                radius: 15,
                speed: 0.05,
                apply: function(enemy, time) {
                    const angle = time * this.speed + enemy.id;
                    const offsetX = Math.cos(angle) * this.radius; // Movimiento circular horizontal
                    const offsetY = Math.sin(angle) * this.radius * 0.3; // Aplastado
                    return { offsetX, offsetY };
                }
            },
            
            // Nivel 5+: Errático
            ERRATIC: {
                name: 'erratic',
                description: 'Movimiento Caótico',
                minLevel: 5,
                changeInterval: 60,
                maxOffset: 25,
                apply: function(enemy, time) {
                    if (!enemy.erraticTarget) { // Inicializar propiedades para movimiento errático
                        enemy.erraticTarget = { x: 0, y: 0 }; // Objetivo de desplazamiento actual
                        enemy.erraticTimer = 0; // Temporizador para cambiar objetivo
                    }
                    
                    enemy.erraticTimer++;
                    if (enemy.erraticTimer >= this.changeInterval) { // Cambiar objetivo cada cierto tiempo
                        enemy.erraticTarget.x = (Math.random() - 0.5) * this.maxOffset * 2; // Rango completo de -maxOffset a +maxOffset
                        enemy.erraticTarget.y = (Math.random() - 0.5) * this.maxOffset; // Menos movimiento vertical para evitar que se alejen demasiado
                        enemy.erraticTimer = 0; // Reiniciar temporizador
                    }
                    
                    if (!enemy.erraticCurrent) enemy.erraticCurrent = { x: 0, y: 0 }; // Posición actual suavizada
                    enemy.erraticCurrent.x += (enemy.erraticTarget.x - enemy.erraticCurrent.x) * 0.1; // Suavizado para movimiento más fluido
                    enemy.erraticCurrent.y += (enemy.erraticTarget.y - enemy.erraticCurrent.y) * 0.1; // Suavizado para movimiento más fluido
                    
                    return { 
                        offsetX: enemy.erraticCurrent.x,  // Desplazamiento horizontal errático
                        offsetY: enemy.erraticCurrent.y   // Desplazamiento vertical errático (más suave para evitar que se alejen demasiado)
                    };
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

    // Física
    PHYSICS: {
        ENEMY_DESPAWN_Y_PERCENT: 1.03  // 103% del alto
    },

    // Audio
    AUDIO: {
        ENABLED: true,
        VOLUME: {
            MASTER: 0.7,   
            SFX: 0.8,      
            MUSIC: 0.5     
        },

        
        // Rutas de efectos de sonido
        SOUNDS: {
            playerShoot: 'assets/sounds/sfx/player-shoot.mp3',
            enemyShoot: 'assets/sounds/sfx/enemy-shoot.mp3',
            explosion: 'assets/sounds/sfx/explosion.mp3',
            playerHit: 'assets/sounds/sfx/player-hit.mp3',
            gameOver: 'assets/sounds/sfx/game-over.mp3',
            levelUp: 'assets/sounds/sfx/level-up.mp3',
            uiClick: 'assets/sounds/sfx/ui-click.mp3',
            gameStart: 'assets/sounds/sfx/game-start.mp3'
        },
        // Música de fondo
        MUSIC: {
            gameplay: 'assets/sounds/music/gameplay.mp3'
        }
    },

    // Almacenamiento
    STORAGE: {
        HIGH_SCORE_KEY: 'spaceDefenderHighScore'
    },

    // Mensajes
    MESSAGES: {
        START: {
            TITLE: 'SPACE DEFENDER',
            TEXT: 'Presiona ENTER para comenzar'
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

    // Debug
    DEBUG: {
        ENABLED: false,
        SHOW_HITBOXES: false,       
        SHOW_FPS: false,
        GOD_MODE: false,
        SHOW_PATTERN_INFO: false
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