package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Cliente;
import java.sql.*;
import java.util.*;
    
import com.carmotors.carmotors.model.entities.Cliente;
import com.carmotors.carmotors.model.entities.OrdenServicio;
import com.carmotors.carmotors.model.dao.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void registerClient(Cliente client) throws SQLException {
        if (client == null) {
            throw new SQLException("Client cannot be null");
        }
        String sql = "INSERT INTO Clientes (nombre, identificacion, telefono, correo_electronico) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getIdentification());
            pstmt.setString(3, client.getPhone());
            pstmt.setString(4, client.getEmail());

            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Cliente> listAllClients() throws SQLException {
        List<Cliente> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clientes ORDER BY nombre";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Cliente client = new Cliente();
                client.setId(rs.getInt("id_cliente"));
                client.setName(rs.getString("nombre"));
                client.setIdentification(rs.getString("identificacion"));
                client.setPhone(rs.getString("telefono"));
                client.setEmail(rs.getString("correo_electronico"));
                client.setDiscountPercentage(rs.getDouble("discount_percentage"));
                client.setRewardPoints(rs.getInt("reward_points"));
                client.setServiceHistory(getServiceHistory(client.getId()));
                clients.add(client);
            }
        }
        return clients;
    }
    public void updateClient(Cliente client) throws SQLException {
        if (client == null) {
            throw new SQLException("Client cannot be null");
        }
        String sql = "UPDATE Clientes SET nombre = ?, identificacion = ?, telefono = ?, correo_electronico = ?, discount_percentage = ?, reward_points = ? WHERE id_cliente = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getIdentification());
            pstmt.setString(3, client.getPhone());
            pstmt.setString(4, client.getEmail());
            pstmt.setDouble(5, client.getDiscountPercentage());
            pstmt.setInt(6, client.getRewardPoints());
            pstmt.setInt(7, client.getId());
            pstmt.executeUpdate();
        }
    }

    public Cliente findClientById(int id) throws SQLException {
        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Cliente client = new Cliente();
                    client.setId(rs.getInt("id_cliente"));
                    client.setName(rs.getString("nombre"));
                    client.setIdentification(rs.getString("identificacion"));
                    client.setPhone(rs.getString("telefono"));
                    client.setEmail(rs.getString("correo_electronico"));
                    client.setDiscountPercentage(rs.getDouble("discount_percentage"));
                    client.setRewardPoints(rs.getInt("reward_points"));
                    client.setServiceHistory(getServiceHistory(id));
                    return client;
                }
            }
        }
        return null;
    }

    private List<OrdenServicio> getServiceHistory(int clientId) throws SQLException {
        List<OrdenServicio> history = new ArrayList<>();
        String sql = "SELECT os.*, s.tipo, s.descripcion, s.costo_mano_obra " +
                     "FROM OrdenesServicio os " +
                     "JOIN Vehiculos v ON os.id_vehiculo = v.id_vehiculo " +
                     "JOIN Servicios s ON os.id_servicio = s.id_servicio " +
                     "WHERE v.id_cliente = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clientId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    OrdenServicio service = new OrdenServicio();
                    service.setId(rs.getInt("id_orden"));
                    service.setVehicleId(rs.getInt("id_vehiculo"));
                    service.setClientId(clientId);
                    service.setMaintenanceType(rs.getString("tipo"));
                    service.setDescription(rs.getString("descripcion"));
                    service.setLaborCost(rs.getDouble("costo_mano_obra"));
                    service.setStatus(rs.getString("estado"));
                    service.setStartDate(rs.getDate("fecha_inicio"));
                    service.setEndDate(rs.getDate("fecha_fin"));
                    history.add(service);
                }
            }
        }
        return history;
    }
}

