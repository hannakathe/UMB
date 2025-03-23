package services;

import java.util.HashMap;
import java.util.List;

// Clase que proporciona un servicio de búsqueda de números en una lista ordenada
public class BinarySearchService {

    // Método para buscar números en una lista ordenada y medir el tiempo de búsqueda
    public static HashMap<Integer, Long[]> searchNumbers(List<Integer> sortedList, List<Integer> numbersToSearch) {
        // HashMap para almacenar los resultados de la búsqueda
        // La clave es el número buscado, y el valor es un arreglo de Long que contiene:
        // [0] el índice del número en la lista (o -1 si no se encuentra),
        // [1] el tiempo de búsqueda en milisegundos,
        // [2] el tiempo de búsqueda en nanosegundos.
        HashMap<Integer, Long[]> searchResults = new HashMap<>();

        // Iterar sobre cada número que se desea buscar
        for (int number : numbersToSearch) {
            // Registrar el tiempo de inicio de la búsqueda en nanosegundos
            long startTime = System.nanoTime();

            // Buscar el número en la lista ordenada usando el método indexOf
            // Este método realiza una búsqueda lineal, no una búsqueda binaria
            int index = sortedList.indexOf(number);

            // Registrar el tiempo de finalización de la búsqueda en nanosegundos
            long endTime = System.nanoTime();

            // Calcular el tiempo transcurrido en milisegundos y nanosegundos
            long elapsedTimeMs = (endTime - startTime) / 1_000_000; // Convertir a milisegundos
            long elapsedTimeNs = (endTime - startTime); // Tiempo en nanosegundos

            // Almacenar los resultados en el HashMap
            searchResults.put(number, new Long[]{(long) index, elapsedTimeMs, elapsedTimeNs});
        }

        // Devolver el HashMap con los resultados de las búsquedas
        return searchResults;
    }
}