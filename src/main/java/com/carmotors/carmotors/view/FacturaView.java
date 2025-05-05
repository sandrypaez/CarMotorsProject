package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.FacturaController;
import com.carmotors.carmotors.model.entities.Factura;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.UUID;

public class FacturaView extends JFrame {
    private final JTextField txtIdOrden = new JTextField(10);
    private final JTextField txtSubtotal = new JTextField(10);
    private final JTextField txtImpuestos = new JTextField(10);
    private final JTextField txtTotal = new JTextField(10);
    private final JTextField txtCUFE = new JTextField(25);
    private final JButton btnGenerar = new JButton("📄 Generar Factura PDF");
    private final JButton btnCargar = new JButton("🧾 Cargar Datos");

    private final FacturaController controller = new FacturaController();

    public FacturaView() {
        setTitle("Generar Factura Electrónica");
        setSize(500, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        txtSubtotal.setEditable(false);
        txtImpuestos.setEditable(false);
        txtTotal.setEditable(false);
        txtCUFE.setEditable(false);

        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#f0f8ff"));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel lblTitulo = new JLabel("🏫 Factura Electrónica");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(20));

        panel.add(createInputPanel("ID Orden:", txtIdOrden));
        panel.add(createInputPanel("Subtotal:", txtSubtotal));
        panel.add(createInputPanel("Impuestos:", txtImpuestos));
        panel.add(createInputPanel("Total:", txtTotal));
        panel.add(createInputPanel("CUFE:", txtCUFE));

        btnCargar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGenerar.setAlignmentX(Component.CENTER_ALIGNMENT);

        stylizeButton(btnCargar);
        stylizeButton(btnGenerar);

        panel.add(Box.createVerticalStrut(15));
        panel.add(btnCargar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(btnGenerar);

        add(panel);

        btnCargar.addActionListener((ActionEvent e) -> {
            try {
                int idOrden = Integer.parseInt(txtIdOrden.getText());
                Factura factura = controller.obtenerDatosFactura(idOrden);
                if (factura != null) {
                    txtSubtotal.setText(String.valueOf(factura.getSubtotal()));
                    txtImpuestos.setText(String.valueOf(factura.getImpuestos()));
                    txtTotal.setText(String.valueOf(factura.getTotal()));
                    String cufe = UUID.randomUUID().toString().replace("-", "");
                    txtCUFE.setText(cufe);
                    factura.setCufe(cufe);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Orden no encontrada.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "⚠️ Error al cargar: " + ex.getMessage());
            }
        });

        btnGenerar.addActionListener((ActionEvent e) -> {
            try {
                Factura factura = new Factura();
                factura.setIdOrden(Integer.parseInt(txtIdOrden.getText()));
                factura.setFechaEmision(new Date());
                factura.setSubtotal(Double.parseDouble(txtSubtotal.getText()));
                factura.setImpuestos(Double.parseDouble(txtImpuestos.getText()));
                factura.setTotal(Double.parseDouble(txtTotal.getText()));
                factura.setCufe(txtCUFE.getText());
                factura.setQrUrl("https://carmotors.com/factura?id=" + factura.getIdOrden());

                controller.generarFacturaPDF(factura);
                JOptionPane.showMessageDialog(this, "✅ Factura generada en 'invoices'");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });
    }

    private void stylizeButton(JButton button) {
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    }

    private JPanel createInputPanel(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(120, 25));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturaView().setVisible(true));
    }
}

              