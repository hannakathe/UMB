package models;  // Esta clase pertenece al paquete "models"

// Definición de la clase pública llamada "Nodo"
public class Nodo {
    // Campo público de tipo entero que almacena el valor del nodo
    public int dato;
    
    // Campo público que referencia al siguiente nodo en la estructura
    // (por defecto será null si es el último nodo)
    public Nodo siguiente;

    // Constructor de la clase Nodo que recibe un valor entero como parámetro
    public Nodo(int dato) {
        this.dato = dato;    // Asigna el valor recibido al campo 'dato'
        this.siguiente = null; // Inicializa la referencia 'siguiente' como null
                               // (indica que inicialmente no tiene nodo siguiente)
    }
}