
package Sencillos;

import java.util.Scanner;



public class Persona1 { 
    private static String[] nombres = new String[100];
    private static String[] apellidos = new String[100];
    private static int[] documentos = new int[100];
    private static int[] semestres = new int[100];
    private static String[] carreras = new String[100];
    private static int contadorEstudiantes = 0;

    public static void inscribir(String nombre, String apellido, int documento, int semestre, String carrera) {
        nombres[contadorEstudiantes] = nombre;
        apellidos[contadorEstudiantes] = apellido;
        documentos[contadorEstudiantes] = documento;
        semestres[contadorEstudiantes] = semestre;
        carreras[contadorEstudiantes] = carrera;
        contadorEstudiantes++;
        System.out.println("Estudiante inscrito correctamente.");
    }

    public static void consultar(int documento) {
        boolean encontrado = false;
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (documentos[i] == documento) {
                System.out.println("Nombre: " + nombres[i]);
                System.out.println("Apellido: " + apellidos[i]);
                System.out.println("Documento: " + documentos[i]);
                System.out.println("Semestre: " + semestres[i]);
                System.out.println("Carrera: " + carreras[i]);
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
        for (int i = 0; i < contadorEstudiantes; i++) {
            System.out.println("Nombre: " + nombres[i]);
            System.out.println("Apellido: " + apellidos[i]);
            System.out.println("Documento: " + documentos[i]);
            System.out.println("Semestre: " + semestres[i]);
            System.out.println("Carrera: " + carreras[i]);
            System.out.println("--------------------------");
        }
    }

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



