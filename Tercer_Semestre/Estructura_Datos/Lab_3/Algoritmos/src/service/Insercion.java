package service; // Define el paquete donde se encuentra la clase

public class Insercion { // Clase que implementa el algoritmo de ordenamiento por inserción
    public static long[] ordenar(int[] arr, boolean ascendente) {
        // Registra el tiempo de inicio en nanosegundos y milisegundos
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        // Recorre el arreglo desde la segunda posición hasta el final
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i]; // Elemento a insertar en la posición correcta
            int j = i - 1;

            // Desplaza los elementos mayores/menores a la derecha para hacer espacio
            while (j >= 0 && ((ascendente && arr[j] > key) || (!ascendente && arr[j] < key))) {
                arr[j + 1] = arr[j]; // Mueve el elemento una posición a la derecha
                j--;
            }
            arr[j + 1] = key; // Inserta el elemento en su posición correcta
        }

        // Registra el tiempo de finalización en nanosegundos y milisegundos
        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();

        // Retorna el tiempo de ejecución en milisegundos y nanosegundos
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
