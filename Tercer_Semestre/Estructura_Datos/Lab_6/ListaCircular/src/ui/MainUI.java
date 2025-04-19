package ui;

import services.ListService;

import javax.swing.*;
import java.awt.*;

public class MainUI extends JFrame {
    private JTextField inputField, updateOldField, updateNewField, randomCountField;
    private JTextArea outputArea;
    private ListService service;

    public MainUI() {
        service = new ListService();
        setTitle("Gestor de Lista Circular");
        setSize(620, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon icono = new ImageIcon("ListaCircular/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Panel para mostrar la lista
        outputArea = new JTextArea(8, 45);
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Contenido de la Lista"));
        add(scrollPane, BorderLayout.NORTH);

        // Panel de operaciones principales
        JPanel operationPanel = new JPanel(new GridLayout(3, 1));

        // Panel Insertar/Eliminar
        JPanel insertDeletePanel = new JPanel(new FlowLayout());
        insertDeletePanel.setBorder(BorderFactory.createTitledBorder("Insertar / Eliminar"));
        inputField = new JTextField(5);
        JButton insertBtn = new JButton("Insertar");
        JButton deleteBtn = new JButton("Eliminar");
        insertDeletePanel.add(new JLabel("Valor:"));
        insertDeletePanel.add(inputField);
        insertDeletePanel.add(insertBtn);
        insertDeletePanel.add(deleteBtn);
        operationPanel.add(insertDeletePanel);

        // Panel Actualizar / Buscar
        JPanel updateSearchPanel = new JPanel(new FlowLayout());
        updateSearchPanel.setBorder(BorderFactory.createTitledBorder("Actualizar / Buscar"));
        updateOldField = new JTextField(5);
        updateNewField = new JTextField(5);
        JButton updateBtn = new JButton("Actualizar");
        JButton searchBtn = new JButton("Buscar");
        updateSearchPanel.add(new JLabel("Valor actual:"));
        updateSearchPanel.add(updateOldField);
        updateSearchPanel.add(new JLabel("Nuevo valor:"));
        updateSearchPanel.add(updateNewField);
        updateSearchPanel.add(updateBtn);
        updateSearchPanel.add(searchBtn);
        operationPanel.add(updateSearchPanel);

        // Panel Herramientas
        JPanel toolsPanel = new JPanel(new FlowLayout());
        toolsPanel.setBorder(BorderFactory.createTitledBorder("Herramientas"));
        JButton sortBtn = new JButton("Ordenar");
        JButton clearBtn = new JButton("Limpiar");
        randomCountField = new JTextField(3);
        JButton randomBtn = new JButton("Aleatorios");
        toolsPanel.add(sortBtn);
        toolsPanel.add(clearBtn);
        toolsPanel.add(new JLabel("Cantidad aleatoria:"));
        toolsPanel.add(randomCountField);
        toolsPanel.add(randomBtn);
        operationPanel.add(toolsPanel);

        add(operationPanel, BorderLayout.CENTER);

        // Eventos
        insertBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                service.insert(val);
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa un número válido.");
            }
        });

        deleteBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(inputField.getText());
                if (!service.delete(val)) showError("No se encontró el valor.");
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa un número válido.");
            }
        });

        updateBtn.addActionListener(e -> {
            try {
                int oldVal = Integer.parseInt(updateOldField.getText());
                int newVal = Integer.parseInt(updateNewField.getText());
                if (!service.update(oldVal, newVal)) showError("No se encontró el valor a actualizar.");
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa valores numéricos.");
            }
        });

        searchBtn.addActionListener(e -> {
            try {
                int val = Integer.parseInt(updateOldField.getText());
                int pos = service.search(val);
                String msg = pos >= 0 ? "Valor encontrado en posición: " + pos : "Valor no encontrado.";
                JOptionPane.showMessageDialog(this, msg);
            } catch (NumberFormatException ex) {
                showError("Ingresa un número válido.");
            }
        });

        sortBtn.addActionListener(e -> {
            service.sort();
            refreshOutput();
        });

        clearBtn.addActionListener(e -> {
            service.clear();
            refreshOutput();
        });

        randomBtn.addActionListener(e -> {
            try {
                int cantidad = Integer.parseInt(randomCountField.getText());
                if (cantidad <= 0) throw new NumberFormatException();
                service.fillRandom(cantidad, 1, 99);
                refreshOutput();
            } catch (NumberFormatException ex) {
                showError("Ingresa una cantidad válida mayor que 0.");
            }
        });

        setLocationRelativeTo(null); // Centrar ventana
    }

    private void refreshOutput() {
        outputArea.setText(service.display());
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
