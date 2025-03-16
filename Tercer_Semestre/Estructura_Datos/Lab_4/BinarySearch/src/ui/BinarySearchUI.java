package ui;

import models.BinarySearchModel;
import services.BinarySearchService;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BinarySearchUI extends JFrame {
    private JTextField cantidadField;
    private JTextArea entradaArea, salidaArea, busquedaArea;
    private JButton generarButton, buscarButton, limpiarButton;
    private JLabel tiempoLabel;

    private BinarySearchModel binarySearchModel;

    public BinarySearchUI() {
        setTitle("Búsqueda Binaria");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        topPanel.add(new JLabel("Cantidad de números:"));
        cantidadField = new JTextField();
        topPanel.add(cantidadField);
        generarButton = new JButton("Generar y Ordenar");
        topPanel.add(generarButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        entradaArea = new JTextArea(5, 40);
        entradaArea.setBorder(BorderFactory.createTitledBorder("Números Generados (Ordenados)"));
        salidaArea = new JTextArea(5, 40);
        salidaArea.setBorder(BorderFactory.createTitledBorder("Resultados de la Búsqueda"));
        busquedaArea = new JTextArea(2, 40);
        busquedaArea.setBorder(BorderFactory.createTitledBorder("Ingrese Números a Buscar (Separados por coma)"));
        centerPanel.add(new JScrollPane(entradaArea));
        centerPanel.add(new JScrollPane(busquedaArea));
        centerPanel.add(new JScrollPane(salidaArea));

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        buscarButton = new JButton("Buscar");
        limpiarButton = new JButton("Limpiar");
        tiempoLabel = new JLabel("Tiempo: ---");

        bottomPanel.add(buscarButton);
        bottomPanel.add(limpiarButton);
        bottomPanel.add(tiempoLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        generarButton.addActionListener(e -> generarNumerosOrdenados());
        buscarButton.addActionListener(e -> buscarNumeros());
        limpiarButton.addActionListener(e -> limpiarCampos());
    }

    private void generarNumerosOrdenados() {
        try {
            int cantidad = Integer.parseInt(cantidadField.getText());
            if (cantidad <= 0) throw new NumberFormatException("Debe ser mayor a 0");

            Set<Integer> numerosUnicos = new HashSet<>();
            Random rand = new Random();
            while (numerosUnicos.size() < cantidad) {
                numerosUnicos.add(rand.nextInt(1_500_001));
            }

            List<Integer> sortedList = new ArrayList<>(numerosUnicos);
            Collections.sort(sortedList);
            binarySearchModel = new BinarySearchModel(sortedList.stream().mapToInt(i -> i).toArray());

            // Formatear la salida para mostrar 20 números por línea
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (int num : sortedList) {
                sb.append(num).append(" ");
                count++;
                if (count % 20 == 0) {
                    sb.append("\n");
                }
            }
            entradaArea.setText(sb.toString().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarNumeros() {
        try {
            if (binarySearchModel == null) {
                JOptionPane.showMessageDialog(this, "Primero genere los números.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] numerosTexto = busquedaArea.getText().split("[,\\s]+");
            List<Integer> numerosABuscar = Arrays.stream(numerosTexto).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());

            HashMap<Integer, Long[]> resultados = BinarySearchService.searchNumbers(binarySearchModel.getSortedNumbers(), numerosABuscar);

            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, Long[]> entry : resultados.entrySet()) {
                sb.append("Número ").append(entry.getKey())
                        .append(": Índice ").append(entry.getValue()[0])
                        .append(", Tiempo: ").append(entry.getValue()[1]).append(" ms / ").append(entry.getValue()[2]).append(" ns\n");
            }

            salidaArea.setText(sb.toString());
            JOptionPane.showMessageDialog(this, "Búsqueda completada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos separados por comas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        cantidadField.setText("");
        entradaArea.setText("");
        busquedaArea.setText("");
        salidaArea.setText("");
        tiempoLabel.setText("Tiempo: ---");
    }
}
