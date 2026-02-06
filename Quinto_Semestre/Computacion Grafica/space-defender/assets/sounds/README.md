# 🔊 SISTEMA DE AUDIO - README RÁPIDO

## ⚡ INICIO RÁPIDO (5 MINUTOS)

### Opción 1: Usar el juego SIN audio (por ahora)

El juego funciona perfectamente sin archivos de audio. Si quieres probarlo primero:

1. Abre `js/config.js`
2. Cambia `AUDIO.ENABLED: true` a `false`
3. ¡Juega sin sonido!

---

### Opción 2: Descargar audio AHORA (15 minutos)

**PASO 1: Ve a estos sitios**

🎵 **Para Efectos de Sonido:**
- https://freesound.org/
- Busca: "laser shoot 8bit", "explosion 8bit", "game over retro"

🎼 **Para Música:**
- https://incompetech.com/music/royalty-free/
- Sección: Electronic/Dance → 8-bit
- Descarga: "8bit Dungeon Boss" o similar

**PASO 2: Descarga estos archivos**

Necesitas 9 archivos en total:

✅ **8 Efectos de Sonido** (assets/sounds/sfx/):
1. player-shoot.mp3
2. enemy-shoot.mp3
3. explosion.mp3
4. player-hit.mp3
5. game-over.mp3
6. level-up.mp3
7. ui-click.mp3
8. game-start.mp3

✅ **1 Música de Fondo** (assets/sounds/music/):
9. gameplay.mp3

**PASO 3: Ponlos en las carpetas**

```
space-defender-v5.1/
└── assets/
    └── sounds/
        ├── sfx/
        │   ├── player-shoot.mp3
        │   ├── enemy-shoot.mp3
        │   ├── explosion.mp3
        │   ├── player-hit.mp3
        │   ├── game-over.mp3
        │   ├── level-up.mp3
        │   ├── ui-click.mp3
        │   └── game-start.mp3
        └── music/
            └── gameplay.mp3
```

**PASO 4: ¡Juega con sonido!** 🎵

---

## 🎯 BÚSQUEDAS RÁPIDAS PARA FREESOUND

Abre Freesound.org y busca EXACTAMENTE esto:

| Archivo | Búsqueda | Qué descargar |
|---------|----------|---------------|
| player-shoot.mp3 | "laser pew" | Sonido corto y agudo |
| enemy-shoot.mp3 | "alien laser" | Similar pero más grave |
| explosion.mp3 | "8bit explosion" | Explosión retro |
| player-hit.mp3 | "hurt 8bit" | Sonido de daño |
| game-over.mp3 | "game over arcade" | Jingle de derrota |
| level-up.mp3 | "level up" | Sonido de éxito |
| ui-click.mp3 | "button click" | Click suave |
| game-start.mp3 | "game start retro" | Inicio de juego |

**Tip:** Filtra por duración < 2 segundos para encontrar más rápido

---

## 🎼 MÚSICA RECOMENDADA

### Incompetech.com (Kevin MacLeod)

1. Ve a: https://incompetech.com/music/royalty-free/music.html
2. Filtra por: Genre > Electronic/Dance
3. Busca términos: "8-bit", "chiptune", "arcade"

**Tracks perfectos para Space Defender:**
- **"8bit Dungeon Boss"** ⭐ (mi favorito)
- "Cipher"
- "Rocket"
- "Digital Lemonade"

4. Descarga MP3
5. Renombra a `gameplay.mp3`
6. Pon en `assets/sounds/music/`

**IMPORTANTE:** Agrega atribución en tu README:
```
Music: "8bit Dungeon Boss" by Kevin MacLeod (incompetech.com)
Licensed under Creative Commons: By Attribution 4.0
```

---

## ⚙️ CONFIGURAR VOLÚMENES

Si los sonidos están muy fuertes/bajos, edita `js/config.js`:

```javascript
AUDIO: {
    ENABLED: true,
    VOLUME: {
        MASTER: 0.7,   // Volumen general (0-1)
        SFX: 0.8,      // Efectos de sonido (0-1)
        MUSIC: 0.5     // Música de fondo (0-1)
    }
}
```

---

## 🎮 CONTROLES DE AUDIO EN EL JUEGO

Una vez que tengas los archivos:

- **M** = Mute/Unmute (silenciar todo)
- La música empieza automáticamente al presionar ENTER
- Los efectos de sonido se reproducen automáticamente

---

## 📚 GUÍA COMPLETA

Para información detallada sobre dónde encontrar audio de calidad, ver:
**GUIA_AUDIO.md** (20+ páginas con todos los recursos)

---

## ✅ CHECKLIST

- [ ] Carpetas creadas (assets/sounds/sfx y assets/sounds/music)
- [ ] 8 efectos de sonido descargados
- [ ] 1 música de fondo descargada
- [ ] Archivos renombrados correctamente
- [ ] Archivos en las carpetas correctas
- [ ] AUDIO.ENABLED = true en config.js
- [ ] Probado el juego con sonido
- [ ] Volúmenes ajustados (si es necesario)

---

## 🚨 PROBLEMAS COMUNES

**"Los sonidos no se escuchan"**
1. Verifica que AUDIO.ENABLED = true
2. Verifica los nombres de archivo (deben ser EXACTOS)
3. Verifica las rutas de las carpetas
4. Abre consola (F12) y busca errores

**"La música no empieza"**
- Normal: La música empieza cuando presionas ENTER (no antes)
- Los navegadores bloquean autoplay hasta que el usuario interactúe

**"Error de carga de archivos"**
- Verifica que los archivos sean .mp3
- Verifica que no haya espacios en los nombres
- Usa guiones (-) no underscores (_)

---

## 🎉 ¡ESO ES TODO!

El audio está completamente integrado. Solo falta agregar los archivos MP3.

**Tiempo estimado:** 15-20 minutos para tener todo funcionando 🎵

---

**Si no quieres lidiar con audio ahora:** Pon `AUDIO.ENABLED: false` y sigue jugando.

**Si quieres audio profesional:** Lee GUIA_AUDIO.md para todas las opciones.