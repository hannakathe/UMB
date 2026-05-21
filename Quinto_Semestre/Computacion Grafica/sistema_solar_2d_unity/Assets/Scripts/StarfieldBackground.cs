using UnityEngine;

/// <summary>
/// Genera un campo de estrellas de fondo usando sprites y partículas.
/// Soporta imagen de galaxia personalizada como fondo base.
/// Tres capas de paralaje para profundidad.
/// </summary>
public class StarfieldBackground : MonoBehaviour
{
    // ─── Configuración de estrellas estáticas ─────────────────────────────────
    [Header("Estrellas Estáticas")]
    [Tooltip("Número total de estrellas a generar")]
    public int starCount = 300;

    [Tooltip("Radio del campo de estrellas en unidades de escena")]
    public float fieldRadius = 120f;

    [Tooltip("Tamaño mínimo de una estrella")]
    public float minStarSize = 0.05f;

    [Tooltip("Tamaño máximo de una estrella")]
    public float maxStarSize = 0.25f;

    // ─── Partículas dinámicas ─────────────────────────────────────────────────
    [Header("Partículas Dinámicas")]
    [Tooltip("Sistema de partículas para polvo estelar (opcional)")]
    public bool createParticles = true;

    [Tooltip("Número de partículas")]
    public int particleCount = 150;

    // ─── Imagen de galaxia ────────────────────────────────────────────────────
    [Header("Fondo de Galaxia")]
    [Tooltip("Sprite de la imagen de galaxia (arrastrar desde Project)")]
    public Sprite galaxySprite;

    [Tooltip("Escala del fondo de galaxia")]
    public float galaxyScale = 80f;

    [Tooltip("Opacidad del fondo de galaxia (0 = invisible, 1 = sólido)")]
    [Range(0f, 1f)]
    public float galaxyOpacity = 0.35f;

    [Tooltip("Velocidad de rotación lenta de la galaxia (grados/min)")]
    public float galaxyRotationSpeed = 0.5f;

    // ─── Capas de paralaje ────────────────────────────────────────────────────
    [Header("Paralaje")]
    [Tooltip("Intensidad del efecto de paralaje (0 = ninguno)")]
    [Range(0f, 0.05f)]
    public float parallaxFactor = 0.01f;

    // ─── Referencias ──────────────────────────────────────────────────────────
    private Camera        _cam;
    private Transform     _galaxyTransform;
    private GameObject[]  _starLayers;
    private Vector3       _lastCamPos;
    private ParticleSystem _particleSystem;

    // ─── Ciclo de vida ────────────────────────────────────────────────────────
    private void Start()
    {
        _cam = Camera.main;
        _lastCamPos = _cam ? _cam.transform.position : Vector3.zero;

        CreateGalaxyBackground();
        CreateStarLayers();
        if (createParticles) CreateParticleField();
    }

    private void Update()
    {
        // Paralaje suave
        if (_cam != null && parallaxFactor > 0f)
        {
            Vector3 delta = _cam.transform.position - _lastCamPos;
            _lastCamPos   = _cam.transform.position;

            if (_starLayers != null)
            {
                for (int i = 0; i < _starLayers.Length; i++)
                {
                    if (_starLayers[i] != null)
                        _starLayers[i].transform.position +=
                            new Vector3(delta.x, delta.y, 0f) * (parallaxFactor * (i + 1));
                }
            }
        }

        // Rotación lenta de la galaxia
        if (_galaxyTransform != null && galaxyRotationSpeed != 0f)
            _galaxyTransform.Rotate(0f, 0f, galaxyRotationSpeed * Time.deltaTime / 60f);
    }

    // ─── Fondo de galaxia ─────────────────────────────────────────────────────
    private void CreateGalaxyBackground()
    {
        GameObject go = new GameObject("GalaxyBackground");
        go.transform.SetParent(transform);
        go.transform.localPosition = new Vector3(0f, 0f, 1f); // Detrás de todo
        go.transform.localScale    = Vector3.one * galaxyScale;
        _galaxyTransform = go.transform;

        SpriteRenderer sr = go.AddComponent<SpriteRenderer>();

        if (galaxySprite != null)
        {
            sr.sprite = galaxySprite;
        }
        else
        {
            // Si no hay sprite, crear un gradiente radial procedural
            sr.sprite = CreateGradientCircleSprite(256, new Color(0.1f, 0.05f, 0.2f, 1f),
                                                         new Color(0.02f, 0.01f, 0.05f, 0f));
        }

        sr.color        = new Color(1f, 1f, 1f, galaxyOpacity);
        sr.sortingOrder = -10; // Más al fondo
    }

    // ─── Capas de estrellas estáticas ─────────────────────────────────────────
    private void CreateStarLayers()
    {
        int layers      = 3;
        int starsPerLayer = starCount / layers;
        _starLayers       = new GameObject[layers];

        // Configuración por capa (brillantez, tamaño, cantidad)
        float[] brightFactor = { 0.5f, 0.75f, 1.0f };
        float[] sizeFactor   = { 0.4f, 0.7f,  1.0f };

        for (int layer = 0; layer < layers; layer++)
        {
            GameObject layerObj = new GameObject($"StarLayer_{layer}");
            layerObj.transform.SetParent(transform);
            layerObj.transform.localPosition = Vector3.zero;
            _starLayers[layer] = layerObj;

            Sprite starSprite = CreateCircleSprite(16);

            for (int i = 0; i < starsPerLayer; i++)
            {
                // Distribución aleatoria en un disco
                Vector2 pos = Random.insideUnitCircle * fieldRadius;

                GameObject star = new GameObject($"Star_{layer}_{i}");
                star.transform.SetParent(layerObj.transform);
                star.transform.localPosition = new Vector3(pos.x, pos.y, 0f);

                float size = Random.Range(minStarSize, maxStarSize) * sizeFactor[layer];
                star.transform.localScale = Vector3.one * size;

                SpriteRenderer sr = star.AddComponent<SpriteRenderer>();
                sr.sprite = starSprite;

                // Color de la estrella: blanca / azulada / amarillenta
                float type = Random.value;
                Color starColor;
                if      (type < 0.6f) starColor = Color.white;
                else if (type < 0.8f) starColor = new Color(0.8f, 0.85f, 1.0f);
                else                  starColor = new Color(1.0f, 0.95f, 0.75f);

                starColor.a     = brightFactor[layer] * Random.Range(0.5f, 1.0f);
                sr.color        = starColor;
                sr.sortingOrder = -5 + layer;
            }
        }
    }

    // ─── Campo de partículas dinámico ─────────────────────────────────────────
    private void CreateParticleField()
    {
        GameObject psObj = new GameObject("StarlightParticles");
        psObj.transform.SetParent(transform);
        psObj.transform.localPosition = Vector3.zero;

        _particleSystem = psObj.AddComponent<ParticleSystem>();
        ParticleSystem.MainModule main = _particleSystem.main;

        main.loop            = true;
        main.maxParticles    = particleCount;
        main.startLifetime   = new ParticleSystem.MinMaxCurve(8f, 20f);
        main.startSpeed      = new ParticleSystem.MinMaxCurve(0.0f, 0.02f);
        main.startSize       = new ParticleSystem.MinMaxCurve(0.03f, 0.12f);
        main.startColor      = new ParticleSystem.MinMaxGradient(
            new Color(0.8f, 0.9f, 1.0f, 0.3f),
            new Color(1.0f, 1.0f, 0.8f, 0.7f)
        );
        main.simulationSpace = ParticleSystemSimulationSpace.World;

        // Emisión
        ParticleSystem.EmissionModule emission = _particleSystem.emission;
        emission.rateOverTime = particleCount / 15f;

        // Forma: disco
        ParticleSystem.ShapeModule shape = _particleSystem.shape;
        shape.shapeType = ParticleSystemShapeType.Circle;
        shape.radius    = fieldRadius * 0.8f;

        // Tamaño a lo largo del tiempo (parpadeo)
        ParticleSystem.SizeOverLifetimeModule sizeLife = _particleSystem.sizeOverLifetime;
        sizeLife.enabled = true;
        AnimationCurve curve = new AnimationCurve(
            new Keyframe(0f, 0f),
            new Keyframe(0.2f, 1f),
            new Keyframe(0.8f, 1f),
            new Keyframe(1f, 0f)
        );
        sizeLife.size = new ParticleSystem.MinMaxCurve(1f, curve);

        // Renderer
        ParticleSystemRenderer psr = psObj.GetComponent<ParticleSystemRenderer>();
        Material mat = new Material(Shader.Find("Sprites/Default"));
        mat.color        = Color.white;
        psr.material     = mat;
        psr.sortingOrder = -4;

        _particleSystem.Play();
    }

    // ─── Utilidades de sprites procedurales ──────────────────────────────────

    /// <summary>Genera un sprite de círculo sólido blanco de resolución dada.</summary>
    private Sprite CreateCircleSprite(int resolution)
    {
        Texture2D tex = new Texture2D(resolution, resolution, TextureFormat.RGBA32, false);
        tex.filterMode = FilterMode.Bilinear;
        tex.wrapMode   = TextureWrapMode.Clamp;

        Vector2 center  = new Vector2(resolution / 2f, resolution / 2f);
        float   radius  = resolution / 2f;

        for (int y = 0; y < resolution; y++)
        for (int x = 0; x < resolution; x++)
        {
            float dist  = Vector2.Distance(new Vector2(x, y), center);
            float alpha = Mathf.Clamp01(1f - dist / radius);
            tex.SetPixel(x, y, new Color(1f, 1f, 1f, alpha * alpha));
        }
        tex.Apply();

        return Sprite.Create(tex,
            new Rect(0, 0, resolution, resolution),
            new Vector2(0.5f, 0.5f),
            resolution);
    }

    /// <summary>Genera un sprite con gradiente radial de dos colores.</summary>
    private Sprite CreateGradientCircleSprite(int resolution, Color centerColor, Color edgeColor)
    {
        Texture2D tex = new Texture2D(resolution, resolution, TextureFormat.RGBA32, false);
        tex.filterMode = FilterMode.Bilinear;
        tex.wrapMode   = TextureWrapMode.Clamp;

        Vector2 center = new Vector2(resolution / 2f, resolution / 2f);
        float   radius = resolution / 2f;

        for (int y = 0; y < resolution; y++)
        for (int x = 0; x < resolution; x++)
        {
            float t     = Vector2.Distance(new Vector2(x, y), center) / radius;
            Color color = Color.Lerp(centerColor, edgeColor, t * t);
            if (t > 1f) color.a = 0f;
            tex.SetPixel(x, y, color);
        }
        tex.Apply();

        return Sprite.Create(tex,
            new Rect(0, 0, resolution, resolution),
            new Vector2(0.5f, 0.5f),
            resolution);
    }
}
