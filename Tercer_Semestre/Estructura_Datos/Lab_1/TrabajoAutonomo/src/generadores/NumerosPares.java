// Declaramos el paquete al que pertenece esta clase
package generadores;

// Importamos la librería para mostrar cuadros de diálogo en Java
import javax.swing.JOptionPane;

// Definimos la clase pública "NumerosPares"
public class NumerosPares {

    // Método estático que genera y muestra los números pares en un rango dado
    public static void generadorPares() {
        // Pedimos al usuario que ingrese el número inicial del rango
        String inputP = JOptionPane.showInputDialog("Ingresa el número inicial (Entero): ");
        
        // Convertimos la entrada del usuario (que es un String) a un número entero
        int p = Integer.parseInt(inputP);

        // Pedimos al usuario que ingrese el número final del rango
        String inputQ = JOptionPane.showInputDialog("Ingresa el número final (Entero): ");
        
        // Convertimos la entrada del usuario a un número entero
        int q = Integer.parseInt(inputQ);

        // Creamos un objeto StringBuilder para almacenar los resultados en una sola cadena de texto
        StringBuilder resultado = new StringBuilder();

        // Bucle que recorre el rango de números desde p hasta q
        for (int i = p; i <= q; i++) {
            // Verificamos si el número actual es par (divisible entre 2 sin residuo)
            if (i % 2 == 0) {
                // Agregamos el número par al StringBuilder junto con un mensaje
                resultado.append(i).append(" Es un número par\n");
            }
        }

        // Mostramos el resultado en un cuadro de diálogo con el título "Números Pares"
        JOptionPane.showMessageDialog(null, resultado.toString(), "Números Pares", JOptionPane.INFORMATION_MESSAGE);
    }
}
