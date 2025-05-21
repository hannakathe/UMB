import java.awt.*;
import javax.swing.*;

public class PanelGrafo extends JPanel {
    Grafo grafo;

    public PanelGrafo(Grafo grafo) {
        this.grafo = grafo;
        setPreferredSize(new Dimension(1000, 700)); // Más espacio visual
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Suavizado de líneas y texto (antialiasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2));

        // Dibujar aristas
        g2.setColor(Color.LIGHT_GRAY);
        for (Arista arista : grafo.aristas) {
            int x1 = arista.origen.x;
            int y1 = arista.origen.y;
            int x2 = arista.destino.x;
            int y2 = arista.destino.y;

            g2.drawLine(x1, y1, x2, y2);

            // Etiqueta del peso
            String pesoStr = String.format("%.1f km", arista.peso);
            int labelX = (x1 + x2) / 2 + 5;
            int labelY = (y1 + y2) / 2 - 5;
            g2.setColor(Color.BLACK);
            g2.drawString(pesoStr, labelX, labelY);
            g2.setColor(Color.LIGHT_GRAY);
        }

        // Dibujar nodos
        for (Nodo nodo : grafo.nodos) {
            int x = nodo.x;
            int y = nodo.y;
            Color color = obtenerColorPorTipo(nodo.tipo);
            g2.setColor(color);
            g2.fillOval(x - 6, y - 6, 12, 12); // Nodo

            // Texto separado para evitar amontonamiento
            g2.setColor(Color.BLACK);
            g2.drawString(nodo.nombre, x + 14, y - 4);
        }
    }

    private Color obtenerColorPorTipo(String tipo) {
        switch (tipo) {
            case "Universidad":
                return new Color(0, 102, 204); // azul oscuro
            case "Restaurante":
                return new Color(255, 153, 51); // naranja
            case "Supermercado":
                return new Color(102, 204, 0); // verde
            case "Tienda":
                return new Color(204, 0, 204); // púrpura
            case "Alcaldia":
                return new Color(128, 128, 128); // gris
            case "TransMilenio":
                return new Color(200, 0, 0); // rojo oscuro
            default:
                return Color.GRAY;
        }
    }
}
