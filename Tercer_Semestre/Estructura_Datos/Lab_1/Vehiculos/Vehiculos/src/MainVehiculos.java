import service.VehiculoService;
import java.util.Scanner;

public class MainVehiculos {
    public static void main(String[] args) {
        // Crea un objeto Scanner para leer la entrada del usuario
        Scanner scanner = new Scanner(System.in);

        // Solicita al usuario el número de vehículos que desea ingresar
        System.out.print("Ingrese el número de vehículos: ");
        int n = scanner.nextInt();  // Lee el número de vehículos

        // Crea una instancia de VehiculoService con el número de vehículos proporcionado
        VehiculoService vehiculoService = new VehiculoService(n);

        // Llama al método agregarVehiculos() para permitir al usuario ingresar los detalles de los vehículos
        vehiculoService.agregarVehiculos();

        // Muestra la lista de vehículos con sus precios por año
        vehiculoService.mostrarListaAutos();

        // Muestra el vehículo más barato de cada año (2019-2023)
        vehiculoService.mostrarAutoMasBaratoCadaAño();

        // Solicita al usuario un año para calcular el promedio de precios en el rango de 30 a 50 millones
        System.out.print("Ingrese el año para calcular el promedio de precios entre 30 y 50 millones: ");
        int año = scanner.nextInt();  // Lee el año seleccionado por el usuario

        // Muestra el promedio de precios de los vehículos en el rango de 30 a 50 millones para el año ingresado
        vehiculoService.mostrarPromedioPrecioRango(año);

        // Cierra el objeto Scanner para liberar los recursos del sistema
        scanner.close();
    }
}
