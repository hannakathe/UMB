// Declaramos el paquete al que pertenece esta clase
package generadores;

// Importamos la librería para mostrar cuadros de diálogo en Java
import javax.swing.JOptionPane;

// Definimos la clase pública "SumaMultiplos"
public class SumaMultiplos {
    
    // Método estático que calcula la suma de los primeros "m" múltiplos de 7 y los primeros "n" múltiplos de 9
    public static void generarSuma() {
        try {
            // Pedimos al usuario que ingrese la cantidad de múltiplos de 7 a considerar
            String inputM = JOptionPane.showInputDialog("Ingresa el número m de múltiplos de 7: ");
            
            // Pedimos al usuario que ingrese la cantidad de múltiplos de 9 a considerar
            String inputN = JOptionPane.showInputDialog("Ingresa el número n de múltiplos de 9: ");
            
            // Convertimos los valores ingresados (que son Strings) a enteros
            int m = Integer.parseInt(inputM);
            int n = Integer.parseInt(inputN);
            
            // Variable para almacenar la suma de los primeros "m" múltiplos de 7
            int sumaM = 0;
            // Bucle que calcula la suma de los múltiplos de 7
            for (int i = 1; i <= m; i++) {
                sumaM += 7 * i; // Se multiplica "i" por 7 y se suma al acumulador
            }
            
            // Variable para almacenar la suma de los primeros "n" múltiplos de 9
            int sumaN = 0;
            // Bucle que calcula la suma de los múltiplos de 9
            for (int i = 1; i <= n; i++) {
                sumaN += 9 * i; // Se multiplica "i" por 9 y se suma al acumulador
            }
            
            // Construimos el mensaje con los resultados
            String resultado = "Suma de los primeros " + m + " múltiplos de 7: " + sumaM + "\n";
            resultado += "Suma de los primeros " + n + " múltiplos de 9: " + sumaN + "\n";
            resultado += "Suma total: " + (sumaM + sumaN);
            
            // Mostramos el resultado en un cuadro de diálogo
            JOptionPane.showMessageDialog(null, resultado, "Resultado de las Sumas", JOptionPane.INFORMATION_MESSAGE);
        
        } catch (NumberFormatException e) {
            // En caso de que el usuario ingrese un valor no numérico, mostramos un mensaje de error
            JOptionPane.showMessageDialog(null, "Por favor, ingresa números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
