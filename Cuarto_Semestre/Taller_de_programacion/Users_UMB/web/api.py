from flask import Flask, jsonify, request
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

DB_PATH = "../database/Usuarios_UMB.db"

# -------------------- CONEXIÓN --------------------
def conectar_db():
    """Crea una conexión a la base de datos SQLite."""
    conn = sqlite3.connect(DB_PATH, check_same_thread=False)
    conn.row_factory = sqlite3.Row
    return conn


# -------------------- CLIENTES --------------------

@app.route("/api/clientes", methods=["GET"])
def listar_clientes():
    """Obtiene todos los clientes junto con su país y ciudad."""
    try:
        with conectar_db() as conn:
            cur = conn.cursor()
            cur.execute("""
                SELECT c.*, p.nombre_pais, ci.nombre_ciudad
                FROM Cliente c
                LEFT JOIN Pais p ON c.cod_pais = p.cod_pais
                LEFT JOIN Ciudad ci ON c.cod_ciudad = ci.cod_ciudad
            """)
            data = [dict(row) for row in cur.fetchall()]
        return jsonify(data)
    except sqlite3.Error as e:
        return jsonify({"error": str(e)}), 500


@app.route("/api/clientes", methods=["POST"])
def crear_cliente():
    """Crea un nuevo cliente en la base de datos."""
    data = request.get_json()
    try:
        with conectar_db() as conn:
            cur = conn.cursor()
            cur.execute("""
                INSERT INTO Cliente (tipoID, nroID, nombres, correo, direccion, celular, cod_ciudad, cod_pais)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """, (
                data.get("tipoID"),
                data.get("nroID"),
                data.get("nombres"),
                data.get("correo"),
                data.get("direccion"),
                data.get("celular"),
                data.get("cod_ciudad"),
                data.get("cod_pais")
            ))
            conn.commit()
        return jsonify({"message": "Cliente creado correctamente"}), 201
    except sqlite3.IntegrityError:
        return jsonify({"error": "El cliente con ese NroID ya existe"}), 400
    except sqlite3.Error as e:
        return jsonify({"error": str(e)}), 500


@app.route("/api/clientes/<nroID>", methods=["PUT"])
def actualizar_cliente(nroID):
    """Actualiza los datos de un cliente existente."""
    data = request.get_json()
    try:
        with conectar_db() as conn:
            cur = conn.cursor()
            cur.execute("""
                UPDATE Cliente SET
                    tipoID=?,
                    nombres=?,
                    correo=?,
                    direccion=?,
                    celular=?,
                    cod_ciudad=?,
                    cod_pais=?
                WHERE nroID=?
            """, (
                data.get("tipoID"),
                data.get("nombres"),
                data.get("correo"),
                data.get("direccion"),
                data.get("celular"),
                data.get("cod_ciudad"),
                data.get("cod_pais"),
                nroID
            ))
            conn.commit()

            if cur.rowcount == 0:
                return jsonify({"error": "Cliente no encontrado"}), 404

        return jsonify({"message": "Cliente actualizado correctamente"}), 200
    except sqlite3.Error as e:
        return jsonify({"error": str(e)}), 500


@app.route("/api/clientes/<nroID>", methods=["DELETE"])
def eliminar_cliente(nroID):
    """Elimina un cliente por su número de identificación."""
    try:
        with conectar_db() as conn:
            cur = conn.cursor()
            cur.execute("DELETE FROM Cliente WHERE nroID=?", (nroID,))
            conn.commit()

            if cur.rowcount == 0:
                return jsonify({"error": "Cliente no encontrado"}), 404

        return jsonify({"message": "Cliente eliminado correctamente"}), 200
    except sqlite3.Error as e:
        return jsonify({"error": str(e)}), 500


# -------------------- PAISES Y CIUDADES --------------------

@app.route("/api/paises", methods=["GET"])
def listar_paises():
    """Lista todos los países disponibles."""
    try:
        with conectar_db() as conn:
            cur = conn.cursor()
            cur.execute("SELECT * FROM Pais ORDER BY nombre_pais ASC")
            data = [dict(row) for row in cur.fetchall()]
        return jsonify(data)
    except sqlite3.Error as e:
        return jsonify({"error": str(e)}), 500


@app.route("/api/ciudades/<int:cod_pais>", methods=["GET"])
def listar_ciudades(cod_pais):
    """Lista las ciudades según el código del país."""
    try:
        with conectar_db() as conn:
            cur = conn.cursor()
            cur.execute("SELECT * FROM Ciudad WHERE cod_pais=? ORDER BY nombre_ciudad ASC", (cod_pais,))
            data = [dict(row) for row in cur.fetchall()]
        return jsonify(data)
    except sqlite3.Error as e:
        return jsonify({"error": str(e)}), 500


# -------------------- MAIN --------------------

if __name__ == "__main__":
    app.run(debug=True)
