package service;

import model.Vehiculo;
import java.util.Scanner;

public class VehiculoService {
    private Vehiculo[] vehiculos;
    private Scanner scanner;

    public VehiculoService(int n) {
        vehiculos = new Vehiculo[n];
        scanner = new Scanner(System.in);
    }

    public void agregarVehiculos() {
        for (int i = 0; i < vehiculos.length; i++) {
            System.out.print("Ingrese la marca del vehículo " + (i + 1) + ": ");
            String marca = scanner.nextLine();
            vehiculos[i] = new Vehiculo(marca);

            for (int year = 2019; year <= 2023; year++) {
                double precio;
                do {
                    System.out.print("Ingrese el precio para " + marca + " en el año " + year + ": ");
                    precio = scanner.nextDouble();
                    try {
                        vehiculos[i].setPrecio(year, precio);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                } while (precio <= 0);
            }
            scanner.nextLine(); // Consumir el salto de línea
        }
    }

    public void mostrarListaAutos() {
        for (Vehiculo vehiculo : vehiculos) {
            System.out.println("Marca: " + vehiculo.getMarca());
            double[] precios = vehiculo.getPrecios();
            for (int i = 0; i < precios.length; i++) {
                System.out.println("Año " + (2019 + i) + ": $" + precios[i]);
            }
        }
    }

    public void mostrarAutoMasBaratoCadaAño() {
        for (int year = 2019; year <= 2023; year++) {
            Vehiculo masBarato = vehiculos[0];
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPrecio(year) < masBarato.getPrecio(year)) {
                    masBarato = vehiculo;
                }
            }
            System.out.println("El auto más barato del año " + year + " es " + masBarato.getMarca() + " con un precio de $" + masBarato.getPrecio(year));
        }
    }

    public void mostrarPromedioPrecioRango(int year) {
        double suma = 0;
        int contador = 0;
        for (Vehiculo vehiculo : vehiculos) {
            double precio = vehiculo.getPrecio(year);
            if (precio >= 30_000_000 && precio <= 50_000_000) {
                suma += precio;
                contador++;
            }
        }
        if (contador > 0) {
            double promedio = suma / contador;
            System.out.println("El promedio de los autos que cuestan entre 30 y 50 millones en el año " + year + " es $" + promedio);
        } else {
            System.out.println("No hay autos en el rango de 30 a 50 millones en el año " + year);
        }
    }
}

