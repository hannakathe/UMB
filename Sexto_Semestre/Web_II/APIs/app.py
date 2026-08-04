import re
from flask import Flask, jsonify, request

app = Flask(__name__)

# ---------------------------------------------------------------------------
# Ejercicio 1 — GET /productos con paginacion
# ---------------------------------------------------------------------------
productos_db = [
    {"id": i, "nombre": f"Producto {i}", "precio": round(i * 3.5, 2)}
    for i in range(1, 26)
]


@app.route("/productos", methods=["GET"])
def listar_productos():
    try:
        page = int(request.args.get("page", 1))
        limit = int(request.args.get("limit", 10))
    except ValueError:
        return jsonify({"error": "page y limit deben ser numeros enteros"}), 400

    if page < 1 or limit < 1:
        return jsonify({"error": "page y limit deben ser mayores a 0"}), 400

    inicio = (page - 1) * limit
    fin = inicio + limit
    items = productos_db[inicio:fin]

    return jsonify({
        "total": len(productos_db),
        "page": page,
        "limit": limit,
        "items": items,
    })


# ---------------------------------------------------------------------------
# Ejercicio 2 — Manejo diferenciado de errores 400 y 404
# ---------------------------------------------------------------------------
usuarios_db = {}
contador_usuario_id = 1
EMAIL_REGEX = re.compile(r"^[^@\s]+@[^@\s]+\.[^@\s]+$")


@app.route("/usuarios", methods=["POST"])
def crear_usuario():
    global contador_usuario_id
    datos = request.get_json(silent=True) or {}
    email = datos.get("email", "")
    nombre = datos.get("nombre", "")

    if not EMAIL_REGEX.match(email):
        return jsonify({"error": "Email invalido"}), 400

    usuario = {"id": contador_usuario_id, "nombre": nombre, "email": email}
    usuarios_db[contador_usuario_id] = usuario
    contador_usuario_id += 1
    return jsonify(usuario), 201


@app.route("/usuarios/<int:usuario_id>", methods=["GET"])
def obtener_usuario(usuario_id):
    usuario = usuarios_db.get(usuario_id)
    if not usuario:
        return jsonify({"error": "Usuario no encontrado"}), 404
    return jsonify(usuario)


if __name__ == "__main__":
    cliente = app.test_client()

    print("== Ejercicio 1: paginacion de /productos ==")
    r = cliente.get("/productos?page=1&limit=5")
    print(r.status_code, r.get_json())

    r = cliente.get("/productos?page=2&limit=5")
    print(r.status_code, r.get_json())

    r = cliente.get("/productos?page=7&limit=5")  # pagina fuera de rango -> items vacios
    print(r.status_code, r.get_json())

    r = cliente.get("/productos?page=0&limit=5")  # page invalido -> 400
    print(r.status_code, r.get_json())

    r = cliente.get("/productos?page=abc&limit=5")  # no numerico -> 400
    print(r.status_code, r.get_json())

    print("\n== Ejercicio 2: validacion de email y 404 ==")
    r = cliente.post("/usuarios", json={"nombre": "Ana", "email": "ana@correo.com"})
    print(r.status_code, r.get_json())

    r = cliente.post("/usuarios", json={"nombre": "Luis", "email": "correo-invalido"})
    print(r.status_code, r.get_json())

    r = cliente.get("/usuarios/1")
    print(r.status_code, r.get_json())

    r = cliente.get("/usuarios/99")
    print(r.status_code, r.get_json())
