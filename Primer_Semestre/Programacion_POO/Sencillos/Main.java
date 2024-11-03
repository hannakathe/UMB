/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sencillos;

// Clase principal para la ejecución del programa
public class Main {
    public static void main(String[] args) {
        // Crear un objeto de tipo Persona
        Persona miPersona = new Persona();
        
        // Establecer el nombre y la edad
        miPersona.setNombre("Juan");
        miPersona.setEdad(25);
        
        // Mostrar los detalles de la persona
        System.out.println("Detalles de la persona:");
        System.out.println("Nombre: " + miPersona.getNombre());
        System.out.println("Edad: " + miPersona.getEdad());
    }
}