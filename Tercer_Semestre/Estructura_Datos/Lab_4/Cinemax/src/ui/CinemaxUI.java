package ui;

import services.CinemaxService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

// Clase principal que representa la interfaz gráfica para gestionar estadísticas de películas en Cinemax
public class CinemaxUI extends JFrame {
    // Componentes de la interfaz gráfica
    private JTextField peliculaField, espectadoresField;
    private JComboBox<Integer> salaCombo, semanaCombo;
    private JTable tabla;
    private DefaultTableModel tableModel;
    private CinemaxService service;
    private EstadisticasPanel estadisticasPanel;

    // Constructor de la clase
    public CinemaxUI() {
        setTitle("Cinemax - Estadísticas de Películas");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icono = new ImageIcon("Cinemax/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Inicialización del servicio y el panel de estadísticas
        service = new CinemaxService();
        estadisticasPanel = new EstadisticasPanel();

        // Panel de entrada de datos
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        inputPanel.add(new JLabel("Película:"));
        peliculaField = new JTextField();
        inputPanel.add(peliculaField);

        inputPanel.add(new JLabel("Semana:"));
        semanaCombo = new JComboBox<>();
        for (int i = 1; i <= 4; i++) semanaCombo.addItem(i);
        inputPanel.add(semanaCombo);

        inputPanel.add(new JLabel("Sala (1-7):"));
        salaCombo = new JComboBox<>();
        for (int i = 1; i <= 7; i++) salaCombo.addItem(i);
        inputPanel.add(salaCombo);

        inputPanel.add(new JLabel("Espectadores:"));
        espectadoresField = new JTextField();
        inputPanel.add(espectadoresField);

        // Botones para agregar registros y calcular estadísticas
        JButton agregarButton = new JButton("Agregar Registro");
        JButton calcularButton = new JButton("Calcular Estadísticas");
        inputPanel.add(agregarButton);
        inputPanel.add(calcularButton);

        // Modelo de la tabla para mostrar los registros
        tableModel = new DefaultTableModel(new String[]{"Semana", "Película", "Sala", "Espectadores"}, 0);
        tabla = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(tabla);

        // Panel de botones para limpiar registros y borrar estadísticas
        JPanel panelBotones = new JPanel();
        JButton limpiarButton = new JButton("Limpiar Registros");
        JButton borrarStatsButton = new JButton("Borrar Estadísticas");
        panelBotones.add(limpiarButton);
        panelBotones.add(borrarStatsButton);

        // Panel dividido para mostrar la tabla y las estadísticas
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableScrollPane, estadisticasPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(500);
        splitPane.setOneTouchExpandable(true);

        // Asignación de acciones a los botones
        agregarButton.addActionListener(this::agregarRegistro);
        calcularButton.addActionListener(e -> estadisticasPanel.agregarEstadistica(service.calcularEstadisticas((int) semanaCombo.getSelectedItem())));
        limpiarButton.addActionListener(e -> tableModel.setRowCount(0));
        borrarStatsButton.addActionListener(e -> {
            service.borrarEstadisticas();
            estadisticasPanel.limpiarPestanas();
        });

        // Configuración del layout y adición de componentes
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Método para agregar un registro de película
    private void agregarRegistro(ActionEvent e) {
        String pelicula = peliculaField.getText().trim().toUpperCase();
        int semana = (int) semanaCombo.getSelectedItem();
        int sala = (int) salaCombo.getSelectedItem();
        try {
            int espectadores = Integer.parseInt(espectadoresField.getText().trim());
            service.registrarVisitas(semana, sala, pelicula, espectadores);
            tableModel.addRow(new Object[]{semana, pelicula, sala, espectadores});
            JOptionPane.showMessageDialog(this, "Registro agregado.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido de espectadores.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}