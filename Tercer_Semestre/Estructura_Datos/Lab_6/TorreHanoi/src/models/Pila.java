// Declaración del paquete al que pertenece la clase
package models;

// Importación de la clase LinkedList de Java para usarla como estructura de datos subyacente
import java.util.LinkedList;

// Definición de una clase genérica llamada 'Pila' (Stack en inglés), donde '<T>' indica que es genérica y puede trabajar con cualquier tipo de dato
public class Pila<T> {
    // Atributo privado que almacenará los elementos de la pila usando una LinkedList
    private LinkedList<T> elementos;

    // Constructor de la clase Pila, inicializa la LinkedList vacía
    public Pila() {
        elementos = new LinkedList<>();
    }

    // Método para agregar un elemento a la cima de la pila (operación 'push')
    public void push(T elemento) {
        elementos.addFirst(elemento); // 'addFirst' añade el elemento al inicio de la LinkedList (LIFO)
    }

    // Método para eliminar y retornar el elemento en la cima de la pila (operación 'pop')
    // Retorna 'null' si la pila está vacía para evitar una excepción
    public T pop() {
        return elementos.isEmpty() ? null : elementos.removeFirst(); // 'removeFirst' elimina y retorna el primer elemento
    }

    // Método para observar el elemento en la cima de la pila sin eliminarlo (operación 'peek')
    // Retorna 'null' si la pila está vacía
    public T peek() {
        return elementos.isEmpty() ? null : elementos.getFirst(); // 'getFirst' retorna el primer elemento sin eliminarlo
    }

    // Método que verifica si la pila está vacía
    public boolean isEmpty() {
        return elementos.isEmpty(); // Retorna 'true' si no hay elementos
    }

    // Método que retorna el número de elementos en la pila
    public int size() {
        return elementos.size(); // Retorna el tamaño de la LinkedList
    }

    // Método getter para obtener la LinkedList subyacente (útil si se necesita acceso directo a la estructura)
    public LinkedList<T> getElementos() {
        return elementos;
    }
}