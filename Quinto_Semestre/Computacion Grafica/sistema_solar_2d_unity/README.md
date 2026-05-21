# Sistema Solar 2D — Unity

Simulacion interactiva del sistema solar en Unity 2D con datos astronomicos reales (escala visual acelerada).
Proyecto de Computacion Grafica — 5to Semestre UMB.

---

## Requisitos previos

| Herramienta        | Version                        |
|--------------------|-------------------------------|
| Unity Hub          | cualquiera                    |
| Unity Editor       | **6000.4.0f1 (Unity 6)** o superior |
| Universal RP (URP) | incluido en el proyecto        |
| TextMeshPro        | incluido en el proyecto        |

---

## Caracteristicas

- Sol con shader procedural de corona y granulacion solar
- 8 planetas con iluminacion esferica Phong (efecto 3D)
- Lunas reales por planeta (nombres, periodos y colores correctos)
- Anillos de Saturno con bandas de Cassini procedurales
- Cinturon de asteroides con 500 asteroides
- 12 constelaciones del zodiaco con estrellas y etiquetas
- Fondo estelar procedural con nebulosas, cumulos y 3500+ estrellas
- Panel de informacion por planeta (datos orbitales, lunas, descripcion)
- Control de velocidad (1 — 10 000 dias/seg) y pausa
- Camara con zoom, paneo libre y reinicio al Sol

---

## Estructura del Proyecto

```
Assets/
├── Resources/
│   └── Sprites/
│       └── stars_bg.png           — Imagen de fondo opcional (no usada actualmente)
└── Scripts/
    ├── TimeManager.cs             — Singleton: escala y pausa del tiempo
    ├── Planet.cs                  — Traslacion orbital, lunas, interaccion mouse
    ├── Moon.cs                    — Orbita de luna alrededor del planeta
    ├── SolarSystemManager.cs      — Instancia Sol, planetas, asteroides, zodiaco
    ├── CameraController.cs        — Zoom suavizado, paneo, seguimiento
    ├── StarfieldBackground.cs     — Nebulosa procedural, cumulos, estrellas, particulas
    ├── UIController.cs            — Slider velocidad, pausa, panel de informacion
    ├── SpriteGenerator.cs         — Sprites circulares procedurales (planetas, sol, anillos)
    └── SceneSetupHelper.cs        — Menu editor: configura toda la escena en un clic
```

---

## Configuracion paso a paso

### Paso 1 — Abrir el proyecto en Unity

**Opcion A (recomendada):** clonar el repositorio y abrirlo desde Unity Hub → Open → seleccionar la carpeta `sistema_solar_2d_unity`.

**Opcion B:** crear proyecto nuevo en Unity Hub:
1. New Project → template **Universal 2D**
2. Nombre: `sistema_solar_2d_unity`
3. Copiar la carpeta `Assets/` del repositorio al nuevo proyecto

---

### Paso 2 — Instalar TextMeshPro

Si Unity muestra el aviso de TMP al abrir la escena:
1. `Window → TextMeshPro → Import TMP Essential Resources`
2. Clic en **Import**

---

### Paso 3 — Configurar la escena (automatico, recomendado)

Una vez que los scripts compilaron sin errores:

1. En la barra de menu de Unity aparece **Solar System**
2. Clic en: **Solar System → 1. Setup Complete Scene**
3. Unity construye automaticamente:
   - Camara 2D ortografica (size 27)
   - TimeManager singleton
   - SolarSystemManager con los 8 planetas y sus lunas
   - Fondo estelar procedural (nebulosas + cumulos + 3500 estrellas)
   - CameraController
   - Canvas UI completo (panel de control + panel de informacion)
4. Aparece dialogo de confirmacion → clic **Entendido**
5. Presiona **▶ Play**

---

### Paso 4 — Configurar la escena manualmente (alternativa)

Si prefieres armar la escena a mano:

#### 4.1 Crear prefabs base

1. Hierarchy → clic derecho → **Create Empty**
2. Agregar componentes: `SpriteRenderer` + `CircleSpriteAutoAssign`
3. Arrastrar al Project → `Assets/Prefabs/`
4. Nombrar `SunPrefab`. Repetir para `PlanetPrefab` y `MoonPrefab`

#### 4.2 GameObjects de escena

| Nombre          | Componentes necesarios                      |
|-----------------|---------------------------------------------|
| TimeManager     | TimeManager                                 |
| SolarSystem     | SolarSystemManager                          |
| Starfield       | StarfieldBackground                         |
| UIController    | UIController                                |
| Main Camera     | Camera (Orthographic, Size 27) + CameraController |

#### 4.3 SolarSystemManager — Inspector

| Campo          | Valor  |
|----------------|--------|
| Sun Prefab     | SunPrefab |
| Planet Prefab  | PlanetPrefab |
| Moon Prefab    | MoonPrefab |
| Distance Scale | 1.0    |
| Use Log Scale  | false  |

#### 4.4 UIController — Inspector

Asignar cada referencia de UI:
- `TimeScaleSlider`, `TimeScaleLabel`
- `PauseButton`, `PauseButtonText`
- `ResetCameraButton`, `ClosePanelButton`
- `InfoPanel`, `PlanetNameText`, `PlanetDetailsText`
- `SolarSystemManager`, `CameraController`

---

## Controles en runtime

| Accion                        | Control                          |
|-------------------------------|----------------------------------|
| Pausar / Reanudar             | Boton **Pausar** o tecla `Espacio` |
| Ajustar velocidad             | Slider (1 — 10 000 dias/seg)     |
| Zoom in / out                 | Rueda del raton                  |
| Paneo libre                   | Clic central o derecho + arrastrar |
| Ver info de un planeta        | Clic sobre el planeta            |
| Cerrar panel de informacion   | Boton **X** (esquina del panel)  |
| Reiniciar camara al Sol       | Boton **Reiniciar Camara**       |
| Ver nombre del planeta        | Pasar el mouse por encima        |

---

## Datos astronomicos incluidos

### Planetas

| Planeta  | Radio orbita (visual) | Periodo traslacion | Lunas simuladas |
|----------|-----------------------|--------------------|-----------------|
| Mercurio | 3.5 u                 | 87.97 dias         | 0               |
| Venus    | 5.5 u                 | 224.70 dias        | 0               |
| Tierra   | 7.5 u                 | 365.25 dias        | 1               |
| Marte    | 9.5 u                 | 686.97 dias        | 2               |
| Jupiter  | 14.0 u                | 11.86 anios        | 4               |
| Saturno  | 17.5 u                | 29.46 anios        | 2               |
| Urano    | 21.0 u                | 84.02 anios        | 2               |
| Neptuno  | 24.5 u                | 164.79 anios       | 1               |

### Lunas con nombre real

| Planeta  | Lunas                                  |
|----------|----------------------------------------|
| Tierra   | Luna                                   |
| Marte    | Fobos, Deimos                          |
| Jupiter  | Io, Europa, Ganimedes, Calisto         |
| Saturno  | Rea, Titan                             |
| Urano    | Titania, Oberon                        |
| Neptuno  | Triton                                 |

---

## Fondo estelar procedural

El fondo se genera en tiempo de ejecucion con `StarfieldBackground`:

| Elemento             | Descripcion                                                  |
|----------------------|--------------------------------------------------------------|
| Nebulosa principal   | Banda diagonal violeta-azul (Perlin noise multicapa)         |
| Nube azulada         | Region de formacion estelar (fria)                           |
| Nube rojiza          | Region de hidrogeno ionizado (calida)                        |
| Estrellas estaticas  | 3 500 estrellas en 3 capas con paralaje                      |
| Cumulos estelares    | 8 cumulos de ~100 estrellas (distribucion gaussiana)         |
| Polvo estelar        | 600 particulas parpadeantes dinamicas                        |

---

## Velocidad de simulacion

```
timeScale = 1       → 1 dia simulado cada segundo real (muy lento)
timeScale = 100     → 100 dias/seg  (valor por defecto)
timeScale = 365     → ~1 anio simulado por segundo
timeScale = 10 000  → ~27 anios simulados por segundo (maximo)
```

El slider de la UI cubre el rango completo 1 — 10 000.

---

## Posibles errores y soluciones

| Error                                    | Solucion                                                    |
|------------------------------------------|-------------------------------------------------------------|
| `TMPro not found`                        | Window → TextMeshPro → Import TMP Essential Resources       |
| `NullReference` en SolarSystemManager   | Asignar los 3 prefabs en el Inspector                       |
| Planetas no visibles                     | Camara → Projection: Orthographic, Size: 27                 |
| UI no aparece                            | Canvas → Render Mode: Screen Space Overlay                  |
| Lunas no orbitan                         | MoonPrefab debe tener el componente `Moon`                  |
| Fondo negro sin estrellas                | Verificar que el GameObject Starfield tiene `StarfieldBackground` |
| Panel de info no aparece al hacer clic  | Verificar que los planetas tienen `CircleCollider2D`        |

---

Proyecto universitario — Computacion Grafica 5to semestre UMB
