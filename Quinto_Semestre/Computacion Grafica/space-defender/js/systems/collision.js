/* ===================================
   SYSTEMS/COLLISION.JS - Sistema de Colisiones
   ===================================
   Detección AABB y gestión de partículas de explosión.
*/

class CollisionSystem {
    constructor() {
        this.particles = [];
    }

    /**
     * Detecta colisión AABB mejorada entre dos rectángulos
     * @param {Object} rect1 - Primer rectángulo {x, y, width, height}
     * @param {Object} rect2 - Segundo rectángulo {x, y, width, height}
     * @returns {boolean} True si hay colisión
     */
    static checkAABB(rect1, rect2) {
        return rect1.x < rect2.x + rect2.width &&
               rect1.x + rect1.width > rect2.x &&
               rect1.y < rect2.y + rect2.height &&
               rect1.y + rect1.height > rect2.y;
    }

    /**
     * Detecta colisión continua (previene tunneling)
     * Verifica si una bala atravesó un rectángulo entre este frame y el anterior
     * @param {Object} bullet - Bala con posición actual y anterior
     * @param {Object} rect - Rectángulo objetivo
     * @returns {boolean} True si hubo colisión
     */
    static checkContinuousCollision(bullet, rect) {
        // Colisión AABB normal
        const bulletBounds = bullet.getBounds();
        if (this.checkAABB(bulletBounds, rect)) {
            return true;
        }
        
        // Verificar si la bala "saltó sobre" el objetivo
        // (útil para balas muy rápidas)
        if (bullet.lastY !== undefined) {
            const minY = Math.min(bullet.y, bullet.lastY);
            const maxY = Math.max(bullet.y, bullet.lastY);
            
            // Verificar si el rectángulo está entre las dos posiciones Y
            const rectInPath = rect.y < maxY && rect.y + rect.height > minY;
            
            // Verificar alineación X
            const xAligned = bullet.x > rect.x && bullet.x < rect.x + rect.width;
            
            if (rectInPath && xAligned) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Verifica colisiones entre balas del jugador y enemigos
     * @param {Array<Bullet>} bullets - Array de balas
     * @param {Array<Enemy>} enemies - Array de enemigos
     * @returns {number} Puntos obtenidos
     */
    checkBulletEnemyCollisions(bullets, enemies) {
        let points = 0;

        bullets.forEach(bullet => {
            // Solo balas del jugador activas
            if (!bullet.active || !bullet.isPlayerBullet) return;

            enemies.forEach(enemy => {
                if (!enemy.active) return;

                // ⭐ Usar detección continua para balas rápidas
                const enemyBounds = enemy.getBounds();
                const collision = CollisionSystem.checkContinuousCollision(bullet, enemyBounds);
                
                if (collision) {
                    // Colisión detectada
                    bullet.destroy();
                    enemy.destroy();
                    points += enemy.points;

                    // ⭐ Audio aislado - NO PUEDE bloquear
                    try {
                        if (window.game && window.game.audioSystem) {
                            Promise.resolve().then(() => {
                                try {
                                    window.game.audioSystem.playSound('explosion', 0.5 + (enemy.type * 0.1));
                                } catch (e) { /* Silenciar */ }
                            });
                        }
                    } catch (e) { /* Silenciar */ }

                    // Crear efecto de explosión
                    this.createExplosion(enemy.x, enemy.y, enemy.color);
                }
            });
        });

        return points;
    }

    /**
     * Verifica colisiones entre balas enemigas y jugador
     * @param {Array<Bullet>} bullets - Array de balas
     * @param {Player} player - Jugador
     * @returns {boolean} True si el jugador fue impactado
     */
    checkEnemyBulletPlayerCollisions(bullets, player) {
        let hit = false;

        bullets.forEach(bullet => {
            // Solo balas enemigas activas
            if (!bullet.active || bullet.isPlayerBullet) return;

            if (CollisionSystem.checkAABB(bullet.getBounds(), player.getBounds())) {
                bullet.destroy();
                
                if (!player.invincible) {
                    player.takeDamage();
                    hit = true;
                    
                    // Efecto de impacto en jugador
                    this.createExplosion(player.x, player.y, '#ff0000');
                }
            }
        });

        return hit;
    }

    /**
     * Verifica colisión directa entre enemigos y jugador
     * @param {Array<Enemy>} enemies - Array de enemigos
     * @param {Player} player - Jugador
     * @returns {boolean} True si hubo colisión
     */
    checkEnemyPlayerCollision(enemies, player) {
        for (let enemy of enemies) {
            if (!enemy.active) continue;

            if (CollisionSystem.checkAABB(enemy.getBounds(), player.getBounds())) {
                if (!player.invincible) {
                    enemy.destroy();
                    player.takeDamage();
                    
                    // Explosión en punto de colisión
                    this.createExplosion(
                        (enemy.x + player.x) / 2,
                        (enemy.y + player.y) / 2,
                        '#ffff00'
                    );
                    
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Crea un efecto de explosión con partículas
     * @param {number} x - Posición X
     * @param {number} y - Posición Y
     * @param {string} color - Color de las partículas
     */
    createExplosion(x, y, color) {
        const particleCount = CONFIG.VISUAL.PARTICLE_COUNT;
        const particleLife = CONFIG.VISUAL.PARTICLE_LIFE;

        for (let i = 0; i < particleCount; i++) {
            const angle = (Math.PI * 2 * i) / particleCount;
            const speed = 2 + Math.random() * 3;
            
            this.particles.push({
                x: x,
                y: y,
                vx: Math.cos(angle) * speed,
                vy: Math.sin(angle) * speed,
                life: particleLife,
                maxLife: particleLife,
                color: color,
                size: 3 + Math.random() * 3
            });
        }
    }

    /**
     * Actualiza y dibuja todas las partículas
     * @param {CanvasRenderingContext2D} ctx - Contexto del canvas
     */
    updateAndDrawParticles(ctx) {
        for (let i = this.particles.length - 1; i >= 0; i--) {
            const p = this.particles[i];
            
            // Actualizar posición
            p.x += p.vx;
            p.y += p.vy;
            p.life--;

            // Eliminar partículas muertas
            if (p.life <= 0) {
                this.particles.splice(i, 1);
                continue;
            }

            // Dibujar partícula con fade out
            const alpha = p.life / p.maxLife;
            ctx.globalAlpha = alpha;
            ctx.fillStyle = p.color;
            ctx.fillRect(p.x - p.size / 2, p.y - p.size / 2, p.size, p.size);
        }
        
        // Resetear alpha
        ctx.globalAlpha = 1.0;
    }

    /**
     * Limpia todas las partículas
     */
    clearParticles() {
        this.particles = [];
    }

    /**
     * Obtiene el número de partículas activas
     * @returns {number} Conteo de partículas
     */
    getParticleCount() {
        return this.particles.length;
    }
}

/* ===================================
   NOTAS SOBRE AABB:
   ===================================
   
   AABB (Axis-Aligned Bounding Box) es ideal para:
   - Objetos rectangulares o cuadrados
   - Detección rápida (4 comparaciones)
   - Juegos 2D con vista cenital
   
   Fórmula:
   rect1 colisiona con rect2 SI:
   - rect1.left < rect2.right AND
   - rect1.right > rect2.left AND
   - rect1.top < rect2.bottom AND
   - rect1.bottom > rect2.top
   
   Alternativas para formas circulares:
   - Detección circular: distancia entre centros < suma de radios
   - SAT (Separating Axis Theorem): para polígonos complejos
*/