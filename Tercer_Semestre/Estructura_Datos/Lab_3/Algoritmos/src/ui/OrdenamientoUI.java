package ui;

import service.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class OrdenamientoUI extends JFrame {
    private JComboBox<String> algoritmoCombo;
    private JCheckBox ascendenteCheck;
    private JTextField cantidadField;
    private JTextArea entradaArea;
    private JTextArea salidaArea;
    private JLabel tiempoLabel;
    private JButton ordenarButton, generarButton, limpiarButton;

    public OrdenamientoUI() {
        setTitle("Ordenamiento de Números");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon icono = new ImageIcon("src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Panel superior
        JPanel topPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        topPanel.add(new JLabel("Cantidad de números:"));
        cantidadField = new JTextField();
        topPanel.add(cantidadField);

        generarButton = new JButton("Generar Aleatorio");
        topPanel.add(generarButton);

        topPanel.add(new JLabel("Algoritmo de ordenamiento:"));
        algoritmoCombo = new JComboBox<>(new String[]{
                "Burbuja", "Inserción", "Selección", "ShellSort", "HeapSort", "QuickSort"
        });
        topPanel.add(algoritmoCombo);

        ascendenteCheck = new JCheckBox("Orden Ascendente", true);
        topPanel.add(ascendenteCheck);

        add(topPanel, BorderLayout.NORTH);

        // Panel central
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        entradaArea = new JTextArea(5, 40);
        salidaArea = new JTextArea(5, 40);
        entradaArea.setBorder(BorderFactory.createTitledBorder("Ingrese Números (Separados por espacio o coma)"));
        salidaArea.setBorder(BorderFactory.createTitledBorder("Números Ordenados"));
        centerPanel.add(new JScrollPane(entradaArea));
        centerPanel.add(new JScrollPane(salidaArea));

        add(centerPanel, BorderLayout.CENTER);

        // Panel inferior
        JPanel bottomPanel = new JPanel(new FlowLayout());
        ordenarButton = new JButton("Ordenar");
        limpiarButton = new JButton("Limpiar"); // Nuevo botón
        tiempoLabel = new JLabel("Tiempo: ---");

        bottomPanel.add(ordenarButton);
        bottomPanel.add(limpiarButton);
        bottomPanel.add(tiempoLabel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Eventos
        generarButton.addActionListener(e -> generarNumeros());
        ordenarButton.addActionListener(e -> ordenarNumeros());
        limpiarButton.addActionListener(e -> limpiarCampos()); // Evento para limpiar los campos
    }

    private void generarNumeros() {
    try {
        int cantidad = Integer.parseInt(cantidadField.getText());
        if (cantidad <= 0) throw new NumberFormatException("Debe ser mayor a 0");
        if (cantidad > 10_500_001) throw new NumberFormatException("Máximo permitido: 1,500,000");

        Random rand = new Random();
        Set<Integer> numerosUnicos = new HashSet<>();

        while (numerosUnicos.size() < cantidad) {
            numerosUnicos.add(rand.nextInt(10_500_001)); // Números entre 0 y 1,500,000
        }

        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int num : numerosUnicos) {
            sb.append(num).append(" ");
            count++;
            if (count % 10 == 0) sb.append("\n");
        }

        entradaArea.setText(sb.toString().trim());
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida entre 1 y 1,500,000", "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    

    private void ordenarNumeros() {
        try {
            String[] numerosTexto = entradaArea.getText().split("[,\\s]+"); // Permite espacios y comas como separadores
            int[] numeros = new int[numerosTexto.length];

            for (int i = 0; i < numerosTexto.length; i++) {
                numeros[i] = Integer.parseInt(numerosTexto[i].trim());
            }

            boolean ascendente = ascendenteCheck.isSelected();
            long[] tiempo;

            String algoritmo = (String) algoritmoCombo.getSelectedItem();
            switch (algoritmo) {
                case "Burbuja":
                    tiempo = Burbuja.ordenar(numeros, ascendente);
                    break;
                case "Inserción":
                    tiempo = Insercion.ordenar(numeros, ascendente);
                    break;
                case "Selección":
                    tiempo = Seleccion.ordenar(numeros, ascendente);
                    break;
                case "ShellSort":
                    tiempo = ShellSort.ordenar(numeros, ascendente);
                    break;
                case "HeapSort":
                    tiempo = HeapSort.ordenar(numeros, ascendente);
                    break;
                case "QuickSort":
                    tiempo = QuickSort.ordenar(numeros, ascendente);
                    break;
                default:
                    throw new IllegalStateException("Algoritmo no válido");
            }

            // Mostrar resultado
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numeros.length; i++) {
                sb.append(numeros[i]).append(" ");
                if ((i + 1) % 10 == 0) sb.append("\n");
            }
            salidaArea.setText(sb.toString().trim());

            // Mostrar tiempos
            tiempoLabel.setText("Tiempo: " + tiempo[0] + " ms | " + tiempo[1] + " ns");
            JOptionPane.showMessageDialog(this, "Ordenamiento completado en " + tiempo[0] + " ms", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese solo números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el procesamiento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        cantidadField.setText("");
        entradaArea.setText("");
        salidaArea.setText("");
        tiempoLabel.setText("Tiempo: ---");
    }
}
