package com.carmotors.carmotors.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String DB_NAME = "carmotors";
    private static final String USER = "root";
    private static final String PASSWORD = "Ytzhak*#2007!";
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME +
            "?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";

    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión a la base de datos establecida.");
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Error al conectar a la base de datos: " + e.getMessage());
            throw e; // Lanza la excepción para que el código que llama la maneje
        }
    }
}
