
from flask import Flask, jsonify, request

# Demo: contrato backend que un frontend (React/Angular/Vue)
# consumiría con fetch/axios.

# Se simula el cliente con un helper Python que imita una petición
# fetch/axios.

app = Flask(__name__)

tareas_db = [
    {
        "id": 1,
        "titulo": "Diseñar wireframes",
        "completada": False
    }
]


@app.route("/api/tareas", methods=["GET"])
def listar_tareas():
    return jsonify(tareas_db)


@app.route("/api/tareas/<int:tarea_id>", methods=["PATCH"])
def actualizar_tarea(tarea_id):
    datos = request.get_json()

    for tarea in tareas_db:
        if tarea["id"] == tarea_id:
            tarea.update(datos)
            return jsonify(tarea)

    return jsonify({"error": "Tarea no encontrada"}), 404


def cliente_frontend_simulado(cliente, metodo, url, **kwargs):
    """
    Simula cómo React/Vue manejaría la respuesta con fetch/axios:
    revisa el status antes de usar los datos.
    """
    respuesta = getattr(cliente, metodo)(url, **kwargs)

    if respuesta.status_code >= 400:
        print(
            f"[Frontend] Error {respuesta.status_code}:",
            respuesta.get_json()
        )
        return None

    return respuesta.get_json()


if __name__ == "__main__":
    cliente = app.test_client()

    print(
        "Tareas:",
        cliente_frontend_simulado(
            cliente,
            "get",
            "/api/tareas"
        )
    )

    print(
        "Actualizada:",
        cliente_frontend_simulado(
            cliente,
            "patch",
            "/api/tareas/1",
            json={"completada": True}
        )
    )

    print(
        "Error simulado:",
        cliente_frontend_simulado(
            cliente,
            "patch",
            "/api/tareas/99",
            json={"completada": True}
        )
    )

