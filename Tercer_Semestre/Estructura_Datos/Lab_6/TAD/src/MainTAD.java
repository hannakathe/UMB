import ui.ColaUI;

public class MainTAD {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            ColaUI frame = new ColaUI();
            frame.setVisible(true);
        });
    }
}
