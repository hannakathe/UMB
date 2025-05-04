package models;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase que representa un árbol binario con soporte para datos numéricos y alfanuméricos.
 * Puede funcionar como árbol binario de búsqueda cuando se usa con datos ordenables.
 */
public class BinaryTree {
    public Node root;          // Nodo raíz del árbol
    public boolean isNumeric;  // Indica si el árbol contiene datos numéricos

    /**
     * Constructor que especifica si el árbol contendrá datos numéricos.
     * @param isNumeric true para árbol numérico, false para alfanumérico
     */
    public BinaryTree(boolean isNumeric) {
        this.isNumeric = isNumeric;
        this.root = null;  // Árbol inicialmente vacío
    }

    /**
     * Constructor por defecto (asume datos alfanuméricos).
     */
    public BinaryTree() {
        this(false);  // Por defecto no es numérico
    }

    /**
     * Inserta un nuevo dato en el árbol.
     * @param data Dato a insertar (numérico o alfanumérico según configuración)
     */
    public void insert(String data) {
        if (root == null) {
            root = new Node(data);  // Si el árbol está vacío, crea la raíz
        } else {
            insertRecursively(root, data);  // Inserta recursivamente
        }
    }

    /**
     * Método auxiliar para inserción recursiva.
     * Maneja inserción para datos numéricos y alfanuméricos.
     */
    private void insertRecursively(Node node, String data) {
        if (isNumeric) {
            // Lógica para datos numéricos (BST numérico)
            int newValue = Integer.parseInt(data);
            int currentValue = Integer.parseInt(node.data);

            if (newValue < currentValue) {
                if (node.left == null) {
                    node.left = new Node(data);  // Insertar a la izquierda
                } else {
                    insertRecursively(node.left, data);  // Recursión izquierda
                }
            } else {
                if (node.right == null) {
                    node.right = new Node(data);  // Insertar a la derecha
                } else {
                    insertRecursively(node.right, data);  // Recursión derecha
                }
            }
        } else {
            // Lógica para datos alfanuméricos (comparación lexicográfica)
            if (data.compareToIgnoreCase(node.data) < 0) {
                if (node.left == null) {
                    node.left = new Node(data);
                } else {
                    insertRecursively(node.left, data);
                }
            } else {
                if (node.right == null) {
                    node.right = new Node(data);
                } else {
                    insertRecursively(node.right, data);
                }
            }
        }
    }

    // ========== MÉTODOS DE RECORRIDO ==========

    /**
     * Recorrido inorden (izquierda-raíz-derecha).
     * @return String con los datos ordenados
     */
    public String inOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        inOrderHelper(root, sb);
        return sb.toString().trim();  // Elimina espacio final
    }

    private void inOrderHelper(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderHelper(node.left, sb);
            sb.append(node.data).append(" ");
            inOrderHelper(node.right, sb);
        }
    }

    /**
     * Recorrido preorden (raíz-izquierda-derecha).
     */
    public String preOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        preOrderHelper(root, sb);
        return sb.toString().trim();
    }

    private void preOrderHelper(Node node, StringBuilder sb) {
        if (node != null) {
            sb.append(node.data).append(" ");
            preOrderHelper(node.left, sb);
            preOrderHelper(node.right, sb);
        }
    }

    /**
     * Recorrido postorden (izquierda-derecha-raíz).
     */
    public String postOrderTraversal() {
        StringBuilder sb = new StringBuilder();
        postOrderHelper(root, sb);
        return sb.toString().trim();
    }

    private void postOrderHelper(Node node, StringBuilder sb) {
        if (node != null) {
            postOrderHelper(node.left, sb);
            postOrderHelper(node.right, sb);
            sb.append(node.data).append(" ");
        }
    }

    /**
     * Recorrido por niveles (BFS).
     */
    public String breadthFirstSearch() {
        StringBuilder sb = new StringBuilder();
        if (root == null) return "";

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            sb.append(node.data).append(" ");

            // Añade hijos a la cola
            if (node.left != null) queue.add(node.left);
            if (node.right != null) queue.add(node.right);
        }

        return sb.toString().trim();
    }

    // ========== MÉTODOS DE ANÁLISIS ==========

    /**
     * Calcula la altura del árbol.
     */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }

    /**
     * Obtiene el grado (número de hijos) de la raíz.
     */
    public int degreeOfRoot() {
        if (root == null) return 0;
        int degree = 0;
        if (root.left != null) degree++;
        if (root.right != null) degree++;
        return degree;
    }

    /**
     * Encuentra el nivel de un nodo específico.
     */
    public int levelOfNode(String data) {
        return findLevel(root, data, 1);  // Empieza en nivel 1 (raíz)
    }

    private int findLevel(Node node, String data, int level) {
        if (node == null) return 0;
        if (node.data.equalsIgnoreCase(data)) return level;

        // Busca primero en el subárbol izquierdo
        int downLevel = findLevel(node.left, data, level + 1);
        if (downLevel != 0) return downLevel;

        // Si no se encontró, busca en el derecho
        return findLevel(node.right, data, level + 1);
    }

    /**
     * Busca un nodo por su dato.
     */
    public Node findNode(String data) {
        return findNodeRecursively(root, data);
    }

    private Node findNodeRecursively(Node node, String data) {
        if (node == null) return null;
        if (node.data.equalsIgnoreCase(data)) return node;

        // Búsqueda en profundidad primero
        Node foundNode = findNodeRecursively(node.left, data);
        if (foundNode != null) return foundNode;

        return findNodeRecursively(node.right, data);
    }
}