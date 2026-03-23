using UnityEngine;
using UnityEngine.EventSystems;

/// <summary>
/// Controlador de cámara 2D para la simulación del Sistema Solar.
/// Permite zoom con rueda del ratón, seguir planetas y arrastrar la vista.
/// </summary>
[RequireComponent(typeof(Camera))]
public class CameraController : MonoBehaviour
{
    // ─── Zoom ─────────────────────────────────────────────────────────────────
    [Header("Zoom")]
    [Tooltip("Tamaño ortográfico mínimo (máximo zoom in)")]
    public float minZoom = 2f;

    [Tooltip("Tamaño ortográfico máximo (máximo zoom out)")]
    public float maxZoom = 80f;

    [Tooltip("Velocidad del zoom con rueda del ratón")]
    public float zoomSpeed = 5f;

    [Tooltip("Suavizado del zoom (lerp)")]
    public float zoomSmoothing = 8f;

    // ─── Paneo (arrastrar) ────────────────────────────────────────────────────
    [Header("Paneo")]
    [Tooltip("Velocidad de desplazamiento al arrastrar")]
    public float panSpeed = 1f;

    // ─── Seguimiento de planeta ───────────────────────────────────────────────
    [Header("Seguimiento")]
    [Tooltip("Transform del objeto a seguir (null = libre)")]
    public Transform followTarget;

    [Tooltip("Velocidad de interpolación hacia el objetivo")]
    public float followSpeed = 5f;

    [Tooltip("Tamaño ortográfico cuando se selecciona seguir un planeta")]
    public float followZoom = 10f;

    // ─── Referencias ──────────────────────────────────────────────────────────
    private Camera _cam;
    private float  _targetZoom;
    private Vector3 _dragOrigin;
    private bool   _isDragging = false;

    // ─── Ciclo de vida ────────────────────────────────────────────────────────
    private void Awake()
    {
        _cam = GetComponent<Camera>();
        _targetZoom = _cam.orthographicSize;
    }

    private void Update()
    {
        HandleZoom();
        HandleDrag();
        HandleFollow();
    }

    // ─── Zoom ─────────────────────────────────────────────────────────────────
    private void HandleZoom()
    {
        // Ignorar scroll si el puntero está sobre la UI
        if (EventSystem.current != null && EventSystem.current.IsPointerOverGameObject())
            return;

        float scroll = Input.GetAxis("Mouse ScrollWheel");
        if (Mathf.Abs(scroll) > 0.001f)
        {
            _targetZoom -= scroll * zoomSpeed * _targetZoom * 0.3f;
            _targetZoom  = Mathf.Clamp(_targetZoom, minZoom, maxZoom);

            // Si el usuario hace zoom manualmente, dejar de seguir el planeta
            // (solo si el cambio es grande)
            if (Mathf.Abs(scroll) > 0.1f)
                followTarget = null;
        }

        // Suavizar el zoom
        _cam.orthographicSize = Mathf.Lerp(
            _cam.orthographicSize, _targetZoom,
            Time.unscaledDeltaTime * zoomSmoothing
        );
    }

    // ─── Paneo con arrastre ───────────────────────────────────────────────────
    private void HandleDrag()
    {
        // Solo arrastrar si no hay objetivo de seguimiento y no estamos sobre UI
        if (followTarget != null) return;

        if (Input.GetMouseButtonDown(2) || Input.GetMouseButtonDown(1)) // Botón central o derecho
        {
            _dragOrigin = _cam.ScreenToWorldPoint(Input.mousePosition);
            _isDragging = true;
        }

        if (Input.GetMouseButtonUp(2) || Input.GetMouseButtonUp(1))
            _isDragging = false;

        if (_isDragging && (Input.GetMouseButton(2) || Input.GetMouseButton(1)))
        {
            Vector3 currentPos = _cam.ScreenToWorldPoint(Input.mousePosition);
            Vector3 delta       = _dragOrigin - currentPos;
            transform.position += new Vector3(delta.x, delta.y, 0f);
        }
    }

    // ─── Seguimiento de planeta ───────────────────────────────────────────────
    private void HandleFollow()
    {
        if (followTarget == null) return;

        // Interpolar posición hacia el objetivo
        Vector3 targetPos = new Vector3(
            followTarget.position.x,
            followTarget.position.y,
            transform.position.z
        );
        transform.position = Vector3.Lerp(
            transform.position, targetPos,
            Time.unscaledDeltaTime * followSpeed
        );

        // Interpolar zoom
        _targetZoom = Mathf.Lerp(_targetZoom, followZoom, Time.unscaledDeltaTime * followSpeed);
    }

    // ─── API pública ──────────────────────────────────────────────────────────

    /// <summary>Seguir el Transform indicado y hacer zoom hacia él.</summary>
    public void FollowTarget(Transform target, float zoomLevel = -1f)
    {
        followTarget = target;
        if (zoomLevel > 0f) followZoom = zoomLevel;
        else                followZoom = Mathf.Max(minZoom, target.localScale.x * 10f);
    }

    /// <summary>Liberar el seguimiento y volver al Sol.</summary>
    public void ResetToSun(Transform sunTransform)
    {
        followTarget = null;
        Vector3 targetPos = new Vector3(
            sunTransform.position.x,
            sunTransform.position.y,
            transform.position.z
        );
        transform.position = targetPos;
        _targetZoom = maxZoom * 0.6f;
    }

    /// <summary>Volver al seguimiento del Sol.</summary>
    public void FollowSun(Transform sunTransform)
    {
        FollowTarget(sunTransform, maxZoom * 0.6f);
    }
}
