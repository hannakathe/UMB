import javax.swing.*;

public class VisualizadorGrafo {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // UNIVERSIDADES
        Nodo unal = new Nodo("Universidad Nacional", 150, 100, "Universidad");
        Nodo andes = new Nodo("Universidad de los Andes", 350, 100, "Universidad");
        Nodo javeriana = new Nodo("Pontificia U. Javeriana", 550, 150, "Universidad");
        Nodo umb = new Nodo("Universidad Manuela Beltrán", 500, 300, "Universidad");
        Nodo externado = new Nodo("U. Externado de Colombia", 200, 300, "Universidad");

        // LUGARES CERCA DE UNIVERSIDADES
        // UNAL
        Nodo oliveto = new Nodo("Oliveto Pizza", 120, 60, "Restaurante");
        Nodo estacionUnal = new Nodo("TM Univ. Nacional", 100, 130, "TransMilenio");
        Nodo alcaldiaTeusa = new Nodo("Alcaldía Teusaquillo", 180, 60, "Alcaldia");

        // ANDES
        Nodo monaMamona = new Nodo("Mona Mamona", 370, 60, "Restaurante");
        Nodo estacionAguas = new Nodo("TM Las Aguas", 330, 80, "TransMilenio");
        Nodo alcaldiaCandelaria = new Nodo("Alcaldía Candelaria", 310, 60, "Alcaldia");

        // JAVERIANA
        Nodo alitas = new Nodo("Alitas Javeriana", 580, 120, "Restaurante");
        Nodo estacionJaveriana = new Nodo("TM Javeriana", 600, 170, "TransMilenio");
        Nodo alcaldiaChapinero1 = new Nodo("Alcaldía Chapinero", 580, 90, "Alcaldia");

        // UMB
        Nodo xocolat = new Nodo("Xocolat & More", 530, 320, "Restaurante");
        Nodo estacionLaSalle = new Nodo("TM La Salle", 510, 270, "TransMilenio");
        Nodo alcaldiaChapinero2 = new Nodo("Alcaldía Chapinero", 470, 280, "Alcaldia");

        // EXTERNADO
        Nodo selina = new Nodo("Selina Candelaria", 180, 330, "Restaurante");
        Nodo estacionMuseo = new Nodo("TM Museo del Oro", 170, 270, "TransMilenio");

        // AGREGAR TODOS LOS NODOS
        Nodo[] nodos = {unal, andes, javeriana, umb, externado,
            oliveto, estacionUnal, alcaldiaTeusa,
            monaMamona, estacionAguas, alcaldiaCandelaria,
            alitas, estacionJaveriana, alcaldiaChapinero1,
            xocolat, estacionLaSalle, alcaldiaChapinero2,
            selina, estacionMuseo};

        for (Nodo n : nodos) grafo.agregarNodo(n);

        // CONEXIONES ENTRE UNIVERSIDADES
        grafo.agregarArista(unal, andes, 3.0);
        grafo.agregarArista(andes, javeriana, 1.1);
        grafo.agregarArista(javeriana, umb, 3.2);
        grafo.agregarArista(umb, externado, 4.6);
        grafo.agregarArista(unal, javeriana, 4.0);
        grafo.agregarArista(andes, umb, 5.5);

        // CONEXIONES LOCALES
        grafo.agregarArista(unal, oliveto, 0.5);
        grafo.agregarArista(unal, estacionUnal, 0.3);
        grafo.agregarArista(unal, alcaldiaTeusa, 0.7);

        grafo.agregarArista(andes, monaMamona, 0.4);
        grafo.agregarArista(andes, estacionAguas, 0.5);
        grafo.agregarArista(andes, alcaldiaCandelaria, 0.6);

        grafo.agregarArista(javeriana, alitas, 0.3);
        grafo.agregarArista(javeriana, estacionJaveriana, 0.4);
        grafo.agregarArista(javeriana, alcaldiaChapinero1, 0.6);

        grafo.agregarArista(umb, xocolat, 0.4);
        grafo.agregarArista(umb, estacionLaSalle, 0.6);
        grafo.agregarArista(umb, alcaldiaChapinero2, 0.7);

        grafo.agregarArista(externado, selina, 0.3);
        grafo.agregarArista(externado, estacionMuseo, 0.5);

        // VISUALIZACIÓN
        JFrame ventana = new JFrame("Grafo de Universidades y lugares en Bogotá D.C.");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(new PanelGrafo(grafo));
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
    }
}