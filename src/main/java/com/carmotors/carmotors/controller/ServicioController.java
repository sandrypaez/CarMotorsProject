package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.ServicioDAO;
import com.carmotors.carmotors.model.entities.Servicio;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServicioController {
    private final ServicioDAO servicioDAO;
    private final Connection conn;

    public ServicioController(Connection conn) {
        this.conn = conn;
        this.servicioDAO = new ServicioDAO(conn);
    }

    public void registrarServicio(Servicio servicio) throws SQLException {
        if (servicio.getTipo() == null || (!servicio.getTipo().equals("Preventivo") && !servicio.getTipo().equals("Correctivo"))) {
            throw new IllegalArgumentException("El tipo debe ser 'Preventivo' o 'Correctivo'");
        }
        if (servicio.getCostoManoObra() == null || servicio.getCostoManoObra().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de mano de obra debe ser no negativo");
        }
        if (servicio.getTiempoEstimado() <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor a 0");
        }
        servicioDAO.create(servicio);
    }

    public Servicio buscarServicioPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del servicio debe ser mayor a 0");
        }
        return servicioDAO.read(id);
    }

    public List<Servicio> listarTodosServicios() throws SQLException {
        return servicioDAO.readAll();
    }

    public void actualizarServicio(Servicio servicio) throws SQLException {
        if (servicio.getIdServicio() <= 0) {
            throw new IllegalArgumentException("El ID del servicio debe ser mayor a 0");
        }
        if (servicio.getTipo() == null || (!servicio.getTipo().equals("Preventivo") && !servicio.getTipo().equals("Correctivo"))) {
            throw new IllegalArgumentException("El tipo debe ser 'Preventivo' o 'Correctivo'");
        }
        if (servicio.getCostoManoObra() == null || servicio.getCostoManoObra().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de mano de obra debe ser no negativo");
        }
        if (servicio.getTiempoEstimado() <= 0) {
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor a 0");
        }
        servicioDAO.update(servicio);
    }

    public void eliminarServicio(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID del servicio debe ser mayor a 0");
        }
        servicioDAO.delete(id);
    }

    public Connection getConnection() {
        return conn;
    }
}