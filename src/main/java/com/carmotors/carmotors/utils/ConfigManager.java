package com.carmotors.carmotors.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static final String CONFIG_FILE = "dbconfig.properties";
    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("❌ No se pudo cargar el archivo de configuración.");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
