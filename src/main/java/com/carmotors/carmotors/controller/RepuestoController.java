package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.RepuestoDAO;
import com.carmotors.carmotors.model.entities.Repuesto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RepuestoController {
    private RepuestoDAO repuestoDAO;
    private Connection conn;

    public RepuestoController(Connection conn) {
        this.conn = conn;
        this.repuestoDAO = new RepuestoDAO(conn);
    }

    public void addRepuesto(Repuesto repuesto) throws SQLException {
        repuestoDAO.create(repuesto);
    }

    public Repuesto getRepuesto(int id) throws SQLException {
        return repuestoDAO.read(id);
    }

    public List<Repuesto> getAllRepuestos() throws SQLException {
        return repuestoDAO.readAll();
    }

    public void updateRepuesto(Repuesto repuesto) throws SQLException {
        repuestoDAO.update(repuesto);
    }

    public void deleteRepuesto(int id) throws SQLException {
        repuestoDAO.delete(id);
    }

    public boolean providerExists(int idProveedor) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Proveedores WHERE id_proveedor = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProveedor);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}