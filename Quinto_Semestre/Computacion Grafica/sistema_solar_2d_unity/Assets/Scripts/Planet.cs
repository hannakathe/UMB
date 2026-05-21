using UnityEngine;
using TMPro;

/// <summary>
/// Controla el movimiento orbital de un planeta alrededor del Sol,
/// su rotación propia, la generación de lunas y la visualización de órbitas.
/// </summary>
public class Planet : MonoBehaviour
{
    // ─── Datos astronómicos ───────────────────────────────────────────────────
    [Header("Datos Astronómicos")]
    public string planetName = "Planeta";

    [Tooltip("Distancia al Sol en unidades de escena (ya escalada)")]
    public float orbitRadius = 5f;

    [Tooltip("Periodo de traslación en días terrestres")]
    public float orbitalPeriodDays = 365f;

    [Tooltip("Periodo de rotación propia en horas terrestres")]
    public float rotationPeriodHours = 24f;

    [Tooltip("Número de lunas a generar")]
    public int moonCount = 0;

    // ─── Visualización ────────────────────────────────────────────────────────
    [Header("Visualización")]
    [Tooltip("Tamaño visual del planeta en unidades de escena")]
    public float displaySize = 0.5f;

    [Tooltip("Color del planeta")]
    public Color planetColor = Color.blue;

    [Tooltip("Ángulo inicial en órbita (grados). Aleatorio si < 0")]
    public float initialAngle = -1f;

    [Tooltip("Mostrar línea de órbita alrededor del Sol")]
    public bool showOrbit = true;

    [Tooltip("Mostrar etiqueta con nombre del planeta")]
    public bool showLabel = true;

    // ─── Prefabs ──────────────────────────────────────────────────────────────
    [Header("Prefabs")]
    [Tooltip("Prefab de luna a instanciar (debe tener componente Moon)")]
    public GameObject moonPrefab;

    // ─── Referencias ──────────────────────────────────────────────────────────
    private Transform _sunTransform;       // Posición del Sol
    private float _currentAngle;           // Ángulo orbital actual (grados)
    private float _orbitalSpeed;           // °/día simulado
    private float _rotationSpeed;          // °/día simulado (rotación propia)
    private LineRenderer _orbitLine;
    private TextMeshPro _label;
    private Moon[] _moons;

    // Constantes
    private const float DEGREES_FULL = 360f;
    private const float HOURS_PER_DAY = 24f;

    // ─── API pública ──────────────────────────────────────────────────────────

    /// <summary>Inicializa el planeta con la referencia al Sol.</summary>
    public void Initialize(Transform sunTransform, GameObject moonPrefabOverride = null)
    {
        _sunTransform = sunTransform;

        // Ángulo inicial: aleatorio si no se especificó
        _currentAngle = (initialAngle < 0f)
            ? Random.Range(0f, 360f)
            : initialAngle;

        // Velocidades angulares
        _orbitalSpeed  = DEGREES_FULL / orbitalPeriodDays;
        _rotationSpeed = (rotationPeriodHours > 0f)
            ? DEGREES_FULL / (rotationPeriodHours / HOURS_PER_DAY)
            : 0f;

        if (moonPrefabOverride != null) moonPrefab = moonPrefabOverride;

        SetupVisuals();
        SetupOrbitLine();
        SetupLabel();
        SpawnMoons();
        PlaceAtOrbit(); // Posición inicial antes del primer Update
    }

    // ─── Configuración visual ─────────────────────────────────────────────────
    private void SetupVisuals()
    {
        transform.localScale = Vector3.one * displaySize;
        SpriteRenderer sr = GetComponent<SpriteRenderer>();
        if (sr != null)
        {
            sr.color = planetColor;
            sr.sortingOrder = 2;
        }
    }

    private void SetupOrbitLine()
    {
        if (!showOrbit) return;

        _orbitLine = gameObject.AddComponent<LineRenderer>();
        _orbitLine.useWorldSpace = true;
        _orbitLine.loop = true;
        _orbitLine.widthMultiplier = 0.04f;
        _orbitLine.positionCount = 128;

        Material mat = new Material(Shader.Find("Sprites/Default"));
        mat.color = new Color(1f, 1f, 1f, 0.15f);
        _orbitLine.material = mat;
        _orbitLine.sortingOrder = 0;

        DrawOrbitLine();
    }

    private void DrawOrbitLine()
    {
        if (_orbitLine == null || _sunTransform == null) return;

        int segments = _orbitLine.positionCount;
        for (int i = 0; i < segments; i++)
        {
            float angle = (i / (float)segments) * Mathf.PI * 2f;
            Vector3 pos = _sunTransform.position + new Vector3(
                Mathf.Cos(angle) * orbitRadius,
                Mathf.Sin(angle) * orbitRadius,
                0f
            );
            _orbitLine.SetPosition(i, pos);
        }
    }

    private void SetupLabel()
    {
        if (!showLabel) return;

        GameObject labelObj = new GameObject("Label_" + planetName);
        labelObj.transform.SetParent(transform);
        labelObj.transform.localPosition = new Vector3(0f, displaySize * 0.9f, 0f);
        labelObj.transform.localScale    = Vector3.one * (0.3f / displaySize);

        _label = labelObj.AddComponent<TextMeshPro>();
        _label.text          = planetName;
        _label.fontSize      = 3f;
        _label.color         = Color.white;
        _label.alignment     = TextAlignmentOptions.Center;
        _label.sortingOrder  = 5;
    }

    // ─── Generación de lunas ──────────────────────────────────────────────────

    // Datos predefinidos para lunas representativas de cada planeta
    private static readonly (string name, float period, float dist, float rotH, Color color)[][] MoonData =
    {
        // [0] Mercury - sin lunas
        new (string, float, float, float, Color)[0],

        // [1] Venus - sin lunas
        new (string, float, float, float, Color)[0],

        // [2] Earth - 1 luna
        new (string, float, float, float, Color)[]
        {
            ("Luna",    27.3f,  0.8f,  655.7f, new Color(0.8f, 0.8f, 0.8f)),
        },

        // [3] Mars - 2 lunas
        new (string, float, float, float, Color)[]
        {
            ("Fobos",  0.32f, 0.5f,   7.6f,  new Color(0.7f, 0.6f, 0.5f)),
            ("Deimos", 1.26f, 0.8f,  30.3f,  new Color(0.75f, 0.65f, 0.55f)),
        },

        // [4] Jupiter - hasta 10 lunas principales
        new (string, float, float, float, Color)[]
        {
            ("Ío",       1.77f,  0.6f,  42.5f,  new Color(1f,   0.9f,  0.4f)),
            ("Europa",   3.55f,  0.75f, 85.2f,  new Color(0.8f, 0.85f, 1f  )),
            ("Ganimedes",7.15f,  0.95f, 171.7f, new Color(0.7f, 0.7f,  0.6f)),
            ("Calisto",  16.7f,  1.15f, 400.5f, new Color(0.5f, 0.45f, 0.4f)),
            ("Amaltea",  0.5f,   0.45f, 12f,    new Color(0.6f, 0.3f,  0.2f)),
            ("Himalia",  250f,   1.35f, 6000f,  new Color(0.6f, 0.55f, 0.5f)),
            ("Elara",    260f,   1.5f,  6240f,  new Color(0.55f,0.5f,  0.45f)),
            ("Pasifae",  735f,   1.65f, 17640f, new Color(0.5f, 0.5f,  0.55f)),
            ("Sinope",   758f,   1.8f,  18200f, new Color(0.5f, 0.45f, 0.5f)),
            ("Lisitea",  259f,   1.95f, 6216f,  new Color(0.6f, 0.6f,  0.65f)),
        },

        // [5] Saturn - hasta 10 lunas
        new (string, float, float, float, Color)[]
        {
            ("Mimas",     0.94f,  0.55f,  22.6f,  new Color(0.85f, 0.85f, 0.85f)),
            ("Encélado",  1.37f,  0.65f,  32.9f,  new Color(0.95f, 0.95f, 1f   )),
            ("Tetis",     1.89f,  0.75f,  45.3f,  new Color(0.8f,  0.8f,  0.85f)),
            ("Dione",     2.74f,  0.85f,  65.7f,  new Color(0.85f, 0.8f,  0.8f )),
            ("Rea",       4.52f,  1.0f,   108.4f, new Color(0.8f,  0.75f, 0.7f )),
            ("Titán",    15.95f,  1.3f,   382.7f, new Color(0.9f,  0.7f,  0.3f )),
            ("Hiperión",  21.3f,  1.5f,   510.7f, new Color(0.75f, 0.65f, 0.5f )),
            ("Jápeto",   79.3f,  1.75f,  1903f,  new Color(0.5f,  0.4f,  0.35f)),
            ("Febe",    550.5f,  2.0f,  13212f,  new Color(0.35f, 0.3f,  0.3f )),
            ("Helene",    2.74f,  0.9f,   65.7f,  new Color(0.8f,  0.8f,  0.85f)),
        },

        // [6] Uranus - hasta 5 lunas
        new (string, float, float, float, Color)[]
        {
            ("Miranda",  1.41f,  0.5f,   33.9f,  new Color(0.7f,  0.75f, 0.8f)),
            ("Ariel",    2.52f,  0.65f,  60.5f,  new Color(0.75f, 0.8f,  0.85f)),
            ("Umbriel",  4.14f,  0.8f,   99.5f,  new Color(0.4f,  0.4f,  0.45f)),
            ("Titania",  8.71f,  0.95f,  209f,   new Color(0.65f, 0.6f,  0.65f)),
            ("Oberón",  13.46f,  1.1f,   323f,   new Color(0.55f, 0.5f,  0.5f )),
        },

        // [7] Neptune - hasta 5 lunas
        new (string, float, float, float, Color)[]
        {
            ("Naíade",   0.29f,  0.45f,   7.0f,  new Color(0.5f,  0.6f,  0.75f)),
            ("Talasa",   0.31f,  0.55f,   7.4f,  new Color(0.5f,  0.6f,  0.75f)),
            ("Galatea",  0.43f,  0.65f,  10.3f,  new Color(0.5f,  0.6f,  0.7f )),
            ("Larisa",   0.55f,  0.75f,  13.3f,  new Color(0.45f, 0.55f, 0.7f )),
            ("Tritón",   5.88f,  1.0f,   141.0f, new Color(0.6f,  0.7f,  0.8f )),
        },
    };

    /// <summary>Índice de este planeta en la lista (0=Mercury … 7=Neptune).</summary>
    public int planetIndex = 0;

    private void SpawnMoons()
    {
        if (moonCount <= 0 || moonPrefab == null) return;

        _moons = new Moon[moonCount];

        // Obtener datos de lunas para este planeta
        var dataSet = (planetIndex >= 0 && planetIndex < MoonData.Length)
            ? MoonData[planetIndex]
            : new (string, float, float, float, Color)[0];

        for (int i = 0; i < moonCount; i++)
        {
            GameObject moonObj = Instantiate(moonPrefab, transform.position, Quaternion.identity);
            moonObj.name = (i < dataSet.Length) ? dataSet[i].name : $"Luna_{i+1}";
            moonObj.transform.SetParent(transform);

            Moon moon = moonObj.GetComponent<Moon>();
            if (moon == null) moon = moonObj.AddComponent<Moon>();

            // Aplicar datos si los hay, o generar automáticamente
            if (i < dataSet.Length)
            {
                moon.moonName           = dataSet[i].name;
                moon.orbitalPeriodDays  = dataSet[i].period;
                moon.orbitRadius        = dataSet[i].dist;
                moon.rotationPeriodHours= dataSet[i].rotH;
                moon.moonColor          = dataSet[i].color;
            }
            else
            {
                // Luna genérica automática
                moon.moonName           = $"Luna_{i+1}";
                moon.orbitRadius        = displaySize * 2f + i * 0.25f;
                moon.orbitalPeriodDays  = 5f + i * 3f;
                moon.rotationPeriodHours= (5f + i * 3f) * 24f;
                moon.moonColor          = Color.Lerp(Color.gray, Color.white, Random.value);
            }

            moon.displaySize   = displaySize * 0.25f;
            moon.initialAngle  = Random.Range(0f, 360f);
            moon.showOrbit     = true;
            moon.Initialize(transform);
            _moons[i] = moon;
        }
    }

    // ─── Posición inicial ─────────────────────────────────────────────────────
    private void PlaceAtOrbit()
    {
        if (_sunTransform == null) return;
        float rad = _currentAngle * Mathf.Deg2Rad;
        transform.position = _sunTransform.position + new Vector3(
            Mathf.Cos(rad) * orbitRadius,
            Mathf.Sin(rad) * orbitRadius,
            0f
        );
    }

    // ─── Ciclo de actualización ───────────────────────────────────────────────
    private void Update()
    {
        if (TimeManager.Instance == null || _sunTransform == null) return;

        float simulatedDays = TimeManager.Instance.SimulationDaysPerSecond * Time.deltaTime;

        // ── Traslación orbital ─────────────────────────────────────────────
        _currentAngle += _orbitalSpeed * simulatedDays;
        if (_currentAngle >= 360f) _currentAngle -= 360f;

        PlaceAtOrbit();

        // ── Rotación propia ────────────────────────────────────────────────
        float rotDeg = _rotationSpeed * simulatedDays;
        transform.Rotate(0f, 0f, rotDeg);
    }

    // ─── Gizmos (visibles solo en el Editor) ─────────────────────────────────
#if UNITY_EDITOR
    private void OnDrawGizmosSelected()
    {
        // Dibujar la órbita en el editor sin necesitar el LineRenderer
        UnityEditor.Handles.color = new Color(1f, 1f, 1f, 0.2f);
        Vector3 center = (_sunTransform != null) ? _sunTransform.position : Vector3.zero;
        UnityEditor.Handles.DrawWireDisc(center, Vector3.forward, orbitRadius);
    }
#endif
}
