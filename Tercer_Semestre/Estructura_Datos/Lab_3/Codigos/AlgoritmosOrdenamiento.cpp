#include <iostream>
#include <vector>
#include <cstdlib>
#include <ctime>
#include <algorithm>
using namespace std;

vector<int> generateRandomArray(int size, int min, int max) {
    vector<int> arr(size);
    for (int &num : arr) {
        num = rand() % (max - min + 1) + min;
    }
    return arr;
}

void printArray(const vector<int> &arr) {
    size_t n = arr.size();
    for (size_t i = 0; i < min(n, size_t(10)); i++) {
        cout << arr[i] << " ";
    }
    cout << "... ";
    for (size_t i = max<size_t>(0, n > 10 ? n - 10 : 0); i < n; i++) {
        cout << arr[i] << " ";
    }
    cout << endl;
}

void burbuja(vector<int> &arr) {
    int n = arr.size();
    for (int i = 0; i < n - 1; i++) {  // Optimizado, se reduce una iteración innecesaria
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) swap(arr[j], arr[j + 1]);
        }
    }
}

void insercion(vector<int> &arr) {
    int n = arr.size();
    for (int i = 1; i < n; i++) {
        int key = arr[i];
        int j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}

void seleccion(vector<int> &arr) {
    int n = arr.size();
    for (int i = 0; i < n - 1; i++) { // Corrección: iterar hasta n - 1
        int minIdx = i;
        for (int j = i + 1; j < n; j++) {
            if (arr[j] < arr[minIdx]) minIdx = j;
        }
        swap(arr[i], arr[minIdx]);
    }
}

void shellSort(vector<int> &arr) {
    int n = arr.size();
    for (int gap = n / 2; gap > 0; gap /= 2) {
        for (int i = gap; i < n; i++) {
            int temp = arr[i];
            int j = i;
            while (j >= gap && arr[j - gap] > temp) {
                arr[j] = arr[j - gap];
                j -= gap;
            }
            arr[j] = temp;
        }
    }
}

void heapify(vector<int> &arr, int n, int i) {
    int largest = i, left = 2 * i + 1, right = 2 * i + 2;
    if (left < n && arr[left] > arr[largest]) largest = left;
    if (right < n && arr[right] > arr[largest]) largest = right;
    if (largest != i) {
        swap(arr[i], arr[largest]);
        heapify(arr, n, largest);
    }
}

void heapSort(vector<int> &arr) {
    int n = arr.size();
    for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
    for (int i = n - 1; i > 0; i--) {
        swap(arr[0], arr[i]);
        heapify(arr, i, 0);
    }
}

void quickSort(vector<int> &arr, int low, int high) {
    if (low >= high) return;
    int pivot = arr[low + (high - low) / 2];
    int i = low, j = high;
    while (i <= j) {
        while (arr[i] < pivot) i++;
        while (arr[j] > pivot) j--;
        if (i <= j) {
            swap(arr[i], arr[j]);
            i++;
            j--;
        }
    }
    quickSort(arr, low, j);
    quickSort(arr, i, high);
}

void quickSort(vector<int> &arr) {
    if (!arr.empty()) quickSort(arr, 0, arr.size() - 1);
}
