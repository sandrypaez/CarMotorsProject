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
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new GridLayout(6, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JButton btnInventario = new JButton("📦 Gestión de Inventarios");
        JButton btnMantenimiento = new JButton("🔧 Mantenimiento y Reparaciones");
        JButton btnClientesFact = new JButton("👤 Clientes y Facturación");
        JButton btnProveedores = new JButton("🏬 Proveedores y Compras");
        JButton btnReportes = new JButton("📊 Reportes y Estadísticas");

        JButton[] botones = {btnInventario, btnMantenimiento, btnClientesFact, btnProveedores, btnReportes};
        for (JButton btn : botones) {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setFocusPainted(false);
            btn.setBackground(new Color(240, 240, 240));
            panel.add(btn);
        }

        btnInventario.addActionListener(e -> new FacturaView().setVisible(true));
        btnMantenimiento.addActionListener(e -> new FacturaView().setVisible(true));
        btnClientesFact.addActionListener(e -> new MainMenu().setVisible(true));
        btnProveedores.addActionListener(e -> new FacturaView().setVisible(true));
        btnReportes.addActionListener(e -> new FacturaView().setVisible(true));

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
