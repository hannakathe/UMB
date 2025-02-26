package ui;

import model.Bus;
import service.Flota;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FlotaUI extends JFrame {
    // Componentes para configurar la cantidad de buses y de ventas por día
    private JTextField txtCantidadBuses;
    private JTextField txtCantidadVentas;
    private JButton btnCrearTabla, btnProcesarVentas, btnBorrarTodo;
    
    // Panel donde se mostrará la “tabla de ingreso” personalizada
    private JPanel panelIngresoVentas;
    
    // Pestañas principales: "Ingreso de Ventas" y "Resultados"
    private JTabbedPane tabbedPanePrincipal;
    // Dentro de "Resultados", cada grupo procesado se agrega en un JTabbedPane
    private JTabbedPane tabbedResultados;
    
    // Variable para contar los cálculos realizados (para etiquetar las pestañas)
    private int calculoCount = 0;
    
    // Número de días (fijo: 7, de lunes a domingo)
    private final String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
    
    // Parámetros globales que se configurarán
    private int cantidadBuses;
    private int cantidadVentasPorDia;
    
    // Lógica para almacenar los datos del grupo actual
    private Flota flota;
    
    public FlotaUI() {
        super("Ventas de Buses Intermunicipales");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 500);
        setLocationRelativeTo(null);
        ImageIcon icono = new ImageIcon("VentasBuses/src/resources/icon.png"); 
        setIconImage(icono.getImage());
        
        // Panel superior de configuración (cantidad de buses y cantidad de ventas por día)
        JPanel panelConfiguracion = new JPanel(new FlowLayout());
        panelConfiguracion.add(new JLabel("Cantidad de buses:"));
        txtCantidadBuses = new JTextField(5);
        panelConfiguracion.add(txtCantidadBuses);
        panelConfiguracion.add(new JLabel("Cantidad de ventas por día:"));
        txtCantidadVentas = new JTextField(5);
        panelConfiguracion.add(txtCantidadVentas);
        btnCrearTabla = new JButton("Crear Tabla de Ventas");
        panelConfiguracion.add(btnCrearTabla);
        btnProcesarVentas = new JButton("Procesar Ventas");
        panelConfiguracion.add(btnProcesarVentas);
        btnBorrarTodo = new JButton("Borrar Todo");
        panelConfiguracion.add(btnBorrarTodo);
        
        // Panel de Ingreso (donde se mostrará la tabla dinámica)
        panelIngresoVentas = new JPanel();
        panelIngresoVentas.setLayout(new BorderLayout());
        panelIngresoVentas.setBorder(new TitledBorder("Ingreso de Ventas por Bus y Día"));
        
        // Panel contenedor para el ingreso y resultados
        tabbedPanePrincipal = new JTabbedPane();
        JPanel panelIngresoCompleto = new JPanel(new BorderLayout());
        panelIngresoCompleto.add(panelConfiguracion, BorderLayout.NORTH);
        panelIngresoCompleto.add(new JScrollPane(panelIngresoVentas), BorderLayout.CENTER);
        tabbedPanePrincipal.addTab("Ingreso de Ventas", panelIngresoCompleto);
        
        // Pestaña de Resultados (inicialmente deshabilitada)
        tabbedResultados = new JTabbedPane();
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.add(tabbedResultados, BorderLayout.CENTER);
        tabbedPanePrincipal.addTab("Resultados", panelResultados);
        tabbedPanePrincipal.setEnabledAt(1, false);
        
        add(tabbedPanePrincipal);
        
        // Acciones de botones
        btnCrearTabla.addActionListener(e -> crearPanelIngresoVentas());
        btnProcesarVentas.addActionListener(e -> procesarVentas());
        btnBorrarTodo.addActionListener(e -> borrarTodo());
    }
    
    // Método para crear el panel de ingreso personalizado (tabla de botones)
    private void crearPanelIngresoVentas() {
        // Validar y obtener parámetros
        try {
            cantidadBuses = Integer.parseInt(txtCantidadBuses.getText().trim());
            cantidadVentasPorDia = Integer.parseInt(txtCantidadVentas.getText().trim());
            if(cantidadBuses <= 0 || cantidadVentasPorDia <= 0) {
                JOptionPane.showMessageDialog(this, "Ambos valores deben ser mayores a cero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos para ambas cantidades.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Crear la instancia de Flota (para el grupo actual)
        flota = new Flota(cantidadBuses);
        
        // Crear un panel con GridLayout: filas = cantidadBuses+1 (para encabezado) y columnas = días+1 (para etiqueta de bus)
        panelIngresoVentas.removeAll();
        panelIngresoVentas.setLayout(new GridLayout(cantidadBuses + 1, dias.length + 1, 5, 5));
        
        // Encabezado superior: celda vacía + nombres de los días
        panelIngresoVentas.add(new JLabel(""));
        for (String dia : dias) {
            JLabel lblDia = new JLabel(dia, SwingConstants.CENTER);
            lblDia.setFont(new Font("Arial", Font.BOLD, 12));
            panelIngresoVentas.add(lblDia);
        }
        
        // Filas para cada bus
        for (int i = 0; i < cantidadBuses; i++) {
            String nombreBus = "Bus #" + (i + 1);
            JLabel lblBus = new JLabel(nombreBus, SwingConstants.CENTER);
            lblBus.setFont(new Font("Arial", Font.BOLD, 12));
            panelIngresoVentas.add(lblBus);
            // Para cada día, se crea un botón para ingresar ventas
            for (int j = 0; j < dias.length; j++) {
                JButton btnIngresar = new JButton("Ingresar");
                final int busIndex = i;
                final int diaIndex = j;
                btnIngresar.addActionListener(e -> abrirDialogoVentas(busIndex, diaIndex, btnIngresar));
                panelIngresoVentas.add(btnIngresar);
            }
        }
        
        panelIngresoVentas.revalidate();
        panelIngresoVentas.repaint();
    }
    
    // Abre un diálogo para ingresar las ventas de un bus y día específico
    private void abrirDialogoVentas(int busIndex, int diaIndex, JButton btnOrigen) {
        JDialog dialogo = new JDialog(this, "Ingresar Ventas - " + "Bus #" + (busIndex+1) + " - " + dias[diaIndex], true);
        dialogo.setSize(300, 200);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panelDialogo = new JPanel(new BorderLayout());
        JPanel panelCampos = new JPanel(new GridLayout(cantidadVentasPorDia, 2, 5, 5));
        
        // Crear array de JTextField para cada venta
        JTextField[] txtVentas = new JTextField[cantidadVentasPorDia];
        for (int i = 0; i < cantidadVentasPorDia; i++) {
            panelCampos.add(new JLabel("Venta " + (i+1) + ":"));
            txtVentas[i] = new JTextField();
            panelCampos.add(txtVentas[i]);
        }
        
        panelDialogo.add(panelCampos, BorderLayout.CENTER);
        JButton btnAceptar = new JButton("Aceptar");
        panelDialogo.add(btnAceptar, BorderLayout.SOUTH);
        
        btnAceptar.addActionListener(e -> {
            List<Double> ventas = new ArrayList<>();
            try {
                for (JTextField campo : txtVentas) {
                    // Si el campo está vacío, se considera 0 (o se puede forzar a que sea obligatorio)
                    String texto = campo.getText().trim();
                    double valor = texto.isEmpty() ? 0.0 : Double.parseDouble(texto);
                    ventas.add(valor);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese valores numéricos válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Almacenar las ventas en el objeto Bus correspondiente
            flota.getBuses().get(busIndex).setVentasParaDia(diaIndex, ventas);
            // Actualizar el texto del botón para indicar que se ingresó la información (por ejemplo, mostrar la cantidad)
            btnOrigen.setText("Ingresado (" + ventas.size() + ")");
            dialogo.dispose();
        });
        
        dialogo.add(panelDialogo);
        dialogo.setVisible(true);
    }
    
    // Procesa los datos ingresados y genera una pestaña de resultados
    private void procesarVentas() {
        if (flota == null) {
            JOptionPane.showMessageDialog(this, "Primero cree la tabla e ingrese las ventas.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Verificar que para cada bus y día se hayan ingresado las ventas (se puede omitir o considerar 0 si falta)
        int buses = flota.getBuses().size();
        for (int i = 0; i < buses; i++) {
            for (int j = 0; j < dias.length; j++) {
                // Si alguna celda no tiene ventas (lista vacía), se considera 0
                if (flota.getBuses().get(i).getVentasPorDia(j).isEmpty()) {
                    flota.getBuses().get(i).setVentasParaDia(j, new ArrayList<>());
                }
            }
        }
        
        // Construir el resumen de resultados
        StringBuilder resumen = new StringBuilder();
        resumen.append("---- Resultados de Ventas Ingresadas ----\n\n");
        Bus busMayor = flota.busMayorGanancia();
        Bus busMenor = flota.busMenorGanancia();
        resumen.append("Bus que más gana: " + busMayor.getNombre() + " (Total: " + busMayor.totalVentas() + ")\n");
        resumen.append("Bus que menos gana: " + busMenor.getNombre() + " (Total: " + busMenor.totalVentas() + ")\n\n");
        resumen.append("Día de mayor venta por cada bus:\n");
        for (int i = 0; i < buses; i++) {
            Bus bus = flota.getBuses().get(i);
            int indiceDia = bus.diaMayorVenta();
            if (indiceDia >= 0) {
                resumen.append(bus.getNombre() + " -> " + dias[indiceDia] + " (Total: " + bus.totalVentasPorDia(indiceDia) + ")\n");
            }
        }
        resumen.append("\n");
        resumen.append("Ventas sobre el promedio por bus:\n");
        for (int i = 0; i < buses; i++) {
            Bus bus = flota.getBuses().get(i);
            double promedio = bus.calcularPromedioVentas();
            resumen.append(bus.getNombre() + " (Promedio: " + promedio + "): ");
            for (int j = 0; j < dias.length; j++) {
                double totalDia = bus.totalVentasPorDia(j);
                if (totalDia > promedio) {
                    resumen.append(dias[j] + " (" + totalDia + ")  ");
                }
            }
            resumen.append("\n");
        }
        resumen.append("\n");
        resumen.append("Promedio general de ventas: " + flota.calcularPromedioGeneral() + "\n");
        
        // --- Preparar tablas para mostrar los datos ---
        // Tabla con las ventas ingresadas (antes del incremento)
        DefaultTableModel modeloOriginal = new DefaultTableModel(getColumnas(), 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (int i = 0; i < buses; i++) {
            Bus bus = flota.getBuses().get(i);
            Object[] filaOriginal = new Object[dias.length + 1];
            filaOriginal[0] = bus.getNombre();
            for (int j = 0; j < dias.length; j++) {
                filaOriginal[j+1] = bus.obtenerVentasComoString(j);
            }
            modeloOriginal.addRow(filaOriginal);
        }
        
        // Aplicar el aumento del 20% a las ventas por debajo del promedio
        flota.aumentarVentasDebajoPromedio(20);
        
        // Tabla con las ventas actualizadas (después del incremento)
        DefaultTableModel modeloActualizada = new DefaultTableModel(getColumnas(), 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        for (int i = 0; i < buses; i++) {
            Bus bus = flota.getBuses().get(i);
            Object[] filaActualizada = new Object[dias.length + 1];
            filaActualizada[0] = bus.getNombre();
            for (int j = 0; j < dias.length; j++) {
                filaActualizada[j+1] = bus.obtenerVentasComoString(j);
            }
            modeloActualizada.addRow(filaActualizada);
        }
        
        JTable tablaOriginal = new JTable(modeloOriginal);
        JScrollPane scrollOriginal = new JScrollPane(tablaOriginal);
        scrollOriginal.setBorder(BorderFactory.createTitledBorder("Ventas Ingresadas (antes del incremento)"));
        scrollOriginal.setPreferredSize(new Dimension(880, 200));
        
        JTable tablaActual = new JTable(modeloActualizada);
        JScrollPane scrollActual = new JScrollPane(tablaActual);
        scrollActual.setBorder(BorderFactory.createTitledBorder("Ventas Actualizadas (después del incremento del 20%)"));
        scrollActual.setPreferredSize(new Dimension(880, 200));
        
        // Panel de contenido con resumen y ambas tablas
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        JTextArea areaRes = new JTextArea(resumen.toString());
        areaRes.setEditable(false);
        areaRes.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollRes = new JScrollPane(areaRes);
        scrollRes.setBorder(BorderFactory.createTitledBorder("Resumen de Ventas"));
        scrollRes.setPreferredSize(new Dimension(880, 150));
        
        panelContenido.add(scrollRes);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(scrollOriginal);
        panelContenido.add(Box.createVerticalStrut(10));
        panelContenido.add(scrollActual);
        
        JPanel panelCalculo = new JPanel(new BorderLayout());
        panelCalculo.add(panelContenido, BorderLayout.CENTER);
        
        calculoCount++;
        tabbedResultados.addTab("Cálculo #" + calculoCount, panelCalculo);
        tabbedPanePrincipal.setEnabledAt(1, true);
        tabbedPanePrincipal.setSelectedIndex(1);
        
        // Limpiar el panel de ingreso para permitir ingresar un nuevo grupo (manteniendo los cálculos previos)
        panelIngresoVentas.removeAll();
        panelIngresoVentas.revalidate();
        panelIngresoVentas.repaint();
        txtCantidadBuses.setText("");
        txtCantidadVentas.setText("");
        flota = null;
    }
    
    // Devuelve el arreglo de nombres de columnas para las tablas de resultados
    private String[] getColumnas() {
        String[] columnas = new String[dias.length + 1];
        columnas[0] = "Bus";
        for (int i = 0; i < dias.length; i++) {
            columnas[i+1] = dias[i];
        }
        return columnas;
    }
    
    // Borrar Todo: reinicia toda la información
    private void borrarTodo() {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro que desea borrar toda la información?", "Confirmar Borrado", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            panelIngresoVentas.removeAll();
            panelIngresoVentas.revalidate();
            panelIngresoVentas.repaint();
            txtCantidadBuses.setText("");
            txtCantidadVentas.setText("");
            flota = null;
            tabbedResultados.removeAll();
            calculoCount = 0;
            tabbedPanePrincipal.setEnabledAt(1, false);
        }
    }
}
