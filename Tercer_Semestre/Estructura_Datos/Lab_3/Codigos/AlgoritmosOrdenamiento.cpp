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
    for (size_t i = 0; i < min(arr.size(), size_t(10)); i++) {
        cout << arr[i] << " ";
    }
    cout << "... ";
    for (size_t i = max(0, (int)arr.size() - 10); i < arr.size(); i++) {
        cout << arr[i] << " ";
    }
    cout << endl;
}

void burbuja(vector<int> &arr) {
    int n = arr.size();
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) swap(arr[j], arr[j + 1]);
        }
    }
}

void insercion(vector<int> &arr) {
    for (size_t i = 1; i < arr.size(); i++) {
        int key = arr[i], j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}

void seleccion(vector<int> &arr) {
    int n = arr.size();
    for (int i = 0; i < n; i++) {
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
            int temp = arr[i], j = i;
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

vector<int> quickSort(vector<int> arr) {
    if (arr.size() <= 1) return arr;
    int pivot = arr[arr.size() / 2];
    vector<int> left, middle, right;
    for (int x : arr) {
        if (x < pivot) left.push_back(x);
        else if (x == pivot) middle.push_back(x);
        else right.push_back(x);
    }
    left = quickSort(left);
    right = quickSort(right);
    left.insert(left.end(), middle.begin(), middle.end());
    left.insert(left.end(), right.begin(), right.end());
    return left;
}
