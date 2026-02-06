/* ===================================
   ENTITIES/ENEMY.JS - v4.0
   ===================================
   Enemigos individuales que bajan a diferentes velocidades.
*/

class Enemy {
    /**
     * Constructor del enemigo individual
     * @param {number} x - Posición X inicial
     * @param {number} y - Posición Y inicial (típicamente fuera de pantalla)
     * @param {number} type - Tipo de enemigo (1, 2, o 3)
     */
    constructor(x, y, type = 1) {
        this.x = x;
        this.y = y;
        this.type = Math.min(Math.max(type, 1), 3); // Entre 1 y 3
        
        // Obtener configuración del tipo
        this.width = CONFIG.ENEMY.WIDTH;
        this.height = CONFIG.ENEMY.HEIGHT;
        
        const typeConfig = CONFIG.ENEMY.TYPES[this.type];
        this.points = typeConfig.POINTS;
        this.color = typeConfig.COLOR;
        this.shootChance = typeConfig.SHOOT_CHANCE;
        this.speed = typeConfig.SPEED; // ⭐ Velocidad individual según tipo
        this.name = typeConfig.NAME;
        
        // Movimiento horizontal aleatorio (opcional)
        if (CONFIG.ENEMY.HORIZONTAL_MOVEMENT.ENABLED) {
            this.horizontalSpeed = (Math.random() - 0.5) * CONFIG.ENEMY.HORIZONTAL_MOVEMENT.MAX_SPEED;
        } else {
            this.horizontalSpeed = 0;
        }
        
        // Estado
        this.active = true;
        
        // Animación
        this.animationFrame = 0;
        this.animationCounter = 0;
        this.animationSpeed = CONFIG.ENEMY.ANIMATION_SPEED;
        
        // ID único para tracking
        this.id = Date.now() + Math.random();
    }

    /**
     * Actualiza el estado del enemigo (cada uno se mueve independiente)
     */
    update() {
        // ⭐ Movimiento vertical - cada enemigo a SU propia velocidad
        this.y += this.speed;
        
        // Movimiento horizontal aleatorio (opcional)
        if (CONFIG.ENEMY.HORIZONTAL_MOVEMENT.ENABLED) {
            this.x += this.horizontalSpeed;
            
            // Cambiar dirección aleatoriamente
            if (Math.random() < CONFIG.ENEMY.HORIZONTAL_MOVEMENT.CHANGE_CHANCE) {
                this.horizontalSpeed = (Math.random() - 0.5) * CONFIG.ENEMY.HORIZONTAL_MOVEMENT.MAX_SPEED;
            }
            
            // Rebotar en los bordes
            if (this.x < this.width / 2) {
                this.x = this.width / 2;
                this.horizontalSpeed = Math.abs(this.horizontalSpeed);
            }
            if (this.x > CONFIG.CANVAS.WIDTH - this.width / 2) {
                this.x = CONFIG.CANVAS.WIDTH - this.width / 2;
                this.horizontalSpeed = -Math.abs(this.horizontalSpeed);
            }
        }
        
        // Desactivar si sale de la pantalla (abajo)
        if (this.y > CONFIG.PHYSICS.ENEMY_DESPAWN_Y) {
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

        if (CONFIG.DEBUG.SHOW_HITBOXES) {
            const bounds = this.getBounds();
            ctx.strokeStyle = this.color;
            ctx.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
            
            // Mostrar velocidad
            ctx.fillStyle = '#ffffff';
            ctx.font = '10px monospace';
            ctx.fillText(`${this.speed.toFixed(1)}`, this.x - 10, this.y - 20);
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
     * Verifica si llegó al fondo (peligro)
     * @returns {boolean}
     */
    hasReachedBottom() {
        return this.y + this.height / 2 >= CONFIG.CANVAS.HEIGHT - 50;
    }

    /**
     * Destruye el enemigo
     */
    destroy() {
        this.active = false;
    }
}

/* ===================================
   ENEMY SPAWNER v4.0
   ===================================
   Genera enemigos individuales de forma progresiva.
*/

class EnemySpawner {
    /**
     * Constructor del spawner
     * @param {number} level - Nivel actual
     */
    constructor(level = 1) {
        this.enemies = [];
        this.level = level;
        
        // Sistema de spawn
        this.spawnInterval = Math.max(
            CONFIG.ENEMY.SPAWN.BASE_INTERVAL - (level * CONFIG.ENEMY.SPAWN.INTERVAL_DECREASE),
            CONFIG.ENEMY.SPAWN.MIN_INTERVAL
        );
        this.timeSinceLastSpawn = 0;
        this.isSpawning = false;
        
        // Estadísticas
        this.totalSpawned = 0;
        this.enemiesKilled = 0;
        
        // Iniciar spawn después de un delay
        setTimeout(() => {
            this.isSpawning = true;
        }, CONFIG.ENEMY.SPAWN.INITIAL_DELAY);
        
        console.log(`🎮 Nivel ${level} - Spawn cada ${this.spawnInterval}ms`);
    }

    /**
     * Selecciona un tipo de enemigo aleatorio basado en pesos
     * @returns {number} Tipo de enemigo (1, 2, o 3)
     */
    selectRandomType() {
        const types = CONFIG.ENEMY.TYPES;
        const weights = [];
        
        // Construir array de pesos
        for (let type in types) {
            for (let i = 0; i < types[type].SPAWN_WEIGHT; i++) {
                weights.push(parseInt(type));
            }
        }
        
        // Seleccionar aleatoriamente
        return weights[Math.floor(Math.random() * weights.length)];
    }

    /**
     * Genera una posición X aleatoria para spawn
     * @returns {number} Posición X
     */
    getRandomSpawnX() {
        const margin = CONFIG.ENEMY.SPAWN.MARGIN_X;
        return margin + Math.random() * (CONFIG.CANVAS.WIDTH - 2 * margin);
    }

    /**
     * Intenta generar un nuevo enemigo
     */
    trySpawn() {
        // Verificar si podemos spawnear
        const activeCount = this.enemies.filter(e => e.active).length;
        
        if (activeCount >= CONFIG.ENEMY.SPAWN.MAX_ACTIVE) {
            return; // Demasiados enemigos en pantalla
        }
        
        // Crear nuevo enemigo
        const type = this.selectRandomType();
        const x = this.getRandomSpawnX();
        const y = CONFIG.ENEMY.SPAWN.SPAWN_Y;
        
        const enemy = new Enemy(x, y, type);
        this.enemies.push(enemy);
        this.totalSpawned++;
        
        if (CONFIG.DEBUG.SHOW_SPAWN_INFO) {
            console.log(`👾 Spawn #${this.totalSpawned}: ${enemy.name} en (${x.toFixed(0)}, ${y})`);
        }
    }

    /**
     * Actualiza todos los enemigos y el sistema de spawn
     * @param {number} deltaTime - Tiempo desde último frame (ms)
     * @returns {Array<Bullet>} Balas disparadas
     */
    update(deltaTime = 16) {
        const bullets = [];
        
        // Sistema de spawn automático
        if (this.isSpawning) {
            this.timeSinceLastSpawn += deltaTime;
            
            if (this.timeSinceLastSpawn >= this.spawnInterval) {
                this.trySpawn();
                this.timeSinceLastSpawn = 0;
            }
        }
        
        // Actualizar cada enemigo individualmente
        this.enemies.forEach(enemy => {
            if (enemy.active) {
                enemy.update();
                
                // Intentar disparar
                const bullet = enemy.shoot();
                if (bullet) bullets.push(bullet);
            }
        });
        
        // Limpiar enemigos inactivos (optimización)
        if (this.enemies.length > 100) {
            this.enemies = this.enemies.filter(e => e.active);
        }
        
        return bullets;
    }

    /**
     * Dibuja todos los enemigos activos
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
                `Enemigos: ${this.getActiveCount()}/${CONFIG.ENEMY.SPAWN.MAX_ACTIVE} | Spawned: ${this.totalSpawned}`,
                10,
                CONFIG.CANVAS.HEIGHT - 10
            );
            
            if (CONFIG.DEBUG.SHOW_SPAWN_INFO) {
                ctx.fillText(
                    `Next spawn: ${((this.spawnInterval - this.timeSinceLastSpawn) / 1000).toFixed(1)}s`,
                    10,
                    CONFIG.CANVAS.HEIGHT - 25
                );
            }
        }
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
     * Verifica si algún enemigo llegó al fondo (game over)
     * @returns {boolean}
     */
    hasReachedBottom() {
        return this.enemies.some(enemy => 
            enemy.active && enemy.hasReachedBottom()
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
     * Reinicia el spawner para un nuevo nivel
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
        this.enemiesKilled = 0;
        this.isSpawning = false;
        
        setTimeout(() => {
            this.isSpawning = true;
        }, CONFIG.ENEMY.SPAWN.INITIAL_DELAY);
        
        console.log(`🎮 Nivel ${level} - Spawn cada ${this.spawnInterval}ms`);
    }
}

// Alias para compatibilidad con código existente
class EnemyManager extends EnemySpawner {}