import ui.EmployeeBonusCalculator;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeBonusCalculator().setVisible(true));
    }
}
