import javax.swing.SwingUtilities;
import ui.ABBFrame;

public class MainABB {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ABBFrame::new);
    }
}
