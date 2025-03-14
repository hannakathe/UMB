import random
import time

# Generar lista aleatoria de n elementos dentro del rango especificado
n = 1000  # Cantidad de números en la lista
lista = [random.randint(100, 7000) for _ in range(n)]

# Mostrar la lista original antes del ordenamiento
print("Lista original:")
print(lista)

# Algoritmos de ordenamiento

# Ordenamiento de burbuja: Compara elementos adyacentes e intercambia si están en orden incorrecto
def burbuja(arr):
    n = len(arr)
    for i in range(n):
        for j in range(0, n - i - 1):  # Se reduce la cantidad de comparaciones en cada iteración
            if arr[j] > arr[j + 1]:  # Intercambia si el elemento actual es mayor que el siguiente
                arr[j], arr[j + 1] = arr[j + 1], arr[j]

# Ordenamiento por inserción: Inserta cada elemento en su posición correcta en una lista ordenada
def insercion(arr):
    for i in range(1, len(arr)):
        key = arr[i]  # Elemento a insertar en la posición correcta
        j = i - 1
        while j >= 0 and key < arr[j]:  # Mueve los elementos mayores hacia la derecha
            arr[j + 1] = arr[j]
            j -= 1
        arr[j + 1] = key  # Inserta el elemento en la posición correcta

# Ordenamiento por selección: Encuentra el elemento mínimo y lo coloca en la posición correspondiente
def seleccion(arr):
    n = len(arr)
    for i in range(n):
        min_idx = i  # Suponemos que el elemento actual es el menor
        for j in range(i + 1, n):
            if arr[j] < arr[min_idx]:  # Si encontramos un número menor, actualizamos el índice mínimo
                min_idx = j
        arr[i], arr[min_idx] = arr[min_idx], arr[i]  # Intercambiamos los valores

# Ordenamiento de Shell: Variante de inserción con intervalos decrecientes para optimizar el proceso
def shell_sort(arr):
    n = len(arr)
    gap = n // 2  # Se empieza con un espacio grande y se va reduciendo
    while gap > 0:
        for i in range(gap, n):
            temp = arr[i]
            j = i
            while j >= gap and arr[j - gap] > temp:  # Inserta elementos en la posición correcta dentro del subgrupo
                arr[j] = arr[j - gap]
                j -= gap
            arr[j] = temp  # Se coloca el elemento en su posición correcta
        gap //= 2  # Se reduce el espacio

# Función auxiliar para heap_sort: Convierte un arreglo en un montículo máximo (heap)
def heapify(arr, n, i):
    largest = i  # Suponemos que la raíz es el mayor
    l = 2 * i + 1  # Hijo izquierdo
    r = 2 * i + 2  # Hijo derecho

    if l < n and arr[l] > arr[largest]:  # Si el hijo izquierdo es mayor, actualizamos el mayor
        largest = l
    if r < n and arr[r] > arr[largest]:  # Si el hijo derecho es mayor, actualizamos el mayor
        largest = r
    if largest != i:  # Si encontramos un nuevo mayor, intercambiamos y seguimos aplicando heapify
        arr[i], arr[largest] = arr[largest], arr[i]
        heapify(arr, n, largest)

# Ordenamiento por montículos (Heap Sort): Organiza la lista usando una estructura de montículos
def heap_sort(arr):
    n = len(arr)
    # Construcción del montículo
    for i in range(n // 2 - 1, -1, -1):
        heapify(arr, n, i)
    # Extraemos los elementos del montículo y los colocamos en orden
    for i in range(n - 1, 0, -1):
        arr[i], arr[0] = arr[0], arr[i]  # Intercambiamos la raíz con el último elemento
        heapify(arr, i, 0)  # Reorganizamos el montículo

# Ordenamiento rápido (Quick Sort): Divide la lista en sublistas menores y mayores que un pivote
def quick_sort(arr):
    if len(arr) <= 1:  # Caso base: si la lista tiene 1 o 0 elementos, ya está ordenada
        return arr
    pivot = arr[len(arr) // 2]  # Se elige un pivote (generalmente en el medio)
    left = [x for x in arr if x < pivot]  # Elementos menores que el pivote
    middle = [x for x in arr if x == pivot]  # Elementos iguales al pivote
    right = [x for x in arr if x > pivot]  # Elementos mayores que el pivote
    return quick_sort(left) + middle + quick_sort(right)  # Se combinan las listas ordenadas

# Función para medir el tiempo de ejecución de cada algoritmo
def medir_tiempo(algoritmo, arr, es_quick_sort=False):
    arr_copy = arr[:]  # Copiamos la lista original para no modificarla
    start = time.time()  # Guardamos el tiempo de inicio
    if es_quick_sort:
        arr_copy = algoritmo(arr_copy)  # QuickSort devuelve una nueva lista, por eso se reasigna
    else:
        algoritmo(arr_copy)  # Para los demás algoritmos, se ordena la copia en su lugar
    end = time.time()  # Guardamos el tiempo de finalización
    print(f"{algoritmo.__name__}: {round((end - start) * 1000, 5)} ms")  # Se muestra el tiempo en milisegundos
    print("")
    print(f"Lista ordenada con {algoritmo.__name__}:")
    print("")
    print(arr_copy[:10], "...", arr_copy[-10:])  # Se muestran los primeros y últimos 10 elementos ordenados
    print("-" * 150)  # Línea divisoria

# Aplicamos cada algoritmo y medimos su tiempo de ejecución
for sort_func in [burbuja, insercion, seleccion, shell_sort, heap_sort, quick_sort]:
    medir_tiempo(sort_func, lista, es_quick_sort=(sort_func == quick_sort))
