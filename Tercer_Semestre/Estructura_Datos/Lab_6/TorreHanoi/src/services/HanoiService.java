// Declaración del paquete al que pertenece la clase
package services;

// Importaciones de clases necesarias
import models.Disk;
import models.Pila;
import java.util.function.BiConsumer;

// Clase que implementa la lógica del juego Torres de Hanoi
public class HanoiService {
    // Arreglo de pilas que representan las tres torres del juego
    private Pila<Disk>[] torres;

    // Constructor que inicializa el juego con un número específico de discos
    @SuppressWarnings("unchecked") // Suprime la advertencia sobre el casteo genérico
    public HanoiService(int numDiscos) {
        // Inicializa el arreglo de torres (3 pilas)
        torres = new Pila[3];
        
        // Crea una nueva pila para cada torre
        for (int i = 0; i < 3; i++) {
            torres[i] = new Pila<>();
        }

        // Apila los discos en la primera torre (torre 0) en orden descendente
        for (int i = numDiscos; i >= 1; i--) {
            torres[0].push(new Disk(i)); // Disco más grande abajo, más pequeño arriba
        }
    }

    // Método para mover un disco de una torre a otra
    public boolean moverDisco(int from, int to) {
        // Verifica si la torre de origen está vacía
        if (torres[from].isEmpty()) return false;

        // Obtiene el disco en la cima de la torre de origen (sin removerlo)
        Disk disco = torres[from].peek();
        
        // Verifica las reglas del juego:
        // 1. La torre destino no debe estar vacía
        // 2. El disco a mover debe ser más pequeño que el disco en la cima de la torre destino
        if (!torres[to].isEmpty() && torres[to].peek().getSize() < disco.getSize()) {
            return false; // Movimiento inválido
        }

        // Mueve el disco de la torre 'from' a la torre 'to'
        torres[to].push(torres[from].pop());
        return true; // Movimiento exitoso
    }

    // Método getter para obtener las torres (útil para visualización)
    public Pila<Disk>[] getTorres() {
        return torres;
    }

    // Verifica si el juego ha terminado (todos los discos en la última torre)
    public boolean juegoTerminado(int numDiscos) {
        return torres[2].size() == numDiscos;
    }

    // Método recursivo que resuelve automáticamente el puzzle
    public void resolverHanoi(int n, int origen, int destino, int auxiliar, BiConsumer<Integer, Integer> callback) {
        // Caso base: Si solo queda un disco, muévelo directamente al destino
        if (n == 1) {
            moverDisco(origen, destino);
            callback.accept(origen, destino); // Notifica el movimiento
            return;
        }

        // Paso 1: Mover n-1 discos de origen a auxiliar (usando destino como temporal)
        resolverHanoi(n - 1, origen, auxiliar, destino, callback);
        
        // Paso 2: Mover el disco más grande al destino
        moverDisco(origen, destino);
        callback.accept(origen, destino); // Notifica el movimiento
        
        // Paso 3: Mover los n-1 discos de auxiliar a destino (usando origen como temporal)
        resolverHanoi(n - 1, auxiliar, destino, origen, callback);
    }
}