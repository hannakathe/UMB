// Package declaration
package services;

// Import required classes
import models.ArbolBinarioBusqueda;
import models.Nodo;
import java.awt.Color;
import java.util.Random;

public class ABBService {
    // Instance of the binary search tree
    public ArbolBinarioBusqueda abb;

    // Constructor initializes an empty BST
    public ABBService() {
        abb = new ArbolBinarioBusqueda();
    }

    // Generates a random tree with 'cantidad' nodes (values 0-99)
    public void generarArbolAleatorio(int cantidad) {
        Random random = new Random();
        for (int i = 0; i < cantidad; i++) {
            int valorAleatorio = random.nextInt(100);  // Random value 0-99
            abb.insertar(valorAleatorio);  // Insert into BST
        }
    }

    // Inserts a new node after a specified existing node
    public void insertarDespuesDe(int valor, int nodoAnterior) {
        Nodo nodoReferencia = buscarNodo(abb.raiz, nodoAnterior);
        if (nodoReferencia != null) {
            Nodo nuevoNodo = new Nodo(valor);
            nodoReferencia.setDerecha(nuevoNodo);  // Always inserts as right child
        }
    }

    // Searches for a node and marks it red if found
    public String buscarYMarcar(int valor) {
        Nodo nodo = buscarNodo(abb.raiz, valor);
        if (nodo != null) {
            nodo.setColor(Color.RED);  // Visual marker
            return "Nodo encontrado";  // Found message
        }
        return "Nodo no encontrado";  // Not found message
    }

    // ========== CORE TREE OPERATIONS ==========

    // Recursive node search helper method
    private Nodo buscarNodo(Nodo nodo, int valor) {
        if (nodo == null)
            return null;
        if (nodo.getValor() == valor)
            return nodo;
        if (valor < nodo.getValor())
            return buscarNodo(nodo.getIzquierda(), valor);
        return buscarNodo(nodo.getDerecha(), valor);
    }

    // Public delete method returns success status
    public boolean eliminar(int valor) {
        Nodo nodo = buscarNodo(abb.raiz, valor);
        if (nodo != null) {
            abb.raiz = eliminarNodo(abb.raiz, valor);
            return true;  // Deletion successful
        }
        return false;  // Value not found
    }

    // Recursive node deletion helper
    private Nodo eliminarNodo(Nodo nodo, int valor) {
        if (nodo == null)
            return null;
        
        // Search for the node to delete
        if (valor < nodo.getValor()) {
            nodo.setIzquierda(eliminarNodo(nodo.getIzquierda(), valor));
        } else if (valor > nodo.getValor()) {
            nodo.setDerecha(eliminarNodo(nodo.getDerecha(), valor));
        } else {
            // Node found - handle 3 cases:
            
            // Case 1: Node with one child or no child
            if (nodo.getIzquierda() == null)
                return nodo.getDerecha();
            if (nodo.getDerecha() == null)
                return nodo.getIzquierda();
            
            // Case 2: Node with two children
            // Get inorder successor (smallest in right subtree)
            Nodo sucesor = obtenerMinimo(nodo.getDerecha());
            // Copy successor's value
            nodo.setValor(sucesor.getValor());
            // Delete the successor
            nodo.setDerecha(eliminarNodo(nodo.getDerecha(), sucesor.getValor()));
        }
        return nodo;
    }

    // Finds the minimum value node in a subtree
    private Nodo obtenerMinimo(Nodo nodo) {
        while (nodo.getIzquierda() != null) {
            nodo = nodo.getIzquierda();
        }
        return nodo;
    }

    // ========== TREE TRAVERSALS ==========

    // In-order traversal (returns sorted values)
    public String inorden() {
        return recorrerInorden(abb.raiz);
    }

    private String recorrerInorden(Nodo nodo) {
        if (nodo == null)
            return "";
        return recorrerInorden(nodo.getIzquierda()) + nodo.getValor() + " " + 
               recorrerInorden(nodo.getDerecha());
    }

    // Pre-order traversal (root -> left -> right)
    public String preorden() {
        return recorrerPreorden(abb.raiz);
    }

    private String recorrerPreorden(Nodo nodo) {
        if (nodo == null)
            return "";
        return nodo.getValor() + " " + 
               recorrerPreorden(nodo.getIzquierda()) + 
               recorrerPreorden(nodo.getDerecha());
    }

    // Post-order traversal (left -> right -> root)
    public String postorden() {
        return recorrerPostorden(abb.raiz);
    }

    private String recorrerPostorden(Nodo nodo) {
        if (nodo == null)
            return "";
        return recorrerPostorden(nodo.getIzquierda()) + 
               recorrerPostorden(nodo.getDerecha()) + 
               nodo.getValor() + " ";
    }

    // ========== TREE PROPERTIES ==========

    // Checks if tree is full (every node has 0 or 2 children)
    public boolean esLleno() {
        return esLlenoRecursivo(abb.raiz);
    }

    private boolean esLlenoRecursivo(Nodo nodo) {
        if (nodo == null)
            return true;
        // If a node has only one child, it's not full
        if ((nodo.getIzquierda() == null && nodo.getDerecha() != null) ||
            (nodo.getIzquierda() != null && nodo.getDerecha() == null)) {
            return false;
        }
        // Check both subtrees recursively
        return esLlenoRecursivo(nodo.getIzquierda()) && 
               esLlenoRecursivo(nodo.getDerecha());
    }
}