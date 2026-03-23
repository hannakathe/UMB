using UnityEngine;

/// <summary>
/// Controla el movimiento orbital de una luna alrededor de su planeta padre.
/// También gestiona la rotación sobre su propio eje.
/// </summary>
public class Moon : MonoBehaviour
{
    // ─── Datos orbitales ──────────────────────────────────────────────────────
    [Header("Datos Orbitales")]
    [Tooltip("Nombre de la luna")]
    public string moonName = "Luna";

    [Tooltip("Distancia al planeta en unidades de escena")]
    public float orbitRadius = 1.5f;

    [Tooltip("Periodo orbital en días terrestres")]
    public float orbitalPeriodDays = 27.3f;

    [Tooltip("Periodo de rotación propia en horas terrestres")]
    public float rotationPeriodHours = 655f;

    [Tooltip("Ángulo inicial en la órbita (grados)")]
    public float initialAngle = 0f;

    // ─── Visualización ────────────────────────────────────────────────────────
    [Header("Visualización")]
    [Tooltip("Tamaño visual de la luna")]
    public float displaySize = 0.15f;

    [Tooltip("Color de la luna")]
    public Color moonColor = Color.gray;

    [Tooltip("Mostrar línea de órbita")]
    public bool showOrbit = true;

    // ─── Referencias internas ─────────────────────────────────────────────────
    private Transform _planetTransform;   // Planeta al que orbita
    private float _currentAngle;          // Ángulo actual en la órbita (grados)
    private float _orbitalSpeed;          // Velocidad angular en °/día simulado
    private float _rotationSpeed;         // Velocidad de rotación en °/hora simulada
    private LineRenderer _orbitLine;

    // Constantes de conversión
    private const float DEGREES_PER_REVOLUTION = 360f;
    private const float HOURS_PER_DAY = 24f;

    // ─── Inicialización ───────────────────────────────────────────────────────
    public void Initialize(Transform planetTransform)
    {
        _planetTransform = planetTransform;
        _currentAngle = initialAngle;

        // Velocidad angular = 360° / periodo (en días)
        _orbitalSpeed = DEGREES_PER_REVOLUTION / orbitalPeriodDays;

        // Velocidad de rotación propia: convertir de horas a días
        _rotationSpeed = (orbitalPeriodHours > 0)
            ? DEGREES_PER_REVOLUTION / (rotationPeriodHours / HOURS_PER_DAY)
            : 0f;

        SetupVisuals();
        SetupOrbitLine();
    }

    // ─── Configuración visual ─────────────────────────────────────────────────
    private void SetupVisuals()
    {
        // Asignar tamaño
        transform.localScale = Vector3.one * displaySize;

        // Asignar color al SpriteRenderer
        SpriteRenderer sr = GetComponent<SpriteRenderer>();
        if (sr != null)
        {
            sr.color = moonColor;
            sr.sortingOrder = 3; // Por encima de órbitas y planetas
        }
    }

    private void SetupOrbitLine()
    {
        if (!showOrbit) return;

        _orbitLine = gameObject.AddComponent<LineRenderer>();
        _orbitLine.useWorldSpace = true;
        _orbitLine.loop = true;
        _orbitLine.widthMultiplier = 0.02f;
        _orbitLine.positionCount = 64;

        // Material simple
        Material mat = new Material(Shader.Find("Sprites/Default"));
        mat.color = new Color(moonColor.r, moonColor.g, moonColor.b, 0.25f);
        _orbitLine.material = mat;
        _orbitLine.sortingOrder = 1;
    }

    // ─── Ciclo de actualización ───────────────────────────────────────────────
    private void Update()
    {
        if (TimeManager.Instance == null || _planetTransform == null) return;

        // Días simulados que avanzan este frame
        float simulatedDays = TimeManager.Instance.SimulationDaysPerSecond * Time.deltaTime;

        // ── Traslación ─────────────────────────────────────────────────────
        _currentAngle += _orbitalSpeed * simulatedDays;
        if (_currentAngle >= 360f) _currentAngle -= 360f;

        float angleRad = _currentAngle * Mathf.Deg2Rad;
        Vector3 offset = new Vector3(
            Mathf.Cos(angleRad) * orbitRadius,
            Mathf.Sin(angleRad) * orbitRadius,
            0f
        );
        transform.position = _planetTransform.position + offset;

        // ── Rotación propia ────────────────────────────────────────────────
        float rotDegrees = _rotationSpeed * simulatedDays;
        transform.Rotate(0f, 0f, rotDegrees);

        // ── Actualizar línea de órbita ─────────────────────────────────────
        UpdateOrbitLine();
    }

    private void UpdateOrbitLine()
    {
        if (_orbitLine == null || _planetTransform == null) return;

        int segments = _orbitLine.positionCount;
        for (int i = 0; i < segments; i++)
        {
            float angle = (i / (float)segments) * Mathf.PI * 2f;
            Vector3 pos = _planetTransform.position + new Vector3(
                Mathf.Cos(angle) * orbitRadius,
                Mathf.Sin(angle) * orbitRadius,
                0f
            );
            _orbitLine.SetPosition(i, pos);
        }
    }
}
