package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

// Clase que representa un Bus y gestiona sus ventas diarias
public class Bus {
    private String nombre; // Nombre del bus
    private List<List<Double>> ventasDiarias; // Lista de listas que almacena las ventas de cada día de la semana

    // Constructor que inicializa el bus con un nombre y crea una lista vacía de ventas para cada día
    public Bus(String nombre) {
        this.nombre = nombre;
        ventasDiarias = new ArrayList<>();
        // Se inicializan 7 listas internas, una para cada día de la semana (lunes a domingo)
        for (int i = 0; i < 7; i++) {
            ventasDiarias.add(new ArrayList<>());
        }
    }

    // Método para obtener el nombre del bus
    public String getNombre() {
        return nombre;
    }

    // Método que establece la lista de ventas para un día específico (0 = Lunes, 6 = Domingo)
    public void setVentasParaDia(int dia, List<Double> ventas) {
        ventasDiarias.set(dia, ventas);
    }

    // Método que devuelve la lista de ventas de un día específico
    public List<Double> getVentasPorDia(int dia) {
        return ventasDiarias.get(dia);
    }

    // Método que devuelve todas las ventas de la semana
    public List<List<Double>> getVentasDiarias() {
        return ventasDiarias;
    }

    // Método que calcula la suma total de ventas de un día específico
    public double totalVentasPorDia(int dia) {
        double suma = 0;
        for (Double venta : ventasDiarias.get(dia)) { // Recorre la lista de ventas de ese día y las suma
            suma += venta;
        }
        return suma; // Retorna la suma total de ese día
    }

    // Método que calcula el promedio de ventas de la semana
    @SuppressWarnings("unused")
    public double calcularPromedioVentas() {
        double suma = 0;
        for (int i = 0; i < 7; i++) { // Suma todas las ventas de cada día
            suma += totalVentasPorDia(i);
        }
        return 7 == 0 ? 0 : suma / 7; // Calcula el promedio dividiendo la suma entre 7 (número de días)
    }

    // Método que calcula el total de ventas en toda la semana
    public double totalVentas() {
        double total = 0;
        for (int i = 0; i < 7; i++) { // Suma las ventas de todos los días
            total += totalVentasPorDia(i);
        }
        return total; // Devuelve la suma total de ventas de la semana
    }

    // Método que encuentra el día con la mayor cantidad de ventas
    public int diaMayorVenta() {
        int indiceMax = 0; // Inicializa el índice con el primer día (lunes)
        double max = totalVentasPorDia(0); // Guarda el total de ventas del primer día como máximo inicial
        for (int i = 1; i < 7; i++) { // Recorre los demás días
            double totalDia = totalVentasPorDia(i);
            if (totalDia > max) { // Si se encuentra un día con más ventas, se actualiza el índice y el máximo
                max = totalDia;
                indiceMax = i;
            }
        }
        return indiceMax; // Retorna el índice del día con más ventas (0 = lunes, 6 = domingo)
    }

    // Método que aumenta en un porcentaje las ventas de los días donde el total está por debajo del promedio
    public void aumentarVentasDebajoPromedio(double porcentaje) {
        double promedioDouble = calcularPromedioVentas(); // Se obtiene el promedio de ventas de la semana
        BigDecimal promedio = new BigDecimal(Double.toString(promedioDouble));
        
        // Se calcula el factor de incremento (ejemplo: 10% se convierte en 1.1)
        BigDecimal factor = BigDecimal.ONE.add(new BigDecimal(Double.toString(porcentaje))
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));

        // Se recorren todos los días de la semana
        for (int dia = 0; dia < 7; dia++) {
            double totalDia = totalVentasPorDia(dia); // Se obtiene el total de ventas de ese día
            BigDecimal totalDiaBD = new BigDecimal(Double.toString(totalDia));

            // Si el total de ventas del día es menor que el promedio, se incrementan sus ventas
            if (totalDiaBD.compareTo(promedio) < 0) {
                List<Double> ventas = ventasDiarias.get(dia);
                for (int i = 0; i < ventas.size(); i++) {
                    // Convierte la venta a BigDecimal para precisión en el cálculo
                    BigDecimal ventaBD = new BigDecimal(Double.toString(ventas.get(i)));
                    // Aplica el incremento multiplicando por el factor
                    BigDecimal ventaActualizada = ventaBD.multiply(factor);
                    // Redondea a 2 decimales y guarda la nueva venta en la lista
                    ventas.set(i, ventaActualizada.setScale(2, RoundingMode.HALF_UP).doubleValue());
                }
            }
        }
    }

    // Método que genera una cadena con las ventas de un día, separadas por coma
    public String obtenerVentasComoString(int dia) {
        List<Double> ventas = ventasDiarias.get(dia);
        if (ventas.isEmpty()) return ""; // Si no hay ventas, retorna una cadena vacía
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ventas.size(); i++) {
            sb.append(String.format("%.2f", ventas.get(i))); // Formatea cada venta a 2 decimales
            if (i < ventas.size() - 1) {
                sb.append(", "); // Agrega coma entre valores
            }
        }
        return sb.toString(); // Retorna la lista de ventas en formato de texto
    }
}
 