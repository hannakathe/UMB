package service; // Define el paquete donde se encuentra la clase

import java.util.Random; // Importa la clase Random para la selección aleatoria del pivote

public class QuickSort { // Clase que implementa el algoritmo de ordenamiento QuickSort
    public static long[] ordenar(int[] arr, boolean ascendente) {
        // Registra el tiempo de inicio en nanosegundos y milisegundos
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        // Llama a la función recursiva para ordenar el arreglo
        quickSortRec(arr, 0, arr.length - 1, ascendente);

        // Registra el tiempo de finalización en nanosegundos y milisegundos
        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();

        // Retorna el tiempo de ejecución en milisegundos y nanosegundos
        return new long[]{endMilli - startMilli, endNano - startNano};
    }

    // Método recursivo optimizado para QuickSort
    private static void quickSortRec(int[] arr, int low, int high, boolean ascendente) {
        while (low < high) { // Usa recursión limitada para evitar desbordamientos de pila
            int pi = partition(arr, low, high, ascendente); // Obtiene la posición del pivote

            // Optimiza la recursión llamando primero al lado más pequeño
            if (pi - low < high - pi) {
                quickSortRec(arr, low, pi - 1, ascendente); // Ordena la mitad izquierda
                low = pi + 1; // Continúa con la mitad derecha sin recursión adicional
            } else {
                quickSortRec(arr, pi + 1, high, ascendente); // Ordena la mitad derecha
                high = pi - 1; // Continúa con la mitad izquierda sin recursión adicional
            }
        }
    }

    // Método para dividir el arreglo en dos partes basado en un pivote aleatorio
    private static int partition(int[] arr, int low, int high, boolean ascendente) {
        Random rand = new Random();
        int randomIndex = low + rand.nextInt(high - low + 1); // Selecciona un índice aleatorio dentro del rango

        // Intercambia el pivote aleatorio con el último elemento para facilitar la partición
        int temp = arr[randomIndex];
        arr[randomIndex] = arr[high];
        arr[high] = temp;

        int pivot = arr[high]; // El pivote se elige como el último elemento
        int i = low - 1; // Índice para colocar los elementos menores/mayores que el pivote

        // Recorre el subarreglo y reubica los elementos en función del pivote
        for (int j = low; j < high; j++) {
            if ((ascendente && arr[j] < pivot) || (!ascendente && arr[j] > pivot)) {
                i++;
                // Intercambia los elementos si están en la posición incorrecta
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Coloca el pivote en su posición correcta intercambiándolo con el elemento en (i + 1)
        temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1; // Retorna la posición final del pivote
    }
}
