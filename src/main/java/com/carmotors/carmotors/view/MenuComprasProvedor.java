package com.carmotors.carmotors.view;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;

public class MenuComprasProvedor extends JFrame {

    public MenuComprasProvedor() {
        setTitle("CarMotors - Menú Principal");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Encabezado
        JLabel titulo = new JLabel("🚗 CarMotors", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(25, 118, 210));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titulo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        panel.setBackground(Color.decode("#E3F2FD"));

        JButton btnCliente = new JButton("👤  Compras");
        JButton btnFactura = new JButton("🧾  Proveedor");

        estilizarBoton(btnCliente);
        estilizarBoton(btnFactura);

        panel.add(btnCliente);
        panel.add(btnFactura);
        add(panel, BorderLayout.CENTER);

        // Acciones
        btnCliente.addActionListener(e -> new ComprasView().setVisible(true));
        btnFactura.addActionListener(e -> new ProveedorView().setVisible(true));
    }

    private void estilizarBoton(JButton boton) {
        boton.setBackground(new Color(33, 150, 243));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        JButton btnAtras = new JButton("🔙 Volver al Menú");
        btnAtras.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnAtras.setFocusPainted(false);
        btnAtras.setBackground(new Color(200, 230, 201));
        btnAtras.setForeground(Color.BLACK);

        btnAtras.addActionListener(e -> {
            this.dispose(); // Cierra la ventana actual
            new MenuPrincipal().setVisible(false); // Abre el menú principal
        });

        add(btnAtras, BorderLayout.SOUTH); // O donde lo quieras posicionar

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error al aplicar estilo FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new MenuComprasProvedor().setVisible(false));
    }
}
