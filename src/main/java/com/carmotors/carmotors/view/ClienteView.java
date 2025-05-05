package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ClienteController;
import com.carmotors.carmotors.model.entities.Cliente;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClienteView extends JFrame {
    private final ClienteController controller = new ClienteController();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public ClienteView() {
        setTitle("CarMotors  - Gestión de Clientes");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(5, 1, 10, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menu.setBackground(new Color(245, 245, 245));

        JButton btnRegistrar = new JButton("📋 Registrar");
        JButton btnListar = new JButton("📑 Listar");
        JButton btnBuscar = new JButton("🔍 Buscar");
        JButton btnActualizar = new JButton("✏️ Actualizar");

        for (JButton btn : new JButton[]{btnRegistrar, btnListar, btnBuscar, btnActualizar}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            menu.add(btn);
        }

        btnRegistrar.addActionListener(e -> cardLayout.show(cardPanel, "registrar"));
        btnListar.addActionListener(e -> {
            cardLayout.show(cardPanel, "listar");
            cargarClientes();
        });
        btnBuscar.addActionListener(e -> cardLayout.show(cardPanel, "buscar"));
        btnActualizar.addActionListener(e -> cardLayout.show(cardPanel, "actualizar"));

        cardPanel.add(crearPanelRegistro(), "registrar");
        cardPanel.add(crearPanelListado(), "listar");
        cardPanel.add(crearPanelBuscar(), "buscar");
        cardPanel.add(crearPanelActualizar(), "actualizar");

        add(menu, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "registrar");
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Cliente"));
        panel.setBackground(Color.WHITE);

        JTextField txtNombre = new JTextField();
        JTextField txtId = new JTextField();
        JTextField txtTel = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPuntos = new JTextField();
        txtPuntos.setEditable(false);
        JTextField txtDescuento = new JTextField();
        txtDescuento.setEditable(false);
        JButton btnGuardar = new JButton("✅ Guardar");

        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Identificación:")); panel.add(txtId);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTel);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Puntos:")); panel.add(txtPuntos);
        panel.add(new JLabel("Descuento %:")); panel.add(txtDescuento);
        panel.add(new JLabel()); panel.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                Cliente cliente = new Cliente();
                cliente.setName(txtNombre.getText());
                cliente.setIdentification(txtId.getText());
                cliente.setPhone(txtTel.getText());
                cliente.setEmail(txtEmail.getText());
                cliente.setDiscountPercentage(0.0);
                cliente.setRewardPoints(0);

                controller.registerClient(cliente);

                txtPuntos.setText("0");
                txtDescuento.setText("0.0");

                JOptionPane.showMessageDialog(this, "✅ Cliente registrado con ID: " + cliente.getId());
                txtNombre.setText(""); txtId.setText(""); txtTel.setText(""); txtEmail.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JTextArea areaListado = new JTextArea();

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Clientes Registrados"));
        areaListado.setEditable(false);
        panel.add(new JScrollPane(areaListado), BorderLayout.CENTER);
        return panel;
    }

    private void cargarClientes() {
        try {
            List<Cliente> lista = controller.listAllClients();
            areaListado.setText("");
            for (Cliente c : lista) {
                areaListado.append("ID: " + c.getId() +
                        ", Nombre: " + c.getName() +
                        ", Tel: " + c.getPhone() +
                        ", Email: " + c.getEmail() +
                        ", Puntos: " + c.getRewardPoints() +
                        ", Descuento: " + c.getDiscountPercentage() + "%\n");
            }
        } catch (Exception e) {
            areaListado.setText("❌ Error: " + e.getMessage());
        }
    }

    private JPanel crearPanelBuscar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Cliente"));

        JTextField txtBuscar = new JTextField();
        JButton btnBuscar = new JButton("🔎 Buscar");
        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Ingrese ID:"), BorderLayout.WEST);
        top.add(txtBuscar, BorderLayout.CENTER);
        top.add(btnBuscar, BorderLayout.EAST);

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText());
                Cliente c = controller.findClientById(id);
                if (c != null) {
                    controller.aplicarBeneficios(c);
                    resultado.setText("ID: " + c.getId() +
                            "\nNombre: " + c.getName() +
                            "\nTeléfono: " + c.getPhone() +
                            "\nEmail: " + c.getEmail() +
                            "\nPuntos: " + c.getRewardPoints() +
                            "\nDescuento: " + c.getDiscountPercentage() + "%");
                } else {
                    resultado.setText("Cliente no encontrado.");
                }
            } catch (Exception ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultado), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelActualizar() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actualizar Cliente"));

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtIdent = new JTextField();
        JTextField txtTel = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPuntos = new JTextField();
        txtPuntos.setEditable(false);
        JTextField txtDescuento = new JTextField();
        txtDescuento.setEditable(false);

        JButton btnCargar = new JButton("📥 Cargar");
        JButton btnActualizar = new JButton("💾 Actualizar");

        panel.add(new JLabel("ID Cliente:")); panel.add(txtId);
        panel.add(new JLabel()); panel.add(btnCargar);
        panel.add(new JLabel("Nombre:")); panel.add(txtNombre);
        panel.add(new JLabel("Identificación:")); panel.add(txtIdent);
        panel.add(new JLabel("Teléfono:")); panel.add(txtTel);
        panel.add(new JLabel("Email:")); panel.add(txtEmail);
        panel.add(new JLabel("Puntos:")); panel.add(txtPuntos);
        panel.add(new JLabel("Descuento:")); panel.add(txtDescuento);
        panel.add(new JLabel()); panel.add(btnActualizar);

        btnCargar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Cliente c = controller.findClientById(id);
                if (c != null) {
                    controller.aplicarBeneficios(c);
                    txtNombre.setText(c.getName());
                    txtIdent.setText(c.getIdentification());
                    txtTel.setText(c.getPhone());
                    txtEmail.setText(c.getEmail());
                    txtPuntos.setText(String.valueOf(c.getRewardPoints()));
                    txtDescuento.setText(c.getDiscountPercentage() + "%");
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente no encontrado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Cliente c = controller.findClientById(id);
                if (c != null) {
                    c.setName(txtNombre.getText());
                    c.setIdentification(txtIdent.getText());
                    c.setPhone(txtTel.getText());
                    c.setEmail(txtEmail.getText());
                    controller.updateClient(c);
                    JOptionPane.showMessageDialog(this, "✅ Cliente actualizado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new ClienteView().setVisible(true));
    }
}