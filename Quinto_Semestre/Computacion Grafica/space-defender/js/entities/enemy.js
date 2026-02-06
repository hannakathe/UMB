/* ===================================
   ENTITIES/ENEMY.JS - v5.0
   ===================================
   Enemigos individuales CON patrones de movimiento.
*/

class Enemy {
    /**
     * Constructor del enemigo individual
     * @param {number} x - Posición X inicial
     * @param {number} y - Posición Y inicial
     * @param {number} type - Tipo de enemigo (1, 2, o 3)
     * @param {Object} pattern - Patrón de movimiento actual
     * @param {number} canvasWidth - Ancho del canvas
     */
    constructor(x, y, type, pattern, canvasWidth) {
        // Posición base
        this.baseX = x;
        this.baseY = y;
        
        // Posición actual (base + offset del patrón)
        this.x = x;
        this.y = y;
        
        this.type = Math.min(Math.max(type, 1), 3);
        this.canvasWidth = canvasWidth;
        
        // Configuración del tipo
        this.width = CONFIG.ENEMY.WIDTH;
        this.height = CONFIG.ENEMY.HEIGHT;
        
        const typeConfig = CONFIG.ENEMY.TYPES[this.type];
        this.points = typeConfig.POINTS;
        this.color = typeConfig.COLOR;
        this.shootChance = typeConfig.SHOOT_CHANCE;
        this.speed = typeConfig.SPEED;
        this.name = typeConfig.NAME;
        
        // Patrón de movimiento
        this.pattern = pattern;
        
        // Estado
        this.active = true;
        
        // Animación
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.animationSpeed = CONFIG.ENEMY.ANIMATION_SPEED;
        
        // ID único y tiempo de spawn para patrones
        this.id = Date.now() + Math.random();
        this.spawnTime = Date.now();
        this.timeAlive = 0;
        
        // Offsets de patrón
        this.patternOffsetX = 0;
        this.patternOffsetY = 0;
    }

    /**
     * Actualiza el estado del enemigo
     * @param {number} time - Tiempo global del juego
     * @param {number} canvasHeight - Alto del canvas
     */
    update(time, canvasHeight) {
        this.timeAlive++;
        
        // Aplicar patrón de movimiento si existe
        if (this.pattern && this.pattern.apply) {
            const offset = this.pattern.apply(this, time);
            this.patternOffsetX = offset.offsetX || 0;
            this.patternOffsetY = offset.offsetY || 0;
        }
        
        // Movimiento vertical base (descenso)
        this.baseY += this.speed;
        
        // Calcular posición final
        this.x = this.baseX + this.patternOffsetX;
        this.y = this.baseY + this.patternOffsetY;
        
        // Mantener dentro de límites horizontales
        const margin = CONFIG.ENEMY.WIDTH / 2;
        if (this.x < margin) {
            this.x = margin;
        }
        if (this.x > this.canvasWidth - margin) {
            this.x = this.canvasWidth - margin;
        }
        
        // Desactivar si sale por abajo
        const despawnY = canvasHeight * CONFIG.PHYSICS.ENEMY_DESPAWN_Y_PERCENT;
        if (this.baseY > despawnY) {
            this.active = false;
        }
        
        // Actualizar animación
        this.animationCounter++;
        if (this.animationCounter >= this.animationSpeed) {
            this.animationFrame = 1 - this.animationFrame;
            this.animationCounter = 0;
        }
    }

    /**
     * Dibuja el enemigo
     * @param {CanvasRenderingContext2D} ctx
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

        // Debug: mostrar hitbox
        if (CONFIG.DEBUG.SHOW_HITBOXES) {
            const bounds = this.getBounds();
            ctx.strokeStyle = this.color;
            ctx.lineWidth = 2;
            ctx.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
            
            // Mostrar hitbox real del sprite (sin expansión)
            ctx.strokeStyle = '#ffffff';
            ctx.lineWidth = 1;
            ctx.setLineDash([2, 2]);
            ctx.strokeRect(
                this.x - this.width / 2,
                this.y - this.height / 2,
                this.width,
                this.height
            );
            ctx.setLineDash([]);
            
            // Mostrar posición base
            ctx.fillStyle = '#00ff00';
            ctx.fillRect(this.baseX - 2, this.baseY - 2, 4, 4);
            
            // Mostrar centro
            ctx.fillStyle = '#ff00ff';
            ctx.fillRect(this.x - 2, this.y - 2, 4, 4);
        }
        
        if (CONFIG.DEBUG.SHOW_PATTERN_INFO) {
            ctx.fillStyle = '#ffffff';
            ctx.font = '10px monospace';
            ctx.fillText(
                `${this.pattern ? this.pattern.name : 'none'}`, 
                this.x - 20, 
                this.y - 25
            );
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
     * Obtiene el rectángulo de colisión (con expansión para mejor jugabilidad)
     * @returns {Object}
     */
    getBounds() {
        const expansion = CONFIG.ENEMY.HITBOX_EXPANSION || 0;
        return {
            x: this.x - (this.width / 2) - expansion,
            y: this.y - (this.height / 2) - expansion,
            width: this.width + (expansion * 2),
            height: this.height + (expansion * 2)
        };
    }

    /**
     * Verifica si llegó al fondo (peligro)
     * @param {number} canvasHeight
     * @returns {boolean}
     */
    hasReachedBottom(canvasHeight) {
        return this.y + this.height / 2 >= canvasHeight - 50;
    }

    /**
     * Destruye el enemigo
     */
    destroy() {
        this.active = false;
    }
}

/* ===================================
   ENEMY SPAWNER v5.0
   ===================================
   Con patrones de movimiento por nivel.
*/

class EnemySpawner {
    /**
     * Constructor del spawner
     * @param {number} level - Nivel actual
     * @param {number} canvasWidth - Ancho del canvas
     * @param {number} canvasHeight - Alto del canvas
     */
    constructor(level, canvasWidth, canvasHeight) {
        this.enemies = [];
        this.level = level;
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        
        // Sistema de spawn
        this.spawnInterval = Math.max(
            CONFIG.ENEMY.SPAWN.BASE_INTERVAL - (level * CONFIG.ENEMY.SPAWN.INTERVAL_DECREASE),
            CONFIG.ENEMY.SPAWN.MIN_INTERVAL
        );
        this.timeSinceLastSpawn = 0;
        this.isSpawning = false;
        
        // Tiempo global (para patrones)
        this.time = 0;
        
        // Patrón actual según nivel
        this.currentPattern = this.getPatternForLevel(level);
        
        // Estadísticas
        this.totalSpawned = 0;
        
        // Iniciar spawn
        setTimeout(() => {
            this.isSpawning = true;
        }, CONFIG.ENEMY.SPAWN.INITIAL_DELAY);
        
        console.log(`🎮 Nivel ${level} - Patrón: ${this.currentPattern.description}`);
    }

    /**
     * Obtiene el patrón apropiado para el nivel
     * @param {number} level
     * @returns {Object}
     */
    getPatternForLevel(level) {
        const patterns = CONFIG.ENEMY.MOVEMENT_PATTERNS;
        
        if (level >= 5) return patterns.ERRATIC;
        if (level >= 4) return patterns.CIRCULAR;
        if (level >= 3) return patterns.ZIGZAG;
        if (level >= 2) return patterns.WAVE;
        return patterns.CLASSIC;
    }

    /**
     * Selecciona tipo aleatorio
     * @returns {number}
     */
    selectRandomType() {
        const types = CONFIG.ENEMY.TYPES;
        const weights = [];
        
        for (let type in types) {
            for (let i = 0; i < types[type].SPAWN_WEIGHT; i++) {
                weights.push(parseInt(type));
            }
        }
        
        return weights[Math.floor(Math.random() * weights.length)];
    }

    /**
     * Obtiene posición X aleatoria
     * @returns {number}
     */
    getRandomSpawnX() {
        const marginPercent = CONFIG.ENEMY.SPAWN.MARGIN_X_PERCENT;
        const margin = this.canvasWidth * marginPercent;
        return margin + Math.random() * (this.canvasWidth - 2 * margin);
    }

    /**
     * Intenta generar un nuevo enemigo
     */
    trySpawn() {
        const activeCount = this.enemies.filter(e => e.active).length;
        
        if (activeCount >= CONFIG.ENEMY.SPAWN.MAX_ACTIVE) {
            return;
        }
        
        const type = this.selectRandomType();
        const x = this.getRandomSpawnX();
        const y = CONFIG.ENEMY.SPAWN.SPAWN_Y;
        
        // Crear enemigo con patrón actual
        const enemy = new Enemy(x, y, type, this.currentPattern, this.canvasWidth);
        this.enemies.push(enemy);
        this.totalSpawned++;
    }

    /**
     * Actualiza todos los enemigos
     * @param {number} deltaTime - ms desde último frame
     * @returns {Array<Bullet>}
     */
    update(deltaTime = 16) {
        const bullets = [];
        
        // Incrementar tiempo global
        this.time++;
        
        // Sistema de spawn
        if (this.isSpawning) {
            this.timeSinceLastSpawn += deltaTime;
            
            if (this.timeSinceLastSpawn >= this.spawnInterval) {
                this.trySpawn();
                this.timeSinceLastSpawn = 0;
            }
        }
        
        // Actualizar cada enemigo
        this.enemies.forEach(enemy => {
            if (enemy.active) {
                enemy.update(this.time, this.canvasHeight);
                
                const bullet = enemy.shoot();
                if (bullet) bullets.push(bullet);
            }
        });
        
        // Limpiar enemigos inactivos
        if (this.enemies.length > 100) {
            this.enemies = this.enemies.filter(e => e.active);
        }
        
        return bullets;
    }

    /**
     * Dibuja todos los enemigos
     * @param {CanvasRenderingContext2D} ctx
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
                this.canvasHeight - 10
            );
        }
    }

    /**
     * Actualiza dimensiones del canvas (responsive)
     * @param {number} width
     * @param {number} height
     */
    updateCanvasSize(width, height) {
        this.canvasWidth = width;
        this.canvasHeight = height;
        
        // Actualizar enemigos existentes
        this.enemies.forEach(enemy => {
            enemy.canvasWidth = width;
        });
    }

    /**
     * Obtiene enemigos activos
     * @returns {Array<Enemy>}
     */
    getActiveEnemies() {
        return this.enemies.filter(e => e.active);
    }

    /**
     * Obtiene conteo de enemigos activos
     * @returns {number}
     */
    getActiveCount() {
        return this.getActiveEnemies().length;
    }

    /**
     * Verifica si alguno llegó al fondo
     * @returns {boolean}
     */
    hasReachedBottom() {
        return this.enemies.some(e => 
            e.active && e.hasReachedBottom(this.canvasHeight)
        );
    }

    /**
     * Pausar/reanudar spawn
     * @param {boolean} pause
     */
    setSpawning(pause) {
        this.isSpawning = !pause;
    }

    /**
     * Obtiene nombre del patrón actual
     * @returns {string}
     */
    getCurrentPatternName() {
        return this.currentPattern.description;
    }

    /**
     * Reinicia para nuevo nivel
     * @param {number} level
     */
    resetForLevel(level) {
        this.level = level;
        this.enemies = [];
        this.spawnInterval = Math.max(
            CONFIG.ENEMY.SPAWN.BASE_INTERVAL - (level * CONFIG.ENEMY.SPAWN.INTERVAL_DECREASE),
            CONFIG.ENEMY.SPAWN.MIN_INTERVAL
        );
        this.timeSinceLastSpawn = 0;
        this.totalSpawned = 0;
        this.time = 0;
        this.isSpawning = false;
        
        // Actualizar patrón
        this.currentPattern = this.getPatternForLevel(level);
        
        setTimeout(() => {
            this.isSpawning = true;
        }, CONFIG.ENEMY.SPAWN.INITIAL_DELAY);
        
        console.log(`🎮 Nivel ${level} - Patrón: ${this.currentPattern.description}`);
    }
}

// Alias
class EnemyManager extends EnemySpawner {}