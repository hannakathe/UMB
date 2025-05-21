import java.util.*;

public class Grafo {
    List<Nodo> nodos;
    List<Arista> aristas;

    public Grafo() {
        nodos = new ArrayList<>();
        aristas = new ArrayList<>();
    }

    public void agregarNodo(Nodo nodo) {
        nodos.add(nodo);
    }

    public void agregarArista(Nodo origen, Nodo destino, double peso) {
        aristas.add(new Arista(origen, destino, peso));
    }

    class agregarNodo {

        public agregarNodo() {
        }
    }
}
