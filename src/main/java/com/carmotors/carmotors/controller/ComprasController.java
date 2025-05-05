/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotors.carmotors.controller;

import com.carmotors.carmotors.model.dao.Compras;
import com.carmotors.carmotors.model.dao.ComprasDAO;
import java.util.List;
/**
 *
 * @author mariacamilaparrasierra
 */
public class ComprasController {

    private ComprasDAO dao = new ComprasDAO();

    public void registrarCompra(Compras compra) {
        dao.save(compra);
    }

    public List<Compras> obtenerCompras() {
        return dao.findAll();
    }
}

