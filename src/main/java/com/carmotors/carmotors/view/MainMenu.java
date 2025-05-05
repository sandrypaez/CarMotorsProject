package com.carmotors.carmotors.view;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {

    public MainMenu() {
        setTitle("🏠 Menú Principal - CarMotors");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setBackground(Color.decode("#e6f0ff"));  // azul muy claro
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel title = new JLabel("🚗 CarMotors - Sistema de Gestión");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCliente = new JButton("👤 Registro de Cliente");
        JButton btnFactura = new JButton("🧾 Generar Factura");

        stylizeButton(btnCliente);
        stylizeButton(btnFactura);

        panel.add(title);
        panel.add(Box.createVerticalStrut(30));
        panel.add(btnCliente);
        panel.add(Box.createVerticalStrut(15));
        panel.add(btnFactura);

        add(panel);

        // 👉 Acciones
        btnCliente.addActionListener(e -> {
            dispose(); // Cierra el menú
            SwingUtilities.invokeLater(() -> new ClienteView().setVisible(true));
        });

        btnFactura.addActionListener(e -> {
            dispose(); // Cierra el menú
            SwingUtilities.invokeLater(() -> new FacturaView().setVisible(true));
        });
    }

    private void stylizeButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setMaximumSize(new Dimension(220, 40));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
