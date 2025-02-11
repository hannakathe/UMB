package service;

import model.Vehiculo;
import java.util.Scanner;

public class VehiculoService {
    // Declaración de los atributos de la clase VehiculoService
    private Vehiculo[] vehiculos;  // Arreglo para almacenar objetos de tipo Vehiculo
    private Scanner scanner;  // Objeto Scanner para recibir entrada del usuario

    // Constructor de la clase VehiculoService, recibe el número de vehículos y crea un arreglo de Vehiculos
    public VehiculoService(int n) {
        vehiculos = new Vehiculo[n];  // Inicializa el arreglo de vehículos con el tamaño pasado como parámetro
        scanner = new Scanner(System.in);  // Inicializa el objeto Scanner para leer entradas del usuario
    }

    // Método para agregar vehículos al arreglo
    public void agregarVehiculos() {
        for (int i = 0; i < vehiculos.length; i++) {
            // Solicita la marca del vehículo
            System.out.print("Ingrese la marca del vehículo " + (i + 1) + ": ");
            String marca = scanner.nextLine();  // Lee la marca del vehículo
            vehiculos[i] = new Vehiculo(marca);  // Crea un nuevo objeto Vehiculo con la marca ingresada

            // Bucle para ingresar los precios de 2019 a 2023
            for (int year = 2019; year <= 2023; year++) {
                double precio;
                do {
                    // Solicita el precio para el vehículo en el año específico
                    System.out.print("Ingrese el precio para " + marca + " en el año " + year + ": ");
                    precio = scanner.nextDouble();  // Lee el precio

                    try {
                        // Intenta establecer el precio para el vehículo en el año correspondiente
                        vehiculos[i].setPrecio(year, precio);
                    } catch (IllegalArgumentException e) {
                        // Si el precio es inválido (negativo o cero), se muestra el mensaje de error
                        System.out.println(e.getMessage()); // La e es la referencia al objeto de la excepción que contiene información sobre el error.
                    }
                } while (precio <= 0);  // Repite si el precio no es válido (mayor que cero)
            }
            scanner.nextLine(); // Consumir el salto de línea restante después de leer el precio
        }
    }

    // Método para mostrar la lista de vehículos y sus precios por año
    public void mostrarListaAutos() {
        // Itera sobre el arreglo de vehículos
        for (Vehiculo vehiculo : vehiculos) {
            System.out.println("Marca: " + vehiculo.getMarca());  // Muestra la marca del vehículo
            double[] precios = vehiculo.getPrecios();  // Obtiene el arreglo de precios del vehículo
            // Muestra el precio de cada vehículo por año (2019 a 2023)
            for (int i = 0; i < precios.length; i++) {
                System.out.println("Año " + (2019 + i) + ": $" + precios[i]);
            }
        }
    }

    // Método para mostrar el auto más barato de cada año (2019-2023)
    public void mostrarAutoMasBaratoCadaAño() {
        // Itera sobre los años de 2019 a 2023
        for (int year = 2019; year <= 2023; year++) {
            Vehiculo masBarato = vehiculos[0];  // Inicializa el primer vehículo como el más barato
            // Busca el vehículo con el precio más bajo para el año específico
            for (Vehiculo vehiculo : vehiculos) {
                if (vehiculo.getPrecio(year) < masBarato.getPrecio(year)) {
                    masBarato = vehiculo;  // Actualiza el vehículo más barato si se encuentra uno más barato
                }
            }
            // Muestra el vehículo más barato del año con su precio
            System.out.println("El auto más barato del año " + year + " es " + masBarato.getMarca() + " con un precio de $" + masBarato.getPrecio(year));
        }
    }

    // Método para calcular el promedio de los precios de los vehículos dentro de un rango específico en un año dado
    public void mostrarPromedioPrecioRango(int year) {
        double suma = 0;  // Variable para acumular los precios dentro del rango
        int contador = 0;  // Contador de vehículos dentro del rango de precios

        // Itera sobre los vehículos
        for (Vehiculo vehiculo : vehiculos) {
            double precio = vehiculo.getPrecio(year);  // Obtiene el precio del vehículo para el año específico
            // Verifica si el precio está dentro del rango de 30 a 50 millones
            if (precio >= 30_000_000 && precio <= 50_000_000) {
                suma += precio;  // Acumula el precio
                contador++;  // Incrementa el contador
            }
        }
        // Si se han encontrado vehículos dentro del rango, calcula y muestra el promedio
        if (contador > 0) {
            double promedio = suma / contador;
            System.out.println("El promedio de los autos que cuestan entre 30 y 50 millones en el año " + year + " es $" + promedio);
        } else {
            // Si no se han encontrado vehículos dentro del rango, se muestra un mensaje indicándolo
            System.out.println("No hay autos en el rango de 30 a 50 millones en el año " + year);
        }
    }
}
