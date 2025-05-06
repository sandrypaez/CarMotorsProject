-- Base de datos: CarMotors
CREATE DATABASE IF NOT EXISTS carmotors;
USE carmotors;

-- Tablas sin dependencias de claves foráneas
CREATE TABLE Clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100),
    direccion VARCHAR(150),
    discount_percentage DOUBLE DEFAULT 0,
    reward_points INT DEFAULT 0,
    fecha_compra DATE,
    INDEX idx_identificacion (identificacion)
);

CREATE TABLE Proveedores (
    id_proveedor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20) UNIQUE,
    contacto VARCHAR(100),
    frecuencia_visita VARCHAR(50)
);

CREATE TABLE Servicios (
    id_servicio INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('Preventivo', 'Correctivo') NOT NULL,
    descripcion TEXT,
    costo_mano_obra DECIMAL(10,2),
    tiempo_estimado INT
);

CREATE TABLE Tecnicos (
    id_tecnico INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    especialidad VARCHAR(100)
);

CREATE TABLE Campanas (
    id_campana INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE
);

CREATE TABLE Promociones (
    id_promocion INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    descripcion TEXT,
    descuento DECIMAL(5,2),
    fecha_inicio DATE,
    fecha_fin DATE
);

CREATE TABLE Reportes (
    id_reporte INT PRIMARY KEY AUTO_INCREMENT,
    tipo_reporte VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_generacion DATE NOT NULL,
    datos TEXT NOT NULL,
    INDEX idx_tipo_reporte (tipo_reporte)
);

-- Tablas con dependencias de claves foráneas (Nivel 1)
CREATE TABLE Vehiculos (
    id_vehiculo INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    placa VARCHAR(20) UNIQUE NOT NULL,
    tipo_vehiculo VARCHAR(20),
    año INT,
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
    INDEX idx_id_cliente (id_cliente)
);

CREATE TABLE Repuestos (
    id_repuesto INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo ENUM('Mecánico', 'Eléctrico', 'Carrocería', 'Consumo') NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    id_proveedor INT,
    cantidad_stock INT DEFAULT 0,
    nivel_minimo_stock INT DEFAULT 0,
    fecha_ingreso DATE,
    fecha_caducidad DATE,
    vida_util_dias INT,
    estado ENUM('Disponible', 'Reservado para trabajo', 'Fuera de servicio'),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor),
    INDEX idx_id_proveedor (id_proveedor)
);

-- Tablas con dependencias de claves foráneas (Nivel 2)
CREATE TABLE Lotes (
    id_lote INT PRIMARY KEY AUTO_INCREMENT,
    id_repuesto INT NOT NULL,
    fecha_ingreso DATE,
    cantidad INT,
    FOREIGN KEY (id_repuesto) REFERENCES Repuestos(id_repuesto),
    INDEX idx_id_repuesto (id_repuesto)
);

CREATE TABLE OrdenesServicio (
    id_orden INT PRIMARY KEY AUTO_INCREMENT,
    id_vehiculo INT NOT NULL,
    id_servicio INT NOT NULL,
    id_campana INT,
    estado ENUM('Pendiente', 'En proceso', 'Completado', 'Entregado') DEFAULT 'Pendiente',
    fecha_inicio DATE,
    fecha_fin DATE,
    recordatorio_fecha DATE,
    recordatorio_enviado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(id_vehiculo),
    FOREIGN KEY (id_servicio) REFERENCES Servicios(id_servicio),
    FOREIGN KEY (id_campana) REFERENCES Campanas(id_campana),
    INDEX idx_id_vehiculo (id_vehiculo),
    INDEX idx_id_servicio (id_servicio),
    INDEX idx_id_campana (id_campana)
);

CREATE TABLE EvaluacionesProveedores (
    id_evaluacion INT PRIMARY KEY AUTO_INCREMENT,
    id_proveedor INT,
    puntualidad INT CHECK(puntualidad BETWEEN 1 AND 10),
    calidad INT CHECK(calidad BETWEEN 1 AND 10),
    costo INT CHECK(costo BETWEEN 1 AND 10),
    comentario TEXT,
    fecha DATE,
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor),
    INDEX idx_id_proveedor (id_proveedor)
);

CREATE TABLE Clientes_Promociones (
    id_cliente INT,
    id_promocion INT,
    fecha_aplicacion DATE,
    PRIMARY KEY (id_cliente, id_promocion),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
    FOREIGN KEY (id_promocion) REFERENCES Promociones(id_promocion),
    INDEX idx_id_cliente (id_cliente)
);

-- Tablas con dependencias de claves foráneas (Nivel 3)
CREATE TABLE Servicios_Repuestos (
    id_repuesto_usado INT PRIMARY KEY AUTO_INCREMENT,
    id_orden INT,
    id_repuesto INT,
    cantidad INT,
    id_lote INT,
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden),
    FOREIGN KEY (id_repuesto) REFERENCES Repuestos(id_repuesto),
    FOREIGN KEY (id_lote) REFERENCES Lotes(id_lote),
    INDEX idx_id_orden (id_orden),
    INDEX idx_id_repuesto (id_repuesto)
);

CREATE TABLE Ordenes_Tecnicos (
    id_orden INT,
    id_tecnico INT,
    PRIMARY KEY (id_orden, id_tecnico),
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden),
    FOREIGN KEY (id_tecnico) REFERENCES Tecnicos(id_tecnico),
    INDEX idx_id_tecnico (id_tecnico)
);

CREATE TABLE Facturas (
    id_factura INT PRIMARY KEY AUTO_INCREMENT,
    id_orden INT NOT NULL,
    fecha_emision DATETIME,
    subtotal DECIMAL(10,2),
    impuestos DECIMAL(10,2),
    total DECIMAL(10,2),
    cufe VARCHAR(100),
    qr_url TEXT,
    pdf_url TEXT,
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden),
    INDEX idx_id_orden (id_orden)
);

CREATE TABLE Vehiculos_Campanas (
    id_campana INT,
    id_vehiculo INT,
    resultado VARCHAR(100),
    PRIMARY KEY (id_campana, id_vehiculo),
    FOREIGN KEY (id_campana) REFERENCES Campanas(id_campana),
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(id_vehiculo),
    INDEX idx_id_campana (id_campana)
);

CREATE TABLE Entregas (
    id_entrega INT PRIMARY KEY AUTO_INCREMENT,
    id_proveedor INT,
    id_repuesto INT,
    cantidad INT NOT NULL,
    fecha_prometida DATE,
    fecha_entrega DATE,
    calidad INT CHECK(calidad BETWEEN 1 AND 10),
    costo DECIMAL(10,2),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor),
    FOREIGN KEY (id_repuesto) REFERENCES Repuestos(id_repuesto),
    INDEX idx_id_proveedor (id_proveedor),
    INDEX idx_id_repuesto (id_repuesto)
);

-- Tablas con dependencias de claves foráneas (Nivel 4)
CREATE TABLE Factura_Detalle (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_factura INT,
    descripcion_servicio TEXT,
    cantidad INT,
    valor_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    FOREIGN KEY (id_factura) REFERENCES Facturas(id_factura),
    INDEX idx_id_factura (id_factura)
);

-- Nueva tabla: Compras (depende de Proveedores)
CREATE TABLE Compras (
    id INT PRIMARY KEY AUTO_INCREMENT,
    proveedor_id INT NOT NULL,
    producto VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    fecha_compra DATE NOT NULL,
    estado ENUM('Pendiente', 'Completado', 'Cancelado') DEFAULT 'Pendiente',
    FOREIGN KEY (proveedor_id) REFERENCES Proveedores(id_proveedor),
    INDEX idx_proveedor_id (proveedor_id)
);