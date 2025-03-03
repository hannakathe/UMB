package service;

public class HeapSort {
    public static long[] ordenar(int[] arr, boolean ascendente) {
        long startNano = System.nanoTime();
        long startMilli = System.currentTimeMillis();

        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, ascendente);
        }
        for (int i = n - 1; i > 0; i--) {
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            heapify(arr, i, 0, ascendente);
        }

        long endNano = System.nanoTime();
        long endMilli = System.currentTimeMillis();
        return new long[]{endMilli - startMilli, endNano - startNano};
    }

    private static void heapify(int[] arr, int n, int i, boolean ascendente) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n && ((ascendente && arr[left] > arr[largest]) || (!ascendente && arr[left] < arr[largest]))) {
            largest = left;
        }
        if (right < n && ((ascendente && arr[right] > arr[largest]) || (!ascendente && arr[right] < arr[largest]))) {
            largest = right;
        }
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;
            heapify(arr, n, largest, ascendente);
        }
    }
}
