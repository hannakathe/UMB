// Declaramos el paquete al que pertenece esta clase
package generadores;

// Importamos la librería para mostrar cuadros de diálogo en Java
import javax.swing.JOptionPane;

// Definimos la clase pública "MultiplosDeTres"
public class MultiplosDeTres {

    // Método estático que genera y muestra múltiplos de 3
    public static void generadorMultiplo() {
        // Pedimos al usuario que ingrese la cantidad de múltiplos de 3 a generar
        String inputCantidad = JOptionPane.showInputDialog("Ingresa el número de múltiplos de 3 a generar: ");
        
        // Convertimos la entrada del usuario (que es un String) a un número entero
        int cantidad = Integer.parseInt(inputCantidad); 
        
        // Creamos un objeto StringBuilder para almacenar los resultados en una sola cadena de texto
        StringBuilder resultado = new StringBuilder();
        
        // Bucle que genera la cantidad de múltiplos de 3 solicitada
        for (int i = 1; i <= cantidad; i++) {
            // Agregamos cada múltiplo de 3 al StringBuilder
            resultado.append("Múltiplo #").append(i).append(" de 3: ").append(i * 3).append("\n");
        }

        // Mostramos el resultado en un cuadro de diálogo con el título "Múltiplos de 3"
        JOptionPane.showMessageDialog(null, resultado.toString(), "Múltiplos de 3", JOptionPane.INFORMATION_MESSAGE);
    }
}
