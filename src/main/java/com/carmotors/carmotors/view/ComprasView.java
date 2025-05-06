package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ComprasController;
import com.carmotors.carmotors.model.entities.Compras;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

public class ComprasView extends JFrame {
    private JTextField txtProveedorId, txtProducto, txtCantidad, txtPrecioUnitario;
    private JComboBox<String> comboEstado;
    private JButton btnRegistrarCompra, btnAtras;
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public ComprasView() {
        setTitle("CarMotors - Registro de Compras");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with gradient background
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DEEP_BLUE),
                "Registrar Compra",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Toolbar for buttons
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Initialize components
        txtProveedorId = new JTextField();
        txtProducto = new JTextField();
        txtCantidad = new JTextField();
        txtPrecioUnitario = new JTextField();
        comboEstado = new JComboBox<>(new String[]{"Pendiente", "Recibido", "Cancelado"});
        btnRegistrarCompra = new JButton("✅ Registrar Compra");
        btnAtras = new JButton("⬅ Atrás");

        // Style text fields
        for (JTextField txt : new JTextField[]{txtProveedorId, txtProducto, txtCantidad, txtPrecioUnitario}) {
            txt.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            txt.setBackground(Color.WHITE);
            txt.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    txt.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    txt.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
                }
            });
        }

        // Style combo box
        comboEstado.setBackground(Color.WHITE);
        comboEstado.setForeground(DEEP_BLUE);
        comboEstado.setBorder(new LineBorder(ACCENT_BLUE, 1, true));

        // Style buttons
        for (JButton btn : new JButton[]{btnRegistrarCompra, btnAtras}) {
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

        // Add components to main panel
        mainPanel.add(new JLabel("ID del Proveedor:"));
        mainPanel.add(txtProveedorId);
        mainPanel.add(new JLabel("Producto:"));
        mainPanel.add(txtProducto);
        mainPanel.add(new JLabel("Cantidad:"));
        mainPanel.add(txtCantidad);
        mainPanel.add(new JLabel("Precio Unitario:"));
        mainPanel.add(txtPrecioUnitario);
        mainPanel.add(new JLabel("Estado:"));
        mainPanel.add(comboEstado);

        // Button actions
        btnRegistrarCompra.addActionListener(e -> registrarCompra());
        btnAtras.addActionListener(e -> dispose());

        // Add toolbar and main panel to frame
        add(toolbar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

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
            JOptionPane.showMessageDialog(this, "✅ Compra registrada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            // Clear fields
            txtProveedorId.setText("");
            txtProducto.setText("");
            txtCantidad.setText("");
            txtPrecioUnitario.setText("");
            comboEstado.setSelectedIndex(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ComprasView::new);
    }
}