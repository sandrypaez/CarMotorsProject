package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.OrdenServicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdenServicioDAO {
    private Connection conn;

    public OrdenServicioDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(OrdenServicio orden) throws SQLException {
        String sql = "INSERT INTO OrdenesServicio (id_vehiculo, id_servicio, estado, fecha_inicio, fecha_fin, recordatorio_fecha, recordatorio_enviado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, orden.getIdVehiculo());
            stmt.setInt(2, orden.getIdServicio());
            stmt.setString(3, orden.getEstado());
            stmt.setDate(4, orden.getFechaInicio());
            stmt.setDate(5, orden.getFechaFin());
            stmt.setDate(6, orden.getRecordatorioFecha());
            stmt.setBoolean(7, orden.isRecordatorioEnviado());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                orden.setId(rs.getInt(1));
            }
        }
    }

    public OrdenServicio read(int id) throws SQLException {
        String sql = "SELECT * FROM OrdenesServicio WHERE id_orden = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrdenServicio(rs);
            }
        }
        return null;
    }

    public List<OrdenServicio> readAll() throws SQLException {
        List<OrdenServicio> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM OrdenesServicio";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ordenes.add(mapResultSetToOrdenServicio(rs));
            }
        }
        return ordenes;
    }

    public void update(OrdenServicio orden) throws SQLException {
        String sql = "UPDATE OrdenesServicio SET id_vehiculo = ?, id_servicio = ?, estado = ?, fecha_inicio = ?, fecha_fin = ?, recordatorio_fecha = ?, recordatorio_enviado = ? WHERE id_orden = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orden.getIdVehiculo());
            stmt.setInt(2, orden.getIdServicio());
            stmt.setString(3, orden.getEstado());
            stmt.setDate(4, orden.getFechaInicio());
            stmt.setDate(5, orden.getFechaFin());
            stmt.setDate(6, orden.getRecordatorioFecha());
            stmt.setBoolean(7, orden.isRecordatorioEnviado());
            stmt.setInt(8, orden.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM OrdenesServicio WHERE id_orden = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void assignTecnico(int idOrden, int idTecnico) throws SQLException {
        String sql = "INSERT INTO Ordenes_Tecnicos (id_orden, id_tecnico) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            stmt.setInt(2, idTecnico);
            stmt.executeUpdate();
        }
    }

    public List<Integer> getTecnicosForOrden(int idOrden) throws SQLException {
        List<Integer> tecnicos = new ArrayList<>();
        String sql = "SELECT id_tecnico FROM Ordenes_Tecnicos WHERE id_orden = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tecnicos.add(rs.getInt("id_tecnico"));
            }
        }
        return tecnicos;
    }

    private OrdenServicio mapResultSetToOrdenServicio(ResultSet rs) throws SQLException {
        OrdenServicio orden = new OrdenServicio();
        orden.setId(rs.getInt("id_orden"));
        orden.setIdVehiculo(rs.getInt("id_vehiculo"));
        orden.setIdServicio(rs.getInt("id_servicio"));
        orden.setEstado(rs.getString("estado"));
        orden.setFechaInicio(rs.getDate("fecha_inicio"));
        orden.setFechaFin(rs.getDate("fecha_fin"));
        orden.setRecordatorioFecha(rs.getDate("recordatorio_fecha"));
        orden.setRecordatorioEnviado(rs.getBoolean("recordatorio_enviado"));
        return orden;
    }
}