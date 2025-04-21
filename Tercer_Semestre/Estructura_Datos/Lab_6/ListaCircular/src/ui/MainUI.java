package ui;

import services.ListService;
import javax.swing.*;
import java.awt.*;

// Clase principal de la interfaz gráfica para gestionar la lista circular
public class MainUI extends JFrame {
    // Componentes de la interfaz
    private JTextField inputField;          // Campo para insertar/eliminar valores
    private JTextField updateOldField;      // Campo para valor actual (actualización)
    private JTextField updateNewField;      // Campo para nuevo valor (actualización)
    private JTextField randomCountField;    // Campo para cantidad de números aleatorios
    private JTextArea outputArea;           // Área para mostrar el contenido de la lista
    private ListService service;            // Servicio para operaciones con la lista

    // Constructor de la ventana principal
    public MainUI() {
        service = new ListService();  // Inicializa el servicio de lista
        
        // Configuración básica de la ventana
        setTitle("Gestor de Lista Circular");
        setSize(620, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon icono = new ImageIcon("ListaCircular/src/resources/icon.png"); 
        setIconImage(icono.getImage());  // Establece el icono de la aplicación

        // Panel superior: Área de visualización de la lista
        outputArea = new JTextArea(8, 45);
        outputArea.setEditable(false);  // Solo lectura
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contenido de la Lista"));
        add(scrollPane, BorderLayout.NORTH);

        // Panel central: Operaciones principales (3 secciones)
        JPanel operationPanel = new JPanel(new GridLayout(3, 1));

        /* ========== Sección Insertar/Eliminar ========== */
        JPanel insertDeletePanel = new JPanel(new FlowLayout());
        insertDeletePanel.setBorder(BorderFactory.createTitledBorder("Insertar / Eliminar"));
        inputField = new JTextField(5);
        JButton insertBtn = new JButton("Insertar");
        JButton deleteBtn = new JButton("Eliminar");
        
        // Agrega componentes al panel
        insertDeletePanel.add(new JLabel("Valor:"));
        insertDeletePanel.add(inputField);
        insertDeletePanel.add(insertBtn);
        insertDeletePanel.add(deleteBtn);
        operationPanel.add(insertDeletePanel);

        /* ========== Sección Actualizar/Buscar ========== */
        JPanel updateSearchPanel = new JPanel(new FlowLayout());
        updateSearchPanel.setBorder(BorderFactory.createTitledBorder("Actualizar / Buscar"));
        updateOldField = new JTextField(5);
        updateNewField = new JTextField(5);
        JButton updateBtn = new JButton("Actualizar");
        JButton searchBtn = new JButton("Buscar");
        
        // Agrega componentes al panel
        updateSearchPanel.add(new JLabel("Valor actual:"));
        updateSearchPanel.add(updateOldField);
        updateSearchPanel.add(new JLabel("Nuevo valor:"));
        updateSearchPanel.add(updateNewField);
        updateSearchPanel.add(updateBtn);
        updateSearchPanel.add(searchBtn);
        operationPanel.add(updateSearchPanel);

        /* ========== Sección Herramientas ========== */
        JPanel toolsPanel = new JPanel(new FlowLayout());
        toolsPanel.setBorder(BorderFactory.createTitledBorder("Herramientas"));
        JButton sortBtn = new JButton("Ordenar");
        JButton clearBtn = new JButton("Limpiar");
        randomCountField = new JTextField(3);
        JButton randomBtn = new JButton("Aleatorios");
        
        // Agrega componentes al panel
        toolsPanel.add(sortBtn);
        toolsPanel.add(clearBtn);
        toolsPanel.add(new JLabel("Cantidad aleatoria:"));
        toolsPanel.add(randomCountField);
        toolsPanel.add(randomBtn);
        operationPanel.add(toolsPanel);

        // Agrega el panel de operaciones al centro de la ventana
        add(operationPanel, BorderLayout.CENTER);

        // ========== Configuración de Eventos ========== //

        // Evento para insertar valores
        insertBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                service.insert(val);
                refreshOutput();  // Actualiza la visualización
            } catch (NumberFormatException ex) {
                showError("Ingresa un número válido.");
            }
        });

        // Evento para eliminar valores
        deleteBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                if (!service.delete(val)) 
                    showError("No se encontró el valor.");
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa un número válido.");
            }
        });

        // Evento para actualizar valores
        updateBtn.addActionListener(e -> {
            try {
                int oldVal = Integer.parseInt(updateOldField.getText());
                int newVal = Integer.parseInt(updateNewField.getText());
                if (!service.update(oldVal, newVal)) 
                    showError("No se encontró el valor a actualizar.");
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa valores numéricos.");
            }
        });

        // Evento para buscar valores
        searchBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(updateOldField.getText());
                int pos = service.search(val);
                String msg = pos >= 0 ? 
                    "Valor encontrado en posición: " + pos : 
                    "Valor no encontrado.";
                JOptionPane.showMessageDialog(this, msg);
            } catch (NumberFormatException ex) {
                showError("Ingresa un número válido.");
            }
        });

        // Evento para ordenar la lista
        sortBtn.addActionListener(e -> {
            service.sort();
            refreshOutput();
        });

        // Evento para limpiar la lista
        clearBtn.addActionListener(e -> {
            service.clear();
            refreshOutput();
        });

        // Evento para generar valores aleatorios
        randomBtn.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(randomCountField.getText());
                if (cantidad <= 0) throw new NumberFormatException();
                service.fillRandom(cantidad, 1, 99);  // Valores entre 1-99
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa una cantidad válida mayor que 0.");
            }
        });

        setLocationRelativeTo(null);  // Centra la ventana en la pantalla
    }

    // Actualiza el contenido mostrado en el área de texto
    private void refreshOutput() {
        outputArea.setText(service.display());
    }

    // Muestra un mensaje de error
    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}