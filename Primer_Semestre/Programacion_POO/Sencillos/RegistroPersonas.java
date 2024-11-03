
package Sencillos;

import java.util.Scanner;

public class RegistroPersonas {
    private static String persona1 = "";
    private static String persona2 = "";
    private static String persona3 = "";

    public static void agregarPersona(String nombre) {
        if (persona1.isEmpty()) {
            persona1 = nombre;
        } else if (persona2.isEmpty()) {
            persona2 = nombre;
        } else if (persona3.isEmpty()) {
            persona3 = nombre;
        } else {
            System.out.println("No se pueden agregar más personas, el registro está lleno.");
        }
        System.out.println("Persona agregada: " + nombre);
    }

    public static boolean consultarPersona(String nombre) {
        if (persona1.equals(nombre) || persona2.equals(nombre) || persona3.equals(nombre)) {
            System.out.println("La persona " + nombre + " está registrada.");
            return true;
        } else {
            System.out.println("La persona " + nombre + " no está registrada.");
            return false;
        }
    }

    public static void listarPersonas() {
        System.out.println("Personas registradas:");
        if (!persona1.isEmpty()) {
            System.out.println(persona1);
        }
        if (!persona2.isEmpty()) {
            System.out.println(persona2);
        }
        if (!persona3.isEmpty()) {
            System.out.println(persona3);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Agregar personas
        agregarPersona("Juan");
        agregarPersona("María");
        agregarPersona("Pedro");

        // Consultar si una persona está en el registro
        System.out.print("Ingrese el nombre de la persona a consultar: ");
        String nombreConsulta = scanner.nextLine();
        consultarPersona(nombreConsulta);

        // Listar todas las personas registradas
        listarPersonas();

        scanner.close();
    }
}
