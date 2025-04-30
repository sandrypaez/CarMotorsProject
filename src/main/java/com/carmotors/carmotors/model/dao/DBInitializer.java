package com.carmotors.carmotors.model.dao;

import com.carmotors.carmotors.utils.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {
    public static void inicializar() {
        try (Connection conn = DriverManager.getConnection(
                ConfigManager.get("admin.url"),
                ConfigManager.get("admin.user"),
                ConfigManager.get("admin.password"));
             Statement stmt = conn.createStatement()) {

            String db = ConfigManager.get("app.db");
            String user = ConfigManager.get("app.user");
            String pass = ConfigManager.get("app.password");

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + db);
            stmt.executeUpdate("CREATE USER IF NOT EXISTS '" + user + "'@'localhost' IDENTIFIED BY '" + pass + "'");
            stmt.executeUpdate("GRANT ALL PRIVILEGES ON " + db + ".* TO '" + user + "'@'localhost'");
            System.out.println("✅ Base de datos y usuario asegurados.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Error al inicializar base de datos.");
        }
    }
}
