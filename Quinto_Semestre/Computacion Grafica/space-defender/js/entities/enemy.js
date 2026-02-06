/* ===================================
   ENTITIES/ENEMY.JS - Enemigos
   ===================================
   Clase Enemy (individual) y EnemyManager (gestor de horda).
*/

class Enemy {
    /**
     * Constructor del enemigo
     * @param {number} x - Posición X inicial
     * @param {number} y - Posición Y inicial
     * @param {number} type - Tipo de enemigo (1, 2, o 3)
     */
    constructor(x, y, type = 1) {
        this.x = x;
        this.y = y;
        this.type = Math.min(type, 3); // Limitar a tipo 3
        
        // Obtener configuración
        this.width = CONFIG.ENEMY.WIDTH;
        this.height = CONFIG.ENEMY.HEIGHT;
        
        // Obtener propiedades según tipo desde CONFIG
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
    }

    /**
     * Actualiza el estado del enemigo
     */
    update() {
        // Actualizar animación
        this.animationCounter++;
        if (this.animationCounter >= this.animationSpeed) {
            this.animationFrame = 1 - this.animationFrame; // Alterna 0-1
            this.animationCounter = 0;
        }
    }

    /**
     * Dibuja el enemigo
     * @param {CanvasRenderingContext2D} ctx - Contexto del canvas
     */
    draw(ctx) {
        // Efecto de brillo
        ctx.shadowBlur = 10;
        ctx.shadowColor = this.color;
        ctx.fillStyle = this.color;
        
        const offset = this.animationFrame * 3; // Animación de antenas
        const halfWidth = this.width / 2;
        const halfHeight = this.height / 2;
        
        // Cuerpo principal
        ctx.fillRect(
            this.x - halfWidth + 8,
            this.y - halfHeight + 10,
            this.width - 16,
            this.height - 15
        );
        
        // Antenas animadas
        ctx.fillRect(
            this.x - halfWidth + offset,
            this.y - halfHeight,
            8,
            15
        );
        ctx.fillRect(
            this.x + halfWidth - 8 - offset,
            this.y - halfHeight,
            8,
            15
        );
        
        // Patas
        ctx.fillRect(this.x - halfWidth, this.y + halfHeight - 10, 8, 10);
        ctx.fillRect(this.x + halfWidth - 8, this.y + halfHeight - 10, 8, 10);
        
        // Ojos
        ctx.fillStyle = '#ffffff';
        ctx.fillRect(this.x - 10, this.y - 5, 6, 6);
        ctx.fillRect(this.x + 4, this.y - 5, 6, 6);

        // Resetear sombra
        ctx.shadowBlur = 0;

        // Debug: mostrar hitbox y tipo
        if (CONFIG.DEBUG.SHOW_HITBOXES) {
            const bounds = this.getBounds();
            ctx.strokeStyle = '#ff0000';
            ctx.lineWidth = 1;
            ctx.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        
        if (CONFIG.DEBUG.ENABLED) {
            ctx.fillStyle = '#ffffff';
            ctx.font = '10px monospace';
            ctx.fillText(`T${this.type}`, this.x - 8, this.y + halfHeight + 15);
        }
    }

    /**
     * Intenta disparar (con probabilidad)
     * @returns {Bullet|null} Bala enemiga o null
     */
    shoot() {
        if (Math.random() < this.shootChance) {
            return new Bullet(
                this.x,
                this.y + this.height / 2,
                false  // No es del jugador
            );
        }
        return null;
    }

    /**
     * Obtiene el rectángulo de colisión
     * @returns {Object} Rectángulo {x, y, width, height}
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
   ENEMY MANAGER - Gestor de Horda
   ===================================
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
        this.direction = 1; // 1=derecha, -1=izquierda
        this.speed = CONFIG.ENEMY.BASE_SPEED + (level * CONFIG.ENEMY.SPEED_INCREMENT);
        this.dropDistance = CONFIG.ENEMY.DROP_DISTANCE;
        
        // Crear grid de enemigos
        this.createEnemyGrid();
    }

    /**
     * Crea el grid inicial de enemigos según el nivel
     */
    createEnemyGrid() {
        const gridConfig = CONFIG.ENEMY.GRID;
        
        // Calcular filas según nivel
        const rows = Math.min(
            gridConfig.ROWS + Math.floor((this.level - 1) * gridConfig.ROWS_PER_LEVEL),
            8  // Máximo 8 filas
        );
        const cols = gridConfig.COLS;

        for (let row = 0; row < rows; row++) {
            for (let col = 0; col < cols; col++) {
                const x = gridConfig.START_X + col * gridConfig.SPACING_X;
                const y = gridConfig.START_Y + row * gridConfig.SPACING_Y;
                
                // Determinar tipo según fila
                let type = 1;
                if (row === 0) type = 3;      // Primera fila: tipo 3
                else if (row === 1) type = 2; // Segunda fila: tipo 2
                // Resto: tipo 1
                
                this.enemies.push(new Enemy(x, y, type));
            }
        }
    }

    /**
     * Actualiza todos los enemigos
     * @returns {Array<Bullet>} Balas disparadas por enemigos
     */
    update() {
        const bullets = [];

        // Encontrar límites del grupo
        let minX = Infinity;
        let maxX = -Infinity;

        this.enemies.forEach(enemy => {
            if (enemy.active) {
                enemy.update();
                
                // Tracking de límites
                const bounds = enemy.getBounds();
                if (bounds.x < minX) minX = bounds.x;
                if (bounds.x + bounds.width > maxX) maxX = bounds.x + bounds.width;

                // Intentar disparar
                const bullet = enemy.shoot();
                if (bullet) bullets.push(bullet);
            }
        });

        // Verificar colisión con bordes
        const hitEdge = maxX >= CONFIG.CANVAS.WIDTH || minX <= 0;

        // Mover todos los enemigos
        this.enemies.forEach(enemy => {
            if (enemy.active) {
                if (hitEdge) {
                    // Bajar y cambiar dirección
                    enemy.y += this.dropDistance;
                } else {
                    // Mover horizontalmente
                    enemy.x += this.speed * this.direction;
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

        // Debug: información del manager
        if (CONFIG.DEBUG.ENABLED) {
            ctx.fillStyle = '#ffffff';
            ctx.font = '12px monospace';
            ctx.fillText(
                `Enemigos: ${this.getActiveCount()} | Vel: ${this.speed.toFixed(1)}`,
                10,
                CONFIG.CANVAS.HEIGHT - 10
            );
        }
    }

    /**
     * Verifica si todos los enemigos fueron destruidos
     * @returns {boolean} True si no quedan enemigos
     */
    allDestroyed() {
        return this.enemies.every(enemy => !enemy.active);
    }

    /**
     * Verifica si algún enemigo llegó a la línea de invasión
     * @returns {boolean} True si invadieron
     */
    hasInvaded() {
        return this.enemies.some(enemy => {
            return enemy.active && enemy.y + enemy.height / 2 >= CONFIG.PHYSICS.INVASION_LINE;
        });
    }

    /**
     * Obtiene solo los enemigos activos
     * @returns {Array<Enemy>} Array de enemigos activos
     */
    getActiveEnemies() {
        return this.enemies.filter(enemy => enemy.active);
    }

    /**
     * Obtiene el conteo de enemigos activos
     * @returns {number} Cantidad de enemigos vivos
     */
    getActiveCount() {
        return this.getActiveEnemies().length;
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
        this.createEnemyGrid();
    }
}