package service;

import java.util.Random;

public class QuickSort {
    public static long[] ordenar(int[] arr, boolean ascendente) {
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        quickSortRec(arr, 0, arr.length - 1, ascendente);

        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        return new long[]{endMilli - startMilli, endNano - startNano};
    }

    private static void quickSortRec(int[] arr, int low, int high, boolean ascendente) {
        while (low < high) { // Usa recursión limitada para evitar desbordamientos
            int pi = partition(arr, low, high, ascendente);
            
            // Optimiza el lado más pequeño primero para evitar profundidad excesiva
            if (pi - low < high - pi) {
                quickSortRec(arr, low, pi - 1, ascendente);
                low = pi + 1;
            } else {
                quickSortRec(arr, pi + 1, high, ascendente);
                high = pi - 1;
            }
        }
    }

    private static int partition(int[] arr, int low, int high, boolean ascendente) {
        Random rand = new Random();
        int randomIndex = low + rand.nextInt(high - low + 1);
        
        // Intercambia el pivote aleatorio con el último elemento
        int temp = arr[randomIndex];
        arr[randomIndex] = arr[high];
        arr[high] = temp;

        int pivot = arr[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if ((ascendente && arr[j] < pivot) || (!ascendente && arr[j] > pivot)) {
                i++;
                temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        // Corrección del intercambio
        temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }
}
