package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ServicioController;
import com.carmotors.carmotors.model.entities.Servicio;
import com.carmotors.carmotors.model.dao.ConexionDB;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public class ServicioView extends JFrame {
    private ServicioController controller;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JTextArea areaListado;
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

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
            JButton btnEliminar = new JButton("🗑 Eliminar");

            // Style buttons
            for (JButton btn : new JButton[]{btnRegistrar, btnListar, btnBuscar, btnActualizar, btnEliminar}) {
                estilizarBoton(btn);
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
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10)) {
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
                "Registrar Servicio",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtTipo = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtCosto = new JTextField("0.00");
        JTextField txtTiempo = new JTextField();
        JButton btnGuardar = new JButton("✅ Guardar");

        // Style text fields
        for (JTextField txt : new JTextField[]{txtTipo, txtDescripcion, txtCosto, txtTiempo}) {
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

        // Style button
        estilizarBoton(btnGuardar);

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
                "Servicios Registrados",
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
                "Buscar Servicio",
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
        estilizarBoton(btnBuscar);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
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
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10)) {
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
                "Actualizar Servicio",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JTextField txtTipo = new JTextField();
        JTextField txtDescripcion = new JTextField();
        JTextField txtCosto = new JTextField();
        JTextField txtTiempo = new JTextField();
        JButton btnCargar = new JButton("📥 Cargar");
        JButton btnActualizar = new JButton("💾 Actualizar");

        // Style text fields
        for (JTextField txt : new JTextField[]{txtId, txtTipo, txtDescripcion, txtCosto, txtTiempo}) {
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

        // Style buttons
        estilizarBoton(btnCargar);
        estilizarBoton(btnActualizar);

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
                "Eliminar Servicio",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtId = new JTextField();
        JButton btnEliminar = new JButton("🗑 Eliminar");

        // Style text field
        txtId.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        txtId.setBackground(Color.WHITE);
        txtId.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtId.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtId.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });

        // Style button
        estilizarBoton(btnEliminar);

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
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

    private void estilizarBoton(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBackground(VIBRANT_BLUE);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        boton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(10, 20, 10, 20)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new ServicioView().setVisible(true));
    }
}