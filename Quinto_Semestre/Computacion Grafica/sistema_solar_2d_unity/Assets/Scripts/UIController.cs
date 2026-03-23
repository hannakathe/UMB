using UnityEngine;
using UnityEngine.UI;
using TMPro;

/// <summary>
/// Controlador de la interfaz de usuario.
/// Gestiona: slider de velocidad, botón de pausa, selección de planeta,
/// etiqueta de velocidad y botón de reinicio de cámara.
/// </summary>
public class UIController : MonoBehaviour
{
    // ─── Referencias de UI ────────────────────────────────────────────────────
    [Header("Controles de Tiempo")]
    [Tooltip("Slider para controlar la velocidad de simulación")]
    public Slider timeScaleSlider;

    [Tooltip("Texto que muestra la velocidad actual")]
    public TextMeshProUGUI timeScaleLabel;

    [Tooltip("Botón de pausa/reanudar")]
    public Button pauseButton;

    [Tooltip("Texto del botón de pausa")]
    public TextMeshProUGUI pauseButtonText;

    [Header("Cámara")]
    [Tooltip("Botón para reiniciar la cámara al Sol")]
    public Button resetCameraButton;

    [Tooltip("Dropdown para seleccionar planeta a seguir")]
    public TMP_Dropdown planetDropdown;

    [Header("Información")]
    [Tooltip("Panel de información del planeta seleccionado")]
    public GameObject infoPanel;

    [Tooltip("Texto del nombre del planeta en el panel de info")]
    public TextMeshProUGUI planetNameText;

    [Tooltip("Texto de detalles del planeta")]
    public TextMeshProUGUI planetDetailsText;

    // ─── Referencias de escena ────────────────────────────────────────────────
    [Header("Referencias de Escena")]
    public SolarSystemManager solarSystemManager;
    public CameraController   cameraController;

    // ─── Estado interno ───────────────────────────────────────────────────────
    private Planet[]  _planets;
    private string[]  _planetNames;

    // Datos de texto para el panel info
    private readonly string[] _planetDescriptions = new string[]
    {
        "Mercurio\nDistancia: 0.39 UA\nPeriodo: 87.97 días\nRotación: 1407.6 h\nLunas: 0\nEl planeta más pequeño y más cercano al Sol.",
        "Venus\nDistancia: 0.72 UA\nPeriodo: 224.70 días\nRotación: 5832.5 h (retrógrada)\nLunas: 0\nEl planeta más caliente del sistema solar.",
        "Tierra\nDistancia: 1.00 UA\nPeriodo: 365.25 días\nRotación: 23.93 h\nLunas: 1\nÚnico planeta conocido con vida.",
        "Marte\nDistancia: 1.52 UA\nPeriodo: 686.97 días\nRotación: 24.62 h\nLunas: 2\nEl planeta rojo, con el volcán más alto del sistema solar.",
        "Júpiter\nDistancia: 5.20 UA\nPeriodo: 4332.59 días\nRotación: 9.93 h\nLunas: ~95\nEl planeta más grande. La Gran Mancha Roja es una tormenta eterna.",
        "Saturno\nDistancia: 9.58 UA\nPeriodo: 10759.22 días\nRotación: 10.66 h\nLunas: ~145\nFamoso por sus anillos de hielo y roca.",
        "Urano\nDistancia: 19.22 UA\nPeriodo: 30688.50 días\nRotación: 17.24 h\nLunas: ~27\nRota de lado; su eje de inclinación es de 98°.",
        "Neptuno\nDistancia: 30.05 UA\nPeriodo: 60195.00 días\nRotación: 16.11 h\nLunas: ~14\nEl planeta más lejano y ventoso del sistema solar.",
    };

    // ─── Ciclo de vida ────────────────────────────────────────────────────────
    private void Start()
    {
        // Esperar un frame para que SolarSystemManager cree los planetas
        Invoke(nameof(InitializeUI), 0.1f);
    }

    private void Update()
    {
        // Actualizar etiqueta de velocidad en tiempo real
        UpdateTimeScaleLabel();

        // Teclado: espacio = pausa
        if (Input.GetKeyDown(KeyCode.Space))
            OnPauseClicked();
    }

    // ─── Inicialización de UI ─────────────────────────────────────────────────
    private void InitializeUI()
    {
        if (solarSystemManager == null)
        {
            solarSystemManager = FindObjectOfType<SolarSystemManager>();
            if (solarSystemManager == null)
            {
                Debug.LogError("[UIController] No se encontró SolarSystemManager.");
                return;
            }
        }

        _planets = solarSystemManager.planets;

        SetupTimeSlider();
        SetupPauseButton();
        SetupResetButton();
        SetupPlanetDropdown();

        if (infoPanel != null) infoPanel.SetActive(false);
    }

    // ─── Slider de velocidad ──────────────────────────────────────────────────
    private void SetupTimeSlider()
    {
        if (timeScaleSlider == null || TimeManager.Instance == null) return;

        TimeManager tm = TimeManager.Instance;
        timeScaleSlider.minValue = tm.minTimeScale;
        timeScaleSlider.maxValue = tm.maxTimeScale;
        timeScaleSlider.value    = tm.timeScale;

        timeScaleSlider.onValueChanged.AddListener(OnTimeScaleChanged);
        UpdateTimeScaleLabel();
    }

    private void OnTimeScaleChanged(float value)
    {
        if (TimeManager.Instance != null)
            TimeManager.Instance.SetTimeScale(value);
        UpdateTimeScaleLabel();
    }

    private void UpdateTimeScaleLabel()
    {
        if (timeScaleLabel == null || TimeManager.Instance == null) return;

        float scale = TimeManager.Instance.timeScale;
        string unit;
        float  display;

        if (scale >= 365f)
        {
            display = scale / 365f;
            unit    = "años/s";
        }
        else
        {
            display = scale;
            unit    = "días/s";
        }

        string pausedStr = TimeManager.Instance.IsPaused ? " [PAUSA]" : "";
        timeScaleLabel.text = $"Velocidad: {display:F1} {unit}{pausedStr}";
    }

    // ─── Botón de pausa ───────────────────────────────────────────────────────
    private void SetupPauseButton()
    {
        if (pauseButton == null) return;
        pauseButton.onClick.AddListener(OnPauseClicked);
        UpdatePauseButtonText();
    }

    private void OnPauseClicked()
    {
        if (TimeManager.Instance == null) return;
        TimeManager.Instance.TogglePause();
        UpdatePauseButtonText();
        UpdateTimeScaleLabel();
    }

    private void UpdatePauseButtonText()
    {
        if (pauseButtonText == null || TimeManager.Instance == null) return;
        pauseButtonText.text = TimeManager.Instance.IsPaused ? "▶ Reanudar" : "⏸ Pausar";
    }

    // ─── Botón de reinicio de cámara ─────────────────────────────────────────
    private void SetupResetButton()
    {
        if (resetCameraButton == null) return;
        resetCameraButton.onClick.AddListener(OnResetCameraClicked);
    }

    private void OnResetCameraClicked()
    {
        if (cameraController == null) return;

        if (solarSystemManager != null && solarSystemManager.sunTransform != null)
            cameraController.ResetToSun(solarSystemManager.sunTransform);

        // Limpiar selección del dropdown
        if (planetDropdown != null) planetDropdown.value = 0;

        if (infoPanel != null) infoPanel.SetActive(false);
    }

    // ─── Dropdown de planetas ─────────────────────────────────────────────────
    private void SetupPlanetDropdown()
    {
        if (planetDropdown == null || _planets == null) return;

        planetDropdown.ClearOptions();
        var options = new System.Collections.Generic.List<string>();
        options.Add("— Ver todo —");

        foreach (Planet p in _planets)
            if (p != null) options.Add(p.planetName);

        planetDropdown.AddOptions(options);
        planetDropdown.onValueChanged.AddListener(OnPlanetSelected);
    }

    private void OnPlanetSelected(int index)
    {
        if (index == 0)
        {
            // Opción "Ver todo": reiniciar cámara
            OnResetCameraClicked();
            return;
        }

        int planetIdx = index - 1;
        if (_planets == null || planetIdx >= _planets.Length) return;

        Planet selected = _planets[planetIdx];
        if (selected == null) return;

        // Seguir el planeta con la cámara
        if (cameraController != null)
            cameraController.FollowTarget(selected.transform);

        // Mostrar panel de información
        ShowPlanetInfo(planetIdx);
    }

    private void ShowPlanetInfo(int planetIdx)
    {
        if (infoPanel == null) return;
        infoPanel.SetActive(true);

        if (planetIdx < _planetDescriptions.Length)
        {
            string desc = _planetDescriptions[planetIdx];
            string[] lines = desc.Split('\n');

            if (planetNameText != null && lines.Length > 0)
                planetNameText.text = lines[0];

            if (planetDetailsText != null)
                planetDetailsText.text = string.Join("\n", lines, 1, lines.Length - 1);
        }
    }
}
