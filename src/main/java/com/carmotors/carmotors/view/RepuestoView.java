package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.RepuestoController;
import com.carmotors.carmotors.model.entities.Repuesto;
import javax.swing.*;
import java.awt.*;
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
        idField.setEditable(false); // ID is auto-generated
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
        panel.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] tipos = {"Mecánico", "Eléctrico", "Carrocería", "Consumo"};
        tipoComboBox = new JComboBox<>(tipos);
        panel.add(tipoComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Marca:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        marcaField = new JTextField();
        panel.add(marcaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Modelo:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        modeloField = new JTextField();
        panel.add(modeloField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0.0;
        panel.add(new JLabel("ID Proveedor:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        idProveedorField = new JTextField();
        panel.add(idProveedorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Cantidad Stock:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cantidadStockField = new JTextField();
        panel.add(cantidadStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Nivel Mínimo Stock:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        nivelMinimoStockField = new JTextField();
        panel.add(nivelMinimoStockField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Fecha Ingreso: (YYYY-MM-DD)"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        fechaIngresoField = new JTextField();
        panel.add(fechaIngresoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Vida Útil (días):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        vidaUtilDiasField = new JTextField();
        panel.add(vidaUtilDiasField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.weightx = 0.0;
        panel.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        String[] estados = {"Disponible", "Reservado para trabajo", "Fuera de servicio"};
        estadoComboBox = new JComboBox<>(estados);
        panel.add(estadoComboBox, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        addButton = new JButton("Agregar");
        panel.add(addButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 11;
        viewAllButton = new JButton("Ver Todos");
        panel.add(viewAllButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 11;
        backButton = new JButton("Atrás");
        panel.add(backButton, gbc);

        // Text Area for displaying repuestos
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        repuestosArea = new JTextArea(10, 40);
        repuestosArea.setEditable(false);
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

        backButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new RepuestoView().setVisible(false));
        });
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RepuestoView().setVisible(true));
    }
}