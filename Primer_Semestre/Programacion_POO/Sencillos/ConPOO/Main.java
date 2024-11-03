
package Sencillos.ConPOO;

import static Sencillos.ConPOO.PersonaConPOO.consultar;
import static Sencillos.ConPOO.PersonaConPOO.inscribir;
import static Sencillos.ConPOO.PersonaConPOO.listar;
import java.util.Scanner;


public class Main {
     public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\nMenú:");
            System.out.println("1. Inscribir estudiante");
            System.out.println("2. Consultar estudiante por documento");
            System.out.println("3. Listar todos los estudiantes");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer de entrada

            switch (opcion) {
                case 1:
                    System.out.println("\nInscripción de Estudiante:");
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String apellido = scanner.nextLine();
                    System.out.print("Documento: ");
                    int documento = scanner.nextInt();
                    System.out.print("Semestre: ");
                    int semestre = scanner.nextInt();
                    scanner.nextLine(); // Limpiar buffer de entrada
                    System.out.print("Carrera: ");
                    String carrera = scanner.nextLine();
                    inscribir(nombre, apellido, documento, semestre, carrera);
                    break;
                case 2:
                    System.out.println("\nConsulta de Estudiante por Documento:");
                    System.out.print("Ingrese el documento a consultar: ");
                    int docConsulta = scanner.nextInt();
                    consultar(docConsulta);
                    break;
                case 3:
                    listar();
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione una opción del menú.");
            }
        } while (opcion != 4);

        scanner.close();
    }
    
}
