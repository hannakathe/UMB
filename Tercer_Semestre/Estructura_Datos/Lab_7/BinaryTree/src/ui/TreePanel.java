package ui;

import models.Node;
import javax.swing.*;
import java.awt.*;

/**
 * Panel personalizado para visualización gráfica de árboles binarios.
 * Dibuja nodos como círculos con sus valores y conexiones como líneas.
 */
public class TreePanel extends JPanel {
    private Node root;  // Referencia al nodo raíz del árbol a visualizar

    /**
     * Establece la raíz del árbol a visualizar y solicita repintado.
     * @param root Nodo raíz del árbol (puede ser null para árbol vacío)
     */
    public void setRoot(Node root) {
        this.root = root;
        repaint();  // Solicita repintado del componente
    }

    /**
     * Método override para pintar el componente.
     * @param g Objeto Graphics para dibujar
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Limpia el área de dibujo
        
        // Solo dibuja si hay un árbol
        if (root != null) {
            // Inicia el dibujo recursivo desde la raíz:
            // - Centrado horizontalmente (getWidth()/2)
            // - 30px desde el borde superior
            // - Espaciado horizontal inicial = 1/4 del ancho del panel
            drawTree(g, root, getWidth() / 2, 30, getWidth() / 4);
        }
    }

    /**
     * Método recursivo para dibujar el árbol.
     * @param g Objeto Graphics para dibujar
     * @param node Nodo actual a dibujar
     * @param x Posición horizontal del nodo
     * @param y Posición vertical del nodo
     * @param xOffset Espaciado horizontal para los nodos hijos
     */
    private void drawTree(Graphics g, Node node, int x, int y, int xOffset) {
        // Dibuja el círculo del nodo (15px de radio)
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        
        // Dibuja el texto del nodo (centrado)
        g.setColor(Color.WHITE);
        g.drawString(node.data, x - 5, y + 5);

        // Dibuja subárbol izquierdo si existe
        if (node.left != null) {
            g.setColor(Color.BLACK);
            // Línea conectando al hijo izquierdo
            g.drawLine(x, y, x - xOffset, y + 50);
            // Dibujo recursivo del hijo izquierdo:
            // - Nueva posición x: actual - offset
            // - Nueva posición y: 50px más abajo
            // - Nuevo offset: la mitad del actual
            drawTree(g, node.left, x - xOffset, y + 50, xOffset / 2);
        }

        // Dibuja subárbol derecho si existe
        if (node.right != null) {
            g.setColor(Color.BLACK);
            // Línea conectando al hijo derecho
            g.drawLine(x, y, x + xOffset, y + 50);
            // Dibujo recursivo del hijo derecho:
            // - Nueva posición x: actual + offset
            // - Nueva posición y: 50px más abajo
            // - Nuevo offset: la mitad del actual
            drawTree(g, node.right, x + xOffset, y + 50, xOffset / 2);
        }
    }
}