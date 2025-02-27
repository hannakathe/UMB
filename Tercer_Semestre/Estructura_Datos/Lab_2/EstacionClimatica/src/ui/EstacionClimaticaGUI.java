package ui; // Define que esta clase pertenece al paquete "ui" (User Interface o Interfaz de Usuario).

import model.Temperatura; // Importa la clase Temperatura del paquete "model".
import service.EstacionClimatica; // Importa la clase EstacionClimatica del paquete "service".

import javax.swing.*; // Importa las clases necesarias para la interfaz gráfica con Swing.
import javax.swing.table.DefaultTableModel; // Importa el modelo de tabla para JTable.
import java.awt.*; // Importa clases para la gestión de diseño de la interfaz.
import java.awt.event.ActionEvent; // Importa la clase para manejar eventos de botones.
import java.util.List; // Importa la lista para manejar colecciones de datos.


public class EstacionClimaticaGUI extends JFrame { // La clase extiende JFrame, lo que significa que es una ventana gráfica.
    
    // Se declara una instancia de la estación climática para gestionar los datos.
    private EstacionClimatica estacion = new EstacionClimatica();
    
    // Componentes de la interfaz gráfica.
    private JTable table; // Tabla para mostrar los datos.
    private DefaultTableModel tableModel; // Modelo de la tabla.
    private JTextField diaField, maxField, minField, rangoMinField, rangoMaxField; // Campos de texto para ingresar datos.
    private JComboBox<String> diaComboBox; // ComboBox para seleccionar días. Menu desplegable. 

    public EstacionClimaticaGUI() { // Constructor de la interfaz gráfica.
        setTitle("Estación Climática"); // Título de la ventana.
        setSize(750, 500); // Dimensiones de la ventana.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la aplicación al cerrar la ventana.
        setLayout(new BorderLayout()); // Usa un diseño de "BorderLayout" para organizar los componentes. 
        ImageIcon icono = new ImageIcon("EstacionClimatica/src/resources/icon.png"); 
        setIconImage(icono.getImage());
        

        // Panel de entrada de datos.
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5)); // Panel con una cuadrícula de 2 filas y 4 columnas.
        diaField = new JTextField(); // Campo de texto para el día.
        maxField = new JTextField(); // Campo de texto para la temperatura máxima.
        minField = new JTextField(); // Campo de texto para la temperatura mínima.

        // Convierte el texto a mayúsculas automáticamente.
        diaField.setDocument(new javax.swing.text.PlainDocument() {
            public void insertString(int offs, String str, javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                super.insertString(offs, str.toUpperCase(), a); // Convierte cada entrada en mayúsculas.
            }
        });

        // Se agregan etiquetas y campos de texto al panel.
        inputPanel.add(new JLabel("Día:"));
        inputPanel.add(diaField);
        inputPanel.add(new JLabel("Máx:"));
        inputPanel.add(maxField);
        inputPanel.add(new JLabel("Mín:"));
        inputPanel.add(minField);

        // Botón para agregar una temperatura.
        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(this::agregarTemperatura); // Asocia el evento al botón.
        inputPanel.add(addButton);

        // Botón para mostrar todos los datos.
        JButton showAllButton = new JButton("Mostrar Todo");
        showAllButton.addActionListener(this::mostrarTodosLosDatos);
        inputPanel.add(showAllButton);

        add(inputPanel, BorderLayout.NORTH); // Agrega el panel en la parte superior de la ventana.

        // Configuración de la tabla.
        tableModel = new DefaultTableModel(new String[]{"Día", "Máx", "Mín"}, 0); // Define las columnas de la tabla.
        table = new JTable(tableModel); // Crea la tabla con el modelo definido.
        add(new JScrollPane(table), BorderLayout.CENTER); // Agrega la tabla con barra de desplazamiento al centro.

        // Panel para filtros y cálculo de medias.
        JPanel filterPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // Panel con tres secciones.

        // Panel para el filtro por rango de temperatura.
        JPanel rangoPanel = new JPanel(new GridLayout(3, 2)); // Panel con una cuadrícula de 3 filas y 2 columnas.
        rangoMinField = new JTextField(); // Campo para el mínimo del rango.
        rangoMaxField = new JTextField(); // Campo para el máximo del rango.
        JButton filtroRangoButton = new JButton("Filtrar Rango");
        filtroRangoButton.addActionListener(this::filtrarPorRango); // Asigna el evento de filtrado.

        rangoPanel.setBorder(BorderFactory.createTitledBorder("Filtrar por Rango")); // Agrega un borde con título.
        rangoPanel.add(new JLabel("Mín:"));
        rangoPanel.add(rangoMinField);
        rangoPanel.add(new JLabel("Máx:"));
        rangoPanel.add(rangoMaxField);
        rangoPanel.add(filtroRangoButton);
        filterPanel.add(rangoPanel); // Se añade el panel al contenedor de filtros.

        // Panel para filtrar temperaturas bajo 0°C.
        JPanel bajoCeroPanel = new JPanel(new BorderLayout());
        JButton filtroBajoCeroButton = new JButton("Filtrar < 0°C");
        filtroBajoCeroButton.addActionListener(this::filtrarPorMinimaBajoCero);
        bajoCeroPanel.setBorder(BorderFactory.createTitledBorder("Temperaturas < 0°C"));
        bajoCeroPanel.add(filtroBajoCeroButton, BorderLayout.CENTER);
        filterPanel.add(bajoCeroPanel); // Se añade el panel al contenedor de filtros.

        // Panel para el cálculo de la media de temperaturas.
        JPanel mediaPanel = new JPanel(new BorderLayout());
        diaComboBox = new JComboBox<>(); // ComboBox para seleccionar un día.
        JButton calcularMediaButton = new JButton("Calcular Media");
        calcularMediaButton.addActionListener(this::calcularMediaPorDia);
        mediaPanel.setBorder(BorderFactory.createTitledBorder("Calcular Media"));
        mediaPanel.add(diaComboBox, BorderLayout.NORTH);
        mediaPanel.add(calcularMediaButton, BorderLayout.SOUTH);
        filterPanel.add(mediaPanel); // Se añade el panel al contenedor de filtros.

        add(filterPanel, BorderLayout.SOUTH); // Se coloca el panel de filtros en la parte inferior.
        setVisible(true); // Hace visible la ventana.
    }

    private void agregarTemperatura(ActionEvent e) {
        try {
            String dia = diaField.getText().trim();
            double max = Double.parseDouble(maxField.getText().trim());
            double min = Double.parseDouble(minField.getText().trim());

            if (dia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese un nombre de día válido.");
                return;
            }


            // Métodos de interacción con los datos.


            // Lógica para agregar temperatura ingresada.
            estacion.agregarTemperatura(dia, max, min); // Agrega los datos a la estación climática.
            tableModel.addRow(new Object[]{dia, max, min}); // Agrega la nueva fila a la tabla.

            // Agrega el día al ComboBox si aún no está en la lista.
            if (((DefaultComboBoxModel<String>) diaComboBox.getModel()).getIndexOf(dia) == -1) {
                diaComboBox.addItem(dia);
            }

            // Limpia los campos de temperatura.
            maxField.setText("");
            minField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.");
        }
    }

    private void filtrarPorRango(ActionEvent e) {
        try {
            double min = Double.parseDouble(rangoMinField.getText().trim());
            double max = Double.parseDouble(rangoMaxField.getText().trim());
            actualizarTabla(estacion.filtrarPorRango(min, max)); // Filtra y actualiza la tabla. Filtra los datos según el rango ingresado.
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese valores numéricos válidos.");
        }
    }

    private void filtrarPorMinimaBajoCero(ActionEvent e) { // Filtra y muestra temperaturas bajo 0°C.
        actualizarTabla(estacion.filtrarPorMinimaBajoCero());
    }

    private void calcularMediaPorDia(ActionEvent e) {
        String diaSeleccionado = (String) diaComboBox.getSelectedItem();
        if (diaSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un día para calcular la media.");
            return;
        }

        double media = estacion.calcularMediaPorDia(diaSeleccionado); // Calcula la media de temperaturas para un día seleccionado.
        JOptionPane.showMessageDialog(this, "Media de " + diaSeleccionado + ": " + media);
    }

    private void mostrarTodosLosDatos(ActionEvent e) { // Muestra todos los datos registrados en la estación.
        actualizarTabla(estacion.obtenerTodasLasTemperaturas());
    }

    private void actualizarTabla(List<Temperatura> datos) {
        tableModel.setRowCount(0); // Limpia la tabla.
        for (Temperatura t : datos) {
            tableModel.addRow(new Object[]{t.getDia(), t.getMaxima(), t.getMinima()}); // Agrega cada temperatura. // Refresca los datos en la tabla.
        }
    }
}
