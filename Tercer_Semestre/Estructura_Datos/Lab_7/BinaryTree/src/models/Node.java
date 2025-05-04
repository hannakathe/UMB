package models;

/**
 * Clase que representa un nodo en un árbol binario.
 * Cada nodo contiene un dato (String) y referencias a sus nodos hijos izquierdo y derecho.
 */
public class Node {
    // Atributos públicos (podrían ser privados con getters/setters si se desea mayor encapsulamiento)
    public String data;   // El dato almacenado en el nodo (puede ser numérico o texto)
    public Node left;     // Referencia al nodo hijo izquierdo
    public Node right;    // Referencia al nodo hijo derecho

    /**
     * Constructor del nodo.
     * @param data El valor que contendrá el nodo (cadena de texto)
     */
    public Node(String data) {
        this.data = data;  // Asigna el dato recibido
        this.left = null;   // Inicializa sin hijo izquierdo
        this.right = null;  // Inicializa sin hijo derecho
    }

    // ========== MÉTODOS ACCESORES (GETTERS/SETTERS) ==========

    /**
     * Obtiene el nodo hijo izquierdo.
     * @return Referencia al nodo izquierdo (puede ser null)
     */
    public Node getLeft() {
        return left;
    }

    /**
     * Establece el nodo hijo izquierdo.
     * @param left Nuevo nodo hijo izquierdo
     */
    public void setLeft(Node left) {
        this.left = left;
    }

    /**
     * Obtiene el nodo hijo derecho.
     * @return Referencia al nodo derecho (puede ser null)
     */
    public Node getRight() {
        return right;
    }

    /**
     * Establece el nodo hijo derecho.
     * @param right Nuevo nodo hijo derecho
     */
    public void setRight(Node right) {
        this.right = right;
    }

    /**
     * Obtiene el valor almacenado en el nodo.
     * @return El dato del nodo (como Object para mayor flexibilidad)
     */
    public Object getValue() {
        return data;
    }

    // Nota: Se podrían agregar estos métodos si se necesita mayor control:
    /*
    public String getData() {
        return data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    */
}