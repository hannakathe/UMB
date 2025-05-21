package grafos;

import java.awt.*;
import javax.swing.*;

public class GrafoVisual extends JPanel {
    private Grafo grafo;
    private int width = 800, height = 600;
    private Point[] posiciones;

    public GrafoVisual(Grafo grafo) {
        this.grafo = grafo;
        this.posiciones = new Point[grafo.getNumNodos()];
        generarPosicionesCirculares();
        JFrame frame = new JFrame("Visualización del Grafo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.add(this);
        frame.setVisible(true);
    }

    private void generarPosicionesCirculares() {
        int radio = 200;
        int centroX = width / 2;
        int centroY = height / 2;
        int n = grafo.getNumNodos();
        for (int i = 0; i < n; i++) {
            double angulo = 2 * Math.PI * i / n;
            int x = (int) (centroX + radio * Math.cos(angulo));
            int y = (int) (centroY + radio * Math.sin(angulo));
            posiciones[i] = new Point(x, y);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g.setFont(new Font("Arial", Font.BOLD, 12));

        Nodo[] nodos = grafo.getNodos();
        double[][] matriz = grafo.getMatrizAdyacencia();

        for (int i = 0; i < grafo.getNumNodos(); i++) {
            for (int j = i + 1; j < grafo.getNumNodos(); j++) {
                if (matriz[i][j] != Double.POSITIVE_INFINITY) {
                    Point p1 = posiciones[i];
                    Point p2 = posiciones[j];
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                    // Etiqueta con el peso
                    int midX = (p1.x + p2.x) / 2;
                    int midY = (p1.y + p2.y) / 2;
                    g.setColor(Color.RED);
                    g.drawString(String.format("%.1f", matriz[i][j]), midX, midY);
                    g.setColor(Color.BLACK);
                }
            }
        }

        // Dibujar nodos
        for (int i = 0; i < grafo.getNumNodos(); i++) {
            Point p = posiciones[i];
            g.setColor(Color.BLUE);
            g.fillOval(p.x - 15, p.y - 15, 30, 30);
            g.setColor(Color.black);
            g.drawString(nodos[i].getNombre(), p.x - 25, p.y - 20);
        }
    }

    public static void mostrar(Grafo grafo) {
        new GrafoVisual(grafo);
    }
}