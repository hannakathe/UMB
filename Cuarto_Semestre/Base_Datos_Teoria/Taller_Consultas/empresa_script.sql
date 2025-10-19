-- ===============================================
-- SCRIPT DE BASE DE DATOS: EMPRESA
-- Autor: Hanna Abril
-- Fecha: Octubre 2025
-- Descripción: Práctica de consultas SQL con WHERE y JOIN
-- ===============================================



-- ===============================================
-- 1️⃣ CREACIÓN DE LA BASE DE DATOS
-- ===============================================

CREATE DATABASE IF NOT EXISTS empresa;
USE empresa;

-- ===============================================
-- 2️⃣ CREACIÓN DE TABLAS
-- ===============================================

-- Tabla Departamento
DROP TABLE IF EXISTS Departamento;
CREATE TABLE Departamento (
    id_departamento INT PRIMARY KEY AUTO_INCREMENT,
    nombre_departamento VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(50) NOT NULL
);

-- Tabla Empleado
DROP TABLE IF EXISTS Empleado;
CREATE TABLE Empleado (
    id_empleado INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    departamento_id INT,
    salario DECIMAL(10,2),
    fecha_contratacion DATE,
    FOREIGN KEY (departamento_id) REFERENCES Departamento(id_departamento)
);

-- ===============================================
-- 3️⃣ INSERCIÓN DE DATOS
-- ===============================================

-- Departamentos
INSERT INTO Departamento (nombre_departamento, ubicacion) VALUES
('Recursos Humanos', 'Sede Central'),
('Marketing', 'Sede Regional'),
('Ventas', 'Sede Central'),
('Soporte', 'Sede Regional'),
('Desarrollo', 'Sede Central'),
('Logística', 'Sede Regional'),
('Finanzas', 'Sede Central'),
('Legal', 'Sede Central'),
('Compras', 'Sede Regional'),
('Dirección', 'Sede Central');

-- Empleados (varios en los mismos departamentos)
INSERT INTO Empleado (nombre, apellido, departamento_id, salario, fecha_contratacion) VALUES
('Laura', 'Sánchez', 1, 3500.00, '2021-03-15'),
('Carlos', 'Suárez', 2, 2800.00, '2019-08-22'),
('Marta', 'Gómez', 3, 4200.00, '2020-06-01'),
('Andrés', 'Sosa', 3, 4100.00, '2022-01-10'),
('Diana', 'Salazar', 5, 5100.00, '2018-09-30'),
('Sergio', 'Serrano', 5, 4700.00, '2023-02-14'),
('Natalia', 'Silva', 7, 5500.00, '2021-11-01'),
('Felipe', 'Santos', 2, 3100.00, '2020-04-18'),
('Camila', 'Sandoval', 3, 2600.00, '2023-05-10'),
('Jorge', 'Soto', 10, 6000.00, '2017-12-05');

-- ===============================================
-- 4️⃣ CONSULTAS SQL (Parte 1 - WHERE)
-- ===============================================

-- 1. Empleados con salario superior a 3000
SELECT * FROM Empleado
WHERE salario > 3000;

-- 2. Empleados contratados después del 1 de enero de 2020
SELECT * FROM Empleado
WHERE fecha_contratacion > '2020-01-01';

-- 3. Empleados que trabajen en el departamento con ID 2
SELECT * FROM Empleado
WHERE departamento_id = 2;

-- 4. Empleados cuyo apellido comience con 'S'
SELECT * FROM Empleado
WHERE apellido LIKE 'S%';

-- 5. Empleados con salario entre 2000 y 5000
SELECT * FROM Empleado
WHERE salario BETWEEN 2000 AND 5000;

-- ===============================================
-- 5️⃣ CONSULTAS SQL (Parte 2 - JOIN + WHERE)
-- ===============================================

-- 6. Nombres de empleados junto con el nombre del departamento donde trabajan
SELECT e.nombre, e.apellido, d.nombre_departamento
FROM Empleado e
JOIN Departamento d ON e.departamento_id = d.id_departamento;

-- 7. Empleados que trabajan en la ubicación 'Sede Central'
SELECT e.nombre, e.apellido, d.ubicacion
FROM Empleado e
JOIN Departamento d ON e.departamento_id = d.id_departamento
WHERE d.ubicacion = 'Sede Central';

-- 8. Empleados del departamento de 'Marketing' con salario superior a 4000
SELECT e.nombre, e.apellido, e.salario, d.nombre_departamento
FROM Empleado e
JOIN Departamento d ON e.departamento_id = d.id_departamento
WHERE d.nombre_departamento = 'Marketing' AND e.salario > 4000;

-- 9. Empleados cuyo departamento no esté en la 'Sede Regional'
SELECT e.nombre, e.apellido, d.ubicacion
FROM Empleado e
JOIN Departamento d ON e.departamento_id = d.id_departamento
WHERE d.ubicacion <> 'Sede Regional';

-- 10. Empleados de los departamentos 'Ventas' o 'Soporte'
SELECT e.nombre, e.apellido, d.nombre_departamento
FROM Empleado e
JOIN Departamento d ON e.departamento_id = d.id_departamento
WHERE d.nombre_departamento IN ('Ventas', 'Soporte');
