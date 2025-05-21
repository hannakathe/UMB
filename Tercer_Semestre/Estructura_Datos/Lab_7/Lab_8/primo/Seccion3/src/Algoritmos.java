import java.util.*;

public class Algoritmos {

    public static void bfs(Grafo grafo, int inicio) {
        boolean[] visitado = new boolean[grafo.vertices];
        Queue<Integer> cola = new LinkedList<>();
        visitado[inicio] = true;
        cola.add(inicio);
        System.out.print("Recorrido BFS: ");
        while (!cola.isEmpty()) {
            int nodo = cola.poll();
            System.out.print((nodo + 1) + " ");
            for (Grafo.Arista arista : grafo.listaAdyacencia[nodo]) {
                if (!visitado[arista.destino]) {
                    visitado[arista.destino] = true;
                    cola.add(arista.destino);
                }
            }
        }
        System.out.println();
    }

    public static void dfs(Grafo grafo, int inicio) {
        boolean[] visitado = new boolean[grafo.vertices];
        System.out.print("Recorrido DFS: ");
        dfsUtil(grafo, inicio, visitado);
        System.out.println();
    }

    private static void dfsUtil(Grafo grafo, int nodo, boolean[] visitado) {
        visitado[nodo] = true;
        System.out.print((nodo + 1) + " ");
        for (Grafo.Arista arista : grafo.listaAdyacencia[nodo]) {
            if (!visitado[arista.destino]) {
                dfsUtil(grafo, arista.destino, visitado);
            }
        }
    }

    public static int dijkstra(Grafo grafo, int inicio, int destino) {
        int[] dist = new int[grafo.vertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[inicio] = 0;
        PriorityQueue<int[]> cola = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        cola.add(new int[]{inicio, 0});

        while (!cola.isEmpty()) {
            int[] actual = cola.poll();
            int nodo = actual[0], costo = actual[1];

            for (Grafo.Arista arista : grafo.listaAdyacencia[nodo]) {
                if (dist[arista.destino] > costo + arista.peso) {
                    dist[arista.destino] = costo + arista.peso;
                    cola.add(new int[]{arista.destino, dist[arista.destino]});
                }
            }
        }
        return dist[destino];
    }

    public static int dfsMaxCost(Grafo grafo, int inicio, int destino) {
        boolean[] visitado = new boolean[grafo.vertices];
        return dfsMaxUtil(grafo, inicio, destino, visitado);
    }

    private static int dfsMaxUtil(Grafo grafo, int actual, int destino, boolean[] visitado) {
        if (actual == destino) return 0;
        visitado[actual] = true;
        int max = Integer.MIN_VALUE;
        for (Grafo.Arista arista : grafo.listaAdyacencia[actual]) {
            if (!visitado[arista.destino]) {
                int costo = dfsMaxUtil(grafo, arista.destino, destino, visitado);
                if (costo != Integer.MIN_VALUE)
                    max = Math.max(max, arista.peso + costo);
            }
        }
        visitado[actual] = false;
        return max;
    }

    public static void floydWarshall(Grafo grafo) {
        int[][] dist = new int[grafo.vertices][grafo.vertices];
        final int INF = 9999;

        for (int i = 0; i < grafo.vertices; i++) {
            for (int j = 0; j < grafo.vertices; j++) {
                if (i == j) dist[i][j] = 0;
                else if (grafo.matrizAdyacencia[i][j] != 0)
                    dist[i][j] = grafo.matrizAdyacencia[i][j];
                else
                    dist[i][j] = INF;
            }
        }

        for (int k = 0; k < grafo.vertices; k++) {
            for (int i = 0; i < grafo.vertices; i++) {
                for (int j = 0; j < grafo.vertices; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j])
                        dist[i][j] = dist[i][k] + dist[k][j];
                }
            }
        }

        System.out.println("Matriz de Floyd-Warshall:");
        for (int i = 0; i < grafo.vertices; i++) {
            for (int j = 0; j < grafo.vertices; j++) {
                if (dist[i][j] == INF)
                    System.out.print("INF\t");
                else
                    System.out.print(dist[i][j] + "\t");
            }
            System.out.println();
        }
    }
}