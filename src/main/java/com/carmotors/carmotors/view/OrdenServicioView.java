package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.OrdenServicioController;
import com.carmotors.carmotors.model.entities.OrdenServicio;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class OrdenServicioView extends JFrame {
    private final OrdenServicioController controller;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JTextArea areaListado = new JTextArea();
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public OrdenServicioView() {
        // Initialize controller with a valid connection or handle failure
        Connection conn = null;
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties")) {
            if (input == null) {
                throw new IOException("No se pudo encontrar el archivo dbconfig.properties en src/main/resources/");
            }
            props.load(input);
            String dbUrl = props.getProperty("app.db");
            String dbUser = props.getProperty("app.user");
            String dbPassword = props.getProperty("app.password");
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            this.controller = new OrdenServicioController(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al inicializar la aplicación: " + e.getMessage() + "\nLa aplicación se cerrará.");
            SwingUtilities.invokeLater(() -> System.exit(1));
            throw new RuntimeException("Failed to initialize OrdenServicioView", e); // Ensure constructor fails
        }

        setTitle("CarMotors - Gestión de Órdenes de Servicio");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Toolbar with "Atrás" button
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAtras = new JButton("⬅ Atrás");
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
        btnAtras.addActionListener(e -> dispose());
        toolbar.add(btnAtras);
        add(toolbar, BorderLayout.NORTH);

        // Menu panel with gradient background
        JPanel menu = new JPanel(new GridLayout(5, 1, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        menu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnRegistrar = new JButton("📋 Registrar");
        JButton btnListar = new JButton("📑 Listar");
        JButton btnBuscar = new JButton("🔍 Buscar");
        JButton btnActualizar = new JButton("✏️ Actualizar");

        // Style buttons
        for (JButton btn : new JButton[]{btnRegistrar, btnListar, btnBuscar, btnActualizar}) {
            btn.setFocusPainted(false);
            btn.setBackground(VIBRANT_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(DEEP_BLUE, 1, true),
                    new EmptyBorder(10, 20, 10, 20)
            ));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(HOVER_BLUE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(VIBRANT_BLUE);
                }
            });
            menu.add(btn);
        }

        btnRegistrar.addActionListener(e -> cardLayout.show(cardPanel, "registrar"));
        btnListar.addActionListener(e -> {
            cardLayout.show(cardPanel, "listar");
            cargarOrdenes();
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
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DEEP_BLUE),
                "Registrar Orden de Servicio",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtIdVehiculo = new JTextField();
        JTextField txtIdServicio = new JTextField();
        JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Pendiente", "En proceso", "Completado", "Entregado"});
        JTextField txtFechaInicio = new JTextField("YYYY-MM-DD");
        JTextField txtFechaFin = new JTextField("YYYY-MM-DD");
        JButton btnGuardar = new JButton("✅ Guardar");

        // Style text fields
        for (JTextField txt : new JTextField[]{txtIdVehiculo, txtIdServicio, txtFechaInicio, txtFechaFin}) {
            txt.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            txt.setBackground(Color.WHITE);
            txt.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    txt.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    txt.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
                }
            });
        }

        // Style combo box
        cbEstado.setBackground(Color.WHITE);
        cbEstado.setForeground(DEEP_BLUE);
        cbEstado.setBorder(new LineBorder(ACCENT_BLUE, 1, true));

        // Style button
        btnGuardar.setFocusPainted(false);
        btnGuardar.setBackground(VIBRANT_BLUE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnGuardar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnGuardar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnGuardar.setBackground(VIBRANT_BLUE);
            }
        });

        panel.add(new JLabel("ID Vehículo:")); panel.add(txtIdVehiculo);
        panel.add(new JLabel("ID Servicio:")); panel.add(txtIdServicio);
        panel.add(new JLabel("Estado:")); panel.add(cbEstado);
        panel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):")); panel.add(txtFechaInicio);
        panel.add(new JLabel("Fecha Fin (YYYY-MM-DD):")); panel.add(txtFechaFin);
        panel.add(new JLabel()); panel.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                OrdenServicio orden = new OrdenServicio();
                orden.setIdVehiculo(Integer.parseInt(txtIdVehiculo.getText()));
                orden.setIdServicio(Integer.parseInt(txtIdServicio.getText()));
                orden.setEstado((String) cbEstado.getSelectedItem());
                orden.setFechaInicio(Date.valueOf(txtFechaInicio.getText()));
                orden.setFechaFin(txtFechaFin.getText().isEmpty() ? null : Date.valueOf(txtFechaFin.getText()));
                orden.setRecordatorioFecha(null);
                orden.setRecordatorioEnviado(false);

                controller.registrarOrdenServicio(orden);

                JOptionPane.showMessageDialog(this, "✅ Orden de servicio registrada con ID: " + orden.getId());
                txtIdVehiculo.setText(""); txtIdServicio.setText(""); cbEstado.setSelectedIndex(0);
                txtFechaInicio.setText("YYYY-MM-DD"); txtFechaFin.setText("YYYY-MM-DD");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        return panel;
    }

    private JPanel crearPanelListado() {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DEEP_BLUE),
                "Órdenes de Servicio Registradas",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        areaListado.setEditable(false);
        areaListado.setBackground(ACCENT_BLUE);
        areaListado.setForeground(DEEP_BLUE);
        areaListado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(areaListado), BorderLayout.CENTER);
        return panel;
    }

    private void cargarOrdenes() {
        try {
            List<OrdenServicio> lista = controller.listarTodasOrdenes();
            areaListado.setText("");
            for (OrdenServicio o : lista) {
                areaListado.append("ID: " + o.getId() +
                        ", ID Vehículo: " + o.getIdVehiculo() +
                        ", ID Servicio: " + o.getIdServicio() +
                        ", Estado: " + o.getEstado() +
                        ", Fecha Inicio: " + o.getFechaInicio() +
                        ", Fecha Fin: " + (o.getFechaFin() != null ? o.getFechaFin() : "N/A") + "\n");
            }
        } catch (Exception e) {
            areaListado.setText("❌ Error: " + e.getMessage());
        }
    }

    private JPanel crearPanelBuscar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DEEP_BLUE),
                "Buscar Orden de Servicio",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtBuscar = new JTextField();
        JButton btnBuscar = new JButton("🔎 Buscar");
        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Style text field
        txtBuscar.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        txtBuscar.setBackground(Color.WHITE);
        txtBuscar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtBuscar.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtBuscar.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });

        // Style button
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBackground(VIBRANT_BLUE);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnBuscar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        btnBuscar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnBuscar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnBuscar.setBackground(VIBRANT_BLUE);
            }
        });

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
        top.add(new JLabel("Ingrese ID:"), BorderLayout.WEST);
        top.add(txtBuscar, BorderLayout.CENTER);
        top.add(btnBuscar, BorderLayout.EAST);

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText());
                OrdenServicio o = controller.buscarOrdenServicioPorId(id);
                if (o != null) {
                    resultado.setText("ID: " + o.getId() +
                            "\nID Vehículo: " + o.getIdVehiculo() +
                            "\nID Servicio: " + o.getIdServicio() +
                            "\nEstado: " + o.getEstado() +
                            "\nFecha Inicio: " + o.getFechaInicio() +
                            "\nFecha Fin: " + (o.getFechaFin() != null ? o.getFechaFin() : "N/A"));
                } else {
                    resultado.setText("Orden de servicio no encontrada.");
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
        JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, SOFT_BLUE, 0, getHeight(), Color.WHITE));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(DEEP_BLUE),
                "Actualizar Orden de Servicio",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JTextField txtIdVehiculo = new JTextField();
        JTextField txtIdServicio = new JTextField();
        JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Pendiente", "En proceso", "Completado", "Entregado"});
        JTextField txtFechaInicio = new JTextField();
        JTextField txtFechaFin = new JTextField();

        JButton btnCargar = new JButton("📥 Cargar");
        JButton btnActualizar = new JButton("💾 Actualizar");

        // Style text fields
        for (JTextField txt : new JTextField[]{txtId, txtIdVehiculo, txtIdServicio, txtFechaInicio, txtFechaFin}) {
            txt.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            txt.setBackground(Color.WHITE);
            txt.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    txt.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
                }

                @Override
                public void focusLost(FocusEvent e) {
                    txt.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
                }
            });
        }

        // Style combo box
        cbEstado.setBackground(Color.WHITE);
        cbEstado.setForeground(DEEP_BLUE);
        cbEstado.setBorder(new LineBorder(ACCENT_BLUE, 1, true));

        // Style buttons
        for (JButton btn : new JButton[]{btnCargar, btnActualizar}) {
            btn.setFocusPainted(false);
            btn.setBackground(VIBRANT_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(DEEP_BLUE, 1, true),
                    new EmptyBorder(10, 20, 10, 20)
            ));
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(HOVER_BLUE);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(VIBRANT_BLUE);
                }
            });
        }

        panel.add(new JLabel("ID Orden:")); panel.add(txtId);
        panel.add(new JLabel()); panel.add(btnCargar);
        panel.add(new JLabel("ID Vehículo:")); panel.add(txtIdVehiculo);
        panel.add(new JLabel("ID Servicio:")); panel.add(txtIdServicio);
        panel.add(new JLabel("Estado:")); panel.add(cbEstado);
        panel.add(new JLabel("Fecha Inicio (YYYY-MM-DD):")); panel.add(txtFechaInicio);
        panel.add(new JLabel("Fecha Fin (YYYY-MM-DD):")); panel.add(txtFechaFin);
        panel.add(new JLabel()); panel.add(btnActualizar);

        btnCargar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                OrdenServicio o = controller.buscarOrdenServicioPorId(id);
                if (o != null) {
                    txtIdVehiculo.setText(String.valueOf(o.getIdVehiculo()));
                    txtIdServicio.setText(String.valueOf(o.getIdServicio()));
                    cbEstado.setSelectedItem(o.getEstado());
                    txtFechaInicio.setText(o.getFechaInicio().toString());
                    txtFechaFin.setText(o.getFechaFin() != null ? o.getFechaFin().toString() : "");
                } else {
                    JOptionPane.showMessageDialog(this, "Orden de servicio no encontrada.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                OrdenServicio o = controller.buscarOrdenServicioPorId(id);
                if (o != null) {
                    o.setIdVehiculo(Integer.parseInt(txtIdVehiculo.getText()));
                    o.setIdServicio(Integer.parseInt(txtIdServicio.getText()));
                    o.setEstado((String) cbEstado.getSelectedItem());
                    o.setFechaInicio(Date.valueOf(txtFechaInicio.getText()));
                    o.setFechaFin(txtFechaFin.getText().isEmpty() ? null : Date.valueOf(txtFechaFin.getText()));
                    controller.actualizarOrdenServicio(o);
                    JOptionPane.showMessageDialog(this, "✅ Orden de servicio actualizada.");
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

        SwingUtilities.invokeLater(() -> new OrdenServicioView().setVisible(true));
    }
}