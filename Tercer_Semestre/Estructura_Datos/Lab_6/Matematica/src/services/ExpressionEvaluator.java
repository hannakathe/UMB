// Declara que esta clase pertenece al paquete 'services'
package services;

// Importa la clase Stack del framework de colecciones de Java
import java.util.Stack;

// Clase pública que evalúa expresiones matemáticas en notación postfija (notación polaca inversa)
public class ExpressionEvaluator {

    // Método estático que evalúa una expresión postfija
    // Parámetro: postfix - expresión en notación postfija (ej: "3 4 5 * +")
    // Retorna: resultado numérico de la expresión (ej: 23.0 para el ejemplo anterior)
    public static double evaluatePostfix(String postfix) {
        // Crea una pila para almacenar operandos (números) durante la evaluación
        Stack<Double> stack = new Stack<>();
        
        // Divide la expresión en tokens (números y operadores) usando espacios como separadores
        for (String token : postfix.split(" ")) {
            // Verifica si el token es un número (uno o más dígitos)
            if (token.matches("\\d+")) {
                // Convierte el token a double y lo agrega a la pila
                stack.push(Double.parseDouble(token));
            } else {
                // Si es un operador, saca los dos últimos operandos
                // Nota: el orden es importante (primero b, luego a)
                double b = stack.pop();  // Segundo operando
                double a = stack.pop();  // Primer operando
                
                // Aplica la operación y guarda el resultado en la pila
                stack.push(applyOperator(a, b, token.charAt(0)));
            }
        }
        // Al final, el resultado final es el único elemento restante en la pila
        return stack.pop();
    }

    // Método auxiliar privado que aplica una operación matemática a dos operandos
    // Parámetros:
    //   a - primer operando (número a la izquierda del operador)
    //   b - segundo operando (número a la derecha del operador)
    //   op - operador matemático (+, -, *, /)
    // Retorna: resultado de la operación
    private static double applyOperator(double a, double b, char op) {
        // Usa una expresión switch (característica de Java 14+) para seleccionar la operación
        return switch (op) {
            case '+' -> a + b;  // Suma
            case '-' -> a - b;  // Resta
            case '*' -> a * b;  // Multiplicación
            case '/' -> a / b;  // División
            default -> 0;  // Valor por defecto (no debería ocurrir con expresiones válidas)
        };
    }
}