package generadores;

import javax.swing.JOptionPane;

public class NumerosPares {
    public static void generadorPares() {
        // Pedir el número inicial
        String inputP = JOptionPane.showInputDialog("Ingresa el número inicial (Entero): ");
        int p = Integer.parseInt(inputP); // Convertir el valor ingresado a entero

        // Pedir el número final
        String inputQ = JOptionPane.showInputDialog("Ingresa el número final (Entero): ");
        int q = Integer.parseInt(inputQ); // Convertir el valor ingresado a entero

        // Generar y mostrar los números pares en el rango
        StringBuilder resultado = new StringBuilder();
        for (int i = p; i <= q; i++) {
            if (i % 2 == 0) {
                resultado.append(i).append(" Es un número par\n");
            }
        }

        // Mostrar el resultado en un cuadro de mensaje
        JOptionPane.showMessageDialog(null, resultado.toString(), "Números Pares", JOptionPane.INFORMATION_MESSAGE);
    }
}
