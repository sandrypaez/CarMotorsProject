package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.FacturaController;
import com.carmotors.carmotors.model.entities.Factura;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class FacturaView extends JFrame {
    private final JTextField txtIdOrden = new JTextField(10);
    private final JButton btnGenerar;
    private final JButton btnAtras;
    private final FacturaController controller = new FacturaController();
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public FacturaView() {
        setTitle("CarMotors - Generar Factura");
        setSize(350, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Toolbar for buttons
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize buttons
        btnGenerar = new JButton("✅ Generar Factura");
        btnAtras = new JButton("🔙 Volver al Menú");

        // Style buttons
        for (JButton btn : new JButton[]{btnGenerar, btnAtras}) {
            btn.setFocusPainted(false);
            btn.setBackground(VIBRANT_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(DEEP_BLUE, 1, true),
                    new EmptyBorder(5, 10, 5, 10)
            ));
            // Hover effect
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(HOVER_BLUE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(VIBRANT_BLUE);
                }
            });
            toolbar.add(btn);
            toolbar.add(Box.createHorizontalStrut(10)); // Space between buttons
        }

        // Main panel with gradient background
        JPanel center = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        center.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DEEP_BLUE),
                "Generar Factura",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        center.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Style text field
        txtIdOrden.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        txtIdOrden.setBackground(Color.WHITE);
        txtIdOrden.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtIdOrden.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtIdOrden.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });

        // Layout components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(new JLabel("ID Orden:"), gbc);
        gbc.gridx = 1;
        center.add(txtIdOrden, gbc);

        // Button actions
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
                String email = controller.obtenerCorreoPorOrden(idOrden);
                JOptionPane.showMessageDialog(this, "✅ Factura generada y enviada al correo: " + (email != null && !email.isEmpty() ? email : "No disponible"), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtIdOrden.setText(""); // Clear field
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnAtras.addActionListener(e -> dispose());

        // Add components to frame
        add(toolbar, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FacturaView().setVisible(true));
    }
}