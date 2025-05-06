package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.RepuestoController;
import com.carmotors.carmotors.model.entities.Repuesto;

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
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class RepuestoView extends JFrame {
    private RepuestoController controller;
    private JTextField idField, nombreField, marcaField, modeloField, idProveedorField,
            cantidadStockField, nivelMinimoStockField, fechaIngresoField, vidaUtilDiasField;
    private JComboBox<String> tipoComboBox;
    private JComboBox<String> estadoComboBox;
    private JTextArea repuestosArea;
    private JButton addButton, viewAllButton, backButton;
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public RepuestoView() {
        // Load database connection properties from dbconfig.properties
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties")) {
            if (input == null) {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el archivo dbconfig.properties en src/main/resources/");
                return;
            }
            props.load(input);
            String dbUrl = props.getProperty("app.db");
            String dbUser = props.getProperty("app.user");
            String dbPassword = props.getProperty("app.password");

            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            this.controller = new RepuestoController(conn);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de configuración: " + e.getMessage());
            return;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos: " + e.getMessage());
            return;
        }

        setTitle("📦 Gestión de Repuestos - CarMotors");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);

        // Toolbar with "Atrás" button
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        backButton = new JButton("⬅ Atrás");
        backButton.setFocusPainted(false);
        backButton.setBackground(VIBRANT_BLUE);
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backButton.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(DEEP_BLUE, 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(VIBRANT_BLUE);
            }
        });
        backButton.addActionListener(e -> dispose());
        toolbar.add(backButton);
        add(toolbar, BorderLayout.NORTH);

        // Main panel with gradient background
        JPanel panel = new JPanel(new GridBagLayout()) {
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
                "Gestión de Repuestos",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels and Fields
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.0;
        panel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        idField = new JTextField();
        idField.setEditable(false);
        idField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        idField.setBackground(Color.WHITE);
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nombreField = new JTextField();
        nombreField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        nombreField.setBackground(Color.WHITE);
        nombreField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                nombreField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                nombreField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] tipos = {"Mecánico", "Eléctrico", "Carrocería", "Consumo"};
        tipoComboBox = new JComboBox<>(tipos);
        tipoComboBox.setBackground(Color.WHITE);
        tipoComboBox.setForeground(DEEP_BLUE);
        tipoComboBox.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        panel.add(tipoComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Marca:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        marcaField = new JTextField();
        marcaField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        marcaField.setBackground(Color.WHITE);
        marcaField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                marcaField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                marcaField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(marcaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Modelo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        modeloField = new JTextField();
        modeloField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        modeloField.setBackground(Color.WHITE);
        modeloField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                modeloField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                modeloField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(modeloField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        panel.add(new JLabel("ID Proveedor:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        idProveedorField = new JTextField();
        idProveedorField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        idProveedorField.setBackground(Color.WHITE);
        idProveedorField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                idProveedorField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                idProveedorField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(idProveedorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Cantidad Stock:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cantidadStockField = new JTextField();
        cantidadStockField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        cantidadStockField.setBackground(Color.WHITE);
        cantidadStockField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                cantidadStockField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                cantidadStockField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(cantidadStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Nivel Mínimo Stock:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nivelMinimoStockField = new JTextField();
        nivelMinimoStockField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        nivelMinimoStockField.setBackground(Color.WHITE);
        nivelMinimoStockField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                nivelMinimoStockField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                nivelMinimoStockField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(nivelMinimoStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Fecha Ingreso: (YYYY-MM-DD)"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        fechaIngresoField = new JTextField();
        fechaIngresoField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        fechaIngresoField.setBackground(Color.WHITE);
        fechaIngresoField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                fechaIngresoField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                fechaIngresoField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(fechaIngresoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Vida Útil (días):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        vidaUtilDiasField = new JTextField();
        vidaUtilDiasField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        vidaUtilDiasField.setBackground(Color.WHITE);
        vidaUtilDiasField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                vidaUtilDiasField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                vidaUtilDiasField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(vidaUtilDiasField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] estados = {"Disponible", "Reservado para trabajo", "Fuera de servicio"};
        estadoComboBox = new JComboBox<>(estados);
        estadoComboBox.setBackground(Color.WHITE);
        estadoComboBox.setForeground(DEEP_BLUE);
        estadoComboBox.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        panel.add(estadoComboBox, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        addButton = new JButton("Agregar");
        estilizarBoton(addButton);
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        viewAllButton = new JButton("Ver Todos");
        estilizarBoton(viewAllButton);
        panel.add(viewAllButton, gbc);

        // Text Area for displaying repuestos
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        repuestosArea = new JTextArea(10, 40);
        repuestosArea.setEditable(false);
        repuestosArea.setBackground(ACCENT_BLUE);
        repuestosArea.setForeground(DEEP_BLUE);
        repuestosArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(repuestosArea), gbc);

        add(panel);

        // Action Listeners
        addButton.addActionListener(e -> {
            try {
                Repuesto repuesto = new Repuesto();
                repuesto.setNombre(nombreField.getText());
                repuesto.setTipo((String) tipoComboBox.getSelectedItem());
                repuesto.setMarca(marcaField.getText());
                repuesto.setModelo(modeloField.getText());
                int idProveedor = Integer.parseInt(idProveedorField.getText());
                if (!controller.providerExists(idProveedor)) {
                    JOptionPane.showMessageDialog(this, "Error: El ID Proveedor " + idProveedor + " no existe en la base de datos.");
                    return;
                }
                repuesto.setIdProveedor(idProveedor);
                repuesto.setCantidadStock(Integer.parseInt(cantidadStockField.getText()));
                repuesto.setNivelMinimoStock(Integer.parseInt(nivelMinimoStockField.getText()));
                repuesto.setFechaIngreso(fechaIngresoField.getText());
                repuesto.setVidaUtilDias(Integer.parseInt(vidaUtilDiasField.getText()));
                repuesto.setEstado((String) estadoComboBox.getSelectedItem());
                controller.addRepuesto(repuesto);
                JOptionPane.showMessageDialog(this, "Repuesto agregado con ID: " + repuesto.getId());
                clearFields();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        viewAllButton.addActionListener(e -> {
            try {
                List<Repuesto> repuestos = controller.getAllRepuestos();
                repuestosArea.setText("");
                for (Repuesto r : repuestos) {
                    repuestosArea.append("ID: " + r.getId() + ", Nombre: " + r.getNombre() +
                            ", Tipo: " + r.getTipo() + ", Marca: " + r.getMarca() +
                            ", Modelo: " + r.getModelo() + ", ID Proveedor: " + r.getIdProveedor() +
                            ", Stock: " + r.getCantidadStock() + ", Mínimo: " + r.getNivelMinimoStock() +
                            ", Fecha: " + r.getFechaIngreso() + ", Vida Útil: " + r.getVidaUtilDias() +
                            ", Estado: " + r.getEstado() + "\n");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar repuestos: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> dispose());
    }

    private void clearFields() {
        idField.setText("");
        nombreField.setText("");
        tipoComboBox.setSelectedIndex(0);
        marcaField.setText("");
        modeloField.setText("");
        idProveedorField.setText("");
        cantidadStockField.setText("");
        nivelMinimoStockField.setText("");
        fechaIngresoField.setText("");
        vidaUtilDiasField.setText("");
        estadoComboBox.setSelectedIndex(0);
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
        SwingUtilities.invokeLater(() -> new RepuestoView().setVisible(true));
    }
}