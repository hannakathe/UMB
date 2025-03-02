// Definimos el paquete al que pertenece esta clase, llamado 'service'.
// Esto organiza el código y permite que otras clases lo importen cuando sea necesario.
package service;

// Importamos la clase Bus desde el paquete 'model'.
import model.Bus;

// Importamos ArrayList y List para manejar colecciones de objetos 'Bus'.
import java.util.ArrayList;
import java.util.List;

// Definimos la clase 'Flota', que representa un conjunto de buses.
public class Flota {
    
    // Lista que almacena los buses de la flota.
    private List<Bus> buses;

    // Constructor que recibe la cantidad de buses intermunicipales y los inicializa.
    public Flota(int cantidadBuses) {
        // Inicializamos la lista de buses como un ArrayList vacío.
        this.buses = new ArrayList<>();
        
        // Creamos los buses con nombres "Bus 1", "Bus 2", ..., según la cantidad dada.
        for (int i = 0; i < cantidadBuses; i++) {
            buses.add(new Bus("Bus " + (i + 1)));
        }
    }

    // Getter Método que devuelve la lista de buses de la flota.
    public List<Bus> getBuses() {
        return buses;
    }

    // Método que muestra los días en los que cada bus tuvo ventas por encima del promedio.
    public void mostrarVentasSobrePromedio() {
        for (Bus bus : buses) {
            // Calculamos el promedio de ventas del bus.
            double promedio = bus.calcularPromedioVentas(); //Obtiene el metodo para calcular el promedio de Bus
            System.out.println("\nVentas sobre el promedio para " + bus.getNombre() + ":");

            // Recorremos los 7 días de la semana verificando si la venta del día supera el promedio.
            for (int i = 0; i < 7; i++) {
                double totalDia = bus.totalVentasPorDia(i);
                if (totalDia > promedio) {
                    System.out.println("Día " + (i + 1) + ": " + totalDia);
                }
            }
        }
    }

    // Método que calcula el promedio general de ventas de todos los buses en la semana.
    public double calcularPromedioGeneral() {
        double sumaTotal = 0;
        int cantidadDias = 0;

        // Sumamos todas las ventas de cada bus en los 7 días de la semana.
        for (Bus bus : buses) {
            for (int i = 0; i < 7; i++) {
                sumaTotal += bus.totalVentasPorDia(i);
                cantidadDias++;
            }
        }

        // Retornamos el promedio, evitando la división por cero.
        return cantidadDias == 0 ? 0 : sumaTotal / cantidadDias;
    }

    // Método que encuentra el bus con mayor ganancia en la semana.
    public Bus busMayorGanancia() {
        // Si no hay buses, retornamos null.
        if (buses.isEmpty()) return null;

        // Inicializamos con el primer bus como el de mayor ganancia.
        Bus mayor = buses.get(0);
        
        // Recorremos la lista de buses para encontrar el de mayor ganancia total.
        for (Bus bus : buses) {
            if (bus.totalVentas() > mayor.totalVentas()) {
                mayor = bus;
            }
        }

        return mayor;
    }

    // Método que encuentra el bus con menor ganancia en la semana.
    public Bus busMenorGanancia() {
        // Si no hay buses, retornamos null.
        if (buses.isEmpty()) return null;

        // Inicializamos con el primer bus como el de menor ganancia.
        Bus menor = buses.get(0);
        
        // Recorremos la lista de buses para encontrar el de menor ganancia total.
        for (Bus bus : buses) {
            if (bus.totalVentas() < menor.totalVentas()) {
                menor = bus;
            }
        }

        return menor;
    }

    // Método que muestra los buses con mayor y menor ganancia en la semana.
    public void mostrarBusMayorYMenorGanancia() {
        Bus mayor = busMayorGanancia();
        Bus menor = busMenorGanancia();

        // Verificamos que existan buses antes de imprimir.
        if (mayor != null && menor != null) {
            System.out.println("\nBus con mayor ganancia en la semana: " + mayor.getNombre() 
                               + " (Total: " + mayor.totalVentas() + ")");
            System.out.println("Bus con menor ganancia en la semana: " + menor.getNombre() 
                               + " (Total: " + menor.totalVentas() + ")");
        }
    }

    // Método que muestra el día con mayor venta para cada bus.
    public void mostrarDiaMayorVentaPorBus() {
        // Arreglo con los nombres de los días de la semana.
        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        for (Bus bus : buses) {
            // Obtenemos el índice del día con mayor venta.
            int indiceDia = bus.diaMayorVenta();
            
            // Verificamos que el índice sea válido y mostramos el resultado.
            if (indiceDia >= 0 && indiceDia < diasSemana.length) {
                System.out.println(bus.getNombre() + " - Día con mayor venta: " + diasSemana[indiceDia] 
                                   + " (Venta: " + bus.totalVentasPorDia(indiceDia) + ")");
            }
        }
    }

    // Método que aumenta en 20% las ventas de los días que están por debajo del promedio.
    public void aumentarVentasDebajoPromedio(int porcentaje) {
        for (Bus bus : buses) {
            bus.aumentarVentasDebajoPromedio(porcentaje);
        }
    }
}
