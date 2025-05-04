package services;

import models.BinaryTree;
import models.Node;
import java.util.Random;

/**
 * Servicio para operaciones con árboles binarios.
 * Proporciona métodos para creación, inserción y consulta de árboles.
 */
public class BinaryTreeService {
    private final BinaryTree tree;  // Árbol binario asociado
    private final boolean isNumeric;  // Indica si el árbol es numérico

    /**
     * Constructor del servicio.
     * @param isNumeric true para árbol numérico, false para alfanumérico
     */
    public BinaryTreeService(boolean isNumeric) {
        this.tree = new BinaryTree(isNumeric);  // Crea un nuevo árbol
        this.isNumeric = isNumeric;  // Establece el tipo de datos
    }

    /**
     * Obtiene el árbol binario gestionado por este servicio.
     */
    public BinaryTree getTree() {
        return tree;
    }

    /**
     * Inserta un dato en el árbol.
     * @param data Valor a insertar (como String)
     */
    public void insert(String data) {
        tree.insert(data);  // Delegación al método del árbol
    }

    /**
     * Crea un árbol automático con datos aleatorios.
     * @param n Cantidad de nodos a generar
     */
    public void createAutomaticTree(int n) {
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            if (isNumeric) {
                // Genera números aleatorios 0-99 para árboles numéricos
                insert(String.valueOf(rand.nextInt(100)));
            } else {
                // Genera letras mayúsculas aleatorias para árboles alfanuméricos
                char letter = (char) (rand.nextInt(26) + 'A');
                insert(String.valueOf(letter));
            }
        }
    }

    // ========== MÉTODOS DE CONSULTA ==========

    /**
     * Obtiene el recorrido inorden del árbol.
     */
    public String getInOrder() {
        return tree.inOrderTraversal();
    }

    /**
     * Obtiene el recorrido preorden del árbol.
     */
    public String getPreOrder() {
        return tree.preOrderTraversal();
    }

    /**
     * Obtiene el recorrido postorden del árbol.
     */
    public String getPostOrder() {
        return tree.postOrderTraversal();
    }

    /**
     * Obtiene el recorrido por niveles (BFS) del árbol.
     */
    public String getBreadthFirstSearch() {
        return tree.breadthFirstSearch();
    }

    /**
     * Calcula la altura del árbol.
     */
    public int getTreeHeight() {
        return tree.height();
    }

    /**
     * Obtiene el grado (número de hijos) de la raíz.
     */
    public int getDegreeOfRoot() {
        return tree.degreeOfRoot();
    }

    /**
     * Obtiene el nivel de un nodo específico.
     * @param data Valor del nodo a buscar
     */
    public int getLevelOfNode(String data) {
        return tree.levelOfNode(data);
    }

    /**
     * Busca un nodo por su valor.
     * @param data Valor a buscar
     * @return Nodo encontrado o null
     */
    public Node findNode(String data) {
        return tree.findNode(data);
    }

    // ========== MÉTODOS DE INSERCIÓN ESPECIAL ==========

    /**
     * Inserta un nodo manualmente después de otro nodo específico.
     * @param previousNode Valor del nodo de referencia
     * @param newNode Valor del nuevo nodo a insertar
     */
    public void insertManual(String previousNode, String newNode) {
        if (this.tree.root == null) {
            // Árbol vacío: el nuevo nodo se convierte en raíz
            this.tree.root = new Node(newNode);
        } else {
            // Busca el nodo de referencia e inserta el nuevo
            insertAfter(previousNode, newNode, this.tree.root);
        }
    }

    /**
     * Método auxiliar recursivo para inserción manual.
     */
    private void insertAfter(String previousNode, String newNode, Node currentNode) {
        if (currentNode == null) return;  // Caso base

        if (currentNode.getValue().equals(previousNode)) {
            // Encontramos el nodo de referencia
            if (currentNode.getLeft() == null) {
                // Prioriza inserción en hijo izquierdo
                currentNode.setLeft(new Node(newNode));
            } else {
                // Usa hijo derecho si izquierdo está ocupado
                currentNode.setRight(new Node(newNode));
            }
        } else {
            // Búsqueda recursiva en ambos subárboles
            insertAfter(previousNode, newNode, currentNode.getLeft());
            insertAfter(previousNode, newNode, currentNode.getRight());
        }
    }

    /**
     * Crea un árbol manual a partir de un arreglo de valores.
     * @param nodesData Valores de los nodos en orden de inserción
     */
    public void createManualTree(String[] nodesData) {
        if (nodesData.length == 0) return;
        
        // El primer elemento es la raíz
        tree.insert(nodesData[0]);
        
        // Inserta el resto de nodos
        for (int i = 1; i < nodesData.length; i++) {
            insert(nodesData[i]);
        }
    }
}