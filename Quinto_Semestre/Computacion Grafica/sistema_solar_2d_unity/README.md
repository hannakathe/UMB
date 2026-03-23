# Sistema Solar 2D — Unity

Simulacion del sistema solar en Unity 2D con datos astronomicos reales (escala acelerada).
Proyecto de Computacion Grafica — 5to Semestre UMB.

---

## Requisitos previos

| Herramienta | Version minima |
|-------------|---------------|
| Unity Hub   | cualquiera    |
| Unity Editor| 2022.3 LTS o superior |
| TextMeshPro | incluido en el proyecto |

---

## Estructura del Proyecto

```
Assets/
└── Scripts/
    ├── TimeManager.cs         — Singleton: escala y pausa de tiempo
    ├── Planet.cs              — Traslacion, rotacion, generacion de lunas
    ├── Moon.cs                — Orbita de luna alrededor del planeta
    ├── SolarSystemManager.cs  — Crea todos los planetas con datos reales
    ├── CameraController.cs    — Zoom, paneo, seguimiento de planetas
    ├── StarfieldBackground.cs — Estrellas, particulas y fondo de galaxia
    ├── UIController.cs        — Slider, pausa, dropdown de planetas, info
    ├── SpriteGenerator.cs     — Sprites circulares procedurales
    └── SceneSetupHelper.cs    — Menu editor para configurar la escena
```

---

## Configuracion paso a paso

### Paso 1 — Crear el proyecto en Unity Hub

1. Abre Unity Hub
2. Clic en New Project
3. Selecciona template 2D Core
4. Nombre: sistema_solar_2d_unity
5. Ubicacion: apunta a la carpeta padre donde esta este repositorio
6. Clic en Create Project

> Alternativa: si Unity ya detecta la carpeta con Assets/, abrela desde Unity Hub → Open.

---

### Paso 2 — Instalar TextMeshPro

Si Unity pide importar TMP:
1. Window → TextMeshPro → Import TMP Essential Resources
2. Clic en Import

---

### Paso 3 — Configurar la escena automaticamente (recomendado)

Una vez compilados los scripts sin errores:

1. En la barra de menu de Unity aparecera Solar System
2. Clic en: Solar System → 1. Setup Complete Scene
3. Unity configurara automaticamente:
   - Camara 2D ortografica
   - TimeManager (Singleton)
   - SolarSystemManager con prefabs de circulo
   - StarfieldBackground (estrellas + particulas)
   - CameraController
   - Canvas con UI completa
4. Aparecera un dialogo de confirmacion
5. Presiona Play

---

### Paso 4 — Configurar la escena manualmente (alternativa)

#### 4.1 Crear prefabs de circulo

1. Hierarchy → clic derecho → Create Empty
2. Agregar componente: SpriteRenderer
3. Agregar componente: CircleSpriteAutoAssign  (genera el circulo en runtime)
4. Arrastrar al panel Project → carpeta Assets/Prefabs/
5. Nombrar: SunPrefab
6. Repetir para PlanetPrefab y MoonPrefab

#### 4.2 GameObjects de escena

| Nombre | Componentes |
|--------|-------------|
| TimeManager | TimeManager |
| SolarSystem | SolarSystemManager |
| Starfield | StarfieldBackground |
| UIController | UIController |
| Main Camera | Camera (ortografica) + CameraController |

#### 4.3 SolarSystemManager — Inspector

- Sun Prefab → SunPrefab
- Planet Prefab → PlanetPrefab
- Moon Prefab → MoonPrefab
- Use Log Scale: activado
- Log Base: 1.8
- Distance Scale: 4

#### 4.4 Camara — Inspector

- Projection: Orthographic
- Size: 35
- Background: negro
- Clear Flags: Solid Color

#### 4.5 UIController — Inspector

Asignar cada referencia de UI (Slider, Buttons, Dropdown, TextMeshPro).

---

### Paso 5 — Agregar imagen de galaxia (opcional)

1. Arrastra tu imagen PNG a Assets/Resources/Sprites/
2. Inspector → Texture Type: Sprite (2D and UI) → Apply
3. Seleccionar el GameObject Starfield
4. StarfieldBackground → Galaxy Sprite → arrastra el sprite
5. Ajustar Galaxy Opacity (0.2 – 0.5 recomendado)

---

## Controles en runtime

| Accion | Control |
|--------|---------|
| Pausar / Reanudar | Boton UI o Espacio |
| Ajustar velocidad | Slider |
| Zoom in/out | Rueda del raton |
| Paneo | Clic central o derecho + arrastrar |
| Seguir un planeta | Dropdown de planetas |
| Ver todo | Boton Ver todo |

---

## Datos astronomicos incluidos

| Planeta  | Distancia (UA) | Traslacion   | Rotacion  | Lunas |
|----------|---------------|--------------|-----------|-------|
| Mercurio | 0.39          | 87.97 dias   | 1407.6 h  | 0     |
| Venus    | 0.72          | 224.70 dias  | 5832.5 h  | 0     |
| Tierra   | 1.00          | 365.25 dias  | 23.93 h   | 1     |
| Marte    | 1.52          | 686.97 dias  | 24.62 h   | 2     |
| Jupiter  | 5.20          | 11.86 anios  | 9.93 h    | 10    |
| Saturno  | 9.58          | 29.46 anios  | 10.66 h   | 10    |
| Urano    | 19.22         | 84.02 anios  | 17.24 h   | 5     |
| Neptuno  | 30.05         | 164.79 anios | 16.11 h   | 5     |

Lunas con nombres reales:
- Tierra: Luna
- Marte: Fobos, Deimos
- Jupiter: Io, Europa, Ganimedes, Calisto + 6 mas
- Saturno: Mimas, Encelado, Tetis, Dione, Rea, Titan + 4 mas
- Urano: Miranda, Ariel, Umbriel, Titania, Oberon
- Neptuno: Naiade, Talasa, Galatea, Larisa, Triton

---

## Ajuste de parametros

### Velocidad de simulacion (TimeManager)
```
timeScale = 10000   → 1 seg real = 10,000 dias simulados (predeterminado)
timeScale = 365     → 1 seg real = 1 anio simulado (lento)
timeScale = 100000  → 1 seg real = ~274 anios simulados (rapido)
```

### Escala de distancias (SolarSystemManager)
```
useLogScale = true   → Escala logaritmica (recomendado)
useLogScale = false  → Escala lineal (Neptuno queda muy lejos)
logBase = 1.8        → Mayor = mas comprimido
distanceScale = 4    → Factor final de escala
```

---

## Posibles errores

| Error | Solucion |
|-------|----------|
| TMPro not found | Window → TextMeshPro → Import TMP Essential Resources |
| NullReference en SolarSystemManager | Asignar los 3 prefabs en el Inspector |
| Planetas no visibles | Camara → Projection: Orthographic |
| UI no aparece | Canvas → Render Mode: Screen Space Overlay |
| Lunas no orbitan | MoonPrefab debe tener el componente Moon |

---

Proyecto universitario — Computacion Grafica 5to semestre UMB
