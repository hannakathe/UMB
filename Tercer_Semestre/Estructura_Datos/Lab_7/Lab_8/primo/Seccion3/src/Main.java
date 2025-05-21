public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo(9);

        grafo.agregarArista(0, 1, 1);
        grafo.agregarArista(0, 2, 1);
        grafo.agregarArista(1, 3, 3);
        grafo.agregarArista(1, 4, 2);
        grafo.agregarArista(2, 1, 2);
        grafo.agregarArista(2, 4, 3);
        grafo.agregarArista(2, 5, 2);
        grafo.agregarArista(3, 5, 1);
        grafo.agregarArista(4, 3, 3);
        grafo.agregarArista(4, 6, 1);
        grafo.agregarArista(5, 6, 2);
        grafo.agregarArista(6, 6, 2);
        grafo.agregarArista(6, 7, 3);
        grafo.agregarArista(6, 8, 1);
        grafo.agregarArista(7, 6, 2);
        grafo.agregarArista(7, 8, 5);

        grafo.mostrarMatrizAdyacencia();
        grafo.mostrarListaAdyacencia();

        Algoritmos.bfs(grafo, 0);
        Algoritmos.dfs(grafo, 0);

        int costoMin = Algoritmos.dijkstra(grafo, 0, 8);
        System.out.println("Costo mínimo de 1 a 9: " + costoMin);

        int costoMax = Algoritmos.dfsMaxCost(grafo, 0, 8);
        System.out.println("Costo máximo de 1 a 9: " + costoMax);

        Algoritmos.floydWarshall(grafo);
        GrafoVisual.mostrar(grafo);
    }
}