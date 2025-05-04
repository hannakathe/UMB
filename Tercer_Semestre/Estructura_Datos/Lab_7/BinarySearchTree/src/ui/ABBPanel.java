// Package declaration for the UI components
package ui;

// Import corrected - using models.Nodo (assuming your Nodo class is in models package)
import models.Nodo;

// Swing and AWT imports for GUI components
import javax.swing.JPanel;
import java.awt.*;

// Custom JPanel for visualizing a binary search tree
public class ABBPanel extends JPanel {
    // Reference to the root node of the tree
    private Nodo raiz;

    // Sets the root node and triggers a repaint
    public void setRaiz(Nodo raiz) {
        this.raiz = raiz;
        repaint();  // Requests the panel to redraw itself
    }

    // Overridden paintComponent method for custom drawing
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // Clear the panel with background color
        
        // Only draw if tree exists
        if (raiz != null) {
            // Start drawing from root:
            // - Center horizontally (getWidth()/2)
            // - Start 40 pixels from top
            // - Initial horizontal offset = 1/4 of panel width
            dibujar(g, raiz, getWidth() / 2, 40, getWidth() / 4);
        }
    }

    // Recursive method to draw the tree
    private void dibujar(Graphics g, Nodo nodo, int x, int y, int xOffset) {
        // Set node color (could use nodo.getColor() if you want colored nodes)
        g.setColor(Color.BLACK);
        
        // Draw node circle (15px radius centered at x,y)
        g.fillOval(x - 15, y - 15, 30, 30);
        
        // Draw node value (centered text)
        g.setColor(Color.WHITE);
        g.drawString(Integer.toString(nodo.getValor()), x - 7, y + 5);

        // Reset color for lines
        g.setColor(Color.BLACK);
        
        // Draw left subtree if exists
        if (nodo.getIzquierda() != null) {
            // Draw connecting line to left child
            g.drawLine(x, y, x - xOffset, y + 50);
            // Recursively draw left child with:
            // - New x position: current x - offset
            // - New y position: 50px below current
            // - New offset: half of current offset
            dibujar(g, nodo.getIzquierda(), x - xOffset, y + 50, xOffset / 2);
        }
        
        // Draw right subtree if exists
        if (nodo.getDerecha() != null) {
            // Draw connecting line to right child
            g.drawLine(x, y, x + xOffset, y + 50);
            // Recursively draw right child
            dibujar(g, nodo.getDerecha(), x + xOffset, y + 50, xOffset / 2);
        }
    }
}