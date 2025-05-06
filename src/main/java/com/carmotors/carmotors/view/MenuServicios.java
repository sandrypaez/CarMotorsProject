package com.carmotors.carmotors.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuServicios extends JFrame {
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public MenuServicios() {
        setTitle("CarMotors - Menú Servicios");
        setSize(420, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Toolbar with "Volver al Menú" button
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAtras = new JButton("🔙 Volver al Menú");
        btnAtras.setFocusPainted(false);
        btnAtras.setBackground(VIBRANT_BLUE);
        btnAtras.setForeground(Color.WHITE);
        btnAtras.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtras.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        btnAtras.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAtras.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnAtras.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnAtras.setBackground(VIBRANT_BLUE);
            }
        });
        btnAtras.addActionListener(e -> {
            this.dispose();
        });
        toolbar.add(btnAtras);
        add(toolbar, BorderLayout.NORTH);

        // Encabezado
        JLabel titulo = new JLabel("🚗 CarMotors", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(DEEP_BLUE);
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titulo, BorderLayout.CENTER);

        // Panel de botones with gradient background
        JPanel panel = new JPanel(new GridLayout(2, 1, 20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Buttons
        JButton btnOrden = new JButton("🧾 Registrar Orden de Servicio");
        JButton btnServicio = new JButton("🔧 Registrar Servicio");

        // Style buttons
        for (JButton btn : new JButton[]{btnOrden, btnServicio}) {
            estilizarBoton(btn);
            panel.add(btn);
        }

        add(panel, BorderLayout.SOUTH);

        // Acciones
        btnServicio.addActionListener(e -> new ServicioView().setVisible(true));
        btnOrden.addActionListener(e -> new OrdenServicioView().setVisible(true));
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

        SwingUtilities.invokeLater(() -> new MenuServicios().setVisible(true));
    }
}