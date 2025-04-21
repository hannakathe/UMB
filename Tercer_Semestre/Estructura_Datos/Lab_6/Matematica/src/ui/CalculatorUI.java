// Declara que esta clase pertenece al paquete 'ui' (interfaz de usuario)
package ui;

// Importa las clases necesarias
import models.ExpressionResult;
import services.ExpressionConverter;
import services.ExpressionEvaluator;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

// Clase principal que representa la interfaz gráfica de la calculadora
public class CalculatorUI extends JFrame {

    // Componentes de la interfaz
    private JTextField inputField;      // Campo para ingresar expresiones
    private JTextArea outputArea;       // Área para mostrar resultados
    private JTextArea historyArea;      // Área para historial de operaciones

    // Constructor de la ventana principal
    public CalculatorUI() {
        setTitle("Evaluador de Expresiones");  // Título de la ventana
        setSize(600, 400);                     // Tamaño de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra la aplicación al salir
        setLocationRelativeTo(null);           // Centra la ventana en la pantalla
        initComponents();                      // Inicializa los componentes
        ImageIcon icono = new ImageIcon("Matematica/src/resources/icon.png"); 
        setIconImage(icono.getImage());        // Establece el icono de la aplicación
    }

    // Método para inicializar los componentes de la interfaz
    private void initComponents() {
        // Creación de componentes
        inputField = new JTextField();         // Campo de entrada de texto
        outputArea = new JTextArea(15, 30);    // Área de salida (15 filas, 30 columnas)
        outputArea.setEditable(false);         // Hace el área de salida no editable
        historyArea = new JTextArea(15, 30);   // Área de historial
        historyArea.setEditable(false);         // Hace el historial no editable

        // Scroll panes para las áreas de texto
        JScrollPane outputScroll = new JScrollPane(outputArea);
        JScrollPane historyScroll = new JScrollPane(historyArea);

        // Botones de la interfaz
        JButton evaluateButton = new JButton("Evaluar");
        JButton clearButton = new JButton("Limpiar");
        JButton randomButton = new JButton("Generar Aleatoria");

        // Asignación de acciones a los botones
        evaluateButton.addActionListener(e -> evaluateExpression());
        clearButton.addActionListener(e -> clearFields());
        randomButton.addActionListener(e -> generateRandomExpression());

        // Panel superior (entrada de datos)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Ingresa expresión infija:"), BorderLayout.NORTH);
        topPanel.add(inputField, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(evaluateButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(clearButton);

        // Panel central (resultados e historial)
        JPanel centerPanel = new JPanel(new GridLayout(1, 2)); // Diseño de 1 fila, 2 columnas
        centerPanel.add(outputScroll);    // Área de resultados
        centerPanel.add(historyScroll);   // Área de historial

        // Configuración del layout principal
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);     // Panel superior
        add(buttonPanel, BorderLayout.CENTER); // Panel central con botones
        add(centerPanel, BorderLayout.SOUTH);  // Panel inferior con resultados
    }

    // Método para evaluar la expresión ingresada
    private void evaluateExpression() {
        // Obtiene y limpia la expresión del campo de entrada
        String input = inputField.getText().replaceAll("\\s+", "");
        
        // Verifica si los paréntesis están balanceados
        if (!ExpressionConverter.isBalanced(input)) {
            outputArea.setText("❌ Expresión no balanceada.");
            return;
        }

        try {
            // Convierte a notación postfija
            String postfix = ExpressionConverter.toPostfix(input);
            // Evalúa la expresión postfija
            double result = ExpressionEvaluator.evaluatePostfix(postfix);
            // Crea objeto con los resultados
            ExpressionResult res = new ExpressionResult(postfix, result);

            // Muestra resultados en el área de salida
            outputArea.setText("✅ Expresión Balanceada\n");
            outputArea.append("Postfija: " + res.getPostfix() + "\n");
            outputArea.append("Resultado: " + res.getResult());

            // Agrega al historial
            historyArea.append("Infix: " + input + "\n");
            historyArea.append("Postfix: " + res.getPostfix() + "\n");
            historyArea.append("Resultado: " + res.getResult() + "\n");
            historyArea.append("-------------------------------\n");

        } catch (Exception ex) {
            outputArea.setText("❌ Error al evaluar expresión.");
        }
    }

    // Método para limpiar los campos
    private void clearFields() {
        inputField.setText("");   // Limpia el campo de entrada
        outputArea.setText("");   // Limpia el área de resultados
    }

    // Método para generar una expresión aleatoria
    private void generateRandomExpression() {
        String[] operators = {"+", "-", "*", "/"};  // Operadores disponibles
        Random rand = new Random();                // Generador de números aleatorios
        
        // Longitud aleatoria de la expresión (3 a 5 operadores)
        int length = rand.nextInt(3) + 3;
    
        StringBuilder expression = new StringBuilder();
    
        // Construye la expresión
        for (int i = 0; i < length; i++) {
            // Genera un número aleatorio entre 1 y 9
            int num = rand.nextInt(9) + 1;
            expression.append(num);  // Añade el número
    
            // Añade operador (excepto después del último número)
            if (i < length - 1) {
                expression.append(operators[rand.nextInt(operators.length)]);
            }
        }
    
        String finalExpression = expression.toString();
        
        // Valida la expresión generada
        if (finalExpression.length() > 2) {
            inputField.setText(finalExpression);  // Muestra la expresión
        } else {
            generateRandomExpression();  // Vuelve a generar si es muy corta
        }
    }
}