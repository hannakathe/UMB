import service.VehiculoService;
import java.util.Scanner;

public class MainVehiculos {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el número de vehículos: ");
        int n = scanner.nextInt();

        VehiculoService vehiculoService = new VehiculoService(n);
        vehiculoService.agregarVehiculos();

        vehiculoService.mostrarListaAutos();
        vehiculoService.mostrarAutoMasBaratoCadaAno();

        System.out.print("Ingrese el año para calcular el promedio de precios entre 30 y 50 millones: ");
        int anio = scanner.nextInt();
        vehiculoService.mostrarPromedioPrecioRango(anio);

        // Cerrar el Scanner
        scanner.close();
    }
}


