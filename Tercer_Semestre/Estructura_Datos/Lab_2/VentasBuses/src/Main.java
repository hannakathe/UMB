import ui.FlotaUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlotaUI gui = new FlotaUI();
            gui.setVisible(true);
        });
    }
}
