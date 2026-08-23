CREATE TABLE Cliente (
    id_cliente     INT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    correo         VARCHAR(100),
    telefono       VARCHAR(20)
);

CREATE TABLE Empleado (
    id_empleado    INT PRIMARY KEY,
    nombre         VARCHAR(100) NOT NULL,
    cargo          VARCHAR(50)
);

CREATE TABLE Categoria (
    id_categoria   INT PRIMARY KEY,
    nombre         VARCHAR(50) NOT NULL
);

CREATE TABLE Producto (
    id_producto         INT PRIMARY KEY,
    nombre              VARCHAR(100) NOT NULL,
    precio              DECIMAL(10,2) NOT NULL,
    cantidad_disponible INT NOT NULL,
    id_categoria        INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

CREATE TABLE Venta (
    id_venta       INT PRIMARY KEY,
    fecha          DATE NOT NULL,
    valor_total    DECIMAL(10,2) NOT NULL,
    id_cliente     INT NOT NULL,
    id_empleado    INT NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id_cliente),
    FOREIGN KEY (id_empleado) REFERENCES Empleado(id_empleado)
);

CREATE TABLE DetalleVenta (
    id_detalle      INT PRIMARY KEY,
    id_venta        INT NOT NULL,
    id_producto     INT NOT NULL,
    cantidad        INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta),
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);