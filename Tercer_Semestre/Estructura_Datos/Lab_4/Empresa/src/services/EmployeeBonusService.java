// Definimos el paquete donde se encuentra la clase EmployeeBonusService.
package services;

// Importamos la clase Employee del paquete models.
import models.Employee;

// Importamos la clase List para manejar listas de empleados.
import java.util.List;

// Importamos Collectors para recopilar resultados de operaciones en streams.
import java.util.stream.Collectors;

// Definimos la clase EmployeeBonusService que contiene métodos estáticos para calcular información sobre empleados.
public class EmployeeBonusService {

    // Método para calcular el salario promedio de una lista de empleados.
    public static float getAverageSalary(List<Employee> employees) {
        // Si la lista está vacía, retornamos 0 para evitar división por cero.
        if (employees.isEmpty()) return 0;
        
        float totalSalary = 0; // Variable para acumular el salario total.

        // Iteramos sobre la lista de empleados y sumamos sus salarios.
        for (Employee emp : employees) {
            totalSalary += emp.getSalary();
        }

        // Retornamos el salario promedio dividiendo el total entre el número de empleados.
        return totalSalary / employees.size();
    }

    // Método para obtener la lista de empleados cuyo salario es menor al promedio.
    public static List<Employee> getEmployeesBelowAverage(List<Employee> employees, float averageSalary) {
        return employees.stream() // Convertimos la lista en un stream.
                .filter(emp -> emp.getSalary() < averageSalary) // Filtramos empleados con salario menor al promedio.
                .collect(Collectors.toList()); // Convertimos el resultado en una lista.
    }

    // Método para obtener el empleado con el mayor bono.
    public static Employee getHighestBonusEmployee(List<Employee> employees) {
        return employees.stream() // Convertimos la lista en un stream.
                .max((e1, e2) -> Float.compare(e1.getBonus(), e2.getBonus())) // Buscamos el empleado con el mayor bono.
                .orElse(null); // Retornamos null si no hay empleados.
    }

    // Método para obtener el empleado con el menor bono mayor a 0.
    public static Employee getLowestBonusEmployee(List<Employee> employees) {
        return employees.stream() // Convertimos la lista en un stream.
                .filter(emp -> emp.getBonus() > 0) // Filtramos empleados con bono mayor a 0.
                .min((e1, e2) -> Float.compare(e1.getBonus(), e2.getBonus())) // Buscamos el empleado con el menor bono.
                .orElse(null); // Retornamos null si no hay empleados con bono mayor a 0.
    }

    // Método para calcular el costo total de los bonos de todos los empleados.
    public static float getTotalBonusCost(List<Employee> employees) {
        return employees.stream() // Convertimos la lista en un stream.
                .map(Employee::getBonus) // Extraemos los bonos de los empleados.
                .reduce(0f, Float::sum); // Sumamos todos los bonos y retornamos el total.
    }

    // Método para buscar un empleado por su RUT.
    public static Employee findEmployeeByRut(List<Employee> employees, int rut) {
        return employees.stream() // Convertimos la lista en un stream.
                .filter(emp -> emp.getRut() == rut) // Filtramos al empleado con el RUT especificado.
                .findFirst() // Tomamos el primer resultado encontrado.
                .orElse(null); // Retornamos null si no se encuentra ningún empleado con ese RUT.
    }
}
