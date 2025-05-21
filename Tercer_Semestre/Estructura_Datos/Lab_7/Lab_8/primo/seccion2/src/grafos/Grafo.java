package grafos;

import java.util.Arrays;

public class Grafo {
    private int numNodos;
    private Nodo[] nodos;
    private double[][] matrizAdyacencia;

    public Grafo(int numNodos) {
        this.numNodos = numNodos;
        nodos = new Nodo[numNodos];
        matrizAdyacencia = new double[numNodos][numNodos];

        for (int i = 0; i < numNodos; i++) {
            Arrays.fill(matrizAdyacencia[i], Double.POSITIVE_INFINITY);
            matrizAdyacencia[i][i] = 0;
        }
    }

    public void setNodo(int indice, Nodo nodo) {
        if (indice >= 0 && indice < numNodos) {
            nodos[indice] = nodo;
        }
    }

    public void agregarArista(int origen, int destino, double peso) {
        matrizAdyacencia[origen][destino] = peso;
        matrizAdyacencia[destino][origen] = peso;
    }

    public void mostrarMatriz() {
        System.out.print("          ");
        for (Nodo nodo : nodos) {
            System.out.printf("%15s", nodo.getNombre());
        }
        System.out.println();
        for (int i = 0; i < numNodos; i++) {
            System.out.printf("%10s", nodos[i].getNombre());
            for (int j = 0; j < numNodos; j++) {
                if (matrizAdyacencia[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.printf("%15s", "INF");
                } else {
                    System.out.printf("%15.2f", matrizAdyacencia[i][j]);
                }
            }
            System.out.println();
        }
    }

    public double[][] getMatrizAdyacencia() {
        return matrizAdyacencia;
    }

    public Nodo[] getNodos() {
        return nodos;
    }

    public int getNumNodos() {
        return numNodos;
    }
}