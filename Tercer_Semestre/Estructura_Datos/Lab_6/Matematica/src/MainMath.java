import ui.CalculatorUI;

public class MainMath {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            CalculatorUI ui = new CalculatorUI();
            ui.setVisible(true);
        });
    }
}
