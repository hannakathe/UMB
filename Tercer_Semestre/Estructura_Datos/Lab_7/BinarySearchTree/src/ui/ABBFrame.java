// Package declaration for the UI components
package ui;

// Import required classes
import services.ABBService;
import javax.swing.*;
import java.awt.*;

// Main application frame for BST visualization
public class ABBFrame extends JFrame {
    // Service layer for tree operations
    private final ABBService service = new ABBService();
    
    // Custom panel for tree visualization
    private final ABBPanel panel = new ABBPanel();
    
    // Text area for displaying tree information
    private final JTextArea output = new JTextArea(6, 30);

    // Constructor sets up the main application window
    public ABBFrame() {
        setTitle("Árbol Binario de Búsqueda");  // Window title
        setSize(1000, 600);  // Initial size (width, height)
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Close behavior
        setLayout(new BorderLayout());  // Main layout manager

        // Configure visualization panel
        panel.setPreferredSize(new Dimension(600, 400));
        add(panel, BorderLayout.CENTER);  // Center area for tree display
        
        // Add control panel to the right
        add(crearPanelControl(), BorderLayout.EAST);
        
        setVisible(true);  // Make window visible
    }

    // Creates the control panel with all buttons and inputs
    private JPanel crearPanelControl() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));  // Vertical layout

        // ===== ROW 1: Insert After Node =====
        JPanel fila1 = new JPanel();
        JTextField valorField = new JTextField(5);  // For new node value
        JTextField nodoAnteriorField = new JTextField(5);  // For reference node
        JButton insertarBtn = new JButton("Insertar Después de Nodo");

        insertarBtn.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(valorField.getText());
                int nodoAnterior = Integer.parseInt(nodoAnteriorField.getText());
                
                // Perform insertion
                service.insertarDespuesDe(valor, nodoAnterior);
                
                // Update visualization
                panel.setRaiz(service.abb.raiz);
                output.setText("Nodo insertado después de " + nodoAnterior);
                actualizarInformacion();
            } catch (NumberFormatException ex) {
                output.setText("Por favor ingrese valores válidos.");
            }
        });

        fila1.add(new JLabel("Valor:"));
        fila1.add(valorField);
        fila1.add(new JLabel("Nodo Anterior:"));
        fila1.add(nodoAnteriorField);
        fila1.add(insertarBtn);

        // ===== ROW 2: Random Tree Generation =====
        JPanel fila2 = new JPanel();
        JButton aleatorioBtn = new JButton("Generar Árbol Aleatorio");

        aleatorioBtn.addActionListener(e -> {
            // Create new empty tree
            service.abb = new models.ArbolBinarioBusqueda();
            
            // Generate 10 random nodes
            service.generarArbolAleatorio(10);
            
            // Update visualization
            panel.setRaiz(service.abb.raiz);
            output.setText("Árbol Aleatorio Generado");
            actualizarInformacion();
        });

        fila2.add(aleatorioBtn);

        // ===== ROW 3: Node Search =====
        JPanel fila3 = new JPanel();
        JButton buscarBtn = new JButton("Buscar Nodo");
        JTextField buscarField = new JTextField(5);

        buscarBtn.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(buscarField.getText());
                
                // Search and mark node
                String resultado = service.buscarYMarcar(valor);
                
                // Show result in dialog
                JOptionPane.showMessageDialog(this, resultado);
                
                // Update visualization (to show marked node)
                panel.setRaiz(service.abb.raiz);
                actualizarInformacion();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un valor válido.");
            }
        });

        fila3.add(new JLabel("Buscar Nodo:"));
        fila3.add(buscarField);
        fila3.add(buscarBtn);

        // ===== ROW 4: Node Deletion =====
        JPanel fila4 = new JPanel();
        JButton eliminarBtn = new JButton("Eliminar Nodo");

        eliminarBtn.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(buscarField.getText());
                
                // Attempt deletion
                boolean eliminado = service.eliminar(valor);
                
                // Show result
                if (eliminado) {
                    output.setText("Nodo eliminado.");
                } else {
                    output.setText("Nodo no encontrado.");
                }
                
                // Update visualization
                panel.setRaiz(service.abb.raiz);
                actualizarInformacion();
            } catch (NumberFormatException ex) {
                output.setText("Por favor ingrese un valor válido.");
            }
        });

        fila4.add(eliminarBtn);

        // Configure output area
        output.setEditable(false);  // Read-only
        output.setLineWrap(true);  // Automatic line wrapping
        output.setWrapStyleWord(true);  // Wrap at word boundaries

        // Assemble all components
        p.add(fila1);
        p.add(fila2);
        p.add(fila3);
        p.add(fila4);
        p.add(new JScrollPane(output));  // Make output scrollable

        return p;
    }

    // Updates the information display with current tree status
    private void actualizarInformacion() {
        String info = "Inorden: " + service.inorden() + "\n" +
                      "Preorden: " + service.preorden() + "\n" +
                      "Postorden: " + service.postorden() + "\n" +
                      "¿Es lleno? " + (service.esLleno() ? "Sí" : "No");
        output.setText(info);
    }
}