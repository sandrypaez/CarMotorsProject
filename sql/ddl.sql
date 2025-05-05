-- Base de datos: CarMotors
CREATE DATABASE IF NOT EXISTS carmotors;
USE carmotors;

-- Tabla: Clientes (ACTUALIZADA con discount_percentage y reward_points)
CREATE TABLE Clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100),
    direccion VARCHAR(150),
    discount_percentage DOUBLE DEFAULT 0,
    reward_points INT DEFAULT 0
);

-- Tabla: Vehículos
CREATE TABLE Vehiculos (
    id_vehiculo INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT NOT NULL,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    placa VARCHAR(20) UNIQUE NOT NULL,
    tipo VARCHAR(20),
    año INT,
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);

-- Tabla: Proveedores
CREATE TABLE Proveedores (
    id_proveedor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20) UNIQUE,
    contacto VARCHAR(100),
    frecuencia_visita VARCHAR(50)
);

-- Tabla: Repuestos
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
    vida_util_dias INT,
    estado ENUM('Disponible', 'Reservado para trabajo', 'Fuera de servicio'),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);

-- Tabla: Lotes de Repuestos
CREATE TABLE Lotes (
    id_lote INT PRIMARY KEY AUTO_INCREMENT,
    id_repuesto INT NOT NULL,
    fecha_ingreso DATE,
    cantidad INT,
    FOREIGN KEY (id_repuesto) REFERENCES Repuestos(id_repuesto)
);

-- Tabla: Servicios
CREATE TABLE Servicios (
    id_servicio INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('Preventivo', 'Correctivo') NOT NULL,
    descripcion TEXT,
    costo_mano_obra DECIMAL(10,2),
    tiempo_estimado INT
);

-- Tabla: Órdenes de Servicio
CREATE TABLE OrdenesServicio (
    id_orden INT PRIMARY KEY AUTO_INCREMENT,
    id_vehiculo INT NOT NULL,
    id_servicio INT NOT NULL,
    estado ENUM('Pendiente', 'En proceso', 'Completado', 'Entregado') DEFAULT 'Pendiente',
    fecha_inicio DATE,
    fecha_fin DATE,
    recordatorio_fecha DATE,
    recordatorio_enviado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(id_vehiculo),
    FOREIGN KEY (id_servicio) REFERENCES Servicios(id_servicio)
);

-- Tabla: Repuestos Usados
CREATE TABLE RepuestosUsados (
    id_repuesto_usado INT PRIMARY KEY AUTO_INCREMENT,
    id_orden INT,
    id_repuesto INT,
    cantidad INT,
    id_lote INT,
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden),
    FOREIGN KEY (id_repuesto) REFERENCES Repuestos(id_repuesto),
    FOREIGN KEY (id_lote) REFERENCES Lotes(id_lote)
);

-- Tabla: Técnicos
CREATE TABLE Tecnicos (
    id_tecnico INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    especialidad VARCHAR(100)
);

-- Tabla: Asignación Técnicos a Ordenes
CREATE TABLE Ordenes_Tecnicos (
    id_orden INT,
    id_tecnico INT,
    PRIMARY KEY (id_orden, id_tecnico),
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden),
    FOREIGN KEY (id_tecnico) REFERENCES Tecnicos(id_tecnico)
);

-- Tabla: Facturas
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
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden)
);

-- Tabla: Detalle de Factura
CREATE TABLE Factura_Detalle (
    id_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_factura INT,
    descripcion_servicio TEXT,
    cantidad INT,
    valor_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    FOREIGN KEY (id_factura) REFERENCES Facturas(id_factura)
);

-- Tabla: Actividades Especiales
CREATE TABLE ActividadesEspeciales (
    id_actividad INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('Campaña', 'Inspección'),
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE
);

-- Participación en Actividades
CREATE TABLE VehiculosActividades (
    id_actividad INT,
    id_vehiculo INT,
    resultado VARCHAR(100),
    PRIMARY KEY (id_actividad, id_vehiculo),
    FOREIGN KEY (id_actividad) REFERENCES ActividadesEspeciales(id_actividad),
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(id_vehiculo)
);

-- Evaluación de Proveedores
CREATE TABLE EvaluacionesProveedores (
    id_evaluacion INT PRIMARY KEY AUTO_INCREMENT,
    id_proveedor INT,
    puntualidad INT CHECK(puntualidad BETWEEN 1 AND 10),
    calidad INT CHECK(calidad BETWEEN 1 AND 10),
    costo INT CHECK(costo BETWEEN 1 AND 10),
    comentario TEXT,
    fecha DATE,
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);

-- Promociones
CREATE TABLE Promociones (
    id_promocion INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    descripcion TEXT,
    descuento DECIMAL(5,2),
    fecha_inicio DATE,
    fecha_fin DATE
);

-- Clientes con Promociones
CREATE TABLE Clientes_Promociones (
    id_cliente INT,
    id_promocion INT,
    fecha_aplicacion DATE,
    PRIMARY KEY (id_cliente, id_promocion),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente),
    FOREIGN KEY (id_promocion) REFERENCES Promociones(id_promocion)
);

-- Reportes
CREATE TABLE Reportes (
    id_reporte INT PRIMARY KEY AUTO_INCREMENT,
    tipo_reporte VARCHAR(50) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_generacion DATE NOT NULL,
    datos TEXT NOT NULL
);