package gui;

import model.Sede;
import services.Empresa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentasGUI extends JFrame {
    private Empresa empresa; // Almacena la instancia de la empresa con sus sedes.
    private JTextField txtSedes;
    private JTextArea txtResultados;
    private JButton btnIngresarVentas, btnCalcularPromedios;
    
    public VentasGUI() {
        setTitle("Gestión de Ventas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        
        add(new JLabel("Ingrese la cantidad de sedes:"));
        txtSedes = new JTextField(5);
        add(txtSedes);
        
        btnIngresarVentas = new JButton("Ingresar Ventas");
        btnCalcularPromedios = new JButton("Calcular Promedios");
        
        add(btnIngresarVentas);
        add(btnCalcularPromedios);
        
        txtResultados = new JTextArea(15, 40);
        txtResultados.setEditable(false);
        add(new JScrollPane(txtResultados));
        
        btnIngresarVentas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int cantidadSedes = Integer.parseInt(txtSedes.getText());
                empresa = new Empresa(cantidadSedes);
                
                for (Sede sede : empresa.getSedes()) {
                    for (int i = 0; i < 7; i++) {
                        String ventaStr = JOptionPane.showInputDialog("Ingrese venta para " + sede.getNombre() + " - Día " + (i + 1));
                        double venta = Double.parseDouble(ventaStr);
                        sede.agregarVenta(venta);
                    }
                }
                txtResultados.setText("Ventas registradas correctamente.\nPresione 'Calcular Promedios'.");
            }
        });
        
        btnCalcularPromedios.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (empresa == null) {
                    txtResultados.setText("Debe ingresar las ventas primero.");
                    return;
                }
                
                StringBuilder resultado = new StringBuilder();
                resultado.append("Ventas sobre el promedio:\n");
                empresa.mostrarVentasSobrePromedio();
                resultado.append("Promedio total de la empresa: ").append(empresa.calcularPromedioGeneral()).append("\n");
                
                empresa.aumentarVentasDebajoPromedio(15);
                resultado.append("Ventas después del aumento del 15%:\n");
                for (Sede sede : empresa.getSedes()) {
                    resultado.append(sede.getNombre()).append(": ").append(sede.getVentas()).append("\n");
                }
                
                txtResultados.setText(resultado.toString());
            }
        });
        
        setVisible(true);
    }
    
    public static void main(String[] args) {
        new VentasGUI();
    }
}
