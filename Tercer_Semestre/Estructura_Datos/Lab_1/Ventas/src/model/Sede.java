// Declaración del paquete 'model' donde se encuentra la clase Sede.
package model;  

// Importación de las clases necesarias.
import java.util.ArrayList;  // Se utiliza para crear listas dinámicas de ventas.
import java.util.List;  // Se utiliza para la interfaz List que define las listas de ventas.

public class Sede {
    // Atributos de la clase 'Sede'.
    private String nombre;  // Nombre de la sede.
    private List<Double> ventas;  // Lista que almacena las ventas de la sede, usando tipo Double para manejar valores decimales.

    // Constructor que recibe el nombre de la sede.
    public Sede(String nombre) {
        this.nombre = nombre;  // Inicializa el nombre de la sede.
        this.ventas = new ArrayList<>();  // Inicializa la lista de ventas como un ArrayList vacío.
    }

    // Método getter que retorna el nombre de la sede.
    public String getNombre() {
        return nombre;
    }

    // Método getter que retorna la lista de ventas de la sede.
    public List<Double> getVentas() {
        return ventas;
    }

    // Método para agregar una venta a la lista de ventas.
    public void agregarVenta(double venta) {
        ventas.add(venta);  // Añade la venta proporcionada a la lista de ventas.
    }

    // Método que calcula el promedio de ventas de la sede.
    public double calcularPromedio() {
        double suma = 0;  // Variable para acumular la suma de las ventas.
        
        // Itera sobre todas las ventas de la sede para sumarlas.
        for (double venta : ventas) {
            suma += venta;
        }
        
        // Retorna el promedio dividiendo la suma de ventas entre la cantidad de ventas.
        return suma / ventas.size();
    }

    // Método que aumenta las ventas que están por debajo del promedio en un porcentaje dado.
    public void aumentarVentasDebajoPromedio(double porcentaje) {
        double promedio = calcularPromedio();  // Calcula el promedio de ventas de la sede.

        // Itera sobre todas las ventas de la sede.
        for (int i = 0; i < ventas.size(); i++) {
            // Si una venta es menor que el promedio, se aumenta en el porcentaje indicado.
            if (ventas.get(i) < promedio) {
                // Se actualiza la venta multiplicándola por (1 + porcentaje/100) para aumentar la venta.
                ventas.set(i, ventas.get(i) * (1 + porcentaje / 100));
            }
        }
    }
}
