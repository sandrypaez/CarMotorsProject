package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.OrdenServicioDAO;
import com.carmotors.carmotors.model.entities.OrdenServicio;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrdenServicioController {
    private OrdenServicioDAO ordenServicioDAO;
    private Connection conn;

    public OrdenServicioController(Connection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("La conexión a la base de datos no puede ser nula");
        }
        this.conn = conn;
        this.ordenServicioDAO = new OrdenServicioDAO(conn);
    }

    public void registrarOrdenServicio(OrdenServicio orden) throws Exception {
        validarOrdenServicio(orden);
        ordenServicioDAO.create(orden);
    }

    public List<OrdenServicio> listarTodasOrdenes() throws SQLException {
        return ordenServicioDAO.readAll();
    }

    public void actualizarOrdenServicio(OrdenServicio orden) throws Exception {
        validarOrdenServicio(orden);
        ordenServicioDAO.update(orden);
    }

    public OrdenServicio buscarOrdenServicioPorId(int id) throws SQLException {
        return ordenServicioDAO.read(id);
    }

    public void eliminarOrdenServicio(int id) throws SQLException {
        ordenServicioDAO.delete(id);
    }

    public void asignarTecnico(int idOrden, int idTecnico) throws SQLException {
        ordenServicioDAO.assignTecnico(idOrden, idTecnico);
    }

    public List<Integer> obtenerTecnicosPorOrden(int idOrden) throws SQLException {
        return ordenServicioDAO.getTecnicosForOrden(idOrden);
    }

    private void validarOrdenServicio(OrdenServicio orden) throws Exception {
        if (orden.getIdVehiculo() <= 0) {
            throw new Exception("El ID del vehículo debe ser un valor positivo");
        }
        if (orden.getIdServicio() <= 0) {
            throw new Exception("El ID del servicio debe ser un valor positivo");
        }
        if (orden.getEstado() == null || orden.getEstado().trim().isEmpty()) {
            throw new Exception("El estado de la orden no puede estar vacío");
        }
        if (!List.of("Pendiente", "En proceso", "Completado", "Entregado").contains(orden.getEstado())) {
            throw new Exception("El estado debe ser uno de: Pendiente, En proceso, Completado, Entregado");
        }
        if (orden.getFechaInicio() == null) {
            throw new Exception("La fecha de inicio no puede estar vacía");
        }
    }
}