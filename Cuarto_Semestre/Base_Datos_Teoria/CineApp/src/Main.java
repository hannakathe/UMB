// Importar las clases necesarias para la interfaz gráfica y conexión a base de datos
import javax.swing.SwingUtilities;
import service.DBConnection;
import ui.CineFrame;
import java.sql.Connection;

// Clase principal que inicia la aplicación del sistema de cine
public class Main {
    
    // Método principal - punto de entrada de la aplicación
    public static void main(String[] args) {
        // Configurar los parámetros de conexión a la base de datos MySQL
        // URL de conexión JDBC para MySQL, incluyendo el nombre de la base de datos 'cine'
        // y el parámetro serverTimezone=UTC para evitar problemas de zona horaria
        String url = "jdbc:mysql://localhost:3306/cine?serverTimezone=UTC";
        
        // Usuario de la base de datos - por defecto 'root' en MySQL
        String user = "root";
        
        // Contraseña de la base de datos - IMPORTANTE: Cambiar por la contraseña real del sistema
        String pass = "ADMIN202#"; // CAMBIA A TU CONTRASEÑA

        try {
            // Intentar establecer conexión con la base de datos usando la clase DBConnection
            Connection con = DBConnection.getConnection(url, user, pass);
            
            // Usar SwingUtilities.invokeLater para asegurar que la interfaz gráfica
            // se cree y ejecute en el Event Dispatch Thread (EDT) de Swing
            // Esto es una buena práctica para aplicaciones Swing
            SwingUtilities.invokeLater(() -> {
                // Crear la ventana principal de la aplicación pasando la conexión como parámetro
                CineFrame frame = new CineFrame(con);
                
                // Centrar la ventana en la pantalla
                frame.setLocationRelativeTo(null);
                
                // Hacer visible la ventana principal
                frame.setVisible(true);
            });
            
        } catch (Exception e) {
            // Manejar cualquier excepción que ocurra durante la conexión
            // Imprimir el stack trace en la consola para debugging
            e.printStackTrace();
            
            // Mostrar un mensaje de error al usuario en un diálogo emergente
            javax.swing.JOptionPane.showMessageDialog(
                null, 
                "Error conectando a la base de datos: " + e.getMessage()
            );
        }
    }
}