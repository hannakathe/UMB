/* ===================================
   ENTITIES/BULLET.JS - Clase Proyectil
   ===================================
   Maneja balas del jugador y enemigos.
*/

class Bullet {
    /**
     * Constructor de la bala
     * @param {number} x - Posición X inicial
     * @param {number} y - Posición Y inicial
     * @param {boolean} isPlayerBullet - Si pertenece al jugador
     */
    constructor(x, y, isPlayerBullet = true) {
        this.x = x;
        this.y = y;
        this.lastY = y;  // Para detección continua
        this.isPlayerBullet = isPlayerBullet;
        
        // Obtener configuración desde CONFIG
        this.width = CONFIG.BULLET.WIDTH;
        this.height = CONFIG.BULLET.HEIGHT;
        
        // Velocidad y color según quien dispara
        if (isPlayerBullet) {
            this.speed = CONFIG.BULLET.PLAYER_SPEED;
            this.color = CONFIG.BULLET.PLAYER_COLOR;
        } else {
            this.speed = CONFIG.BULLET.ENEMY_SPEED;
            this.color = CONFIG.BULLET.ENEMY_COLOR;
        }
        
        // Estado
        this.active = true;
    }

    /**
     * Actualiza la posición de la bala
     */
    update() {
        // Guardar posición anterior para detección continua
        this.lastY = this.y;
        
        // Mover verticalmente
        this.y += this.speed;

        // Desactivar si sale de la pantalla
        if (this.y < -this.height || this.y > CONFIG.CANVAS.HEIGHT + this.height) {
            this.active = false;
        }
    }

    /**
     * Dibuja la bala en el canvas
     * @param {CanvasRenderingContext2D} ctx - Contexto del canvas
     */
    draw(ctx) {
        // Efecto de brillo
        ctx.shadowBlur = 10;
        ctx.shadowColor = this.color;

        // Dibujar la bala como rectángulo
        ctx.fillStyle = this.color;
        ctx.fillRect(
            this.x - this.width / 2,
            this.y,
            this.width,
            this.height
        );

        // Resetear sombra
        ctx.shadowBlur = 0;

        // Debug: mostrar hitbox
        if (CONFIG.DEBUG.SHOW_HITBOXES) {
            const bounds = this.getBounds();
            ctx.strokeStyle = this.isPlayerBullet ? '#00ff00' : '#ff0000';
            ctx.strokeRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    /**
     * Obtiene el rectángulo de colisión
     * @returns {Object} Rectángulo {x, y, width, height}
     */
    getBounds() {
        return {
            x: this.x - this.width / 2,
            y: this.y,
            width: this.width,
            height: this.height
        };
    }

    /**
     * Desactiva la bala (la marca para eliminación)
     */
    destroy() {
        this.active = false;
    }
}