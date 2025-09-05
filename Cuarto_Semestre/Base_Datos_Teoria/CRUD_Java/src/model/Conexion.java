package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/VENTAS?useSSL=false&serverTimezone=UTC";

    private static final String USER = "root";       // Cambia por tu usuario
    private static final String PASSWORD = "ADMIN202#";       // Cambia por tu contraseña

    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
