// Define que esta clase pertenece al paquete 'services'
package services;

// Importa la clase Stack de Java Collections Framework
import java.util.Stack;

// Clase pública que contiene métodos para conversión y validación de expresiones matemáticas
public class ExpressionConverter {

    // Método estático que verifica si una expresión tiene paréntesis/llaves balanceados
    // Parámetro: expression - cadena con la expresión a evaluar
    // Retorna: true si los delimitadores están balanceados, false en caso contrario
    public static boolean isBalanced(String expression) {
        // Crea una pila para rastrear los delimitadores de apertura
        Stack<Character> stack = new Stack<>();
        
        // Itera sobre cada caracter de la expresión
        for (char ch : expression.toCharArray()) {
            // Si es un delimitador de apertura, lo agrega a la pila
            if (ch == '(' || ch == '[') {
                stack.push(ch);
            } 
            // Si es un delimitador de cierre
            else if (ch == ')' || ch == ']') {
                // Si la pila está vacía, no hay delimitador de apertura correspondiente
                if (stack.isEmpty()) return false;
                
                // Obtiene el último delimitador de apertura
                char top = stack.pop();
                
                // Verifica que coincidan los tipos de delimitadores
                if ((ch == ')' && top != '(') || (ch == ']' && top != '[')) {
                    return false;
                }
            }
        }
        // Si la pila está vacía, todos los delimitadores estaban balanceados
        return stack.isEmpty();
    }

    // Método estático que convierte una expresión infija a postfija (notación polaca inversa)
    // Parámetro: infix - expresión en notación infija (ej: "3 + 4 * 5")
    // Retorna: la expresión en notación postfija (ej: "3 4 5 * +")
    public static String toPostfix(String infix) {
        // StringBuilder para construir el resultado
        StringBuilder output = new StringBuilder();
        // Pila para manejar los operadores
        Stack<Character> stack = new Stack<>();

        // Procesa cada caracter de la expresión infija
        for (char ch : infix.toCharArray()) {
            // Si es un dígito, lo agrega directamente al output
            if (Character.isDigit(ch)) {
                output.append(ch).append(" ");
            } 
            // Si es un delimitador de apertura, lo pone en la pila
            else if (ch == '(' || ch == '[') {
                stack.push(ch);
            } 
            // Si es un delimitador de cierre
            else if (ch == ')' || ch == ']') {
                // Saca todos los operadores hasta encontrar el delimitador de apertura
                while (!stack.isEmpty() && stack.peek() != '(' && stack.peek() != '[') {
                    output.append(stack.pop()).append(" ");
                }
                stack.pop(); // Elimina el delimitador de apertura de la pila
            } 
            // Si es un operador
            else if (isOperator(ch)) {
                // Saca operadores con mayor o igual precedencia
                while (!stack.isEmpty() && precedence(ch) <= precedence(stack.peek())) {
                    output.append(stack.pop()).append(" ");
                }
                // Agrega el operador actual a la pila
                stack.push(ch);
            }
        }

        // Saca todos los operadores restantes de la pila
        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(" ");
        }

        // Retorna el resultado eliminando espacios extras al final
        return output.toString().trim();
    }

    // Método auxiliar que determina si un caracter es un operador
    // Parámetro: ch - caracter a evaluar
    // Retorna: true si es +, -, * o /; false en caso contrario
    private static boolean isOperator(char ch) {
        return ch == '+' || ch == '-' || ch == '*' || ch == '/';
    }

    // Método auxiliar que determina la precedencia de un operador
    // Parámetro: ch - operador a evaluar
    // Retorna: 1 para + y - (precedencia baja), 2 para * y / (precedencia alta)
    private static int precedence(char ch) {
        return (ch == '+' || ch == '-') ? 1 : 2;
    }
}