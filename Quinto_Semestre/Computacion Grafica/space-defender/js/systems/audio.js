/* ===================================
   SYSTEMS/AUDIO.JS - Sistema de Audio
   ===================================
   Gestiona todos los efectos de sonido y música del juego.
*/

class AudioSystem {
    constructor() {
        // Estado del sistema
        this.enabled = CONFIG.AUDIO.ENABLED;
        this.muted = false;
        
        // Volúmenes
        this.masterVolume = CONFIG.AUDIO.VOLUME.MASTER;
        this.sfxVolume = CONFIG.AUDIO.VOLUME.SFX;
        this.musicVolume = CONFIG.AUDIO.VOLUME.MUSIC;
        
        // Objetos de audio
        this.sounds = {};
        this.music = null;
        this.currentMusic = null;
        
        // Pool de sonidos para efectos que se repiten mucho
        this.soundPools = {};
        
        // Inicializar
        this.init();
    }

    /**
     * Inicializa el sistema de audio
     */
    init() {
        if (!this.enabled) {
            console.log('🔇 Sistema de audio deshabilitado');
            return;
        }

        console.log('🔊 Inicializando sistema de audio...');
        
        // Cargar efectos de sonido
        this.loadSounds();
        
        // Cargar música
        this.loadMusic();
        
        console.log('✅ Sistema de audio listo');
    }

    /**
     * Carga todos los efectos de sonido
     */
    loadSounds() {
        const soundFiles = CONFIG.AUDIO.SOUNDS;
        
        for (let soundName in soundFiles) {
            const soundPath = soundFiles[soundName];
            
            // Crear pool de sonidos para efectos frecuentes
            if (['playerShoot', 'enemyShoot', 'explosion'].includes(soundName)) {
                this.soundPools[soundName] = this.createSoundPool(soundPath, 5);
            } else {
                this.sounds[soundName] = this.createSound(soundPath);
            }
        }
    }

    /**
     * Carga la música de fondo
     */
    loadMusic() {
        const musicFile = CONFIG.AUDIO.MUSIC.gameplay;
        if (musicFile) {
            this.music = this.createSound(musicFile, true);
        }
    }

    /**
     * Crea un objeto de audio
     * @param {string} path - Ruta del archivo de audio
     * @param {boolean} loop - Si debe hacer loop
     * @returns {Audio}
     */
    createSound(path, loop = false) {
        try {
            const audio = new Audio(path);
            audio.loop = loop;
            audio.preload = 'auto';
            
            // Manejar errores de carga
            audio.addEventListener('error', (e) => {
                console.warn(`⚠️ No se pudo cargar audio: ${path}`);
            });
            
            return audio;
        } catch (error) {
            console.warn(`⚠️ Error creando audio: ${path}`, error);
            return null;
        }
    }

    /**
     * Crea un pool de sonidos (para efectos frecuentes)
     * @param {string} path - Ruta del archivo
     * @param {number} poolSize - Cantidad de instancias
     * @returns {Array}
     */
    createSoundPool(path, poolSize = 3) {
        const pool = [];
        for (let i = 0; i < poolSize; i++) {
            pool.push(this.createSound(path));
        }
        return pool;
    }

    /**
     * Reproduce un efecto de sonido
     * @param {string} soundName - Nombre del sonido
     * @param {number} volumeMultiplier - Multiplicador de volumen (0-1)
     */
    playSound(soundName, volumeMultiplier = 1.0) {
        // Salida inmediata si está deshabilitado o silenciado
        if (!this.enabled || this.muted) return;

        // Ejecutar de forma completamente asíncrona
        requestAnimationFrame(() => {
            try {
                // Si es un pool, buscar una instancia disponible
                if (this.soundPools[soundName]) {
                    const pool = this.soundPools[soundName];
                    if (!pool || pool.length === 0) return;
                    
                    const availableSound = pool.find(sound => {
                        if (!sound) return false;
                        try {
                            return sound.paused || sound.ended || sound.currentTime === 0;
                        } catch {
                            return false;
                        }
                    });
                    
                    if (availableSound) {
                        try {
                            availableSound.volume = this.masterVolume * this.sfxVolume * volumeMultiplier;
                            availableSound.currentTime = 0;
                            availableSound.play().catch(() => {});
                        } catch {
                            // Silenciar
                        }
                    }
                } 
                // Sonido individual
                else if (this.sounds[soundName]) {
                    try {
                        const sound = this.sounds[soundName];
                        sound.volume = this.masterVolume * this.sfxVolume * volumeMultiplier;
                        sound.currentTime = 0;
                        sound.play().catch(() => {});
                    } catch {
                        // Silenciar
                    }
                }
            } catch {
            }
        });
    }

    /**
     * Reproduce música de fondo
     * @param {string} musicName - Nombre de la música
     */
    playMusic(musicName = 'gameplay') {
        if (!this.enabled || this.muted || !this.music) return;

        try {
            if (this.currentMusic !== musicName) {
                this.stopMusic();
                
                this.music.volume = this.masterVolume * this.musicVolume;
                this.music.play().catch(e => {
                    console.log('ℹ️ Música pendiente de interacción del usuario');
                });
                this.currentMusic = musicName;
                
                console.log('🎵 Reproduciendo música:', musicName);
            }
        } catch (error) {
            console.warn('Error reproduciendo música:', error);
        }
    }

    /**
     * Detiene la música
     */
    stopMusic() {
        if (this.music) {
            this.music.pause();
            this.music.currentTime = 0;
            this.currentMusic = null;
        }
    }

    /**
     * Pausa la música
     */
    pauseMusic() {
        if (this.music) {
            this.music.pause();
        }
    }

    /**
     * Reanuda la música
     */
    resumeMusic() {
        if (this.music && this.currentMusic && !this.muted) {
            this.music.play().catch(e => {
                // Silenciar error
            });
        }
    }

    /**
     * Activa/desactiva el mute
     */
    toggleMute() {
        this.muted = !this.muted;
        
        if (this.muted) {
            this.pauseMusic();
        } else {
            this.resumeMusic();
        }
        
        console.log(this.muted ? '🔇 Audio silenciado' : '🔊 Audio activado');
        return this.muted;
    }

    /**
     * Cambia el volumen maestro
     * @param {number} volume - Volumen (0-1)
     */
    setMasterVolume(volume) {
        this.masterVolume = Math.max(0, Math.min(1, volume));
        if (this.music) {
            this.music.volume = this.masterVolume * this.musicVolume;
        }
    }

    /**
     * Cambia el volumen de efectos
     * @param {number} volume - Volumen (0-1)
     */
    setSFXVolume(volume) {
        this.sfxVolume = Math.max(0, Math.min(1, volume));
    }

    /**
     * Cambia el volumen de música
     * @param {number} volume - Volumen (0-1)
     */
    setMusicVolume(volume) {
        this.musicVolume = Math.max(0, Math.min(1, volume));
        if (this.music) {
            this.music.volume = this.masterVolume * this.musicVolume;
        }
    }

    /**
     * Habilita/deshabilita el sistema de audio
     * @param {boolean} enabled
     */
    setEnabled(enabled) {
        this.enabled = enabled;
        if (!enabled) {
            this.stopMusic();
        }
    }

    /**
     * Limpia todos los recursos de audio
     */
    cleanup() {
        this.stopMusic();
        
        // Pausar todos los sonidos
        for (let soundName in this.sounds) {
            if (this.sounds[soundName]) {
                this.sounds[soundName].pause();
            }
        }
        
        // Pausar todos los pools
        for (let poolName in this.soundPools) {
            this.soundPools[poolName].forEach(sound => {
                if (sound) sound.pause();
            });
        }
    }
}