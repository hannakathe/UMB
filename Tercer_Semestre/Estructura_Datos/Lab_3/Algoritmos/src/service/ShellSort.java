package service; // Define el paquete donde se encuentra la clase

public class ShellSort { // Clase que implementa el algoritmo Shell Sort
    public static long[] ordenar(int[] arr, boolean ascendente) {
        // Registra el tiempo de inicio en nanosegundos y milisegundos
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length; // Obtiene el tamaño del arreglo

        // Se define el tamaño del salto inicial (gap) como la mitad del tamaño del arreglo
        for (int gap = n / 2; gap > 0; gap /= 2) {
            // Recorre el arreglo desde la posición del gap hasta el final
            for (int i = gap; i < n; i++) {
                int temp = arr[i]; // Almacena el valor actual
                int j;

                // Realiza la inserción desplazando los elementos según el gap
                for (j = i; j >= gap && ((ascendente && arr[j - gap] > temp) || (!ascendente && arr[j - gap] < temp)); j -= gap) {
                    arr[j] = arr[j - gap]; // Desplaza los elementos hacia adelante
                }

                arr[j] = temp; // Coloca el elemento en su posición correcta
            }
        }

        // Registra el tiempo de finalización en nanosegundos y milisegundos
        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();

        // Retorna el tiempo de ejecución en milisegundos y nanosegundos
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
