using UnityEngine;

/// <summary>
/// Genera sprites de círculos procedurales en tiempo de ejecución.
/// Se usa para crear los sprites del Sol, planetas y lunas sin necesitar assets externos.
/// Adjuntar a un GameObject vacío en la escena, o llamar desde otros scripts.
/// </summary>
public static class SpriteGenerator
{
    // Cache de sprites generados para no recrearlos
    private static Sprite _circleSprite;
    private static Sprite _glowSprite;

    /// <summary>
    /// Devuelve un sprite de círculo blanco sólido (con suavizado de bordes).
    /// Se genera una única vez y se reutiliza.
    /// </summary>
    public static Sprite GetCircleSprite(int resolution = 64)
    {
        if (_circleSprite != null) return _circleSprite;
        _circleSprite = CreateCircle(resolution, Color.white, smoothEdge: true);
        return _circleSprite;
    }

    /// <summary>
    /// Devuelve un sprite circular con halo difuso (para el Sol).
    /// </summary>
    public static Sprite GetGlowSprite(int resolution = 128)
    {
        if (_glowSprite != null) return _glowSprite;
        _glowSprite = CreateGlow(resolution);
        return _glowSprite;
    }

    // ─── Generadores ──────────────────────────────────────────────────────────

    /// <summary>Crea un círculo sólido de color dado.</summary>
    public static Sprite CreateCircle(int resolution, Color color, bool smoothEdge = true)
    {
        Texture2D tex    = new Texture2D(resolution, resolution, TextureFormat.RGBA32, false);
        tex.filterMode   = FilterMode.Bilinear;
        tex.wrapMode     = TextureWrapMode.Clamp;

        float  center = resolution / 2f;
        float  radius = resolution / 2f - 1f;
        Color[] pixels = new Color[resolution * resolution];

        for (int y = 0; y < resolution; y++)
        for (int x = 0; x < resolution; x++)
        {
            float dist  = Vector2.Distance(new Vector2(x, y), new Vector2(center, center));
            float alpha;

            if (smoothEdge)
                alpha = Mathf.Clamp01(1f - (dist - (radius - 1.5f)) / 1.5f);
            else
                alpha = (dist <= radius) ? 1f : 0f;

            pixels[y * resolution + x] = new Color(color.r, color.g, color.b, color.a * alpha);
        }

        tex.SetPixels(pixels);
        tex.Apply();

        return Sprite.Create(tex,
            new Rect(0, 0, resolution, resolution),
            new Vector2(0.5f, 0.5f),
            resolution);
    }

    /// <summary>Crea un círculo con gradiente radial (centro brillante, bordes difusos).</summary>
    public static Sprite CreateGlow(int resolution)
    {
        Texture2D tex  = new Texture2D(resolution, resolution, TextureFormat.RGBA32, false);
        tex.filterMode = FilterMode.Bilinear;
        tex.wrapMode   = TextureWrapMode.Clamp;

        float  center = resolution / 2f;
        float  radius = resolution / 2f;
        Color[] pixels = new Color[resolution * resolution];

        for (int y = 0; y < resolution; y++)
        for (int x = 0; x < resolution; x++)
        {
            float t     = Vector2.Distance(new Vector2(x, y), new Vector2(center, center)) / radius;
            float alpha = Mathf.Clamp01(1f - t * t);
            pixels[y * resolution + x] = new Color(1f, 1f, 1f, alpha);
        }

        tex.SetPixels(pixels);
        tex.Apply();

        return Sprite.Create(tex,
            new Rect(0, 0, resolution, resolution),
            new Vector2(0.5f, 0.5f),
            resolution);
    }

    /// <summary>
    /// Asigna un sprite circular generado al SpriteRenderer del GameObject dado.
    /// Útil para inicializar prefabs en Start().
    /// </summary>
    public static void AssignCircleSprite(GameObject go, int resolution = 64)
    {
        SpriteRenderer sr = go.GetComponent<SpriteRenderer>();
        if (sr == null) sr = go.AddComponent<SpriteRenderer>();
        sr.sprite = GetCircleSprite(resolution);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Componente que inicializa sprites en todos los planetas/lunas al inicio
// ─────────────────────────────────────────────────────────────────────────────

/// <summary>
/// Añadir a cualquier GameObject que necesite un sprite circular en runtime.
/// Se auto-asigna al despertar.
/// </summary>
[RequireComponent(typeof(SpriteRenderer))]
public class CircleSpriteAutoAssign : MonoBehaviour
{
    [Tooltip("Resolución de la textura (potencia de 2 recomendada)")]
    public int resolution = 64;

    [Tooltip("Si true, usa el sprite de halo difuso en vez del círculo sólido")]
    public bool useGlow = false;

    private void Awake()
    {
        SpriteRenderer sr = GetComponent<SpriteRenderer>();
        sr.sprite = useGlow
            ? SpriteGenerator.GetGlowSprite(resolution)
            : SpriteGenerator.GetCircleSprite(resolution);
    }
}
