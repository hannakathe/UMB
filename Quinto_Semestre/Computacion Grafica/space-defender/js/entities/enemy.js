/* ===================================
   ENTITIES/ENEMY.JS - Enemigos v3.0
   ===================================
   Con patrones de movimiento múltiples y corrección de bugs.
*/

class Enemy {
    /**
     * Constructor del enemigo
     * @param {number} x - Posición X inicial
     * @param {number} y - Posición Y inicial
     * @param {number} type - Tipo de enemigo (1, 2, o 3)
     * @param {number} gridRow - Fila en el grid
     * @param {number} gridCol - Columna en el grid
     * @param {number} gridIndex - Índice único en el grid
     */
    constructor(x, y, type, gridRow, gridCol, gridIndex) {
        // Posición base (nunca cambia, es la posición en el grid)
        this.baseX = x;
        this.baseY = y;
        
        // Posición actual (se calcula con base + offsets)
        this.x = x;
        this.y = y;
        
        this.type = Math.min(type, 3);
        
        // Información del grid (para patrones)
        this.gridRow = gridRow;
        this.gridCol = gridCol;
        this.gridIndex = gridIndex;
        
        // Obtener configuración
        this.width = CONFIG.ENEMY.WIDTH;
        this.height = CONFIG.ENEMY.HEIGHT;
        
        const typeConfig = CONFIG.ENEMY.TYPES[this.type];
        this.points = typeConfig.POINTS;
        this.color = typeConfig.COLOR;
        this.shootChance = typeConfig.SHOOT_CHANCE;
        
        // Estado
        this.active = true;
        
        // Animación
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.animationSpeed = CONFIG.ENEMY.ANIMATION_SPEED;
        
        // Offsets de patrones de movimiento
        this.waveOffset = 0;
        this.zigzagOffset = 0;
        this.circularOffsetX = 0;
        this.circularOffsetY = 0;
        this.erraticOffset = { x: 0, y: 0 };
    }

    /**
     * Actualiza el estado del enemigo
     * @param {number} time - Frame/tiempo actual
     * @param {Object} pattern - Patrón de movimiento actual
     */
    update(time, pattern) {
        // Actualizar animación
        this.animationCounter++;
        if (this.animationCounter >= this.animationSpeed) {
            this.animationFrame = 1 - this.animationFrame;
            this.animationCounter = 0;
        }
        
        // Aplicar patrón de movimiento si existe
        if (pattern && pattern.update) {
            pattern.update(this, null, time);
        }
        
        // Calcular posición final con todos los offsets
        this.calculateFinalPosition();
    }

    /**
     * Calcula la posición final aplicando todos los offsets
     */
    calculateFinalPosition() {
        this.x = this.baseX 
            + (this.waveOffset || 0) 
            + (this.zigzagOffset || 0)
            + (this.circularOffsetX || 0)
            + (this.erraticOffset?.x || 0);
            
        this.y = this.baseY 
            + (this.circularOffsetY || 0)
            + (this.erraticOffset?.y || 0);
    }

    /**
     * Mueve la posición base del enemigo (para movimiento en grupo)
     * @param {number} dx - Delta X
     * @param {number} dy - Delta Y
     */
    moveBase(dx, dy) {
        this.baseX += dx;
        this.baseY += dy;
        this.calculateFinalPosition();
    }

    /**
     * Dibuja el enemigo
     * @param {CanvasRenderingContext2D} ctx - Contexto del canvas
     */
    draw(ctx) {
        ctx.shadowBlur = 10;
        ctx.shadowColor = this.color;
        ctx.fillStyle = this.color;
        
        const offset = this.animationFrame * 3;
        const halfWidth = this.width / 2;
        const halfHeight = this.height / 2;
        
        // Cuerpo
        ctx.fillRect(
            this.x - halfWidth + 8,
            this.y - halfHeight + 10,
            this.width - 16,
            this.height - 15
        );
        
        // Antenas animadas
        ctx.fillRect(this.x - halfWidth + offset, this.y - halfHeight, 8, 15);
        ctx.fillRect(this.x + halfWidth - 8 - offset, this.y - halfHeight, 8, 15);
        
        // Patas
        ctx.fillRect(this.x - halfWidth, this.y + halfHeight - 10, 8, 10);
        ctx.fillRect(this.x + halfWidth - 8, this.y + halfHeight - 10, 8, 10);
        
        // Ojos
        ctx.fillStyle = '#ffffff';
        ctx.fillRect(this.x - 10, this.y - 5, 6, 6);
        ctx.fillRect(this.x + 4, this.y - 5, 6, 6);

        ctx.shadowBlur = 0;

        if (CONFIG.DEBUG.SHOW_HITBOXES) {
            const bounds = this.getBounds();
            ctx.strokeStyle = '#ff0000';
            ctx.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
            
            // Mostrar posición base
            ctx.fillStyle = '#00ff00';
            ctx.fillRect(this.baseX - 2, this.baseY - 2, 4, 4);
        }
    }

    /**
     * Intenta disparar
     * @returns {Bullet|null}
     */
    shoot() {
        if (Math.random() < this.shootChance) {
            return new Bullet(this.x, this.y + this.height / 2, false);
        }
        return null;
    }

    /**
     * Obtiene el rectángulo de colisión
     * @returns {Object}
     */
    getBounds() {
        return {
            x: this.x - this.width / 2,
            y: this.y - this.height / 2,
            width: this.width,
            height: this.height
        };
    }

    /**
     * Destruye el enemigo
     */
    destroy() {
        this.active = false;
    }
}

/* ===================================
   ENEMY MANAGER v3.0
   ===================================
   Gestor con patrones de movimiento múltiples.
*/

class EnemyManager {
    /**
     * Constructor del gestor
     * @param {number} level - Nivel actual
     */
    constructor(level = 1) {
        this.enemies = [];
        this.level = level;
        
        // Movimiento en grupo
        this.direction = 1;
        this.speed = CONFIG.ENEMY.BASE_SPEED + (level * CONFIG.ENEMY.SPEED_INCREMENT);
        this.dropDistance = CONFIG.ENEMY.DROP_DISTANCE;
        
        // Tiempo/frame counter para patrones
        this.time = 0;
        
        // Determinar patrón actual según nivel
        this.currentPattern = this.getPatternForLevel(level);
        
        // Crear grid de enemigos
        this.createEnemyGrid();
        
        console.log(`Nivel ${level}: Patrón "${this.currentPattern.name}"`);
    }

    /**
     * Obtiene el patrón de movimiento apropiado para el nivel
     * @param {number} level - Nivel actual
     * @returns {Object} Patrón de movimiento
     */
    getPatternForLevel(level) {
        const patterns = CONFIG.ENEMY.MOVEMENT_PATTERNS;
        
        // Seleccionar patrón según nivel
        if (level >= 5) return patterns.ERRATIC;
        if (level >= 4) return patterns.CIRCULAR;
        if (level >= 3) return patterns.ZIGZAG;
        if (level >= 2) return patterns.WAVE;
        return patterns.CLASSIC;
    }

    /**
     * Crea el grid inicial de enemigos
     */
    createEnemyGrid() {
        const gridConfig = CONFIG.ENEMY.GRID;
        
        const rows = Math.min(
            gridConfig.ROWS + Math.floor((this.level - 1) * gridConfig.ROWS_PER_LEVEL),
            8
        );
        const cols = gridConfig.COLS;

        let index = 0;
        for (let row = 0; row < rows; row++) {
            for (let col = 0; col < cols; col++) {
                const x = gridConfig.START_X + col * gridConfig.SPACING_X;
                const y = gridConfig.START_Y + row * gridConfig.SPACING_Y;
                
                // Determinar tipo según fila
                let type = 1;
                if (row === 0) type = 3;
                else if (row === 1) type = 2;
                
                this.enemies.push(new Enemy(x, y, type, row, col, index));
                index++;
            }
        }
    }

    /**
     * Actualiza todos los enemigos
     * @returns {Array<Bullet>} Balas disparadas
     */
    update() {
        const bullets = [];
        this.time++;

        // IMPORTANTE: Filtrar solo enemigos ACTIVOS para cálculos
        const activeEnemies = this.enemies.filter(e => e.active);
        
        if (activeEnemies.length === 0) {
            return bullets;
        }

        // Encontrar límites del grupo (solo enemigos activos)
        let minX = Infinity;
        let maxX = -Infinity;

        activeEnemies.forEach(enemy => {
            // Actualizar cada enemigo con el patrón actual
            enemy.update(this.time, this.currentPattern);
            
            // Tracking de límites usando posición BASE (no final)
            // Esto previene que los offsets afecten el movimiento del grupo
            if (enemy.baseX - enemy.width / 2 < minX) {
                minX = enemy.baseX - enemy.width / 2;
            }
            if (enemy.baseX + enemy.width / 2 > maxX) {
                maxX = enemy.baseX + enemy.width / 2;
            }

            // Intentar disparar
            const bullet = enemy.shoot();
            if (bullet) bullets.push(bullet);
        });

        // Verificar colisión con bordes
        const hitEdge = maxX >= CONFIG.CANVAS.WIDTH || minX <= 0;

        // Mover TODOS los enemigos (activos e inactivos mantienen posición base)
        this.enemies.forEach(enemy => {
            if (enemy.active) {
                if (hitEdge) {
                    // Bajar
                    enemy.moveBase(0, this.dropDistance);
                } else {
                    // Mover horizontalmente
                    enemy.moveBase(this.speed * this.direction, 0);
                }
            }
        });

        // Cambiar dirección si tocaron borde
        if (hitEdge) {
            this.direction *= -1;
        }

        return bullets;
    }

    /**
     * Dibuja todos los enemigos activos
     * @param {CanvasRenderingContext2D} ctx - Contexto del canvas
     */
    draw(ctx) {
        this.enemies.forEach(enemy => {
            if (enemy.active) {
                enemy.draw(ctx);
            }
        });

        if (CONFIG.DEBUG.ENABLED) {
            ctx.fillStyle = '#ffffff';
            ctx.font = '12px monospace';
            ctx.fillText(
                `Enemigos: ${this.getActiveCount()} | Patrón: ${this.currentPattern.name}`,
                10,
                CONFIG.CANVAS.HEIGHT - 10
            );
        }
    }

    /**
     * Verifica si todos los enemigos fueron destruidos
     * @returns {boolean}
     */
    allDestroyed() {
        return this.enemies.every(enemy => !enemy.active);
    }

    /**
     * Verifica si algún enemigo llegó a la línea de invasión
     * @returns {boolean}
     */
    hasInvaded() {
        return this.enemies.some(enemy => {
            return enemy.active && enemy.y + enemy.height / 2 >= CONFIG.PHYSICS.INVASION_LINE;
        });
    }

    /**
     * Obtiene solo los enemigos activos
     * @returns {Array<Enemy>}
     */
    getActiveEnemies() {
        return this.enemies.filter(enemy => enemy.active);
    }

    /**
     * Obtiene el conteo de enemigos activos
     * @returns {number}
     */
    getActiveCount() {
        return this.getActiveEnemies().length;
    }

    /**
     * Obtiene el nombre del patrón actual
     * @returns {string}
     */
    getCurrentPatternName() {
        return this.currentPattern.description || this.currentPattern.name;
    }

    /**
     * Reinicia el manager con un nuevo nivel
     * @param {number} level - Nuevo nivel
     */
    resetForLevel(level) {
        this.level = level;
        this.enemies = [];
        this.direction = 1;
        this.speed = CONFIG.ENEMY.BASE_SPEED + (level * CONFIG.ENEMY.SPEED_INCREMENT);
        this.time = 0;
        this.currentPattern = this.getPatternForLevel(level);
        this.createEnemyGrid();
        
        console.log(`Nivel ${level}: Patrón "${this.currentPattern.name}"`);
    }
}