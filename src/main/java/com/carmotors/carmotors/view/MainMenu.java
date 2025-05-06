package com.carmotors.carmotors.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JFrame {
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public MainMenu() {
        setTitle("CarMotors - Menú Principal");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Toolbar (optional, kept minimal)
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(toolbar, BorderLayout.NORTH);

        // Encabezado
        JLabel titulo = new JLabel("🚗 CarMotors", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(DEEP_BLUE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.NORTH);

        // Panel de botones with gradient background
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        add(panelBotones, BorderLayout.CENTER);

        // Buttons
        JButton btnCliente = new JButton("👤 Registrar Cliente");
        JButton btnFactura = new JButton("🧾 Generar Factura");
        JButton btnVehiculo = new JButton("🚘 Registrar Vehículo");

        // Style and add option buttons
        for (JButton btn : new JButton[]{btnCliente, btnFactura, btnVehiculo}) {
            estilizarBoton(btn);
            panelBotones.add(btn);
        }

        // Panel for "Volver al Menú" button
        JPanel panelVolver = new JPanel(new FlowLayout(FlowLayout.CENTER)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelVolver.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));
        add(panelVolver, BorderLayout.SOUTH);

        // "Volver al Menú" button
        JButton btnVolver = new JButton("🔙 Volver al Menú");
        estilizarBoton(btnVolver);
        panelVolver.add(btnVolver);

        // Acciones
        btnCliente.addActionListener(e -> new ClienteView().setVisible(true));
        btnFactura.addActionListener(e -> new FacturaView().setVisible(true));
        btnVehiculo.addActionListener(e -> new VehiculoView().setVisible(true));
        btnVolver.addActionListener(e -> {
            this.dispose(); // Close current window
            // No action needed to "return" to MainMenu since it's already the main menu
        });
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(VIBRANT_BLUE);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Hover effect
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(VIBRANT_BLUE);
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error al aplicar estilo FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}