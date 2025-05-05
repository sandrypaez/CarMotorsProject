package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.ProveedorDAO;
import com.carmotors.carmotors.model.entities.Proveedor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProveedorController {
    private ProveedorDAO proveedorDAO;

    public ProveedorController(Connection conn) {
        this.proveedorDAO = new ProveedorDAO(conn);
    }

    public void addProveedor(Proveedor proveedor) throws SQLException {
        proveedorDAO.create(proveedor);
    }

    public Proveedor getProveedor(int id) throws SQLException {
        return proveedorDAO.read(id);
    }

    public List<Proveedor> getAllProveedores() throws SQLException {
        return proveedorDAO.readAll();
    }

    public void updateProveedor(Proveedor proveedor) throws SQLException {
        proveedorDAO.update(proveedor);
    }

    public void deleteProveedor(int id) throws SQLException {
        proveedorDAO.delete(id);
    }
}