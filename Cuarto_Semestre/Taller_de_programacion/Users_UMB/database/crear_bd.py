import sqlite3

# Conectar a la base de datos (se crea si no existe)
conn = sqlite3.connect("Usuarios_UMB.db")
c = conn.cursor()

# ---------------------------
# Crear tabla Pais
# ---------------------------
c.execute("""
CREATE TABLE IF NOT EXISTS Pais (
    cod_pais INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_pais TEXT NOT NULL
)
""")

# ---------------------------
# Crear tabla Ciudad
# ---------------------------
c.execute("""
CREATE TABLE IF NOT EXISTS Ciudad (
    cod_ciudad INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre_ciudad TEXT NOT NULL,
    cod_pais INTEGER,
    FOREIGN KEY(cod_pais) REFERENCES Pais(cod_pais)
)
""")

# ---------------------------
# Crear tabla Cliente
# ---------------------------
c.execute("""
CREATE TABLE IF NOT EXISTS Cliente (
    tipoID TEXT,
    nroID TEXT PRIMARY KEY,
    nombres TEXT NOT NULL,
    correo TEXT,
    direccion TEXT,
    celular TEXT,
    cod_ciudad INTEGER,
    cod_pais INTEGER,
    FOREIGN KEY(cod_ciudad) REFERENCES Ciudad(cod_ciudad),
    FOREIGN KEY(cod_pais) REFERENCES Pais(cod_pais)
)
""")

# ---------------------------
# Insertar datos en Pais
# ---------------------------
paises = [
    ("Colombia",),
    ("México",),
    ("Argentina",)
]
c.executemany("INSERT INTO Pais(nombre_pais) VALUES(?)", paises)

# ---------------------------
# Insertar datos en Ciudad
# ---------------------------
ciudades = [
    ("Bogotá", 1),
    ("Medellín", 1),
    ("Cali", 1),
    ("Ciudad de México", 2),
    ("Guadalajara", 2),
    ("Buenos Aires", 3),
    ("Córdoba", 3)
]
c.executemany("INSERT INTO Ciudad(nombre_ciudad, cod_pais) VALUES(?, ?)", ciudades)

# ---------------------------
# Insertar datos en Cliente
# ---------------------------
clientes = [
    ("CC", "1001", "Juan Pérez", "juanperez@mail.com", "Calle 1 #10-20", "3001234567", 1, 1),
    ("CC", "1002", "María Gómez", "mariagomez@mail.com", "Calle 2 #15-30", "3009876543", 2, 1),
    ("CC", "1003", "Carlos Ruiz", "carlosruiz@mail.com", "Carrera 5 #20-10", "3005554444", 3, 1),
    ("CC", "2001", "Ana Torres", "anatorres@mail.com", "Av. Reforma 100", "3111234567", 4, 2),
    ("CC", "2002", "Luis Fernández", "luisfernandez@mail.com", "Av. Juárez 250", "3119876543", 5, 2),
    ("CC", "3001", "Sofía Díaz", "sofiadiaz@mail.com", "Calle Florida 120", "3121234567", 6, 3),
    ("CC", "3002", "Diego Martínez", "diegomartinez@mail.com", "Calle Córdoba 50", "3129876543", 7, 3)
]
c.executemany("""
INSERT INTO Cliente(tipoID, nroID, nombres, correo, direccion, celular, cod_ciudad, cod_pais)
VALUES(?,?,?,?,?,?,?,?)
""", clientes)

# ---------------------------
# Guardar cambios y cerrar conexión
# ---------------------------
conn.commit()
conn.close()

print("Base de datos, tablas y registros creados correctamente.")
