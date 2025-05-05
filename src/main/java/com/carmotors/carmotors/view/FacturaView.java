package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.FacturaController;
import com.carmotors.carmotors.model.entities.Factura;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class FacturaView extends JFrame {
    public FacturaView() {
        setTitle("Generar Factura");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTextField txtOrden = new JTextField(10);
        JButton btnGenerar = new JButton("Generar Factura");

        btnGenerar.addActionListener((ActionEvent e) -> {
            try {
                int ordenId = Integer.parseInt(txtOrden.getText());
                Factura factura = new Factura();
                factura.setIdOrden(ordenId);
                factura.setFechaEmision(new Date());
                factura.setSubtotal(100000);
                factura.setImpuestos(19000);
                factura.setTotal(119000);
                factura.setCufe("CUFE123456");
                factura.setQrUrl("https://carmotors.com/factura?id=" + ordenId);

                new FacturaController().generarFacturaPDF(factura);
                JOptionPane.showMessageDialog(this, "✅ Factura generada correctamente.");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("ID Orden:"));
        panel.add(txtOrden);
        panel.add(btnGenerar);

        add(panel);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturaView().setVisible(true));
    }
}
