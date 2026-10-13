"""Renderiza cada diagramas/*.drawio a PNG usando el visor oficial de diagrams.net en Chromium (Playwright).

Uso:  python diagramas/render_png.py
Requiere conexión a internet (carga viewer.diagrams.net) y: pip install playwright && python -m playwright install chromium
"""
import html
import json
import sys
from pathlib import Path

from playwright.sync_api import sync_playwright

DIR = Path(__file__).resolve().parent
VIEWER = "https://viewer.diagrams.net/js/viewer-static.min.js"

PAGE = """<!doctype html><html><head><meta charset="utf-8">
<style>body{{margin:0;background:#fff}} #wrap{{display:inline-block;padding:16px;background:#fff}}</style></head>
<body><div id="wrap"><div class="mxgraph" style="min-width:200px;min-height:200px" data-mxgraph="{cfg}"></div></div>
<script src="{viewer}"></script></body></html>"""


def main(names):
    files = sorted(DIR.glob("*.drawio"))
    if names:
        files = [f for f in files if any(n in f.stem for n in names)]
    with sync_playwright() as p:
        browser = p.chromium.launch()
        page = browser.new_page(device_scale_factor=2, viewport={"width": 1800, "height": 1200})
        for f in files:
            cfg = json.dumps({"xml": f.read_text(encoding="utf-8"), "toolbar": "", "nav": False,
                              "lightbox": False, "resize": False, "border": 0})
            page.set_content(PAGE.format(cfg=html.escape(cfg, quote=True), viewer=VIEWER))
            page.wait_for_selector("#wrap svg", timeout=60000)
            page.wait_for_timeout(800)
            out = f.with_suffix(".png")
            page.locator("#wrap").screenshot(path=str(out))
            print("PNG:", out.name)
        browser.close()


if __name__ == "__main__":
    main(sys.argv[1:])
