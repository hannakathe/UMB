/*#include <iostream>
#include <vector>
#include <algorithm>
#include <cstdlib>
#include <ctime>
#include <chrono>

using namespace std;
using namespace std::chrono;

vector<int> generarLista(int n, int min, int max) {
    vector<int> lista(n);
    for (int i = 0; i < n; i++) {
        lista[i] = rand() % (max - min + 1) + min;
    }
    return lista;
}

void burbuja(vector<int>& arr) {
    int n = arr.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1])
                swap(arr[j], arr[j + 1]);
        }
    }
}

void quickSort(vector<int>& arr, int low, int high) {
    if (low < high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;
                swap(arr[i], arr[j]);
            }
        }
        swap(arr[i + 1], arr[high]);
        quickSort(arr, low, i);
        quickSort(arr, i + 2, high);
    }
}

int main() {
    srand(time(0));
    int n = 1000;
    vector<int> lista = generarLista(n, 100, 7000);

    auto arr_copy = lista;
    auto start = high_resolution_clock::now();
    burbuja(arr_copy);
    auto end = high_resolution_clock::now();
    cout << "Burbuja: " << duration_cast<milliseconds>(end - start).count() << " ms\n";

    arr_copy = lista;
    start = high_resolution_clock::now();
    quickSort(arr_copy, 0, n - 1);
    end = high_resolution_clock::now();
    cout << "QuickSort: " << duration_cast<milliseconds>(end - start).count() << " ms\n";

    return 0;
}
*/