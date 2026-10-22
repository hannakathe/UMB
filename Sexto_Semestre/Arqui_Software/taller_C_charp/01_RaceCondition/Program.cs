using System;
using System.Collections.Generic;
using System.Threading;

public class ERP_Architecture_Demo
{
    // ANTI-PATRÓN: estado global público (Space-Based mal implementado)
    public static Dictionary<string, int> InventarioGlobal = new Dictionary<string, int>();
    public static int OrdenesProcesadas = 0;

    public static void Main()
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;
        Console.WriteLine("--- INICIANDO ERP MULTIHILO ---");
        InventarioGlobal.Add("Laptop", 100);
        List<Thread> tuberias = new List<Thread>();
        for (int i = 0; i < 50; i++)
        {
            Thread peticion = new Thread(EjecutarPipeFilter);
            tuberias.Add(peticion);
            peticion.Start();
        }
        foreach (Thread t in tuberias) t.Join();   // Orquestador

        Console.WriteLine($"Órdenes procesadas: {OrdenesProcesadas}");
        Console.WriteLine($"Inventario final (deben quedar 50): {InventarioGlobal["Laptop"]}");

        if (InventarioGlobal["Laptop"] != 50 || OrdenesProcesadas != 50)
            Console.WriteLine("¡ERROR ARQUITECTÓNICO CRÍTICO! El estado del ERP es inconsistente.");
    }

    public static void EjecutarPipeFilter()
    {
        ValidarOrden();         // Filtro 1
        DescontarInventario();  // Filtro 2
        RegistrarMetrica();     // Filtro 3
    }

    private static void ValidarOrden() { Thread.Sleep(10); }

    private static void DescontarInventario()
    {
        int stockActual = InventarioGlobal["Laptop"];   // LEER
        Thread.Sleep(5);                                // I/O simulado
        InventarioGlobal["Laptop"] = stockActual - 1;   // ESCRIBIR (pisa a otros hilos)
    }

    private static void RegistrarMetrica() { OrdenesProcesadas++; }   // no atómico
}
