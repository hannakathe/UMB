import model.Empresa;
import service.EmpresaService;

public class MainVentas {
    public static void main(String[] args) {
        Empresa empresa = new Empresa();
        EmpresaService service = new EmpresaService(empresa);

        empresa.mostrarVentas();
        System.out.println("\nPromedio semanal de la empresa: " + service.promedioSemanal());
        
        double[] promedios = service.promedioPorSede();
        System.out.println("Promedio por sede:");
        for (int i = 0; i < promedios.length; i++) {
            System.out.println("Sede " + (i + 1) + ": " + promedios[i]);
        }
        
        service.diasSobrePromedio();
        service.aumentarVentasBajas();
        
        System.out.println("\nVentas después del aumento:");
        empresa.mostrarVentas();
    }
}

