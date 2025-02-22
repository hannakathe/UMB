// Definimos el paquete al que pertenece esta clase.
package service;

// Importamos la clase Random para generar números aleatorios.
import java.util.Random;

// Importamos las clases necesarias para manejar cuadros de diálogo y tablas en Swing.
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Definimos la clase "ArrayService", que proporciona métodos para crear y manipular arrays.
public class ArrayService {

    // Declaramos una instancia de Random como una constante final para generar valores aleatorios.
    private final Random rand = new Random();

    // MÉTODO PARA CREAR UN ARRAY UNIDIMENSIONAL (VECTOR)
    public int[] createUnidimensional(int size, boolean randomFill) {
        // Se crea un array de tamaño "size".
        int[] array = new int[size];

        // Se recorre cada posición del array para llenarlo con valores.
        for (int i = 0; i < size; i++) {
            // Si "randomFill" es true, se llena con un número aleatorio entre 0 y 99.
            // Si es false, se solicita al usuario que ingrese un valor a través de un cuadro de diálogo.
            array[i] = randomFill ? rand.nextInt(100) : Integer.parseInt(
                JOptionPane.showInputDialog("Ingrese valor para x=" + i)
            );
        }

        // Se devuelve el array generado.
        return array;
    }

    // MÉTODO PARA CREAR UN ARRAY BIDIMENSIONAL (MATRIZ)
    public int[][] createBidimensional(int rows, int cols, boolean randomFill) {
        // Se crea una matriz con la cantidad de filas y columnas especificadas.
        int[][] array = new int[rows][cols];

        // Se recorren todas las posiciones de la matriz.
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                // Si "randomFill" es true, se asigna un valor aleatorio.
                // Si es false, se solicita al usuario ingresar un valor.
                array[x][y] = randomFill ? rand.nextInt(100) : Integer.parseInt(
                    JOptionPane.showInputDialog("Ingrese valor para x=" + x + ", y=" + y)
                );
            }
        }

        // Se devuelve la matriz generada.
        return array;
    }

    // MÉTODO PARA CREAR UN ARRAY TRIDIMENSIONAL (CUBO DE VALORES)
    public int[][][] createTridimensional(int depth, int rows, int cols, boolean randomFill) {
        // Se crea una estructura tridimensional con las dimensiones especificadas.
        int[][][] array = new int[depth][rows][cols];

        // Se recorren todas las posiciones de la estructura tridimensional.
        for (int z = 0; z < depth; z++) { // Profundidad
            for (int x = 0; x < rows; x++) { // Filas
                for (int y = 0; y < cols; y++) { // Columnas
                    // Si "randomFill" es true, se asigna un valor aleatorio.
                    // Si es false, se solicita al usuario ingresar un valor.
                    array[z][x][y] = randomFill ? rand.nextInt(100) : Integer.parseInt(
                        JOptionPane.showInputDialog("Ingrese valor para z=" + z + ", x=" + x + ", y=" + y)
                    );
                }
            }
        }

        // Se devuelve la estructura tridimensional generada.
        return array;
    }

    // MÉTODO PARA CREAR UNA TABLA A PARTIR DE UN ARRAY BIDIMENSIONAL
    public JTable createTableFromArray(int[][] array) {
        // Se crea un arreglo de nombres de columna basado en la cantidad de columnas del array.
        String[] columnNames = new String[array[0].length];
        for (int i = 0; i < array[0].length; i++) {
            columnNames[i] = "y=" + i; // Se asignan nombres de columna según el índice.
        }

        // Se crea un modelo de tabla con los nombres de columna.
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Se recorren las filas del array para agregar cada una a la tabla.
        for (int x = 0; x < array.length; x++) {
            // Se crea una fila como un arreglo de objetos.
            Object[] row = new Object[array[x].length];

            // Se llena la fila con los valores de la matriz.
            for (int y = 0; y < array[x].length; y++) {
                row[y] = array[x][y];
            }

            // Se añade la fila al modelo de la tabla.
            model.addRow(row);
        }

        // Se devuelve una nueva JTable con el modelo creado.
        return new JTable(model);
    }
}
