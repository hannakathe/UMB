// Define un paquete llamado 'models' para organizar clases relacionadas
package models;

// Declaración de la clase pública 'ExpressionResult'
public class ExpressionResult {
    // Campo privado para almacenar la expresión en notación postfija (notación polaca inversa)
    private String postfix;
    
    // Campo privado para almacenar el resultado numérico de la expresión
    private double result;

    // Constructor de la clase que recibe dos parámetros:
    // - postfix: la expresión en notación postfija
    // - result: el resultado calculado de la expresión
    public ExpressionResult(String postfix, double result) {
        // Asigna el parámetro postfix al campo de clase postfix
        this.postfix = postfix;
        // Asigna el parámetro result al campo de clase result
        this.result = result;
    }

    // Método getter para obtener la expresión postfija
    // Retorna: la expresión en notación postfija almacenada
    public String getPostfix() {
        return postfix;
    }

    // Método getter para obtener el resultado numérico
    // Retorna: el valor numérico del resultado almacenado
    public double getResult() {
        return result;
    }
}