// EstacionClimatica.java
package service;

import model.Temperatura;
import java.util.ArrayList;
import java.util.List;

public class EstacionClimatica {
    // Lista para almacenar objetos de tipo Temperatura
    private List<Temperatura> temperaturas = new ArrayList<>();

    // Método para agregar una nueva temperatura a la lista
    public void agregarTemperatura(String dia, double maxima, double minima) {
        temperaturas.add(new Temperatura(dia, maxima, minima));
    }

    // Método para filtrar temperaturas dentro de un rango dado
    public List<Temperatura> filtrarPorRango(double min, double max) {
        List<Temperatura> resultado = new ArrayList<>();
        for (Temperatura t : temperaturas) {
            if (t.getMinima() >= min && t.getMaxima() <= max) { 
                resultado.add(t);
            }
        }
        return resultado;
    }

    // Método para filtrar temperaturas con mínima por debajo de cero
    public List<Temperatura> filtrarPorMinimaBajoCero() {
        List<Temperatura> resultado = new ArrayList<>();
        for (Temperatura t : temperaturas) {
            if (t.getMinima() < 0) {
                resultado.add(t);
            }
        }
        return resultado;
    }

    // Método para calcular la media de las temperaturas por día
    public double calcularMediaPorDia(String dia) {
        double suma = 0;
        int contador = 0;
        for (Temperatura t : temperaturas) {
            if (t.getDia().equalsIgnoreCase(dia)) {
                suma += (t.getMaxima() + t.getMinima()) / 2;
                contador++;
            }
        }
        return (contador == 0) ? 0 : suma / contador;
    }

    // Método para obtener todas las temperaturas almacenadas
    public List<Temperatura> obtenerTodasLasTemperaturas() {
        return new ArrayList<>(temperaturas);
    }
}
