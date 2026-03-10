# main.py
import os

from direct.showbase.ShowBase import ShowBase
from direct.gui.OnscreenText  import OnscreenText
from direct.gui.OnscreenImage import OnscreenImage
from direct.gui.DirectGui     import DirectFrame, DirectButton
from direct.task              import Task
from panda3d.core             import (
    TextNode, WindowProperties, CardMaker,
    Point3, Vec3, TransparencyAttrib, Filename,
    AmbientLight, DirectionalLight,
    getModelPath,
)

from player       import Player, CHARACTER_DATA
from bullet       import Bullet
from game_manager import GameManager

# ── rutas absolutas a assets ───────────────────────────────────────────────────
_BASE_DIR    = os.path.dirname(os.path.abspath(__file__))
_IMG_DIR     = os.path.join(_BASE_DIR, "assets", "img")
_TEX_DIR     = os.path.join(_BASE_DIR, "assets", "textures")
_MODEL_DIR   = os.path.join(_BASE_DIR, "assets", "3d_models")

# Añadir carpeta de modelos al path de Panda3D para resolver texturas .fbm/
getModelPath().prependDirectory(Filename.fromOsSpecific(_MODEL_DIR))


def _img(name: str) -> str:
    return Filename.fromOsSpecific(os.path.join(_IMG_DIR, name)).getFullpath()

def _tex(rel: str) -> str:
    return Filename.fromOsSpecific(os.path.join(_TEX_DIR, rel)).getFullpath()

def _model(name: str) -> str:
    return Filename.fromOsSpecific(os.path.join(_MODEL_DIR, name)).getFullpath()


# ── mapa personaje → archivo FBX ──────────────────────────────────────────────
CHARACTER_MODELS = {
    "rex":   "alex-soldado-3d.fbx",
    "viper": "hanna-francotirador-3d.fbx",
    "blaze": "negro-operador-3d.fbx",
}


class OperacionNexus(ShowBase):
    def __init__(self):
        ShowBase.__init__(self)

        self.setBackgroundColor(0.04, 0.04, 0.08, 1)

        props = WindowProperties()
        props.setTitle("Operacion Nexus")
        self.win.requestProperties(props)

        # estado interno
        self.player      = None
        self.menu_nodes  = []      # nodos 2D del menú
        self.model_nodes = []      # nodos 3D de los personajes en el menú
        self.popup_nodes = []      # nodos del popup de habilidades
        self._selected_key = None  # personaje actualmente en popup

        self._setup_menu_lights()
        self._show_menu()

    # ══════════════════════════════════════════════════════════════════════════
    # ILUMINACIÓN PARA EL MENÚ 3D
    # ══════════════════════════════════════════════════════════════════════════
    def _setup_menu_lights(self):
        # Luz ambiental suave
        amb = AmbientLight("menu_amb")
        amb.setColor((0.35, 0.35, 0.35, 1))
        self._menu_amb = self.render.attachNewNode(amb)
        self.render.setLight(self._menu_amb)

        # Luz direccional frontal
        sun = DirectionalLight("menu_sun")
        sun.setColor((0.9, 0.9, 0.85, 1))
        self._menu_sun = self.render.attachNewNode(sun)
        self._menu_sun.setHpr(30, -60, 0)
        self.render.setLight(self._menu_sun)

    def _remove_menu_lights(self):
        self.render.clearLight(self._menu_amb)
        self.render.clearLight(self._menu_sun)
        self._menu_amb.removeNode()
        self._menu_sun.removeNode()

    # ══════════════════════════════════════════════════════════════════════════
    # MENÚ DE SELECCIÓN
    # ══════════════════════════════════════════════════════════════════════════
    def _show_menu(self):
        # ── cámara del menú (vista frontal para los modelos) ─────────────────
        self.disableMouse()
        self.camera.setPos(0, -10, 2)
        self.camera.lookAt(0, 0, 1)

        # ── fondo con bg_general.png ─────────────────────────────────────────
        bg_tex = self.loader.loadTexture(_tex("background/bg_general.png"))
        bg = OnscreenImage(image=bg_tex, pos=(0, 0, 0), scale=(1.78, 1, 1))
        bg.setBin("background", -100)
        self.menu_nodes.append(bg)

        # ── overlay oscuro semitransparente sobre el fondo ───────────────────
        overlay = DirectFrame(
            frameColor=(0, 0, 0, 0.52),
            frameSize=(-1.78, 1.78, -1.1, 1.1),
        )
        overlay.setBin("background", -99)
        self.menu_nodes.append(overlay)

        # ── título ────────────────────────────────────────────────────────────
        self.menu_nodes.append(OnscreenText(
            text="OPERACION NEXUS", pos=(0, 0.88), scale=0.115,
            fg=(0.0, 1.0, 0.4, 1), align=TextNode.ACenter,
            shadow=(0, 0, 0, 1), shadowOffset=(0.003, 0.003),
        ))
        self.menu_nodes.append(OnscreenText(
            text="Selecciona tu operativo", pos=(0, 0.74), scale=0.048,
            fg=(0.55, 0.85, 0.55, 1), align=TextNode.ACenter,
        ))

        # ── zonas de clic para cada personaje ─────────────────────────────────
        # (se colocan detrás de los modelos 3D para capturar el click)
        xs = [-0.62, 0.0, 0.62]
        keys = list(CHARACTER_DATA.keys())
        for i, key in enumerate(keys):
            x = xs[i]
            d = CHARACTER_DATA[key]
            r, g, b, _ = d["color"]

            # Área clicable invisible que cubre la zona del modelo
            hit_area = DirectButton(
                frameColor=(r * 0.12, g * 0.12, b * 0.12, 0.55),
                frameSize=(-0.28, 0.28, -0.65, 0.55),
                pos=(x, 0, 0.10),
                relief=1,
                command=self._open_popup,
                extraArgs=[key],
                text="",
            )
            self.menu_nodes.append(hit_area)

            # Nombre del operativo sobre el modelo
            self.menu_nodes.append(OnscreenText(
                text=d["name"], pos=(x, -0.48), scale=0.060,
                fg=d["color"], align=TextNode.ACenter,
                shadow=(0, 0, 0, 1), shadowOffset=(0.002, 0.002),
            ))
            self.menu_nodes.append(OnscreenText(
                text=d["role"], pos=(x, -0.55), scale=0.036,
                fg=(0.85, 0.85, 0.85, 1), align=TextNode.ACenter,
            ))
            self.menu_nodes.append(OnscreenText(
                text=f"VEL {d['speed']}   HP {d['hp']}",
                pos=(x, -0.62), scale=0.030,
                fg=(0.60, 0.82, 0.60, 1), align=TextNode.ACenter,
            ))
            self.menu_nodes.append(OnscreenText(
                text="[ Click para ver ]", pos=(x, -0.70), scale=0.028,
                fg=(0.40, 0.65, 0.40, 0.85), align=TextNode.ACenter,
            ))

        # ── cargar modelos FBX 3D ─────────────────────────────────────────────
        model_positions = [
            (-0.62, 0, 1),   # rex
            ( 0.00, 0, 1),   # viper
            ( 0.62, 0, 1),   # blaze
        ]
        for i, key in enumerate(keys):
            fbx_path = _model(CHARACTER_MODELS[key])
            try:
                mdl = self.loader.loadModel(fbx_path)
                mdl.reparentTo(self.render)

                # Centrar y escalar automáticamente
                bounds = mdl.getBounds()
                center = bounds.getCenter()
                radius = bounds.getRadius()
                scale  = 1.1 / max(radius, 0.001)

                mdl.setPos(
                    model_positions[i][0] - center.x * scale,
                    model_positions[i][1],
                    model_positions[i][2] - center.z * scale,
                )
                mdl.setScale(scale)

                # Rotación lenta continua
                mdl.setH(0)
                self.taskMgr.add(
                    lambda task, m=mdl: self._spin_model(task, m),
                    f"spin_{key}",
                )
                self.model_nodes.append(mdl)
            except Exception as e:
                print(f"[WARN] No se pudo cargar {CHARACTER_MODELS[key]}: {e}")
                # Fallback: cubo coloreado
                fallback = self.loader.loadModel("models/misc/rgbCube")
                fallback.reparentTo(self.render)
                fallback.setScale(0.9)
                fallback.setPos(model_positions[i])
                fallback.setColorScale(*CHARACTER_DATA[key]["color"])
                self.model_nodes.append(fallback)

    def _spin_model(self, task, model):
        """Rota el modelo lentamente en el eje Z (yaw)."""
        if model.isEmpty():
            return Task.done
        model.setH(model.getH() + 30 * globalClock.getDt())
        return Task.cont

    def _clear_menu(self):
        # Detener rotaciones
        keys = list(CHARACTER_DATA.keys())
        for key in keys:
            self.taskMgr.remove(f"spin_{key}")

        for n in self.menu_nodes:
            n.destroy()
        self.menu_nodes.clear()

        for m in self.model_nodes:
            if not m.isEmpty():
                m.removeNode()
        self.model_nodes.clear()

    # ══════════════════════════════════════════════════════════════════════════
    # POPUP DE HABILIDADES
    # ══════════════════════════════════════════════════════════════════════════
    def _open_popup(self, key):
        if self.popup_nodes:
            return   # ya hay un popup abierto

        self._selected_key = key
        d = CHARACTER_DATA[key]
        r, g, b, _ = d["color"]

        # ── fondo semitransparente que captura clics fuera del panel ─────────
        backdrop = DirectButton(
            frameColor=(0, 0, 0, 0.70),
            frameSize=(-1.78, 1.78, -1.1, 1.1),
            relief=0,
            command=self._close_popup,
            text="",
        )
        self.popup_nodes.append(backdrop)

        # ── panel central ─────────────────────────────────────────────────────
        panel = DirectFrame(
            frameColor=(0.05, 0.08, 0.05, 0.97),
            frameSize=(-0.68, 0.68, -0.60, 0.62),
        )
        self.popup_nodes.append(panel)

        # ── imagen del operador (columna izquierda) ───────────────────────────
        img_tex = self.loader.loadTexture(_img(d["image"]))
        img = OnscreenImage(image=img_tex, pos=(-0.36, 0, 0.10), scale=(0.24, 1, 0.33))
        img.setTransparency(TransparencyAttrib.MAlpha)
        self.popup_nodes.append(img)

        # ── columna derecha ────────────────────────────────────────────────────
        X = 0.04

        self.popup_nodes.append(OnscreenText(
            text=d["name"], pos=(X, 0.51), scale=0.082,
            fg=d["color"], align=TextNode.ALeft,
            shadow=(0, 0, 0, 1), shadowOffset=(0.003, 0.003),
        ))
        self.popup_nodes.append(OnscreenText(
            text=d["role"], pos=(X, 0.41), scale=0.042,
            fg=(0.88, 0.88, 0.88, 1), align=TextNode.ALeft,
        ))
        self.popup_nodes.append(OnscreenText(
            text=f"Velocidad: {d['speed']}     HP: {d['hp']}",
            pos=(X, 0.32), scale=0.034,
            fg=(0.65, 0.88, 0.65, 1), align=TextNode.ALeft,
        ))
        self.popup_nodes.append(OnscreenText(
            text=d["bio"], pos=(X, 0.21), scale=0.032,
            fg=(0.75, 0.80, 0.75, 1), align=TextNode.ALeft,
        ))

        # separador
        self.popup_nodes.append(OnscreenText(
            text="─" * 28,
            pos=(X, 0.11), scale=0.028,
            fg=(r * 0.6, g * 0.6, b * 0.6, 1), align=TextNode.ALeft,
        ))

        # capacidades
        self.popup_nodes.append(OnscreenText(
            text="CAPACIDADES",
            pos=(X, 0.04), scale=0.038,
            fg=d["color"], align=TextNode.ALeft,
        ))

        z_slots = [-0.08, -0.24, -0.41]
        for ab, z in zip(d["abilities"], z_slots):
            self.popup_nodes.append(DirectFrame(
                frameColor=(r * 0.08, g * 0.08, b * 0.08, 0.85),
                frameSize=(-0.01, 0.60, -0.09, 0.07),
                pos=(X, 0, z),
            ))
            self.popup_nodes.append(OnscreenText(
                text=ab["tag"], pos=(X + 0.02, z + 0.04), scale=0.024,
                fg=(r * 0.85, g * 0.85, b * 0.85, 1), align=TextNode.ALeft,
            ))
            self.popup_nodes.append(OnscreenText(
                text=ab["name"], pos=(X + 0.02, z - 0.02), scale=0.036,
                fg=d["color"], align=TextNode.ALeft,
            ))
            self.popup_nodes.append(OnscreenText(
                text=ab["desc"], pos=(X + 0.04, z - 0.09), scale=0.028,
                fg=(0.75, 0.82, 0.75, 1), align=TextNode.ALeft,
            ))

        # ── botones ───────────────────────────────────────────────────────────
        self.popup_nodes.append(DirectButton(
            text="JUGAR",
            scale=0.060, pos=(0.40, 0, -0.52),
            command=self._start_game, extraArgs=[key],
            frameColor=(r * 0.55, g * 0.55, b * 0.55, 1.0),
            text_fg=(1, 1, 1, 1), relief=1,
        ))
        self.popup_nodes.append(DirectButton(
            text="< Volver",
            scale=0.048, pos=(-0.40, 0, -0.52),
            command=self._close_popup,
            frameColor=(0.10, 0.16, 0.10, 1.0),
            text_fg=(0.75, 1.0, 0.75, 1), relief=1,
        ))

    def _close_popup(self):
        for n in self.popup_nodes:
            n.destroy()
        self.popup_nodes.clear()
        self._selected_key = None

    # ══════════════════════════════════════════════════════════════════════════
    # INICIO DEL JUEGO
    # ══════════════════════════════════════════════════════════════════════════
    def _start_game(self, character_key):
        self._close_popup()
        self._clear_menu()
        self._remove_menu_lights()

        self._build_floor()
        self._setup_game_camera()
        self.player  = Player(self, character_key)
        self.bullets = []
        self._build_hud()
        self.taskMgr.add(self._follow_camera, "follow_camera")
        self.accept("mouse1", self._shoot)
        self.gm = GameManager(self, self.player)

    # ── suelo ──────────────────────────────────────────────────────────────────
    def _build_floor(self):
        cm = CardMaker("floor")
        cm.setFrame(-20, 20, -20, 20)
        floor = self.render.attachNewNode(cm.generate())
        floor.setP(-90)
        floor.setColor(0.08, 0.13, 0.08, 1)

    # ── cámaras ────────────────────────────────────────────────────────────────
    def _setup_game_camera(self):
        self.camera.setPos(0, -18, 14)
        self.camera.lookAt(0, 0, 0)

    def _follow_camera(self, task):
        if self.player:
            p = self.player.get_pos()
            self.camera.setPos(p.x, p.y - 18, p.z + 14)
            self.camera.lookAt(p)
        return Task.cont

    # ── disparo ────────────────────────────────────────────────────────────────
    def _shoot(self):
        if not self.player or not self.mouseWatcherNode.hasMouse():
            return

        mpos   = self.mouseWatcherNode.getMouse()
        near_p = Point3()
        far_p  = Point3()
        self.camLens.extrude(mpos, near_p, far_p)

        near_w = self.render.getRelativePoint(self.camera, near_p)
        far_w  = self.render.getRelativePoint(self.camera, far_p)
        ray    = far_w - near_w

        if abs(ray.z) < 1e-6:
            return
        t      = (0.5 - near_w.z) / ray.z
        target = near_w + ray * t

        direction = Vec3(target - self.player.get_pos())
        if direction.length() < 1e-6:
            return
        direction.normalize()

        b = Bullet(self, self.player.get_pos(), direction)
        self.bullets.append(b)
        self.bullets = [x for x in self.bullets if x.is_alive()]

    # ── HUD ────────────────────────────────────────────────────────────────────
    def _build_hud(self):
        d = self.player.data
        self.hud_name = OnscreenText(
            text=f"{d['name']}  |  {d['role']}",
            pos=(-1.3, 0.91), scale=0.050,
            fg=d["color"], align=TextNode.ALeft,
        )
        self.hud_hp = OnscreenText(
            text=f"HP: {self.player.hp} / {self.player.max_hp}",
            pos=(-1.3, 0.83), scale=0.052,
            fg=(0.0, 1.0, 0.4, 1), align=TextNode.ALeft,
        )
        self.hud_ability = OnscreenText(
            text=f"[Q] {d['ability']}  —  {d['desc']}",
            pos=(-1.3, -0.93), scale=0.040,
            fg=(0.7, 0.9, 0.7, 1), align=TextNode.ALeft,
        )


app = OperacionNexus()
app.run()
