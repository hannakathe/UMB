package models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Clase que representa un modelo para realizar búsquedas binarias en una lista de números ordenados
public class BinarySearchModel {
    // Lista de números ordenados
    private List<Integer> sortedNumbers;

    // Constructor que recibe un arreglo de números ordenados y lo convierte en una lista
    public BinarySearchModel(int[] sortedArray) {
        // Convierte el arreglo de enteros en una lista de enteros utilizando streams
        this.sortedNumbers = Arrays.stream(sortedArray).boxed().collect(Collectors.toList());
    }

    // Método para obtener el índice de un número en la lista
    public int getIndexOf(int number) {
        // Utiliza el método indexOf de la lista para encontrar el índice del número
        return sortedNumbers.indexOf(number);
    }

    // Método para obtener la lista de números ordenados
    public List<Integer> getSortedNumbers() {
        return sortedNumbers;
    }
}