from flask import Flask, jsonify, request, g
from flask_cors import CORS
import sqlite3
import os

DB_PATH = os.path.join(os.path.dirname(__file__), '..', 'database', 'Usuarios_UMB.db')
DB_PATH = os.path.abspath(DB_PATH)

app = Flask(__name__)
CORS(app)

def get_db():
    db = getattr(g, '_database', None)
    if db is None:
        db = g._database = sqlite3.connect(DB_PATH)
        db.row_factory = sqlite3.Row
    return db

@app.teardown_appcontext
def close_connection(exception):
    db = getattr(g, '_database', None)
    if db is not None:
        db.close()

# --- Clientes CRUD ---
@app.route('/api/clientes', methods=['GET'])
def listar_clientes():
    db = get_db()
    cur = db.execute("SELECT c.*, ci.nombre_ciudad, p.nombre_pais FROM Cliente c LEFT JOIN Ciudad ci ON c.cod_ciudad=ci.cod_ciudad LEFT JOIN Pais p ON c.cod_pais=p.cod_pais")
    rows = cur.fetchall()
    clientes = [dict(row) for row in rows]
    return jsonify(clientes)

@app.route('/api/clientes', methods=['POST'])
def crear_cliente():
    data = request.json
    sql = "INSERT INTO Cliente(tipoID, nroID, nombres, correo, direccion, celular, cod_ciudad, cod_pais) VALUES(?,?,?,?,?,?,?,?)"
    db = get_db()
    db.execute(sql, (data.get('tipoID'), data['nroID'], data['nombres'], data.get('correo'), data.get('direccion'), data.get('celular'), data.get('cod_ciudad'), data.get('cod_pais')))
    db.commit()
    return jsonify({'ok': True}), 201

@app.route('/api/clientes/<nroID>', methods=['PUT'])
def actualizar_cliente(nroID):
    data = request.json
    sql = "UPDATE Cliente SET tipoID=?, nombres=?, correo=?, direccion=?, celular=?, cod_ciudad=?, cod_pais=? WHERE nroID=?"
    db = get_db()
    db.execute(sql, (data.get('tipoID'), data.get('nombres'), data.get('correo'), data.get('direccion'), data.get('celular'), data.get('cod_ciudad'), data.get('cod_pais'), nroID))
    db.commit()
    return jsonify({'ok': True})

@app.route('/api/clientes/<nroID>', methods=['DELETE'])
def eliminar_cliente(nroID):
    db = get_db()
    db.execute("DELETE FROM Cliente WHERE nroID=?", (nroID,))
    db.commit()
    return jsonify({'ok': True})

# --- Paises y Ciudades ---
@app.route('/api/paises', methods=['GET'])
def listar_paises():
    db = get_db()
    cur = db.execute("SELECT * FROM Pais")
    rows = cur.fetchall()
    return jsonify([dict(r) for r in rows])

@app.route('/api/ciudades/<int:cod_pais>', methods=['GET'])
def listar_ciudades_por_pais(cod_pais):
    db = get_db()
    cur = db.execute("SELECT * FROM Ciudad WHERE cod_pais=?", (cod_pais,))
    rows = cur.fetchall()
    return jsonify([dict(r) for r in rows])

if __name__ == '__main__':
    print("Usando DB:", DB_PATH)
    app.run(host='0.0.0.0', port=5000, debug=True)
