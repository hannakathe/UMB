using System;

public class ArquitecturaSaga
{
    // Microservicio Inventario (su propia "base de datos")
    public static int InventarioLaptops = 5;
    // Microservicio Pagos (su propia "base de datos")
    public static decimal SaldoCliente = 1000m;

    public static void Main()
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;
        Console.WriteLine("--- COMPRA CON PATRÓN SAGA (ORQUESTACIÓN) ---");
        Console.WriteLine($"Estado Inicial -> Laptops: {InventarioLaptops} | Saldo: ${SaldoCliente}\n");

        OrquestadorSagaCompra(1500m);

        Console.WriteLine($"\nEstado Final -> Laptops: {InventarioLaptops} | Saldo: ${SaldoCliente}");
    }

    public static void OrquestadorSagaCompra(decimal costo)
    {
        bool inventarioDescontado = false;
        try
        {
            DescontarInventario();          // T1
            inventarioDescontado = true;    // estado del paso
            ProcesarPago(costo);            // T2
            Console.WriteLine("SAGA COMPLETADA");
        }
        catch (Exception ex)
        {
            Console.WriteLine($"[FALLO EN LA SAGA] {ex.Message}");
            if (inventarioDescontado)
                CompensarInventario();      // C1: deshace T1
            Console.WriteLine("SAGA ABORTADA CORRECTAMENTE");
        }
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

    public static void CompensarInventario()
    {
        Console.WriteLine("<= Inventario (COMPENSACIÓN): Devolviendo 1 Laptop...");
        InventarioLaptops++;
    }
}
