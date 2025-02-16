import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BusSalesGUI {
    private int M;
    private int[][] ventas;
    private JFrame frame;
    private JTextArea textArea;
    
    public BusSalesGUI(int M) {
        this.M = M;
        this.ventas = new int[M][7];
        initializeGUI();
    }
    
    private void initializeGUI() {
        frame = new JFrame("Gestión de Ventas de Buses");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        JButton btnIngresar = new JButton("Ingresar Ventas");
        JButton btnMostrar = new JButton("Mostrar Resultados");
        JButton btnAumentar = new JButton("Aumentar Ventas");
        
        btnIngresar.addActionListener(e -> ingresarVentas());
        btnMostrar.addActionListener(e -> mostrarResultados());
        btnAumentar.addActionListener(e -> aumentarVentas());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnIngresar);
        buttonPanel.add(btnMostrar);
        buttonPanel.add(btnAumentar);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.add(panel);
        frame.setVisible(true);
    }
    
    private void ingresarVentas() {
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < 7; j++) {
                String input = JOptionPane.showInputDialog("Ingrese las ventas del Bus " + (i+1) + " para el día " + (j+1));
                ventas[i][j] = Integer.parseInt(input);
            }
        }
    }
    
    private void mostrarResultados() {
        int busMax = obtenerBusMax();
        int busMin = obtenerBusMin();
        
        StringBuilder sb = new StringBuilder();
        sb.append("Bus que más gana: Bus ").append(busMax + 1).append("\n");
        sb.append("Bus que menos gana: Bus ").append(busMin + 1).append("\n");
        
        for (int i = 0; i < M; i++) {
            int diaMax = obtenerDiaMax(i);
            sb.append("El Bus ").append(i + 1).append(" gana más el día ").append(diaMax + 1).append("\n");
        }
        
        textArea.setText(sb.toString());
    }
    
    private int obtenerBusMax() {
        int maxGanancias = 0, busIndex = 0;
        for (int i = 0; i < M; i++) {
            int total = 0;
            for (int j = 0; j < 7; j++) {
                total += ventas[i][j];
            }
            if (total > maxGanancias) {
                maxGanancias = total;
                busIndex = i;
            }
        }
        return busIndex;
    }
    
    private int obtenerBusMin() {
        int minGanancias = Integer.MAX_VALUE, busIndex = 0;
        for (int i = 0; i < M; i++) {
            int total = 0;
            for (int j = 0; j < 7; j++) {
                total += ventas[i][j];
            }
            if (total < minGanancias) {
                minGanancias = total;
                busIndex = i;
            }
        }
        return busIndex;
    }
    
    private int obtenerDiaMax(int busIndex) {
        int maxVenta = 0, diaIndex = 0;
        for (int j = 0; j < 7; j++) {
            if (ventas[busIndex][j] > maxVenta) {
                maxVenta = ventas[busIndex][j];
                diaIndex = j;
            }
        }
        return diaIndex;
    }
    
    private void aumentarVentas() {
        double totalVentas = 0;
        int totalDias = M * 7;
        
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < 7; j++) {
                totalVentas += ventas[i][j];
            }
        }
        
        double promedio = totalVentas / totalDias;
        
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < 7; j++) {
                if (ventas[i][j] < promedio) {
                    ventas[i][j] *= 1.2;
                }
            }
        }
        
        JOptionPane.showMessageDialog(frame, "Ventas aumentadas en un 20% para valores por debajo del promedio.");
    }
    
    public static void main(String[] args) {
        int M = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la cantidad de buses"));
        new BusSalesGUI(M);
    }
}
