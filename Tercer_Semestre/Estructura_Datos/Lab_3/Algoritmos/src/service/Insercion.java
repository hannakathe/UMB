package service;

public class Insercion {
    public static long[] ordenar(int[] arr, boolean ascendente) {
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && ((ascendente && arr[j] > key) || (!ascendente && arr[j] < key))) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }

        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
