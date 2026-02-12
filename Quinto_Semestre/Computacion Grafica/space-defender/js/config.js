/* ===================================
   CONFIG.JS - Configuración Centralizada para Space Defender
     - Contiene todas las constantes y parámetros del juego
     - Facilita ajustes de dificultad, balance y depuración
     - Mejora la mantenibilidad al centralizar valores clave
   ===================================
*/

const CONFIG = {
    // Dimensiones Base del Canvas (se escala automáticamente)
    CANVAS: {
        BASE_WIDTH: 800,
        BASE_HEIGHT: 600,
        ASPECT_RATIO: 800 / 600  
    },

    // Configuración del Jugador
    PLAYER: {
        WIDTH: 40,
        HEIGHT: 30,
        SPEED: 6,
        INITIAL_LIVES: 4,
        SHOOT_COOLDOWN: 200,
        INVINCIBLE_TIME: 2000,
        START_X_PERCENT: 0.5,  
        START_Y_PERCENT: 0.9,  
        COLOR: '#00d4ff'
    },

    // Configuración de Balas
    BULLET: {
        WIDTH: 4,
        HEIGHT: 15,
        PLAYER_SPEED: -6,
        ENEMY_SPEED: 4,
        PLAYER_COLOR: '#ffffff',
        ENEMY_COLOR: '#ff0000'
    },

    // Configuración de Enemigos
    ENEMY: {
        WIDTH: 35,
        HEIGHT: 30,
        HITBOX_EXPANSION: 4,
        
        // Sistema de Spawn
        SPAWN: {
            INITIAL_DELAY: 500,
            BASE_INTERVAL: 1000,
            INTERVAL_DECREASE: 100,
            MIN_INTERVAL: 400,
            MAX_ACTIVE_BASE: 15,        // Nivel 1 = 15 enemigos
            MAX_ACTIVE_PER_LEVEL: 3,    // +3 enemigos por nivel
            MAX_ACTIVE_CAP: 35,          // Máximo absoluto = 35 enemigos            
            SPAWN_Y: -40,
            MARGIN_X_PERCENT: 0.06
        },

        // Tipos de enemigos
        TYPES: {
            1: { 
                POINTS: 10, 
                COLOR: '#00ff41',
                SPEED: 2,           
                SHOOT_CHANCE: 0.002,
                SPAWN_WEIGHT: 5,
                NAME: 'Verde'
            },
            2: { 
                POINTS: 20, 
                COLOR: '#ffff00',
                SPEED: 2.5,        
                SHOOT_CHANCE: 0.004,
                SPAWN_WEIGHT: 3,
                NAME: 'Amarillo'
            },
            3: { 
                POINTS: 30, 
                COLOR: '#ff0000',
                SPEED: 3,         
                SHOOT_CHANCE: 0.007,
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
                    return { offsetX: 0, offsetY: 0 };
                }
            },
            
            // Nivel 2: Movimiento ondulatorio
            WAVE: {
                name: 'wave',
                description: 'Ondas Sinusoidales',
                minLevel: 2,
                amplitude: 50,     
                frequency: 0.04,
                apply: function(enemy, time) {
                    const wave = Math.sin(time * this.frequency + enemy.spawnTime * 0.3) * this.amplitude;
                    return { offsetX: wave, offsetY: 0 };
                }
            },
            
            // Nivel 3: Zigzag
            ZIGZAG: {
                name: 'zigzag',
                description: 'Movimiento en Zigzag',
                minLevel: 3,
                amplitude: 5.0,    
                frequency: 0.12,    
                apply: function(enemy, time) {
                    const zigzag = Math.sin(time * this.frequency + enemy.id) * this.amplitude;
                    return { offsetX: zigzag, offsetY: 0 };
                }
            },
            
            // Nivel 4: Circular/Espiral
            CIRCULAR: {
                name: 'circular',
                description: 'Órbitas Circulares',
                minLevel: 4,
                radius: 25,        
                speed: 0.05,
                apply: function(enemy, time) {
                    const angle = time * this.speed + enemy.id;
                    const offsetX = Math.cos(angle) * this.radius;
                    const offsetY = Math.sin(angle) * this.radius * 0.3;
                    return { offsetX, offsetY };
                }
            },
            
            // Nivel 5+: Errático
            ERRATIC: {
                name: 'erratic',
                description: 'Movimiento Caótico',
                minLevel: 5,
                changeInterval: 40,  
                maxOffset: 45,      
                apply: function(enemy, time) {
                    if (!enemy.erraticTarget) {
                        enemy.erraticTarget = { x: 0, y: 0 };
                        enemy.erraticTimer = 0;
                    }
                    
                    enemy.erraticTimer++;
                    if (enemy.erraticTimer >= this.changeInterval) {
                        enemy.erraticTarget.x = (Math.random() - 0.5) * this.maxOffset * 2;
                        enemy.erraticTarget.y = (Math.random() - 0.5) * this.maxOffset;
                        enemy.erraticTimer = 0;
                    }
                    
                    if (!enemy.erraticCurrent) enemy.erraticCurrent = { x: 0, y: 0 };
                    enemy.erraticCurrent.x += (enemy.erraticTarget.x - enemy.erraticCurrent.x) * 0.1;
                    enemy.erraticCurrent.y += (enemy.erraticTarget.y - enemy.erraticCurrent.y) * 0.1;
                    
                    return { 
                        offsetX: enemy.erraticCurrent.x, 
                        offsetY: enemy.erraticCurrent.y 
                    };
                }
            },
            
            // Nivel 6+: Random
            RANDOM: {
                name: 'random',
                description: 'Movimiento Aleatorio',
                minLevel: 6,
                apply: function(enemy, time) {
                    // 1. Si el enemigo aún no tiene un patrón asignado, elegimos uno
                    if (!enemy.assignedPattern) {
                        // Obtenemos todos los patrones disponibles en CONFIG
                        const patterns = CONFIG.ENEMY.MOVEMENT_PATTERNS;
                        
                        // Filtramos para NO elegir 'RANDOM' (evitar bucle infinito) 
                        // y respetar el nivel mínimo del patrón
                        const validKeys = Object.keys(patterns).filter(key => 
                            key !== 'RANDOM' && 
                            (enemy.level || 1) >= patterns[key].minLevel
                        );

                        // Elegimos una clave al azar y guardamos el objeto del patrón
                        const randomKey = validKeys[Math.floor(Math.random() * validKeys.length)];
                        enemy.assignedPattern = patterns[randomKey];
                        
                        // Opcional: Para Debug
                        if (CONFIG.DEBUG.SHOW_PATTERN_INFO) {
                            console.log(`Enemigo ${enemy.id} asignado a: ${enemy.assignedPattern.name}`);
                        }
                    }

                    // 2. Ejecutamos el patrón que le fue asignado
                    return enemy.assignedPattern.apply(enemy, time);
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

    // Sistema de Niveles DUAL
    LEVELS: {
        ENEMIES_FOR_NEXT_LEVEL: 10,   // Enemigos necesarios para subir de nivel
        SCORE_FOR_NEXT_LEVEL: 150     // Puntos necesarios (se multiplica × nivel)
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
        ENEMY_DESPAWN_Y_PERCENT: 1.03
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
        ENABLED: false ,
        SHOW_HITBOXES: false ,
        SHOW_FPS: false ,
        GOD_MODE: false ,
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