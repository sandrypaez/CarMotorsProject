package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.VehiculoDAO;
import com.carmotors.carmotors.model.entities.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class VehiculoController {
    private final VehiculoDAO vehiculoDAO;
    private final Connection conn;

    public VehiculoController(Connection conn) {
        this.conn = conn;
        this.vehiculoDAO = new VehiculoDAO(conn);
    }

    public void registrarVehiculo(Vehiculo vehiculo) throws SQLException {
        // Validate required fields
        if (vehiculo.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser mayor a 0");
        }
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo es obligatoria");
        }

        // Check if placa already exists
        if (existePlaca(vehiculo.getPlaca())) {
            throw new SQLException("La placa " + vehiculo.getPlaca() + " ya está registrada");
        }

        // Validate foreign key: Check if idCliente exists in Clientes table
        if (!existeCliente(vehiculo.getIdCliente())) {
            throw new SQLException("El cliente con ID " + vehiculo.getIdCliente() + " no existe");
        }

        vehiculoDAO.create(vehiculo);
    }

    public Vehiculo buscarVehiculoPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del vehículo debe ser mayor a 0");
        }
        return vehiculoDAO.read(id);
    }

    public Vehiculo buscarVehiculoPorPlaca(String placa) throws SQLException {
        if (placa == null || placa.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa no puede estar vacía");
        }
        String sql = "SELECT id_vehiculo, id_cliente, marca, modelo, placa, tipo_vehiculo FROM Vehiculos WHERE placa = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Vehiculo(
                            rs.getInt("id_vehiculo"),
                            rs.getInt("id_cliente"),
                            rs.getString("marca"),
                            rs.getString("modelo"),
                            rs.getString("placa"),
                            rs.getString("tipo_vehiculo")
                    );
                }
            }
        }
        return null;
    }

    public List<Vehiculo> listarTodosVehiculos() throws SQLException {
        return vehiculoDAO.readAll();
    }

    public void actualizarVehiculo(Vehiculo vehiculo) throws SQLException {
        // Validate required fields
        if (vehiculo.getId() <= 0) {
            throw new IllegalArgumentException("El ID del vehículo debe ser mayor a 0");
        }
        if (vehiculo.getIdCliente() <= 0) {
            throw new IllegalArgumentException("El ID del cliente debe ser mayor a 0");
        }
        if (vehiculo.getPlaca() == null || vehiculo.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo es obligatoria");
        }

        // Check if placa is already used by another vehicle
        Vehiculo existingVehiculo = buscarVehiculoPorPlaca(vehiculo.getPlaca());
        if (existingVehiculo != null && existingVehiculo.getId() != vehiculo.getId()) {
            throw new SQLException("La placa " + vehiculo.getPlaca() + " ya está registrada en otro vehículo");
        }

        // Validate foreign key: Check if idCliente exists in Clientes table
        if (!existeCliente(vehiculo.getIdCliente())) {
            throw new SQLException("El cliente con ID " + vehiculo.getIdCliente() + " no existe");
        }

        vehiculoDAO.update(vehiculo);
    }

    public void eliminarVehiculo(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del vehículo debe ser mayor a 0");
        }
        vehiculoDAO.delete(id);
    }

    public Connection getConnection() {
        return conn;
    }

    private boolean existePlaca(String placa) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Vehiculos WHERE placa = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, placa);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private boolean existeCliente(int idCliente) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idCliente);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}