// Define el paquete al que pertenece la clase
package model;

// Importa la clase ArrayList para manejar listas dinámicas
import java.util.ArrayList;

// Declaración de la clase pública Profesor
public class Profesor {
    // Atributos privados de la clase (encapsulamiento)
    private String nombres;          // Nombres del profesor
    private String apellidos;        // Apellidos del profesor
    private String direccion;        // Dirección de residencia
    private String telefono;         // Número de teléfono
    private String carrera;          // Carrera asociada
    private ArrayList<String> cursos; // Lista de cursos que imparte (como Strings)

    // Constructor de la clase (inicializa todos los atributos)
    public Profesor(String nombres, String apellidos, 
                   String direccion, String telefono, 
                   String carrera, ArrayList<String> cursos) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.carrera = carrera;
        this.cursos = cursos;  // Asigna la lista completa de cursos
    }

    // --- MÉTODOS GETTER Y SETTER (forma compacta) ---

    // Para nombres
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    // Para apellidos
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    // Para dirección
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    // Para teléfono
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    // Para carrera
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    // Para cursos (devuelve/establece la lista completa)
    public ArrayList<String> getCursos() { return cursos; }
    public void setCursos(ArrayList<String> cursos) { this.cursos = cursos; }

    // Sobrescribe el método toString() para representación textual del objeto
    @Override
    public String toString() {
        // Ejemplo: "María García - Ingeniería - Cursos: [Matemáticas, Física]"
        return nombres + " " + apellidos + " - " + carrera + " - Cursos: " + cursos;
    }
}