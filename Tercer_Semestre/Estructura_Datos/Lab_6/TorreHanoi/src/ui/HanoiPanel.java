package ui;

import models.Disk;
import models.Pila;
import javax.swing.*;
import java.awt.*;
import java.util.List;

// Panel personalizado para visualizar las Torres de Hanoi
public class HanoiPanel extends JPanel {
    // Torres del juego (arreglo de pilas de discos)
    private Pila<Disk>[] torres;
    // Número total de discos en el juego
    private int numDiscos;

    // Constructor que recibe las torres y el número de discos
    public HanoiPanel(Pila<Disk>[] torres, int numDiscos) {
        this.torres = torres;
        this.numDiscos = numDiscos;
        setPreferredSize(new Dimension(600, 300)); // Tamaño preferido del panel
        setBackground(Color.WHITE); // Fondo blanco
    }

    // Método para actualizar la visualización con nuevas torres
    public void actualizar(Pila<Disk>[] torres) {
        this.torres = torres;
        repaint(); // Vuelve a pintar el panel
    }

    // Método que dibuja el componente
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // Convertimos a Graphics2D para más funcionalidad

        // Configuración para suavizado de bordes (antialiasing)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dimensiones del panel
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Cálculos para el dibujo
        int torreAncho = panelWidth / 3; // Ancho asignado a cada torre
        int torreCentroX; // Centro horizontal de cada torre
        int baseY = panelHeight - 20; // Posición Y de la base
        int discoAltura = 20; // Altura de cada disco

        // Dibujamos las 3 torres
        for (int i = 0; i < 3; i++) {
            torreCentroX = torreAncho * i + torreAncho / 2; // Calcula el centro de la torre actual

            // Dibuja el poste de la torre (rectángulo vertical)
            g2.setColor(Color.GRAY);
            g2.fillRect(torreCentroX - 5, panelHeight / 6, 10, baseY - panelHeight / 6);

            // Obtenemos los discos de esta torre
            List<Disk> discos = torres[i].getElementos();
            int nivel = 0; // Contador de niveles (discos apilados)

            // Dibujamos los discos de abajo hacia arriba
            for (int j = discos.size() - 1; j >= 0; j--) {
                Disk d = discos.get(j);
                // El ancho del disco depende de su tamaño
                int discoAncho = 20 + d.getSize() * 20;
                // Posición X centrada respecto al poste
                int discoX = torreCentroX - discoAncho / 2;
                // Posición Y según el nivel
                int discoY = baseY - nivel * discoAltura;

                // Color basado en el tamaño del disco (HSB para variación de color)
                float hue = (float) d.getSize() / numDiscos;
                Color colorInicio = Color.getHSBColor(hue, 0.6f, 1.0f);
                Color colorFin = colorInicio.darker(); // Color más oscuro para el degradado

                // Creamos un degradado horizontal para el disco
                GradientPaint gp = new GradientPaint(
                        discoX, discoY - discoAltura, // Punto inicial
                        colorInicio,
                        discoX + discoAncho, discoY, // Punto final
                        colorFin
                );

                // Dibujamos el disco con bordes redondeados
                g2.setPaint(gp);
                g2.fillRoundRect(discoX, discoY - discoAltura, discoAncho, discoAltura, 10, 10);

                nivel++; // Pasamos al siguiente nivel
            }
        }
    }
}