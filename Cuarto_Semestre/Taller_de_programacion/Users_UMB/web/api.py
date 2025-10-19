from flask import Flask, jsonify, request
from flask_cors import CORS
import sqlite3

app = Flask(__name__)
CORS(app)

DB_PATH = "../database/Usuarios_UMB.db"

def conectar_db():
    conn = sqlite3.connect(DB_PATH)
    conn.row_factory = sqlite3.Row
    return conn

# -------------------- CLIENTES --------------------

@app.route("/api/clientes", methods=["GET"])
def listar_clientes():
    conn = conectar_db()
    cur = conn.cursor()
    cur.execute("""
        SELECT c.*, p.nombre_pais, ci.nombre_ciudad
        FROM Cliente c
        LEFT JOIN Pais p ON c.cod_pais = p.cod_pais
        LEFT JOIN Ciudad ci ON c.cod_ciudad = ci.cod_ciudad
    """)
    data = [dict(row) for row in cur.fetchall()]
    conn.close()
    return jsonify(data)

@app.route("/api/clientes", methods=["POST"])
def crear_cliente():
    data = request.get_json()
    conn = conectar_db()
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
    conn.close()
    return jsonify({"message": "Cliente creado"}), 201

@app.route("/api/clientes/<nroID>", methods=["PUT"])
def actualizar_cliente(nroID):
    data = request.get_json()
    conn = conectar_db()
    cur = conn.cursor()
    cur.execute("""
        UPDATE Cliente SET
        tipoID=?, nombres=?, correo=?, direccion=?, celular=?, cod_ciudad=?, cod_pais=?
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
    conn.close()
    return jsonify({"message": "Cliente actualizado"}), 200

@app.route("/api/clientes/<nroID>", methods=["DELETE"])
def eliminar_cliente(nroID):
    conn = conectar_db()
    cur = conn.cursor()
    cur.execute("DELETE FROM Cliente WHERE nroID=?", (nroID,))
    conn.commit()
    conn.close()
    return jsonify({"message": "Cliente eliminado"}), 200

# -------------------- PAISES Y CIUDADES --------------------

@app.route("/api/paises", methods=["GET"])
def listar_paises():
    conn = conectar_db()
    cur = conn.cursor()
    cur.execute("SELECT * FROM Pais")
    data = [dict(row) for row in cur.fetchall()]
    conn.close()
    return jsonify(data)

@app.route("/api/ciudades/<int:cod_pais>", methods=["GET"])
def listar_ciudades(cod_pais):
    conn = conectar_db()
    cur = conn.cursor()
    cur.execute("SELECT * FROM Ciudad WHERE cod_pais=?", (cod_pais,))
    data = [dict(row) for row in cur.fetchall()]
    conn.close()
    return jsonify(data)

# -------------------- MAIN --------------------

if __name__ == "__main__":
    app.run(debug=True)
