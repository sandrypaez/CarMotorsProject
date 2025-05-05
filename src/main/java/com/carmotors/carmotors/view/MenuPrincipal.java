package com.carmotors.carmotors.view;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

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

        // Encabezado superior
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 118, 210));
        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titulo = new JLabel("🚗 CarMotors - Sistema de Gestión");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(Color.WHITE);
        titulo.setHorizontalAlignment(JLabel.CENTER);
        header.add(titulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(250, 250, 250));
        panelBotones.setLayout(new GridLayout(5, 1, 20, 20));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 120, 40, 120));

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

        add(panelBotones, BorderLayout.CENTER);

        // Footer con sombra
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(245, 245, 245));
        footer.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(200, 200, 200)));

        JLabel textoFooter = new JLabel("📱 Optimizado para móviles - CarMotors © 2025", JLabel.CENTER);
        textoFooter.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        textoFooter.setForeground(Color.DARK_GRAY);
        textoFooter.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        footer.add(textoFooter, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        // Acciones
        btnInventario.addActionListener(e -> new RepuestoView().setVisible(true));
        btnMantenimiento.addActionListener(e -> new FacturaView().setVisible(true));
        btnClientesFact.addActionListener(e -> new MainMenu().setVisible(true));
        btnProveedores.addActionListener(e -> new FacturaView().setVisible(true));
        btnReportes.addActionListener(e -> new FacturaView().setVisible(true));
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        boton.setFocusPainted(false);
        boton.setBackground(new Color(227, 242, 253));
        boton.setForeground(new Color(21, 101, 192));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(100, 181, 246), 2),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
        ));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(250, 45));
        return boton;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
