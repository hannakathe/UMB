"""
EJERCICIO 2 - Token CSRF por sesion
Ingenieria Web II (UMB) - Sesion 6

Requisitos:
  1. Generar un token CSRF unico por sesion.
  2. Validar el token al procesar un formulario.
  3. Rechazar la solicitud si el token no coincide.

CSRF (Cross-Site Request Forgery): un sitio malicioso hace que el navegador
de la victima envie una peticion a nuestra aplicacion aprovechando su
cookie de sesion. El token CSRF lo frena porque el atacante no puede leer
el token secreto asociado a la sesion de la victima.

Ejecutar:  python ejercicio2_csrf.py
"""

import hmac
import secrets


class CSRFError(PermissionError):
    """Se lanza cuando el token CSRF falta o no coincide con el de la sesion."""


# "Almacen" de sesiones en memoria: id_sesion -> {"csrf_token": "..."}
_SESIONES: dict[str, dict] = {}


def crear_sesion() -> str:
    """Crea una sesion nueva y devuelve su identificador."""
    id_sesion = secrets.token_urlsafe(16)
    _SESIONES[id_sesion] = {}
    return id_sesion


def generar_token(id_sesion: str) -> str:
    """
    Genera (una sola vez) un token CSRF unico y lo guarda en la sesion.
    Si la sesion ya tiene token, devuelve el mismo.
    """
    if id_sesion not in _SESIONES:
        raise KeyError("Sesion inexistente.")
    sesion = _SESIONES[id_sesion]
    if "csrf_token" not in sesion:
        sesion["csrf_token"] = secrets.token_urlsafe(32)
    return sesion["csrf_token"]


def validar_token(id_sesion: str, token_recibido: str | None) -> bool:
    """
    Compara el token del formulario con el de la sesion en tiempo constante.
    Devuelve True solo si ambos existen y son iguales.
    """
    sesion = _SESIONES.get(id_sesion)
    if not sesion or "csrf_token" not in sesion:
        return False
    if not token_recibido:
        return False
    return hmac.compare_digest(str(sesion["csrf_token"]), str(token_recibido))


def procesar_formulario(id_sesion: str, datos_formulario: dict) -> dict:
    """
    Procesa un formulario SOLO si el campo 'csrf_token' es valido para la sesion.
    Si no coincide, lanza CSRFError y no se ejecuta ninguna accion.
    """
    token_recibido = datos_formulario.get("csrf_token")
    if not validar_token(id_sesion, token_recibido):
        raise CSRFError("Solicitud rechazada: token CSRF invalido o ausente.")

    # A partir de aqui la peticion es legitima.
    payload = {k: v for k, v in datos_formulario.items() if k != "csrf_token"}
    return {"estado": "procesado", "datos": payload}


# --------------------------------------------------------------------------- #
#  Pruebas
# --------------------------------------------------------------------------- #
def _pruebas() -> None:
    # 1. Token unico por sesion
    s1 = crear_sesion()
    s2 = crear_sesion()
    t1 = generar_token(s1)
    t2 = generar_token(s2)
    assert t1 != t2, "Dos sesiones no deben compartir token"
    assert generar_token(s1) == t1, "El token debe ser estable dentro de la sesion"
    print("[OK] Token unico por sesion:")
    print("     sesion 1 ->", t1)
    print("     sesion 2 ->", t2)

    # 2. Formulario con token correcto -> se procesa
    ok = procesar_formulario(s1, {"csrf_token": t1, "comentario": "Hola"})
    assert ok["estado"] == "procesado"
    print("[OK] Formulario con token valido:", ok)

    # 3. Token equivocado -> se rechaza
    try:
        procesar_formulario(s1, {"csrf_token": t2, "comentario": "ataque"})
    except CSRFError as e:
        print("[OK] Rechazado token de otra sesion:", e)
    else:
        raise AssertionError("Debio rechazar el token de otra sesion")

    # 4. Sin token -> se rechaza
    try:
        procesar_formulario(s1, {"comentario": "ataque"})
    except CSRFError as e:
        print("[OK] Rechazado formulario sin token:", e)
    else:
        raise AssertionError("Debio rechazar el formulario sin token")

    # 5. Token manipulado -> se rechaza
    assert validar_token(s1, t1 + "x") is False
    print("[OK] Rechazado token manipulado.")

    print("\nOK: todas las pruebas del Ejercicio 2 pasaron.")


if __name__ == "__main__":
    _pruebas()
