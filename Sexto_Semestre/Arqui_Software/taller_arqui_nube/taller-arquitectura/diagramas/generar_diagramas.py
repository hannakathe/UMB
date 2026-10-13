"""Genera los 10 diagramas del taller como .drawio (diagrams.net) y .mmd (Mermaid).

Uso:  python diagramas/generar_diagramas.py
Después: python diagramas/render_png.py   (renderiza cada .drawio a .png)
"""
from pathlib import Path
from xml.sax.saxutils import escape

OUT = Path(__file__).resolve().parent

# Paleta por atributo de calidad (fill, stroke)
C = {
    "esc": ("#dae8fc", "#6c8ebf"),   # Escalabilidad - azul
    "seg": ("#f8cecc", "#b85450"),   # Seguridad - rojo
    "ren": ("#ffe6cc", "#d79b00"),   # Rendimiento - naranja
    "dis": ("#d5e8d4", "#82b366"),   # Disponibilidad - verde
    "man": ("#e1d5e7", "#9673a6"),   # Mantenibilidad - morado
    "usa": ("#fff2cc", "#d6b656"),   # Usabilidad - amarillo
    "gris": ("#f5f5f5", "#666666"),
    "core": ("#d5e8d4", "#2d7600"),
    "rojo": ("#f8cecc", "#cc0000"),
    "verde": ("#d5e8d4", "#2d7600"),
    "nar": ("#ffe6cc", "#d79b00"),
    "azul": ("#dae8fc", "#6c8ebf"),
    "blanco": ("#ffffff", "#333333"),
}


def box(c, extra=""):
    f, s = C[c]
    return f"rounded=1;whiteSpace=wrap;html=1;fillColor={f};strokeColor={s};fontSize=12;arcSize=8;{extra}"


def sticky(c, extra=""):
    f, s = C[c]
    return f"shape=note;size=14;whiteSpace=wrap;html=1;fillColor={f};strokeColor={s};fontSize=12;shadow=1;align=left;spacingLeft=6;spacingRight=6;{extra}"


def cyl(c, extra=""):
    f, s = C[c]
    return f"shape=cylinder3;whiteSpace=wrap;html=1;boundedLbl=1;backgroundOutline=1;size=10;fillColor={f};strokeColor={s};fontSize=11;{extra}"


def cont(c, extra=""):
    f, s = C[c]
    return (f"rounded=1;whiteSpace=wrap;html=1;fillColor={f};strokeColor={s};dashed=1;verticalAlign=top;"
            f"align=left;spacingLeft=10;spacingTop=4;fontStyle=1;fontSize=13;arcSize=3;{extra}")


TITLE = "text;html=1;fontSize=20;fontStyle=1;align=center;verticalAlign=middle;"
TEXT = "text;html=1;fontSize=12;align=left;verticalAlign=top;whiteSpace=wrap;"
EDGE = "edgeStyle=orthogonalEdgeStyle;rounded=1;html=1;endArrow=block;endFill=1;strokeColor=#333333;fontSize=11;labelBackgroundColor=#ffffff;"
TREE = "edgeStyle=elbowEdgeStyle;elbow=horizontal;rounded=1;html=1;endArrow=none;strokeColor=#555555;exitX=1;exitY=0.5;entryX=0;entryY=0.5;"
STRAIGHT = "rounded=0;html=1;endArrow=block;endFill=1;strokeColor=#333333;fontSize=11;labelBackgroundColor=#ffffff;"


class Diagram:
    def __init__(self, name, title, w=1400, h=900):
        self.name, self.title, self.w, self.h = name, title, w, h
        self.cells, self.n = [], 0

    def _id(self):
        self.n += 1
        return f"c{self.n}"

    def node(self, label, x, y, w, h, style):
        i = self._id()
        self.cells.append(
            f'<mxCell id="{i}" value="{escape(label, {chr(34): "&quot;"})}" style="{style}" vertex="1" parent="1">'
            f'<mxGeometry x="{x}" y="{y}" width="{w}" height="{h}" as="geometry"/></mxCell>')
        return i

    def edge(self, s, t, label="", style=EDGE, src_lbl=None, tgt_lbl=None, points=None):
        i = self._id()
        label = label.replace("\n", "<br>")
        pts = ""
        if points:
            pts = '<Array as="points">' + "".join(f'<mxPoint x="{x}" y="{y}"/>' for x, y in points) + "</Array>"
        self.cells.append(
            f'<mxCell id="{i}" value="{escape(label, {chr(34): "&quot;"})}" style="{style}" edge="1" parent="1" source="{s}" target="{t}">'
            f'<mxGeometry relative="1" as="geometry">{pts}</mxGeometry></mxCell>')
        for lbl, pos in ((src_lbl, -0.75), (tgt_lbl, 0.75)):
            if lbl:
                j = self._id()
                self.cells.append(
                    f'<mxCell id="{j}" value="{escape(lbl)}" style="edgeLabel;html=1;align=center;verticalAlign=middle;resizable=0;points=[];fontSize=12;fontStyle=1;" vertex="1" connectable="0" parent="{i}">'
                    f'<mxGeometry x="{pos}" relative="1" as="geometry"><mxPoint y="-12" as="offset"/></mxGeometry></mxCell>')
        return i

    def arrow(self, p1, p2, label="", style=STRAIGHT, points=None, ox=0, oy=-11):
        """Flecha libre entre dos coordenadas (para el diagrama de secuencia)."""
        i = self._id()
        pts = ""
        if points:
            pts = '<Array as="points">' + "".join(f'<mxPoint x="{x}" y="{y}"/>' for x, y in points) + "</Array>"
        self.cells.append(
            f'<mxCell id="{i}" value="{escape(label, {chr(34): "&quot;"})}" style="{style}" edge="1" parent="1">'
            f'<mxGeometry relative="1" as="geometry"><mxPoint x="{p1[0]}" y="{p1[1]}" as="sourcePoint"/>'
            f'<mxPoint x="{p2[0]}" y="{p2[1]}" as="targetPoint"/>{pts}<mxPoint x="{ox}" y="{oy}" as="offset"/></mxGeometry></mxCell>')
        return i

    def save(self):
        self.node(self.title, 0, 0, self.w, 40, TITLE)
        xml = (f'<mxfile host="generar_diagramas.py"><diagram name="{escape(self.title)}" id="{self.name}">'
               f'<mxGraphModel dx="1200" dy="800" grid="1" gridSize="10" guides="1" tooltips="1" connect="1" arrows="1" '
               f'fold="1" page="1" pageScale="1" pageWidth="{self.w + 40}" pageHeight="{self.h + 40}" math="0" shadow="0">'
               f'<root><mxCell id="0"/><mxCell id="1" parent="0"/>{"".join(self.cells)}</root></mxGraphModel></diagram></mxfile>')
        (OUT / f"{self.name}.drawio").write_text(xml, encoding="utf-8")


# ---------------------------------------------------------------- 01 NFR
def d01():
    d = Diagram("01_nfr_eventflow", "01 · Lluvia de ideas de NFR de EventFlow por atributo de calidad", 1340, 760)
    center = d.node("<b>EventFlow</b><br>Requisitos No Funcionales (NFR)", 550, 55, 240, 60, box("blanco", "fontSize=14;"))
    cols = [
        ("esc", "📈 Escalabilidad", [
            "El sistema debe manejar <b>10.000 usuarios en 1 minuto</b> cuando salen a la venta las entradas de un gran concierto.",
            "Pasar de 1.000 a <b>100.000 usuarios concurrentes</b> (festival) añadiendo réplicas, sin cambiar código.",
        ]),
        ("seg", "🔒 Seguridad", [
            "Los datos de pago deben ser <b>100 % seguros</b>: tokenización (PCI DSS), TLS 1.3, nunca guardar el número de tarjeta.",
            "RBAC: un organizador solo ve las ventas de <b>sus</b> eventos; el administrador ve todos.",
            "Cada entrada lleva un <b>QR firmado</b> de un solo uso: no se puede falsificar ni revender el mismo ID.",
        ]),
        ("ren", "⚡ Rendimiento", [
            "Consulta de disponibilidad de asientos con <b>p95 ≤ 200 ms</b> durante un lanzamiento.",
            "Confirmar la compra en <b>≤ 2 s (p95)</b> con 10.000 usuarios concurrentes.",
        ]),
        ("dis", "🟢 Disponibilidad", [
            "<b>99,99 %</b> de disponibilidad del flujo de compra (≈ 52 min de caída al año).",
            "Si cae el servicio de Pagos, la orden queda <b>pendiente</b> y se reintenta: no se pierde ni se duplica.",
        ]),
        ("man", "🛠 Mantenibilidad", [
            "Cambiar la pasarela (Stripe → PayU) modificando <b>solo un adaptador</b>.",
            "Cada servicio se despliega de forma independiente en <b>&lt; 15 min</b> mediante CI/CD.",
        ]),
        ("usa", "🙂 Usabilidad", [
            "Comprar una entrada en <b>≤ 3 pasos</b> y menos de 90 s.",
            "Feedback visible en cada acción: <i>“Reservando tu entrada…”</i>.",
            "Carga masiva de eventos por <b>CSV/plantilla</b> para promotores frecuentes.",
        ]),
    ]
    for i, (c, head, notes) in enumerate(cols):
        x = 20 + i * 220
        h = d.node(f"<b>{head}</b>", x, 165, 200, 50, box(c, "fontSize=14;"))
        d.edge(center, h, style=EDGE + "endArrow=none;dashed=1;")
        for j, txt in enumerate(notes):
            d.node(txt, x, 240 + j * 125, 200, 110, sticky(c))
    d.node("Cada nota adhesiva es un NFR medible; el color indica el atributo de calidad con el que se relaciona.",
           20, 620, 1300, 30, TEXT + "fontStyle=2;align=center;")
    d.save()


# ---------------------------------------------------------------- 02 Stakeholders
def d02():
    d = Diagram("02_stakeholders", "02 · Mapa de stakeholders de EventFlow y su perspectiva", 1240, 720)
    center = d.node("<b>🎫 EventFlow</b><br>Plataforma de venta de entradas", 480, 280, 280, 100, box("azul", "fontSize=15;"))
    sts = [
        ("usa", 40, 50, "👤 Usuario (comprador)", "“Que la app sea rápida y fácil de usar”", "Usabilidad · Rendimiento"),
        ("seg", 920, 50, "💰 Finanzas", "“Que los pagos no fallen”", "Fiabilidad · Seguridad"),
        ("man", 40, 470, "👩‍💻 Desarrollador", "“Que sea fácil de mantener”", "Mantenibilidad · Modificabilidad"),
        ("ren", 920, 470, "🎤 Organizador de eventos", "“Ver mis ventas en tiempo real y cargar eventos rápido”", "Usabilidad (eficiencia) · Seguridad (RBAC)"),
        ("dis", 480, 540, "🛡 Administrador de la plataforma", "“Que esté siempre en línea y sea auditable”", "Disponibilidad · Seguridad"),
    ]
    for c, x, y, who, quote, attrs in sts:
        n = d.node(f"<b>{who}</b><br><i>{quote}</i><br><br>⇒ <b>{attrs}</b>", x, y, 280, 130, box(c, "fontSize=13;"))
        d.edge(n, center, style=STRAIGHT + "endArrow=none;dashed=1;strokeWidth=2;")
    d.save()


# ---------------------------------------------------------------- 03 Dominio
def cls(name, attrs, ops):
    return (f'<div style="text-align:center"><b>{name}</b></div><hr size="1">' + "<br>".join(attrs)
            + '<hr size="1">' + "<br>".join(ops))


def d03():
    d = Diagram("03_modelo_dominio", "03 · Modelo de dominio de EventFlow (DDD): Dominio Core vs. subdominios genéricos", 1360, 700)
    d.node("Subdominio genérico<br>(Login / Usuarios)", 20, 60, 270, 330, cont("gris"))
    d.node("⭐ DOMINIO CORE — Gestión de Inventario de Entradas y Pedidos", 330, 60, 680, 520, cont("core", "fillColor=#f3faf0;"))
    d.node("Subdominio de soporte<br>(Pagos → pasarela externa)", 1050, 280, 290, 300, cont("nar", "fillColor=#fffaf3;"))
    st = "rounded=0;whiteSpace=wrap;html=1;align=left;verticalAlign=top;spacingLeft=8;fontSize=12;"
    usuario = d.node(cls("Usuario", ["id: UUID", "nombre: String", "email: String", "rol: {Comprador, Organizador, Admin}"],
                         ["registrarse()", "iniciarSesion()"]), 40, 130, 230, 170, st + "fillColor=#f5f5f5;strokeColor=#666666;")
    evento = d.node(cls("Evento", ["id: UUID", "nombre: String", "fecha: DateTime", "lugar: String", "aforo: int"],
                        ["publicar()", "consultarDisponibilidad()"]), 370, 110, 240, 180, st + "fillColor=#d5e8d4;strokeColor=#2d7600;")
    entrada = d.node(cls("Entrada", ["id: UUID", "asiento: String (ej. F-12)", "precio: Decimal", "estado: {Disponible, Reservada, Vendida}", "qrFirmado: String"],
                         ["reservar()", "liberar()", "validarAcceso()"]), 730, 110, 250, 200, st + "fillColor=#d5e8d4;strokeColor=#2d7600;")
    orden = d.node(cls("Orden", ["id: UUID", "fecha: DateTime", "total: Decimal", "estado: {Pendiente, Pagada, Cancelada, Expirada}"],
                       ["crear()", "confirmar()", "cancelar()"]), 370, 370, 270, 180, st + "fillColor=#d5e8d4;strokeColor=#2d7600;")
    pago = d.node(cls("Pago", ["id: UUID", "monto: Decimal", "metodo: String", "tokenPasarela: String", "estado: {Aprobado, Rechazado}"],
                      ["autorizar()", "reembolsar()"]), 1070, 350, 250, 190, st + "fillColor=#ffe6cc;strokeColor=#d79b00;")
    E = STRAIGHT + "endArrow=none;strokeWidth=1.5;"
    O = EDGE + "endArrow=none;strokeWidth=1.5;"
    d.edge(usuario, evento, "organiza", E + "exitX=1;exitY=0.3;entryX=0;entryY=0.4;", "1", "0..*")
    d.edge(usuario, orden, "realiza", O + "exitX=0.5;exitY=1;entryX=0;entryY=0.5;", "1", "0..*")
    d.edge(evento, entrada, "emite", E + "exitX=1;exitY=0.4;entryX=0;entryY=0.36;", "1", "1..*")
    d.edge(orden, entrada, "contiene", O + "exitX=1;exitY=0.3;entryX=0.5;entryY=1;", "1", "1..*")
    d.edge(orden, pago, "se paga con", E + "exitX=1;exitY=0.75;entryX=0;entryY=0.8;", "1", "0..*")
    d.node("El <b>Dominio Core</b> es lo que diferencia a EventFlow en el mercado: vender sin sobreventa ni pérdidas el inventario "
           "limitado de entradas durante picos de demanda. Login/Usuarios es un problema resuelto (genérico) que puede comprarse o "
           "delegarse (OAuth, Auth0, Cognito); Pagos se delega a una pasarela.", 20, 600, 1320, 70, TEXT)
    d.save()


# ---------------------------------------------------------------- 04 Monolito
def d04():
    d = Diagram("04_monolito_capas", "04 · EventFlow como Monolito en Capas — “El Bloque Unido”", 1400, 760)
    nav = d.node("🌐 <b>Navegador del usuario</b>", 400, 50, 260, 45, box("blanco"))
    d.node("Monolito EventFlow (un solo artefacto desplegable)", 40, 115, 980, 455, cont("gris", "dashed=0;fillColor=#fafafa;"))
    bands = [
        ("azul", 150, "<b>Capa de Presentación (UI)</b><br>El navegador del usuario vive aquí",
         ["index.html", "CSS", "JavaScript"], "esc"),
        ("verde", 285, "<b>Capa de Lógica de Negocio</b><br>El “Cerebro”: reglas de negocio",
         ["<b>UserService</b><br>Registrar, Login", "<b>EventService</b><br>Listar eventos",
          "<b>OrderService</b><br>Crear orden, comprar entrada<br>🔥 99 % CPU", "<b>PaymentService</b><br>Procesar pago"], "dis"),
        ("nar", 435, "<b>Capa de Acceso a Datos (DAL)</b><br>El “Cartero”: habla con la BD",
         ["<b>UserDAO</b><br>CRUD Usuarios", "<b>EventDAO</b><br>CRUD Eventos", "<b>OrderDAO</b><br>CRUD Órdenes", "<b>PaymentDAO</b><br>CRUD Pagos"], "ren"),
    ]
    band_ids = []
    for c, y, lbl, items, ic in bands:
        b = d.node("", 60, y, 940, 120, box(c, "arcSize=4;"))
        band_ids.append(b)
        d.node(lbl, 70, y + 10, 230, 100, TEXT + "verticalAlign=middle;")
        for k, it in enumerate(items):
            st = box("blanco")
            if "99 %" in it:
                st = box("rojo", "strokeWidth=3;")
            d.node(it, 310 + k * 172, y + 20, 160, 80, st)
    db = d.node("<b>Base de Datos única (SQL)</b><br>Users · Events · Orders · Payments", 400, 600, 260, 120, cyl("azul", "fontSize=12;"))
    d.edge(nav, band_ids[0], "HTTP")
    d.edge(band_ids[0], band_ids[1], "llama")
    d.edge(band_ids[1], band_ids[2], "usa")
    d.edge(band_ids[2], db, "SQL")
    d.node("✔ <b>Beneficio</b><br>Separar la lógica de negocio de la presentación <b>facilita la reutilización del código</b>: "
           "la misma OrderService puede servir a la web, a una app móvil o a una API.", 1060, 115, 320, 150, sticky("verde"))
    d.node("⚠ <b>Problema (NFR de 10.000 usuarios)</b><br>En un pico de venta OrderService consume el 99 % de la CPU y "
           "<b>todo</b> el monolito se vuelve lento: login, lista de eventos… No se puede escalar solo OrderService: hay que "
           "replicar la aplicación entera (y la BD única sigue siendo un cuello de botella).<br><b>Atributo afectado: Escalabilidad.</b>",
           1060, 290, 320, 250, sticky("rojo"))
    d.save()


# ---------------------------------------------------------------- 05 Microservicios
def d05():
    d = Diagram("05_microservicios", "05 · EventFlow como Microservicios + API Gateway + Bus de Eventos — “El Equipo de Especialistas”", 1560, 700)
    nav = d.node("🌐 <b>Navegador / App móvil</b>", 560, 55, 260, 45, box("blanco"))
    gw = d.node("<b>API GATEWAY</b> (El Portero)<br>autenticación JWT · enrutamiento · rate limiting", 490, 130, 400, 60, box("gris", "fontSize=13;"))
    d.edge(nav, gw, "HTTP/REST")
    svcs = [
        ("gris", "<b>Servicio-Usuarios</b><br><i>subdominio genérico</i>", "DB_Usr"),
        ("core", "<b>Servicio-Eventos</b><br><i>dominio core</i>", "DB_Evt"),
        ("core", "<b>Servicio-Pedidos</b><br><i>dominio core</i><br>×50 réplicas en el pico", "DB_Ord"),
        ("core", "<b>Servicio-Entradas</b><br><i>inventario · core</i>", "DB_Ent"),
        ("nar", "<b>Servicio-Pagos</b><br><i>soporte</i>", "DB_Pay"),
    ]
    W, Y = 190, 250
    bus = d.node("<b>BUS DE EVENTOS</b> (Kafka / RabbitMQ) — tópico <i>pagos.aprobados</i>", 40, 500, 1260, 55,
                 box("man", "fontSize=14;strokeWidth=2;"))
    ids = []
    for k, (c, lbl, db) in enumerate(svcs):
        x = 40 + k * 265
        extra = "strokeWidth=2;"
        if "×50" in lbl:
            d.node("", x + 16, Y - 16, W, 90, box(c, "opacity=40;"))
            d.node("", x + 8, Y - 8, W, 90, box(c, "opacity=70;"))
            extra += "shadow=1;"
        s = d.node(lbl, x, Y, W, 90, box(c, extra))
        ids.append((s, x))
        dbn = d.node(f"<b>{db}</b>", x, Y + 125, 95, 70, cyl("azul"))
        d.edge(s, dbn, style=STRAIGHT + "exitX=0.25;exitY=1;entryX=0.5;entryY=0;")
        d.edge(gw, s, style=EDGE + "strokeColor=#888888;")
    def bus_link(svc, x, label, publish):
        ex = round((x + 0.8 * W - 40) / 1260, 4)
        if publish:
            d.edge(svc, bus, label, EDGE + f"exitX=0.8;exitY=1;entryX={ex};entryY=0;strokeColor=#9673a6;strokeWidth=3;dashed=1;")
        else:
            d.edge(bus, svc, label, EDGE + f"exitX={ex};exitY=0;entryX=0.8;entryY=1;strokeColor=#9673a6;strokeWidth=2;")
    bus_link(ids[4][0], ids[4][1], "publica 'PagoAprobado'", True)
    bus_link(ids[2][0], ids[2][1], "se suscribe", False)
    bus_link(ids[3][0], ids[3][1], "se suscribe", False)
    pas = d.node("💳 <b>Pasarela externa</b><br>(Stripe / PayU)", 1360, Y, 170, 90, box("blanco", "dashed=1;"))
    d.edge(ids[4][0], pas, "vía adaptador")
    d.node("Cada servicio tiene su propia base de datos y se despliega/escala de forma independiente. "
           "Pagos no conoce a sus suscriptores: publica el evento y el bus lo entrega (invocación implícita).",
           40, 590, 1480, 50, TEXT + "fontStyle=2;align=center;")
    d.save()


# ---------------------------------------------------------------- 06 Secuencia
def d06():
    d = Diagram("06_secuencia_pago_aprobado", "06 · Diagrama de secuencia del evento PagoAprobado (invocación implícita)", 1300, 800)
    parts = [("👤 Comprador", "blanco"), ("Servicio-Pedidos", "core"), ("Servicio-Pagos", "nar"),
             ("💳 Pasarela", "gris"), ("Bus de Eventos", "man"), ("Servicio-Entradas", "core")]
    cx = {}
    for k, (p, c) in enumerate(parts):
        x = 40 + k * 210
        f, s = C[c]
        d.node(f"<b>{p}</b>", x, 60, 160, 680,
               f"shape=umlLifeline;perimeter=lifelinePerimeter;whiteSpace=wrap;html=1;container=0;collapsible=0;"
               f"recursiveResize=0;outlineConnect=0;size=45;fillColor={f};strokeColor={s};fontSize=12;")
        cx[k] = x + 80
    SYNC = STRAIGHT
    REPLY = STRAIGHT + "dashed=1;endArrow=open;"
    EVT = STRAIGHT + "strokeColor=#9673a6;strokeWidth=3;fontStyle=1;"
    msgs = [
        (0, 1, "1. POST /pedidos (Idempotency-Key)", SYNC),
        (1, 0, "2. 201 Orden PENDIENTE_PAGO (reserva 10 min)", REPLY),
        (0, 2, "3. Pagar orden (tarjeta tokenizada)", SYNC),
        (2, 3, "4. Autorizar cargo", SYNC),
        (3, 2, "5. Aprobado", REPLY),
        (2, 4, "6. publish PagoAprobado {ordenId, monto}", EVT),
        (2, 0, "7. 202 “Pago en verificación…”", REPLY),
        (4, 1, "8. entrega PagoAprobado (suscriptor)", EVT),
        (1, 1, "9. Orden → PAGADA", SYNC),
        (4, 5, "10. entrega PagoAprobado (suscriptor)", EVT),
        (5, 5, "11. Emitir entradas con QR firmado", SYNC),
        (5, 4, "12. publish EntradasEmitidas", EVT),
    ]
    for i, (a, b, lbl, st) in enumerate(msgs):
        y = 140 + i * 47
        if a == b:
            d.arrow((cx[a], y), (cx[a], y + 22), lbl, st + "align=left;labelBackgroundColor=none;",
                    points=[(cx[a] + 40, y), (cx[a] + 40, y + 22)], ox=-25, oy=0)
        else:
            d.arrow((cx[a], y), (cx[b], y), lbl, st + "labelBackgroundColor=none;")
    d.node("💡 <b>Invocación implícita:</b> Servicio-Pagos <u>no llama</u> a Pedidos ni a Entradas; solo publica "
           "<b>PagoAprobado</b> en el bus. Cada suscriptor reacciona por su cuenta. Agregar un nuevo suscriptor "
           "(p. ej. Notificaciones) no exige tocar Pagos.", 40, 745, 1240, 50, sticky("man", "align=center;"))
    d.save()


# ---------------------------------------------------------------- 07 Árbol de utilidad
def d07():
    rows = [
        ("esc", "📈 Escalabilidad", "Picos de venta", "(A,A)", "10.000 usuarios llegan en 1 min al abrir la venta de un concierto → el sistema escala Pedidos y Pagos automáticamente y atiende con tasa de error &lt; 1 %."),
        ("esc", "📈 Escalabilidad", "Crecimiento de demanda", "(A,M)", "Pasar de 1.000 a 100.000 usuarios concurrentes (festival) añadiendo réplicas detrás del balanceador, sin cambiar código."),
        ("ren", "⚡ Rendimiento", "Latencia", "(A,A)", "La consulta de disponibilidad responde con p95 ≤ 200 ms con 10.000 usuarios concurrentes."),
        ("ren", "⚡ Rendimiento", "Concurrencia", "(A,A)", "Dos usuarios compran el último asiento F-12 a la vez → solo uno lo obtiene, cero sobreventa, respuesta &lt; 2 s."),
        ("dis", "🟢 Disponibilidad", "Tolerancia a fallos", "(A,M)", "Cae una instancia de Pedidos → el balanceador la retira en &lt; 5 s; disponibilidad mensual ≥ 99,99 %."),
        ("seg", "🔒 Seguridad", "Confidencialidad de pagos", "(A,M)", "Ningún número de tarjeta se almacena (tokenización PCI DSS); 0 incidentes de exposición."),
        ("seg", "🔒 Seguridad", "Integridad de entradas", "(A,M)", "Un QR duplicado o alterado se rechaza en el 100 % de los accesos al evento."),
        ("seg", "🔒 Seguridad", "Autorización (RBAC)", "(M,B)", "Un organizador que pide ventas de un evento ajeno recibe 403 en el 100 % de los casos."),
        ("man", "🛠 Mantenibilidad", "Modificabilidad de pasarela", "(M,M)", "Cambiar Stripe → PayU en ≤ 5 días-persona tocando solo el adaptador de Pagos."),
        ("man", "🛠 Mantenibilidad", "Cambio de bus", "(M,M)", "Migrar Kafka → RabbitMQ afectando solo a los servicios que publican/se suscriben."),
        ("usa", "🙂 Usabilidad", "Eficiencia del promotor", "(M,B)", "Un promotor carga 50 eventos con una plantilla CSV en &lt; 10 min."),
    ]
    RH, Y0 = 72, 70
    d = Diagram("07_arbol_utilidad_atam", "07 · Árbol de Utilidad ATAM de EventFlow (Importancia, Dificultad)", 1300, Y0 + len(rows) * RH + 60)
    total_h = len(rows) * RH
    root = d.node("<b>Utilidad</b><br>EventFlow", 20, Y0 + total_h / 2 - 40, 130, 80, box("blanco", "fontSize=15;"))
    attrs = {}
    for i, r in enumerate(rows):
        attrs.setdefault(r[1], []).append(i)
    for a, idxs in attrs.items():
        c = rows[idxs[0]][0]
        y = Y0 + (idxs[0] + idxs[-1]) / 2 * RH
        an = d.node(f"<b>{a}</b>", 195, y + 5, 175, 52, box(c, "fontSize=13;"))
        d.edge(root, an, style=TREE)
        for i in idxs:
            _, _, ref, pr, sc = rows[i]
            yy = Y0 + i * RH
            rn = d.node(ref, 410, yy + 10, 190, 42, box(c))
            d.edge(an, rn, style=TREE)
            hot = pr == "(A,A)"
            sn = d.node(f"<b>{pr}</b> {sc}", 640, yy + 3, 640, 58,
                        box("rojo" if hot else "blanco", "align=left;spacingLeft=8;" + ("strokeWidth=2;" if hot else "")))
            d.edge(rn, sn, style=TREE)
    d.node("(Importancia para el negocio, Dificultad técnica): A = Alta · M = Media · B = Baja. "
           "En rojo los escenarios (A,A): son los que guían la arquitectura y se analizan primero en el ATAM.",
           20, Y0 + total_h + 10, 1260, 40, TEXT + "fontStyle=2;")
    d.save()


# ---------------------------------------------------------------- 08 SUMP actual
def d08():
    d = Diagram("08_sump_actual", "08 · Arquitectura ACTUAL del SUMP — el “Frankenstein” (punto de sensibilidad y riesgo en rojo)", 1440, 820)
    est = d.node("👥<br><b>20.000 estudiantes</b><br>lunes 8:00 a.m.", 20, 230, 140, 90, box("blanco"))
    d.node("🏢 Data center On-Premise (servidor físico anticuado)", 190, 60, 640, 560, cont("gris"))
    d.node("<b>Monolito Java 8 · Spring MVC</b> (registro de asignaturas)", 220, 105, 580, 235, box("azul", "verticalAlign=top;arcSize=3;"))
    jsp = d.node("Vistas web (JSP)", 240, 150, 160, 60, box("blanco"))
    ins = d.node("Controlador<br>Inscripciones", 420, 150, 160, 60, box("blanco"))
    pgw = d.node("Controlador<br><b>PaymentGateway</b>", 600, 150, 180, 60, box("rojo", "strokeWidth=3;"))
    d.node("Pool de hilos (p. ej. 200)<br><b>hilos bloqueados esperando indefinidamente</b>", 600, 240, 180, 80,
           box("rojo", "dashed=1;"))
    ora = d.node("<b>Oracle centralizada</b><br>bloqueos ACID estrictos", 380, 410, 220, 130, cyl("nar", "fontSize=12;"))
    d.node("⚖ <b>Punto de trade-off</b>: validación de 5 pasos + bloqueo ACID (Seguridad/Consistencia) vs. respuesta ≤ 2 s para 20.000 usuarios (Rendimiento/Disponibilidad).",
           620, 400, 195, 190, sticky("nar", "fontSize=11;"))
    d.edge(est, jsp, "HTTPS", EDGE + "entryX=0;entryY=0.5;")
    d.edge(jsp, ins)
    d.edge(ins, pgw)
    d.edge(ins, ora, "JDBC (SQL directo)", STRAIGHT + "exitX=0.5;exitY=1;entryX=0.55;entryY=0;")
    d.node("☁ AWS", 980, 60, 430, 330, cont("nar", "fillColor=#fffaf3;"))
    node = d.node("<b>Microservicio “Módulo de Pagos”</b><br>Node.js (creado por un practicante)", 1010, 110, 370, 80, box("nar"))
    dbp = d.node("BD de pagos", 1120, 250, 150, 100, cyl("gris"))
    d.edge(node, dbp)
    pas = d.node("💳 <b>Pasarela de pagos actual</b><br>(proveedor externo)", 1010, 460, 370, 70, box("blanco", "dashed=1;"))
    d.edge(node, pas, "API del proveedor", EDGE + "exitX=0.15;exitY=1;entryX=0.15;entryY=0;")
    d.edge(pgw, node, "HTTP REST SÍNCRONO\nvía VPN inestable\n(sin timeout)",
           EDGE + "strokeColor=#cc0000;strokeWidth=4;dashed=1;fontColor=#cc0000;fontStyle=1;")
    d.node("🔴 <b>Punto de sensibilidad</b><br>La llamada REST síncrona Java → Node por la VPN. El tiempo de respuesta de "
           "<i>cada</i> inscripción queda atado a la latencia de la VPN y de AWS: si AWS tarda 3 s, la inscripción tarda ≥ 3 s.",
           190, 650, 600, 140, sticky("rojo", "strokeWidth=2;"))
    d.node("🔴 <b>Riesgo crítico</b><br>Sin timeout ni circuit breaker, cada fallo de la VPN deja un hilo bloqueado. Con 20.000 "
           "estudiantes el pool se agota en segundos → <b>colapso total</b> a las 8:00 a.m. Además, no hay transacción entre Oracle "
           "y la BD de pagos → <b>pagos perdidos / transacciones huérfanas</b>.",
           820, 650, 590, 140, sticky("rojo", "strokeWidth=2;"))
    d.save()


# ---------------------------------------------------------------- 09 SUMP propuesto
def d09():
    d = Diagram("09_sump_propuesto", "09 · Arquitectura PROPUESTA del SUMP: asíncrona, con outbox, caché, anticorrupción y circuit breaker", 1660, 860)
    est = d.node("👥<br><b>20.000 estudiantes</b>", 20, 230, 130, 80, box("blanco"))
    lb = d.node("<b>CDN + WAF<br>Balanceador de carga</b>", 180, 225, 150, 90, box("azul"))
    d.node("Monolito Java modernizado (N réplicas, sin estado)", 370, 60, 480, 290, cont("azul", "fillColor=#f4f8fd;dashed=0;"))
    ins = d.node("<b>Módulo Inscripciones</b><br>responde en &lt; 2 s", 390, 220, 210, 70, box("blanco"))
    cons = d.node("<b>Consumidor</b> de eventos<br><i>PagoConfirmado</i>", 620, 220, 210, 70, box("blanco"))
    d.node("🛡 <b>Circuit breaker + timeout 2 s</b> (Resilience4j) para consultas puntuales de estado de pago; fallback: “Pago en verificación”",
           390, 100, 440, 90, box("verde", "strokeWidth=2;"))
    redis = d.node("<b>Caché Redis</b><br>catálogo de asignaturas y cupos (TTL corto)", 370, 400, 210, 110, cyl("rojo", "fillColor=#fde9e9;"))
    ora = d.node("<b>Oracle</b><br>inscripciones + tabla <b>OUTBOX</b><br>(misma transacción ACID)", 620, 400, 230, 130, cyl("nar"))
    relay = d.node("<b>Relay Outbox / CDC</b><br>(p. ej. Debezium)", 900, 430, 180, 70, box("man"))
    q = d.node("📨 <b>Cola de mensajería</b><br>RabbitMQ / Amazon SQS<br>durable · reintentos · DLQ", 900, 175, 200, 110,
               box("man", "strokeWidth=2;"))
    d.node("☁ AWS", 1150, 60, 490, 560, cont("nar", "fillColor=#fffaf3;"))
    pag = d.node("<b>Servicio de Pagos</b> (Node.js)<br>consumidor <b>idempotente</b> (clave = id de matrícula)", 1180, 110, 430, 75, box("nar"))
    dbp = d.node("<b>BD Pagos</b><br>ACID + registro de idempotencia", 1180, 220, 200, 100, cyl("nar"))
    port = d.node("«interfaz» <b>PuertoPasarelaPago</b><br>capa anticorrupción", 1180, 355, 430, 60, box("verde", "strokeWidth=2;"))
    a1 = d.node("Adaptador<br>pasarela actual", 1180, 455, 200, 60, box("blanco"))
    a2 = d.node("<b>Adaptador Recaudo<br>Gubernamental (nuevo)</b>", 1410, 455, 200, 60, box("verde"))
    e1 = d.node("💳 Pasarela actual", 1180, 660, 200, 55, box("blanco", "dashed=1;"))
    e2 = d.node("🏛 Sistema gubernamental<br>de recaudos", 1410, 660, 200, 55, box("blanco", "dashed=1;"))
    d.edge(est, lb)
    d.edge(lb, ins, style=EDGE + "entryX=0;entryY=0.5;")
    d.edge(ins, redis, "lee catálogo", EDGE + "exitX=0.3;exitY=1;")
    d.edge(ins, ora, "escribe inscripción\n+ evento outbox", EDGE + "exitX=0.8;exitY=1;entryX=0.3;entryY=0;")
    d.edge(ora, relay)
    d.edge(relay, q, "SolicitudPago")
    d.edge(q, pag, "consume", EDGE + "exitY=0.25;exitX=1;entryX=0;entryY=0.3;")
    d.edge(pag, q, "publica", EDGE + "exitX=0;exitY=0.85;entryX=1;entryY=0.75;dashed=1;")
    d.edge(q, cons, "PagoConfirmado", EDGE + "exitX=0;exitY=0.5;entryX=1;entryY=0.5;")
    d.edge(pag, dbp, style=EDGE + "exitX=0.25;exitY=1;")
    d.edge(pag, port, style=EDGE + "exitX=0.75;exitY=1;entryX=0.75;entryY=0;")
    d.edge(port, a1)
    d.edge(port, a2)
    d.edge(a1, e1)
    d.edge(a2, e2)
    d.node("✔ <b>Gana:</b> Disponibilidad y Rendimiento — la inscripción ya no espera a AWS; un fallo de la VPN no bloquea hilos.<br>"
           "✔ <b>Gana:</b> Modificabilidad — la nueva pasarela es un adaptador más detrás del puerto.<br>"
           "✖ <b>Se sacrifica:</b> consistencia inmediata (estado “Pago en verificación” durante segundos → consistencia eventual) "
           "y simplicidad operativa (cola, relay y caché que monitorear).", 20, 740, 1130, 105, sticky("verde"))
    d.save()


# ---------------------------------------------------------------- 10 SAAM
def d10():
    d = Diagram("10_saam_impacto", "10 · Análisis SAAM de impacto: componentes afectados por cada escenario de cambio", 1520, 700)
    d.node("Escenario 1 · EventFlow: cambiar el Bus de Eventos de Kafka a RabbitMQ", 20, 60, 720, 560, cont("gris", "fillColor=#fcfcfc;"))
    gw = d.node("API Gateway", 260, 110, 240, 45, box("verde"))
    names = [("Servicio-<br>Usuarios", "verde"), ("Servicio-<br>Eventos", "verde"), ("Servicio-<br>Pedidos<br>(suscriptor)", "rojo"),
             ("Servicio-<br>Entradas<br>(suscriptor)", "rojo"), ("Servicio-<br>Pagos<br>(publicador)", "rojo")]
    svc = []
    for k, (n, c) in enumerate(names):
        s = d.node(f"<b>{n}</b>", 40 + k * 137, 200, 125, 80, box(c, "strokeWidth=2;" if c == "rojo" else ""))
        svc.append(s)
        d.edge(gw, s, style=EDGE + "strokeColor=#999999;")
    bus = d.node("<b>Bus de eventos: Kafka → RabbitMQ</b> (se reemplaza)", 40, 340, 680, 55, box("rojo", "strokeWidth=3;"))
    for k in (2, 3, 4):
        d.edge(svc[k], bus, style=STRAIGHT + "endArrow=none;strokeColor=#cc0000;strokeWidth=2;dashed=1;")
    d.node("<b>Conteo:</b> 3 de 5 servicios (solo su cliente/adaptador de mensajería) + la infraestructura del bus. "
           "Usuarios, Eventos y el API Gateway <b>ni se enteran</b>.<br>Si los servicios usan un puerto común "
           "<i>PublicadorEventos/SuscriptorEventos</i>, el cambio se reduce a 1 librería compartida + configuración.<br>"
           "<b>Veredicto: ALTA modificabilidad ✅</b>", 40, 420, 680, 180, sticky("verde"))

    d.node("Escenario 2 · SUMP: nueva pasarela gubernamental de recaudos en 30 días", 780, 60, 720, 560, cont("gris", "fillColor=#fcfcfc;"))
    comp = [("Interfaz web (pantallas de pago JSP)", "rojo"), ("Monolito Java (controlador PaymentGateway, lógica de matrícula)", "rojo"),
            ("Microservicio de Pagos Node.js (integración con la pasarela)", "rojo"),
            ("BD Oracle (estados de pago, referencias, conciliación)", "rojo"), ("VPN / red (reglas, certificados del gobierno)", "nar"),
            ("Reportes de Contabilidad (conciliación)", "nar")]
    for k, (n, c) in enumerate(comp):
        x = 800 + (k % 3) * 230
        y = 115 + (k // 3) * 120
        d.node(f"<b>{n}</b>", x, y, 215, 95, box(c, "strokeWidth=2;" if c == "rojo" else ""))
    d.node("<b>Conteo:</b> 4 componentes afectados con seguridad (UI, Monolito, Microservicio, Oracle) + 2 probables (VPN, reportes) "
           "= 4–6 de 6. No existe una interfaz que aísle la pasarela: el monolito conoce los detalles del microservicio y la BD "
           "guarda datos propios del proveedor, así que el cambio <b>se propaga</b>.<br><b>Veredicto: BAJA modificabilidad ❌</b> — "
           "alto riesgo de incumplir el plazo legal de 30 días.", 800, 365, 680, 235, sticky("rojo"))
    d.node("Leyenda: 🟥 afectado · 🟧 probablemente afectado · 🟩 no afectado", 20, 640, 1480, 30, TEXT + "align=center;fontStyle=2;")
    d.save()


# ---------------------------------------------------------------- Mermaid
MMD = {
"01_nfr_eventflow": """mindmap
  root((EventFlow NFR))
    Escalabilidad
      10.000 usuarios en 1 minuto al abrir la venta
      De 1.000 a 100.000 concurrentes sin cambiar código
    Seguridad
      Datos de pago 100% seguros - tokenización PCI DSS
      RBAC organizador vs administrador
      QR firmado de un solo uso
    Rendimiento
      Disponibilidad de asientos p95 <= 200 ms
      Confirmación de compra <= 2 s
    Disponibilidad
      99,99% del flujo de compra
      Orden pendiente + reintento si cae Pagos
    Mantenibilidad
      Cambiar pasarela tocando solo un adaptador
      Despliegue independiente < 15 min
    Usabilidad
      Compra en <= 3 pasos
      Feedback Reservando tu entrada...
      Carga masiva por CSV
""",
"02_stakeholders": """flowchart TB
    E(("EventFlow"))
    U["Usuario comprador<br/>'Que la app sea rápida y fácil de usar'<br/>Usabilidad - Rendimiento"] --- E
    F["Finanzas<br/>'Que los pagos no fallen'<br/>Fiabilidad - Seguridad"] --- E
    D["Desarrollador<br/>'Que sea fácil de mantener'<br/>Mantenibilidad"] --- E
    O["Organizador<br/>'Ver mis ventas y cargar eventos rápido'<br/>Usabilidad - Seguridad RBAC"] --- E
    A["Administrador<br/>'Siempre en línea y auditable'<br/>Disponibilidad - Seguridad"] --- E
""",
"03_modelo_dominio": """classDiagram
    direction LR
    class Usuario {
      +UUID id
      +String nombre
      +String email
      +Rol rol
      +registrarse()
      +iniciarSesion()
    }
    class Evento {
      +UUID id
      +String nombre
      +DateTime fecha
      +int aforo
      +consultarDisponibilidad()
    }
    class Entrada {
      +UUID id
      +String asiento
      +Estado estado
      +String qrFirmado
      +reservar()
    }
    class Orden {
      +UUID id
      +Decimal total
      +Estado estado
      +confirmar()
    }
    class Pago {
      +UUID id
      +Decimal monto
      +String tokenPasarela
      +autorizar()
    }
    Usuario "1" -- "0..*" Evento : organiza
    Usuario "1" -- "0..*" Orden : realiza
    Evento "1" -- "1..*" Entrada : emite
    Orden "1" -- "1..*" Entrada : contiene
    Orden "1" -- "0..*" Pago : se paga con
    note for Evento "Dominio Core: Evento, Entrada, Orden (inventario y pedidos)"
    note for Usuario "Subdominio genérico (Login)"
""",
"04_monolito_capas": """flowchart TB
    N["Navegador del usuario"] --> P
    subgraph M["Monolito EventFlow"]
      P["Presentación: index.html, CSS, JavaScript"]
      subgraph L["Lógica de negocio"]
        US[UserService] --- ES[EventService] --- OS["OrderService 99% CPU"] --- PS[PaymentService]
      end
      subgraph DAL["Acceso a datos"]
        UD[UserDAO] --- ED[EventDAO] --- OD[OrderDAO] --- PD[PaymentDAO]
      end
      P --> L --> DAL
    end
    DAL --> DB[("BD única SQL: Users, Events, Orders, Payments")]
    classDef hot fill:#f8cecc,stroke:#cc0000,stroke-width:3px
    class OS hot
""",
"05_microservicios": """flowchart TB
    N["Navegador / App"] -->|HTTP/REST| GW["API Gateway"]
    GW --> SU[Servicio-Usuarios] --> DU[(DB_Usr)]
    GW --> SE[Servicio-Eventos] --> DE[(DB_Evt)]
    GW --> SP["Servicio-Pedidos x50"] --> DO[(DB_Ord)]
    GW --> SN[Servicio-Entradas] --> DN[(DB_Ent)]
    GW --> SG[Servicio-Pagos] --> DG[(DB_Pay)]
    SG -. "publica PagoAprobado" .-> BUS{{"Bus de eventos Kafka / RabbitMQ"}}
    BUS -- "se suscribe" --> SP
    BUS -- "se suscribe" --> SN
    SG --> PX["Pasarela externa"]
    classDef core fill:#d5e8d4,stroke:#2d7600
    class SE,SP,SN core
""",
"06_secuencia_pago_aprobado": """sequenceDiagram
    actor C as Comprador
    participant PE as Servicio-Pedidos
    participant PA as Servicio-Pagos
    participant PS as Pasarela
    participant B as Bus de Eventos
    participant EN as Servicio-Entradas
    C->>PE: 1. POST /pedidos (Idempotency-Key)
    PE-->>C: 2. 201 Orden PENDIENTE_PAGO
    C->>PA: 3. Pagar orden (token)
    PA->>PS: 4. Autorizar cargo
    PS-->>PA: 5. Aprobado
    PA->>B: 6. publish PagoAprobado {ordenId, monto}
    PA-->>C: 7. 202 Pago en verificación
    B->>PE: 8. entrega PagoAprobado
    PE->>PE: 9. Orden a PAGADA
    B->>EN: 10. entrega PagoAprobado
    EN->>EN: 11. Emitir QR firmado
    EN->>B: 12. publish EntradasEmitidas
    Note over PA,EN: Invocación implícita: Pagos no conoce a sus suscriptores
""",
"07_arbol_utilidad_atam": """flowchart LR
    U((Utilidad)) --> E[Escalabilidad] & R[Rendimiento] & D[Disponibilidad] & S[Seguridad] & M[Mantenibilidad] & Us[Usabilidad]
    E --> E1["(A,A) 10.000 usuarios en 1 min, error < 1%"]
    E --> E2["(A,M) 1.000 a 100.000 concurrentes con réplicas"]
    R --> R1["(A,A) Disponibilidad p95 <= 200 ms"]
    R --> R2["(A,A) Último asiento F-12: cero sobreventa"]
    D --> D1["(A,M) Falla instancia: retirada < 5 s, 99,99%"]
    S --> S1["(A,M) Tokenización PCI DSS"]
    S --> S2["(A,M) QR duplicado rechazado 100%"]
    S --> S3["(M,B) RBAC: 403 a eventos ajenos"]
    M --> M1["(M,M) Stripe a PayU solo en adaptador"]
    M --> M2["(M,M) Kafka a RabbitMQ"]
    Us --> U1["(M,B) 50 eventos por CSV < 10 min"]
    classDef aa fill:#f8cecc,stroke:#cc0000,stroke-width:2px
    class E1,R1,R2 aa
""",
"08_sump_actual": """flowchart LR
    EST["20.000 estudiantes"] --> JSP
    subgraph ONP["On-Premise"]
      JSP[Vistas JSP] --> INS[Controlador Inscripciones] --> PGW[Controlador PaymentGateway]
      INS --> ORA[("Oracle centralizada - ACID estricto")]
    end
    subgraph AWS["AWS"]
      NODE["Microservicio Pagos Node.js"] --> DBP[(BD pagos)]
    end
    PGW == "REST SÍNCRONO por VPN inestable, sin timeout" ==> NODE
    NODE --> PAS["Pasarela actual"]
    classDef risk fill:#f8cecc,stroke:#cc0000,stroke-width:3px
    class PGW risk
    linkStyle 3 stroke:#cc0000,stroke-width:4px
""",
"09_sump_propuesto": """flowchart LR
    EST[Estudiantes] --> LB["CDN + WAF + Balanceador"] --> INS["Monolito Java N réplicas - Inscripciones"]
    INS --> REDIS[("Caché Redis")]
    INS --> ORA[("Oracle + tabla OUTBOX")]
    ORA --> RELAY["Relay Outbox / CDC"] --> Q{{"Cola RabbitMQ / SQS"}}
    Q --> PAG["Servicio Pagos idempotente"] --> DBP[(BD Pagos)]
    PAG -. PagoConfirmado .-> Q
    Q --> CONS["Consumidor PagoConfirmado"]
    PAG --> PORT["Puerto PasarelaPago - anticorrupción"]
    PORT --> A1[Adaptador pasarela actual] --> E1[Pasarela actual]
    PORT --> A2[Adaptador recaudo gubernamental] --> E2[Sistema gubernamental]
    CB["Circuit breaker + timeout 2 s"] -.-> INS
""",
"10_saam_impacto": """flowchart TB
    subgraph S1["Escenario 1: Kafka a RabbitMQ (EventFlow)"]
      GW[API Gateway]:::ok --> SU[Usuarios]:::ok & SE[Eventos]:::ok & SP[Pedidos]:::hit & SN[Entradas]:::hit & SG[Pagos]:::hit
      SP & SN & SG --- BUS[Bus Kafka a RabbitMQ]:::hit
    end
    subgraph S2["Escenario 2: nueva pasarela (SUMP)"]
      UI[Interfaz web]:::hit
      MON[Monolito Java]:::hit
      NODE[Microservicio Node]:::hit
      ORA[Oracle]:::hit
      VPN[VPN / red]:::maybe
      REP[Reportes contables]:::maybe
    end
    classDef hit fill:#f8cecc,stroke:#cc0000,stroke-width:2px
    classDef maybe fill:#ffe6cc,stroke:#d79b00
    classDef ok fill:#d5e8d4,stroke:#2d7600
""",
}


if __name__ == "__main__":
    for fn in (d01, d02, d03, d04, d05, d06, d07, d08, d09, d10):
        fn()
    for name, src in MMD.items():
        (OUT / f"{name}.mmd").write_text(src, encoding="utf-8")
    print("Generados:", len(list(OUT.glob("*.drawio"))), ".drawio y", len(list(OUT.glob("*.mmd"))), ".mmd")
