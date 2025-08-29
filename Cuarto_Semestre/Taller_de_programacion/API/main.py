from fastapi import FastAPI, HTTPException, status
import sqlite3
from models.producto import Producto

app = FastAPI()


@app.get("/")
def inicio():
    return {"mensaje": "Hi Class!"}



#Add Function
@app.get("/create-db/")
def create_db():
    #funcion para crear la base de datos en sqlite con campos id, nombre, precio
    conn = sqlite3.connect('miwebsite.db')
    cursor = conn.cursor()
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS productos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre TEXT NOT NULL,
            precio REAL NOT NULL
        )
    ''')
    conn.commit()
    conn.close()
    return {"message": "Database created successfully"}, status.HTTP_201_CREATED



@app.post("/producto/")
def crear_producto(producto: Producto):
    conn = sqlite3.connect('miwebsite.db')
    cursor = conn.cursor()
    cursor.execute('''
        INSERT INTO productos (nombre, precio) VALUES (?, ?)
    ''', (producto.name, producto.price))
    conn.commit()
    conn.close()
    return {
        "message": "Producto creado correctamente",
        "data": producto.dict()
    }

@app.get("/productos/")
def listar_productos():
    conn = sqlite3.connect('miwebsite.db')
    cursor = conn.cursor()
    cursor.execute('SELECT id, nombre, precio FROM productos')
    productos = cursor.fetchall()
    conn.close()
    return {
        "message": "Lista de productos",
        "data": [{"id": row[0], "nombre": row[1], "precio": row[2]} for row in productos]
    }