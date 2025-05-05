package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.FacturaController;
import com.carmotors.carmotors.model.entities.Factura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;

public class FacturaView extends JFrame {

    private final JTextField txtIdOrden = new JTextField(10);
    private final JButton btnGenerar = new JButton("Generar Factura");

    private final FacturaController controller = new FacturaController();

    public FacturaView() {
        setTitle("Generar Factura");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JButton btnAtras = new JButton("🔙 Volver al Menú");
        btnAtras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAtras.setFocusPainted(false);
        btnAtras.setBackground(new Color(200, 230, 201));
        btnAtras.setForeground(Color.BLACK);

        btnAtras.addActionListener(e -> {
            this.dispose(); // Cierra la ventana actual
            new MenuPrincipal().setVisible(true); // Abre el menú principal
        });

        add(btnAtras, BorderLayout.SOUTH); // O donde lo quieras posicionar

        JLabel titulo = new JLabel("Generar Factura", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(titulo, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(new JLabel("ID Orden:"), gbc);
        gbc.gridx = 1;
        center.add(txtIdOrden, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        btnGenerar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        center.add(btnGenerar, gbc);

        add(center, BorderLayout.CENTER);

        btnGenerar.addActionListener((ActionEvent e) -> {
            try {
                int idOrden = Integer.parseInt(txtIdOrden.getText().trim());
                Factura factura = controller.obtenerDatosFactura(idOrden);
                if (factura == null) {
                    throw new Exception("Orden no encontrada");
                }
                factura.setIdOrden(idOrden);
                factura.setFechaEmision(new Date());
                factura.setQrUrl("https://carmotors.com/invoice/INV-" + factura.getIdOrden());
                controller.generarFacturaPDF(factura);
                JOptionPane.showMessageDialog(this, "✅ Factura generada y enviada al correo: " + factura.getEmail(), "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturaView().setVisible(true));
    }
}
