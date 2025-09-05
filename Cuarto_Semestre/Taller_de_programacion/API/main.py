from fastapi import FastAPI, HTTPException, status, Form
from fastapi.responses import JSONResponse
import sqlite3
from models.producto import Producto
from openai import OpenAI


#API KEY: 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

app = FastAPI()

client = OpenAI(
  base_url="https://openrouter.ai/api/v1",
  api_key="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",
)

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

@app.get("/producto/")
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
    
    
#Segundo endpoint con openai
@app.post("/significado-nombre")
async def obtener_significado(nombre: str = Form(...)):
    try:
        respuesta = client.chat.completions.create(
            extra_headers={
                "HTTP-Referer": "", # Optional. Site URL for rankings on openrouter.ai.
                "X-Title": "", # Optional. Site title for rankings on openrouter.ai.
            },
            model="gpt-oss-20b:free",
            messages=[
                {"role": "system", "content": "Eres un experto en etimología de nombres."},
                {"role": "user", "content": f"¿Cuál es el significado del nombre {nombre}?"}
            ]
        )

        significado = respuesta.choices[0].message.content.strip()
        return JSONResponse(content={
            "nombre": nombre,
            "significado": significado
        })
    except Exception as e:
        return JSONResponse(content={"error": str(e)}, status_code=500)