package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ServicioController;
import com.carmotors.carmotors.model.entities.Servicio;
import com.carmotors.carmotors.model.dao.ConexionDB;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ServicioView extends JFrame {
    private ServicioController controller;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea areaListado;

    public ServicioView() {
        // Initialize controller with connection from ConexionDB
        Connection conn = null;
        try {
            conn = ConexionDB.getConnection();
            this.controller = new ServicioController(conn);

            // Initialize UI components
            this.cardLayout = new CardLayout();
            this.cardPanel = new JPanel(cardLayout);
            this.areaListado = new JTextArea();

            setTitle("CarMotors - Gestión de Servicios");
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
                cargarServicios();
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "❌ Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
            return;
        }
    }

    private JPanel crearPanelRegistro() {
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Registrar Servicio"));
        panel.setBackground(Color.WHITE);

        JTextField txtTipo = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtCosto = new JTextField("0.00");
        JTextField txtTiempo = new JTextField();
        JButton btnGuardar = new JButton("✅ Guardar");

        panel.add(new JLabel("Tipo (Preventivo/Correctivo):")); panel.add(txtTipo);
        panel.add(new JLabel("Descripción:")); panel.add(txtDescripcion);
        panel.add(new JLabel("Costo Mano de Obra:")); panel.add(txtCosto);
        panel.add(new JLabel("Tiempo Estimado (horas):")); panel.add(txtTiempo);
        panel.add(new JLabel()); panel.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                Servicio servicio = new Servicio();
                servicio.setTipo(txtTipo.getText());
                servicio.setDescripcion(txtDescripcion.getText());
                servicio.setCostoManoObra(new BigDecimal(txtCosto.getText()));
                servicio.setTiempoEstimado(Integer.parseInt(txtTiempo.getText()));

                controller.registrarServicio(servicio);

                JOptionPane.showMessageDialog(this, "✅ Servicio registrado con ID: " + servicio.getIdServicio());
                txtTipo.setText("");
                txtDescripcion.setText("");
                txtCosto.setText("0.00");
                txtTiempo.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Servicios Registrados"));
        areaListado.setEditable(false);
        panel.add(new JScrollPane(areaListado), BorderLayout.CENTER);
        return panel;
    }

    private void cargarServicios() {
        try {
            List<Servicio> lista = controller.listarTodosServicios();
            areaListado.setText("");
            for (Servicio s : lista) {
                areaListado.append("ID: " + s.getIdServicio() +
                        ", Tipo: " + s.getTipo() +
                        ", Descripción: " + s.getDescripcion() +
                        ", Costo: " + s.getCostoManoObra() +
                        ", Tiempo: " + s.getTiempoEstimado() + " horas\n");
            }
        } catch (Exception e) {
            areaListado.setText("❌ Error: " + e.getMessage());
        }
    }

    private JPanel crearPanelBuscar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Buscar Servicio"));

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
                Servicio s = controller.buscarServicioPorId(id);
                if (s != null) {
                    resultado.setText("ID: " + s.getIdServicio() +
                            "\nTipo: " + s.getTipo() +
                            "\nDescripción: " + s.getDescripcion() +
                            "\nCosto: " + s.getCostoManoObra() +
                            "\nTiempo: " + s.getTiempoEstimado() + " horas");
                } else {
                    resultado.setText("Servicio no encontrado.");
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
        panel.setBorder(BorderFactory.createTitledBorder("Actualizar Servicio"));

        JTextField txtId = new JTextField();
        JTextField txtTipo = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtCosto = new JTextField();
        JTextField txtTiempo = new JTextField();
        JButton btnCargar = new JButton("📥 Cargar");
        JButton btnActualizar = new JButton("💾 Actualizar");

        panel.add(new JLabel("ID Servicio:")); panel.add(txtId);
        panel.add(new JLabel()); panel.add(btnCargar);
        panel.add(new JLabel("Tipo (Preventivo/Correctivo):")); panel.add(txtTipo);
        panel.add(new JLabel("Descripción:")); panel.add(txtDescripcion);
        panel.add(new JLabel("Costo Mano de Obra:")); panel.add(txtCosto);
        panel.add(new JLabel("Tiempo Estimado (horas):")); panel.add(txtTiempo);
        panel.add(new JLabel()); panel.add(btnActualizar);

        btnCargar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Servicio s = controller.buscarServicioPorId(id);
                if (s != null) {
                    txtTipo.setText(s.getTipo());
                    txtDescripcion.setText(s.getDescripcion());
                    txtCosto.setText(s.getCostoManoObra().toString());
                    txtTiempo.setText(String.valueOf(s.getTiempoEstimado()));
                } else {
                    JOptionPane.showMessageDialog(this, "Servicio no encontrado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Servicio s = controller.buscarServicioPorId(id);
                if (s != null) {
                    s.setTipo(txtTipo.getText());
                    s.setDescripcion(txtDescripcion.getText());
                    s.setCostoManoObra(new BigDecimal(txtCosto.getText()));
                    s.setTiempoEstimado(Integer.parseInt(txtTiempo.getText()));
                    controller.actualizarServicio(s);
                    JOptionPane.showMessageDialog(this, "✅ Servicio actualizado.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Eliminar Servicio"));

        JTextField txtId = new JTextField();
        JButton btnEliminar = new JButton("🗑 Eliminar");

        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Ingrese ID:"), BorderLayout.WEST);
        top.add(txtId, BorderLayout.CENTER);
        top.add(btnEliminar, BorderLayout.EAST);

        btnEliminar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                controller.eliminarServicio(id);
                JOptionPane.showMessageDialog(this, "✅ Servicio eliminado.");
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

        SwingUtilities.invokeLater(() -> new ServicioView().setVisible(true));
    }
}