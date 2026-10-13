"""Renderiza los wireframes HTML a PNG (escritorio y móvil). Uso: python wireframes/render_wireframes.py"""
from pathlib import Path
from playwright.sync_api import sync_playwright

DIR = Path(__file__).resolve().parent
VISTAS = {"escritorio": {"width": 1366, "height": 800}, "movil": {"width": 390, "height": 844}}

with sync_playwright() as p:
    b = p.chromium.launch()
    for html in ("eventflow_malo", "eventflow_corregido"):
        for vista, vp in VISTAS.items():
            pg = b.new_page(viewport=vp, device_scale_factor=2, is_mobile=(vista == "movil"))
            pg.goto((DIR / f"{html}.html").as_uri())
            pg.wait_for_timeout(400)
            out = DIR / f"{html}_{vista}.png"
            pg.screenshot(path=str(out), full_page=True, animations="disabled")
            print("PNG:", out.name)
            pg.close()
    b.close()
