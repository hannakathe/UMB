package service;

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
        if (low < high) {
            int pi = partition(arr, low, high, ascendente);
            quickSortRec(arr, low, pi - 1, ascendente);
            quickSortRec(arr, pi + 1, high, ascendente);
        }
    }

    private static int partition(int[] arr, int low, int high, boolean ascendente) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if ((ascendente && arr[j] < pivot) || (!ascendente && arr[j] > pivot)) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        arr[i + 1] = arr[high];
        arr[high] = arr[i + 1];
        return i + 1;
    }
}
