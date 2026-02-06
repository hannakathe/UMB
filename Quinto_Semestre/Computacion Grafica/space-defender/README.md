# 🚀 SPACE DEFENDER v3.0
## Versión con Pantalla de Intro, Controles Gráficos y Patrones de Movimiento

---

## 🆕 NOVEDADES DE LA VERSIÓN 3.0

### ✅ CORRECCIONES DE BUGS

**Bug Crítico CORREGIDO:** 
- ❌ **Problema:** Al disparar a una nave enemiga, todas las demás caían inmediatamente sobre el jugador
- ✅ **Solución:** Separación de posición base (grid) y posición visual (con efectos)
- 📝 **Explicación técnica:** Ahora cada enemigo tiene `baseX/baseY` (posición en el grid) y `x/y` (posición visual). Los límites del grupo se calculan con la posición base, no la visual, por lo que los efectos visuales no afectan el movimiento del grupo.

### ✨ NUEVAS CARACTERÍSTICAS

#### 1. **Pantalla de Intro Cinematográfica**
- Historia del juego con narrativa
- Diseño visual impactante con efectos
- Transición suave al gameplay
- Presiona ENTER para comenzar

#### 2. **HUD Gráfico Mejorado**
- **Controles laterales** con teclas visuales (estilo imagen de referencia)
- **Info de enemigos** con puntos de cada tipo
- **High Score** siempre visible
- Diseño inspirado en interfaces de juegos AAA

#### 3. **5 Patrones de Movimiento de Enemigos**

| Nivel | Patrón | Descripción |
|-------|--------|-------------|
| 1 | **Clásico** | Movimiento horizontal + descenso (Space Invaders original) |
| 2 | **Ondulatorio** | Movimiento en olas sinusoidales |
| 3 | **Zigzag** | Trayectoria en zigzag vertical |
| 4 | **Circular** | Órbitas circulares individuales |
| 5+ | **Errático** | Movimiento impredecible y caótico |

#### 4. **Dificultad Progresiva Inteligente**
- Cada nivel aumenta la complejidad del patrón
- Velocidad incremental
- Más enemigos por nivel
- Mayor frecuencia de disparos

---

## 🎮 CONTROLES

| Acción | Teclas |
|--------|--------|
| **Mover nave izquierda** | `←` o `A` |
| **Mover nave derecha** | `→` o `D` |
| **Disparar** | `SPACE` |
| **Pausar/Reanudar** | `P` |
| **Comenzar (intro)** | `ENTER` |
| **Reiniciar (game over)** | `R` |

---

## 📁 ESTRUCTURA DEL PROYECTO

```
space-defender-v3/
├── index.html              ← Con intro screen y sidebars
├── css/
│   └── style.css          ← Estilos mejorados
├── js/
│   ├── config.js          ← Con patrones de movimiento
│   ├── core/
│   │   ├── main.js        ← Game loop
│   │   └── game.js        ← Lógica con soporte intro
│   ├── entities/
│   │   ├── player.js
│   │   ├── enemy.js       ← ⭐ ACTUALIZADO con patrones
│   │   └── bullet.js
│   └── systems/
│       └── collision.js
└── assets/
    ├── images/
    └── sounds/
```

---

## 🚀 CÓMO EJECUTAR

```bash
# Opción 1: Abrir directamente
# Doble clic en index.html

# Opción 2: Servidor local (Python)
python -m http.server 8000
# Abre: http://localhost:8000

# Opción 3: VS Code Live Server
# Instala extensión "Live Server"
# Clic derecho en index.html → "Open with Live Server"
```

---

## ⚙️ PERSONALIZACIÓN

### Cambiar Dificultad

Edita `js/config.js`:

```javascript
// Más fácil
CONFIG.PLAYER.SPEED = 7;
CONFIG.PLAYER.INITIAL_LIVES = 5;
CONFIG.ENEMY.BASE_SPEED = 0.8;

// Más difícil  
CONFIG.PLAYER.SPEED = 3;
CONFIG.ENEMY.BASE_SPEED = 1.5;
CONFIG.ENEMY.TYPES[1].SHOOT_CHANCE = 0.002;
```

### Modificar Patrones de Movimiento

En `js/config.js`, sección `ENEMY.MOVEMENT_PATTERNS`:

```javascript
// Ejemplo: Hacer el patrón ondulatorio más pronunciado
WAVE: {
    name: 'wave',
    minLevel: 2,
    amplitude: 25,  // Aumentar de 15 a 25
    frequency: 0.08 // Más rápido
}
```

### Activar Debug

```javascript
CONFIG.DEBUG = {
    ENABLED: true,        // Info en pantalla
    SHOW_HITBOXES: true,  // Ver colisiones
    SHOW_FPS: true,       // Ver FPS
    GOD_MODE: true        // Invencibilidad
};
```

---

## 🐛 EXPLICACIÓN TÉCNICA DEL BUG CORREGIDO

### El Problema Original

```javascript
// ANTES (v2.0) - INCORRECTO
activeEnemies.forEach(enemy => {
    const bounds = enemy.getBounds(); // Usaba posición FINAL (con offsets)
    if (bounds.x < minX) minX = bounds.x;
    if (bounds.x + bounds.width > maxX) maxX = bounds.x + bounds.width;
});
```

**¿Qué pasaba?**
1. Enemigo destruido → Ya no está en `activeEnemies`
2. Cálculo de límites excluye ese enemigo
3. Si estaba en el borde, `minX` o `maxX` cambia drásticamente
4. El grupo detecta borde falso y TODOS bajan inmediatamente

### La Solución (v3.0)

```javascript
// AHORA (v3.0) - CORRECTO
class Enemy {
    constructor(x, y, type, gridRow, gridCol, gridIndex) {
        this.baseX = x;  // ← Posición en el GRID (nunca cambia)
        this.baseY = y;
        this.x = x;      // ← Posición VISUAL (base + offsets)
        this.y = y;
    }
    
    moveBase(dx, dy) {
        this.baseX += dx;  // ← Mover solo la base
        this.baseY += dy;
        this.calculateFinalPosition(); // Actualizar visual
    }
}

// En EnemyManager.update()
activeEnemies.forEach(enemy => {
    // Usar posición BASE para límites, NO la visual
    if (enemy.baseX - enemy.width / 2 < minX) {
        minX = enemy.baseX - enemy.width / 2;
    }
    // ...
});
```

**¿Por qué funciona?**
- `baseX/baseY` = Posición en el grid lógico (no afectada por offsets visuales)
- `x/y` = Posición visual (base + wave + zigzag + circular + erratic)
- Límites del grupo usan solo `baseX/baseY`
- Los efectos visuales no afectan el movimiento del grupo

---

## 📊 PATRONES DE MOVIMIENTO EN DETALLE

### 1. Clásico (Nivel 1)
```
→ → → → → →
          ↓
← ← ← ← ← ←
          ↓
```
- Movimiento original de Space Invaders
- Horizontal hasta tocar borde
- Descenso y cambio de dirección

### 2. Ondulatorio (Nivel 2)
```
   ↗     ↗     ↗
 ↗   ↘ ↗   ↘ ↗   ↘
→     →     →     →
```
- Movimiento base horizontal
- Offset sinusoidal vertical
- Cada columna desfasada

### 3. Zigzag (Nivel 3)
```
  ↗ ↘   ↗ ↘   ↗ ↘
→   →   →   →   →
 ↘ ↗   ↘ ↗   ↘ ↗
```
- Movimiento en forma de zigzag
- Cada fila con fase diferente
- Patrón diagonal

### 4. Circular (Nivel 4)
```
  ○     ○     ○
    ○     ○     ○
  ○     ○     ○
```
- Cada enemigo en su propia órbita
- Radio pequeño alrededor de posición base
- Desfase entre enemigos

### 5. Errático (Nivel 5+)
```
 →↗  ↘←  ↑↓  →←
   ↖↓  ↗↑  ↙→  ↗
 ←↓  ↑→  ←↗  ↓↘
```
- Cambios aleatorios de dirección
- Impredecible
- Dificulta apuntar

---

## 🎨 CUSTOMIZAR CONTROLES GRÁFICOS

Los controles laterales están en `index.html`:

```html
<!-- Agregar nuevo control -->
<div class="control-item">
    <div class="keys-group">
        <span class="key">ESC</span>
    </div>
    <span class="control-desc">Salir</span>
</div>
```

Estilos en `css/style.css`:

```css
.key {
    background: linear-gradient(145deg, #2a2a2a, #1a1a1a);
    border: 2px solid #00d4ff;
    /* ... más estilos ... */
}
```

---

## 📝 PARA EL INFORME DEL LAB

### Especificar Uso de IA

```markdown
**IA Utilizada:** Claude (Anthropic) - Sonnet 4.5  
**Versión:** 3.0

**Generado por IA:**
- Corrección de bug de movimiento enemigo
- Sistema de patrones de movimiento (5 patrones)
- Pantalla de intro con historia
- HUD gráfico con controles visuales
- Arquitectura base/visual para enemigos

**Porcentaje IA:** ~95%

**Mejoras manuales realizadas:**
- [Agrega tus modificaciones aquí]
```

### Conceptos Técnicos Aprendidos

1. **Separación de Lógica y Visualización**
   - Posición base vs posición visual
   - Offsets acumulativos para efectos

2. **Patrones de Comportamiento**
   - Strategy Pattern para movimiento
   - Funciones como objetos de configuración

3. **UI/UX Mejorado**
   - Pantallas de estado (intro, menu, game)
   - Feedback visual progresivo

4. **Debug y Optimización**
   - Filtrado eficiente de entidades activas
   - Separación de concerns

---

## 🎓 PROGRESIÓN DE DIFICULTAD

### Sistema de Niveles

```
Nivel 1: Clásico     → 3 filas × 8 cols = 24 enemigos
Nivel 2: Ondulatorio → 3.5 filas × 8 = ~28 enemigos
Nivel 3: Zigzag      → 4 filas × 8 = 32 enemigos
Nivel 4: Circular    → 4.5 filas × 8 = ~36 enemigos
Nivel 5+: Errático   → 5+ filas × 8 = 40+ enemigos
```

### Incrementos por Nivel

- **Velocidad:** +0.15 por nivel
- **Filas:** +0.5 cada 2 niveles
- **Disparos:** Más frecuentes según tipo
- **Patrón:** Más complejo cada nivel

---

## 🏆 LOGROS DESBLOQUEABLES (Ideas Futuras)

```javascript
// Agregar en config.js
CONFIG.ACHIEVEMENTS = {
    FIRST_BLOOD: { score: 10, desc: "Primera destrucción" },
    SHARPSHOOTER: { score: 100, desc: "100 puntos sin perder vida" },
    WAVE_MASTER: { level: 2, desc: "Superar nivel ondulatorio" },
    PATTERN_EXPERT: { level: 5, desc: "Alcanzar patrón errático" }
};
```

---

## ✅ CHECKLIST DE ENTREGA

- [ ] Juego funciona sin errores
- [ ] Todos los 5 patrones implementados
- [ ] Bug de movimiento corregido
- [ ] Pantalla de intro operativa
- [ ] Controles gráficos visibles
- [ ] Capturas de pantalla tomadas
- [ ] Código comentado
- [ ] GDD actualizado
- [ ] Informe completo

---

## 🔧 TROUBLESHOOTING

### Los enemigos siguen cayendo al destruir uno

- Verifica que estés usando la versión v3.0
- Confirma que `enemy.js` tiene las propiedades `baseX/baseY`
- Revisa que `EnemyManager.update()` use `enemy.baseX` para límites

### Los patrones no se ven

- Activa `CONFIG.DEBUG.ENABLED = true`
- Verifica que el nivel sea apropiado para el patrón
- Revisa consola por errores

### La pantalla de intro no aparece

- Verifica que `index.html` tenga el div `#introScreen`
- Confirma que CSS tiene la clase `.intro-screen.active`
- Revisa que `game.js` inicie en estado `INTRO`

---

**¡Disfruta la nueva versión mejorada! 🚀**

*v3.0 - Con pantalla de intro, controles gráficos, y patrones de movimiento*