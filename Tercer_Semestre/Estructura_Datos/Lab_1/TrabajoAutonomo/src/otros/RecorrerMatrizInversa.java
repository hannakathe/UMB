// Definimos el paquete al que pertenece esta clase
package otros;

// Importamos JOptionPane para mostrar cuadros de diálogo y Random para generar números aleatorios
import javax.swing.JOptionPane;
import java.util.Random;

// Definimos la clase "RecorrerMatrizInversa"
public class RecorrerMatrizInversa {
    
    // Declaramos una matriz bidimensional de enteros
    private int[][] matriz;
    
    // Definimos una constante que indica el tamaño de la matriz (10x10)
    private static final int SIZE = 10;

    // Constructor de la clase que inicializa la matriz y la llena con valores aleatorios
    public RecorrerMatrizInversa() {
        matriz = new int[SIZE][SIZE]; // Se crea una matriz de 10x10
        llenarMatriz(); // Se llena la matriz con números aleatorios
    }

    // Método privado para llenar la matriz con números aleatorios entre 0 y 99
    private void llenarMatriz() {
        Random random = new Random(); // Se crea un objeto Random para generar números aleatorios
        
        // Se recorre la matriz fila por fila
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matriz[i][j] = random.nextInt(100); // Se asigna un número aleatorio entre 0 y 99
            }
        }
    }

    // Método para imprimir la matriz en orden inverso y mostrar un conteo de elementos procesados
    public void imprimirMatrizInversa() {
        int count = 0; // Variable para contar los elementos recorridos
        StringBuilder resultado = new StringBuilder(); // Se usa StringBuilder para construir la salida de manera eficiente
        
        // Se recorre la matriz en orden inverso (de la última fila a la primera, y de la última columna a la primera)
        for (int i = SIZE - 1; i >= 0; i--) {
            for (int j = SIZE - 1; j >= 0; j--) {
                resultado.append(matriz[i][j]).append(" "); // Se agrega el elemento actual al StringBuilder
                count++; // Se incrementa el contador de elementos
                
                // Cada vez que se ha procesado un número igual al tamaño de la fila (10 elementos), se agrega una línea con el conteo
                if (count % SIZE == 0) {
                    resultado.append("\nConteo de elementos: ").append(count).append("\n");
                }
            }
        }

        // Se muestra el resultado en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, resultado.toString(), "Matriz Inversa", JOptionPane.INFORMATION_MESSAGE);
    }
}
