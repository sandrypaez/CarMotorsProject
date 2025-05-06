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
    public Factura obtenerDatosFactura(int idOrden) throws SQLException {
        String sql = "SELECT s.costo_mano_obra, " +
                "IFNULL(SUM(sr.cantidad), 0) AS total_repuestos " +
                "FROM OrdenesServicio os " +
                "JOIN Servicios s ON os.id_servicio = s.id_servicio " +
                "LEFT JOIN Servicios_Repuestos sr ON os.id_orden = sr.id_orden " +
                "WHERE os.id_orden = ? " +
                "GROUP BY s.costo_mano_obra";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double manoObra = rs.getDouble("costo_mano_obra");
                    double repuestos = rs.getDouble("total_repuestos"); // realmente solo suma cantidades por ahora
                    double subtotal = manoObra + repuestos; // esto es simbólico, puedes ajustar cuando tengas precios
                    double impuestos = subtotal * 0.19;
                    double total = subtotal + impuestos;

                    Factura factura = new Factura();
                    factura.setIdOrden(idOrden);
                    factura.setSubtotal(subtotal);
                    factura.setImpuestos(impuestos);
                    factura.setTotal(total);
                    return factura;
                }
            }
        }
        return null;
    }

}
