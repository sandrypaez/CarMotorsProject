package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {
    private final Connection conn;

    public VehiculoDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Vehiculo vehiculo) throws SQLException {
        String sql = "INSERT INTO Vehiculos (id_cliente, marca, modelo, placa, tipo_vehiculo, año) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, vehiculo.getIdCliente());
            pstmt.setString(2, vehiculo.getMarca());
            pstmt.setString(3, vehiculo.getModelo());
            pstmt.setString(4, vehiculo.getPlaca());
            pstmt.setString(5, vehiculo.getTipo());
            pstmt.setInt(6, 0); // año is not in Vehiculo.java, default to 0 or handle separately if needed
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vehiculo.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Vehiculo read(int id) throws SQLException {
        String sql = "SELECT id_vehiculo, id_cliente, marca, modelo, placa, tipo_vehiculo, año FROM Vehiculos WHERE id_vehiculo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
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

    public List<Vehiculo> readAll() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT id_vehiculo, id_cliente, marca, modelo, placa, tipo_vehiculo, año FROM Vehiculos";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                vehiculos.add(new Vehiculo(
                        rs.getInt("id_vehiculo"),
                        rs.getInt("id_cliente"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("placa"),
                        rs.getString("tipo_vehiculo")
                ));
            }
        }
        return vehiculos;
    }

    public void update(Vehiculo vehiculo) throws SQLException {
        String sql = "UPDATE Vehiculos SET id_cliente = ?, marca = ?, modelo = ?, placa = ?, tipo_vehiculo = ?, año = ? WHERE id_vehiculo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehiculo.getIdCliente());
            pstmt.setString(2, vehiculo.getMarca());
            pstmt.setString(3, vehiculo.getModelo());
            pstmt.setString(4, vehiculo.getPlaca());
            pstmt.setString(5, vehiculo.getTipo());
            pstmt.setInt(6, 0); // año is not in Vehiculo.java, default to 0 or handle separately
            pstmt.setInt(7, vehiculo.getId());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Vehiculos WHERE id_vehiculo = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}