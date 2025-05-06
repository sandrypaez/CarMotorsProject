package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Cliente;
import com.carmotors.carmotors.model.entities.OrdenServicio;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection conn;

    public ClienteDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Cliente cliente) throws SQLException {
        if (cliente == null) {
            throw new SQLException("El cliente no puede ser nulo");
        }
        String sql = "INSERT INTO Clientes (nombre, identificacion, telefono, correo_electronico, direccion, discount_percentage, reward_points) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getIdentificacion());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getCorreoElectronico());
            pstmt.setString(5, cliente.getDireccion());
            pstmt.setDouble(6, cliente.getDiscountPercentage());
            pstmt.setInt(7, cliente.getRewardPoints());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Cliente> readAll() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY nombre";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setIdentificacion(rs.getString("identificacion"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setCorreoElectronico(rs.getString("correo_electronico"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setDiscountPercentage(rs.getDouble("discount_percentage"));
                cliente.setRewardPoints(rs.getInt("reward_points"));
                cliente.setServiceHistory(getOrdenesServicioPorCliente(cliente.getId()));
                clientes.add(cliente);
            }
        }
        return clientes;
    }

    public void update(Cliente cliente) throws SQLException {
        if (cliente == null) {
            throw new SQLException("El cliente no puede ser nulo");
        }
        String sql = "UPDATE Clientes SET nombre = ?, identificacion = ?, telefono = ?, correo_electronico = ?, direccion = ?, discount_percentage = ?, reward_points = ? WHERE id_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getIdentificacion());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getCorreoElectronico());
            pstmt.setString(5, cliente.getDireccion());
            pstmt.setObject(6, cliente.getFechaCompra() != null ? Date.valueOf(cliente.getFechaCompra()) : null);
            pstmt.setDouble(7, cliente.getDiscountPercentage());
            pstmt.setInt(8, cliente.getRewardPoints());
            pstmt.setInt(9, cliente.getId());
            pstmt.executeUpdate();
        }
    }

    public Cliente read(int id) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente();
                    cliente.setId(rs.getInt("id_cliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setIdentificacion(rs.getString("identificacion"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setCorreoElectronico(rs.getString("correo_electronico"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setDiscountPercentage(rs.getDouble("discount_percentage"));
                    cliente.setRewardPoints(rs.getInt("reward_points"));
                    cliente.setServiceHistory(getOrdenesServicioPorCliente(id));
                    return cliente;
                }
            }
        }
        return null;
    }

    public List<OrdenServicio> getOrdenesServicioPorCliente(int clientId) throws SQLException {
        List<OrdenServicio> history = new ArrayList<>();
        String sql = "SELECT os.* " +
                "FROM OrdenesServicio os " +
                "JOIN Vehiculos v ON os.id_vehiculo = v.id_vehiculo " +
                "WHERE v.id_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrdenServicio service = new OrdenServicio();
                    service.setId(rs.getInt("id_orden"));
                    service.setIdVehiculo(rs.getInt("id_vehiculo"));
                    service.setIdServicio(rs.getInt("id_servicio"));
                    service.setEstado(rs.getString("estado"));
                    service.setFechaInicio(rs.getDate("fecha_inicio"));
                    service.setFechaFin(rs.getDate("fecha_fin"));
                    service.setRecordatorioFecha(rs.getDate("recordatorio_fecha"));
                    service.setRecordatorioEnviado(rs.getBoolean("recordatorio_enviado"));
                    history.add(service);
                }
            }
        }
        return history;
    }
}