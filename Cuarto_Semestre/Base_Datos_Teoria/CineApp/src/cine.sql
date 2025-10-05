-- cine.sql
CREATE DATABASE IF NOT EXISTS cine;
USE cine;

-- Clientes
CREATE TABLE IF NOT EXISTS clientes(
    documento INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(50)
);

-- Peliculas
CREATE TABLE IF NOT EXISTS peliculas(
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    idioma VARCHAR(20) NOT NULL DEFAULT 'Español'
);

-- Salas
CREATE TABLE IF NOT EXISTS salas(
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_sala VARCHAR(50) NOT NULL,
    capacidad INT NOT NULL
);

-- Funciones
CREATE TABLE IF NOT EXISTS funciones(
    id INT AUTO_INCREMENT PRIMARY KEY,
    pelicula_id INT NOT NULL,
    sala_id INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    FOREIGN KEY (pelicula_id) REFERENCES peliculas(id) ON DELETE CASCADE,
    FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE CASCADE
);

-- Asientos
CREATE TABLE IF NOT EXISTS asientos(
    id INT AUTO_INCREMENT PRIMARY KEY,
    sala_id INT NOT NULL,
    numero_silla VARCHAR(10) NOT NULL,
    disponible BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (sala_id) REFERENCES salas(id) ON DELETE CASCADE
);

-- Entradas
CREATE TABLE IF NOT EXISTS entradas(
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_documento INT NOT NULL,
    funcion_id INT NOT NULL,
    asiento_id INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    fecha_compra DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (funcion_id) REFERENCES funciones(id) ON DELETE CASCADE,
    FOREIGN KEY (asiento_id) REFERENCES asientos(id) ON DELETE CASCADE
);

-- Facturas
CREATE TABLE IF NOT EXISTS facturas(
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_documento INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(10,2) NOT NULL,
    datos_empresa VARCHAR(255)
);

-- Detalle factura
CREATE TABLE IF NOT EXISTS detalle_factura(
    id INT AUTO_INCREMENT PRIMARY KEY,
    factura_id INT NOT NULL,
    entrada_id INT NOT NULL,
    cantidad INT NOT NULL,
    valor_unitario DECIMAL(10,2) NOT NULL,
    valor_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (factura_id) REFERENCES facturas(id) ON DELETE CASCADE,
    FOREIGN KEY (entrada_id) REFERENCES entradas(id) ON DELETE CASCADE
);

-- =============================================
-- INSERCIÓN DE DATOS BÁSICOS (SIN CLIENTES NI VENTAS)
-- =============================================

-- Insertar películas con diferentes idiomas
INSERT INTO peliculas (titulo, genero, idioma) VALUES
('Avatar: El Camino del Agua', 'Ciencia Ficción', 'Español'),
('Avatar: The Way of Water', 'Ciencia Ficción', 'Inglés'),
('John Wick 4', 'Acción', 'Español'),
('John Wick: Chapter 4', 'Acción', 'Inglés'),
('Super Mario Bros: La Película', 'Animación', 'Español'),
('The Super Mario Bros Movie', 'Animación', 'Inglés'),
('Transformers: El Despertar de las Bestias', 'Ciencia Ficción', 'Español'),
('Transformers: Rise of the Beasts', 'Ciencia Ficción', 'Inglés'),
('Spider-Man: Across the Spider-Verse', 'Animación', 'Español'),
('Spider-Man: Across the Spider-Verse', 'Animación', 'Inglés');

-- Insertar salas (6 salas diferentes)
INSERT INTO salas (tipo_sala, capacidad) VALUES
('Sala 2D Estándar', 80),
('Sala 4D', 50),
('Sala 3D', 60),
('Sala VIP', 40),
('Sala Premium', 30),
('Sala IMAX', 100);

-- Insertar asientos para cada sala (TODOS DISPONIBLES - disponible = TRUE)
-- Sala 1: Estándar 2D (80 asientos - 8 filas x 10 columnas)
INSERT INTO asientos (sala_id, numero_silla, disponible) VALUES
(1, 'A1', true), (1, 'A2', true), (1, 'A3', true), (1, 'A4', true), (1, 'A5', true), (1, 'A6', true), (1, 'A7', true), (1, 'A8', true), (1, 'A9', true), (1, 'A10', true),
(1, 'B1', true), (1, 'B2', true), (1, 'B3', true), (1, 'B4', true), (1, 'B5', true), (1, 'B6', true), (1, 'B7', true), (1, 'B8', true), (1, 'B9', true), (1, 'B10', true),
(1, 'C1', true), (1, 'C2', true), (1, 'C3', true), (1, 'C4', true), (1, 'C5', true), (1, 'C6', true), (1, 'C7', true), (1, 'C8', true), (1, 'C9', true), (1, 'C10', true),
(1, 'D1', true), (1, 'D2', true), (1, 'D3', true), (1, 'D4', true), (1, 'D5', true), (1, 'D6', true), (1, 'D7', true), (1, 'D8', true), (1, 'D9', true), (1, 'D10', true),
(1, 'E1', true), (1, 'E2', true), (1, 'E3', true), (1, 'E4', true), (1, 'E5', true), (1, 'E6', true), (1, 'E7', true), (1, 'E8', true), (1, 'E9', true), (1, 'E10', true),
(1, 'F1', true), (1, 'F2', true), (1, 'F3', true), (1, 'F4', true), (1, 'F5', true), (1, 'F6', true), (1, 'F7', true), (1, 'F8', true), (1, 'F9', true), (1, 'F10', true),
(1, 'G1', true), (1, 'G2', true), (1, 'G3', true), (1, 'G4', true), (1, 'G5', true), (1, 'G6', true), (1, 'G7', true), (1, 'G8', true), (1, 'G9', true), (1, 'G10', true),
(1, 'H1', true), (1, 'H2', true), (1, 'H3', true), (1, 'H4', true), (1, 'H5', true), (1, 'H6', true), (1, 'H7', true), (1, 'H8', true), (1, 'H9', true), (1, 'H10', true);

-- Sala 2: 4D (50 asientos - 5 filas x 10 columnas)
INSERT INTO asientos (sala_id, numero_silla, disponible) VALUES
(2, 'A1', true), (2, 'A2', true), (2, 'A3', true), (2, 'A4', true), (2, 'A5', true), (2, 'A6', true), (2, 'A7', true), (2, 'A8', true), (2, 'A9', true), (2, 'A10', true),
(2, 'B1', true), (2, 'B2', true), (2, 'B3', true), (2, 'B4', true), (2, 'B5', true), (2, 'B6', true), (2, 'B7', true), (2, 'B8', true), (2, 'B9', true), (2, 'B10', true),
(2, 'C1', true), (2, 'C2', true), (2, 'C3', true), (2, 'C4', true), (2, 'C5', true), (2, 'C6', true), (2, 'C7', true), (2, 'C8', true), (2, 'C9', true), (2, 'C10', true),
(2, 'D1', true), (2, 'D2', true), (2, 'D3', true), (2, 'D4', true), (2, 'D5', true), (2, 'D6', true), (2, 'D7', true), (2, 'D8', true), (2, 'D9', true), (2, 'D10', true),
(2, 'E1', true), (2, 'E2', true), (2, 'E3', true), (2, 'E4', true), (2, 'E5', true), (2, 'E6', true), (2, 'E7', true), (2, 'E8', true), (2, 'E9', true), (2, 'E10', true);

-- Sala 3: 3D (60 asientos - 6 filas x 10 columnas)
INSERT INTO asientos (sala_id, numero_silla, disponible) VALUES
(3, 'A1', true), (3, 'A2', true), (3, 'A3', true), (3, 'A4', true), (3, 'A5', true), (3, 'A6', true), (3, 'A7', true), (3, 'A8', true), (3, 'A9', true), (3, 'A10', true),
(3, 'B1', true), (3, 'B2', true), (3, 'B3', true), (3, 'B4', true), (3, 'B5', true), (3, 'B6', true), (3, 'B7', true), (3, 'B8', true), (3, 'B9', true), (3, 'B10', true),
(3, 'C1', true), (3, 'C2', true), (3, 'C3', true), (3, 'C4', true), (3, 'C5', true), (3, 'C6', true), (3, 'C7', true), (3, 'C8', true), (3, 'C9', true), (3, 'C10', true),
(3, 'D1', true), (3, 'D2', true), (3, 'D3', true), (3, 'D4', true), (3, 'D5', true), (3, 'D6', true), (3, 'D7', true), (3, 'D8', true), (3, 'D9', true), (3, 'D10', true),
(3, 'E1', true), (3, 'E2', true), (3, 'E3', true), (3, 'E4', true), (3, 'E5', true), (3, 'E6', true), (3, 'E7', true), (3, 'E8', true), (3, 'E9', true), (3, 'E10', true),
(3, 'F1', true), (3, 'F2', true), (3, 'F3', true), (3, 'F4', true), (3, 'F5', true), (3, 'F6', true), (3, 'F7', true), (3, 'F8', true), (3, 'F9', true), (3, 'F10', true);

-- Sala 4: VIP (40 asientos - 4 filas x 10 columnas)
INSERT INTO asientos (sala_id, numero_silla, disponible) VALUES
(4, 'A1', true), (4, 'A2', true), (4, 'A3', true), (4, 'A4', true), (4, 'A5', true), (4, 'A6', true), (4, 'A7', true), (4, 'A8', true), (4, 'A9', true), (4, 'A10', true),
(4, 'B1', true), (4, 'B2', true), (4, 'B3', true), (4, 'B4', true), (4, 'B5', true), (4, 'B6', true), (4, 'B7', true), (4, 'B8', true), (4, 'B9', true), (4, 'B10', true),
(4, 'C1', true), (4, 'C2', true), (4, 'C3', true), (4, 'C4', true), (4, 'C5', true), (4, 'C6', true), (4, 'C7', true), (4, 'C8', true), (4, 'C9', true), (4, 'C10', true),
(4, 'D1', true), (4, 'D2', true), (4, 'D3', true), (4, 'D4', true), (4, 'D5', true), (4, 'D6', true), (4, 'D7', true), (4, 'D8', true), (4, 'D9', true), (4, 'D10', true);

-- Sala 5: Premium (30 asientos - 3 filas x 10 columnas)
INSERT INTO asientos (sala_id, numero_silla, disponible) VALUES
(5, 'A1', true), (5, 'A2', true), (5, 'A3', true), (5, 'A4', true), (5, 'A5', true), (5, 'A6', true), (5, 'A7', true), (5, 'A8', true), (5, 'A9', true), (5, 'A10', true),
(5, 'B1', true), (5, 'B2', true), (5, 'B3', true), (5, 'B4', true), (5, 'B5', true), (5, 'B6', true), (5, 'B7', true), (5, 'B8', true), (5, 'B9', true), (5, 'B10', true),
(5, 'C1', true), (5, 'C2', true), (5, 'C3', true), (5, 'C4', true), (5, 'C5', true), (5, 'C6', true), (5, 'C7', true), (5, 'C8', true), (5, 'C9', true), (5, 'C10', true);

-- Sala 6: IMAX (100 asientos - 10 filas x 10 columnas)
INSERT INTO asientos (sala_id, numero_silla, disponible) VALUES
(6, 'A1', true), (6, 'A2', true), (6, 'A3', true), (6, 'A4', true), (6, 'A5', true), (6, 'A6', true), (6, 'A7', true), (6, 'A8', true), (6, 'A9', true), (6, 'A10', true),
(6, 'B1', true), (6, 'B2', true), (6, 'B3', true), (6, 'B4', true), (6, 'B5', true), (6, 'B6', true), (6, 'B7', true), (6, 'B8', true), (6, 'B9', true), (6, 'B10', true),
(6, 'C1', true), (6, 'C2', true), (6, 'C3', true), (6, 'C4', true), (6, 'C5', true), (6, 'C6', true), (6, 'C7', true), (6, 'C8', true), (6, 'C9', true), (6, 'C10', true),
(6, 'D1', true), (6, 'D2', true), (6, 'D3', true), (6, 'D4', true), (6, 'D5', true), (6, 'D6', true), (6, 'D7', true), (6, 'D8', true), (6, 'D9', true), (6, 'D10', true),
(6, 'E1', true), (6, 'E2', true), (6, 'E3', true), (6, 'E4', true), (6, 'E5', true), (6, 'E6', true), (6, 'E7', true), (6, 'E8', true), (6, 'E9', true), (6, 'E10', true),
(6, 'F1', true), (6, 'F2', true), (6, 'F3', true), (6, 'F4', true), (6, 'F5', true), (6, 'F6', true), (6, 'F7', true), (6, 'F8', true), (6, 'F9', true), (6, 'F10', true),
(6, 'G1', true), (6, 'G2', true), (6, 'G3', true), (6, 'G4', true), (6, 'G5', true), (6, 'G6', true), (6, 'G7', true), (6, 'G8', true), (6, 'G9', true), (6, 'G10', true),
(6, 'H1', true), (6, 'H2', true), (6, 'H3', true), (6, 'H4', true), (6, 'H5', true), (6, 'H6', true), (6, 'H7', true), (6, 'H8', true), (6, 'H9', true), (6, 'H10', true),
(6, 'I1', true), (6, 'I2', true), (6, 'I3', true), (6, 'I4', true), (6, 'I5', true), (6, 'I6', true), (6, 'I7', true), (6, 'I8', true), (6, 'I9', true), (6, 'I10', true),
(6, 'J1', true), (6, 'J2', true), (6, 'J3', true), (6, 'J4', true), (6, 'J5', true), (6, 'J6', true), (6, 'J7', true), (6, 'J8', true), (6, 'J9', true), (6, 'J10', true);

-- Insertar funciones para octubre 2025 (desde el 15 de octubre)
INSERT INTO funciones (pelicula_id, sala_id, fecha_hora) VALUES
-- 15 de Octubre 2025 - Jueves
(1, 6, '2025-10-15 10:00:00'),  -- Avatar Español en IMAX
(2, 6, '2025-10-15 14:00:00'),  -- Avatar Inglés en IMAX
(3, 1, '2025-10-15 11:00:00'),  -- John Wick Español en 2D
(4, 1, '2025-10-15 15:00:00'),  -- John Wick Inglés en 2D
(5, 3, '2025-10-15 12:00:00'),  -- Mario Bros Español en 3D
(6, 3, '2025-10-15 16:00:00'),  -- Mario Bros Inglés en 3D
(7, 4, '2025-10-15 13:00:00'),  -- Transformers Español en VIP
(8, 4, '2025-10-15 17:00:00'),  -- Transformers Inglés en VIP
(9, 5, '2025-10-15 18:00:00'),  -- Spider-Man Español en Premium
(10, 5, '2025-10-15 20:00:00'), -- Spider-Man Inglés en Premium

-- 16 de Octubre 2025 - Viernes
(1, 2, '2025-10-16 10:00:00'),  -- Avatar Español en 4D
(2, 2, '2025-10-16 14:00:00'),  -- Avatar Inglés en 4D
(3, 3, '2025-10-16 11:00:00'),  -- John Wick Español en 3D
(4, 3, '2025-10-16 15:00:00'),  -- John Wick Inglés en 3D
(5, 4, '2025-10-16 12:00:00'),  -- Mario Bros Español en VIP
(6, 4, '2025-10-16 16:00:00'),  -- Mario Bros Inglés en VIP
(7, 5, '2025-10-16 13:00:00'),  -- Transformers Español en Premium
(8, 5, '2025-10-16 17:00:00'),  -- Transformers Inglés en Premium
(9, 6, '2025-10-16 18:00:00'),  -- Spider-Man Español en IMAX
(10, 6, '2025-10-16 20:00:00'), -- Spider-Man Inglés en IMAX

-- 17 de Octubre 2025 - Sábado
(1, 6, '2025-10-17 09:00:00'),  -- Avatar Español en IMAX
(2, 6, '2025-10-17 13:00:00'),  -- Avatar Inglés en IMAX
(3, 1, '2025-10-17 10:00:00'),  -- John Wick Español en 2D
(4, 1, '2025-10-17 14:00:00'),  -- John Wick Inglés en 2D
(5, 3, '2025-10-17 11:00:00'),  -- Mario Bros Español en 3D
(6, 3, '2025-10-17 15:00:00'),  -- Mario Bros Inglés en 3D
(1, 2, '2025-10-17 21:00:00'),  -- Avatar Español en 4D (noche)

-- 18 de Octubre 2025 - Domingo
(2, 6, '2025-10-18 10:00:00'),  -- Avatar Inglés en IMAX
(3, 1, '2025-10-18 11:00:00'),  -- John Wick Español en 2D
(4, 3, '2025-10-18 12:00:00'),  -- John Wick Inglés en 3D
(5, 4, '2025-10-18 13:00:00'),  -- Mario Bros Español en VIP
(6, 5, '2025-10-18 14:00:00'),  -- Mario Bros Inglés en Premium
(7, 2, '2025-10-18 15:00:00'),  -- Transformers Español en 4D
(8, 6, '2025-10-18 16:00:00'),  -- Transformers Inglés en IMAX
(9, 3, '2025-10-18 17:00:00'),  -- Spider-Man Español en 3D
(10, 4, '2025-10-18 18:00:00'), -- Spider-Man Inglés en VIP

-- 19 de Octubre 2025 - Lunes
(1, 1, '2025-10-19 10:00:00'),  -- Avatar Español en 2D
(2, 3, '2025-10-19 14:00:00'),  -- Avatar Inglés en 3D
(3, 4, '2025-10-19 11:00:00'),  -- John Wick Español en VIP
(4, 5, '2025-10-19 15:00:00'),  -- John Wick Inglés en Premium
(8, 4, '2025-10-19 17:00:00'),  -- Transformers Inglés en VIP
(9, 5, '2025-10-19 18:00:00'),  -- Spider-Man Español en Premium
(10, 6, '2025-10-19 20:00:00'), -- Spider-Man Inglés en IMAX

-- 20 de Octubre 2025 - Martes
(1, 4, '2025-10-20 10:00:00'),  -- Avatar Español en VIP
(2, 5, '2025-10-20 14:00:00'),  -- Avatar Inglés en Premium
(3, 6, '2025-10-20 11:00:00'),  -- John Wick Español en IMAX
(4, 1, '2025-10-20 15:00:00'),  -- John Wick Inglés en 2D
(5, 2, '2025-10-20 12:00:00'),  -- Mario Bros Español en 4D
(6, 3, '2025-10-20 16:00:00'),  -- Mario Bros Inglés en 3D
(7, 4, '2025-10-20 13:00:00'),  -- Transformers Español en VIP
(8, 5, '2025-10-20 17:00:00'),  -- Transformers Inglés en Premium
(9, 6, '2025-10-20 18:00:00'),  -- Spider-Man Español en IMAX
(10, 1, '2025-10-20 20:00:00'), -- Spider-Man Inglés en 2D

-- 21 de Octubre 2025 - Miércoles
(1, 3, '2025-10-21 10:00:00'),  -- Avatar Español en 3D
(2, 4, '2025-10-21 14:00:00'),  -- Avatar Inglés en VIP
(3, 5, '2025-10-21 11:00:00'),  -- John Wick Español en Premium
(4, 6, '2025-10-21 15:00:00'),  -- John Wick Inglés en IMAX
(5, 1, '2025-10-21 12:00:00'),  -- Mario Bros Español en 2D
(6, 2, '2025-10-21 16:00:00'),  -- Mario Bros Inglés en 4D
(8, 4, '2025-10-21 17:00:00'),  -- Transformers Inglés en VIP
(9, 5, '2025-10-21 18:00:00'),  -- Spider-Man Español en Premium
(10, 6, '2025-10-21 20:00:00'), -- Spider-Man Inglés en IMAX

-- 22 de Octubre 2025 - Jueves
(1, 6, '2025-10-22 10:00:00'),  -- Avatar Español en IMAX
(2, 1, '2025-10-22 14:00:00'),  -- Avatar Inglés en 2D
(3, 2, '2025-10-22 11:00:00'),  -- John Wick Español en 4D
(4, 3, '2025-10-22 15:00:00'),  -- John Wick Inglés en 3D
(5, 4, '2025-10-22 12:00:00'),  -- Mario Bros Español en VIP
(6, 5, '2025-10-22 16:00:00'),  -- Mario Bros Inglés en Premium
(9, 2, '2025-10-22 18:00:00'),  -- Spider-Man Español en 4D
(10, 3, '2025-10-22 20:00:00'), -- Spider-Man Inglés en 3D

-- 23 de Octubre 2025 - Viernes
(1, 4, '2025-10-23 10:00:00'),  -- Avatar Español en VIP
(2, 5, '2025-10-23 14:00:00'),  -- Avatar Inglés en Premium
(3, 6, '2025-10-23 11:00:00'),  -- John Wick Español en IMAX
(4, 1, '2025-10-23 15:00:00'),  -- John Wick Inglés en 2D
(5, 2, '2025-10-23 12:00:00'),  -- Mario Bros Español en 4D
(6, 3, '2025-10-23 16:00:00'),  -- Mario Bros Inglés en 3D
(7, 4, '2025-10-23 13:00:00'),  -- Transformers Español en VIP
(8, 5, '2025-10-23 17:00:00'),  -- Transformers Inglés en Premium
(9, 6, '2025-10-23 18:00:00'),  -- Spider-Man Español en IMAX
(10, 1, '2025-10-23 20:00:00'), -- Spider-Man Inglés en 2D

-- 24 de Octubre 2025 - Sábado
(1, 6, '2025-10-24 09:00:00'),  -- Avatar Español en IMAX
(2, 6, '2025-10-24 13:00:00'),  -- Avatar Inglés en IMAX
(3, 1, '2025-10-24 10:00:00'),  -- John Wick Español en 2D
(4, 1, '2025-10-24 14:00:00'),  -- John Wick Inglés en 2D
(5, 3, '2025-10-24 11:00:00'),  -- Mario Bros Español en 3D
(6, 3, '2025-10-24 15:00:00'),  -- Mario Bros Inglés en 3D
(7, 4, '2025-10-24 12:00:00'),  -- Transformers Español en VIP
(8, 4, '2025-10-24 16:00:00'),  -- Transformers Inglés en VIP
(9, 5, '2025-10-24 17:00:00'),  -- Spider-Man Español en Premium
(10, 5, '2025-10-24 19:00:00'), -- Spider-Man Inglés en Premium
(1, 2, '2025-10-24 21:00:00'),  -- Avatar Español en 4D (noche)

-- 26 de Octubre 2025 - Lunes
(1, 1, '2025-10-26 10:00:00'),  -- Avatar Español en 2D
(2, 3, '2025-10-26 14:00:00'),  -- Avatar Inglés en 3D
(3, 4, '2025-10-26 11:00:00'),  -- John Wick Español en VIP
(4, 5, '2025-10-26 15:00:00'),  -- John Wick Inglés en Premium
(5, 6, '2025-10-26 12:00:00'),  -- Mario Bros Español en IMAX
(6, 2, '2025-10-26 16:00:00'),  -- Mario Bros Inglés en 4D
(9, 5, '2025-10-26 18:00:00'),  -- Spider-Man Español en Premium
(10, 6, '2025-10-26 20:00:00'), -- Spider-Man Inglés en IMAX

-- 27 de Octubre 2025 - Martes
(1, 4, '2025-10-27 10:00:00'),  -- Avatar Español en VIP
(2, 5, '2025-10-27 14:00:00'),  -- Avatar Inglés en Premium
(3, 6, '2025-10-27 11:00:00'),  -- John Wick Español en IMAX
(4, 1, '2025-10-27 15:00:00'),  -- John Wick Inglés en 2D
(5, 2, '2025-10-27 12:00:00'),  -- Mario Bros Español en 4D
(6, 3, '2025-10-27 16:00:00'),  -- Mario Bros Inglés en 3D
(7, 4, '2025-10-27 13:00:00'),  -- Transformers Español en VIP
(8, 5, '2025-10-27 17:00:00'),  -- Transformers Inglés en Premium
(9, 6, '2025-10-27 18:00:00'),  -- Spider-Man Español en IMAX
(10, 1, '2025-10-27 20:00:00'), -- Spider-Man Inglés en 2D

-- 28 de Octubre 2025 - Miércoles
(1, 3, '2025-10-28 10:00:00'),  -- Avatar Español en 3D
(2, 4, '2025-10-28 14:00:00'),  -- Avatar Inglés en VIP
(3, 5, '2025-10-28 11:00:00'),  -- John Wick Español en Premium
(4, 6, '2025-10-28 15:00:00'),  -- John Wick Inglés en IMAX
(5, 1, '2025-10-28 12:00:00'),  -- Mario Bros Español en 2D
(10, 6, '2025-10-28 20:00:00'), -- Spider-Man Inglés en IMAX

-- 29 de Octubre 2025 - Jueves
(1, 6, '2025-10-29 10:00:00'),  -- Avatar Español en IMAX
(5, 4, '2025-10-29 12:00:00'),  -- Mario Bros Español en VIP
(6, 5, '2025-10-29 16:00:00'),  -- Mario Bros Inglés en Premium
(7, 6, '2025-10-29 13:00:00'),  -- Transformers Español en IMAX
(8, 1, '2025-10-29 17:00:00'),  -- Transformers Inglés en 2D
(9, 2, '2025-10-29 18:00:00'),  -- Spider-Man Español en 4D
(10, 3, '2025-10-29 20:00:00'), -- Spider-Man Inglés en 3D

-- 30 de Octubre 2025 - Viernes
(1, 4, '2025-10-30 10:00:00'),  -- Avatar Español en VIP
(2, 5, '2025-10-30 14:00:00'),  -- Avatar Inglés en Premium
(3, 6, '2025-10-30 11:00:00'),  -- John Wick Español en IMAX
(4, 1, '2025-10-30 15:00:00'),  -- John Wick Inglés en 2D
(5, 2, '2025-10-30 12:00:00'),  -- Mario Bros Español en 4D
(7, 4, '2025-10-30 13:00:00'),  -- Transformers Español en VIP
(8, 5, '2025-10-30 17:00:00'),  -- Transformers Inglés en Premium
(9, 6, '2025-10-30 18:00:00'),  -- Spider-Man Español en IMAX
(10, 1, '2025-10-30 20:00:00'), -- Spider-Man Inglés en 2D

-- 31 de Octubre 2025 - Sábado (Halloween)
(2, 6, '2025-10-31 13:00:00'),  -- Avatar Inglés en IMAX
(3, 1, '2025-10-31 10:00:00'),  -- John Wick Español en 2D
(9, 5, '2025-10-31 17:00:00');  -- Spider-Man Español en Premium

COMMIT;