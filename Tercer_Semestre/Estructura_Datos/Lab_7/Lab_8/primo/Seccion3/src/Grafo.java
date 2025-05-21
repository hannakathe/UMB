import java.util.*;

public class Grafo {
    public int vertices;
    public int[][] matrizAdyacencia;
    public LinkedList<Arista>[] listaAdyacencia;

    public static class Arista {
        public int destino, peso;
        public Arista(int destino, int peso) {
            this.destino = destino;
            this.peso = peso;
        }
    }

    public Grafo(int v) {
        this.vertices = v;
        matrizAdyacencia = new int[v][v];
        listaAdyacencia = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            listaAdyacencia[i] = new LinkedList<>();
        }
    }

    public void agregarArista(int origen, int destino, int peso) {
        matrizAdyacencia[origen][destino] = peso;
        listaAdyacencia[origen].add(new Arista(destino, peso));
    }

    public void mostrarMatrizAdyacencia() {
        System.out.println("Matriz de Adyacencia:");
        for (int[] fila : matrizAdyacencia) {
            for (int val : fila) {
                System.out.print(val + "\t");
            }
            System.out.println();
        }
    }

    public void mostrarListaAdyacencia() {
        System.out.println("Lista de Adyacencia:");
        for (int i = 0; i < vertices; i++) {
            System.out.print((i + 1) + " -> ");
            for (Arista arista : listaAdyacencia[i]) {
                System.out.print((arista.destino + 1) + "(peso:" + arista.peso + ") ");
            }
            System.out.println();
        }
    }
}