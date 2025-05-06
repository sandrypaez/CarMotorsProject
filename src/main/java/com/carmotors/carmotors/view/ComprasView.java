/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.carmotors.carmotors.view;


import com.carmotors.carmotors.controller.ComprasController;
import com.carmotors.carmotors.model.entities.Compras;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ComprasView extends JFrame {
    private JTextField txtProveedorId, txtProducto, txtCantidad, txtPrecioUnitario;
    private JComboBox<String> comboEstado;
    private JButton btnRegistrarCompra;

    public ComprasView() {
        setTitle("Registro de Compras");
        setSize(400, 300);
        setLayout(new GridLayout(6, 2, 5, 5));

        txtProveedorId = new JTextField();
        txtProducto = new JTextField();
        txtCantidad = new JTextField();
        txtPrecioUnitario = new JTextField();
        comboEstado = new JComboBox<>(new String[]{"Pendiente", "Recibido", "Cancelado"});
        btnRegistrarCompra = new JButton("Registrar Compra");

        add(new JLabel("ID del Proveedor:"));
        add(txtProveedorId);
        add(new JLabel("Producto:"));
        add(txtProducto);
        add(new JLabel("Cantidad:"));
        add(txtCantidad);
        add(new JLabel("Precio Unitario:"));
        add(txtPrecioUnitario);
        add(new JLabel("Estado:"));
        add(comboEstado);
        add(new JLabel(""));
        add(btnRegistrarCompra);

        btnRegistrarCompra.addActionListener(e -> registrarCompra());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void registrarCompra() {
        ComprasController controller = new ComprasController();
        Compras compra = new Compras();

        try {
            compra.setProveedorId(Integer.parseInt(txtProveedorId.getText()));
            compra.setProducto(txtProducto.getText());
            compra.setCantidad(Integer.parseInt(txtCantidad.getText()));
            compra.setPrecioUnitario(Double.parseDouble(txtPrecioUnitario.getText()));
            compra.setEstado(comboEstado.getSelectedItem().toString());
            compra.setFechaCompra(new Date());

            controller.registrarCompra(compra);
            JOptionPane.showMessageDialog(this, "Compra registrada correctamente.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ComprasView::new);
    }
}
