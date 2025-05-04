package ui;

import services.BinaryTreeService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana principal para la visualización interactiva de árboles binarios.
 * Permite crear, modificar y analizar árboles con interfaz gráfica.
 */
public class TreeFrame extends JFrame {
    // Componentes de la interfaz
    private final TreePanel treePanel;          // Panel de visualización del árbol
    private final JTextArea infoArea;          // Área de información textual
    private BinaryTreeService service;         // Servicio para operaciones del árbol
    private final JRadioButton numbersRadioButton;  // Botón para árbol numérico
    private final JRadioButton lettersRadioButton;  // Botón para árbol alfabético
    private String selectedNode = null;        // Almacena el nodo seleccionado para operaciones

    /**
     * Constructor que configura la ventana principal.
     */
    public TreeFrame() {
        super("Árbol Binario Visual");
        this.setSize(1300, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Inicialización del servicio (por defecto numérico)
        service = new BinaryTreeService(true);

        // Configuración de componentes principales
        treePanel = new TreePanel();  // Panel de dibujo del árbol
        infoArea = new JTextArea();   // Área de información
        infoArea.setEditable(false);  // Solo lectura

        // Panel de controles básicos
        JPanel controlPanel = new JPanel();
        JTextField levelNodeField = new JTextField(5);  // Campo para buscar nivel de nodo

        // Botones principales
        JButton clearBtn = new JButton("Limpiar");
        JButton infoBtn = new JButton("Información del Árbol");
        JButton createTreeBtn = new JButton("Crear Árbol");

        // Radio buttons para selección de tipo de árbol
        numbersRadioButton = new JRadioButton("Números", true);
        lettersRadioButton = new JRadioButton("Letras");

        // Agrupación de radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(numbersRadioButton);
        group.add(lettersRadioButton);

        // Listeners para cambio de tipo de árbol
        numbersRadioButton.addActionListener(e -> updateTreeType());
        lettersRadioButton.addActionListener(e -> updateTreeType());

        // ========== EVENT HANDLERS ==========

        // Creación de árbol automático
        createTreeBtn.addActionListener((ActionEvent e) -> {
            if (numbersRadioButton.isSelected()) {
                service = new BinaryTreeService(true);  // Árbol numérico
            } else {
                service = new BinaryTreeService(false); // Árbol alfabético
            }

            service.createAutomaticTree(7);  // Genera árbol con 7 nodos
            treePanel.setRoot(service.getTree().root);
            updateInfoPanel("Árbol generado automáticamente.");
        });

        // Limpieza del árbol
        clearBtn.addActionListener((ActionEvent e) -> {
            service.getTree().root = null;  // Elimina el árbol
            treePanel.setRoot(null);
            updateInfoPanel("");
        });

        // Generación de información del árbol
        infoBtn.addActionListener((ActionEvent e) -> {
            StringBuilder info = new StringBuilder();

            // Sección de recorrido In-Orden
            info.append("🎯 Recorrido In-Orden 🎯\n");
            info.append("Es el recorrido que visita el nodo izquierdo, luego el nodo actual, y finalmente el nodo derecho.\n\n");
            info.append("Resultado: " + service.getInOrder() + "\n");
            info.append(getNodeNavigationInfo(service.getInOrder(), "In-Orden"));
            info.append("\n\n");

            // Sección de recorrido Pre-Orden
            info.append("🎯 Recorrido Pre-Orden 🎯\n");
            info.append("Es el recorrido que visita el nodo actual, luego el nodo izquierdo, y finalmente el nodo derecho.\n\n");
            info.append("Resultado: " + service.getPreOrder() + "\n");
            info.append(getNodeNavigationInfo(service.getPreOrder(), "Pre-Orden"));
            info.append("\n\n");

            // Sección de recorrido Post-Orden
            info.append("🎯 Recorrido Post-Orden 🎯\n");
            info.append("Es el recorrido que visita primero los nodos izquierdo y derecho, y al final el nodo actual.\n\n");
            info.append("Resultado: " + service.getPostOrder() + "\n");
            info.append(getNodeNavigationInfo(service.getPostOrder(), "Post-Orden"));
            info.append("\n\n");

            // Sección de BFS
            info.append("🎯 Búsqueda en Amplitud (BFS) 🎯\n");
            info.append("Es un recorrido en el que se exploran primero los nodos a nivel superficial antes de profundizar.\n\n");
            info.append("Resultado: " + service.getBreadthFirstSearch() + "\n");
            info.append("\n\n");

            // Sección de altura
            info.append("🎯 Altura del árbol 🎯\n");
            info.append("Es el número máximo de niveles desde la raíz hasta un nodo hoja.\n\n");
            info.append("Resultado: " + service.getTreeHeight() + "\n");
            info.append("\n\n");

            // Sección de grado de la raíz
            info.append("🎯 Grado de la raíz 🎯\n");
            info.append("Es el número de hijos directos que tiene la raíz.\n\n");
            info.append("Resultado: " + service.getDegreeOfRoot() + "\n");
            info.append("\n\n");

            // Nivel de nodo específico
            String node = levelNodeField.getText().trim();
            if (!node.isEmpty()) {
                int level = service.getLevelOfNode(node);
                info.append("🎯 Nivel del nodo '" + node + "' 🎯\n");
                info.append("El nivel representa la distancia del nodo desde la raíz. \n\n");
                info.append("Resultado: " + (level == 0 ? "No encontrado" : level) + "\n");
            }

            updateInfoPanel(info.toString());
        });

        // ========== PANEL DE CREACIÓN ==========
        JPanel treeCreationPanel = new JPanel();
        treeCreationPanel.add(numbersRadioButton);
        treeCreationPanel.add(lettersRadioButton);
        treeCreationPanel.add(createTreeBtn);

        controlPanel.add(clearBtn);
        controlPanel.add(infoBtn);

        JScrollPane infoScroll = new JScrollPane(infoArea);
        infoScroll.setPreferredSize(new Dimension(600, 600));

        // ========== PANEL DE INSERCIÓN MANUAL ==========
        JPanel manualPanel = new JPanel();
        JTextField valueField = new JTextField(5);
        JTextField previousNodeField = new JTextField(5);
        JButton insertBtn = new JButton("Insertar nodo");

        manualPanel.add(new JLabel("Nodo anterior:"));
        manualPanel.add(previousNodeField);
        manualPanel.add(new JLabel("Nodo a ingresar:"));
        manualPanel.add(valueField);
        manualPanel.add(insertBtn);

        // Manejador de inserción manual
        insertBtn.addActionListener((ActionEvent e) -> {
            String previousNode = previousNodeField.getText().trim();
            String newNode = valueField.getText().trim();

            if (!previousNode.isEmpty() && !newNode.isEmpty()) {
                selectedNode = previousNode;
                service.insertManual(selectedNode, newNode);
                updateInfoPanel("Nodo '" + newNode + "' insertado después de '" + selectedNode + "'.");
                treePanel.setRoot(service.getTree().root);
                previousNodeField.setText("");
                valueField.setText("");
            } else {
                updateInfoPanel("Por favor, ingrese tanto el nodo anterior como el nodo a ingresar.");
            }
        });

        // ========== DISPOSICIÓN FINAL ==========
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(treeCreationPanel, BorderLayout.NORTH);
        topPanel.add(manualPanel, BorderLayout.SOUTH);

        this.add(treePanel, BorderLayout.CENTER);
        this.add(infoScroll, BorderLayout.EAST);
        this.add(controlPanel, BorderLayout.SOUTH);
        this.add(topPanel, BorderLayout.NORTH);
        this.setVisible(true);
    }

    /**
     * Actualiza el tipo de árbol según la selección del usuario.
     */
    private void updateTreeType() {
        if (numbersRadioButton.isSelected()) {
            service = new BinaryTreeService(true);  // Árbol numérico
        } else if (lettersRadioButton.isSelected()) {
            service = new BinaryTreeService(false); // Árbol alfabético
        }
    }

    /**
     * Actualiza el panel de información.
     * @param info Texto a mostrar
     */
    private void updateInfoPanel(String info) {
        infoArea.setText(info);
    }

    /**
     * Genera información de navegación para los nodos en un recorrido.
     * @param traversalResult Resultado del recorrido
     * @param traversalType Tipo de recorrido
     * @return String con información formateada
     */
    private String getNodeNavigationInfo(String traversalResult, String traversalType) {
        String[] nodes = traversalResult.split(" ");
        StringBuilder navigationInfo = new StringBuilder();

        navigationInfo.append("Información de los nodos (" + traversalType + "):\n");
        for (int i = 0; i < nodes.length; i++) {
            String previousNode = (i > 0) ? nodes[i - 1] : "N/A";
            String nextNode = (i < nodes.length - 1) ? nodes[i + 1] : "N/A";
            navigationInfo.append("Nodo '" + nodes[i] + "': Siguiente Nodo: " + nextNode + 
                               ", Nodo Anterior: " + previousNode + "\n");
        }

        return navigationInfo.toString();
    }
}