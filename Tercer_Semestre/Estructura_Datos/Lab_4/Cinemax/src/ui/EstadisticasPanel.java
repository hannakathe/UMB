package ui;

import javax.swing.*;
import java.awt.*;

// Clase que representa un panel para mostrar estadísticas en pestañas
public class EstadisticasPanel extends JPanel {
    // Componentes de la interfaz gráfica
    private JTabbedPane tabbedPane; // Panel de pestañas para organizar las estadísticas
    private int conteoPestanas = 1; // Contador para numerar las pestañas

    // Constructor de la clase
    public EstadisticasPanel() {
        // Configuración del layout y borde del panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("📊 Estadísticas")); // Título del panel

        // Inicialización del panel de pestañas
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER); // Añadir el panel de pestañas al centro del panel principal

        // Establecer el tamaño preferido del panel
        setPreferredSize(new Dimension(400, 600));
    }

    // Método para agregar una nueva estadística en una pestaña
    public void agregarEstadistica(String estadistica) {
        // Crear un área de texto para mostrar la estadística
        JTextArea textArea = new JTextArea(estadistica);
        textArea.setEditable(false); // Hacer que el área de texto no sea editable
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Configurar la fuente del texto

        // Añadir un JScrollPane para permitir el desplazamiento si el contenido es grande
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Añadir una nueva pestaña con el contenido de la estadística
        tabbedPane.addTab("Semana " + conteoPestanas++, scrollPane); // Nombre de la pestaña: "Semana X"
    }

    // Método para limpiar todas las pestañas y reiniciar el contador
    public void limpiarPestanas() {
        tabbedPane.removeAll(); // Eliminar todas las pestañas
        conteoPestanas = 1; // Reiniciar el contador de pestañas
    }
}