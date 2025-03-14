package service; // Define el paquete donde se encuentra la clase

public class Seleccion { // Clase que implementa el algoritmo de ordenamiento por selección
    public static long[] ordenar(int[] arr, boolean ascendente) {
        // Registra el tiempo de inicio en nanosegundos y milisegundos
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length; // Obtiene el tamaño del arreglo

        // Itera sobre el arreglo para seleccionar el elemento mínimo/máximo
        for (int i = 0; i < n - 1; i++) {
            int idx = i; // Índice del elemento mínimo/máximo

            // Busca el menor/mayor elemento en el subarreglo restante
            for (int j = i + 1; j < n; j++) {
                if ((ascendente && arr[j] < arr[idx]) || (!ascendente && arr[j] > arr[idx])) {
                    idx = j; // Actualiza el índice si encuentra un nuevo mínimo/máximo
                }
            }

            // Intercambia el elemento encontrado con el actual
            int temp = arr[idx];
            arr[idx] = arr[i];
            arr[i] = temp;
        }

        // Registra el tiempo de finalización en nanosegundos y milisegundos
        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();

        // Retorna el tiempo de ejecución en milisegundos y nanosegundos
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
