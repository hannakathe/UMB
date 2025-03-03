package service;

public class ShellSort {
    public static long[] ordenar(int[] arr, boolean ascendente) {
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length;
        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                int j;
                for (j = i; j >= gap && ((ascendente && arr[j - gap] > temp) || (!ascendente && arr[j - gap] < temp)); j -= gap) {
                    arr[j] = arr[j - gap];
                }
                arr[j] = temp;
            }
        }

        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        return new long[]{endMilli - startMilli, endNano - startNano};
    }
}
