// Temperatura.java
package model;

public class Temperatura {
    private String dia; // Nombre del día
    private double maxima; // Temperatura máxima
    private double minima; // Temperatura mínima

    // Constructor para inicializar los atributos de la clase
    public Temperatura(String dia, double maxima, double minima) {
        this.dia = dia;
        this.maxima = maxima;
        this.minima = minima;
    }

    // Método para obtener el nombre del día
    public String getDia() {
        return dia;
    }

    // Método para obtener la temperatura máxima
    public double getMaxima() {
        return maxima;
    }

    // Método para obtener la temperatura mínima
    public double getMinima() {
        return minima;
    }
}
