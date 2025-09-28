import modelservice.ModelService;
import ui.UI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicializa la BD leyendo database/init_db.sql si existe
        ModelService.initDatabase();

        // Lanzar UI en el EDT de Swing
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.setVisible(true);
        });
    }
}
