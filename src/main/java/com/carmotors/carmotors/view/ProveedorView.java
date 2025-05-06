package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ProveedorController;
import com.carmotors.carmotors.model.entities.Proveedor;

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

public class ProveedorView extends JFrame {
    private ProveedorController controller;
    private JTextField idField, nombreField, nitField, contactoField, frecuenciaVisitaField;
    private JTextArea proveedoresArea;
    private JButton addButton, updateButton, deleteButton, viewAllButton, backButton;
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

    public ProveedorView() {
        // Load database connection properties from dbconfig.properties
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("dbconfig.properties")) {
            if (input == null) {
                JOptionPane.showMessageDialog(this, "No se pudo encontrar el archivo dbconfig.properties");
                return;
            }
            props.load(input);
            String dbUrl = props.getProperty("app.db");
            String dbUser = props.getProperty("app.user");
            String dbPassword = props.getProperty("app.password");

            Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            this.controller = new ProveedorController(conn);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo de configuración: " + e.getMessage());
            return;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error de conexión a la base de datos: " + e.getMessage());
            return;
        }

        setTitle("🤝 Gestión de Proveedores - CarMotors");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(700, 300);
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
                "Gestión de Proveedores",
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
        idField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        idField.setBackground(Color.WHITE);
        idField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                idField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                idField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
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
        panel.add(new JLabel("NIT:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nitField = new JTextField();
        nitField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        nitField.setBackground(Color.WHITE);
        nitField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                nitField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                nitField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(nitField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Contacto:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        contactoField = new JTextField();
        contactoField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        contactoField.setBackground(Color.WHITE);
        contactoField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                contactoField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                contactoField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(contactoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Frecuencia Visita:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        frecuenciaVisitaField = new JTextField();
        frecuenciaVisitaField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
        frecuenciaVisitaField.setBackground(Color.WHITE);
        frecuenciaVisitaField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                frecuenciaVisitaField.setBorder(new LineBorder(VIBRANT_BLUE, 2, true));
            }

            @Override
            public void focusLost(FocusEvent e) {
                frecuenciaVisitaField.setBorder(new LineBorder(ACCENT_BLUE, 1, true));
            }
        });
        panel.add(frecuenciaVisitaField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        addButton = new JButton("Agregar");
        estilizarBoton(addButton);
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        updateButton = new JButton("Actualizar");
        estilizarBoton(updateButton);
        panel.add(updateButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        deleteButton = new JButton("Eliminar");
        estilizarBoton(deleteButton);
        panel.add(deleteButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 5;
        viewAllButton = new JButton("Ver Todos");
        estilizarBoton(viewAllButton);
        panel.add(viewAllButton, gbc);

        // Text Area for displaying proveedores
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        proveedoresArea = new JTextArea(5, 40);
        proveedoresArea.setEditable(false);
        proveedoresArea.setBackground(ACCENT_BLUE);
        proveedoresArea.setForeground(DEEP_BLUE);
        proveedoresArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(new JScrollPane(proveedoresArea), gbc);

        add(panel);

        // Action Listeners
        addButton.addActionListener(e -> {
            try {
                Proveedor proveedor = new Proveedor();
                proveedor.setNombre(nombreField.getText());
                proveedor.setNit(nitField.getText());
                proveedor.setContacto(contactoField.getText());
                proveedor.setFrecuenciaVisita(frecuenciaVisitaField.getText());
                controller.addProveedor(proveedor);
                JOptionPane.showMessageDialog(this, "Proveedor agregado con ID: " + proveedor.getId());
                clearFields();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        viewAllButton.addActionListener(e -> {
            try {
                List<Proveedor> proveedores = controller.getAllProveedores();
                proveedoresArea.setText("");
                for (Proveedor p : proveedores) {
                    proveedoresArea.append(p.getId() + ", " + p.getNombre() + ", " + p.getContacto() + "\n");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al cargar proveedores: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            dispose();
        });

        // Add listeners for update and delete as needed
    }

    private void clearFields() {
        idField.setText("");
        nombreField.setText("");
        nitField.setText("");
        contactoField.setText("");
        frecuenciaVisitaField.setText("");
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
        SwingUtilities.invokeLater(() -> new ProveedorView().setVisible(true));
    }
}