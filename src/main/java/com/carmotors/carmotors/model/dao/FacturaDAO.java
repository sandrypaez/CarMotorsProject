package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Factura;
import java.sql.*;

public class FacturaDAO {
    public void guardarFactura(Factura factura) throws SQLException {
        String sql = "INSERT INTO Facturas (id_orden, fecha_emision, subtotal, impuestos, total, cufe, qr_url) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, factura.getIdOrden());
            stmt.setDate(2, new java.sql.Date(factura.getFechaEmision().getTime()));
            stmt.setDouble(3, factura.getSubtotal());
            stmt.setDouble(4, factura.getImpuestos());
            stmt.setDouble(5, factura.getTotal());
            stmt.setString(6, factura.getCufe());
            stmt.setString(7, factura.getQrUrl());
            stmt.executeUpdate();
        }
    }
}
