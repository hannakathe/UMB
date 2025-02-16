package model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Bus {
    private String nombre;
    // Para cada día (índices 0 a 6) se almacena una lista de ventas.
    private List<List<Double>> ventasDiarias;

    public Bus(String nombre) {
        this.nombre = nombre;
        ventasDiarias = new ArrayList<>();
        // Inicializa 7 días (Lunes a Domingo)
        for (int i = 0; i < 7; i++) {
            ventasDiarias.add(new ArrayList<>());
        }
    }

    public String getNombre() {
        return nombre;
    }

    // Establece las ventas de un día (índice 0-6)
    public void setVentasParaDia(int dia, List<Double> ventas) {
        ventasDiarias.set(dia, ventas);
    }

    // Retorna las ventas del día indicado
    public List<Double> getVentasPorDia(int dia) {
        return ventasDiarias.get(dia);
    }

    public List<List<Double>> getVentasDiarias() {
        return ventasDiarias;
    }

    // Suma las ventas de un día
    public double totalVentasPorDia(int dia) {
        double suma = 0;
        for (Double venta : ventasDiarias.get(dia)) {
            suma += venta;
        }
        return suma;
    }

    // Calcula el promedio de las ventas diarias (suma total de cada día / 7)
    @SuppressWarnings("unused")
    public double calcularPromedioVentas() {
        double suma = 0;
        for (int i = 0; i < 7; i++) {
            suma += totalVentasPorDia(i);
        }
        return 7 == 0 ? 0 : suma / 7;
    }

    // Suma total de ventas en la semana
    public double totalVentas() {
        double total = 0;
        for (int i = 0; i < 7; i++) {
            total += totalVentasPorDia(i);
        }
        return total;
    }

    // Retorna el índice del día con mayor total de ventas
    public int diaMayorVenta() {
        int indiceMax = 0;
        double max = totalVentasPorDia(0);
        for (int i = 1; i < 7; i++) {
            double totalDia = totalVentasPorDia(i);
            if (totalDia > max) {
                max = totalDia;
                indiceMax = i;
            }
        }
        return indiceMax;
    }

    // Aumenta en "porcentaje" % las ventas (cada valor) de los días cuyo total esté por debajo del promedio
    public void aumentarVentasDebajoPromedio(double porcentaje) {
        double promedioDouble = calcularPromedioVentas();
        BigDecimal promedio = new BigDecimal(Double.toString(promedioDouble));
        BigDecimal factor = BigDecimal.ONE.add(new BigDecimal(Double.toString(porcentaje))
                .divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP));

        for (int dia = 0; dia < 7; dia++) {
            double totalDia = totalVentasPorDia(dia);
            BigDecimal totalDiaBD = new BigDecimal(Double.toString(totalDia));
            if (totalDiaBD.compareTo(promedio) < 0) {
                List<Double> ventas = ventasDiarias.get(dia);
                for (int i = 0; i < ventas.size(); i++) {
                    BigDecimal ventaBD = new BigDecimal(Double.toString(ventas.get(i)));
                    BigDecimal ventaActualizada = ventaBD.multiply(factor);
                    ventas.set(i, ventaActualizada.setScale(2, RoundingMode.HALF_UP).doubleValue());
                }
            }
        }
    }

    // Retorna una representación en cadena de las ventas de un día, separadas por coma
    public String obtenerVentasComoString(int dia) {
        List<Double> ventas = ventasDiarias.get(dia);
        if (ventas.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ventas.size(); i++) {
            sb.append(String.format("%.2f", ventas.get(i)));
            if (i < ventas.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
