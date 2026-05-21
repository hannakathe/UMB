using UnityEngine;

/// <summary>
/// Singleton que gestiona el tiempo global de la simulación.
/// Permite pausar, reanudar y ajustar la velocidad de simulación.
/// </summary>
public class TimeManager : MonoBehaviour
{
    // ─── Singleton ────────────────────────────────────────────────────────────
    public static TimeManager Instance { get; private set; }

    // ─── Configuración pública ────────────────────────────────────────────────
    [Header("Escala de Tiempo")]
    [Tooltip("1 segundo real = timeScale días del sistema solar")]
    public float timeScale = 10000f;

    [Tooltip("Velocidad mínima permitida (días/segundo)")]
    public float minTimeScale = 100f;

    [Tooltip("Velocidad máxima permitida (días/segundo)")]
    public float maxTimeScale = 100000f;

    // ─── Estado interno ───────────────────────────────────────────────────────
    private bool _paused = false;

    // ─── Propiedades ──────────────────────────────────────────────────────────

    /// <summary>Tiempo de simulación que avanza cada segundo real.</summary>
    public float SimulationDaysPerSecond => _paused ? 0f : timeScale;

    /// <summary>Si la simulación está en pausa.</summary>
    public bool IsPaused => _paused;

    // ─── Ciclo de vida ────────────────────────────────────────────────────────
    private void Awake()
    {
        // Patrón Singleton: solo existe una instancia
        if (Instance != null && Instance != this)
        {
            Destroy(gameObject);
            return;
        }
        Instance = this;
        DontDestroyOnLoad(gameObject);
    }

    // ─── API pública ──────────────────────────────────────────────────────────

    /// <summary>Pausa o reanuda la simulación.</summary>
    public void TogglePause()
    {
        _paused = !_paused;
    }

    /// <summary>Establece la pausa directamente.</summary>
    public void SetPaused(bool paused) => _paused = paused;

    /// <summary>Cambia la escala de tiempo, respetando los límites.</summary>
    public void SetTimeScale(float newScale)
    {
        timeScale = Mathf.Clamp(newScale, minTimeScale, maxTimeScale);
    }
}
