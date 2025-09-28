import modelservice.ModelService;
import ui.UI;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Inicializar BD (ejecuta database/init_db.sql si existe)
        ModelService.initDatabase();

        // Lanzar interfaz en EDT
        SwingUtilities.invokeLater(() -> {
            UI ui = new UI();
            ui.setVisible(true);
        });
    }
}
