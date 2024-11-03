
package Sencillos;
import java.util.ArrayList;

public class Person {
    private ArrayList<String> personas;

    // Constructor
    public Person() {
        this.personas = new ArrayList<String>();
    }

    // Método para agregar una persona al registro
    public void agregarPersona(String nombre) {
        personas.add(nombre);
        System.out.println("Persona agregada: " + nombre);
    }

    // Método para consultar si una persona está en el registro
    public boolean consultarPersona(String nombre) {
        if (personas.contains(nombre)) {
            System.out.println("La persona " + nombre + " está registrada.");
            return true;
        } else {
            System.out.println("La persona " + nombre + " no está registrada.");
            return false;
        }
    }

    // Método para listar todas las personas registradas
    public void listarPersonas() {
        System.out.println("Personas registradas:");
        for (String persona : personas) {
            System.out.println(persona);
        }
    }

    public static void main(String[] args) {
        Person registro = new Person();

        // Agregar personas
        registro.agregarPersona("Juan");
        registro.agregarPersona("María");
        registro.agregarPersona("Pedro");

        // Consultar si una persona está en el registro
        registro.consultarPersona("María");
        registro.consultarPersona("Carlos");

        // Listar todas las personas registradas
        registro.listarPersonas();
    }
}
