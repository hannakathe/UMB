// Definimos el paquete al que pertenece esta clase
package otros;

// Importamos la librería necesaria para los cuadros de diálogo
import javax.swing.*;

// Declaramos la clase pública "ImprimirArreglo"
public class ImprimirArreglo {

    // Método genérico para imprimir un arreglo de cualquier tipo (Integer, String, Boolean, etc.)
    public static <T> void imprimirArreglo(T[] arreglo) {
        // Utilizamos StringBuilder para construir la cadena de salida de manera eficiente
        StringBuilder sb = new StringBuilder();
        
        // Iteramos sobre cada elemento del arreglo
        for (T elemento : arreglo) {
            sb.append(elemento).append(" "); // Agregamos el elemento al StringBuilder con un espacio
        }

        // Mostramos el contenido del arreglo en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, sb.toString(), "Arreglo", JOptionPane.INFORMATION_MESSAGE);
    }

    // Método para solicitar al usuario un arreglo de enteros con un tamaño específico
    public static Integer[] ingresarArregloEnteros(int tamanio) {
        // Creamos un arreglo de tipo Integer con el tamaño especificado por el usuario
        Integer[] arreglo = new Integer[tamanio];

        // Bucle para solicitar cada elemento del arreglo
        for (int i = 0; i < tamanio; i++) {
            // Pedimos al usuario que ingrese un número entero
            String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo:");
            
            // Convertimos el valor ingresado de String a Integer y lo almacenamos en el arreglo
            arreglo[i] = Integer.parseInt(input);
        }
        
        // Retornamos el arreglo con los valores ingresados
        return arreglo;
    }

    // Método para solicitar al usuario un arreglo de cadenas (Strings) con un tamaño específico
    public static String[] ingresarArregloCadenas(int tamanio) {
        // Creamos un arreglo de tipo String con el tamaño especificado
        String[] arreglo = new String[tamanio];

        // Bucle para solicitar cada elemento del arreglo
        for (int i = 0; i < tamanio; i++) {
            // Pedimos al usuario que ingrese una cadena y la almacenamos en el arreglo
            arreglo[i] = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo:");
        }
        
        // Retornamos el arreglo con los valores ingresados
        return arreglo;
    }

    // Método para solicitar al usuario un arreglo de valores booleanos con un tamaño específico
    public static Boolean[] ingresarArregloBooleanos(int tamanio) {
        // Creamos un arreglo de tipo Boolean con el tamaño especificado
        Boolean[] arreglo = new Boolean[tamanio];

        // Bucle para solicitar cada elemento del arreglo
        for (int i = 0; i < tamanio; i++) {
            // Pedimos al usuario que ingrese un valor booleano (true/false)
            String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo (true/false):");
            
            // Convertimos la entrada de String a Boolean y la almacenamos en el arreglo
            arreglo[i] = Boolean.parseBoolean(input);
        }
        
        // Retornamos el arreglo con los valores ingresados
        return arreglo;
    }
}
