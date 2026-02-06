/* ===================================
   CORE/MAIN.JS - Punto de Entrada
   ===================================
   Inicializa el juego y ejecuta el game loop principal.
*/

/**
 * Inicialización cuando el DOM está listo
 */
document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 Iniciando Space Defender...');
    
    // Obtener el canvas
    const canvas = document.getElementById('gameCanvas');
    
    if (!canvas) {
        console.error('❌ Error: No se encontró el elemento canvas');
        return;
    }

    // Verificar que CONFIG esté disponible
    if (typeof CONFIG === 'undefined') {
        console.error('❌ Error: CONFIG no está definido. Verifica que config.js se cargue primero.');
        return;
    }

    // Crear instancia del juego
    const game = new Game(canvas);
    console.log('✅ Juego creado correctamente');

    // Mostrar mensaje inicial
    const startMsg = CONFIG.MESSAGES.START;
    game.showMessage(startMsg.TITLE, startMsg.TEXT);

    // Variable para tracking de tiempo (deltaTime)
    let lastTime = 0;

    /**
     * Game Loop Principal
     * Se ejecuta ~60 veces por segundo usando requestAnimationFrame
     * 
     * @param {number} currentTime - Timestamp actual en milisegundos
     */
    function gameLoop(currentTime) {
        // Calcular deltaTime (tiempo entre frames)
        // Nota: En esta implementación simple no lo usamos,
        // pero está disponible para futuras mejoras
        const deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        // Actualizar lógica del juego
        game.update();
        
        // Renderizar todo en el canvas
        game.draw();
        
        // Solicitar el siguiente frame
        requestAnimationFrame(gameLoop);
    }

    // Iniciar el game loop
    console.log('🎮 Iniciando game loop...');
    requestAnimationFrame(gameLoop);

    // Log de información útil
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('📊 CONTROLES DEL JUEGO');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('  Mover nave:    ← → o A/D');
    console.log('  Disparar:      ESPACIO');
    console.log('  Pausar:        P');
    console.log('  Comenzar:      ENTER');
    console.log('  Reiniciar:     R (tras Game Over)');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('🎯 OBJETIVO');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('  Elimina todas las oleadas de invasores');
    console.log('  Protege la Tierra y consigue el high score');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log(`❤️  Vidas iniciales: ${CONFIG.PLAYER.INITIAL_LIVES}`);
    console.log(`⭐ High Score actual: ${game.highScore}`);
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    
    if (CONFIG.DEBUG.ENABLED) {
        console.log('🐛 MODO DEBUG ACTIVADO');
        console.log(`  Show Hitboxes: ${CONFIG.DEBUG.SHOW_HITBOXES}`);
        console.log(`  Show FPS: ${CONFIG.DEBUG.SHOW_FPS}`);
        console.log(`  God Mode: ${CONFIG.DEBUG.GOD_MODE}`);
        console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    }
    
    console.log('✨ ¡Buena suerte, comandante!');
    console.log('');
});

/* ===================================
   DOCUMENTACIÓN TÉCNICA
   ===================================

   ARQUITECTURA DEL JUEGO:
   
   1. CONFIG (config.js)
      └─ Constantes globales y configuración
   
   2. ENTIDADES (entities/)
      ├─ Bullet: Proyectiles del jugador y enemigos
      ├─ Player: Nave del jugador con input y estado
      └─ Enemy: Enemigos individuales y EnemyManager
   
   3. SISTEMAS (systems/)
      └─ CollisionSystem: Detección AABB y partículas
   
   4. CORE (core/)
      ├─ Game: Lógica principal, estados y UI
      └─ Main: Game loop y punto de entrada

   FLUJO DEL GAME LOOP:
   
   1. requestAnimationFrame() llama a gameLoop()
   2. game.update() actualiza lógica:
      - Movimiento del jugador
      - Disparos
      - Enemigos
      - Colisiones
      - Estados del juego
   3. game.draw() renderiza:
      - Fondo (estrellas)
      - Entidades (jugador, enemigos, balas)
      - Partículas
      - Debug info
   4. Se solicita el siguiente frame
   
   ESTADOS DEL JUEGO:
   
   MENU → PLAYING → PAUSED
     ↓       ↓         ↓
     ↓    LEVEL_COMPLETE
     ↓       ↓
     ↓    GAME_OVER → MENU
     
   MATEMÁTICAS USADAS:
   
   - Vectores 2D: Posición (x,y) y velocidad
   - AABB: Detección de colisiones rectangulares
   - Trigonometría: Partículas (ángulos y círculo unitario)
   
   OPTIMIZACIONES:
   
   - Pool implícito de balas (filter en vez de splice)
   - Partículas con vida limitada
   - Solo se actualizan objetos activos
   - requestAnimationFrame para sincronización con monitor
   
   MEJORAS FUTURAS:
   
   - Delta time para movimiento independiente de FPS
   - Sprite batching para mejor rendimiento
   - Audio Web API para sonidos
   - WebGL para efectos avanzados
   - Touch controls para móviles
   - Networking para multijugador
   
   ===================================
   CRÉDITOS Y AGRADECIMIENTOS:
   ===================================
   
   Desarrollado con asistencia de:
   - Claude (Anthropic) - IA para generación de código
   
   Inspirado en:
   - Space Invaders (1978) - Taito
   - Galaga (1981) - Namco
   
   Tecnologías:
   - HTML5 Canvas API
   - JavaScript ES6+
   - CSS3
   
   Referencias:
   - MDN Web Docs
   - W3Schools
   - Game Programming Patterns
   
   ===================================
*/