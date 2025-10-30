import controller.ClienteController;
import model.Cliente;

public class Main {
    public static void main(String[] args) {
        ClienteController controller = new ClienteController();

        System.out.println("=== LISTA DE CLIENTES ===");
        controller.listar().forEach(c ->
            System.out.println(c.getNombres() + " - " + c.getCorreo())
        );

        // Ejemplo de inserción
        Cliente nuevo = new Cliente("CC", "4001", "Laura Herrera", "laura@mail.com", "Cra 80 #45-22", "3155551234", 1, 1);
        controller.agregar(nuevo);

        System.out.println("Cliente agregado con éxito.");
    }
}
