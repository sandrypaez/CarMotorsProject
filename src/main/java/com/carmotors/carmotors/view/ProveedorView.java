package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ProveedorController;
import com.carmotors.carmotors.model.entities.Proveedor;
import javax.swing.*;
import java.awt.*;
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

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
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
        panel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nombreField = new JTextField();
        panel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        panel.add(new JLabel("NIT:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nitField = new JTextField();
        panel.add(nitField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Contacto:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        contactoField = new JTextField();
        panel.add(contactoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Frecuencia Visita:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        frecuenciaVisitaField = new JTextField();
        panel.add(frecuenciaVisitaField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        addButton = new JButton("Agregar");
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        updateButton = new JButton("Actualizar");
        panel.add(updateButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        deleteButton = new JButton("Eliminar");
        panel.add(deleteButton, gbc);

        gbc.gridx = 3;
        gbc.gridy = 5;
        viewAllButton = new JButton("Ver Todos");
        panel.add(viewAllButton, gbc);

        gbc.gridx = 4;
        gbc.gridy = 5;
        backButton = new JButton("Atrás");
        panel.add(backButton, gbc);

        // Text Area for displaying proveedores
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        proveedoresArea = new JTextArea(5, 40);
        proveedoresArea.setEditable(false);
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
            SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProveedorView().setVisible(true));
    }
}