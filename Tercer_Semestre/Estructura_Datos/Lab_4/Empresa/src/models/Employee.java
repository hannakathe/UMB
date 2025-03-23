// Definimos el paquete donde se encuentra la clase Employee.
package models;

// Definimos la clase Employee.
public class Employee {
    // Declaramos una variable privada de tipo entero para almacenar el RUT del empleado.
    private int rut;
    
    // Declaramos una variable privada de tipo float para almacenar el salario del empleado.
    private float salary;
    
    // Declaramos una variable privada de tipo float para almacenar el bono del empleado.
    private float bonus;

    // Constructor de la clase Employee, que recibe el RUT y el salario como parámetros.
    public Employee(int rut, float salary) {
        this.rut = rut;   // Asigna el valor del parámetro 'rut' al atributo de la clase.
        this.salary = salary;  // Asigna el valor del parámetro 'salary' al atributo de la clase.
        this.bonus = 0;    // Inicializa el bono en 0.
    }

    // Método getter para obtener el RUT del empleado.
    public int getRut() {
        return rut;
    }

    // Método getter para obtener el salario del empleado.
    public float getSalary() {
        return salary;
    }

    // Método getter para obtener el bono del empleado.
    public float getBonus() {
        return bonus;
    }

    // Método setter para establecer el bono del empleado.
    public void setBonus(float bonus) {
        this.bonus = bonus;  // Asigna el valor recibido como parámetro al atributo bonus.
    }
}
