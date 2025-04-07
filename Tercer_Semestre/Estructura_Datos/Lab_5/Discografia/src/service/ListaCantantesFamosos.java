// Define el paquete al que pertenece la clase
package service;

// Importa la clase CantanteFamoso del paquete model
import model.CantanteFamoso;
// Importa clases de utilidad de Java (incluye List, ArrayList, etc.)
import java.util.*;

// Declaración de la clase pública ListaCantantesFamosos
public class ListaCantantesFamosos {
    // Constante pública que representa una biblioteca de cantantes (actualmente inicializada como null)
    public static final List<CantanteFamoso> BIBLIOTECA = null;
    
    // Lista privada que almacenará los cantantes (implementación con ArrayList)
    private ArrayList<CantanteFamoso> listaCantantes = new ArrayList<>();

    // Método para agregar un cantante a la lista
    public void agregarCantante(CantanteFamoso cantante) {
        listaCantantes.add(cantante);  // Añade el objeto cantante al ArrayList
    }

    // Método para eliminar un cantante por su nombre (no sensible a mayúsculas/minúsculas)
    public void eliminarCantante(String nombre) {
        // removeIf elimina todos los elementos que cumplan la condición
        listaCantantes.removeIf(c -> c.getNombre().equalsIgnoreCase(nombre));
    }

    // Método para modificar un cantante existente
    public boolean modificarCantante(String nombre, CantanteFamoso nuevo) {
        // Recorre la lista buscando por nombre
        for (int i = 0; i < listaCantantes.size(); i++) {
            if (listaCantantes.get(i).getNombre().equalsIgnoreCase(nombre)) {
                // Si encuentra el cantante, lo reemplaza con el nuevo objeto
                listaCantantes.set(i, nuevo);
                return true;  // Retorna true indicando éxito
            }
        }
        return false;  // Retorna false si no encontró el cantante
    }

    // Método que devuelve la lista completa de cantantes
    public List<CantanteFamoso> obtenerLista() {
        return listaCantantes;  // Retorna la referencia a la lista
    }

    // Método que devuelve una lista ordenada por ventas (de mayor a menor)
    public List<CantanteFamoso> obtenerOrdenadosPorVentas() {
        // Crea una copia de la lista original para no modificarla
        List<CantanteFamoso> copia = new ArrayList<>(listaCantantes);
        // Ordena la copia usando una expresión lambda como comparador
        copia.sort((a, b) -> Integer.compare(b.getTotalDeVentas(), a.getTotalDeVentas()));
        return copia;  // Retorna la lista ordenada
    }
}
