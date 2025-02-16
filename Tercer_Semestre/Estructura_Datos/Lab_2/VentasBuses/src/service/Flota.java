package service;

import model.Bus;
import java.util.ArrayList;
import java.util.List;

public class Flota {
    private List<Bus> buses;

    // Constructor que recibe la cantidad de buses intermunicipales
    public Flota(int cantidadBuses) {
        this.buses = new ArrayList<>();
        for (int i = 0; i < cantidadBuses; i++) {
            buses.add(new Bus("Bus " + (i + 1)));
        }
    }

    public List<Bus> getBuses() {
        return buses;
    }

    public void mostrarVentasSobrePromedio() {
        for (Bus bus : buses) {
            double promedio = bus.calcularPromedioVentas();
            System.out.println("\nVentas sobre el promedio para " + bus.getNombre() + ":");
            for (int i = 0; i < 7; i++) {
                double totalDia = bus.totalVentasPorDia(i);
                if (totalDia > promedio) {
                    System.out.println("Día " + (i + 1) + ": " + totalDia);
                }
            }
        }
    }

    public double calcularPromedioGeneral() {
        double sumaTotal = 0;
        int cantidadDias = 0;
        for (Bus bus : buses) {
            for (int i = 0; i < 7; i++) {
                sumaTotal += bus.totalVentasPorDia(i);
                cantidadDias++;
            }
        }
        return cantidadDias == 0 ? 0 : sumaTotal / cantidadDias;
    }

    public Bus busMayorGanancia() {
        if (buses.isEmpty()) return null;
        Bus mayor = buses.get(0);
        for (Bus bus : buses) {
            if (bus.totalVentas() > mayor.totalVentas()) {
                mayor = bus;
            }
        }
        return mayor;
    }

    public Bus busMenorGanancia() {
        if (buses.isEmpty()) return null;
        Bus menor = buses.get(0);
        for (Bus bus : buses) {
            if (bus.totalVentas() < menor.totalVentas()) {
                menor = bus;
            }
        }
        return menor;
    }

    public void mostrarBusMayorYMenorGanancia() {
        Bus mayor = busMayorGanancia();
        Bus menor = busMenorGanancia();
        if (mayor != null && menor != null) {
            System.out.println("\nBus con mayor ganancia en la semana: " + mayor.getNombre() + " (Total: " + mayor.totalVentas() + ")");
            System.out.println("Bus con menor ganancia en la semana: " + menor.getNombre() + " (Total: " + menor.totalVentas() + ")");
        }
    }

    public void mostrarDiaMayorVentaPorBus() {
        String[] diasSemana = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        for (Bus bus : buses) {
            int indiceDia = bus.diaMayorVenta();
            if (indiceDia >= 0 && indiceDia < diasSemana.length) {
                System.out.println(bus.getNombre() + " - Día con mayor venta: " + diasSemana[indiceDia]
                        + " (Venta: " + bus.totalVentasPorDia(indiceDia) + ")");
            }
        }
    }

    public void aumentarVentasDebajoPromedio(int i) {
        for (Bus bus : buses) {
            bus.aumentarVentasDebajoPromedio(20);
        }
    }
}
