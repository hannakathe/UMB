package ui;

import services.HanoiService;
import javax.swing.*;
import java.awt.*;

// Clase principal de la interfaz gráfica para el juego de Torres de Hanoi
public class HanoiUI extends JFrame {
    // Servicio que contiene la lógica del juego
    private HanoiService service;
    // Número inicial de discos (valor por defecto: 5)
    private int numDiscos = 5;

    // Componentes de la interfaz
    private JComboBox<String> origen, destino; // Selectores de torres
    private JButton moverBtn; // Botón para mover discos
    private HanoiPanel hanoiPanel; // Panel de visualización del juego
    private JTextArea historialTextArea; // Área de texto para registrar movimientos

    // Variable de control para el hilo de resolución automática
    private volatile boolean resolviendo = false;

    // Constructor principal
    public HanoiUI() {
        service = new HanoiService(numDiscos); // Inicializa el servicio
        setTitle("Torres de Hanoi"); // Título de la ventana
        setSize(900, 500); // Tamaño de la ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Comportamiento al cerrar
        setLayout(new BorderLayout()); // Diseño de la ventana
        
        // Configuración del ícono de la aplicación
        ImageIcon icono = new ImageIcon("TorreHanoi/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Inicialización del panel de visualización
        hanoiPanel = new HanoiPanel(service.getTorres(), numDiscos);
        add(hanoiPanel, BorderLayout.CENTER); // Añade al centro

        // Configuración del área de historial de movimientos
        historialTextArea = new JTextArea();
        historialTextArea.setEditable(false); // Solo lectura
        JScrollPane historialScroll = new JScrollPane(historialTextArea);
        historialScroll.setPreferredSize(new Dimension(200, 0)); // Ancho fijo
        add(historialScroll, BorderLayout.EAST); // Añade a la derecha

        // Panel de controles en la parte inferior
        JPanel controls = new JPanel();

        // ComboBox para seleccionar torres
        origen = new JComboBox<>(new String[]{"Torre 1", "Torre 2", "Torre 3"});
        destino = new JComboBox<>(new String[]{"Torre 1", "Torre 2", "Torre 3"});
        moverBtn = new JButton("Mover"); // Botón para mover manualmente

        // Botón para resolver automáticamente
        JButton resolverBtn = new JButton("Resolver automáticamente");
        resolverBtn.addActionListener(e -> resolverAutomaticamente());

        // Botón para reiniciar el juego
        JButton reiniciarBtn = new JButton("Reiniciar juego");
        reiniciarBtn.addActionListener(e -> reiniciarJuego());

        // Acción para mover disco manualmente
        moverBtn.addActionListener(e -> moverDisco());

        // Agregar componentes al panel de controles
        controls.add(resolverBtn);
        controls.add(reiniciarBtn);
        controls.add(new JLabel("Origen:"));
        controls.add(origen);
        controls.add(new JLabel("Destino:"));
        controls.add(destino);
        controls.add(moverBtn);

        add(controls, BorderLayout.SOUTH); // Añadir controles abajo

        actualizarTorres(); // Actualizar visualización inicial
        setVisible(true); // Mostrar ventana
    }

    // Método para resolver el juego automáticamente
    private void resolverAutomaticamente() {
        resolviendo = true; // Activar bandera de resolución

        new Thread(() -> {
            // Deshabilitar controles durante la resolución
            moverBtn.setEnabled(false);
            origen.setEnabled(false);
            destino.setEnabled(false);

            // Ejecutar algoritmo de resolución
            service.resolverHanoi(numDiscos, 0, 2, 1, (from, to) -> {
                if (!resolviendo) return; // Salir si se canceló la resolución

                agregarMovimiento(from, to); // Registrar movimiento
                actualizarTorres(); // Actualizar visualización

                try {
                    Thread.sleep(400); // Pausa para visualización
                } catch (InterruptedException ignored) {}
            });

            // Rehabilitar controles al finalizar
            moverBtn.setEnabled(true);
            origen.setEnabled(true);
            destino.setEnabled(true);

            // Mostrar mensaje si se completó
            if (resolviendo && service.juegoTerminado(numDiscos)) {
                JOptionPane.showMessageDialog(this, "¡Resolución completa!");
            }

            resolviendo = false; // Finalizar resolución
        }).start(); // Iniciar hilo
    }

    // Método para mover disco manualmente
    private void moverDisco() {
        int from = origen.getSelectedIndex(); // Torre origen
        int to = destino.getSelectedIndex(); // Torre destino

        // Validación de movimiento al mismo lugar
        if (from == to) {
            JOptionPane.showMessageDialog(this, "No puedes mover al mismo lugar");
            return;
        }

        // Intentar mover el disco
        boolean moved = service.moverDisco(from, to);
        if (!moved) {
            JOptionPane.showMessageDialog(this, "Movimiento no válido");
        } else {
            agregarMovimiento(from, to); // Registrar movimiento exitoso
        }

        actualizarTorres(); // Actualizar visualización

        // Verificar si el juego terminó
        if (service.juegoTerminado(numDiscos)) {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Has ganado");
        }
    }

    // Método para registrar un movimiento en el historial
    private void agregarMovimiento(int from, int to) {
        String movimiento = String.format("Movió disco de Torre %d a Torre %d", from + 1, to + 1);
        historialTextArea.append(movimiento + "\n"); // Agregar al historial
        // Auto-scroll al final del texto
        historialTextArea.setCaretPosition(historialTextArea.getDocument().getLength());
    }

    // Método para actualizar la visualización de las torres
    private void actualizarTorres() {
        hanoiPanel.actualizar(service.getTorres());
    }

    // Método para reiniciar el juego
    private void reiniciarJuego() {
        // Cancelar resolución automática si está activa
        resolviendo = false;

        // Reiniciar todos los componentes
        service = new HanoiService(numDiscos); // Nuevo servicio
        historialTextArea.setText(""); // Limpiar historial
        hanoiPanel.actualizar(service.getTorres()); // Actualizar visualización
    }
}