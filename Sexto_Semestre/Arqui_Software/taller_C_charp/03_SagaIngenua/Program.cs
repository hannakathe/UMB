using System;

public class ArquitecturaIngenua
{
    // Microservicio Inventario (su propia "base de datos")
    public static int InventarioLaptops = 5;
    // Microservicio Pagos (su propia "base de datos")
    public static decimal SaldoCliente = 1000m;

    public static void Main()
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;
        Console.WriteLine("--- COMPRA SIN SAGA (TRY-CATCH INGENUO) ---");
        Console.WriteLine($"Estado Inicial -> Laptops: {InventarioLaptops} | Saldo: ${SaldoCliente}\n");

        try
        {
            DescontarInventario();   // Microservicio Inventario: 5 -> 4  (ya confirmado)
            ProcesarPago(1500m);     // Microservicio Pagos: lanza excepción
            Console.WriteLine("Compra exitosa.");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[ERROR DE SISTEMA] {ex.Message}");
            // Nadie devuelve la laptop: el inventario queda en 4
        }

        Console.WriteLine($"\nEstado Final -> Laptops: {InventarioLaptops} | Saldo: ${SaldoCliente}");
    }

    public static void DescontarInventario()
    {
        Console.WriteLine("=> Inventario: Descontando 1 Laptop...");
        InventarioLaptops--;
    }

    public static void ProcesarPago(decimal costo)
    {
        Console.WriteLine($"=> Pagos: Cobrando ${costo}...");
        if (SaldoCliente < costo)
            throw new Exception("Fondos insuficientes en la cuenta del cliente.");
        SaldoCliente -= costo;
    }
}
