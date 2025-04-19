package ui;

import models.ExpressionResult;
import services.ExpressionConverter;
import services.ExpressionEvaluator;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class CalculatorUI extends JFrame {

    private JTextField inputField;
    private JTextArea outputArea;
    private JTextArea historyArea;

    public CalculatorUI() {
        setTitle("Evaluador de Expresiones");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        ImageIcon icono = new ImageIcon("Matematica/src/resources/icon.png"); 
        setIconImage(icono.getImage());
    }

    private void initComponents() {
        inputField = new JTextField();
        outputArea = new JTextArea(15, 30);
        outputArea.setEditable(false);
        historyArea = new JTextArea(15, 30);
        historyArea.setEditable(false);

        JScrollPane outputScroll = new JScrollPane(outputArea);
        JScrollPane historyScroll = new JScrollPane(historyArea);

        JButton evaluateButton = new JButton("Evaluar");
        JButton clearButton = new JButton("Limpiar");
        JButton randomButton = new JButton("Generar Aleatoria");

        evaluateButton.addActionListener(e -> evaluateExpression());
        clearButton.addActionListener(e -> clearFields());
        randomButton.addActionListener(e -> generateRandomExpression());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(new JLabel("Ingresa expresión infija:"), BorderLayout.NORTH);
        topPanel.add(inputField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(evaluateButton);
        buttonPanel.add(randomButton);
        buttonPanel.add(clearButton);

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(outputScroll);
        centerPanel.add(historyScroll);

        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.SOUTH);
    }

    private void evaluateExpression() {
        String input = inputField.getText().replaceAll("\\s+", "");
        if (!ExpressionConverter.isBalanced(input)) {
            outputArea.setText("❌ Expresión no balanceada.");
            return;
        }

        try {
            String postfix = ExpressionConverter.toPostfix(input);
            double result = ExpressionEvaluator.evaluatePostfix(postfix);
            ExpressionResult res = new ExpressionResult(postfix, result);

            outputArea.setText("✅ Expresión Balanceada\n");
            outputArea.append("Postfija: " + res.getPostfix() + "\n");
            outputArea.append("Resultado: " + res.getResult());

            // Agregar al historial
            historyArea.append("Infix: " + input + "\n");
            historyArea.append("Postfix: " + res.getPostfix() + "\n");
            historyArea.append("Resultado: " + res.getResult() + "\n");
            historyArea.append("-------------------------------\n");

        } catch (Exception ex) {
            outputArea.setText("❌ Error al evaluar expresión.");
        }
    }

    private void clearFields() {
        inputField.setText("");
        outputArea.setText("");
    }

    private void generateRandomExpression() {
        String[] operators = {"+", "-", "*", "/"};
        Random rand = new Random();
        int length = rand.nextInt(3) + 3;  // Número aleatorio de operadores (3 a 5 operadores)
    
        StringBuilder expression = new StringBuilder();
    
        // Genera números y operadores
        for (int i = 0; i < length; i++) {
            int num = rand.nextInt(9) + 1;  // Genera números del 1 al 9
            expression.append(num);  // Añade el número
    
            if (i < length - 1) {  // Añade operador, excepto al final
                expression.append(operators[rand.nextInt(operators.length)]);
            }
        }
    
        // Asegurarse de que la expresión tenga al menos 3 elementos (números y operadores)
        String finalExpression = expression.toString();
        
        // Si la expresión generada es válida, se muestra
        if (finalExpression.length() > 2) {
            inputField.setText(finalExpression);  // Muestra la expresión generada en el campo de texto
        } else {
            generateRandomExpression();  // Si la expresión generada es demasiado simple, vuelve a generar
        }
    }
    
}
