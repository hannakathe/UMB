import javax.swing.SwingUtilities;
import service.DBConnection;
import ui.CineFrame;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        // Ajusta credenciales si es necesario
        String url = "jdbc:mysql://localhost:3306/cine?serverTimezone=UTC";
        String user = "root";
        String pass = "ADMIN202#"; // CAMBIA A TU CONTRASEÑA

        try {
            Connection con = DBConnection.getConnection(url, user, pass);
            SwingUtilities.invokeLater(() -> {
                CineFrame frame = new CineFrame(con);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Error conectando a la base de datos: " + e.getMessage());
        }
    }
}
