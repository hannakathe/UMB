import grafos.*;

public class Main {
    public static void main(String[] args) {
        int numNodos = 10;
        Grafo grafo = new Grafo(numNodos);

        // Nombres nodos (ejemplo Bogotá)
        String[] nombres = {
            "Manuela Beltrán", "Unal", "Javeriana", "Andes", "Distrital",
            "San Ignacio", "Politecnico", "Crepes&Waffles", "Parque93", "Alcaldía"
        };

        for (int i = 0; i < numNodos; i++) {
            grafo.setNodo(i, new Nodo(nombres[i]));
        }

        // Agregar aristas (30 aristas con distancias aproximadas)
        grafo.agregarArista(0, 1, 5.2);
        grafo.agregarArista(0, 2, 6.3);
        grafo.agregarArista(1, 2, 3.4);
        grafo.agregarArista(1, 3, 7.1);
        grafo.agregarArista(2, 3, 2.5);
        grafo.agregarArista(3, 4, 8.3);
        grafo.agregarArista(4, 5, 3.9);
        grafo.agregarArista(5, 6, 4.7);
        grafo.agregarArista(6, 7, 6.0);
        grafo.agregarArista(7, 8, 1.2);
        grafo.agregarArista(8, 9, 2.8);
        grafo.agregarArista(0, 9, 9.0);
        grafo.agregarArista(2, 7, 5.4);
        grafo.agregarArista(1, 8, 6.1);
        grafo.agregarArista(3, 9, 7.5);
        grafo.agregarArista(4, 7, 4.9);
        grafo.agregarArista(5, 8, 3.1);
        grafo.agregarArista(6, 9, 7.3);
        grafo.agregarArista(0, 5, 5.8);
        grafo.agregarArista(1, 6, 6.2);
        grafo.agregarArista(2, 8, 4.3);
        grafo.agregarArista(3, 7, 5.7);
        grafo.agregarArista(4, 9, 6.8);
        grafo.agregarArista(0, 3, 4.6);
        grafo.agregarArista(1, 4, 7.4);
        grafo.agregarArista(2, 5, 3.8);
        grafo.agregarArista(3, 6, 5.1);
        grafo.agregarArista(4, 8, 6.0);
        grafo.agregarArista(5, 9, 4.2);
        grafo.agregarArista(6, 0, 6.5);

        // Mostrar matriz de adyacencia
        System.out.println("Matriz de adyacencia:");
        grafo.mostrarMatriz();

        // Dijkstra desde Manuela Beltrán (nodo 0)
        double[] distanciasDijkstra = Dijkstra.dijkstra(grafo, 0);
        System.out.println("\nDistancias mínimas desde Manuela Beltrán:");
        for (int i = 0; i < distanciasDijkstra.length; i++) {
            System.out.printf("%s -> %.2f\n", nombres[i], distanciasDijkstra[i]);
        }

        // Floyd-Warshall
        double[][] distFloyd = FloydWarshall.floydWarshall(grafo);
        System.out.println("\nMatriz de distancias mínimas (Floyd-Warshall):");
        for (int i = 0; i < numNodos; i++) {
            for (int j = 0; j < numNodos; j++) {
                if (distFloyd[i][j] == Double.POSITIVE_INFINITY)
                    System.out.print("∞ ");
                else
                    System.out.printf("%.2f ", distFloyd[i][j]);
            }
            System.out.println();
        }

        // Árbol de expansión mínima
        double costoPrim = Prim.prim(grafo);
        System.out.printf("\nCosto total del MST (Prim): %.2f\n", costoPrim);

        Kruskal kruskal = new Kruskal(numNodos);
        double costoKruskal = kruskal.kruskalMST(grafo);
        System.out.printf("Costo total del MST (Kruskal): %.2f\n", costoKruskal);
        GrafoVisual.mostrar(grafo);

    }
}