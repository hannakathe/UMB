package grafos;

import java.util.*;

class Arista implements Comparable<Arista> {
    int origen, destino;
    double peso;

    public Arista(int origen, int destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
    }

    public int compareTo(Arista otra) {
        return Double.compare(this.peso, otra.peso);
    }
}

public class Kruskal {
    int[] padre;

    public Kruskal(int n) {
        padre = new int[n];
        for (int i = 0; i < n; i++) {
            padre[i] = i;
        }
    }

    int find(int i) {
        if (padre[i] != i)
            padre[i] = find(padre[i]);
        return padre[i];
    }

    void union(int x, int y) {
        int xset = find(x);
        int yset = find(y);
        padre[xset] = yset;
    }

    public double kruskalMST(Grafo grafo) {
        int n = grafo.getNumNodos();
        PriorityQueue<Arista> pq = new PriorityQueue<>();
        double[][] matriz = grafo.getMatrizAdyacencia();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matriz[i][j] != Double.POSITIVE_INFINITY) {
                    pq.add(new Arista(i, j, matriz[i][j]));
                }
            }
        }

        double resultado = 0;
        while (!pq.isEmpty()) {
            Arista arista = pq.poll();
            int x = find(arista.origen);
            int y = find(arista.destino);
            if (x != y) {
                resultado += arista.peso;
                union(x, y);
            }
        }
        return resultado;
    }
}