package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.utils.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                String dbName = ConfigManager.get("app.db");
                String user = ConfigManager.get("app.user");
                String pass = ConfigManager.get("app.password");
                String url = "jdbc:mysql://localhost:3306/" + dbName;

                connection = DriverManager.getConnection(url, user, pass);
                System.out.println("✅ Conexión a base de datos establecida con éxito.");

            } catch (SQLException e) {
                System.err.println("❌ Error al conectar a la base de datos:");
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al cerrar la conexión:");
            e.printStackTrace();
        }
    }
}
