import javax.swing.*;
import java.awt.*;
import java.util.*;

public class GrafoVisual extends JPanel {
    private final Grafo grafo;
    private final Map<Integer, Point> posiciones;

    public GrafoVisual(Grafo grafo) {
        this.grafo = grafo;
        posiciones = new HashMap<>();
        asignarPosiciones();
    }

    private void asignarPosiciones() {
        posiciones.put(0, new Point(50, 100));
        posiciones.put(1, new Point(150, 50));
        posiciones.put(2, new Point(150, 150));
        posiciones.put(3, new Point(250, 30));
        posiciones.put(4, new Point(250, 170));
        posiciones.put(5, new Point(350, 100));
        posiciones.put(6, new Point(450, 60));
        posiciones.put(7, new Point(450, 160));
        posiciones.put(8, new Point(550, 100));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.BLACK);

        // Dibujar aristas
        for (int i = 0; i < grafo.vertices; i++) {
            Point p1 = posiciones.get(i);
            for (Grafo.Arista arista : grafo.listaAdyacencia[i]) {
                Point p2 = posiciones.get(arista.destino);
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g2.drawString(String.valueOf(arista.peso), midX, midY);
            }
        }

        // Dibujar nodos
        for (Map.Entry<Integer, Point> entry : posiciones.entrySet()) {
            int nodo = entry.getKey();
            Point p = entry.getValue();
            g2.setColor(Color.CYAN);
            g2.fillOval(p.x - 15, p.y - 15, 30, 30);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - 15, p.y - 15, 30, 30);
            g2.drawString(String.valueOf(nodo + 1), p.x - 5, p.y + 5);
        }
    }

    public static void mostrar(Grafo grafo) {
        JFrame frame = new JFrame("Visualización del Grafo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GrafoVisual(grafo));
        frame.setSize(700, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}