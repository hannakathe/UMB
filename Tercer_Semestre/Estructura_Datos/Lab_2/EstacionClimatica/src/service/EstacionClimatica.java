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
    //for (TipoElemento variable : colección) {Código a ejecutar para cada elemento}

    public List<Temperatura> filtrarPorRango(double min, double max) {
        List<Temperatura> resultado = new ArrayList<>(); // Retorno: El método devolverá una lista de objetos de tipo Temperatura (List<Temperatura>).
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
            if (t.getDia().equalsIgnoreCase(dia)) { //no importa si el dia esta en minus o mayus
                suma += (t.getMaxima() + t.getMinima()) / 2;
                contador++;
            }
        }
        return (contador == 0) ? 0 : suma / contador;//si la condicion (contador == 0) es verdadera retorna 0, si es falsa retorna suma / contador
    }

    // Método para obtener todas las temperaturas almacenadas
    public List<Temperatura> obtenerTodasLasTemperaturas() {
        return new ArrayList<>(temperaturas);
    }
}
