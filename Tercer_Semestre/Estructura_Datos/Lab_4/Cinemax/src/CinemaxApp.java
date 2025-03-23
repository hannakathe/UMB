import ui.CinemaxUI;

import javax.swing.*;

public class CinemaxApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CinemaxUI ui = new CinemaxUI();
            ui.setVisible(true);
        });
    }
}
