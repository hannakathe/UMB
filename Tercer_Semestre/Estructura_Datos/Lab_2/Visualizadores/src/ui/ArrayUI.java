package ui;

import javax.swing.*;
import service.ArrayService;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.table.DefaultTableModel;

public class ArrayUI extends JFrame {
    // Instancia del servicio para manejar los arreglos
    private final ArrayService arrayService = new ArrayService();
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JScrollPane scrollPane;
    private final JTextField inputX, inputY, inputZ;

    public ArrayUI() {
        setTitle("Gestión de Arreglos"); // Título de la ventana
        setSize(900, 500); // Tamaño inicial de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierre de la aplicación al cerrar la ventana
        setLayout(new BorderLayout()); // Uso de un BorderLayout para la disposición de los componentes

        // Establece el icono de la aplicación
        ImageIcon icono = new ImageIcon("Visualizadores/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Panel de entrada con disposición de flujo
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel labelX = new JLabel("Dimensión X:");
        inputX = new JTextField(5);
        JLabel labelY = new JLabel("Dimensión Y:");
        inputY = new JTextField(5);
        JLabel labelZ = new JLabel("Dimensión Z:");
        inputZ = new JTextField(5);

        // Botones para seleccionar la dimensión del arreglo
        JButton btnUnidimensional = new JButton("Unidimensional");
        JButton btnBidimensional = new JButton("Bidimensional");
        JButton btnTridimensional = new JButton("Tridimensional");
        JButton btnClear = new JButton("Limpiar");

        // Agregar elementos al panel de entrada
        inputPanel.add(labelX);
        inputPanel.add(inputX);
        inputPanel.add(labelY);
        inputPanel.add(inputY);
        inputPanel.add(labelZ);
        inputPanel.add(inputZ);
        inputPanel.add(btnUnidimensional);
        inputPanel.add(btnBidimensional);
        inputPanel.add(btnTridimensional);
        inputPanel.add(btnClear);

        // Configuración de la tabla donde se mostrarán los datos
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(650, 350));

        // Agregar componentes a la ventana
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Eventos de botones
        btnUnidimensional.addActionListener(e -> handleUnidimensional());
        btnBidimensional.addActionListener(e -> handleBidimensional());
        btnTridimensional.addActionListener(e -> handleTridimensional());
        btnClear.addActionListener(e -> clearFields());

        // Ajuste del tamaño de la tabla cuando se redimensiona la ventana
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setPreferredSize(new Dimension(getWidth() - 50, getHeight() - 150));
                scrollPane.revalidate();
            }
        });
    }

    // Manejo de la generación de un arreglo unidimensional
    private void handleUnidimensional() {
        int x = Integer.parseInt(inputX.getText());
        boolean randomFill = confirmRandom();
        int[] array = arrayService.createUnidimensional(x, randomFill);

        tableModel.setColumnCount(2);
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"Índice X", "Valor"});

        for (int i = 0; i < array.length; i++) {
            tableModel.addRow(new Object[]{i, "(" + i + ") = " + array[i]});
        }
    }

    // Manejo de la generación de un arreglo bidimensional
    private void handleBidimensional() {
        int x = Integer.parseInt(inputX.getText());
        int y = Integer.parseInt(inputY.getText());
        boolean randomFill = confirmRandom();
        int[][] array = arrayService.createBidimensional(x, y, randomFill);

        tableModel.setColumnCount(y + 1);
        tableModel.setRowCount(0);

        // Definir encabezados de la tabla
        String[] headers = new String[y + 1];
        headers[0] = "X \\ Y";
        for (int j = 1; j <= y; j++) {
            headers[j] = String.valueOf(j - 1);
        }
        tableModel.setColumnIdentifiers(headers);

        // Llenar la tabla con los datos
        for (int i = 0; i < x; i++) {
            Object[] row = new Object[y + 1];
            row[0] = i;
            for (int j = 0; j < y; j++) {
                row[j + 1] = "(" + i + "," + j + ") = " + array[i][j];
            }
            tableModel.addRow(row);
        }
    }

    // Manejo de la generación de un arreglo tridimensional
    private void handleTridimensional() {
        int x = Integer.parseInt(inputX.getText());
        int y = Integer.parseInt(inputY.getText());
        int z = Integer.parseInt(inputZ.getText());
        boolean randomFill = confirmRandom();
        int[][][] array = arrayService.createTridimensional(x, y, z, randomFill);

        tableModel.setColumnCount(y + 1);
        tableModel.setRowCount(0);

        for (int k = 0; k < z; k++) {
            tableModel.addRow(new Object[]{"Capa Z = " + k});
            String[] headers = new String[y + 1];
            headers[0] = "X \\ Y";
            for (int j = 1; j <= y; j++) {
                headers[j] = String.valueOf(j - 1);
            }
            tableModel.setColumnIdentifiers(headers);

            for (int i = 0; i < x; i++) {
                Object[] row = new Object[y + 1];
                row[0] = i;
                for (int j = 0; j < y; j++) {
                    row[j + 1] = "(" + i + "," + j + "," + k + ") = " + array[i][j][k];
                }
                tableModel.addRow(row);
            }
            tableModel.addRow(new Object[]{" "}); // Espaciado entre capas
        }
    }

    // Limpia los campos de entrada y la tabla
    private void clearFields() {
        tableModel.setRowCount(0);
        inputX.setText("");
        inputY.setText("");
        inputZ.setText("");
    }

    // Muestra un cuadro de diálogo para confirmar si se llenará el arreglo con valores aleatorios
    private boolean confirmRandom() {
        return JOptionPane.showConfirmDialog(this, "¿Llenar con valores aleatorios?", "Opciones",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
