package model;  
// Define el paquete al que pertenece esta clase (en este caso, "model").
// Sirve para organizar las clases dentro del proyecto.

// Importación de librerías necesarias para la conexión con la base de datos.
import java.sql.Connection;     // Representa una conexión a la base de datos.
import java.sql.DriverManager;  // Clase que maneja la conexión con la base de datos.
import java.sql.SQLException;   // Manejo de excepciones al trabajar con SQL.

public class Conexion {
    // Constante que contiene la URL de conexión a la base de datos MySQL.
    // jdbc:mysql:// → Indica el tipo de base de datos y protocolo.
    // 127.0.0.1:3306 → Dirección IP (localhost) y puerto por defecto de MySQL.
    // VENTAS → Nombre de la base de datos a la que se conectará.
    // useSSL=false → Desactiva el uso de SSL (útil en entornos locales).
    // serverTimezone=UTC → Ajusta el uso horario para evitar problemas de zona horaria.
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/VENTAS?useSSL=false&serverTimezone=UTC";

    // Usuario de la base de datos (en este caso "root").
    private static final String USER = "root";       

    // Contraseña del usuario de la base de datos (en este caso "ADMIN202#").
    private static final String PASSWORD = "ADMIN202#";       

    // Método público y estático que devuelve un objeto Connection.
    // Este método se usará en el proyecto para obtener la conexión con la base de datos.
    // Puede lanzar una excepción SQLException si ocurre un error en la conexión.
    public static Connection getConexion() throws SQLException {
        // DriverManager.getConnection() establece la conexión utilizando la URL, usuario y contraseña definidos arriba.
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
