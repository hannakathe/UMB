package generadores;
import javax.swing.JOptionPane;

public class SumaMultiplos {
    
    // Método para generar la suma de los primeros m múltiplos de 7 y los primeros n múltiplos de 9
    public static void generarSuma() {
        try {
            // Pedir al usuario el número de múltiplos de 7 (m) y de 9 (n)
            String inputM = JOptionPane.showInputDialog("Ingresa el número m de múltiplos de 7: ");
            String inputN = JOptionPane.showInputDialog("Ingresa el número n de múltiplos de 9: ");
            
            int m = Integer.parseInt(inputM);
            int n = Integer.parseInt(inputN);
            
            // Calcular la suma de los primeros m múltiplos de 7
            int sumaM = 0;
            for (int i = 1; i <= m; i++) {
                sumaM += 7 * i;
            }
            
            // Calcular la suma de los primeros n múltiplos de 9
            int sumaN = 0;
            for (int i = 1; i <= n; i++) {
                sumaN += 9 * i;
            }
            
            // Mostrar los resultados en una ventana de mensaje
            String resultado = "Suma de los primeros " + m + " múltiplos de 7: " + sumaM + "\n";
            resultado += "Suma de los primeros " + n + " múltiplos de 9: " + sumaN + "\n";
            resultado += "Suma total: " + (sumaM + sumaN);
            
            JOptionPane.showMessageDialog(null, resultado, "Resultado de las Sumas", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}

