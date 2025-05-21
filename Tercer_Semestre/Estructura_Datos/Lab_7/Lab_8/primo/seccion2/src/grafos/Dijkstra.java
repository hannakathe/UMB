package grafos;

import java.util.Arrays;

public class Dijkstra {
    public static double[] dijkstra(Grafo grafo, int origen) {
        int n = grafo.getNumNodos();
        double[] dist = new double[n];
        boolean[] visitado = new boolean[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[origen] = 0;

        for (int i = 0; i < n - 1; i++) {
            int u = -1;
            double minDist = Double.POSITIVE_INFINITY;
            for (int j = 0; j < n; j++) {
                if (!visitado[j] && dist[j] < minDist) {
                    minDist = dist[j];
                    u = j;
                }
            }

            visitado[u] = true;

            for (int v = 0; v < n; v++) {
                double peso = grafo.getMatrizAdyacencia()[u][v];
                if (!visitado[v] && peso != Double.POSITIVE_INFINITY && dist[u] + peso < dist[v]) {
                    dist[v] = dist[u] + peso;
                }
            }
        }

        return dist;
    }
}