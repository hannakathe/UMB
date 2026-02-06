/* ===================================
   CORE/MAIN.JS - Punto de Entrada v3.0
   ===================================
   Con soporte para pantalla de intro.
*/

/**
 * Inicialización cuando el DOM está listo
 */
document.addEventListener('DOMContentLoaded', () => {
    console.log('🚀 Iniciando Space Defender v3.0...');
    
    // Obtener el canvas
    const canvas = document.getElementById('gameCanvas');
    
    if (!canvas) {
        console.error('❌ Error: No se encontró el elemento canvas');
        return;
    }

    // Verificar que CONFIG esté disponible
    if (typeof CONFIG === 'undefined') {
        console.error('❌ Error: CONFIG no está definido');
        return;
    }

    // Crear instancia del juego
    const game = new Game(canvas);
    console.log('✅ Juego creado correctamente');

    // Variable para tracking de tiempo
    let lastTime = 0;

    /**
     * Game Loop Principal
     * @param {number} currentTime - Timestamp actual
     */
    function gameLoop(currentTime) {
        const deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        // Actualizar lógica
        game.update();
        
        // Renderizar
        game.draw();
        
        // Solicitar siguiente frame
        requestAnimationFrame(gameLoop);
    }

    // Iniciar el game loop
    console.log('🎮 Iniciando game loop...');
    requestAnimationFrame(gameLoop);

    // Logs informativos
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('🎮 SPACE DEFENDER v3.0');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log('');
    console.log('✨ NOVEDADES DE LA VERSIÓN 3.0:');
    console.log('  • Pantalla de intro con historia');
    console.log('  • Controles gráficos laterales');
    console.log('  • 5 patrones de movimiento enemigo');
    console.log('  • Dificultad progresiva según nivel');
    console.log('  • Corrección de bug de movimiento');
    console.log('');
    console.log('📊 CONTROLES:');
    console.log('  • Mover: ← → o A/D');
    console.log('  • Disparar: ESPACIO');
    console.log('  • Pausar: P');
    console.log('  • Comenzar: ENTER');
    console.log('');
    console.log('🎯 PATRONES DE MOVIMIENTO:');
    console.log('  • Nivel 1: Clásico (Space Invaders)');
    console.log('  • Nivel 2: Ondulatorio');
    console.log('  • Nivel 3: Zigzag');
    console.log('  • Nivel 4: Circular');
    console.log('  • Nivel 5+: Errático');
    console.log('');
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    console.log(`⭐ High Score actual: ${game.highScore}`);
    console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    
    if (CONFIG.DEBUG.ENABLED) {
        console.log('🐛 MODO DEBUG ACTIVADO');
        console.log(`  • Hitboxes: ${CONFIG.DEBUG.SHOW_HITBOXES}`);
        console.log(`  • FPS: ${CONFIG.DEBUG.SHOW_FPS}`);
        console.log(`  • God Mode: ${CONFIG.DEBUG.GOD_MODE}`);
        console.log('━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━');
    }
    
    console.log('');
    console.log('✨ ¡Presiona ENTER en la pantalla de intro para comenzar!');
    console.log('');
});

/* ===================================
   DOCUMENTACIÓN v3.0
   ===================================

   CORRECCIONES DE BUGS:
   
   1. Bug de movimiento enemigo CORREGIDO:
      - Problema: Al destruir un enemigo, todos caían
      - Causa: Cálculo de límites incluía posición final con offsets
      - Solución: Separar posición base (baseX/baseY) de posición visual
      - Los límites ahora usan solo baseX/baseY
      - Los offsets de patrones solo afectan la visualización
   
   NUEVAS CARACTERÍSTICAS:
   
   1. Pantalla de Intro:
      - Historia del juego
      - Diseño cinematográfico
      - Transición suave al juego
   
   2. Controles Gráficos:
      - Teclas visuales estilo imagen de referencia
      - Info de enemigos con puntos
      - High score visible
   
   3. Patrones de Movimiento:
      - 5 patrones diferentes
      - Progresión automática por nivel
      - Combinación de offsets independientes
   
   ARQUITECTURA DE PATRONES:
   
   Cada enemigo tiene:
   - baseX, baseY: Posición en el grid (nunca cambia)
   - x, y: Posición visual (base + offsets)
   - Offsets individuales: wave, zigzag, circular, erratic
   
   El manager mueve solo la posición base.
   Los patrones agregan offsets visuales.
   Las colisiones y límites usan posición visual.
   
   ===================================
*/