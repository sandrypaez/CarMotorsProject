/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotors.carmotors.model.dao;

/**
 *
 * @author mariacamilaparrasierra
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.carmotors.carmotors.model.entities.Compras;

public class ComprasDAO {

    public void save(Compras compra) {
        String sql = "INSERT INTO compras (proveedor_id, producto, cantidad, precio_unitario, fecha_compra, estado) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, compra.getProveedorId());
            stmt.setString(2, compra.getProducto());
            stmt.setInt(3, compra.getCantidad());
            stmt.setDouble(4, compra.getPrecioUnitario());
            stmt.setDate(5, new java.sql.Date(compra.getFechaCompra().getTime()));
            stmt.setString(6, compra.getEstado());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Compras> findAll() {
        List<Compras> compras = new ArrayList<>();
        String sql = "SELECT * FROM compras";

        try (Connection conn = ConexionDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Compras c = new Compras();
                c.setId(rs.getInt("id"));
                c.setProveedorId(rs.getInt("proveedor_id"));
                c.setProducto(rs.getString("producto"));
                c.setCantidad(rs.getInt("cantidad"));
                c.setPrecioUnitario(rs.getDouble("precio_unitario"));
                c.setFechaCompra(rs.getDate("fecha_compra"));
                c.setEstado(rs.getString("estado"));
                compras.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compras;
    }
}
