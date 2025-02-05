// Importación de las clases necesarias
import model.Sede;  // Importa la clase 'Sede' del paquete 'model', donde se gestiona cada sede.
import services.Empresa;  // Importa la clase 'Empresa' del paquete 'services', donde se gestionan las sedes de la empresa.
import java.util.Scanner;  // Importa la clase 'Scanner' para la entrada de datos desde el teclado.

public class MainVentas {
    public static void main(String[] args) {
        // Se crea un objeto Scanner para capturar la entrada de datos del usuario.
        Scanner scanner = new Scanner(System.in);

        // Solicita al usuario la cantidad de sedes que tiene la empresa.
        System.out.print("Ingrese la cantidad de sedes: ");
        int cantidadDeSedes = scanner.nextInt();  // El usuario ingresa el número de sedes.

        // Se crea una instancia de la clase 'Empresa', pasando como parámetro la cantidad de sedes que el usuario ingresó.
        Empresa empresa = new Empresa(cantidadDeSedes);

        // Itera sobre cada sede en la empresa para ingresar las ventas de 7 días.
        for (Sede sede : empresa.getSedes()) {
            System.out.println("Ingrese las ventas para " + sede.getNombre() + " (7 días):");
            // Itera 7 veces (para cada día de la semana)
            for (int i = 0; i < 7; i++) {
                // Solicita al usuario ingresar las ventas de cada día.
                System.out.print("Día " + (i + 1) + ": ");
                double venta = scanner.nextDouble();  // El usuario ingresa el valor de la venta.
                sede.agregarVenta(venta);  // La venta se agrega a la lista de ventas de la sede.
            }
        }

        // Imprime las ventas que están por encima del promedio de cada sede.
        System.out.println("\nVentas sobre el promedio de cada sede:");
        System.out.println("");  // Salto de línea para mejorar la legibilidad.
        empresa.mostrarVentasSobrePromedio();  // Llama al método para mostrar las ventas sobre el promedio de cada sede.

        // Calcula e imprime el promedio general de ventas de toda la empresa.
        System.out.println("\nPromedio de ventas total de la empresa: " + empresa.calcularPromedioGeneral());

        // Llama al método que aumenta en un 15% las ventas por debajo del promedio de cada sede.
        empresa.aumentarVentasDebajoPromedio(15);  // El parámetro 15 representa el porcentaje de aumento.

        // Imprime las ventas después de haber aumentado las que estaban por debajo del promedio.
        System.out.println("\nVentas después de aumentar las ventas diarias por debajo del promedio en un 15%:");
        System.out.println("");  // Salto de línea para mejorar la legibilidad.

        // Itera nuevamente sobre las sedes para mostrar sus ventas después del aumento.
        for (Sede sede : empresa.getSedes()) {
            // Imprime las ventas de cada sede después del ajuste.
            System.out.println("Ventas para " + sede.getNombre() + ": " + sede.getVentas());
            System.out.println("");  // Salto de línea para mejorar la legibilidad.
        }

        // Cierra el objeto Scanner para liberar recursos.
        scanner.close();
    }
}
