// Define el paquete al que pertenece la clase
package service;

// Importa la clase Estudiante del paquete model
import model.Estudiante;
// Importa clases de utilidad de Java
import java.util.ArrayList;
import java.util.HashMap;

// Declaración de la clase pública GestionEstudiantes
public class GestionEstudiantes {
    // Mapa (diccionario) que almacena estudiantes usando su código como clave
    private HashMap<String, Estudiante> estudiantes = new HashMap<>();

    // Método para insertar un nuevo estudiante
    public void insertar(String codigo, Estudiante estudiante) {
        // Agrega el estudiante al HashMap usando su código como clave
        estudiantes.put(estudiante.getCodigo(), estudiante);
    }

    // Método que devuelve todos los estudiantes como un HashMap
    public HashMap<String, Estudiante> obtenerTodos() {
        return estudiantes;  // Retorna la referencia al HashMap completo
    }

    // Método para borrar un estudiante por su código (duplicado, ver eliminar())
    public void borrar(String codigo) {
        estudiantes.remove(codigo);  // Elimina el estudiante con el código especificado
    }

    // Método para consultar un estudiante por su código
    public Estudiante consultar(String codigo) {
        return estudiantes.get(codigo);  // Retorna el estudiante o null si no existe
    }

    // Método que devuelve todos los estudiantes como una ArrayList
    public ArrayList<Estudiante> listarEstudiantes() {
        // Crea una nueva ArrayList con los valores del HashMap (los estudiantes)
        return new ArrayList<>(estudiantes.values());
    }

    // Método para eliminar un estudiante por su código (igual que borrar())
    public void eliminar(String codigo) {
        estudiantes.remove(codigo);  // Elimina el estudiante con el código especificado
    }
}