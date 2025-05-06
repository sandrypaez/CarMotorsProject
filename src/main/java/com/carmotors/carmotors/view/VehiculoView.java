package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.VehiculoController;
import com.carmotors.carmotors.model.entities.Vehiculo;
import com.carmotors.carmotors.model.dao.ConexionDB;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

public class VehiculoView extends JFrame {
    private VehiculoController controller;  // Se quitó 'final'
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JTextArea areaListado = new JTextArea();

    public VehiculoView() {
        // Initialize controller with connection from ConexionDB
        try {
            Connection conn = ConexionDB.getConnection();
            this.controller = new VehiculoController(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
            return;
        }

        setTitle("CarMotors - Gestión de Vehículos");
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
        JButton btnEliminar = new JButton("🗑 Eliminar");

        for (JButton btn : new JButton[]{btnRegistrar, btnListar, btnBuscar, btnActualizar, btnEliminar}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            menu.add(btn);
        }

        btnRegistrar.addActionListener(e -> cardLayout.show(cardPanel, "registrar"));
        btnListar.addActionListener(e -> {
            cardLayout.show(cardPanel, "listar");
            cargarVehiculos();
        });
        btnBuscar.addActionListener(e -> cardLayout.show(cardPanel, "buscar"));
        btnActualizar.addActionListener(e -> cardLayout.show(cardPanel, "actualizar"));
        btnEliminar.addActionListener(e -> cardLayout.show(cardPanel, "eliminar"));

        cardPanel.add(crearPanelRegistro(), "registrar");
        cardPanel.add(crearPanelListado(), "listar");
        cardPanel.add(crearPanelBuscar(), "buscar");
        cardPanel.add(crearPanelActualizar(), "actualizar");
        cardPanel.add(crearPanelEliminar(), "eliminar");

        add(menu, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "registrar");
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Vehículo"));
        panel.setBackground(Color.WHITE);

        JTextField txtIdCliente = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtPlaca = new JTextField();
        JTextField txtTipo = new JTextField();
        JButton btnGuardar = new JButton("✅ Guardar");

        panel.add(new JLabel("ID Cliente:")); panel.add(txtIdCliente);
        panel.add(new JLabel("Marca:")); panel.add(txtMarca);
        panel.add(new JLabel("Modelo:")); panel.add(txtModelo);
        panel.add(new JLabel("Placa:")); panel.add(txtPlaca);
        panel.add(new JLabel("Tipo:")); panel.add(txtTipo);
        panel.add(new JLabel()); panel.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
                vehiculo.setMarca(txtMarca.getText());
                vehiculo.setModelo(txtModelo.getText());
                vehiculo.setPlaca(txtPlaca.getText());
                vehiculo.setTipo(txtTipo.getText());

                controller.registrarVehiculo(vehiculo);

                JOptionPane.showMessageDialog(this, "✅ Vehículo registrado con ID: " + vehiculo.getId());
                txtIdCliente.setText("");
                txtMarca.setText("");
                txtModelo.setText("");
                txtPlaca.setText("");
                txtTipo.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Vehículos Registrados"));
        areaListado.setEditable(false);
        panel.add(new JScrollPane(areaListado), BorderLayout.CENTER);
        return panel;
    }

    private void cargarVehiculos() {
        try {
            List<Vehiculo> lista = controller.listarTodosVehiculos();
            areaListado.setText("");
            for (Vehiculo v : lista) {
                areaListado.append("ID: " + v.getId() +
                        ", ID Cliente: " + v.getIdCliente() +
                        ", Marca: " + v.getMarca() +
                        ", Modelo: " + v.getModelo() +
                        ", Placa: " + v.getPlaca() +
                        ", Tipo: " + v.getTipo() + "\n");
            }
        } catch (Exception e) {
            areaListado.setText("❌ Error: " + e.getMessage());
        }
    }

    private JPanel crearPanelBuscar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Vehículo"));

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
                Vehiculo v = controller.buscarVehiculoPorId(id);
                if (v != null) {
                    resultado.setText("ID: " + v.getId() +
                            "\nID Cliente: " + v.getIdCliente() +
                            "\nMarca: " + v.getMarca() +
                            "\nModelo: " + v.getModelo() +
                            "\nPlaca: " + v.getPlaca() +
                            "\nTipo: " + v.getTipo());
                } else {
                    resultado.setText("Vehículo no encontrado.");
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
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Actualizar Vehículo"));

        JTextField txtId = new JTextField();
        JTextField txtIdCliente = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtPlaca = new JTextField();
        JTextField txtTipo = new JTextField();
        JButton btnCargar = new JButton("📥 Cargar");
        JButton btnActualizar = new JButton("💾 Actualizar");

        panel.add(new JLabel("ID Vehículo:")); panel.add(txtId);
        panel.add(new JLabel()); panel.add(btnCargar);
        panel.add(new JLabel("ID Cliente:")); panel.add(txtIdCliente);
        panel.add(new JLabel("Marca:")); panel.add(txtMarca);
        panel.add(new JLabel("Modelo:")); panel.add(txtModelo);
        panel.add(new JLabel("Placa:")); panel.add(txtPlaca);
        panel.add(new JLabel("Tipo:")); panel.add(txtTipo);
        panel.add(new JLabel()); panel.add(btnActualizar);

        btnCargar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Vehiculo v = controller.buscarVehiculoPorId(id);
                if (v != null) {
                    txtIdCliente.setText(String.valueOf(v.getIdCliente()));
                    txtMarca.setText(v.getMarca());
                    txtModelo.setText(v.getModelo());
                    txtPlaca.setText(v.getPlaca());
                    txtTipo.setText(v.getTipo());
                } else {
                    JOptionPane.showMessageDialog(this, "Vehículo no encontrado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Vehiculo v = controller.buscarVehiculoPorId(id);
                if (v != null) {
                    v.setIdCliente(Integer.parseInt(txtIdCliente.getText()));
                    v.setMarca(txtMarca.getText());
                    v.setModelo(txtModelo.getText());
                    v.setPlaca(txtPlaca.getText());
                    v.setTipo(txtTipo.getText());
                    controller.actualizarVehiculo(v);
                    JOptionPane.showMessageDialog(this, "✅ Vehículo actualizado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Eliminar Vehículo"));

        JTextField txtId = new JTextField();
        JButton btnEliminar = new JButton("🗑 Eliminar");

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Ingrese ID:"), BorderLayout.WEST);
        top.add(txtId, BorderLayout.CENTER);
        top.add(btnEliminar, BorderLayout.EAST);

        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                controller.eliminarVehiculo(id);
                JOptionPane.showMessageDialog(this, "✅ Vehículo eliminado.");
                txtId.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        panel.add(top, BorderLayout.NORTH);
        return panel;
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new VehiculoView().setVisible(true));
    }
}
