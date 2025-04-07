import ui.CantantesGUI;

public class MainC {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new CantantesGUI().setVisible(true);
        });
    }
}
