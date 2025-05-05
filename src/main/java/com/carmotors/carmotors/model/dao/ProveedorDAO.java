package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Proveedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {
    private Connection conn;

    public ProveedorDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Proveedor proveedor) throws SQLException {
        String sql = "INSERT INTO Proveedores (nombre, nit, contacto, frecuencia_visita) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getContacto());
            stmt.setString(4, proveedor.getFrecuenciaVisita());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                proveedor.setId(rs.getInt(1));
            }
        }
    }

    public Proveedor read(int id) throws SQLException {
        String sql = "SELECT * FROM Proveedores WHERE id_proveedor = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToProveedor(rs);
            }
        }
        return null;
    }

    public List<Proveedor> readAll() throws SQLException {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                proveedores.add(mapResultSetToProveedor(rs));
            }
        }
        return proveedores;
    }

    public void update(Proveedor proveedor) throws SQLException {
        String sql = "UPDATE Proveedores SET nombre = ?, nit = ?, contacto = ?, frecuencia_visita = ? WHERE id_proveedor = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, proveedor.getNombre());
            stmt.setString(2, proveedor.getNit());
            stmt.setString(3, proveedor.getContacto());
            stmt.setString(4, proveedor.getFrecuenciaVisita());
            stmt.setInt(5, proveedor.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Proveedores WHERE id_proveedor = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Proveedor mapResultSetToProveedor(ResultSet rs) throws SQLException {
        Proveedor proveedor = new Proveedor();
        proveedor.setId(rs.getInt("id_proveedor"));
        proveedor.setNombre(rs.getString("nombre"));
        proveedor.setNit(rs.getString("nit"));
        proveedor.setContacto(rs.getString("contacto"));
        proveedor.setFrecuenciaVisita(rs.getString("frecuencia_visita"));
        return proveedor;
    }
}