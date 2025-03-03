function generarLista(n, min, max) {
    return Array.from({ length: n }, () => Math.floor(Math.random() * (max - min + 1) + min));
}

function burbuja(arr) {
    let n = arr.length;
    for (let i = 0; i < n - 1; i++) {
        for (let j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                [arr[j], arr[j + 1]] = [arr[j + 1], arr[j]];
            }
        }
    }
}

function quickSort(arr) {
    if (arr.length <= 1) return arr;
    let pivot = arr[Math.floor(arr.length / 2)];
    let left = arr.filter(x => x < pivot);
    let middle = arr.filter(x => x === pivot);
    let right = arr.filter(x => x > pivot);
    return [...quickSort(left), ...middle, ...quickSort(right)];
}

let n = 1000;
let lista = generarLista(n, 100, 7000);

let arr_copy = [...lista];
let start = performance.now();
burbuja(arr_copy);
let end = performance.now();
console.log(`Burbuja: ${(end - start).toFixed(5)} ms`);

start = performance.now();
let sorted = quickSort([...lista]);
end = performance.now();
console.log(`QuickSort: ${(end - start).toFixed(5)} ms`);
