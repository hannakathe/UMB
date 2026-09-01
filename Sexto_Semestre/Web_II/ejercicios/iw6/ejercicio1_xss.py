"""
EJERCICIO 1 - Sanitizacion contra XSS almacenado
Ingenieria Web II (UMB) - Sesion 6

Requisitos:
  1. Detectar etiquetas <script> dentro de un comentario.
  2. Rechazar comentarios con contenido malicioso.
  3. Aceptar comentarios validos SIN alterarlos.

El XSS almacenado ocurre cuando un comentario malicioso se guarda en la
base de datos y luego se sirve a otros usuarios, ejecutandose en su
navegador. La defensa mas robusta es escapar SIEMPRE al renderizar, pero
aqui ademas rechazamos el contenido peligroso antes de persistirlo.

Ejecutar:  python ejercicio1_xss.py
"""

import re


class ComentarioMaliciosoError(ValueError):
    """Se lanza cuando un comentario contiene un patron de XSS."""


# Patrones tipicos de inyeccion de scripts (todos sin distincion de mayusculas)
PATRONES_PELIGROSOS = [
    re.compile(r"<\s*script", re.IGNORECASE),          # <script ...>
    re.compile(r"<\s*/\s*script\s*>", re.IGNORECASE),   # </script>
    re.compile(r"<\s*iframe", re.IGNORECASE),           # <iframe ...>
    re.compile(r"<\s*img[^>]*on\w+\s*=", re.IGNORECASE),  # <img onerror=...>
    re.compile(r"on\w+\s*=\s*['\"]", re.IGNORECASE),    # onclick=, onload=, ...
    re.compile(r"javascript\s*:", re.IGNORECASE),        # href="javascript:..."
    re.compile(r"<\s*svg[^>]*on\w+\s*=", re.IGNORECASE),  # <svg onload=...>
]


def contiene_script(comentario: str) -> bool:
    """Devuelve True si el comentario incluye una etiqueta <script> (abierta o cerrada)."""
    return bool(
        re.search(r"<\s*/?\s*script", comentario, re.IGNORECASE)
    )


def detectar_amenaza(comentario: str) -> str | None:
    """Devuelve el patron malicioso encontrado, o None si el comentario es seguro."""
    for patron in PATRONES_PELIGROSOS:
        coincidencia = patron.search(comentario)
        if coincidencia:
            return coincidencia.group(0)
    return None


def validar_comentario(comentario: str) -> str:
    """
    Valida un comentario destinado a almacenarse.

    - Si contiene contenido malicioso -> lanza ComentarioMaliciosoError.
    - Si es valido -> lo devuelve EXACTAMENTE igual (sin alterar).
    """
    if not isinstance(comentario, str):
        raise TypeError("El comentario debe ser texto.")

    amenaza = detectar_amenaza(comentario)
    if amenaza is not None:
        raise ComentarioMaliciosoError(
            f"Comentario rechazado: se detecto contenido peligroso -> {amenaza!r}"
        )
    return comentario


def guardar_comentario(almacen: list[str], comentario: str) -> str:
    """Valida y, si pasa, agrega el comentario intacto al 'almacen' (lista en memoria)."""
    limpio = validar_comentario(comentario)
    almacen.append(limpio)
    return limpio


# --------------------------------------------------------------------------- #
#  Pruebas
# --------------------------------------------------------------------------- #
def _pruebas() -> None:
    maliciosos = [
        "<script>alert('XSS')</script>",
        "Hola <SCRIPT src='http://malo.io/x.js'></SCRIPT>",
        "<img src=x onerror=alert(1)>",
        "<a href=\"javascript:alert(1)\">click</a>",
        "<iframe src='http://malo.io'></iframe>",
        "texto <  script >evil</ script >",
    ]
    validos = [
        "Muy buen articulo, gracias por compartir.",
        "El precio 3 < 5 y 10 > 2, pero sin etiquetas.",
        "Codigo: usar parametros ? en las consultas SQL.",
        "Me gusto :) 100% recomendado",
        "",
    ]

    almacen: list[str] = []

    for c in maliciosos:
        try:
            validar_comentario(c)
        except ComentarioMaliciosoError as e:
            print("[RECHAZADO] ", repr(c))
            print("            ", e)
        else:
            raise AssertionError(f"Deberia haberse rechazado: {c!r}")

    for c in validos:
        resultado = guardar_comentario(almacen, c)
        assert resultado == c, "El comentario valido fue alterado"
        print("[ACEPTADO]  ", repr(c), "-> guardado sin cambios")

    assert contiene_script("<script>x</script>") is True
    assert contiene_script("hola mundo") is False
    assert almacen == validos, "El almacen no conserva los comentarios intactos"
    print("\nOK: todas las pruebas del Ejercicio 1 pasaron.")


if __name__ == "__main__":
    _pruebas()
