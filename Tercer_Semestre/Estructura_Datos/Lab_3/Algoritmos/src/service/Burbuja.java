package service;

public class Burbuja {
    public static long[] ordenar(int[] arr, boolean ascendente) {
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if ((ascendente && arr[j] > arr[j + 1]) || (!ascendente && arr[j] < arr[j + 1])) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }

        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
