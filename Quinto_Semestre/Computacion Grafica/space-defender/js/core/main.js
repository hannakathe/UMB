/* ===================================
   CORE/MAIN.JS - v5.0 RESPONSIVE
   ===================================
   Spawn individual + Patrones de movimiento + Responsive.
*/

document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 Iniciando Space Defender v5.0...');
    
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
    window.game = game; // ⭐ Hacer accesible globalmente
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
    console.log('🎮 SPACE DEFENDER v5.0 - RESPONSIVE EDITION');
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

/* ===================================
   DOCUMENTACIÓN v5.0
   ===================================

   VERSIÓN 5.0 - LA VERSIÓN DEFINITIVA
   
   CARACTERÍSTICAS PRINCIPALES:
   
   1. SISTEMA DE SPAWN INDIVIDUAL (v4.0):
      - Enemigos aparecen uno por uno
      - Cada tipo con velocidad propia
      - Spawn continuo durante partida
      - Máximo 15 enemigos en pantalla
   
   2. PATRONES DE MOVIMIENTO (v3.0):
      ⭐ NUEVO: Aplicados a enemigos individuales
      
      Nivel 1 - CLÁSICO:
      - Solo descenso vertical
      - Sin offsets horizontales
      
      Nivel 2 - ONDAS:
      - Movimiento sinusoidal horizontal
      - Amplitude: 30px
      - Frequency: 0.04
      
      Nivel 3 - ZIGZAG:
      - Oscilación más rápida
      - Amplitude: 2.5px
      - Frequency: 0.08
      
      Nivel 4 - CIRCULAR:
      - Órbitas elípticas
      - Radius: 15px
      - Speed: 0.05
      
      Nivel 5+ - ERRÁTICO:
      - Cambios aleatorios cada 60 frames
      - Offset máximo: 25px
      - Movimiento impredecible
   
   3. DISEÑO RESPONSIVE:
      - Canvas escala automáticamente
      - Mantiene aspect ratio 4:3
      - Funciona en móviles/tablets/desktop
      - UI adaptable
      - Controles laterales ocultos en móvil
   
   ARQUITECTURA:
   
   Enemy:
   - baseX, baseY: Posición de descenso
   - patternOffsetX, patternOffsetY: Offset del patrón
   - x, y: Posición final (base + offset)
   
   EnemySpawner:
   - Genera enemigos con patrón actual
   - Patrón cambia según nivel
   - Aplica patrón en cada update
   
   Game:
   - setupResponsiveCanvas(): Configura escalado
   - resizeCanvas(): Ajusta dimensiones
   - handleResize(): Listener de window.resize
   
   RESPONSIVE:
   
   CSS:
   - clamp() para tamaños de fuente
   - Media queries para breakpoints
   - Canvas con max-width y aspect-ratio
   
   JavaScript:
   - Dimensiones base (lógicas): 800x600
   - Dimensiones físicas: Escaladas
   - Factor de escala calculado automáticamente
   
   ===================================
*/