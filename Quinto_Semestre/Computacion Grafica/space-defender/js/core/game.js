/* ===================================
   CORE/GAME.JS - Lógica principal del juego
   ===================================
   Sistema responsive con patrones de movimiento.
*/

class Game {
    /**
     * Constructor del juego
     * @param {HTMLCanvasElement} canvas
     */
    constructor(canvas) {
        this.canvas = canvas;
        this.ctx = canvas.getContext('2d');
        
        // Dimensiones base (lógicas)
        this.baseWidth = CONFIG.CANVAS.BASE_WIDTH;
        this.baseHeight = CONFIG.CANVAS.BASE_HEIGHT;
        
        // Dimensiones actuales (físicas del canvas)
        this.width = this.baseWidth;
        this.height = this.baseHeight;
        
        // Factor de escala para responsive
        this.scale = 1;
        
        // Configurar canvas responsive
        this.setupResponsiveCanvas();
        
        // Estado inicial
        this.currentState = CONFIG.STATES.INTRO;
        
        // Entidades
        this.player = null;
        this.enemySpawner = null;
        this.bullets = [];
        
        // Sistemas
        this.collisionSystem = new CollisionSystem();
        this.audioSystem = new AudioSystem();  // ⭐ Sistema de audio
        
        // Puntuación
        this.score = 0;
        this.level = 1;
        this.enemiesKilled = 0;
        this.enemiesKilledThisLevel = 0;
        this.enemiesForNextLevel = 10;
        

        this.scoreForNextLevel = 150;  // Puntos necesarios para subir
        this.scoreThisLevel = 0;        // Puntos acumulados este nivel
     
        
        this.highScore = this.loadHighScore();
        
        // Efectos
        this.stars = this.createStars();
        
        // UI Referencias
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
        
        if (this.ui.highScoreDisplay) {
            this.ui.highScoreDisplay.textContent = this.highScore;
        }
        
        // Controles
        this.setupControls();
        
        // FPS
        this.fps = 0;
        this.frameCount = 0;
        this.lastFpsUpdate = Date.now();
        this.lastFrameTime = Date.now();
        
        // Listener para resize
        window.addEventListener('resize', () => this.handleResize());
    }

    /**
     * Configura el canvas para ser responsive
     */
    setupResponsiveCanvas() {
        this.resizeCanvas();
    }

    /**
     * Redimensiona el canvas manteniendo aspect ratio
     */
    resizeCanvas() {
        const container = this.canvas.parentElement;
        const containerWidth = container.clientWidth - 80; // Margen
        const containerHeight = window.innerHeight - 200; // Espacio para UI
        
        // Calcular escala manteniendo aspect ratio
        const scaleX = containerWidth / this.baseWidth;
        const scaleY = containerHeight / this.baseHeight;
        this.scale = Math.min(scaleX, scaleY, 1); // Máximo 1x
        
        // Aplicar nuevas dimensiones
        this.width = this.baseWidth * this.scale;
        this.height = this.baseHeight * this.scale;
        
        // Configurar canvas físico
        this.canvas.width = this.baseWidth;
        this.canvas.height = this.baseHeight;
        
        // El CSS maneja el escalado visual
        this.canvas.style.width = this.width + 'px';
        this.canvas.style.height = this.height + 'px';
        
        // Actualizar enemySpawner si existe
        if (this.enemySpawner) {
            this.enemySpawner.updateCanvasSize(this.baseWidth, this.baseHeight);
        }
        
        // Actualizar jugador si existe
        if (this.player) {
            this.player.x = this.baseWidth * CONFIG.PLAYER.START_X_PERCENT;
            this.player.y = this.baseHeight * CONFIG.PLAYER.START_Y_PERCENT;
        }
    }

    /**
     * Maneja el evento resize
     */
    handleResize() {
        this.resizeCanvas();
    }

    /**
     * Configura controles
     */
    setupControls() {
        // Controles de teclado
        document.addEventListener('keydown', (e) => this.handleKeyDown(e));
        document.addEventListener('keyup', (e) => this.handleKeyUp(e));
        
        // Controles de volumen
        this.setupVolumeControls();
    }
    
    /**
     * Configura los controles de volumen de la UI
     */
    setupVolumeControls() {
        const volumeMaster = document.getElementById('volumeMaster');
        const volumeSFX = document.getElementById('volumeSFX');
        const volumeMusic = document.getElementById('volumeMusic');
        const muteButton = document.getElementById('muteButton');
        
        // Valores de display
        const volumeMasterValue = document.getElementById('volumeMasterValue');
        const volumeSFXValue = document.getElementById('volumeSFXValue');
        const volumeMusicValue = document.getElementById('volumeMusicValue');
        
        // Master volume
        if (volumeMaster) {
            volumeMaster.addEventListener('input', (e) => {
                const value = e.target.value / 100;
                this.audioSystem.setMasterVolume(value);
                if (volumeMasterValue) {
                    volumeMasterValue.textContent = e.target.value + '%';
                }
            });
        }
        
        // SFX volume
        if (volumeSFX) {
            volumeSFX.addEventListener('input', (e) => {
                const value = e.target.value / 100;
                this.audioSystem.setSFXVolume(value);
                if (volumeSFXValue) {
                    volumeSFXValue.textContent = e.target.value + '%';
                }
            });
        }
        
        // Music volume
        if (volumeMusic) {
            volumeMusic.addEventListener('input', (e) => {
                const value = e.target.value / 100;
                this.audioSystem.setMusicVolume(value);
                if (volumeMusicValue) {
                    volumeMusicValue.textContent = e.target.value + '%';
                }
            });
        }
        
        // Mute button
        if (muteButton) {
            muteButton.addEventListener('click', () => {
                const isMuted = this.audioSystem.toggleMute();
                muteButton.textContent = isMuted ? '🔊 Unmute' : '🔇 Mute';
                muteButton.classList.toggle('muted', isMuted);
            });
        }
    }

    /**
     * Maneja teclas presionadas
     */
    handleKeyDown(e) {
        if (this.currentState === CONFIG.STATES.INTRO) {
            if (e.key === 'Enter') {
                this.exitIntro();
                e.preventDefault();
            }
            return;
        }

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
     * Sale de intro
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
        
        try {
            Promise.resolve().then(() => {
                try {
                    this.audioSystem.playMusic();
                } catch (e) { /* Silenciar */ }
            });
        } catch (e) { /* Silenciar */ }
        
        this.start();
    }

    /**
     * Inicia el juego
     */
    start() {
        this.currentState = CONFIG.STATES.PLAYING;
        this.score = 0;
        this.level = 1;
        this.enemiesKilled = 0;
        this.enemiesKilledThisLevel = 0;
        
        // Crear entidades con dimensiones base
        const playerX = this.baseWidth * CONFIG.PLAYER.START_X_PERCENT;
        const playerY = this.baseHeight * CONFIG.PLAYER.START_Y_PERCENT;
        this.player = new Player(playerX, playerY);
        
        this.enemySpawner = new EnemySpawner(this.level, this.baseWidth, this.baseHeight);
        
        this.bullets = [];
        this.collisionSystem.clearParticles();
        
        this.hideMessage();
        this.updateUI();
        
        console.log('🎮 Juego iniciado - Canvas:', this.baseWidth, 'x', this.baseHeight);
    }

    /**
     * Pausa el juego
     */
    pause() {
        this.currentState = CONFIG.STATES.PAUSED;
        if (this.enemySpawner) {
            this.enemySpawner.setSpawning(true);
        }
        
        try {
            Promise.resolve().then(() => {
                try {
                    this.audioSystem.pauseMusic();
                    this.audioSystem.playSound('pause');
                } catch (e) { /* Silenciar */ }
            });
        } catch (e) { /* Silenciar */ }
        
        const msg = CONFIG.MESSAGES.PAUSED;
        this.showMessage(msg.TITLE, msg.TEXT);
    }

    /**
     * Reanuda el juego
     */
    resume() {
        this.currentState = CONFIG.STATES.PLAYING;
        if (this.enemySpawner) {
            this.enemySpawner.setSpawning(false);
        }
        
        try {
            Promise.resolve().then(() => {
                try {
                    this.audioSystem.resumeMusic();
                    this.audioSystem.playSound('click');
                } catch (e) { /* Silenciar */ }
            });
        } catch (e) { /* Silenciar */ }
        
        this.hideMessage();
    }

    /**
     * Reinicia el juego
     */
    restart() {
        // Ocultar mensaje de game over
        this.hideMessage();
        
        try {
            Promise.resolve().then(() => {
                try {
                    this.audioSystem.playMusic();
                } catch (e) { /* Silenciar */ }
            });
        } catch (e) { /* Silenciar */ }
        
        // Iniciar nuevo juego
        this.start();
    }

    /**
     * Game over
     */
    gameOver() {
        this.currentState = CONFIG.STATES.GAME_OVER;
        
        if (this.enemySpawner) {
            this.enemySpawner.setSpawning(true);
        }
        
        try {
            Promise.resolve().then(() => {
                try {
                    this.audioSystem.stopMusic();
                    this.audioSystem.playSound('gameOver');
                } catch (e) { /* Silenciar */ }
            });
        } catch (e) { /* Silenciar */ }
        
        if (this.score > this.highScore) {
            this.highScore = this.score;
            this.saveHighScore(this.highScore);
            
            if (this.ui.highScoreDisplay) {
                this.ui.highScoreDisplay.textContent = this.highScore;
            }
        }
        
        const msg = CONFIG.MESSAGES.GAME_OVER;
        this.showMessage(msg.TITLE, msg.TEXT(this.score, this.highScore));
    }

    /**
     * Siguiente nivel
     */
    nextLevel() {
        this.level++;
        this.enemiesKilledThisLevel = 0;
        
        this.scoreThisLevel = 0;
        
        try {
            Promise.resolve().then(() => {
                try {
                    this.audioSystem.playSound('levelUp');
                } catch (e) { /* Silenciar */ }
            });
        } catch (e) { /* Silenciar */ }
        
        if (this.enemySpawner) {
            this.enemySpawner.resetForLevel(this.level);
        }
        
        this.scoreForNextLevel = CONFIG.LEVELS.SCORE_FOR_NEXT_LEVEL * this.level;
        
        console.log(`⭐ Nivel ${this.level} - Patrón: ${this.enemySpawner.getCurrentPatternName()}`);
        
        console.log(`   Requisitos: ${CONFIG.LEVELS.ENEMIES_FOR_NEXT_LEVEL} kills + ${this.scoreForNextLevel} puntos`);
    }

    /**
     * Actualiza la lógica
     */
    update() {
        if (this.currentState !== CONFIG.STATES.PLAYING) return;

        const currentTime = Date.now();
        const deltaTime = currentTime - this.lastFrameTime;
        this.lastFrameTime = currentTime;

        // Actualizar jugador
        this.player.update(this.baseWidth);

        // Disparos
        if (this.player.keys.shoot) {
            const bullet = this.player.shoot();
            if (bullet) {
                this.bullets.push(bullet);
            }
        }

        // Enemigos
        if (this.enemySpawner) {
            const enemyBullets = this.enemySpawner.update(deltaTime);
            this.bullets.push(...enemyBullets);
        }

        // Balas
        this.bullets = this.bullets.filter(bullet => {
            bullet.update();
            return bullet.active;
        });

        // Colisiones
        const pointsEarned = this.collisionSystem.checkBulletEnemyCollisions(
            this.bullets,
            this.enemySpawner ? this.enemySpawner.getActiveEnemies() : []
        );
        
        if (pointsEarned > 0) {
            this.score += pointsEarned;
            
            this.scoreThisLevel += pointsEarned;
            
            const enemiesDestroyed = Math.floor(pointsEarned / 10);
            this.enemiesKilled += enemiesDestroyed;
            this.enemiesKilledThisLevel += enemiesDestroyed;
            
            const hasEnoughKills = this.enemiesKilledThisLevel >= this.enemiesForNextLevel;
            const hasEnoughScore = this.scoreThisLevel >= this.scoreForNextLevel;
            
            if (hasEnoughKills && hasEnoughScore) {
                this.nextLevel();
            }
        }

        this.collisionSystem.checkEnemyBulletPlayerCollisions(this.bullets, this.player);
        this.collisionSystem.checkEnemyPlayerCollision(
            this.enemySpawner ? this.enemySpawner.getActiveEnemies() : [],
            this.player
        );

        this.checkGameConditions();
        this.updateUI();

        if (CONFIG.DEBUG.SHOW_FPS) {
            this.updateFPS();
        }
    }

    /**
     * Verifica condiciones de juego
     */
    checkGameConditions() {
        if (!this.player.isAlive()) {
            this.gameOver();
            return;
        }

        if (this.enemySpawner && this.enemySpawner.hasReachedBottom()) {
            this.gameOver();
        }
    }

    /**
     * Dibuja todo
     */
    draw() {
        this.ctx.fillStyle = '#000000';
        this.ctx.fillRect(0, 0, this.baseWidth, this.baseHeight);

        this.drawStars();

        if (this.player) {
            this.player.draw(this.ctx);
        }

        if (this.enemySpawner) {
            this.enemySpawner.draw(this.ctx);
        }

        this.bullets.forEach(bullet => bullet.draw(this.ctx));
        this.collisionSystem.updateAndDrawParticles(this.ctx);

        if (CONFIG.DEBUG.ENABLED) {
            this.drawDebugInfo();
        }

        if (CONFIG.DEBUG.SHOW_FPS) {
            this.drawFPS();
        }
    }

    /**
     * Crea estrellas
     */
    createStars() {
        const stars = [];
        for (let i = 0; i < CONFIG.VISUAL.STAR_COUNT; i++) {
            stars.push({
                x: Math.random() * this.baseWidth,
                y: Math.random() * this.baseHeight,
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
            if (star.y > this.baseHeight) {
                star.y = 0;
                star.x = Math.random() * this.baseWidth;
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
     */
    showMessage(title, text) {
        if (this.ui.messagePanel && this.ui.messageTitle && this.ui.messageText) {
            this.ui.messageTitle.textContent = title;
            this.ui.messageText.innerHTML = text;
            this.ui.messagePanel.classList.add('active');
            console.log('📢 Mensaje mostrado:', title);
        } else {
            console.warn('⚠️ No se pudo mostrar mensaje - elementos UI no encontrados');
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
     */
    saveHighScore(score) {
        try {
            localStorage.setItem(CONFIG.STORAGE.HIGH_SCORE_KEY, score.toString());
        } catch (e) {
            console.warn('Error guardando high score');
        }
    }

    /**
     * Carga high score
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
     * Dibuja debug
     */
    drawDebugInfo() {
        const lines = [
            `Canvas: ${this.baseWidth}x${this.baseHeight} (${this.scale.toFixed(2)}x)`,
            `Estado: ${this.currentState}`,
            `Balas: ${this.bullets.length}`,
            `Kills: ${this.enemiesKilledThisLevel}/${this.enemiesForNextLevel}`,
            `Score Level: ${this.scoreThisLevel}/${this.scoreForNextLevel}`,
            `Patrón: ${this.enemySpawner ? this.enemySpawner.currentPattern.name : 'N/A'}`
        ];

        this.ctx.fillStyle = '#ffff00';
        this.ctx.font = '12px monospace';
        lines.forEach((line, i) => {
            this.ctx.fillText(line, 10, 40 + i * 15);
        });
    }

    /**
     * Obtiene estado
     */
    getState() {
        return this.currentState;
    }
}