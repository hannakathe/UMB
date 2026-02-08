/* ===================================
   ENTITIES/PLAYER.JS - Clase Jugador
   ===================================
   Maneja la nave del jugador, input y estado.
*/

class Player {
    /**
     * Constructor del jugador
     * @param {number} x - Posición X inicial
     * @param {number} y - Posición Y inicial
     */
    constructor(x, y) {
        this.x = x;
        this.y = y;
        
        // Obtener configuración desde CONFIG
        this.width = CONFIG.PLAYER.WIDTH;
        this.height = CONFIG.PLAYER.HEIGHT;
        this.speed = CONFIG.PLAYER.SPEED;
        this.color = CONFIG.PLAYER.COLOR;
        
        // Sistema de vidas
        this.lives = CONFIG.PLAYER.INITIAL_LIVES;
        this.maxLives = CONFIG.PLAYER.INITIAL_LIVES;
        
        // Control de disparo
        this.canShoot = true;
        this.shootCooldown = CONFIG.PLAYER.SHOOT_COOLDOWN;
        
        // Invencibilidad temporal
        this.invincible = CONFIG.DEBUG.GOD_MODE;
        this.invincibleTime = CONFIG.PLAYER.INVINCIBLE_TIME;
        this.blinkTimer = 0;
        
        // Estado de las teclas
        this.keys = {
            left: false,
            right: false,
            shoot: false
        };
    }

    /**
     * Actualiza la lógica del jugador
     * @param {number} canvasWidth - Ancho del canvas (para responsive)
     */
    update(canvasWidth = CONFIG.CANVAS.BASE_WIDTH) {
        // Movimiento horizontal
        if (this.keys.left) {
            this.x -= this.speed;
        }
        if (this.keys.right) {
            this.x += this.speed;
        }

        // Limitar a los bordes del canvas
        const halfWidth = this.width / 2;
        const margin = 10; // Margen de seguridad para evitar que se salga
        
        // Límite izquierdo
        const minX = halfWidth + margin;
        if (this.x < minX) {
            this.x = minX;
        }
        
        // Límite derecho
        const maxX = canvasWidth - halfWidth - margin;
        if (this.x > maxX) {
            this.x = maxX;
        }

        // Actualizar timer de parpadeo si es invencible
        if (this.invincible && !CONFIG.DEBUG.GOD_MODE) {
            this.blinkTimer += 16; // ~60fps
        }
    }

    /**
     * Dibuja la nave en el canvas
     * @param {CanvasRenderingContext2D} ctx - Contexto del canvas
     */
    draw(ctx) {
        // Parpadeo durante invencibilidad
        if (this.invincible && !CONFIG.DEBUG.GOD_MODE) {
            // Parpadear cada 200ms
            if (Math.floor(this.blinkTimer / 200) % 2 === 0) {
                return; // No dibujar en algunos frames
            }
        }

        // Efecto de brillo
        ctx.shadowBlur = CONFIG.VISUAL.SHADOW_BLUR;
        ctx.shadowColor = this.color;

        // Dibujar nave triangular
        ctx.fillStyle = this.color;
        ctx.beginPath();
        
        const halfWidth = this.width / 2;
        const halfHeight = this.height / 2;
        
        // Punta superior
        ctx.moveTo(this.x, this.y - halfHeight);
        
        // Esquina inferior izquierda
        ctx.lineTo(this.x - halfWidth, this.y + halfHeight);
        
        // Centro inferior con hendidura
        ctx.lineTo(this.x - this.width / 6, this.y + halfHeight / 1.5);
        ctx.lineTo(this.x + this.width / 6, this.y + halfHeight / 1.5);
        
        // Esquina inferior derecha
        ctx.lineTo(this.x + halfWidth, this.y + halfHeight);
        
        ctx.closePath();
        ctx.fill();

        // Cockpit (ventana de la nave)
        ctx.fillStyle = '#ffffff';
        ctx.beginPath();
        ctx.arc(this.x, this.y - 5, 5, 0, Math.PI * 2);
        ctx.fill();

        // Resetear sombra
        ctx.shadowBlur = 0;

        // Debug: mostrar hitbox
        if (CONFIG.DEBUG.SHOW_HITBOXES) {
            const bounds = this.getBounds();
            ctx.strokeStyle = '#00ff00';
            ctx.lineWidth = 2;
            ctx.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        // Debug: mostrar estado
        if (CONFIG.DEBUG.ENABLED) {
            ctx.fillStyle = '#ffffff';
            ctx.font = '12px monospace';
            ctx.fillText(`Lives: ${this.lives}`, this.x - 20, this.y - 40);
            if (this.invincible) {
                ctx.fillText('INVINCIBLE', this.x - 30, this.y - 55);
            }
        }
    }

    /**
     * Intenta disparar una bala
     * @returns {Bullet|null} Nueva bala o null si no puede disparar
     */
    shoot() {
        if (this.canShoot) {
            this.canShoot = false;
            
            // ⭐ Audio completamente aislado - NO PUEDE bloquear
            try {
                if (window.game && window.game.audioSystem) {
                    // Ejecutar en microtask separada
                    Promise.resolve().then(() => {
                        try {
                            window.game.audioSystem.playSound('playerShoot');
                        } catch (e) {
                            // Silenciar completamente cualquier error
                        }
                    });
                }
            } catch (e) {
                // Silenciar completamente cualquier error
            }
            
            // Reiniciar cooldown
            setTimeout(() => {
                this.canShoot = true;
            }, this.shootCooldown);

            // Crear y retornar nueva bala del jugador
            return new Bullet(
                this.x,
                this.y - this.height / 2,
                true  // isPlayerBullet
            );
        }
        return null;
    }

    /**
     * El jugador recibe daño
     */
    takeDamage() {
        if (!this.invincible) {
            this.lives--;
            
            //Audio aislado - NO PUEDE bloquear
            try {
                if (window.game && window.game.audioSystem && this.lives > 0) {
                    Promise.resolve().then(() => {
                        try {
                            window.game.audioSystem.playSound('playerHit', 0.8);
                        } catch (e) { /* Silenciar */ }
                    });
                }
            } catch (e) { /* Silenciar */ }
            
            if (this.lives > 0) {
                // Activar invencibilidad temporal
                this.invincible = true;
                this.blinkTimer = 0;
                
                setTimeout(() => {
                    this.invincible = false;
                }, this.invincibleTime);
            }
        }
    }

    /**
     * Recupera una vida (power-up futuro)
     */
    heal() {
        if (this.lives < this.maxLives) {
            this.lives++;
        }
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
     * Verifica si el jugador está vivo
     * @returns {boolean} True si tiene vidas
     */
    isAlive() {
        return this.lives > 0;
    }

    /**
     * Reinicia el estado del jugador
     */
    reset() {
        this.x = CONFIG.PLAYER.START_X;
        this.y = CONFIG.PLAYER.START_Y;
        this.lives = CONFIG.PLAYER.INITIAL_LIVES;
        this.invincible = CONFIG.DEBUG.GOD_MODE;
        this.blinkTimer = 0;
        this.canShoot = true;
        this.keys = {
            left: false,
            right: false,
            shoot: false
        };
    }
}