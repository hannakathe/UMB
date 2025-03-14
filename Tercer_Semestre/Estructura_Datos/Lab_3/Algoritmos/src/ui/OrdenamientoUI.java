package ui; // Define el paquete donde se encuentra la clase

import service.*; // Importa todas las clases de ordenamiento desde el paquete service

import javax.swing.*; // Importa librerías para la interfaz gráfica
import java.awt.*; // Importa clases de diseño gráfico
import java.util.HashSet; // Importa HashSet para almacenar valores únicos
import java.util.Random; // Importa Random para generar números aleatorios
import java.util.Set; // Importa Set para trabajar con conjuntos

public class OrdenamientoUI extends JFrame { // Clase que extiende JFrame para crear la ventana de la aplicación
    private JComboBox<String> algoritmoCombo; // Menú desplegable para elegir el algoritmo de ordenamiento
    private JCheckBox ascendenteCheck; // Casilla de verificación para seleccionar el orden ascendente o descendente
    private JTextField cantidadField; // Campo de entrada para ingresar la cantidad de números a generar
    private JTextArea entradaArea; // Área de texto para mostrar los números ingresados o generados
    private JTextArea salidaArea; // Área de texto para mostrar los números ordenados
    private JLabel tiempoLabel; // Etiqueta para mostrar el tiempo de ejecución del algoritmo
    private JButton ordenarButton, generarButton, limpiarButton; // Botones para ejecutar acciones

    public OrdenamientoUI() { // Constructor de la interfaz gráfica
        setTitle("Ordenamiento de Números"); // Título de la ventana
        setSize(600, 500); // Dimensiones de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Acción al cerrar la ventana
        setLayout(new BorderLayout()); // Establece el diseño de la ventana

        // Establece el ícono de la aplicación
        ImageIcon icono = new ImageIcon("Algoritmos/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Panel superior con controles de entrada
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

        add(topPanel, BorderLayout.NORTH); // Agrega el panel superior a la ventana

        // Panel central con áreas de texto
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        entradaArea = new JTextArea(5, 40);
        salidaArea = new JTextArea(5, 40);
        entradaArea.setBorder(BorderFactory.createTitledBorder("Ingrese Números (Separados por espacio o coma)"));
        salidaArea.setBorder(BorderFactory.createTitledBorder("Números Ordenados"));
        centerPanel.add(new JScrollPane(entradaArea));
        centerPanel.add(new JScrollPane(salidaArea));

        add(centerPanel, BorderLayout.CENTER); // Agrega el panel central a la ventana

        // Panel inferior con botones y tiempo de ejecución
        JPanel bottomPanel = new JPanel(new FlowLayout());
        ordenarButton = new JButton("Ordenar");
        limpiarButton = new JButton("Limpiar");
        tiempoLabel = new JLabel("Tiempo: ---");

        bottomPanel.add(ordenarButton);
        bottomPanel.add(limpiarButton);
        bottomPanel.add(tiempoLabel);

        add(bottomPanel, BorderLayout.SOUTH); // Agrega el panel inferior a la ventana

        // Eventos de los botones
        generarButton.addActionListener(e -> generarNumeros());
        ordenarButton.addActionListener(e -> ordenarNumeros());
        limpiarButton.addActionListener(e -> limpiarCampos());
    }

    /**
     * Genera una cantidad de números aleatorios únicos dentro de un rango.
     */
    private void generarNumeros() {
        try {
            int cantidad = Integer.parseInt(cantidadField.getText());
            if (cantidad <= 0) throw new NumberFormatException("Debe ser mayor a 0");
            if (cantidad > 1_500_000) throw new NumberFormatException("Máximo permitido: 1,500,000");

            Random rand = new Random();
            Set<Integer> numerosUnicos = new HashSet<>();

            // Genera números aleatorios únicos
            while (numerosUnicos.size() < cantidad) {
                numerosUnicos.add(rand.nextInt(1_500_001)); // Números entre 0 y 1,500,000
            }

            // Convierte los números en un String con formato
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (int num : numerosUnicos) {
                sb.append(num).append(" ");
                count++;
                if (count % 10 == 0) sb.append("\n"); // Formatea cada 10 números por línea
            }

            entradaArea.setText(sb.toString().trim()); // Muestra los números en el área de entrada
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida entre 1 y 1,500,000", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Ordena los números ingresados utilizando el algoritmo seleccionado.
     */
    private void ordenarNumeros() {
        try {
            String[] numerosTexto = entradaArea.getText().split("[,\\s]+"); // Separa los números por espacios o comas
            int[] numeros = new int[numerosTexto.length];

            // Convierte los valores de texto en enteros
            for (int i = 0; i < numerosTexto.length; i++) {
                numeros[i] = Integer.parseInt(numerosTexto[i].trim());
            }

            boolean ascendente = ascendenteCheck.isSelected();
            long[] tiempo;

            // Selecciona el algoritmo de ordenamiento
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

            // Formatea el resultado ordenado
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < numeros.length; i++) {
                sb.append(numeros[i]).append(" ");
                if ((i + 1) % 10 == 0) sb.append("\n");
            }
            salidaArea.setText(sb.toString().trim());

            // Muestra el tiempo de ejecución
            tiempoLabel.setText("Tiempo: " + tiempo[0] + " ms | " + tiempo[1] + " ns");
            JOptionPane.showMessageDialog(this, "Ordenamiento completado en " + tiempo[0] + " ms", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ingrese solo números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en el procesamiento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Limpia los campos de entrada, salida y el tiempo de ejecución.
     */
    private void limpiarCampos() {
        cantidadField.setText("");
        entradaArea.setText("");
        salidaArea.setText("");
        tiempoLabel.setText("Tiempo: ---");
    }
}
