package generadores;

import javax.swing.JOptionPane;

public class MultiplosDeTres {
    public static void generadorMultiplo() {
        // Pedir la cantidad de múltiplos a generar
        String inputCantidad = JOptionPane.showInputDialog("Ingresa el número de múltiplos de 3 a generar: ");
        int cantidad = Integer.parseInt(inputCantidad); // Convertir el valor ingresado a entero

        // Generar y mostrar los múltiplos de 3
        StringBuilder resultado = new StringBuilder();
        for (int i = 1; i <= cantidad; i++) {
            resultado.append("Múltiplo #").append(i).append(" de 3: ").append(i * 3).append("\n");
        }

        // Mostrar el resultado en un cuadro de mensaje
        JOptionPane.showMessageDialog(null, resultado.toString(), "Múltiplos de 3", JOptionPane.INFORMATION_MESSAGE);
    }
}
