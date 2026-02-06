/* ===================================
   CORE/MAIN.JS - v4.0
   ===================================
   Sistema de spawn continuo individual.
*/

document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 Iniciando Space Defender v4.0...');
    
    const canvas = document.getElementById('gameCanvas');
    
    if (!canvas) {
        console.error('❌ Error: No se encontró el canvas');
        return;
    }

    if (typeof CONFIG === 'undefined') {
        console.error('❌ Error: CONFIG no está definido');
        return;
    }

    // Crear juego
    const game = new Game(canvas);
    console.log('✅ Juego creado');

    let lastTime = 0;

    /**
     * Game Loop
     * @param {number} currentTime
     */
    function gameLoop(currentTime) {
        const deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        game.update();
        game.draw();
        
        requestAnimationFrame(gameLoop);
    }

    console.log('🎮 Iniciando game loop...');
    requestAnimationFrame(gameLoop);

    // Logs
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('🎮 SPACE DEFENDER v4.0');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('');
    console.log('✨ NUEVA MECÁNICA DE SPAWN INDIVIDUAL:');
    console.log('  • Enemigos aparecen uno por uno');
    console.log('  • Cada uno baja a su propia velocidad');
    console.log('  • Verdes (lentos) → Amarillos (medios) → Rojos (rápidos)');
    console.log('  • Spawn progresivo y continuo');
    console.log('  • NO se mueven en bloque');
    console.log('');
    console.log('📊 VELOCIDADES POR COLOR:');
    console.log(`  • 🟢 Verde:    ${CONFIG.ENEMY.TYPES[1].SPEED} px/frame`);
    console.log(`  • 🟡 Amarillo: ${CONFIG.ENEMY.TYPES[2].SPEED} px/frame`);
    console.log(`  • 🔴 Rojo:     ${CONFIG.ENEMY.TYPES[3].SPEED} px/frame`);
    console.log('');
    console.log('🎯 PROGRESIÓN:');
    console.log('  • Mata 10 enemigos para subir de nivel');
    console.log('  • Cada nivel: spawn más rápido');
    console.log('  • Máximo 15 enemigos en pantalla');
    console.log('');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log(`⭐ High Score actual: ${game.highScore}`);
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('');
    console.log('✨ ¡Presiona ENTER para comenzar!');
    console.log('');
});

/* ===================================
   DOCUMENTACIÓN v4.0
   ===================================

   CAMBIOS PRINCIPALES:
   
   1. Sistema de Spawn Individual:
      - Los enemigos NO se mueven en bloque
      - Cada enemigo tiene su propia velocidad vertical
      - Aparición progresiva (uno por uno)
      - Spawn continuo durante toda la partida
   
   2. Velocidades por Tipo:
      - Verde (tipo 1):  1.5 px/frame (LENTO)
      - Amarillo (tipo 2): 2.5 px/frame (MEDIO)
      - Rojo (tipo 3):    4.0 px/frame (RÁPIDO)
   
   3. Sistema de Progresión:
      - Ya no hay "niveles completos"
      - Subes de nivel matando X enemigos
      - Cada nivel: spawn más frecuente
      - Dificultad continua y escalable
   
   4. Movimiento Horizontal Opcional:
      - Los enemigos pueden tener movimiento horizontal aleatorio
      - Se configuran CONFIG.ENEMY.HORIZONTAL_MOVEMENT
      - Cambian dirección aleatoriamente
      - Rebotan en los bordes
   
   ARQUITECTURA:
   
   EnemySpawner:
   - Genera enemigos con intervalo de tiempo
   - Control de máximo en pantalla
   - Pesos de probabilidad por tipo
   - Posición X aleatoria en spawn
   
   Enemy:
   - Posición (x, y) independiente
   - Velocidad vertical propia
   - Sin coordinación con otros
   - Se auto-destruye al salir
   
   ===================================
*/