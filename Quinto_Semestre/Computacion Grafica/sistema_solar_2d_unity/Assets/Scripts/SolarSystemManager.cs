using UnityEngine;

/// <summary>
/// Gestor principal del sistema solar.
/// Instancia el Sol, todos los planetas y configura la simulación.
/// Asigna datos astronómicos reales (con escala visual).
/// </summary>
public class SolarSystemManager : MonoBehaviour
{
    // ─── Prefabs ──────────────────────────────────────────────────────────────
    [Header("Prefabs (arrastrar desde el Project)")]
    [Tooltip("Prefab del Sol (sprite circular amarillo)")]
    public GameObject sunPrefab;

    [Tooltip("Prefab genérico de planeta (sprite circular)")]
    public GameObject planetPrefab;

    [Tooltip("Prefab genérico de luna (sprite circular pequeño)")]
    public GameObject moonPrefab;

    // ─── Escala ───────────────────────────────────────────────────────────────
    [Header("Escala de Distancias")]
    [Tooltip("Factor para convertir UA a unidades de escena.\n" +
             "Valor sugerido: 4 (logarítmico para caber en pantalla)")]
    public float distanceScale = 4f;

    [Tooltip("Usar escala logarítmica de distancias (más realista visualmente)")]
    public bool useLogScale = true;

    [Tooltip("Base de la escala logarítmica (solo si useLogScale = true)")]
    public float logBase = 1.8f;

    // ─── Referencias ──────────────────────────────────────────────────────────
    [HideInInspector] public Transform sunTransform;
    [HideInInspector] public Planet[]  planets;

    // ─────────────────────────────────────────────────────────────────────────
    // Estructura interna de datos de planetas
    // ─────────────────────────────────────────────────────────────────────────
    private struct PlanetData
    {
        public string name;
        public float  distanceAU;       // Distancia media al Sol en UA
        public float  orbitalPeriodDays;// Periodo de traslación en días terrestres
        public float  rotationHours;    // Periodo de rotación en horas terrestres
        public int    moonCount;        // Lunas a simular
        public float  sizeFactor;       // Factor de tamaño relativo (Tierra = 1)
        public Color  color;
        public int    planetIndex;      // Índice para tabla de lunas en Planet.cs
    }

    // Datos astronómicos reales (distancias en UA, periodos en días/horas)
    private readonly PlanetData[] _planetDataset = new PlanetData[]
    {
        new PlanetData
        {
            name = "Mercurio", distanceAU = 0.39f, orbitalPeriodDays = 87.97f,
            rotationHours = 1407.6f, moonCount = 0, sizeFactor = 0.38f,
            color = new Color(0.75f, 0.72f, 0.65f), planetIndex = 0
        },
        new PlanetData
        {
            name = "Venus", distanceAU = 0.72f, orbitalPeriodDays = 224.70f,
            rotationHours = 5832.5f, moonCount = 0, sizeFactor = 0.95f,
            color = new Color(0.95f, 0.85f, 0.55f), planetIndex = 1
        },
        new PlanetData
        {
            name = "Tierra", distanceAU = 1.00f, orbitalPeriodDays = 365.25f,
            rotationHours = 23.93f, moonCount = 1, sizeFactor = 1.00f,
            color = new Color(0.30f, 0.55f, 0.95f), planetIndex = 2
        },
        new PlanetData
        {
            name = "Marte", distanceAU = 1.52f, orbitalPeriodDays = 686.97f,
            rotationHours = 24.62f, moonCount = 2, sizeFactor = 0.53f,
            color = new Color(0.85f, 0.35f, 0.15f), planetIndex = 3
        },
        new PlanetData
        {
            name = "Júpiter", distanceAU = 5.20f, orbitalPeriodDays = 4332.59f,
            rotationHours = 9.93f, moonCount = 10, sizeFactor = 2.8f,
            color = new Color(0.85f, 0.70f, 0.50f), planetIndex = 4
        },
        new PlanetData
        {
            name = "Saturno", distanceAU = 9.58f, orbitalPeriodDays = 10759.22f,
            rotationHours = 10.66f, moonCount = 10, sizeFactor = 2.4f,
            color = new Color(0.95f, 0.85f, 0.60f), planetIndex = 5
        },
        new PlanetData
        {
            name = "Urano", distanceAU = 19.22f, orbitalPeriodDays = 30688.50f,
            rotationHours = 17.24f, moonCount = 5, sizeFactor = 1.8f,
            color = new Color(0.55f, 0.85f, 0.95f), planetIndex = 6
        },
        new PlanetData
        {
            name = "Neptuno", distanceAU = 30.05f, orbitalPeriodDays = 60195.00f,
            rotationHours = 16.11f, moonCount = 5, sizeFactor = 1.7f,
            color = new Color(0.25f, 0.40f, 0.95f), planetIndex = 7
        },
    };

    // ─── Ciclo de vida ────────────────────────────────────────────────────────
    private void Start()
    {
        // Validación de prefabs
        if (sunPrefab == null || planetPrefab == null || moonPrefab == null)
        {
            Debug.LogError("[SolarSystemManager] Faltan prefabs asignados en el Inspector.");
            return;
        }

        CreateSun();
        CreatePlanets();
    }

    // ─── Creación del Sol ─────────────────────────────────────────────────────
    private void CreateSun()
    {
        GameObject sunObj = Instantiate(sunPrefab, Vector3.zero, Quaternion.identity);
        sunObj.name = "Sol";
        sunObj.transform.SetParent(transform);

        // Tamaño visual del Sol (proporcionalmente grande)
        sunObj.transform.localScale = Vector3.one * 1.5f;

        SpriteRenderer sr = sunObj.GetComponent<SpriteRenderer>();
        if (sr != null)
        {
            sr.color = new Color(1f, 0.92f, 0.23f); // Amarillo solar
            sr.sortingOrder = 2;
        }

        // Halo de luz (SpriteRenderer secundario más grande y transparente)
        GameObject halo = new GameObject("SunHalo");
        halo.transform.SetParent(sunObj.transform);
        halo.transform.localPosition = Vector3.zero;
        halo.transform.localScale    = Vector3.one * 1.6f;

        SpriteRenderer haloSr = halo.AddComponent<SpriteRenderer>();
        if (sr != null) haloSr.sprite = sr.sprite;
        haloSr.color        = new Color(1f, 0.75f, 0.0f, 0.3f);
        haloSr.sortingOrder = 1;

        sunTransform = sunObj.transform;
    }

    // ─── Creación de planetas ─────────────────────────────────────────────────
    private void CreatePlanets()
    {
        planets = new Planet[_planetDataset.Length];

        for (int i = 0; i < _planetDataset.Length; i++)
        {
            PlanetData data = _planetDataset[i];

            GameObject planetObj = Instantiate(planetPrefab, Vector3.zero, Quaternion.identity);
            planetObj.name = data.name;
            planetObj.transform.SetParent(transform);

            Planet planet = planetObj.GetComponent<Planet>();
            if (planet == null) planet = planetObj.AddComponent<Planet>();

            // Asignar datos astronómicos
            planet.planetName        = data.name;
            planet.orbitRadius       = ScaleDistance(data.distanceAU);
            planet.orbitalPeriodDays = data.orbitalPeriodDays;
            planet.rotationPeriodHours = data.rotationHours;
            planet.moonCount         = data.moonCount;
            planet.displaySize       = 0.35f * data.sizeFactor;
            planet.planetColor       = data.color;
            planet.planetIndex       = data.planetIndex;
            planet.showOrbit         = true;
            planet.showLabel         = true;
            planet.initialAngle      = -1f; // Aleatorio

            planet.Initialize(sunTransform, moonPrefab);
            planets[i] = planet;

            Debug.Log($"[SolarSystemManager] Creado: {data.name} | " +
                      $"Radio: {planet.orbitRadius:F2} u | " +
                      $"Periodo: {data.orbitalPeriodDays} días | " +
                      $"Lunas: {data.moonCount}");
        }
    }

    /// <summary>
    /// Convierte UA a unidades de escena.
    /// Usa escala logarítmica para comprimir las grandes distancias exteriores.
    /// </summary>
    private float ScaleDistance(float distanceAU)
    {
        if (useLogScale)
        {
            // Escala logarítmica: log_base(distanceAU + 1) * distanceScale
            return Mathf.Log(distanceAU + 1f, logBase) * distanceScale;
        }
        return distanceAU * distanceScale;
    }

    // ─── API pública ──────────────────────────────────────────────────────────

    /// <summary>Devuelve el Transform del planeta por nombre.</summary>
    public Transform GetPlanetTransform(string name)
    {
        foreach (Planet p in planets)
            if (p != null && p.planetName == name)
                return p.transform;
        return null;
    }

    /// <summary>Devuelve todos los planetas como array de Transforms.</summary>
    public Transform[] GetAllPlanetTransforms()
    {
        if (planets == null) return new Transform[0];
        Transform[] result = new Transform[planets.Length];
        for (int i = 0; i < planets.Length; i++)
            result[i] = planets[i] != null ? planets[i].transform : null;
        return result;
    }
}
