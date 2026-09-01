"""
TRABAJO PROYECTO - Sesion 6, Ingenieria Web II (UMB)
Servidor web que se puede validar desde el navegador e integra:

  * Ejercicio 1: sanitizacion contra XSS almacenado (ejercicio1_xss.py)
  * Ejercicio 2: token CSRF por sesion             (ejercicio2_csrf.py)

Ejecutar:
    python app.py
Luego abrir en el navegador:  http://127.0.0.1:5000
"""

import hmac
import secrets

from flask import (
    Flask,
    request,
    session,
    redirect,
    url_for,
    render_template,
)

from ejercicio1_xss import validar_comentario, ComentarioMaliciosoError

app = Flask(__name__)
# Clave para firmar la cookie de sesion (en produccion: variable de entorno).
app.secret_key = secrets.token_hex(32)

# Almacen de comentarios en memoria (se reinicia al reiniciar el servidor).
COMENTARIOS: list[str] = []


# --------------------------------------------------------------------------- #
#  CSRF por sesion (Ejercicio 2)
# --------------------------------------------------------------------------- #
def obtener_token_csrf() -> str:
    """Genera un token CSRF unico la primera vez y lo reutiliza en la sesion."""
    if "csrf_token" not in session:
        session["csrf_token"] = secrets.token_urlsafe(32)
    return session["csrf_token"]


def token_csrf_valido(token_recibido: str | None) -> bool:
    esperado = session.get("csrf_token")
    if not esperado or not token_recibido:
        return False
    return hmac.compare_digest(str(esperado), str(token_recibido))


# Hace que {{ csrf_token() }} este disponible en todas las plantillas.
app.jinja_env.globals["csrf_token"] = obtener_token_csrf


# --------------------------------------------------------------------------- #
#  Rutas
# --------------------------------------------------------------------------- #
@app.get("/")
def inicio():
    return "Hola, Ingenieria Web II"


@app.get("/comentarios")
def ver_comentarios():
    obtener_token_csrf()  # asegura que exista el token para el formulario
    return render_template(
        "comentarios.html",
        comentarios=COMENTARIOS,
        mensaje=request.args.get("mensaje"),
        error=request.args.get("error"),
    )


@app.post("/comentarios")
def crear_comentario():
    # 1. Defensa CSRF (Ejercicio 2)
    if not token_csrf_valido(request.form.get("csrf_token")):
        return redirect(url_for("ver_comentarios",
                                error="Token CSRF invalido o ausente. Solicitud rechazada."))

    # 2. Defensa XSS almacenado (Ejercicio 1)
    texto = request.form.get("comentario", "")
    try:
        limpio = validar_comentario(texto)
    except ComentarioMaliciosoError as e:
        return redirect(url_for("ver_comentarios", error=str(e)))

    COMENTARIOS.append(limpio)
    return redirect(url_for("ver_comentarios",
                            mensaje="Comentario publicado correctamente."))


@app.get("/salud")
def salud():
    return {"estado": "ok", "comentarios": len(COMENTARIOS)}


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)
