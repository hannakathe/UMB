package ui;

import java.util.Scanner;

import services.TreeService;

public class UI {
    private final TreeService service;

    public UI() {
        this.service = new TreeService();
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n--- MENÚ ---");
                System.out.println("1. Crear árbol");
                System.out.println("2. Recorrer árbol");
                System.out.println("3. Mostrar grado y altura");
                System.out.println("4. Buscar nivel de nodo");
                System.out.println("5. Búsqueda en amplitud (BFS)");
                System.out.println("6. Salir");
                System.out.print("Seleccione una opción: ");
                int option = scanner.nextInt();

                switch (option) {
                    case 1 -> service.createTree();
                    case 2 -> service.traverse();
                    case 3 -> service.displayCalculations();
                    case 4 -> service.searchNode();
                    case 5 -> service.bfs();
                    case 6 -> {
                        System.out.println("¡Hasta luego!");
                        return;
                    }
                    default -> System.out.println("Opción inválida.");
                }
            }
        }
    }
}
