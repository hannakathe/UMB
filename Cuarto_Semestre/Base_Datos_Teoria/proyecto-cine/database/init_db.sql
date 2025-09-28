-- init_db.sql (SQLite)

PRAGMA foreign_keys = ON;

-- Clientes
CREATE TABLE IF NOT EXISTS clientes (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  nombre TEXT NOT NULL,
  documento TEXT UNIQUE NOT NULL,
  telefono TEXT
);

-- Películas
CREATE TABLE IF NOT EXISTS peliculas (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  titulo TEXT NOT NULL,
  genero TEXT,
  valor REAL DEFAULT 0
);

-- Salas
CREATE TABLE IF NOT EXISTS salas (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  tipo_sala TEXT
);

-- Funciones
CREATE TABLE IF NOT EXISTS funciones (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  sala_id INTEGER,
  pelicula_id INTEGER,
  fecha_hora TEXT,
  FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE SET NULL,
  FOREIGN KEY (pelicula_id) REFERENCES peliculas(id) ON DELETE SET NULL
);

-- Asientos
CREATE TABLE IF NOT EXISTS asientos (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  sala_id INTEGER,
  numero_silla TEXT,
  FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE CASCADE
);

-- Entradas
CREATE TABLE IF NOT EXISTS entradas (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  asiento_id INTEGER,
  funcion_id INTEGER,
  cliente_id INTEGER,
  valor REAL,
  FOREIGN KEY (asiento_id) REFERENCES asientos(id),
  FOREIGN KEY (funcion_id) REFERENCES funciones(id),
  FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Facturas
CREATE TABLE IF NOT EXISTS facturas (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  cliente_id INTEGER,
  empresa_nombre TEXT,
  empresa_documento TEXT,
  contacto TEXT,
  fecha TEXT,
  valor_total REAL,
  FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

-- Detalle Factura
CREATE TABLE IF NOT EXISTS detalle_factura (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  factura_id INTEGER,
  entrada_id INTEGER,
  cantidad INTEGER,
  valor_unitario REAL,
  valor_total REAL,
  FOREIGN KEY (factura_id) REFERENCES facturas(id),
  FOREIGN KEY (entrada_id) REFERENCES entradas(id)
);

-- Datos de ejemplo
INSERT INTO clientes (nombre, documento, telefono) VALUES
('Hanna Abril','100200300','3115550000'),
('Juan Perez','200300400','3101234567');

INSERT INTO peliculas (titulo, genero, valor) VALUES
('Avengers','Acción',2500),
('La La Land','Musical',1800),
('Interstellar','Ciencia ficción',3000);
