// Definimos el paquete al que pertenece esta clase
package otros;

// Importamos la librería necesaria para los cuadros de diálogo
import javax.swing.*;

public class ImprimirArreglo {

    // Método genérico para imprimir un arreglo de cualquier tipo
    public static <T> void imprimirArreglo(T[] arreglo) {
        StringBuilder sb = new StringBuilder();
        for (T elemento : arreglo) {
            sb.append(elemento).append(" ");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Arreglo", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para solicitar al usuario un arreglo de enteros
    public static Integer[] ingresarArregloEnteros(int tamaño) {
        Integer[] arreglo = new Integer[tamaño];
        for (int i = 0; i < tamaño; i++) {
            while (true) { // Se usa un bucle infinito para asegurar que el usuario ingrese un valor válido antes de continuar
                try {
                    String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo:");
                    if (input == null) { // Si el usuario cancela la entrada
                        JOptionPane.showMessageDialog(null, "Entrada cancelada. Se asignará 0.");
                        arreglo[i] = 0;
                        break;
                    }
                    arreglo[i] = Integer.parseInt(input);
                    break; // Salimos del bucle si la conversión es exitosa
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Se muestra un mensaje de error si el usuario introduce un valor no numérico.");
                }
            }
        }
        return arreglo;
    }

    // Método para solicitar un arreglo de cadenas
    public static String[] ingresarArregloCadenas(int tamaño) {
        String[] arreglo = new String[tamaño];
        for (int i = 0; i < tamaño; i++) {
            while (true) { // Se asegura que el usuario no deje el campo vacío
                String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo:");
                if (input == null || input.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Entrada vacía o cancelada. Se asignará una cadena vacía.");
                    arreglo[i] = "";
                } else {
                    arreglo[i] = input;
                }
                break;
            }
        }
        return arreglo;
    }

    // Método para solicitar un arreglo de valores booleanos
    public static Boolean[] ingresarArregloBooleanos(int tamaño) {
        Boolean[] arreglo = new Boolean[tamaño];
        for (int i = 0; i < tamaño; i++) {
            while (true) { // Se usa un bucle para validar la entrada del usuario
                String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo (true/false):");
                if (input == null) { // Si el usuario cancela la entrada
                    JOptionPane.showMessageDialog(null, "Entrada cancelada. Se asignará false.");
                    arreglo[i] = false;
                    break;
                }
                input = input.trim().toLowerCase(); // Se normaliza la entrada para evitar errores
                if (input.equals("true") || input.equals("false")) {
                    arreglo[i] = Boolean.parseBoolean(input);
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, ingrese 'true' o 'false'.");
                }
            }
        }
        return arreglo;
    }
}
