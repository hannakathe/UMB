
package Sencillos.ConPOO;
class Estudiante {
    private String nombre;
    private String apellido;
    private int documento;
    private int semestre;
    private String carrera;

    public int getDocumento() {
        return documento;
    }

    public Estudiante(String nombre, String apellido, int documento, int semestre, String carrera) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.semestre = semestre;
        this.carrera = carrera;
    }

    

    public String toString() {
        return "Nombre: " + nombre + "\n" +
               "Apellido: " + apellido + "\n" +
               "Documento: " + documento + "\n" +
               "Semestre: " + semestre + "\n" +
               "Carrera: " + carrera + "\n";
    }
}