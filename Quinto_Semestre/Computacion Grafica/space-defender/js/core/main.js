/* ===================================
   CORE/MAIN.JS - Entrada principal del juego
   ===================================
   Spawn individual + Patrones de movimiento + Responsive.
*/

document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 Iniciando Space Defender...');
    
    const canvas = document.getElementById('gameCanvas');
    
    if (!canvas) {
        console.error('❌ Error: No se encontró el canvas');
        return;
    }

    if (typeof CONFIG === 'undefined') {
        console.error('❌ Error: CONFIG no está definido');
        return;
    }

    // Crear juego y hacerlo global
    const game = new Game(canvas);
    window.game = game; 
    console.log('✅ Juego creado');

    let lastTime = 0;

    /**
     * Game Loop
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

    // Logs informativos
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('🎮 SPACE DEFENDER - RESPONSIVE EDITION');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('');
    console.log('✨ NUEVAS CARACTERÍSTICAS:');
    console.log('  • ✅ Canvas totalmente responsive');
    console.log('  • ✅ Spawn individual de enemigos');
    console.log('  • ✅ 5 Patrones de movimiento por nivel');
    console.log('  • ✅ Enemigos disparan más');
    console.log('  • ✅ Funciona en móviles y tablets');
    console.log('');
    console.log('🌀 PATRONES POR NIVEL:');
    console.log('  • Nivel 1: Descenso Directo (clásico)');
    console.log('  • Nivel 2: Ondas Sinusoidales');
    console.log('  • Nivel 3: Movimiento en Zigzag');
    console.log('  • Nivel 4: Órbitas Circulares');
    console.log('  • Nivel 5+: Movimiento Caótico');
    console.log('');
    console.log('📱 RESPONSIVE:');
    console.log('  • Desktop: 800x600px');
    console.log('  • Tablet: Escala automática');
    console.log('  • Móvil: Optimizado para pantalla');
    console.log('');
    console.log('📊 VELOCIDADES POR COLOR:');
    console.log(`  • 🟢 Verde:    ${CONFIG.ENEMY.TYPES[1].SPEED} px/frame + patrón`);
    console.log(`  • 🟡 Amarillo: ${CONFIG.ENEMY.TYPES[2].SPEED} px/frame + patrón`);
    console.log(`  • 🔴 Rojo:     ${CONFIG.ENEMY.TYPES[3].SPEED} px/frame + patrón`);
    console.log('');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log(`⭐ High Score actual: ${game.highScore}`);
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    
    if (CONFIG.DEBUG.ENABLED) {
        console.log('🐛 MODO DEBUG ACTIVADO');
        console.log(`  • Hitboxes: ${CONFIG.DEBUG.SHOW_HITBOXES}`);
        console.log(`  • FPS: ${CONFIG.DEBUG.SHOW_FPS}`);
        console.log(`  • Pattern Info: ${CONFIG.DEBUG.SHOW_PATTERN_INFO}`);
        console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    }
    
    console.log('');
    console.log('✨ ¡Presiona ENTER para comenzar!');
    console.log('');
});

