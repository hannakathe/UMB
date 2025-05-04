package services;

import java.util.Scanner;
import models.BinaryTree;

public class TreeService {
    private final BinaryTree tree;

    public TreeService() {
        this.tree = new BinaryTree();
    }

    public void createTree() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese '1' para árbol de números o '2' para letras:");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consumir salto de línea

        if (choice == 1) {
            createNumberTree(scanner);
        } else if (choice == 2) {
            createLetterTree(scanner);
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private void createNumberTree(Scanner scanner) {
        System.out.println("Ingrese números (escriba 'done' para terminar):");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) break;
            try {
                tree.insert(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println("Valor no válido.");
            }
        }
    }

    private void createLetterTree(Scanner scanner) {
        System.out.println("Ingrese letras (escriba 'done' para terminar):");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("done")) break;
            if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                tree.insert(input.charAt(0));
            } else {
                System.out.println("Ingrese solo una letra.");
            }
        }
    }

    public void traverse() {
        System.out.println("In-orden:");
        tree.inOrder();
        System.out.println("\nPre-orden:");
        tree.preOrder();
        System.out.println("\nPost-orden:");
        tree.postOrder();
        System.out.println();
    }

    public void displayCalculations() {
        System.out.println("Grado del árbol: " + tree.calculateDegree());
        System.out.println("Altura del árbol: " + tree.calculateHeight());
    }

    public void searchNode() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Ingrese el dato a buscar:");
            String input = scanner.nextLine();
            try {
                int number = Integer.parseInt(input);
                System.out.println("Nivel del nodo: " + tree.calculateLevel(number));
            } catch (NumberFormatException e) {
                if (input.length() == 1) {
                    System.out.println("Nivel del nodo: " + tree.calculateLevel(input.charAt(0)));
                } else {
                    System.out.println("Dato inválido.");
                }
            }
        }
    }

    public void bfs() {
        System.out.println("Recorrido BFS:");
        tree.breadthFirstSearch();
        System.out.println();
    }
}
