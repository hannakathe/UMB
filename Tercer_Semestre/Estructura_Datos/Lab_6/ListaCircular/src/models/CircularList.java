package models;

public class CircularList {
    // Puntero al primer nodo de la lista (cabeza)
    private Node head = null;

    // Inserta un nuevo nodo con el valor especificado al final de la lista
    public void insert(int value) {
        Node newNode = new Node(value); // Crea nuevo nodo (ya es circular por sí mismo)
        if (head == null) {
            head = newNode; // Si la lista está vacía, el nuevo nodo se convierte en la cabeza
        } else {
            Node temp = head;
            // Recorre hasta encontrar el último nodo (el que apunta a head)
            while (temp.next != head) temp = temp.next;
            // Conecta el último nodo con el nuevo
            temp.next = newNode;
            // El nuevo nodo apunta a la cabeza, manteniendo la circularidad
            newNode.next = head;
        }
    }

    // Elimina el primer nodo con el valor especificado
    public boolean delete(int value) {
        if (head == null) return false; // Lista vacía
        
        // Caso especial: único nodo en la lista
        if (head.value == value && head.next == head) {
            head = null;
            return true;
        }
        
        Node current = head, prev = null;
        do {
            if (current.value == value) {
                if (prev != null) {
                    // Eliminación de nodo intermedio
                    prev.next = current.next;
                } else {
                    // Eliminación del nodo cabeza
                    Node tail = head;
                    while (tail.next != head) tail = tail.next;
                    head = head.next;
                    tail.next = head; // Mantiene la circularidad
                }
                return true;
            }
            prev = current;
            current = current.next;
        } while (current != head); // Recorre hasta volver al inicio
        
        return false; // Valor no encontrado
    }

    // Actualiza el primer nodo con oldValue por newValue
    public boolean update(int oldValue, int newValue) {
        Node current = head;
        if (head == null) return false; // Lista vacía
        
        do {
            if (current.value == oldValue) {
                current.value = newValue; // Actualiza el valor
                return true;
            }
            current = current.next;
        } while (current != head);
        
        return false; // Valor no encontrado
    }

    // Busca un valor y devuelve su posición (índice), o -1 si no se encuentra
    public int search(int value) {
        Node current = head;
        int pos = 0;
        if (head == null) return -1; // Lista vacía
        
        do {
            if (current.value == value) return pos;
            pos++;
            current = current.next;
        } while (current != head);
        
        return -1; // Valor no encontrado
    }

    // Ordena la lista usando el algoritmo Bubble Sort adaptado para listas circulares
    public void sort() {
        if (head == null || head.next == head) return; // Lista vacía o con un solo elemento
        
        boolean swapped;
        do {
            swapped = false;
            Node current = head;
            do {
                Node nextNode = current.next;
                // Compara nodos adyacentes
                if (current != nextNode && current.value > nextNode.value) {
                    // Intercambia valores
                    int temp = current.value;
                    current.value = nextNode.value;
                    nextNode.value = temp;
                    swapped = true;
                }
                current = current.next;
            } while (current.next != head); // Recorre hasta el penúltimo nodo
        } while (swapped); // Repite si hubo intercambios
    }

    // Vacía la lista
    public void clear() {
        head = null; // El recolector de basura se encarga del resto
    }

    // Representación de la lista como cadena de texto
    @Override
    public String toString() {
        if (head == null) return "[]"; // Lista vacía
        
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        do {
            sb.append(current.value).append(", ");
            current = current.next;
        } while (current != head); // Recorre todos los nodos
        
        sb.setLength(sb.length() - 2); // Elimina la última coma y espacio
        sb.append("]");
        return sb.toString();
    }
}