package service; // Define el paquete donde se encuentra la clase

public class HeapSort { // Clase que implementa el algoritmo de ordenamiento HeapSort
    public static long[] ordenar(int[] arr, boolean ascendente) {
        // Registra el tiempo de inicio en nanosegundos y milisegundos
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length; // Obtiene la longitud del arreglo

        // Construye el heap (montículo) reorganizando el arreglo
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, ascendente);
        }

        // Extrae elementos uno por uno del heap
        for (int i = n - 1; i > 0; i--) {
            // Mueve la raíz actual (mayor o menor elemento) al final
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;

            // Llama a heapify en la parte reducida del heap
            heapify(arr, i, 0, ascendente);
        }

        // Registra el tiempo de finalización en nanosegundos y milisegundos
        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        
        // Retorna el tiempo de ejecución en milisegundos y nanosegundos
        return new long[]{endMilli - startMilli, endNano - startNano};
    }

    // Función para reorganizar el subárbol con raíz en el índice i
    private static void heapify(int[] arr, int n, int i, boolean ascendente) {
        int largest = i; // Inicializa el nodo más grande como raíz
        int left = 2 * i + 1; // Calcula el índice del hijo izquierdo
        int right = 2 * i + 2; // Calcula el índice del hijo derecho

        // Verifica si el hijo izquierdo es mayor/menor que la raíz según el orden deseado
        if (left < n && ((ascendente && arr[left] > arr[largest]) || (!ascendente && arr[left] < arr[largest]))) {
            largest = left;
        }

        // Verifica si el hijo derecho es mayor/menor que la raíz según el orden deseado
        if (right < n && ((ascendente && arr[right] > arr[largest]) || (!ascendente && arr[right] < arr[largest]))) {
            largest = right;
        }

        // Si el nodo raíz no es el mayor/menor, intercambia y continúa reorganizando
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Llama recursivamente para asegurar la propiedad del heap
            heapify(arr, n, largest, ascendente);
        }
    }
}
