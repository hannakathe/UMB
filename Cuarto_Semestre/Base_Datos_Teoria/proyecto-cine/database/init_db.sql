-- init_db_mysql.sql - Sistema Cine (MySQL)
CREATE DATABASE IF NOT EXISTS cine_db;
USE cine_db;

-- Clientes
CREATE TABLE IF NOT EXISTS clientes (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  documento VARCHAR(50) UNIQUE NOT NULL,
  telefono VARCHAR(20)
) ENGINE=InnoDB;

-- Películas
CREATE TABLE IF NOT EXISTS peliculas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(150) NOT NULL,
  genero VARCHAR(50),
  valor_boleta DECIMAL(10,2) DEFAULT 0
) ENGINE=InnoDB;

-- Salas
CREATE TABLE IF NOT EXISTS salas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  tipo_sala VARCHAR(50)
) ENGINE=InnoDB;

-- Funciones
CREATE TABLE IF NOT EXISTS funciones (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sala_id INT,
  pelicula_id INT,
  fecha_hora DATETIME,
  FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE SET NULL ON UPDATE CASCADE,
  FOREIGN KEY (pelicula_id) REFERENCES peliculas(id) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Asientos
CREATE TABLE IF NOT EXISTS asientos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  sala_id INT,
  numero_silla VARCHAR(10),
  FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB;

-- Entradas
CREATE TABLE IF NOT EXISTS entradas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  asiento_id INT,
  funcion_id INT,
  cliente_id INT,
  valor DECIMAL(10,2),
  tipo_pago VARCHAR(50),
  fecha_compra DATE,
  FOREIGN KEY (asiento_id) REFERENCES asientos(id),
  FOREIGN KEY (funcion_id) REFERENCES funciones(id),
  FOREIGN KEY (cliente_id) REFERENCES clientes(id)
) ENGINE=InnoDB;

-- Facturas
CREATE TABLE IF NOT EXISTS facturas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cliente_id INT,
  empresa_nombre VARCHAR(100),
  empresa_documento VARCHAR(50),
  contacto VARCHAR(100),
  fecha DATE,
  tipo_pago VARCHAR(50),
  valor_total DECIMAL(10,2),
  FOREIGN KEY (cliente_id) REFERENCES clientes(id)
) ENGINE=InnoDB;

-- Detalle Factura
CREATE TABLE IF NOT EXISTS detalle_factura (
  id INT AUTO_INCREMENT PRIMARY KEY,
  factura_id INT,
  entrada_id INT,
  cantidad INT,
  valor_unitario DECIMAL(10,2),
  valor_total DECIMAL(10,2),
  FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
  FOREIGN KEY (entrada_id) REFERENCES entradas(id)
) ENGINE=InnoDB;

-- Datos de ejemplo
INSERT INTO clientes (nombre, documento, telefono) VALUES
  ('Hanna Abril','100200300','3115550000'),
  ('Juan Perez','200300400','3101234567');

INSERT INTO peliculas (titulo, genero, valor_boleta) VALUES
  ('Avengers','Acción',2500.00),
  ('La La Land','Musical',1800.00),
  ('Interstellar','Ciencia ficción',3000.00);

INSERT INTO salas (tipo_sala) VALUES ('2D'), ('3D'), ('VIP');

INSERT INTO funciones (sala_id, pelicula_id, fecha_hora) VALUES
  (1,1,'2025-10-01 15:00:00'),
  (2,2,'2025-10-01 17:30:00'),
  (3,3,'2025-10-01 20:00:00');

INSERT INTO asientos (sala_id, numero_silla) VALUES
 (1,'A1'),(1,'A2'),(1,'A3'),
 (2,'B1'),(2,'B2'),(2,'B3');

INSERT INTO entradas (asiento_id, funcion_id, cliente_id, valor, tipo_pago, fecha_compra) VALUES
 (1,1,1,2500.00,'Tarjeta','2025-09-28'),
 (2,1,2,2500.00,'Efectivo','2025-09-28');
