//NO SE ESTA USANDO ESTE SCRIOPT

// Definimos el paquete donde se encuentra la clase EmployeeDataGenerator.
package services;

// Importamos la clase Employee desde el paquete models.
import models.Employee;

// Importamos ArrayList y List para manejar listas de empleados.
import java.util.ArrayList;
import java.util.List;

// Importamos Random para generar números aleatorios.
import java.util.Random;

// Definimos la clase EmployeeDataGenerator, que se encarga de generar empleados ficticios.
public class EmployeeDataGenerator {

    // Método estático para generar una lista de empleados con datos aleatorios.
    public static List<Employee> generateEmployees(int count) {
        // Creamos una lista vacía para almacenar los empleados generados.
        List<Employee> employees = new ArrayList<>();
        
        // Creamos un objeto de la clase Random para generar valores aleatorios.
        Random rand = new Random();

        // Bucle para generar la cantidad de empleados indicada por el parámetro 'count'.
        for (int i = 0; i < count; i++) {
            // Generamos un RUT ficticio entre 10,000,000 y 99,999,999.
            int rut = 10000000 + rand.nextInt(90000000);
            
            // Generamos un salario aleatorio entre 500,000 y 3,500,000.
            float salary = 500000 + rand.nextFloat() * 3000000;

            // Creamos un nuevo empleado con el RUT y salario generados y lo agregamos a la lista.
            employees.add(new Employee(rut, salary));
        }

        // Retornamos la lista de empleados generados.
        return employees;
    }
}
