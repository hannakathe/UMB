/* ===================================
   CORE/GAME.JS - Lógica Principal v3.0
   ===================================
   Con soporte para intro y patrones de movimiento.
*/

class Game {
    /**
     * Constructor del juego
     * @param {HTMLCanvasElement} canvas - Elemento canvas
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
        this.enemyManager = null;
        this.bullets = [];
        
        // Sistemas
        this.collisionSystem = new CollisionSystem();
        
        // Puntuación y progresión
        this.score = 0;
        this.level = 1;
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
        
        // FPS counter
        this.fps = 0;
        this.frameCount = 0;
        this.lastFpsUpdate = Date.now();
    }

    /**
     * Configura los event listeners del teclado
     */
    setupControls() {
        document.addEventListener('keydown', (e) => this.handleKeyDown(e));
        document.addEventListener('keyup', (e) => this.handleKeyUp(e));
    }

    /**
     * Maneja teclas presionadas
     * @param {KeyboardEvent} e - Evento de teclado
     */
    handleKeyDown(e) {
        // Intro screen: ENTER para empezar
        if (this.currentState === CONFIG.STATES.INTRO) {
            if (e.key === 'Enter') {
                this.exitIntro();
                e.preventDefault();
            }
            return;
        }

        // Controles del jugador (solo en estado PLAYING)
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

        // Controles de estado del juego
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
     * @param {KeyboardEvent} e - Evento de teclado
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
        // Ocultar intro
        if (this.ui.introScreen) {
            this.ui.introScreen.classList.remove('active');
            setTimeout(() => {
                this.ui.introScreen.style.display = 'none';
            }, 300);
        }
        
        // Mostrar juego
        if (this.ui.gameContainer) {
            this.ui.gameContainer.style.display = 'flex';
        }
        
        // Iniciar juego automáticamente
        this.start();
    }

    /**
     * Inicia una nueva partida
     */
    start() {
        this.currentState = CONFIG.STATES.PLAYING;
        this.score = 0;
        this.level = 1;
        
        // Crear entidades
        this.player = new Player(CONFIG.PLAYER.START_X, CONFIG.PLAYER.START_Y);
        this.enemyManager = new EnemyManager(this.level);
        
        // Limpiar arrays
        this.bullets = [];
        this.collisionSystem.clearParticles();
        
        // Ocultar mensaje
        this.hideMessage();
        
        // Actualizar UI
        this.updateUI();
    }

    /**
     * Pausa el juego
     */
    pause() {
        this.currentState = CONFIG.STATES.PAUSED;
        const msg = CONFIG.MESSAGES.PAUSED;
        this.showMessage(msg.TITLE, msg.TEXT);
    }

    /**
     * Reanuda el juego
     */
    resume() {
        this.currentState = CONFIG.STATES.PLAYING;
        this.hideMessage();
    }

    /**
     * Reinicia el juego
     */
    restart() {
        this.start();
    }

    /**
     * Termina el juego (Game Over)
     */
    gameOver() {
        this.currentState = CONFIG.STATES.GAME_OVER;
        
        // Guardar high score
        if (this.score > this.highScore) {
            this.highScore = this.score;
            this.saveHighScore(this.highScore);
            
            // Actualizar display
            if (this.ui.highScoreDisplay) {
                this.ui.highScoreDisplay.textContent = this.highScore;
            }
        }
        
        const msg = CONFIG.MESSAGES.GAME_OVER;
        this.showMessage(
            msg.TITLE,
            msg.TEXT(this.score, this.highScore)
        );
    }

    /**
     * Avanza al siguiente nivel
     */
    nextLevel() {
        this.level++;
        this.currentState = CONFIG.STATES.LEVEL_COMPLETE;
        
        // Reiniciar enemigos con nuevo nivel
        this.enemyManager.resetForLevel(this.level);
        
        // Limpiar balas
        this.bullets = [];
        
        // Obtener nombre del nuevo patrón
        const patternName = this.enemyManager.getCurrentPatternName();
        
        // Mostrar mensaje temporal
        const msg = CONFIG.MESSAGES.LEVEL_COMPLETE;
        this.showMessage(
            msg.TITLE(this.level),
            msg.TEXT(patternName)
        );
        
        setTimeout(() => {
            if (this.currentState === CONFIG.STATES.LEVEL_COMPLETE) {
                this.currentState = CONFIG.STATES.PLAYING;
                this.hideMessage();
            }
        }, 2500);
    }

    /**
     * Actualiza la lógica del juego (llamado cada frame)
     */
    update() {
        // Solo actualizar si el juego está activo
        if (this.currentState !== CONFIG.STATES.PLAYING) return;

        // Actualizar jugador
        this.player.update();

        // Manejar disparo del jugador
        if (this.player.keys.shoot) {
            const bullet = this.player.shoot();
            if (bullet) {
                this.bullets.push(bullet);
            }
        }

        // Actualizar enemigos y obtener sus disparos
        const enemyBullets = this.enemyManager.update();
        this.bullets.push(...enemyBullets);

        // Actualizar todas las balas
        this.bullets = this.bullets.filter(bullet => {
            bullet.update();
            return bullet.active;
        });

        // Detectar colisiones y actualizar puntuación
        const pointsEarned = this.collisionSystem.checkBulletEnemyCollisions(
            this.bullets,
            this.enemyManager.getActiveEnemies()
        );
        this.score += pointsEarned;

        // Colisiones balas enemigas vs jugador
        this.collisionSystem.checkEnemyBulletPlayerCollisions(
            this.bullets,
            this.player
        );

        // Colisión directa enemigos vs jugador
        this.collisionSystem.checkEnemyPlayerCollision(
            this.enemyManager.getActiveEnemies(),
            this.player
        );

        // Verificar condiciones de fin de juego
        this.checkGameConditions();

        // Actualizar UI
        this.updateUI();

        // Actualizar FPS counter
        if (CONFIG.DEBUG.SHOW_FPS) {
            this.updateFPS();
        }
    }

    /**
     * Verifica condiciones de victoria/derrota
     */
    checkGameConditions() {
        // Derrota: sin vidas
        if (!this.player.isAlive()) {
            this.gameOver();
            return;
        }

        // Derrota: invasión
        if (this.enemyManager.hasInvaded()) {
            this.gameOver();
            return;
        }

        // Victoria: todos los enemigos eliminados
        if (this.enemyManager.allDestroyed()) {
            this.nextLevel();
        }
    }

    /**
     * Dibuja todos los elementos en el canvas
     */
    draw() {
        // Limpiar canvas
        this.ctx.fillStyle = '#000000';
        this.ctx.fillRect(0, 0, this.width, this.height);

        // Dibujar estrellas de fondo
        this.drawStars();

        // Dibujar entidades si existen
        if (this.player) {
            this.player.draw(this.ctx);
        }

        if (this.enemyManager) {
            this.enemyManager.draw(this.ctx);
        }

        // Dibujar balas
        this.bullets.forEach(bullet => bullet.draw(this.ctx));

        // Dibujar partículas
        this.collisionSystem.updateAndDrawParticles(this.ctx);

        // Debug info
        if (CONFIG.DEBUG.ENABLED) {
            this.drawDebugInfo();
        }

        if (CONFIG.DEBUG.SHOW_FPS) {
            this.drawFPS();
        }
    }

    /**
     * Crea estrellas para el fondo
     * @returns {Array}
     */
    createStars() {
        const stars = [];
        const count = CONFIG.VISUAL.STAR_COUNT;

        for (let i = 0; i < count; i++) {
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
     * Dibuja y anima las estrellas del fondo
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
     * Actualiza la interfaz de usuario (HUD)
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
     * Muestra un mensaje en pantalla
     * @param {string} title - Título
     * @param {string} text - Texto (puede incluir HTML)
     */
    showMessage(title, text) {
        if (this.ui.messagePanel) {
            this.ui.messageTitle.textContent = title;
            this.ui.messageText.innerHTML = text;
            this.ui.messagePanel.classList.add('active');
        }
    }

    /**
     * Oculta el mensaje
     */
    hideMessage() {
        if (this.ui.messagePanel) {
            this.ui.messagePanel.classList.remove('active');
        }
    }

    /**
     * Guarda el high score
     * @param {number} score
     */
    saveHighScore(score) {
        try {
            localStorage.setItem(CONFIG.STORAGE.HIGH_SCORE_KEY, score.toString());
        } catch (e) {
            console.warn('No se pudo guardar el high score:', e);
        }
    }

    /**
     * Carga el high score
     * @returns {number}
     */
    loadHighScore() {
        try {
            const saved = localStorage.getItem(CONFIG.STORAGE.HIGH_SCORE_KEY);
            return saved ? parseInt(saved) : 0;
        } catch (e) {
            console.warn('No se pudo cargar el high score:', e);
            return 0;
        }
    }

    /**
     * Actualiza el contador de FPS
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
     * Dibuja el contador de FPS
     */
    drawFPS() {
        this.ctx.fillStyle = '#00ff00';
        this.ctx.font = 'bold 16px monospace';
        this.ctx.fillText(`FPS: ${this.fps}`, 10, 20);
    }

    /**
     * Dibuja información de debug
     */
    drawDebugInfo() {
        const lines = [
            `Estado: ${this.currentState}`,
            `Balas: ${this.bullets.length}`,
            `Partículas: ${this.collisionSystem.getParticleCount()}`,
            `Enemigos: ${this.enemyManager ? this.enemyManager.getActiveCount() : 0}`,
            `Patrón: ${this.enemyManager ? this.enemyManager.currentPattern.name : 'N/A'}`
        ];

        this.ctx.fillStyle = '#ffff00';
        this.ctx.font = '12px monospace';
        
        lines.forEach((line, i) => {
            this.ctx.fillText(line, 10, 40 + i * 15);
        });
    }

    /**
     * Obtiene el estado actual del juego
     * @returns {string}
     */
    getState() {
        return this.currentState;
    }
}