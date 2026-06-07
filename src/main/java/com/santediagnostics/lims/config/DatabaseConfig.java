package com.santediagnostics.lims.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConfig {

    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream in = DatabaseConfig.class.getResourceAsStream("/config/application.properties")) {
            Properties props = new Properties();
            props.load(in);
            url      = props.getProperty("db.url");
            user     = props.getProperty("db.user");
            password = props.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load database config", e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(url, user, password);
    }
}
