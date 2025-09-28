package services;

import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/cine_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // <- reemplaza
    private static final String PASS = "tu_password"; // <- reemplaza

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void close(AutoCloseable ac) {
        if (ac == null) return;
        try { ac.close(); } catch (Exception e) { /* ignore */ }
    }
}
