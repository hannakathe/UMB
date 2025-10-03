package service;

// Importa las clases necesarias para manejar la conexión con la base de datos
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase encargada de gestionar la conexión con la base de datos
public class DBConnection {

    // Método estático que devuelve un objeto Connection para conectarse a la BD
    // Recibe como parámetros la URL, el usuario y la contraseña de la base de datos
    public static Connection getConnection(String url, String user, String pass) 
            throws ClassNotFoundException, SQLException {
        
        // Carga el driver de MySQL en memoria
        // "com.mysql.cj.jdbc.Driver" es el driver JDBC de MySQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // Devuelve una conexión activa utilizando el DriverManager
        // Se conecta a la BD con la URL, usuario y contraseña indicados
        return DriverManager.getConnection(url, user, pass);
    }
}
