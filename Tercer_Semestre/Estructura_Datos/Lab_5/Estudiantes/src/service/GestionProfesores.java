// Paquete donde se ubica la clase
package service;

// Importación de clases necesarias
import model.Profesor;  // Clase modelo Profesor
import java.util.HashMap;  // Para usar la estructura HashMap

// Clase que gestiona los profesores usando un HashMap
public class GestionProfesores {
    // HashMap que almacena profesores (clave: código, valor: objeto Profesor)
    private HashMap<String, Profesor> profesores = new HashMap<>();

    // Método para agregar un nuevo profesor
    public void insertar(String codigo, Profesor profesor) {
        profesores.put(codigo, profesor);  // Añade al HashMap
    }

    // Busca y devuelve un profesor por su código
    public Profesor consultar(String codigo) {
        return profesores.get(codigo);  // Retorna null si no existe
    }

    // Actualiza los datos de un profesor existente
    public void modificar(String codigo, Profesor profesor) {
        profesores.put(codigo, profesor);  // Sobrescribe el profesor
    }

    // Elimina un profesor (método duplicado, igual que eliminar())
    public void borrar(String codigo) {
        profesores.remove(codigo);  // Quita del HashMap
    }

    // Devuelve todos los profesores registrados
    public HashMap<String, Profesor> obtenerTodos() {
        return profesores;  // Retorna el HashMap completo
    }

    // Elimina un profesor (método duplicado, igual que borrar())
    public void eliminar(String codigo) {
        profesores.remove(codigo);  // Elimina por código
    }
}