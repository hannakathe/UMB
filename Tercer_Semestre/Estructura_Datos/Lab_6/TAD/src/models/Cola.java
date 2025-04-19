package models;  // Esta clase pertenece al paquete "models"

public class Cola {
    // Puntero al primer nodo de la cola (donde se sacan elementos)
    private Nodo frente;
    // Puntero al último nodo de la cola (donde se agregan elementos)
    private Nodo fin;

    // Constructor que inicializa una cola vacía
    public Cola() {
        this.frente = this.fin = null;  // Ambos punteros a null indican cola vacía
    }

    // Verifica si la cola está vacía
    public boolean estaVacia() {
        return frente == null;  // Si frente es null, no hay elementos
    }

    // Método para agregar un elemento al final de la cola (operación ENQUEUE)
    public void encolar(int dato) {
        Nodo nuevo = new Nodo(dato);  // Crea un nuevo nodo con el dato
        
        if (estaVacia()) {
            // Caso especial: cola vacía, nuevo nodo es frente y fin
            frente = fin = nuevo;
        } else {
            // Caso normal: enlaza el nuevo nodo al final
            fin.siguiente = nuevo;  // El actual fin apunta al nuevo nodo
            fin = nuevo;           // El nuevo nodo ahora es el fin
        }
    }

    // Método para eliminar y obtener el elemento del frente (operación DEQUEUE)
    public int desencolar() {
        if (estaVacia()) throw new IllegalStateException("La cola está vacía");
        
        int valor = frente.dato;    // Guarda el valor del frente
        frente = frente.siguiente;  // Mueve el frente al siguiente nodo
        
        // Si después de mover, frente es null, la cola quedó vacía
        if (frente == null) fin = null;
        
        return valor;  // Retorna el valor guardado
    }

    // Método para obtener una representación visual de la cola
    public String mostrar() {
        StringBuilder sb = new StringBuilder();  // Para construcción eficiente del string
        Nodo actual = frente;  // Puntero auxiliar para recorrer la cola
        int contador = 0;      // Contador para formato
        
        while (actual != null) {
            sb.append(actual.dato).append(" ➡️ ");  // Agrega el dato y flecha
            
            contador++;
            // Cada 10 elementos agrega un salto de línea para mejor visualización
            if (contador % 10 == 0) {
                sb.append("\n");
            }
            
            actual = actual.siguiente;  // Avanza al siguiente nodo
        }
        
        sb.append("null");  // Indica el final de la cola
        return sb.toString();
    }

    // Vacía completamente la cola
    public void vaciar() {
        frente = null;  // Al perder la referencia, el GC recolecta los nodos
        fin = null;
    }
}