package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ClienteController;
import com.carmotors.carmotors.model.dao.ConexionDB;
import com.carmotors.carmotors.model.entities.Cliente;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.List;

public class ClienteView extends JFrame {
    private ClienteController controller;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    // Define new blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public ClienteView() {
        try {
            Connection conn = ConexionDB.getConnection();
            this.controller = new ClienteController(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al conectar con la base de datos: " + e.getMessage());
            System.exit(1);
        }

        setTitle("CarMotors - Gestión de Clientes");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Toolbar for buttons
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnRegistrar = new JButton("📋 Registrar");
        JButton btnListar = new JButton("📑 Listar");
        JButton btnBuscar = new JButton("🔍 Buscar");
        JButton btnActualizar = new JButton("✏️ Actualizar");

        for (JButton btn : new JButton[]{btnRegistrar, btnListar, btnBuscar, btnActualizar}) {
            btn.setFocusPainted(false);
            btn.setBackground(VIBRANT_BLUE);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(DEEP_BLUE, 1, true),
                    new EmptyBorder(5, 10, 5, 10)
            ));
            // Hover effect
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
            toolbar.add(btn);
            toolbar.add(Box.createHorizontalStrut(10)); // Space between buttons
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

        add(toolbar, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "registrar");
    }

    private JPanel crearPanelRegistro() {
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
                "Registrar Cliente",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtNombre = new JTextField();
        JTextField txtId = new JTextField();
        JTextField txtTel = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtDireccion = new JTextField();
        JTextField txtPuntos = new JTextField("0");
        txtPuntos.setEditable(false);
        txtPuntos.setBackground(ACCENT_BLUE);
        JTextField txtDescuento = new JTextField("0.0");
        txtDescuento.setEditable(false);
        txtDescuento.setBackground(ACCENT_BLUE);
        JButton btnGuardar = new JButton("✅ Guardar");
        btnGuardar.setBackground(VIBRANT_BLUE);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
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

        // Style text fields
        for (JTextField txt : new JTextField[]{txtNombre, txtId, txtTel, txtEmail, txtDireccion}) {
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

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Identificación:"));
        panel.add(txtId);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTel);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Puntos:"));
        panel.add(txtPuntos);
        panel.add(new JLabel("Descuento %:"));
        panel.add(txtDescuento);
        panel.add(new JLabel());
        panel.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                Cliente cliente = new Cliente();
                cliente.setNombre(txtNombre.getText());
                cliente.setIdentificacion(txtId.getText());
                cliente.setTelefono(txtTel.getText());
                cliente.setCorreoElectronico(txtEmail.getText());
                cliente.setDireccion(txtDireccion.getText());
                cliente.setDiscountPercentage(0.0);
                cliente.setRewardPoints(0);

                controller.registrarCliente(cliente);

                JOptionPane.showMessageDialog(this, "✅ Cliente registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                txtNombre.setText("");
                txtId.setText("");
                txtTel.setText("");
                txtEmail.setText("");
                txtPuntos.setText("0");
                txtDescuento.setText("0.0");
                txtDireccion.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JTextArea areaListado = new JTextArea();

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
                "Clientes Registrados",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        areaListado.setEditable(false);
        areaListado.setBackground(ACCENT_BLUE);
        areaListado.setForeground(DEEP_BLUE);
        areaListado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(areaListado);
        scrollPane.setBorder(new LineBorder(VIBRANT_BLUE, 1, true));
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void cargarClientes() {
        try {
            List<Cliente> lista = controller.listarTodosClientes();
            areaListado.setText("");
            for (Cliente c : lista) {
                areaListado.append("ID: " + c.getId() +
                        ", Nombre: " + c.getNombre() +
                        ", Tel: " + c.getTelefono() +
                        ", Email: " + c.getCorreoElectronico() +
                        ", Puntos: " + c.getRewardPoints() +
                        ", Descuento: " + c.getDiscountPercentage() + "%\n");
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
                "Buscar Cliente",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField txtBuscar = new JTextField();
        txtBuscar.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
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
        JButton btnBuscar = new JButton("🔎 Buscar");
        btnBuscar.setBackground(VIBRANT_BLUE);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
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
        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.setOpaque(false);
        top.add(new JLabel("Ingrese ID:"), BorderLayout.WEST);
        top.add(txtBuscar, BorderLayout.CENTER);
        top.add(btnBuscar, BorderLayout.EAST);

        btnBuscar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtBuscar.getText());
                Cliente c = controller.buscarClientePorId(id);
                if (c != null) {
                    controller.aplicarBeneficios(c);
                    resultado.setText("ID: " + c.getId() +
                            "\nNombre: " + c.getNombre() +
                            "\nTeléfono: " + c.getTelefono() +
                            "\nEmail: " + c.getCorreoElectronico() +
                            "\nPuntos: " + c.getRewardPoints() +
                            "\nDescuento: " + c.getDiscountPercentage() + "%");
                } else {
                    resultado.setText("Cliente no encontrado.");
                }
            } catch (Exception ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        JScrollPane scrollPane = new JScrollPane(resultado);
        scrollPane.setBorder(new LineBorder(VIBRANT_BLUE, 1, true));
        panel.add(top, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelActualizar() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10)) {
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
                "Actualizar Cliente",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JButton btnAtras = new JButton("🔙 Volver al Menú");
        btnAtras.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAtras.setFocusPainted(false);
        btnAtras.setBackground(VIBRANT_BLUE);
        btnAtras.setForeground(Color.WHITE);
        btnAtras.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
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

        btnAtras.addActionListener(e -> {
            this.dispose();
        });

        add(btnAtras, BorderLayout.SOUTH);

        JTextField txtId = new JTextField();
        JTextField txtNombre = new JTextField();
        JTextField txtIdent = new JTextField();
        JTextField txtTel = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtPuntos = new JTextField();
        txtPuntos.setEditable(false);
        txtPuntos.setBackground(ACCENT_BLUE);
        JTextField txtDescuento = new JTextField();
        txtDescuento.setEditable(false);
        txtDescuento.setBackground(ACCENT_BLUE);

        // Style text fields
        for (JTextField txt : new JTextField[]{txtId, txtNombre, txtIdent, txtTel, txtEmail}) {
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

        JButton btnCargar = new JButton("📥 Cargar");
        btnCargar.setBackground(VIBRANT_BLUE);
        btnCargar.setForeground(Color.WHITE);
        btnCargar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        btnCargar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnCargar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnCargar.setBackground(VIBRANT_BLUE);
            }
        });

        JButton btnActualizar = new JButton("💾 Actualizar");
        btnActualizar.setBackground(VIBRANT_BLUE);
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        btnActualizar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnActualizar.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnActualizar.setBackground(VIBRANT_BLUE);
            }
        });

        panel.add(new JLabel("ID Cliente:"));
        panel.add(txtId);
        panel.add(new JLabel());
        panel.add(btnCargar);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Identificación:"));
        panel.add(txtIdent);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTel);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Puntos:"));
        panel.add(txtPuntos);
        panel.add(new JLabel("Descuento:"));
        panel.add(txtDescuento);
        panel.add(new JLabel());
        panel.add(btnActualizar);

        btnCargar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Cliente c = controller.buscarClientePorId(id);
                if (c != null) {
                    controller.aplicarBeneficios(c);
                    txtNombre.setText(c.getNombre());
                    txtIdent.setText(c.getIdentificacion());
                    txtTel.setText(c.getTelefono());
                    txtEmail.setText(c.getCorreoElectronico());
                    txtPuntos.setText(String.valueOf(c.getRewardPoints()));
                    txtDescuento.setText(c.getDiscountPercentage() + "%");
                } else {
                    JOptionPane.showMessageDialog(this, "Cliente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActualizar.addActionListener(e -> {
            try {
                int id = Integer.parseInt(txtId.getText());
                Cliente c = controller.buscarClientePorId(id);
                if (c != null) {
                    c.setNombre(txtNombre.getText());
                    c.setIdentificacion(txtIdent.getText());
                    c.setTelefono(txtTel.getText());
                    c.setCorreoElectronico(txtEmail.getText());
                    controller.actualizarCliente(c);
                    JOptionPane.showMessageDialog(this, "✅ Cliente actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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