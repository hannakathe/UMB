package grafos;

import java.util.Arrays;

public class Prim {
    public static double prim(Grafo grafo) {
        int n = grafo.getNumNodos();
        boolean[] visitado = new boolean[n];
        double[] clave = new double[n];
        Arrays.fill(clave, Double.POSITIVE_INFINITY);
        clave[0] = 0;
        double total = 0;

        for (int count = 0; count < n; count++) {
            double min = Double.POSITIVE_INFINITY;
            int u = -1;

            for (int v = 0; v < n; v++) {
                if (!visitado[v] && clave[v] < min) {
                    min = clave[v];
                    u = v;
                }
            }

            visitado[u] = true;
            total += clave[u];

            for (int v = 0; v < n; v++) {
                double peso = grafo.getMatrizAdyacencia()[u][v];
                if (!visitado[v] && peso < clave[v]) {
                    clave[v] = peso;
                }
            }
        }

        return total;
    }
}