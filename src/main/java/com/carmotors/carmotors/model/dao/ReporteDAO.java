package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.model.entities.Reporte;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {
    private Connection conn;

    public ReporteDAO(Connection conn) {
        this.conn = conn;
    }

    public void create(Reporte reporte) throws SQLException {
        String sql = "INSERT INTO Reportes (tipo_reporte, descripcion, fecha_generacion, datos) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, reporte.getTipoReporte());
            pstmt.setString(2, reporte.getDescripcion());
            pstmt.setObject(3, Date.valueOf(reporte.getFechaGeneracion()));
            pstmt.setString(4, reporte.getDatos());
            pstmt.executeUpdate();
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    reporte.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<Reporte> readAll() throws SQLException {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM Reportes ORDER BY fecha_generacion DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Reporte reporte = new Reporte();
                reporte.setId(rs.getInt("id_reporte"));
                reporte.setTipoReporte(rs.getString("tipo_reporte"));
                reporte.setDescripcion(rs.getString("descripcion"));
                reporte.setFechaGeneracion(rs.getDate("fecha_generacion").toLocalDate());
                reporte.setDatos(rs.getString("datos"));
                reportes.add(reporte);
            }
        }
        return reportes;
    }

    public List<Reporte> readByTipo(String tipoReporte) throws SQLException {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM Reportes WHERE tipo_reporte = ? ORDER BY fecha_generacion DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tipoReporte);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reporte reporte = new Reporte();
                    reporte.setId(rs.getInt("id_reporte"));
                    reporte.setTipoReporte(rs.getString("tipo_reporte"));
                    reporte.setDescripcion(rs.getString("descripcion"));
                    reporte.setFechaGeneracion(rs.getDate("fecha_generacion").toLocalDate());
                    reporte.setDatos(rs.getString("datos"));
                    reportes.add(reporte);
                }
            }
        }
        return reportes;
    }
}