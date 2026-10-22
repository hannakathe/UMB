using System;
using System.Collections.Generic;
using System.Threading;

// 1. Encapsulamiento estricto y alta cohesión
public class GestorInventario
{
    private int stockLaptops = 100;
    private int ordenesProcesadas = 0;
    private readonly object candadoTransaccional = new object();

    public int StockLaptops => stockLaptops;
    public int OrdenesProcesadas => ordenesProcesadas;

    // Único punto de entrada para modificar el estado
    public void ProcesarTransaccionSegura()
    {
        lock (candadoTransaccional)
        {
            // SECCIÓN CRÍTICA AISLADA
            stockLaptops--;
            ordenesProcesadas++;
        }
    }
}

public class ERP_Architecture_Fixed
{
    private static readonly GestorInventario gestor = new GestorInventario();

    public static void Main()
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;
        Console.WriteLine("--- INICIANDO ERP MULTIHILO (REFACTORIZADO) ---");
        Console.WriteLine($"Inventario Inicial de Laptops: {gestor.StockLaptops}\n");

        List<Thread> tuberias = new List<Thread>();
        for (int i = 0; i < 50; i++)
        {
            Thread peticion = new Thread(EjecutarPipeFilter);
            tuberias.Add(peticion);
            peticion.Start();
        }
        foreach (Thread t in tuberias) t.Join();

        Console.WriteLine("\n--- REPORTE FINAL DEL ERP ---");
        Console.WriteLine($"Órdenes realmente procesadas: {gestor.OrdenesProcesadas}");
        Console.WriteLine($"Inventario Final de Laptops: {gestor.StockLaptops}");
    }

    public static void EjecutarPipeFilter()
    {
        ValidarOrden();                       // Filtro 1
        gestor.ProcesarTransaccionSegura();   // Filtros 2 y 3, atómicos
    }

    private static void ValidarOrden() { Thread.Sleep(10); }
}
