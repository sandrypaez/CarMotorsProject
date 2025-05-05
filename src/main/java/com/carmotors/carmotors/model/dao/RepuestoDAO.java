package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Repuesto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepuestoDAO {
    private Connection conn;

    public RepuestoDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Repuesto repuesto) throws SQLException {
        String sql = "INSERT INTO Repuestos (nombre, tipo, marca, modelo, id_proveedor, cantidad_stock, nivel_minimo_stock, fecha_ingreso, vida_util_dias, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, repuesto.getNombre());
            stmt.setString(2, repuesto.getTipo());
            stmt.setString(3, repuesto.getMarca());
            stmt.setString(4, repuesto.getModelo());
            stmt.setInt(5, repuesto.getIdProveedor());
            stmt.setInt(6, repuesto.getCantidadStock());
            stmt.setInt(7, repuesto.getNivelMinimoStock());
            stmt.setString(8, repuesto.getFechaIngreso());
            stmt.setInt(9, repuesto.getVidaUtilDias());
            stmt.setString(10, repuesto.getEstado());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                repuesto.setId(rs.getInt(1));
            }
        }
    }

    public Repuesto read(int id) throws SQLException {
        String sql = "SELECT * FROM Repuestos WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToRepuesto(rs);
            }
        }
        return null;
    }

    public List<Repuesto> readAll() throws SQLException {
        List<Repuesto> repuestos = new ArrayList<>();
        String sql = "SELECT * FROM Repuestos";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                repuestos.add(mapResultSetToRepuesto(rs));
            }
        }
        return repuestos;
    }

    public void update(Repuesto repuesto) throws SQLException {
        String sql = "UPDATE Repuestos SET nombre = ?, tipo = ?, marca = ?, modelo = ?, id_proveedor = ?, cantidad_stock = ?, nivel_minimo_stock = ?, fecha_ingreso = ?, vida_util_dias = ?, estado = ? WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, repuesto.getNombre());
            stmt.setString(2, repuesto.getTipo());
            stmt.setString(3, repuesto.getMarca());
            stmt.setString(4, repuesto.getModelo());
            stmt.setInt(5, repuesto.getIdProveedor());
            stmt.setInt(6, repuesto.getCantidadStock());
            stmt.setInt(7, repuesto.getNivelMinimoStock());
            stmt.setString(8, repuesto.getFechaIngreso());
            stmt.setInt(9, repuesto.getVidaUtilDias());
            stmt.setString(10, repuesto.getEstado());
            stmt.setInt(11, repuesto.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Repuestos WHERE id_repuesto = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Repuesto mapResultSetToRepuesto(ResultSet rs) throws SQLException {
        Repuesto repuesto = new Repuesto();
        repuesto.setId(rs.getInt("id_repuesto"));
        repuesto.setNombre(rs.getString("nombre"));
        repuesto.setTipo(rs.getString("tipo"));
        repuesto.setMarca(rs.getString("marca"));
        repuesto.setModelo(rs.getString("modelo"));
        repuesto.setIdProveedor(rs.getInt("id_proveedor"));
        repuesto.setCantidadStock(rs.getInt("cantidad_stock"));
        repuesto.setNivelMinimoStock(rs.getInt("nivel_minimo_stock"));
        repuesto.setFechaIngreso(rs.getString("fecha_ingreso"));
        repuesto.setVidaUtilDias(rs.getInt("vida_util_dias"));
        repuesto.setEstado(rs.getString("estado"));
        return repuesto;
    }
}