package otros;

import javax.swing.JOptionPane;
import java.util.Random;

public class RecorrerMatrizInversa {
    private int[][] matriz;
    private static final int SIZE = 10;

    public RecorrerMatrizInversa() {
        matriz = new int[SIZE][SIZE];
        llenarMatriz();
    }

    // Método para llenar la matriz con números aleatorios entre 0 y 99
    private void llenarMatriz() {
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matriz[i][j] = random.nextInt(100);
            }
        }
    }

    // Método para imprimir la matriz en orden inverso y mostrar el conteo
    public void imprimirMatrizInversa() {
        int count = 0;
        StringBuilder resultado = new StringBuilder();
        
        for (int i = SIZE - 1; i >= 0; i--) {
            for (int j = SIZE - 1; j >= 0; j--) {
                resultado.append(matriz[i][j]).append(" ");
                count++;

                if (count % SIZE == 0) {
                    resultado.append("\nConteo de elementos: ").append(count).append("\n");
                }
            }
        }

        // Mostrar el resultado en un cuadro de mensaje
        JOptionPane.showMessageDialog(null, resultado.toString(), "Matriz Inversa", JOptionPane.INFORMATION_MESSAGE);
    }
}
