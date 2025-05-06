package com.carmotors.carmotors.view;

import com.carmotors.carmotors.controller.ReporteController;
import com.carmotors.carmotors.model.entities.Reporte;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    // Define blue color palette
    private static final Color SOFT_BLUE = new Color(173, 216, 230); // Light pastel blue
    private static final Color VIBRANT_BLUE = new Color(30, 144, 255); // Dodger blue
    private static final Color DEEP_BLUE = new Color(0, 51, 102); // Navy blue
    private static final Color ACCENT_BLUE = new Color(135, 206, 250); // Sky blue
    private static final Color HOVER_BLUE = new Color(65, 105, 225); // Royal blue

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

        // Toolbar with "Atrás" button
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setBackground(DEEP_BLUE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAtrasMain = new JButton("⬅ Atrás");
        estilizarBoton(btnAtrasMain);
        btnAtrasMain.addActionListener(e -> dispose());
        toolbar.add(btnAtrasMain);
        add(toolbar, BorderLayout.NORTH);

        // Menú Principal with gradient background
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

        JButton btnInventario = new JButton("📦 Inventario de Repuestos");
        JButton btnMantenimiento = new JButton("🛠 Mantenimiento y Reparaciones");
        JButton btnClientes = new JButton("👥 Clientes");
        JButton btnProveedores = new JButton("🚚 Proveedores");
        JButton btnCampanas = new JButton("📈 Campañas y Actividades Especiales");

        // Style buttons
        for (JButton btn : new JButton[]{btnInventario, btnMantenimiento, btnClientes, btnProveedores, btnCampanas}) {
            estilizarBoton(btn);
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
                "Inventario de Repuestos",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));

        JPanel submenu = new JPanel(new GridLayout(4, 1, 10, 10));
        submenu.setOpaque(false);
        JButton btnListado = new JButton("Listado detallado de repuestos");
        JButton btnConsumo = new JButton("Análisis de consumo por períodos");
        JButton btnAlertas = new JButton("Alertas de productos vencidos o próximos a caducar");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnListado, btnConsumo, btnAlertas, btnAtras}) {
            estilizarBoton(btn);
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                "Mantenimiento y Reparaciones",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));

        JPanel submenu = new JPanel(new GridLayout(4, 1, 10, 10));
        submenu.setOpaque(false);
        JButton btnServicios = new JButton("Servicios más solicitados por tipo de vehículo");
        JButton btnProductividad = new JButton("Productividad de técnicos");
        JButton btnHistorial = new JButton("Historial de mantenimientos por cliente o vehículo");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnServicios, btnProductividad, btnHistorial, btnAtras}) {
            estilizarBoton(btn);
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                "Clientes",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));

        JPanel submenu = new JPanel(new GridLayout(3, 1, 10, 10));
        submenu.setOpaque(false);
        JButton btnHistorial = new JButton("Historial de servicios por cliente");
        JButton btnFrecuentes = new JButton("Clientes frecuentes y su facturación");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnHistorial, btnFrecuentes, btnAtras}) {
            estilizarBoton(btn);
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                "Proveedores",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));

        JPanel submenu = new JPanel(new GridLayout(3, 1, 10, 10));
        submenu.setOpaque(false);
        JButton btnEvaluacion = new JButton("Evaluación de proveedores");
        JButton btnHistorial = new JButton("Historial de productos entregados por proveedor");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnEvaluacion, btnHistorial, btnAtras}) {
            estilizarBoton(btn);
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
                "Campañas y Actividades Especiales",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 16),
                DEEP_BLUE
        ));

        JPanel submenu = new JPanel(new GridLayout(2, 1, 10, 10));
        submenu.setOpaque(false);
        JButton btnEvaluacion = new JButton("Evaluación de campañas de mantenimiento preventivo");
        JButton btnAtras = new JButton("⬅ Atrás");

        for (JButton btn : new JButton[]{btnEvaluacion, btnAtras}) {
            estilizarBoton(btn);
            submenu.add(btn);
        }

        JTextArea resultado = new JTextArea();
        resultado.setEditable(false);
        resultado.setBackground(ACCENT_BLUE);
        resultado.setForeground(DEEP_BLUE);
        resultado.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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

    private void estilizarBoton(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBackground(VIBRANT_BLUE);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
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

        SwingUtilities.invokeLater(() -> new ReporteView().setVisible(true));
    }
}