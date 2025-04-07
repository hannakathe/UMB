// Define el paquete al que pertenece la clase
package model;

// Importa la clase ArrayList para manejar listas dinámicas
import java.util.ArrayList;

// Declaración de la clase pública Estudiante
public class Estudiante {
    // Atributos privados de la clase (encapsulamiento)
    private String codigo;           // Código identificador del estudiante
    private String nombres;          // Nombres del estudiante
    private String apellidos;        // Apellidos del estudiante
    private String direccion;        // Dirección de residencia
    private String telefono;         // Número de teléfono
    private String carrera;          // Carrera que estudia
    private ArrayList<Double> notas; // Lista de notas del estudiante (como Double)

    // Constructor de la clase (inicializa todos los atributos)
    public Estudiante(String codigo, String nombres, String apellidos, 
                     String direccion, String telefono, String carrera, 
                     ArrayList<Double> notas) {
        this.codigo = codigo;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.carrera = carrera;
        this.notas = notas;  // Asigna la lista completa de notas
    }

    // --- MÉTODOS GETTERS (para acceder a los atributos) ---
    
    public String getCodigo() {
        return codigo;  // Devuelve el código del estudiante
    }

    public String getNombres() {
        return nombres;  // Devuelve los nombres
    }

    public String getApellidos() {
        return apellidos;  // Devuelve los apellidos
    }

    public String getDireccion() {
        return direccion;  // Devuelve la dirección
    }

    public String getTelefono() {
        return telefono;  // Devuelve el teléfono
    }

    public String getCarrera() {
        return carrera;  // Devuelve la carrera
    }

    // Devuelve la lista completa de notas (referencia al ArrayList)
    public ArrayList<Double> getNotas() {
        return notas;
    }

    // --- MÉTODOS SETTERS (para modificar atributos) ---
    // (Nota: No hay setter para código, ya que parece ser un identificador inmutable)

    public void setNombres(String nombres) {
        this.nombres = nombres;  // Actualiza los nombres
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;  // Actualiza los apellidos
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;  // Actualiza la carrera
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;  // Actualiza la dirección
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;  // Actualiza el teléfono
    }

    // Reemplaza toda la lista de notas por una nueva
    public void setNotas(ArrayList<Double> notas) {
        this.notas = notas;
    }

    // Sobrescribe el método toString() para representación textual del objeto
    @Override
    public String toString() {
        // Ejemplo: "Juan Pérez - Ingeniería - Notas: [4.5, 3.8, 4.0]"
        return nombres + " " + apellidos + " - " + carrera + " - Notas: " + notas;
    }
}