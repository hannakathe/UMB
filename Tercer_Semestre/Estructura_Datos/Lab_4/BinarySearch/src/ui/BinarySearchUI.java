package ui;

import models.BinarySearchModel;
import services.BinarySearchService;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

// Clase principal que representa la interfaz gráfica para realizar búsquedas binarias
public class BinarySearchUI extends JFrame {
    // Componentes de la interfaz gráfica
    private JTextField cantidadField;
    private JTextArea entradaArea, salidaArea, busquedaArea;
    private JButton generarButton, buscarButton, limpiarButton;
    private JLabel tiempoLabel;

    // Modelo para almacenar los números ordenados
    private BinarySearchModel binarySearchModel;

    // Constructor de la clase
    public BinarySearchUI() {
        setTitle("Búsqueda Binaria");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon icono = new ImageIcon("BinarySearch/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Panel superior para ingresar la cantidad de números y generar la lista
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        topPanel.add(new JLabel("Cantidad de números:"));
        cantidadField = new JTextField();
        topPanel.add(cantidadField);
        generarButton = new JButton("Generar y Ordenar");
        topPanel.add(generarButton);
        add(topPanel, BorderLayout.NORTH);

        // Panel central para mostrar los números generados, ingresar números a buscar y mostrar resultados
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

        // Panel inferior con botones para buscar y limpiar
        JPanel bottomPanel = new JPanel(new FlowLayout());
        buscarButton = new JButton("Buscar");
        limpiarButton = new JButton("Limpiar");
        //tiempoLabel = new JLabel("Tiempo: ---");

        bottomPanel.add(buscarButton);
        bottomPanel.add(limpiarButton);
        //bottomPanel.add(tiempoLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        // Asignar acciones a los botones
        generarButton.addActionListener(e -> generarNumerosOrdenados());
        buscarButton.addActionListener(e -> buscarNumeros());
        limpiarButton.addActionListener(e -> limpiarCampos());
    }

    // Método para generar números aleatorios, ordenarlos y mostrarlos en la interfaz
    private void generarNumerosOrdenados() {
        try {
            int cantidad = Integer.parseInt(cantidadField.getText());
            if (cantidad <= 0) throw new NumberFormatException("Debe ser mayor a 0");

            // Generar números únicos aleatorios
            Set<Integer> numerosUnicos = new HashSet<>();
            Random rand = new Random();
            while (numerosUnicos.size() < cantidad) {
                numerosUnicos.add(rand.nextInt(1_500_001));
            }

            // Ordenar los números generados
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

    // Método para buscar números en la lista ordenada
    private void buscarNumeros() {
        try {
            if (binarySearchModel == null) {
                JOptionPane.showMessageDialog(this, "Primero genere los números.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Obtener los números a buscar desde el área de texto
            String[] numerosTexto = busquedaArea.getText().split("[,\\s]+");
            List<Integer> numerosABuscar = Arrays.stream(numerosTexto).map(String::trim).map(Integer::parseInt).collect(Collectors.toList());

            // Realizar la búsqueda binaria y obtener resultados
            HashMap<Integer, Long[]> resultados = BinarySearchService.searchNumbers(binarySearchModel.getSortedNumbers(), numerosABuscar);

            // Mostrar los resultados en el área de salida
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

    // Método para limpiar todos los campos de la interfaz
    private void limpiarCampos() {
        cantidadField.setText("");
        entradaArea.setText("");
        busquedaArea.setText("");
        salidaArea.setText("");
        tiempoLabel.setText("Tiempo: ---");
    }
}