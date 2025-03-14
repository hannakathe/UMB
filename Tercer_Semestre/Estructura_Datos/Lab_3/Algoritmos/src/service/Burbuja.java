package service; // Define el paquete donde se encuentra la clase

public class Burbuja { // Clase que implementa el algoritmo de ordenamiento de burbuja
    public static long[] ordenar(int[] arr, boolean ascendente) {
        // Registra el tiempo de inicio en nanosegundos y milisegundos
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length; // Obtiene la longitud del arreglo
        // Bucle externo para recorrer el arreglo
        for (int i = 0; i < n - 1; i++) {
            // Bucle interno para comparar y ordenar elementos adyacentes
            for (int j = 0; j < n - i - 1; j++) {
                // Verifica si el orden debe ser ascendente o descendente
                if ((ascendente && arr[j] > arr[j + 1]) || (!ascendente && arr[j] < arr[j + 1])) {
                    // Intercambia los elementos si están en el orden incorrecto
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        // Registra el tiempo de finalización en nanosegundos y milisegundos
        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        
        // Retorna el tiempo de ejecución en milisegundos y nanosegundos
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
