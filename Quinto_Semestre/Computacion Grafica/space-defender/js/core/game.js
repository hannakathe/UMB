/* ===================================
   CORE/GAME.JS - v4.0
   ===================================
   Sistema de spawn continuo con enemigos individuales.
*/

class Game {
    /**
     * Constructor del juego
     * @param {HTMLCanvasElement} canvas
     */
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');
        
        this.width = CONFIG.CANVAS.WIDTH;
        this.height = CONFIG.CANVAS.HEIGHT;
        
        // Estado inicial: INTRO
        this.currentState = CONFIG.STATES.INTRO;
        
        // Entidades del juego
        this.player = null;
        this.enemySpawner = null;
        this.bullets = [];
        
        // Sistemas
        this.collisionSystem = new CollisionSystem();
        
        // Puntuación y progresión
        this.score = 0;
        this.level = 1;
        this.enemiesKilled = 0;
        this.enemiesKilledThisLevel = 0;
        this.enemiesForNextLevel = 10; // Matar 10 para subir nivel
        this.highScore = this.loadHighScore();
        
        // Efectos visuales
        this.stars = this.createStars();
        
        // Referencias a elementos HTML
        this.ui = {
            score: document.getElementById('score'),
            level: document.getElementById('level'),
            lives: document.getElementById('lives'),
            messagePanel: document.getElementById('messagePanel'),
            messageTitle: document.getElementById('messageTitle'),
            messageText: document.getElementById('messageText'),
            highScoreDisplay: document.getElementById('highScoreDisplay'),
            introScreen: document.getElementById('introScreen'),
            gameContainer: document.getElementById('gameContainer')
        };
        
        // Actualizar high score display
        if (this.ui.highScoreDisplay) {
            this.ui.highScoreDisplay.textContent = this.highScore;
        }
        
        // Configurar controles
        this.setupControls();
        
        // FPS y delta time
        this.fps = 0;
        this.frameCount = 0;
        this.lastFpsUpdate = Date.now();
        this.lastFrameTime = Date.now();
    }

    /**
     * Configura los event listeners
     */
    setupControls() {
        document.addEventListener('keydown', (e) => this.handleKeyDown(e));
        document.addEventListener('keyup', (e) => this.handleKeyUp(e));
    }

    /**
     * Maneja teclas presionadas
     * @param {KeyboardEvent} e
     */
    handleKeyDown(e) {
        // Intro screen
        if (this.currentState === CONFIG.STATES.INTRO) {
            if (e.key === 'Enter') {
                this.exitIntro();
                e.preventDefault();
            }
            return;
        }

        // Controles del jugador
        if (this.player && this.currentState === CONFIG.STATES.PLAYING) {
            if (e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') {
                this.player.keys.left = true;
                e.preventDefault();
            }
            if (e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') {
                this.player.keys.right = true;
                e.preventDefault();
            }
            if (e.key === ' ') {
                this.player.keys.shoot = true;
                e.preventDefault();
            }
        }

        // Controles de estado
        if (e.key === 'p' || e.key === 'P') {
            if (this.currentState === CONFIG.STATES.PLAYING) {
                this.pause();
            } else if (this.currentState === CONFIG.STATES.PAUSED) {
                this.resume();
            }
            e.preventDefault();
        }

        if (e.key === 'r' || e.key === 'R') {
            if (this.currentState === CONFIG.STATES.GAME_OVER) {
                this.restart();
            }
            e.preventDefault();
        }
    }

    /**
     * Maneja teclas liberadas
     * @param {KeyboardEvent} e
     */
    handleKeyUp(e) {
        if (!this.player) return;

        if (e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') {
            this.player.keys.left = false;
        }
        if (e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') {
            this.player.keys.right = false;
        }
        if (e.key === ' ') {
            this.player.keys.shoot = false;
        }
    }

    /**
     * Sale de la pantalla de intro
     */
    exitIntro() {
        if (this.ui.introScreen) {
            this.ui.introScreen.classList.remove('active');
            setTimeout(() => {
                this.ui.introScreen.style.display = 'none';
            }, 300);
        }
        
        if (this.ui.gameContainer) {
            this.ui.gameContainer.style.display = 'flex';
        }
        
        this.start();
    }

    /**
     * Inicia una nueva partida
     */
    start() {
        this.currentState = CONFIG.STATES.PLAYING;
        this.score = 0;
        this.level = 1;
        this.enemiesKilled = 0;
        this.enemiesKilledThisLevel = 0;
        
        // Crear entidades
        this.player = new Player(CONFIG.PLAYER.START_X, CONFIG.PLAYER.START_Y);
        this.enemySpawner = new EnemySpawner(this.level);
        
        // Limpiar arrays
        this.bullets = [];
        this.collisionSystem.clearParticles();
        
        // Ocultar mensaje
        this.hideMessage();
        
        // Actualizar UI
        this.updateUI();
        
        console.log('🎮 ¡Juego iniciado!');
    }

    /**
     * Pausa el juego
     */
    pause() {
        this.currentState = CONFIG.STATES.PAUSED;
        if (this.enemySpawner) {
            this.enemySpawner.setSpawning(true); // Pausar spawn
        }
        const msg = CONFIG.MESSAGES.PAUSED;
        this.showMessage(msg.TITLE, msg.TEXT);
    }

    /**
     * Reanuda el juego
     */
    resume() {
        this.currentState = CONFIG.STATES.PLAYING;
        if (this.enemySpawner) {
            this.enemySpawner.setSpawning(false); // Reanudar spawn
        }
        this.hideMessage();
    }

    /**
     * Reinicia el juego
     */
    restart() {
        this.start();
    }

    /**
     * Termina el juego
     */
    gameOver() {
        this.currentState = CONFIG.STATES.GAME_OVER;
        
        // Pausar spawn
        if (this.enemySpawner) {
            this.enemySpawner.setSpawning(true);
        }
        
        // Guardar high score
        if (this.score > this.highScore) {
            this.highScore = this.score;
            this.saveHighScore(this.highScore);
            
            if (this.ui.highScoreDisplay) {
                this.ui.highScoreDisplay.textContent = this.highScore;
            }
        }
        
        const msg = CONFIG.MESSAGES.GAME_OVER;
        this.showMessage(
            msg.TITLE,
            msg.TEXT(this.score, this.highScore)
        );
        
        console.log(`💀 Game Over - Puntuación: ${this.score}`);
    }

    /**
     * Avanza al siguiente nivel
     */
    nextLevel() {
        this.level++;
        this.enemiesKilledThisLevel = 0;
        
        // Aumentar dificultad
        if (this.enemySpawner) {
            this.enemySpawner.resetForLevel(this.level);
        }
        
        // Mensaje breve (no pausar juego)
        console.log(`⭐ ¡Nivel ${this.level}!`);
        
        // Mostrar nivel en HUD (ya se actualiza automáticamente)
    }

    /**
     * Actualiza la lógica del juego
     */
    update() {
        // Solo actualizar si está jugando
        if (this.currentState !== CONFIG.STATES.PLAYING) return;

        // Calcular delta time
        const currentTime = Date.now();
        const deltaTime = currentTime - this.lastFrameTime;
        this.lastFrameTime = currentTime;

        // Actualizar jugador
        this.player.update();

        // Manejar disparo
        if (this.player.keys.shoot) {
            const bullet = this.player.shoot();
            if (bullet) {
                this.bullets.push(bullet);
            }
        }

        // Actualizar enemigos y obtener disparos
        if (this.enemySpawner) {
            const enemyBullets = this.enemySpawner.update(deltaTime);
            this.bullets.push(...enemyBullets);
        }

        // Actualizar balas
        this.bullets = this.bullets.filter(bullet => {
            bullet.update();
            return bullet.active;
        });

        // Detectar colisiones
        const pointsEarned = this.collisionSystem.checkBulletEnemyCollisions(
            this.bullets,
            this.enemySpawner ? this.enemySpawner.getActiveEnemies() : []
        );
        
        if (pointsEarned > 0) {
            this.score += pointsEarned;
            const enemiesDestroyed = Math.floor(pointsEarned / 10); // Aproximado
            this.enemiesKilled += enemiesDestroyed;
            this.enemiesKilledThisLevel += enemiesDestroyed;
            
            // Verificar si sube de nivel
            if (this.enemiesKilledThisLevel >= this.enemiesForNextLevel) {
                this.nextLevel();
            }
        }

        // Colisiones balas enemigas vs jugador
        this.collisionSystem.checkEnemyBulletPlayerCollisions(
            this.bullets,
            this.player
        );

        // Colisión directa enemigos vs jugador
        this.collisionSystem.checkEnemyPlayerCollision(
            this.enemySpawner ? this.enemySpawner.getActiveEnemies() : [],
            this.player
        );

        // Verificar game over
        this.checkGameConditions();

        // Actualizar UI
        this.updateUI();

        // FPS
        if (CONFIG.DEBUG.SHOW_FPS) {
            this.updateFPS();
        }
    }

    /**
     * Verifica condiciones de derrota
     */
    checkGameConditions() {
        // Sin vidas
        if (!this.player.isAlive()) {
            this.gameOver();
            return;
        }

        // Enemigos llegaron al fondo
        if (this.enemySpawner && this.enemySpawner.hasReachedBottom()) {
            this.gameOver();
        }
    }

    /**
     * Dibuja todo en el canvas
     */
    draw() {
        // Limpiar
        this.ctx.fillStyle = '#000000';
        this.ctx.fillRect(0, 0, this.width, this.height);

        // Fondo
        this.drawStars();

        // Entidades
        if (this.player) {
            this.player.draw(this.ctx);
        }

        if (this.enemySpawner) {
            this.enemySpawner.draw(this.ctx);
        }

        // Balas
        this.bullets.forEach(bullet => bullet.draw(this.ctx));

        // Partículas
        this.collisionSystem.updateAndDrawParticles(this.ctx);

        // Debug
        if (CONFIG.DEBUG.ENABLED) {
            this.drawDebugInfo();
        }

        if (CONFIG.DEBUG.SHOW_FPS) {
            this.drawFPS();
        }
    }

    /**
     * Crea estrellas
     * @returns {Array}
     */
    createStars() {
        const stars = [];
        for (let i = 0; i < CONFIG.VISUAL.STAR_COUNT; i++) {
            stars.push({
                x: Math.random() * this.width,
                y: Math.random() * this.height,
                size: Math.random() * 2,
                speed: Math.random() * 0.5 + 0.1
            });
        }
        return stars;
    }

    /**
     * Dibuja estrellas
     */
    drawStars() {
        this.ctx.fillStyle = '#ffffff';
        this.stars.forEach(star => {
            this.ctx.fillRect(star.x, star.y, star.size, star.size);
            star.y += star.speed;
            if (star.y > this.height) {
                star.y = 0;
                star.x = Math.random() * this.width;
            }
        });
    }

    /**
     * Actualiza UI
     */
    updateUI() {
        if (this.ui.score) {
            this.ui.score.textContent = this.score;
        }
        if (this.ui.level) {
            this.ui.level.textContent = this.level;
        }
        if (this.ui.lives && this.player) {
            this.ui.lives.textContent = '❤️'.repeat(Math.max(0, this.player.lives));
        }
    }

    /**
     * Muestra mensaje
     * @param {string} title
     * @param {string} text
     */
    showMessage(title, text) {
        if (this.ui.messagePanel) {
            this.ui.messageTitle.textContent = title;
            this.ui.messageText.innerHTML = text;
            this.ui.messagePanel.classList.add('active');
        }
    }

    /**
     * Oculta mensaje
     */
    hideMessage() {
        if (this.ui.messagePanel) {
            this.ui.messagePanel.classList.remove('active');
        }
    }

    /**
     * Guarda high score
     * @param {number} score
     */
    saveHighScore(score) {
        try {
            localStorage.setItem(CONFIG.STORAGE.HIGH_SCORE_KEY, score.toString());
        } catch (e) {
            console.warn('No se pudo guardar el high score');
        }
    }

    /**
     * Carga high score
     * @returns {number}
     */
    loadHighScore() {
        try {
            const saved = localStorage.getItem(CONFIG.STORAGE.HIGH_SCORE_KEY);
            return saved ? parseInt(saved) : 0;
        } catch (e) {
            return 0;
        }
    }

    /**
     * Actualiza FPS
     */
    updateFPS() {
        this.frameCount++;
        const now = Date.now();
        if (now - this.lastFpsUpdate >= 1000) {
            this.fps = this.frameCount;
            this.frameCount = 0;
            this.lastFpsUpdate = now;
        }
    }

    /**
     * Dibuja FPS
     */
    drawFPS() {
        this.ctx.fillStyle = '#00ff00';
        this.ctx.font = 'bold 16px monospace';
        this.ctx.fillText(`FPS: ${this.fps}`, 10, 20);
    }

    /**
     * Dibuja info de debug
     */
    drawDebugInfo() {
        const lines = [
            `Estado: ${this.currentState}`,
            `Balas: ${this.bullets.length}`,
            `Kills: ${this.enemiesKilledThisLevel}/${this.enemiesForNextLevel}`,
            `Total: ${this.enemiesKilled}`
        ];

        this.ctx.fillStyle = '#ffff00';
        this.ctx.font = '12px monospace';
        lines.forEach((line, i) => {
            this.ctx.fillText(line, 10, 40 + i * 15);
        });
    }

    /**
     * Obtiene estado actual
     * @returns {string}
     */
    getState() {
        return this.currentState;
    }
}