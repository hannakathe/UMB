package ui;  // Pertenece al paquete de interfaz de usuario

import services.ColaService;  // Importa el servicio de colas
import javax.swing.*;         // Componentes gráficos Swing
import java.awt.*;            // AWT para manejo básico de GUI
import java.awt.event.ActionEvent;  // Eventos de acción
import java.util.Random;      // Para generación de números aleatorios

// Clase que representa la interfaz gráfica de usuario para manipular colas
public class ColaUI extends JFrame {  // Hereda de JFrame (ventana principal)
    
    // Componentes de la interfaz
    private ColaService colaService;    // Servicio que maneja la lógica de colas
    private JTextField inputField;      // Campo para ingresar valores manualmente
    private JTextField aleatorioCantidadField;  // Campo para cantidad de aleatorios
    private JTextArea colaText;        // Área de texto para mostrar la cola

    // Constructor que configura la ventana principal
    public ColaUI() {
        colaService = new ColaService();  // Inicializa el servicio

        // Configuración básica de la ventana
        setTitle("TAD Cola - Java");      // Título de la ventana
        setSize(500, 800);                // Dimensiones (ancho, alto)
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Cierra la aplicación al salir
        setLayout(new FlowLayout());      // Diseño de flujo para componentes
        
        // Configuración del ícono (ruta relativa)
        ImageIcon icono = new ImageIcon("TAD/src/resources/icon.png");
        setIconImage(icono.getImage());

        // Inicialización de componentes
        inputField = new JTextField(10);  // Campo de texto de 10 columnas
        JButton encolarBtn = new JButton("Encolar");
        JButton desencolarBtn = new JButton("Desencolar");
        JButton limpiarBtn = new JButton("Limpiar");

        colaText = new JTextArea(40, 40);  // Área grande para mostrar la cola
        colaText.setEditable(false);       // Solo lectura

        aleatorioCantidadField = new JTextField(5);  // Campo para cantidad de aleatorios
        JButton aleatorioBtn = new JButton("Encolar Aleatorios");

        // Configuración de eventos (Action Listeners)
        
        // Botón Limpiar: vacía la cola completa
        limpiarBtn.addActionListener((ActionEvent e) -> {
            colaService.limpiarCola();
            actualizarTextoCola();  // Actualiza la vista
        });
        
        // Botón Encolar: agrega elemento manual
        encolarBtn.addActionListener((ActionEvent e) -> {
            try {
                int valor = Integer.parseInt(inputField.getText());  // Convierte texto a número
                colaService.agregarElemento(valor);
                actualizarTextoCola();
                inputField.setText("");  // Limpia el campo después de agregar
            } catch (NumberFormatException ex) {
                // Manejo de error si no se ingresa un número
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
            }
        });

        // Botón Desencolar: remueve el primer elemento
        desencolarBtn.addActionListener((ActionEvent e) -> {
            try {
                int valor = colaService.eliminarElemento();
                // Muestra diálogo con el elemento removido
                JOptionPane.showMessageDialog(this, "Elemento eliminado: " + valor);
                actualizarTextoCola();
            } catch (IllegalStateException ex) {
                // Manejo de cola vacía
                JOptionPane.showMessageDialog(this, "La cola está vacía.");
            }
        });

        // Botón Aleatorios: agrega N elementos aleatorios
        aleatorioBtn.addActionListener((ActionEvent e) -> {
            try {
                int cantidad = Integer.parseInt(aleatorioCantidadField.getText());
                Random random = new Random();
                for (int i = 0; i < cantidad; i++) {
                    int valor = random.nextInt(1000);  // Números entre 0-999
                    colaService.agregarElemento(valor);
                }
                actualizarTextoCola();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese una cantidad válida.");
            }
        });

        // Agregar componentes a la ventana (orden de aparición)
        
        // Sección para encolar manualmente
        add(new JLabel("Ingrese un número:"));
        add(inputField);
        add(encolarBtn);
        add(desencolarBtn);
        add(limpiarBtn);

        // Sección para encolar aleatorios
        add(new JLabel("Cantidad aleatoria:"));
        add(aleatorioCantidadField);
        add(aleatorioBtn);

        // Área de visualización con barra de desplazamiento
        add(new JScrollPane(colaText));
    }

    // Actualiza el área de texto con el estado actual de la cola
    private void actualizarTextoCola() {
        colaText.setText(colaService.obtenerCola());
    }
}