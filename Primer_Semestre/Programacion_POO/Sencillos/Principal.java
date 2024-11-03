package Sencillos;

import otro.Ejemplo;

public class Principal {
    public static void main(String[] args) {
   
        // Crear un objeto de tipo Vehiculo
       Ejemplo miCoche = new Ejemplo("Toyota", "Corolla", 2020);
        
        // Llamar al método imprimirDetalles para mostrar la información del vehículo
        System.out.println("Detalles de mi coche:");
        miCoche.imprimirDetalles();
    }
    }
