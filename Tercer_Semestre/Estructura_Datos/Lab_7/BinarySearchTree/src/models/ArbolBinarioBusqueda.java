// Define el paquete al que pertenece la clase
package models;

// Clase que implementa un Árbol Binario de Búsqueda (BST)
public class ArbolBinarioBusqueda {
    // Atributo que representa la raíz del árbol
    public Nodo raiz;

    // Constructor: inicializa un árbol vacío (raíz nula)
    public ArbolBinarioBusqueda() {
        this.raiz = null;
    }

    // ========== MÉTODOS PRINCIPALES ==========

    // Método público para insertar un valor en el árbol
    public void insertar(int valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    // Método recursivo privado para inserción
    private Nodo insertarRecursivo(Nodo raiz, int valor) {
        // Caso base: si llegamos a un nodo nulo, creamos uno nuevo
        if (raiz == null) {
            return new Nodo(valor);
        }

        // Si el valor es menor, insertamos en el subárbol izquierdo
        if (valor < raiz.valor) {
            raiz.izquierda = insertarRecursivo(raiz.izquierda, valor);
        } 
        // Si es mayor, insertamos en el subárbol derecho
        else if (valor > raiz.valor) {
            raiz.derecha = insertarRecursivo(raiz.derecha, valor);
        }
        // Si es igual, no hacemos nada (BST no permite duplicados en esta implementación)
        return raiz;
    }

    // Método especial: inserta un nuevo nodo después de un nodo específico
    public void insertarDespuesDe(int valor, int nodoAnterior) {
        // Buscamos el nodo de referencia
        Nodo nodoX = buscarNodo(raiz, nodoAnterior);
        
        if (nodoX != null) {
            Nodo nuevoNodo = new Nodo(valor);
            
            // Si no tiene hijo derecho, lo colocamos ahí
            if (nodoX.derecha == null) {
                nodoX.derecha = nuevoNodo;
            } 
            // Si ya tiene hijo derecho, hacemos un reacomodo
            else {
                // El nuevo nodo adopta al hijo derecho existente como su hijo izquierdo
                nuevoNodo.izquierda = nodoX.derecha;
                // El nuevo nodo se convierte en el hijo derecho
                nodoX.derecha = nuevoNodo;
            }
        }
    }

    // ========== MÉTODOS DE BÚSQUEDA ==========

    // Busca un nodo con un valor específico (método privado recursivo)
    private Nodo buscarNodo(Nodo raiz, int valor) {
        // Caso base: nodo nulo o encontramos el valor
        if (raiz == null || raiz.valor == valor) {
            return raiz;
        }
        // Búsqueda en el subárbol izquierdo si el valor es menor
        if (valor < raiz.valor) {
            return buscarNodo(raiz.izquierda, valor);
        }
        // Búsqueda en el subárbol derecho si el valor es mayor
        return buscarNodo(raiz.derecha, valor);
    }

    // ========== MÉTODOS DE RECORRIDO ==========

    // Recorrido InOrden (público)
    public String inOrden() {
        return inOrdenRecursivo(raiz);
    }

    // Versión recursiva privada del InOrden
    private String inOrdenRecursivo(Nodo raiz) {
        // Caso base: nodo nulo
        if (raiz == null) {
            return "";
        }
        // Patrón InOrden: izquierda -> raíz -> derecha
        return inOrdenRecursivo(raiz.izquierda) + raiz.valor + " " + inOrdenRecursivo(raiz.derecha);
    }

    // (Aquí irían otros recorridos como PreOrden y PostOrden...)
}