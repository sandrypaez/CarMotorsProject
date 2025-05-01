CREATE DATABASE IF NOT EXISTS CarMotors;
USE CarMotors;

-- Tabla: Clientes
CREATE TABLE Clientes (
    id_cliente INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) NOT NULL,
    telefono VARCHAR(20),
    correo_electronico VARCHAR(100)
);

-- Tabla: Vehículos
CREATE TABLE Vehiculos (
    id_vehiculo INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    marca VARCHAR(50),
    modelo VARCHAR(50),
    placa VARCHAR(20) UNIQUE,
    tipo VARCHAR(20),
    FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);

-- Tabla: Proveedores
CREATE TABLE Proveedores (
    id_proveedor INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20),
    contacto VARCHAR(100),
    frecuencia_visita VARCHAR(50)
);

-- Tabla: Repuestos
CREATE TABLE Repuestos (
    id_repuesto INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    tipo ENUM('Mecánico', 'Eléctrico', 'Carrocería', 'Consumo'),
    marca VARCHAR(50),
    modelo VARCHAR(50),
    id_proveedor INT,
    cantidad_stock INT,
    nivel_minimo_stock INT,
    fecha_ingreso DATE,
    vida_util_dias INT,
    estado ENUM('Disponible', 'Reservado para trabajo', 'Fuera de servicio'),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);

-- Tabla: Lotes de Repuestos
CREATE TABLE Lotes (
    id_lote INT PRIMARY KEY AUTO_INCREMENT,
    id_repuesto INT,
    fecha_ingreso DATE,
    cantidad INT,
    FOREIGN KEY (id_repuesto) REFERENCES Repuestos(id_repuesto)
);

-- Tabla: Servicios de Mantenimiento
CREATE TABLE Servicios (
    id_servicio INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('Preventivo', 'Correctivo'),
    descripcion TEXT,
    costo_mano_obra DECIMAL(10,2),
    tiempo_estimado INT
);

-- Tabla: Órdenes de Servicio (registro de mantenimientos realizados)
CREATE TABLE OrdenesServicio (
    id_orden INT PRIMARY KEY AUTO_INCREMENT,
    id_vehiculo INT,
    id_servicio INT,
    estado ENUM('Pendiente', 'En proceso', 'Completado', 'Entregado'),
    fecha_inicio DATE,
    fecha_fin DATE,
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(id_vehiculo),
    FOREIGN KEY (id_servicio) REFERENCES Servicios(id_servicio)
);

-- Tabla: Repuestos Usados en Servicios
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

-- Tabla: Asignación de Técnicos a Órdenes
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
    id_orden INT,
    fecha_emision DATE,
    subtotal DECIMAL(10,2),
    impuestos DECIMAL(10,2),
    total DECIMAL(10,2),
    cufe VARCHAR(100),
    qr_url TEXT,
    FOREIGN KEY (id_orden) REFERENCES OrdenesServicio(id_orden)
);

-- Tabla: Actividades Especiales
CREATE TABLE ActividadesEspeciales (
    id_actividad INT PRIMARY KEY AUTO_INCREMENT,
    tipo ENUM('Campaña', 'Inspección'),
    descripcion TEXT,
    fecha_inicio DATE,
    fecha_fin DATE
);

-- Tabla: Participación de Vehículos en Actividades Especiales
CREATE TABLE VehiculosActividades (
    id_actividad INT,
    id_vehiculo INT,
    resultado VARCHAR(100), -- Aprobado, Reparaciones necesarias, Rechazado
    PRIMARY KEY (id_actividad, id_vehiculo),
    FOREIGN KEY (id_actividad) REFERENCES ActividadesEspeciales(id_actividad),
    FOREIGN KEY (id_vehiculo) REFERENCES Vehiculos(id_vehiculo)
);

-- Tabla: Evaluaciones de Proveedores
CREATE TABLE EvaluacionesProveedores (
    id_evaluacion INT PRIMARY KEY AUTO_INCREMENT,
    id_proveedor INT,
    puntualidad INT,
    calidad INT,
    costo INT,
    comentario TEXT,
    fecha DATE,
    FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);
