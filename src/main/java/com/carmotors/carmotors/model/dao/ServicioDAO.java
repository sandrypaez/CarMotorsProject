package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {
    private final Connection conn;

    public ServicioDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Servicio servicio) throws SQLException {
        String sql = "INSERT INTO Servicios (tipo, descripcion, costo_mano_obra, tiempo_estimado) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, servicio.getTipo());
            pstmt.setString(2, servicio.getDescripcion());
            pstmt.setBigDecimal(3, servicio.getCostoManoObra());
            pstmt.setInt(4, servicio.getTiempoEstimado());
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    servicio.setIdServicio(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Servicio read(int id) throws SQLException {
        String sql = "SELECT id_servicio, tipo, descripcion, costo_mano_obra, tiempo_estimado FROM Servicios WHERE id_servicio = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Servicio(
                            rs.getInt("id_servicio"),
                            rs.getString("tipo"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("costo_mano_obra"),
                            rs.getInt("tiempo_estimado")
                    );
                }
            }
        }
        return null;
    }

    public List<Servicio> readAll() throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        String sql = "SELECT id_servicio, tipo, descripcion, costo_mano_obra, tiempo_estimado FROM Servicios";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                servicios.add(new Servicio(
                        rs.getInt("id_servicio"),
                        rs.getString("tipo"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("costo_mano_obra"),
                        rs.getInt("tiempo_estimado")
                ));
            }
        }
        return servicios;
    }

    public void update(Servicio servicio) throws SQLException {
        String sql = "UPDATE Servicios SET tipo = ?, descripcion = ?, costo_mano_obra = ?, tiempo_estimado = ? WHERE id_servicio = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, servicio.getTipo());
            pstmt.setString(2, servicio.getDescripcion());
            pstmt.setBigDecimal(3, servicio.getCostoManoObra());
            pstmt.setInt(4, servicio.getTiempoEstimado());
            pstmt.setInt(5, servicio.getIdServicio());
            pstmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM Servicios WHERE id_servicio = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}