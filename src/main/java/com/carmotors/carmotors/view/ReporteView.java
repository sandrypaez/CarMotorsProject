package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ReporteController;
import com.carmotors.carmotors.model.entities.Reporte;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class ReporteView extends JFrame {
    private final ReporteController controller;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    public ReporteView() {
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
            this.controller = new ReporteController(conn);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al inicializar la aplicación: " + e.getMessage() + "\nLa aplicación se cerrará.");
            SwingUtilities.invokeLater(() -> System.exit(1));
            throw new RuntimeException("Failed to initialize ReporteView", e);
        }

        setTitle("CarMotors - Reportes y Estadísticas");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Menú Principal
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(6, 1, 10, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menu.setBackground(new Color(245, 245, 245));

        JButton btnInventario = new JButton("📦 Inventario de Repuestos");
        JButton btnMantenimiento = new JButton("🛠 Mantenimiento y Reparaciones");
        JButton btnClientes = new JButton("👥 Clientes");
        JButton btnProveedores = new JButton("🚚 Proveedores");
        JButton btnCampanas = new JButton("📈 Campañas y Actividades Especiales");

        for (JButton btn : new JButton[]{btnInventario, btnMantenimiento, btnClientes, btnProveedores, btnCampanas}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            menu.add(btn);
        }

        btnInventario.addActionListener(e -> cardLayout.show(cardPanel, "inventario"));
        btnMantenimiento.addActionListener(e -> cardLayout.show(cardPanel, "mantenimiento"));
        btnClientes.addActionListener(e -> cardLayout.show(cardPanel, "clientes"));
        btnProveedores.addActionListener(e -> cardLayout.show(cardPanel, "proveedores"));
        btnCampanas.addActionListener(e -> cardLayout.show(cardPanel, "campanas"));

        cardPanel.add(crearPanelInventario(), "inventario");
        cardPanel.add(crearPanelMantenimiento(), "mantenimiento");
        cardPanel.add(crearPanelClientes(), "clientes");
        cardPanel.add(crearPanelProveedores(), "proveedores");
        cardPanel.add(crearPanelCampanas(), "campanas");

        add(menu, BorderLayout.WEST);
        add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, "inventario");
    }

    private JPanel crearPanelInventario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Inventario de Repuestos"));
        panel.setBackground(Color.WHITE);

        JPanel submenu = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnListado = new JButton("Listado detallado de repuestos");
        JButton btnConsumo = new JButton("Análisis de consumo por períodos");
        JButton btnAlertas = new JButton("Alertas de productos vencidos o próximos a caducar");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnListado, btnConsumo, btnAlertas, btnAtras}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultado);

        btnListado.addActionListener(e -> {
            try {
                controller.generarReporteInventarioRepuestos();
                List<Reporte> reportes = controller.listarReportesPorTipo("Inventario");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Listado detallado de repuestos")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnConsumo.addActionListener(e -> {
            try {
                controller.generarReporteConsumoRepuestos();
                List<Reporte> reportes = controller.listarReportesPorTipo("Inventario");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Análisis de consumo por períodos")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnAlertas.addActionListener(e -> {
            try {
                controller.generarReporteAlertasRepuestos();
                List<Reporte> reportes = controller.listarReportesPorTipo("Inventario");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Alertas de productos vencidos o próximos a caducar")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnAtras.addActionListener(e -> dispose());

        panel.add(submenu, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelMantenimiento() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Mantenimiento y Reparaciones"));
        panel.setBackground(Color.WHITE);

        JPanel submenu = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnServicios = new JButton("Servicios más solicitados por tipo de vehículo");
        JButton btnProductividad = new JButton("Productividad de técnicos");
        JButton btnHistorial = new JButton("Historial de mantenimientos por cliente o vehículo");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnServicios, btnProductividad, btnHistorial, btnAtras}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultado);

        btnServicios.addActionListener(e -> {
            try {
                controller.generarReporteServiciosPorTipoVehiculo();
                List<Reporte> reportes = controller.listarReportesPorTipo("Mantenimiento");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Servicios más solicitados por tipo de vehículo")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnProductividad.addActionListener(e -> {
            try {
                controller.generarReporteProductividadTecnicos();
                List<Reporte> reportes = controller.listarReportesPorTipo("Mantenimiento");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Productividad de técnicos")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnHistorial.addActionListener(e -> {
            try {
                controller.generarReporteHistorialMantenimientos();
                List<Reporte> reportes = controller.listarReportesPorTipo("Mantenimiento");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Historial de mantenimientos por cliente o vehículo")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnAtras.addActionListener(e -> dispose());

        panel.add(submenu, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Clientes"));
        panel.setBackground(Color.WHITE);

        JPanel submenu = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton btnHistorial = new JButton("Historial de servicios por cliente");
        JButton btnFrecuentes = new JButton("Clientes frecuentes y su facturación");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnHistorial, btnFrecuentes, btnAtras}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultado);

        btnHistorial.addActionListener(e -> {
            try {
                controller.generarReporteHistorialServiciosCliente();
                List<Reporte> reportes = controller.listarReportesPorTipo("Clientes");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Historial de servicios por cliente")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnFrecuentes.addActionListener(e -> {
            try {
                controller.generarReporteClientesFrecuentes();
                List<Reporte> reportes = controller.listarReportesPorTipo("Clientes");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Clientes frecuentes y su facturación")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnAtras.addActionListener(e -> dispose());

        panel.add(submenu, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelProveedores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Proveedores"));
        panel.setBackground(Color.WHITE);

        JPanel submenu = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton btnEvaluacion = new JButton("Evaluación de proveedores");
        JButton btnHistorial = new JButton("Historial de productos entregados por proveedor");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnEvaluacion, btnHistorial, btnAtras}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultado);

        btnEvaluacion.addActionListener(e -> {
            try {
                controller.generarReporteEvaluacionProveedores();
                List<Reporte> reportes = controller.listarReportesPorTipo("Proveedores");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Evaluación de proveedores")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnHistorial.addActionListener(e -> {
            try {
                controller.generarReporteHistorialEntregasProveedores();
                List<Reporte> reportes = controller.listarReportesPorTipo("Proveedores");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Historial de productos entregados por proveedor")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnAtras.addActionListener(e -> dispose());

        panel.add(submenu, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelCampanas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Campañas y Actividades Especiales"));
        panel.setBackground(Color.WHITE);

        JPanel submenu = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton btnEvaluacion = new JButton("Evaluación de campañas de mantenimiento preventivo");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnEvaluacion, btnAtras}) {
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultado);

        btnEvaluacion.addActionListener(e -> {
            try {
                controller.generarReporteEvaluacionCampanas();
                List<Reporte> reportes = controller.listarReportesPorTipo("Campañas");
                resultado.setText("");
                for (Reporte reporte : reportes) {
                    if (reporte.getDescripcion().equals("Evaluación de campañas de mantenimiento preventivo")) {
                        resultado.append("Reporte generado el " + reporte.getFechaGeneracion() + ":\n");
                        resultado.append(reporte.getDatos() + "\n\n");
                    }
                }
            } catch (SQLException ex) {
                resultado.setText("❌ Error: " + ex.getMessage());
            }
        });

        btnAtras.addActionListener(e -> dispose());

        panel.add(submenu, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public static void main(String[] args) {
        try {
            FlatLightLaf.setup();
        } catch (Exception e) {
            System.err.println("Error al aplicar FlatLaf: " + e.getMessage());
        }

        SwingUtilities.invokeLater(() -> new ReporteView().setVisible(true));
    }
}