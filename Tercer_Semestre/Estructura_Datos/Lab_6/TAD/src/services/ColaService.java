package services;  // Esta clase pertenece al paquete "services"

import models.Cola;  // Importa la clase Cola del paquete models

// Definición de la clase pública ColaService que actúa como intermediario para operaciones con colas
public class ColaService {
    
    // Campo privado que almacena una instancia de la clase Cola (composición)
    private Cola cola;

    // Constructor que inicializa el servicio creando una nueva instancia de Cola
    public ColaService() {
        this.cola = new Cola();  // Crea una cola vacía al instanciar el servicio
    }

    // Método para agregar un elemento a la cola
    public void agregarElemento(int dato) {
        cola.encolar(dato);  // Delega la operación al método encolar() de la Cola
    }

    // Método para eliminar y retornar el primer elemento de la cola (FIFO)
    public int eliminarElemento() {
        return cola.desencolar();  // Delega la operación al método desencolar() de la Cola
    }

    // Método para obtener una representación en String del estado actual de la cola
    public String obtenerCola() {
        return cola.mostrar();  // Delega la operación al método mostrar() de la Cola
    }

    // Método que verifica si la cola está vacía
    public boolean estaVacia() {
        return cola.estaVacia();  // Delega la verificación al método estaVacia() de la Cola
    }

    // Método para vaciar completamente la cola
    public void limpiarCola() {
        cola.vaciar();  // Delega la operación al método vaciar() de la Cola
    }
}