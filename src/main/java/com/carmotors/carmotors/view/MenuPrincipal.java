package com.carmotors.carmotors.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPrincipal extends JFrame {
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public MenuPrincipal() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf: " + e.getMessage());
        }

        setTitle("CarMotors - Menú Principal");
        setSize(750, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Toolbar (minimal, for consistency)
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(toolbar, BorderLayout.NORTH);

        // Encabezado superior
        JPanel header = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, DEEP_BLUE, 0, getHeight(), VIBRANT_BLUE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titulo = new JLabel("🚗 CarMotors - Sistema de Gestión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));
        add(panelBotones, BorderLayout.CENTER);

        JButton btnInventario = crearBoton("📦 Gestión de Inventarios");
        JButton btnMantenimiento = crearBoton("🔧 Mantenimiento y Reparaciones");
        JButton btnClientesFact = crearBoton("👤 Clientes y Facturación");
        JButton btnProveedores = crearBoton("🏬 Proveedores y Compras");
        JButton btnReportes = crearBoton("📊 Reportes y Estadísticas");

        panelBotones.add(btnInventario);
        panelBotones.add(btnMantenimiento);
        panelBotones.add(btnClientesFact);
        panelBotones.add(btnProveedores);
        panelBotones.add(btnReportes);

        // Footer con sombra
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(ACCENT_BLUE);
        footer.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, DEEP_BLUE));

        JLabel textoFooter = new JLabel("📱 Optimizado para móviles - CarMotors © 2025", JLabel.CENTER);
        textoFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        textoFooter.setForeground(DEEP_BLUE);
        textoFooter.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footer.add(textoFooter, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        // Acciones
        btnInventario.addActionListener(e -> new RepuestoView().setVisible(true));
        btnMantenimiento.addActionListener(e -> new MenuServicios().setVisible(true));
        btnClientesFact.addActionListener(e -> new MainMenu().setVisible(true));
        btnProveedores.addActionListener(e -> new MenuComprasProveedor().setVisible(true));
        btnReportes.addActionListener(e -> new ReporteView().setVisible(true));
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setFocusPainted(false);
        boton.setBackground(VIBRANT_BLUE);
        boton.setForeground(Color.WHITE);
        boton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 2, true),
                new EmptyBorder(12, 24, 12, 24)
        ));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(250, 45));
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
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}