package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.ReporteDAO;
import com.carmotors.carmotors.model.entities.Reporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReporteController {
    private ReporteDAO reporteDAO;
    private Connection conn;

    public ReporteController(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no puede ser nula");
        }
        this.conn = conn;
        this.reporteDAO = new ReporteDAO(conn);
    }

    public List<Reporte> listarReportesPorTipo(String tipoReporte) throws SQLException {
        return reporteDAO.readByTipo(tipoReporte);
    }

    public void generarReporteInventarioRepuestos() throws SQLException {
        String sql = "SELECT tipo, marca, estado, cantidad, fecha_caducidad " +
                "FROM Repuestos";
        StringBuilder datos = new StringBuilder("Listado detallado de repuestos:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Tipo: %s, Marca: %s, Estado: %s, Cantidad: %d, Fecha Caducidad: %s\n",
                        rs.getString("tipo"), rs.getString("marca"), rs.getString("estado"),
                        rs.getInt("cantidad"), rs.getDate("fecha_caducidad")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Inventario");
        reporte.setDescripcion("Listado detallado de repuestos");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteConsumoRepuestos() throws SQLException {
        String sql = "SELECT r.tipo, r.marca, COUNT(*) as cantidad_usada, s.descripcion " +
                "FROM Repuestos r " +
                "JOIN Servicios_Repuestos sr ON r.id_repuesto = sr.id_repuesto " +
                "JOIN Servicios s ON sr.id_servicio = s.id_servicio " +
                "GROUP BY r.tipo, r.marca, s.descripcion";
        StringBuilder datos = new StringBuilder("Análisis de consumo de repuestos:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Tipo: %s, Marca: %s, Cantidad Usada: %d, Servicio: %s\n",
                        rs.getString("tipo"), rs.getString("marca"), rs.getInt("cantidad_usada"),
                        rs.getString("descripcion")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Inventario");
        reporte.setDescripcion("Análisis de consumo por períodos");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteAlertasRepuestos() throws SQLException {
        String sql = "SELECT tipo, marca, cantidad, fecha_caducidad " +
                "FROM Repuestos " +
                "WHERE fecha_caducidad <= DATE_ADD(CURDATE(), INTERVAL 30 DAY) OR cantidad < 5";
        StringBuilder datos = new StringBuilder("Alertas de repuestos:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Tipo: %s, Marca: %s, Cantidad: %d, Fecha Caducidad: %s\n",
                        rs.getString("tipo"), rs.getString("marca"), rs.getInt("cantidad"),
                        rs.getDate("fecha_caducidad")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Inventario");
        reporte.setDescripcion("Alertas de productos vencidos o próximos a caducar");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteServiciosPorTipoVehiculo() throws SQLException {
        String sql = "SELECT v.tipo_vehiculo, s.descripcion, COUNT(*) as cantidad " +
                "FROM OrdenesServicio os " +
                "JOIN Vehiculos v ON os.id_vehiculo = v.id_vehiculo " +
                "JOIN Servicios s ON os.id_servicio = s.id_servicio " +
                "GROUP BY v.tipo_vehiculo, s.descripcion";
        StringBuilder datos = new StringBuilder("Servicios más solicitados por tipo de vehículo:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Tipo Vehículo: %s, Servicio: %s, Cantidad: %d\n",
                        rs.getString("tipo_vehiculo"), rs.getString("descripcion"), rs.getInt("cantidad")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Mantenimiento");
        reporte.setDescripcion("Servicios más solicitados por tipo de vehículo");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteProductividadTecnicos() throws SQLException {
        String sql = "SELECT t.nombre, COUNT(*) as trabajos, AVG(DATEDIFF(os.fecha_fin, os.fecha_inicio)) as tiempo_promedio " +
                "FROM OrdenesServicio os " +
                "JOIN Ordenes_Tecnicos ot ON os.id_orden = ot.id_orden " +
                "JOIN Tecnicos t ON ot.id_tecnico = t.id_tecnico " +
                "WHERE os.estado = 'Completado' " +
                "GROUP BY t.id_tecnico, t.nombre";
        StringBuilder datos = new StringBuilder("Productividad de técnicos:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Técnico: %s, Trabajos Completados: %d, Tiempo Promedio (días): %.2f\n",
                        rs.getString("nombre"), rs.getInt("trabajos"), rs.getDouble("tiempo_promedio")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Mantenimiento");
        reporte.setDescripcion("Productividad de técnicos");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteHistorialMantenimientos() throws SQLException {
        String sql = "SELECT c.nombre, v.placa, s.descripcion, os.fecha_inicio, os.fecha_fin " +
                "FROM OrdenesServicio os " +
                "JOIN Vehiculos v ON os.id_vehiculo = v.id_vehiculo " +
                "JOIN Clientes c ON v.id_cliente = c.id_cliente " +
                "JOIN Servicios s ON os.id_servicio = s.id_servicio";
        StringBuilder datos = new StringBuilder("Historial de mantenimientos por cliente/vehículo:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Cliente: %s, Vehículo: %s, Servicio: %s, Inicio: %s, Fin: %s\n",
                        rs.getString("nombre"), rs.getString("placa"), rs.getString("descripcion"),
                        rs.getDate("fecha_inicio"), rs.getDate("fecha_fin")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Mantenimiento");
        reporte.setDescripcion("Historial de mantenimientos por cliente o vehículo");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteHistorialServiciosCliente() throws SQLException {
        String sql = "SELECT c.nombre, s.descripcion, f.total, r.tipo as repuesto " +
                "FROM OrdenesServicio os " +
                "JOIN Vehiculos v ON os.id_vehiculo = v.id_vehiculo " +
                "JOIN Clientes c ON v.id_cliente = c.id_cliente " +
                "JOIN Servicios s ON os.id_servicio = s.id_servicio " +
                "LEFT JOIN Facturas f ON os.id_orden = f.id_orden " +
                "LEFT JOIN Servicios_Repuestos sr ON s.id_servicio = sr.id_servicio " +
                "LEFT JOIN Repuestos r ON sr.id_repuesto = r.id_repuesto";
        StringBuilder datos = new StringBuilder("Historial de servicios por cliente:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Cliente: %s, Servicio: %s, Total Facturado: %.2f, Repuesto: %s\n",
                        rs.getString("nombre"), rs.getString("descripcion"), rs.getDouble("total"),
                        rs.getString("repuesto") != null ? rs.getString("repuesto") : "N/A"));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Clientes");
        reporte.setDescripcion("Historial de servicios por cliente");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteClientesFrecuentes() throws SQLException {
        String sql = "SELECT c.nombre, COUNT(os.id_orden) as num_servicios, SUM(f.total) as total_facturado " +
                "FROM Clientes c " +
                "LEFT JOIN Vehiculos v ON c.id_cliente = v.id_cliente " +
                "LEFT JOIN OrdenesServicio os ON v.id_vehiculo = os.id_vehiculo " +
                "LEFT JOIN Facturas f ON os.id_orden = f.id_orden " +
                "GROUP BY c.id_cliente, c.nombre " +
                "ORDER BY num_servicios DESC LIMIT 10";
        StringBuilder datos = new StringBuilder("Clientes frecuentes y su facturación:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Cliente: %s, Número de Servicios: %d, Total Facturado: %.2f\n",
                        rs.getString("nombre"), rs.getInt("num_servicios"), rs.getDouble("total_facturado")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Clientes");
        reporte.setDescripcion("Clientes frecuentes y su facturación");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteEvaluacionProveedores() throws SQLException {
        String sql = "SELECT p.nombre, AVG(CASE WHEN e.fecha_entrega <= e.fecha_prometida THEN 1 ELSE 0 END) as puntualidad, " +
                "AVG(e.calidad) as calidad_promedio, AVG(e.costo) as costo_promedio " +
                "FROM Proveedores p " +
                "JOIN Entregas e ON p.id_proveedor = e.id_proveedor " +
                "GROUP BY p.id_proveedor, p.nombre";
        StringBuilder datos = new StringBuilder("Evaluación de proveedores:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Proveedor: %s, Puntualidad: %.2f%%, Calidad: %.2f, Costo Promedio: %.2f\n",
                        rs.getString("nombre"), rs.getDouble("puntualidad") * 100, rs.getDouble("calidad_promedio"),
                        rs.getDouble("costo_promedio")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Proveedores");
        reporte.setDescripcion("Evaluación de proveedores");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteHistorialEntregasProveedores() throws SQLException {
        String sql = "SELECT p.nombre, r.tipo, e.cantidad, e.fecha_entrega " +
                "FROM Proveedores p " +
                "JOIN Entregas e ON p.id_proveedor = e.id_proveedor " +
                "JOIN Repuestos r ON e.id_repuesto = r.id_repuesto";
        StringBuilder datos = new StringBuilder("Historial de productos entregados por proveedor:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Proveedor: %s, Repuesto: %s, Cantidad: %d, Fecha Entrega: %s\n",
                        rs.getString("nombre"), rs.getString("tipo"), rs.getInt("cantidad"),
                        rs.getDate("fecha_entrega")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Proveedores");
        reporte.setDescripcion("Historial de productos entregados por proveedor");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }

    public void generarReporteEvaluacionCampanas() throws SQLException {
        String sql = "SELECT c.nombre, COUNT(os.id_orden) as participaciones, SUM(f.total) as impacto_economico, s.descripcion " +
                "FROM Campanas c " +
                "JOIN OrdenesServicio os ON c.id_campana = os.id_campana " +
                "JOIN Facturas f ON os.id_orden = f.id_orden " +
                "JOIN Servicios s ON os.id_servicio = s.id_servicio " +
                "GROUP BY c.id_campana, c.nombre, s.descripcion";
        StringBuilder datos = new StringBuilder("Evaluación de campañas de mantenimiento preventivo:\n");
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                datos.append(String.format("Campaña: %s, Participaciones: %d, Impacto Económico: %.2f, Servicio: %s\n",
                        rs.getString("nombre"), rs.getInt("participaciones"), rs.getDouble("impacto_economico"),
                        rs.getString("descripcion")));
            }
        }
        Reporte reporte = new Reporte();
        reporte.setTipoReporte("Campañas");
        reporte.setDescripcion("Evaluación de campañas de mantenimiento preventivo");
        reporte.setFechaGeneracion(LocalDate.now());
        reporte.setDatos(datos.toString());
        reporteDAO.create(reporte);
    }
}