package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Factura;
import java.sql.*;
import java.util.UUID;

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
        String sql = "SELECT os.id_orden, s.costo_mano_obra, " +
                "IFNULL(SUM(sr.cantidad), 0) AS total_repuestos, " +
                "v.placa AS vehiculo, c.nombre AS nombre_cliente, c.identificacion AS documento_cliente, " +
                "c.direccion AS direccion_cliente, s.descripcion AS descripcion_servicio, " +
                "c.correo_electronico AS email, c.telefono AS telefono " +
                "FROM OrdenesServicio os " +
                "JOIN Servicios s ON os.id_servicio = s.id_servicio " +
                "JOIN Vehiculos v ON os.id_vehiculo = v.id_vehiculo " +
                "JOIN Clientes c ON v.id_cliente = c.id_cliente " +
                "LEFT JOIN Servicios_Repuestos sr ON os.id_orden = sr.id_orden " +
                "WHERE os.id_orden = ? " +
                "GROUP BY os.id_orden, s.costo_mano_obra, v.placa, c.nombre, c.identificacion, c.direccion, s.descripcion, c.correo_electronico, c.telefono";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idOrden);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double manoObra = rs.getDouble("costo_mano_obra");
                    double repuestos = rs.getDouble("total_repuestos"); // Adjust if prices are available
                    double subtotal = manoObra + repuestos;
                    double impuestos = subtotal * 0.19;
                    double total = subtotal + impuestos;

                    Factura factura = new Factura();
                    factura.setIdOrden(idOrden);
                    factura.setSubtotal(subtotal);
                    factura.setImpuestos(impuestos);
                    factura.setTotal(total);
                    factura.setFechaEmision(new java.util.Date());
                    factura.setCufe("CUFE-" + UUID.randomUUID().toString().substring(0, 8));
                    factura.setQrUrl("https://localhost:3306/invoice/factura_" + idOrden + ".pdf");
                    factura.setVehiculo(rs.getString("vehiculo"));
                    factura.setNombreCliente(rs.getString("nombre_cliente"));
                    factura.setDocumentoCliente(rs.getString("documento_cliente"));
                    factura.setDireccionCliente(rs.getString("direccion_cliente"));
                    factura.setDescripcionServicio(rs.getString("descripcion_servicio"));
                    factura.setPrecioUnitario(subtotal); // Use subtotal as precioUnitario for now
                    factura.setEmail(rs.getString("email"));
                    factura.setTelefono(rs.getString("telefono"));
                    return factura;
                }
            }
        }
        return null;
    }
}