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
)

from player       import Player, CHARACTER_DATA
from bullet       import Bullet
from game_manager import GameManager

# ── ruta absoluta a assets/img/ ───────────────────────────────────────────────
_IMG_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "assets", "img")

def _img(name: str) -> str:
    """Convierte ruta Windows → formato Unix que Panda3D espera (/d/path/...).
    Filename.fromOsSpecific hace la conversión correcta de D:/... a /d/...
    """
    win_path = os.path.join(_IMG_DIR, name)
    return Filename.fromOsSpecific(win_path).getFullpath()


class OperacionNexus(ShowBase):
    def __init__(self):
        ShowBase.__init__(self)

        self.setBackgroundColor(0.05, 0.05, 0.10, 1)

        props = WindowProperties()
        props.setTitle("Operacion Nexus")
        self.win.requestProperties(props)

        self.player     = None
        self.menu_nodes = []
        self.vis_nodes  = []
        self._show_menu()

    # ══════════════════════════════════════════════════════════════════════════
    # MENÚ DE SELECCIÓN
    # ══════════════════════════════════════════════════════════════════════════
    def _show_menu(self):
        # ── título ────────────────────────────────────────────────────────────
        self.menu_nodes.append(OnscreenText(
            text="OPERACION NEXUS", pos=(0, 0.88), scale=0.115,
            fg=(0.0, 1.0, 0.4, 1), align=TextNode.ACenter,
        ))
        self.menu_nodes.append(OnscreenText(
            text="Selecciona tu operativo", pos=(0, 0.73), scale=0.048,
            fg=(0.55, 0.85, 0.55, 1), align=TextNode.ACenter,
        ))

        xs = [-0.62, 0.0, 0.62]
        for i, (key, d) in enumerate(CHARACTER_DATA.items()):
            x = xs[i]
            r, g, b, _ = d["color"]

            # ── tarjeta ───────────────────────────────────────────────────────
            self.menu_nodes.append(DirectFrame(
                frameColor=(0.06, 0.10, 0.06, 0.93),
                frameSize=(-0.27, 0.27, -0.50, 0.48),
                pos=(x, 0, 0.06),
            ))

            # ── imagen del operador ───────────────────────────────────────────
            tex = self.loader.loadTexture(_img(d["image"]))
            img = OnscreenImage(image=tex, pos=(x, 0, 0.30), scale=(0.19, 1, 0.22))
            img.setTransparency(TransparencyAttrib.MAlpha)
            self.menu_nodes.append(img)

            # ── nombre ────────────────────────────────────────────────────────
            self.menu_nodes.append(OnscreenText(
                text=d["name"], pos=(x, 0.06), scale=0.068,
                fg=d["color"], align=TextNode.ACenter,
            ))

            # ── rol ───────────────────────────────────────────────────────────
            self.menu_nodes.append(OnscreenText(
                text=d["role"], pos=(x, -0.01), scale=0.040,
                fg=(0.85, 0.85, 0.85, 1), align=TextNode.ACenter,
            ))

            # ── stats rápidos ─────────────────────────────────────────────────
            self.menu_nodes.append(OnscreenText(
                text=f"VEL {d['speed']}   HP {d['hp']}",
                pos=(x, -0.09), scale=0.033,
                fg=(0.60, 0.82, 0.60, 1), align=TextNode.ACenter,
            ))

            # ── botón JUGAR ───────────────────────────────────────────────────
            self.menu_nodes.append(DirectButton(
                text="Jugar",
                scale=0.052, pos=(x, 0, -0.27),
                command=self._start_game, extraArgs=[key],
                frameColor=(r * 0.55, g * 0.55, b * 0.55, 1.0),
                text_fg=(1, 1, 1, 1), relief=1,
            ))

            # ── botón VISUALIZAR ──────────────────────────────────────────────
            self.menu_nodes.append(DirectButton(
                text="Visualizar",
                scale=0.044, pos=(x, 0, -0.39),
                command=self._show_visualizar, extraArgs=[key],
                frameColor=(0.10, 0.16, 0.10, 1.0),
                text_fg=(0.75, 1.0, 0.75, 1), relief=1,
            ))

    def _clear_menu(self):
        for n in self.menu_nodes:
            n.destroy()
        self.menu_nodes.clear()

    # ══════════════════════════════════════════════════════════════════════════
    # PANEL VISUALIZAR
    # ══════════════════════════════════════════════════════════════════════════
    def _show_visualizar(self, key):
        d    = CHARACTER_DATA[key]
        r, g, b, _ = d["color"]

        # Ocultar tarjetas del menú mientras el panel está abierto
        for n in self.menu_nodes:
            n.hide()

        # ── overlay oscuro ────────────────────────────────────────────────────
        self.vis_nodes.append(DirectFrame(
            frameColor=(0, 0, 0, 0.84),
            frameSize=(-1.5, 1.5, -1.1, 1.1),
        ))

        # ── panel principal ───────────────────────────────────────────────────
        self.vis_nodes.append(DirectFrame(
            frameColor=(0.05, 0.08, 0.05, 0.97),
            frameSize=(-0.72, 0.72, -0.60, 0.60),
        ))

        # ── imagen grande (columna izquierda) ─────────────────────────────────
        big_tex = self.loader.loadTexture(_img(d["image"]))
        big_img = OnscreenImage(image=big_tex, pos=(-0.38, 0, 0.08), scale=(0.26, 1, 0.36))
        big_img.setTransparency(TransparencyAttrib.MAlpha)
        self.vis_nodes.append(big_img)

        # ── columna derecha: info ─────────────────────────────────────────────
        X = 0.04   # borde izquierdo de la columna derecha

        # nombre
        self.vis_nodes.append(OnscreenText(
            text=d["name"], pos=(X, 0.50), scale=0.085,
            fg=d["color"], align=TextNode.ALeft,
        ))
        # rol
        self.vis_nodes.append(OnscreenText(
            text=d["role"], pos=(X, 0.41), scale=0.044,
            fg=(0.88, 0.88, 0.88, 1), align=TextNode.ALeft,
        ))
        # stats
        self.vis_nodes.append(OnscreenText(
            text=f"Velocidad: {d['speed']}     HP: {d['hp']}",
            pos=(X, 0.32), scale=0.036,
            fg=(0.65, 0.88, 0.65, 1), align=TextNode.ALeft,
        ))
        # bio
        self.vis_nodes.append(OnscreenText(
            text=d["bio"], pos=(X, 0.21), scale=0.034,
            fg=(0.75, 0.80, 0.75, 1), align=TextNode.ALeft,
        ))

        # separador visual
        self.vis_nodes.append(OnscreenText(
            text="─" * 26,
            pos=(X, 0.11), scale=0.030,
            fg=(r * 0.6, g * 0.6, b * 0.6, 1), align=TextNode.ALeft,
        ))

        # encabezado capacidades
        self.vis_nodes.append(OnscreenText(
            text="CAPACIDADES",
            pos=(X, 0.03), scale=0.040,
            fg=d["color"], align=TextNode.ALeft,
        ))

        # ── las 3 habilidades ─────────────────────────────────────────────────
        z_slots = [-0.08, -0.25, -0.42]   # z base de cada habilidad

        for ab, z in zip(d["abilities"], z_slots):
            # mini-fondo de habilidad
            self.vis_nodes.append(DirectFrame(
                frameColor=(r * 0.08, g * 0.08, b * 0.08, 0.85),
                frameSize=(-0.01, 0.64, -0.10, 0.07),
                pos=(X, 0, z),
            ))
            # tag (PASIVA / HABILIDAD [Q] / ULTIMATE [R])
            self.vis_nodes.append(OnscreenText(
                text=ab["tag"], pos=(X + 0.02, z + 0.04), scale=0.026,
                fg=(r * 0.85, g * 0.85, b * 0.85, 1), align=TextNode.ALeft,
            ))
            # nombre de la habilidad
            self.vis_nodes.append(OnscreenText(
                text=ab["name"], pos=(X + 0.02, z - 0.02), scale=0.038,
                fg=d["color"], align=TextNode.ALeft,
            ))
            # descripción
            self.vis_nodes.append(OnscreenText(
                text=ab["desc"], pos=(X + 0.04, z - 0.10), scale=0.030,
                fg=(0.75, 0.82, 0.75, 1), align=TextNode.ALeft,
            ))

        # ── botón Volver ──────────────────────────────────────────────────────
        self.vis_nodes.append(DirectButton(
            text="<  Volver",
            scale=0.052, pos=(0, 0, -0.54),
            command=self._close_visualizar,
            frameColor=(0.10, 0.16, 0.10, 1.0),
            text_fg=(0.75, 1.0, 0.75, 1), relief=1,
        ))

    def _close_visualizar(self):
        for n in self.vis_nodes:
            n.destroy()
        self.vis_nodes.clear()
        # Restaurar el menú
        for n in self.menu_nodes:
            n.show()

    # ══════════════════════════════════════════════════════════════════════════
    # INICIO DEL JUEGO
    # ══════════════════════════════════════════════════════════════════════════
    def _start_game(self, character_key):
        # Cerrar panel Visualizar si estuviera abierto
        if self.vis_nodes:
            self._close_visualizar()
        self._clear_menu()
        self._build_floor()
        self._setup_camera()
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

    # ── cámara ─────────────────────────────────────────────────────────────────
    def _setup_camera(self):
        self.disableMouse()
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
