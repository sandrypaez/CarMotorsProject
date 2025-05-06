-- Insert Data into CarMotors Database
-- Ensure the database and tables are already created before running these inserts.
-- The database schema should match the structure provided in the previous query.

USE carmotors;

-- 1. Tablas sin dependencias de claves foráneas
-- Clientes
INSERT INTO Clientes (nombre, identificacion, telefono, correo_electronico, direccion, discount_percentage, reward_points, fecha_compra) VALUES
('Juan Pérez', '123456789', '3001234567', 'juan.perez@email.com', 'Calle 123 #45-67, Bogotá', 5.0, 100, '2025-01-15'),
('María Gómez', '987654321', '3109876543', 'maria.gomez@email.com', 'Carrera 7 #89-12, Medellín', 10.0, 200, '2025-02-20'),
('Carlos López', '456789123', '3204567891', 'carlos.lopez@email.com', 'Avenida 68 #34-56, Cali', 0.0, 50, '2025-03-10'),
('Ana Rodríguez', '321654987', '3153216549', 'ana.rodriguez@email.com', 'Calle 50 #12-34, Barranquilla', 15.0, 300, '2025-04-05'),
('Pedro Sánchez', '654987321', '3176549873', 'pedro.sanchez@email.com', 'Carrera 15 #78-90, Bogotá', 5.0, 150, '2025-04-25');

-- Proveedores
INSERT INTO Proveedores (nombre, nit, contacto, frecuencia_visita) VALUES
('Repuestos S.A.', '900123456-1', 'Luis Martínez - 3001112222', 'Mensual'),
('AutoPartes Ltda.', '900654321-2', 'Sofía Vargas - 3102223333', 'Quincenal'),
('Motores y Más', '900987654-3', 'Andrés Gómez - 3203334444', 'Semanal'),
('Distribuidora del Valle', '900456789-4', 'Clara Díaz - 3154445555', 'Mensual'),
('Repuestos Elite', '900789123-5', 'Felipe Rojas - 3175556666', 'Quincenal');

-- Servicios
INSERT INTO Servicios (tipo, descripcion, costo_mano_obra, tiempo_estimado) VALUES
('Preventivo', 'Cambio de aceite y filtro', 150000.00, 2),
('Correctivo', 'Reparación de frenos delanteros', 350000.00, 4),
('Preventivo', 'Alineación y balanceo', 120000.00, 1),
('Correctivo', 'Cambio de embrague', 800000.00, 6),
('Preventivo', 'Revisión general de 20,000 km', 250000.00, 3);

-- Tecnicos
INSERT INTO Tecnicos (nombre, especialidad) VALUES
('Manuel Torres', 'Mecánica general'),
('Laura Castillo', 'Frenos y suspensión'),
('Diego Morales', 'Electricidad automotriz'),
('Sofía Ramírez', 'Alineación y balanceo'),
('Jorge Salazar', 'Motores y transmisiones');

-- Campanas
INSERT INTO Campanas (nombre, descripcion, fecha_inicio, fecha_fin) VALUES
('Revisión gratuita de frenos', 'Revisión gratuita para clientes frecuentes', '2025-01-01', '2025-01-31'),
('Cambio de aceite con descuento', '20% de descuento en cambio de aceite', '2025-02-01', '2025-02-28'),
('Semana de alineación', 'Alineación y balanceo al 50% de descuento', '2025-03-01', '2025-03-07'),
('Revisión pre-viaje', 'Revisión completa para viajes largos', '2025-04-01', '2025-04-15'),
('Mantenimiento de flotas', 'Descuentos para empresas con flotas', '2025-05-01', '2025-05-31');

-- Promociones
INSERT INTO Promociones (nombre, descripcion, descuento, fecha_inicio, fecha_fin) VALUES
('Descuento por fidelidad', '10% de descuento para clientes con más de 5 visitas', 10.00, '2025-01-01', '2025-12-31'),
('Promoción de verano', '15% de descuento en servicios preventivos', 15.00, '2025-06-01', '2025-08-31'),
('Black Friday', '20% de descuento en todos los servicios', 20.00, '2025-11-25', '2025-11-30'),
('Revisión gratuita', 'Revisión gratuita al contratar un servicio mayor', 0.00, '2025-03-01', '2025-03-31'),
('Descuento en repuestos', '10% de descuento en repuestos seleccionados', 10.00, '2025-04-01', '2025-04-30');

-- Reportes
INSERT INTO Reportes (tipo_reporte, descripcion, fecha_generacion, datos) VALUES
('Inventario', 'Listado detallado de repuestos', '2025-05-01', 'Aceite motor: 50 unidades, Filtro de aire: 30 unidades'),
('Mantenimiento', 'Servicios más solicitados por tipo de vehículo', '2025-05-02', 'Sedanes: Cambio de aceite (20), SUVs: Alineación (15)'),
('Clientes', 'Clientes frecuentes y su facturación', '2025-05-03', 'Juan Pérez: $1,500,000, María Gómez: $2,000,000'),
('Proveedores', 'Evaluación de proveedores', '2025-05-04', 'Repuestos S.A.: Puntualidad 8, Calidad 7'),
('Campañas', 'Evaluación de campañas de mantenimiento preventivo', '2025-05-05', 'Revisión gratuita de frenos: 50 participantes');

-- 2. Tablas con dependencias de claves foráneas (Nivel 1)
-- Vehiculos (depende de Clientes)
INSERT INTO Vehiculos (id_cliente, marca, modelo, placa, tipo_vehiculo, año) VALUES
(1, 'Toyota', 'Corolla', 'ABC123', 'Sedán', 2020),
(2, 'Mazda', 'CX-5', 'XYZ789', 'SUV', 2022),
(3, 'Ford', 'Fiesta', 'DEF456', 'Hatchback', 2019),
(4, 'Chevrolet', 'Spark', 'GHI789', 'Compacto', 2021),
(5, 'Nissan', 'Versa', 'JKL012', 'Sedán', 2023);

-- Repuestos (depende de Proveedores)
INSERT INTO Repuestos (nombre, tipo, marca, modelo, id_proveedor, cantidad_stock, nivel_minimo_stock, fecha_ingreso, fecha_caducidad, vida_util_dias, estado) VALUES
('Aceite de motor 5W-30', 'Consumo', 'Castrol', 'Magnatec', 1, 50, 10, '2025-01-10', '2026-01-10', 365, 'Disponible'),
('Filtro de aire', 'Mecánico', 'Bosch', 'Universal', 2, 30, 5, '2025-02-15', NULL, 180, 'Disponible'),
('Pastillas de freno', 'Mecánico', 'Brembo', 'Ceramic', 3, 20, 5, '2025-03-01', NULL, 730, 'Disponible'),
('Batería 12V', 'Eléctrico', 'Exide', 'Extreme', 4, 15, 3, '2025-04-01', '2027-04-01', 730, 'Disponible'),
('Parachoques delantero', 'Carrocería', 'Toyota', 'Corolla 2020', 5, 10, 2, '2025-04-10', NULL, 1095, 'Disponible');

-- 3. Tablas con dependencias de claves foráneas (Nivel 2)
-- Lotes (depende de Repuestos)
INSERT INTO Lotes (id_repuesto, fecha_ingreso, cantidad) VALUES
(1, '2025-01-10', 50),
(2, '2025-02-15', 30),
(3, '2025-03-01', 20),
(4, '2025-04-01', 15),
(5, '2025-04-10', 10);

-- OrdenesServicio (depende de Vehiculos, Servicios, Campanas)
-- Reset AUTO_INCREMENT to ensure id_orden starts at 1
ALTER TABLE OrdenesServicio AUTO_INCREMENT = 1;
INSERT INTO OrdenesServicio (id_vehiculo, id_servicio, id_campana, estado, fecha_inicio, fecha_fin, recordatorio_fecha, recordatorio_enviado) VALUES
(1, 1, 1, 'Completado', '2025-01-15', '2025-01-15', '2025-01-14', TRUE),
(2, 2, NULL, 'En proceso', '2025-02-20', NULL, '2025-02-19', FALSE),
(3, 3, 3, 'Completado', '2025-03-10', '2025-03-10', '2025-03-09', TRUE),
(4, 4, NULL, 'Pendiente', '2025-04-05', NULL, '2025-04-04', FALSE),
(5, 5, 4, 'Completado', '2025-04-25', '2025-04-25', '2025-04-24', TRUE);

-- EvaluacionesProveedores (depende de Proveedores)
INSERT INTO EvaluacionesProveedores (id_proveedor, puntualidad, calidad, costo, comentario, fecha) VALUES
(1, 8, 7, 6, 'Entrega puntual pero calidad mejorable', '2025-01-20'),
(2, 9, 8, 7, 'Buen proveedor, precios razonables', '2025-02-25'),
(3, 7, 6, 5, 'Entrega tardía, calidad promedio', '2025-03-15'),
(4, 8, 9, 8, 'Excelente calidad y puntualidad', '2025-04-10'),
(5, 6, 7, 6, 'Entrega a tiempo, costo elevado', '2025-04-20');

-- Clientes_Promociones (depende de Clientes, Promociones)
INSERT INTO Clientes_Promociones (id_cliente, id_promocion, fecha_aplicacion) VALUES
(1, 1, '2025-01-15'),
(2, 2, '2025-06-01'),
(3, 3, '2025-11-25'),
(4, 4, '2025-03-10'),
(5, 5, '2025-04-25');

-- 4. Tablas con dependencias de claves foráneas (Nivel 3)
-- Servicios_Repuestos (depende de OrdenesServicio, Repuestos, Lotes)
-- Reset AUTO_INCREMENT to ensure id_repuesto_usado starts at 1
ALTER TABLE Servicios_Repuestos AUTO_INCREMENT = 1;
INSERT INTO Servicios_Repuestos (id_orden, id_repuesto, cantidad, id_lote) VALUES
(1, 1, 2, 1),
(2, 3, 4, 3),
(3, 2, 1, 2),
(4, 4, 1, 4),
(5, 5, 1, 5);

-- Ordenes_Tecnicos (depende de OrdenesServicio, Tecnicos)
INSERT INTO Ordenes_Tecnicos (id_orden, id_tecnico) VALUES
(1, 1),
(2, 2),
(3, 4),
(4, 5),
(5, 3);

-- Facturas (depende de OrdenesServicio)
INSERT INTO Facturas (id_orden, fecha_emision, subtotal, impuestos, total, cufe, qr_url, pdf_url) VALUES
(1, '2025-01-15 14:30:00', 150000.00, 28500.00, 178500.00, 'CUFE12345', 'http://qr.com/123', 'http://pdf.com/123'),
(2, '2025-02-20 10:00:00', 350000.00, 66500.00, 416500.00, 'CUFE67890', 'http://qr.com/678', 'http://pdf.com/678'),
(3, '2025-03-10 16:00:00', 120000.00, 22800.00, 142800.00, 'CUFE54321', 'http://qr.com/543', 'http://pdf.com/543'),
(4, '2025-04-05 09:00:00', 800000.00, 152000.00, 952000.00, 'CUFE09876', 'http://qr.com/098', 'http://pdf.com/098'),
(5, '2025-04-25 15:00:00', 250000.00, 47500.00, 297500.00, 'CUFE11223', 'http://qr.com/112', 'http://pdf.com/112');

-- Vehiculos_Campanas (depende de Campanas, Vehiculos)
INSERT INTO Vehiculos_Campanas (id_campana, id_vehiculo, resultado) VALUES
(1, 1, 'Frenos en buen estado'),
(2, 2, 'Cambio de aceite realizado'),
(3, 3, 'Alineación completada'),
(4, 4, 'Revisión pre-viaje aprobada'),
(5, 5, 'Mantenimiento de flota realizado');

-- Entregas (depende de Proveedores, Repuestos)
INSERT INTO Entregas (id_proveedor, id_repuesto, cantidad, fecha_prometida, fecha_entrega, calidad, costo) VALUES
(1, 1, 50, '2025-01-08', '2025-01-10', 8, 500000.00),
(2, 2, 30, '2025-02-13', '2025-02-15', 7, 300000.00),
(3, 3, 20, '2025-02-28', '2025-03-01', 6, 400000.00),
(4, 4, 15, '2025-03-30', '2025-04-01', 9, 600000.00),
(5, 5, 10, '2025-04-08', '2025-04-10', 7, 700000.00);

-- 5. Tablas con dependencias de claves foráneas (Nivel 4)
-- Factura_Detalle (depende de Facturas)
INSERT INTO Factura_Detalle (id_factura, descripcion_servicio, cantidad, valor_unitario, subtotal) VALUES
(1, 'Cambio de aceite y filtro', 1, 150000.00, 150000.00),
(2, 'Reparación de frenos delanteros', 1, 350000.00, 350000.00),
(3, 'Alineación y balanceo', 1, 120000.00, 120000.00),
(4, 'Cambio de embrague', 1, 800000.00, 800000.00),
(5, 'Revisión general de 20,000 km', 1, 250000.00, 250000.00);

-- Compras (depende de Proveedores)
INSERT INTO Compras (proveedor_id, producto, cantidad, precio_unitario, fecha_compra, estado) VALUES
(1, 'Aceite de motor 5W-30', 50, 10000.00, '2025-01-10', 'Completado'),
(2, 'Filtro de aire', 30, 10000.00, '2025-02-15', 'Completado'),
(3, 'Pastillas de freno', 20, 20000.00, '2025-03-01', 'Completado'),
(4, 'Batería 12V', 15, 40000.00, '2025-04-01', 'Completado'),
(5, 'Parachoques delantero', 10, 70000.00, '2025-04-10', 'Completado');

-- End of Insert Statements