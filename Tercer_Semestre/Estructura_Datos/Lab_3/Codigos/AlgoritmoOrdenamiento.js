function generateRandomArray(size, min, max) {
    return Array.from({ length: size }, () => Math.floor(Math.random() * (max - min + 1)) + min);
}

let n = 1000;
let array = generateRandomArray(n, 100, 7000);

console.log("Lista original:");
console.log(array);

// Algoritmos de ordenamiento
function burbuja(arr) {
    let n = arr.length;
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];
            }
        }
    }
}

function insercion(arr) {
    for (let i = 1; i < arr.length; i++) {
        let key = arr[i];
        let j = i - 1;
        while (j >= 0 && arr[j] > key) {
            arr[j + 1] = arr[j];
            j--;
        }
        arr[j + 1] = key;
    }
}

function seleccion(arr) {
    let n = arr.length;
    for (let i = 0; i < n; i++) {
        let minIdx = i;
        for (let j = i + 1; j < n; j++) {
            if (arr[j] < arr[minIdx]) {
                minIdx = j;
            }
        }
        [arr[i], arr[minIdx]] = [arr[minIdx], arr[i]];
    }
}

function shellSort(arr) {
    let n = arr.length;
    let gap = Math.floor(n / 2);
    while (gap > 0) {
        for (let i = gap; i < n; i++) {
            let temp = arr[i];
            let j = i;
            while (j >= gap && arr[j - gap] > temp) {
                arr[j] = arr[j - gap];
                j -= gap;
            }
            arr[j] = temp;
        }
        gap = Math.floor(gap / 2);
    }
}

function heapify(arr, n, i) {
    let largest = i;
    let left = 2 * i + 1;
    let right = 2 * i + 2;
    if (left < n && arr[left] > arr[largest]) largest = left;
    if (right < n && arr[right] > arr[largest]) largest = right;
    if (largest !== i) {
        [arr[i], arr[largest]] = [arr[largest], arr[i]];
        heapify(arr, n, largest);
    }
}

function heapSort(arr) {
    let n = arr.length;
    for (let i = Math.floor(n / 2) - 1; i >= 0; i--) {
        heapify(arr, n, i);
    }
    for (let i = n - 1; i > 0; i--) {
        [arr[0], arr[i]] = [arr[i], arr[0]];
        heapify(arr, i, 0);
    }
}

function quickSort(arr) {
    if (arr.length <= 1) return arr;
    let pivot = arr[Math.floor(arr.length / 2)];
    let left = arr.filter(x => x < pivot);
    let middle = arr.filter(x => x === pivot);
    let right = arr.filter(x => x > pivot);
    return quickSort(left).concat(middle, quickSort(right));
}

// Medición de tiempos
function measureTime(algorithm, arr, isQuickSort = false) {
    let arrCopy = [...arr];
    let start = performance.now();
    if (isQuickSort) {
        arrCopy = algorithm(arrCopy);
    } else {
        algorithm(arrCopy);
    }
    let end = performance.now();
    console.log(`${algorithm.name}: ${(end - start).toFixed(5)} ms`);
    console.log(`Lista ordenada con ${algorithm.name}:`);
    console.log(arrCopy.slice(0, 10), "...", arrCopy.slice(-10));
    console.log("-".repeat(150));
}

[burbuja, insercion, seleccion, shellSort, heapSort, quickSort].forEach(algo => {
    measureTime(algo, array, algo === quickSort);
});
