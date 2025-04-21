// Define que esta clase pertenece al paquete 'models'
package models;

// Clase que representa un nodo para una estructura de datos circular
public class Node {
    // Campo público que almacena el valor del nodo (entero)
    public int value;
    
    // Campo público que almacena la referencia al siguiente nodo en la estructura
    public Node next;

    // Constructor que recibe el valor para el nodo
    // Al crearse, el nodo se apunta a sí mismo, formando un ciclo
    public Node(int value) {
        this.value = value;  // Asigna el valor recibido al campo value
        this.next = this;    // Hace que el nodo se autoreferencie (estructura circular)
    }
}