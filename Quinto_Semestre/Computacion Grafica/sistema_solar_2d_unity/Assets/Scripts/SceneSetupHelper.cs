using UnityEngine;
#if UNITY_EDITOR
using UnityEditor;
using UnityEngine.UI;
using TMPro;

/// <summary>
/// Herramienta de editor que construye la escena completa del Sistema Solar
/// automáticamente desde el menú Unity → Solar System → Setup Scene.
/// Solo está disponible en el Editor (no se compila en la build final).
/// </summary>
public class SceneSetupHelper : MonoBehaviour
{
    [MenuItem("Solar System/1. Setup Complete Scene")]
    public static void SetupScene()
    {
        Debug.Log("[SceneSetup] Iniciando configuración de escena...");

        // ── Limpiar la escena ───────────────────────────────────────────
        ClearScene();

        // ── Cámara principal ────────────────────────────────────────────
        Camera mainCam = SetupCamera();

        // ── Canvas UI ───────────────────────────────────────────────────
        Canvas canvas = SetupCanvas();

        // ── TimeManager ─────────────────────────────────────────────────
        GameObject tmObj = new GameObject("TimeManager");
        TimeManager tm = tmObj.AddComponent<TimeManager>();
        tm.timeScale    = 10000f;
        tm.minTimeScale = 100f;
        tm.maxTimeScale = 100000f;

        // ── SolarSystemManager ──────────────────────────────────────────
        GameObject ssmObj = new GameObject("SolarSystem");
        SolarSystemManager ssm = ssmObj.AddComponent<SolarSystemManager>();

        // Crear y asignar prefabs
        ssm.sunPrefab    = CreateBasicPrefab("SunPrefab",    1.5f, new Color(1f, 0.92f, 0.23f));
        ssm.planetPrefab = CreateBasicPrefab("PlanetPrefab", 0.5f, Color.white);
        ssm.moonPrefab   = CreateBasicPrefab("MoonPrefab",   0.15f, Color.gray);
        ssm.distanceScale = 4f;
        ssm.useLogScale   = true;
        ssm.logBase       = 1.8f;

        // ── StarfieldBackground ─────────────────────────────────────────
        GameObject sfObj = new GameObject("Starfield");
        StarfieldBackground sf = sfObj.AddComponent<StarfieldBackground>();
        sf.starCount       = 300;
        sf.fieldRadius     = 120f;
        sf.createParticles = true;
        sf.particleCount   = 150;
        sf.galaxyOpacity   = 0.35f;

        // ── CameraController ────────────────────────────────────────────
        CameraController cc = mainCam.gameObject.AddComponent<CameraController>();
        cc.minZoom    = 2f;
        cc.maxZoom    = 80f;
        cc.zoomSpeed  = 5f;
        cc.followZoom = 15f;

        // ── UI ──────────────────────────────────────────────────────────
        UIController ui = SetupUI(canvas, ssm, cc);

        // ── UIController en la escena ───────────────────────────────────
        GameObject uiCtrlObj = new GameObject("UIController");
        UIController uiCtrl = uiCtrlObj.AddComponent<UIController>();
        uiCtrl.solarSystemManager = ssm;
        uiCtrl.cameraController   = cc;

        // Conectar referencias UI (asignadas desde SetupUI)
        uiCtrl.timeScaleSlider   = ui.timeScaleSlider;
        uiCtrl.timeScaleLabel    = ui.timeScaleLabel;
        uiCtrl.pauseButton       = ui.pauseButton;
        uiCtrl.pauseButtonText   = ui.pauseButtonText;
        uiCtrl.resetCameraButton = ui.resetCameraButton;
        uiCtrl.planetDropdown    = ui.planetDropdown;
        uiCtrl.infoPanel         = ui.infoPanel;
        uiCtrl.planetNameText    = ui.planetNameText;
        uiCtrl.planetDetailsText = ui.planetDetailsText;

        // Fondo negro de cámara
        mainCam.backgroundColor = Color.black;

        Debug.Log("[SceneSetup] ¡Escena configurada correctamente! Presiona Play para ver la simulación.");
        EditorUtility.DisplayDialog(
            "Sistema Solar 2D",
            "¡Escena configurada!\n\nPresiona ▶ Play para iniciar la simulación.\n\n" +
            "Controles:\n• Rueda del ratón: Zoom\n• Botón central: Paneo\n• Dropdown: Seguir planeta\n• Espacio: Pausar",
            "¡Entendido!"
        );
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private static void ClearScene()
    {
        string[] toDelete = { "TimeManager", "SolarSystem", "Starfield",
                               "Main Camera", "UIController", "Canvas",
                               "EventSystem" };
        foreach (string name in toDelete)
        {
            GameObject go = GameObject.Find(name);
            if (go != null) DestroyImmediate(go);
        }
    }

    private static Camera SetupCamera()
    {
        GameObject camObj = new GameObject("Main Camera");
        camObj.tag = "MainCamera";

        Camera cam = camObj.AddComponent<Camera>();
        cam.orthographic     = true;
        cam.orthographicSize = 35f;
        cam.backgroundColor  = Color.black;
        cam.clearFlags       = CameraClearFlags.SolidColor;
        cam.transform.position = new Vector3(0f, 0f, -10f);
        cam.farClipPlane     = 1000f;

        // Audio listener
        camObj.AddComponent<AudioListener>();

        return cam;
    }

    private static Canvas SetupCanvas()
    {
        GameObject canvasObj = new GameObject("Canvas");
        Canvas canvas = canvasObj.AddComponent<Canvas>();
        canvas.renderMode = RenderMode.ScreenSpaceOverlay;
        canvas.sortingOrder = 10;

        CanvasScaler scaler = canvasObj.AddComponent<CanvasScaler>();
        scaler.uiScaleMode        = CanvasScaler.ScaleMode.ScaleWithScreenSize;
        scaler.referenceResolution = new Vector2(1920, 1080);
        scaler.matchWidthOrHeight  = 0.5f;

        canvasObj.AddComponent<UnityEngine.UI.GraphicRaycaster>();

        // EventSystem
        GameObject esObj = new GameObject("EventSystem");
        esObj.AddComponent<UnityEngine.EventSystems.EventSystem>();
        esObj.AddComponent<UnityEngine.EventSystems.StandaloneInputModule>();

        return canvas;
    }

    /// <summary>
    /// Crea toda la UI y devuelve un UIController temporal con las referencias.
    /// El llamador copia las referencias al UIController real de escena.
    /// </summary>
    private static UIController SetupUI(Canvas canvas, SolarSystemManager ssm, CameraController cc)
    {
        RectTransform canvasRect = canvas.GetComponent<RectTransform>();

        // ── Panel izquierdo (controles) ────────────────────────────────
        GameObject panel = CreatePanel(canvas.transform, "ControlPanel",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -10), new Vector2(280, 200),
            new Color(0f, 0f, 0.1f, 0.75f));

        // Título
        CreateText(panel.transform, "Title", "🌌 Sistema Solar 2D",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -10), new Vector2(260, 30), 14f, Color.white);

        // Slider de velocidad
        CreateText(panel.transform, "SpeedLabel_Static", "Velocidad de Tiempo:",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -45), new Vector2(260, 22), 11f, Color.white);

        Slider slider = CreateSlider(panel.transform, "TimeSlider",
            new Vector2(10, -70), new Vector2(260, 25));

        TextMeshProUGUI speedLabel = CreateText(panel.transform, "SpeedValue", "Velocidad: ...",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -100), new Vector2(260, 22), 10f,
            new Color(0.8f, 0.9f, 1f));

        // Botón pausa
        Button pauseBtn = CreateButton(panel.transform, "PauseBtn", "⏸ Pausar",
            new Vector2(10, -130), new Vector2(125, 30),
            new Color(0.15f, 0.35f, 0.65f));
        TextMeshProUGUI pauseBtnText = pauseBtn.GetComponentInChildren<TextMeshProUGUI>();

        // Botón reiniciar cámara
        Button resetBtn = CreateButton(panel.transform, "ResetCamBtn", "🌟 Ver todo",
            new Vector2(145, -130), new Vector2(125, 30),
            new Color(0.25f, 0.15f, 0.5f));

        // ── Dropdown de selección de planeta ──────────────────────────
        CreateText(panel.transform, "PlanetLabel_Static", "Seguir planeta:",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -168), new Vector2(260, 22), 11f, Color.white);

        // El dropdown se agrega al Canvas directamente para que sea visible
        TMP_Dropdown dropdown = CreateDropdown(panel.transform, "PlanetDropdown",
            new Vector2(10, -193), new Vector2(260, 30));

        // ── Panel de información (esquina inferior izquierda) ──────────
        GameObject infoPanel = CreatePanel(canvas.transform, "InfoPanel",
            new Vector2(0, 0), new Vector2(0, 0),
            new Vector2(10, 10), new Vector2(280, 150),
            new Color(0f, 0f, 0.1f, 0.8f));
        infoPanel.SetActive(false);

        TextMeshProUGUI infoName = CreateText(infoPanel.transform, "InfoName", "Planeta",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -10), new Vector2(260, 28), 14f, new Color(1f, 0.85f, 0.3f));

        TextMeshProUGUI infoDetails = CreateText(infoPanel.transform, "InfoDetails", "",
            new Vector2(0, 1), new Vector2(0, 1),
            new Vector2(10, -42), new Vector2(260, 100), 9f, Color.white);

        // Devolver un UIController temporal para pasar referencias
        UIController temp = new GameObject("_Temp_UIRefs").AddComponent<UIController>();
        temp.timeScaleSlider   = slider;
        temp.timeScaleLabel    = speedLabel;
        temp.pauseButton       = pauseBtn;
        temp.pauseButtonText   = pauseBtnText;
        temp.resetCameraButton = resetBtn;
        temp.planetDropdown    = dropdown;
        temp.infoPanel         = infoPanel;
        temp.planetNameText    = infoName;
        temp.planetDetailsText = infoDetails;

        return temp;
    }

    // ─── Utilidades de creación de UI ─────────────────────────────────────────
    private static GameObject CreatePanel(Transform parent, string name,
        Vector2 anchorMin, Vector2 anchorMax,
        Vector2 anchoredPos, Vector2 sizeDelta,
        Color color)
    {
        GameObject go = new GameObject(name);
        go.transform.SetParent(parent, false);

        RectTransform rt = go.AddComponent<RectTransform>();
        rt.anchorMin     = anchorMin;
        rt.anchorMax     = anchorMax;
        rt.pivot         = anchorMin;
        rt.anchoredPosition = anchoredPos;
        rt.sizeDelta     = sizeDelta;

        Image img = go.AddComponent<Image>();
        img.color = color;

        return go;
    }

    private static TextMeshProUGUI CreateText(Transform parent, string name, string text,
        Vector2 anchorMin, Vector2 anchorMax,
        Vector2 anchoredPos, Vector2 sizeDelta,
        float fontSize, Color color)
    {
        GameObject go = new GameObject(name);
        go.transform.SetParent(parent, false);

        RectTransform rt = go.AddComponent<RectTransform>();
        rt.anchorMin     = anchorMin;
        rt.anchorMax     = anchorMax;
        rt.pivot         = anchorMin;
        rt.anchoredPosition = anchoredPos;
        rt.sizeDelta     = sizeDelta;

        TextMeshProUGUI tmp = go.AddComponent<TextMeshProUGUI>();
        tmp.text     = text;
        tmp.fontSize = fontSize;
        tmp.color    = color;

        return tmp;
    }

    private static Slider CreateSlider(Transform parent, string name,
        Vector2 anchoredPos, Vector2 sizeDelta)
    {
        GameObject go = new GameObject(name);
        go.transform.SetParent(parent, false);

        RectTransform rt = go.AddComponent<RectTransform>();
        rt.anchorMin = new Vector2(0, 1);
        rt.anchorMax = new Vector2(0, 1);
        rt.pivot     = new Vector2(0, 1);
        rt.anchoredPosition = anchoredPos;
        rt.sizeDelta = sizeDelta;

        Slider slider = go.AddComponent<Slider>();
        slider.minValue = 100f;
        slider.maxValue = 100000f;
        slider.value    = 10000f;

        // Background
        GameObject bg = new GameObject("Background");
        bg.transform.SetParent(go.transform, false);
        RectTransform bgRt = bg.AddComponent<RectTransform>();
        bgRt.anchorMin = Vector2.zero;
        bgRt.anchorMax = Vector2.one;
        bgRt.sizeDelta = Vector2.zero;
        Image bgImg = bg.AddComponent<Image>();
        bgImg.color = new Color(0.1f, 0.1f, 0.2f);

        // Fill Area
        GameObject fillArea = new GameObject("Fill Area");
        fillArea.transform.SetParent(go.transform, false);
        RectTransform faRt = fillArea.AddComponent<RectTransform>();
        faRt.anchorMin = Vector2.zero;
        faRt.anchorMax = Vector2.one;
        faRt.sizeDelta = new Vector2(-20f, 0f);
        faRt.anchoredPosition = new Vector2(-5f, 0f);

        GameObject fill = new GameObject("Fill");
        fill.transform.SetParent(fillArea.transform, false);
        RectTransform fillRt = fill.AddComponent<RectTransform>();
        fillRt.anchorMin = Vector2.zero;
        fillRt.anchorMax = new Vector2(0.5f, 1f);
        fillRt.sizeDelta = new Vector2(10f, 0f);
        Image fillImg = fill.AddComponent<Image>();
        fillImg.color = new Color(0.3f, 0.6f, 1f);

        // Handle
        GameObject handleArea = new GameObject("Handle Slide Area");
        handleArea.transform.SetParent(go.transform, false);
        RectTransform haRt = handleArea.AddComponent<RectTransform>();
        haRt.anchorMin = Vector2.zero;
        haRt.anchorMax = Vector2.one;
        haRt.sizeDelta = new Vector2(-20f, 0f);

        GameObject handle = new GameObject("Handle");
        handle.transform.SetParent(handleArea.transform, false);
        RectTransform hRt = handle.AddComponent<RectTransform>();
        hRt.sizeDelta = new Vector2(20f, 0f);
        Image hImg = handle.AddComponent<Image>();
        hImg.color = Color.white;

        slider.fillRect   = fillRt;
        slider.handleRect = hRt;
        slider.targetGraphic = hImg;

        return slider;
    }

    private static Button CreateButton(Transform parent, string name, string label,
        Vector2 anchoredPos, Vector2 sizeDelta, Color color)
    {
        GameObject go = new GameObject(name);
        go.transform.SetParent(parent, false);

        RectTransform rt = go.AddComponent<RectTransform>();
        rt.anchorMin = new Vector2(0, 1);
        rt.anchorMax = new Vector2(0, 1);
        rt.pivot     = new Vector2(0, 1);
        rt.anchoredPosition = anchoredPos;
        rt.sizeDelta = sizeDelta;

        Image img = go.AddComponent<Image>();
        img.color = color;

        Button btn = go.AddComponent<Button>();
        ColorBlock cb = btn.colors;
        cb.highlightedColor = color * 1.3f;
        cb.pressedColor     = color * 0.7f;
        btn.colors = cb;

        // Texto del botón
        GameObject textObj = new GameObject("Text");
        textObj.transform.SetParent(go.transform, false);
        RectTransform textRt = textObj.AddComponent<RectTransform>();
        textRt.anchorMin = Vector2.zero;
        textRt.anchorMax = Vector2.one;
        textRt.sizeDelta = Vector2.zero;

        TextMeshProUGUI tmp = textObj.AddComponent<TextMeshProUGUI>();
        tmp.text      = label;
        tmp.fontSize  = 11f;
        tmp.color     = Color.white;
        tmp.alignment = TextAlignmentOptions.Center;

        return btn;
    }

    private static TMP_Dropdown CreateDropdown(Transform parent, string name,
        Vector2 anchoredPos, Vector2 sizeDelta)
    {
        GameObject go = new GameObject(name);
        go.transform.SetParent(parent, false);

        RectTransform rt = go.AddComponent<RectTransform>();
        rt.anchorMin = new Vector2(0, 1);
        rt.anchorMax = new Vector2(0, 1);
        rt.pivot     = new Vector2(0, 1);
        rt.anchoredPosition = anchoredPos;
        rt.sizeDelta = sizeDelta;

        Image img = go.AddComponent<Image>();
        img.color = new Color(0.1f, 0.2f, 0.4f);

        TMP_Dropdown dropdown = go.AddComponent<TMP_Dropdown>();

        // Caption text
        GameObject captionObj = new GameObject("Label");
        captionObj.transform.SetParent(go.transform, false);
        RectTransform cRt = captionObj.AddComponent<RectTransform>();
        cRt.anchorMin = new Vector2(0, 0);
        cRt.anchorMax = new Vector2(1, 1);
        cRt.sizeDelta = new Vector2(-25f, 0f);
        cRt.anchoredPosition = new Vector2(-12f, 0f);

        TextMeshProUGUI captionText = captionObj.AddComponent<TextMeshProUGUI>();
        captionText.text     = "— Ver todo —";
        captionText.fontSize = 11f;
        captionText.color    = Color.white;
        captionText.alignment = TextAlignmentOptions.MidlineLeft;

        dropdown.captionText = captionText;

        return dropdown;
    }

    [MenuItem("Solar System/2. Create Circle Sprite (Sol)")]
    public static void InfoMenu()
    {
        EditorUtility.DisplayDialog("Info",
            "Usa 'Solar System → 1. Setup Complete Scene' para configurar la escena completa.\n\n" +
            "Los sprites de círculo se generan automáticamente en tiempo de ejecución.\n\n" +
            "Puedes agregar una imagen de galaxia al componente StarfieldBackground → Galaxy Sprite.",
            "OK");
    }
}
#endif
