
package Sencillos.ConPOO;

import java.util.ArrayList;
import java.util.Scanner;



public class PersonaConPOO {
    private static ArrayList<Estudiante> estudiantes = new ArrayList<>();

    public static void inscribir(String nombre, String apellido, int documento, int semestre, String carrera) {
        Estudiante estudiante = new Estudiante(nombre, apellido, documento, semestre, carrera);
        estudiantes.add(estudiante);
        System.out.println("Estudiante inscrito correctamente.");
    }

    public static void consultar(int documento) {
        boolean encontrado = false;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getDocumento() == documento) {
                System.out.println("Estudiante encontrado:\n" + estudiante);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("No se encontró ningún estudiante con ese documento.");
        }
    }

    public static void listar() {
        System.out.println("Lista de Estudiantes:");
        for (Estudiante estudiante : estudiantes) {
            System.out.println(estudiante);
        }
    }

   
}


