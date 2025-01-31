package otros;

import javax.swing.*;

public class ImprimirArreglo {
    public static <T> void imprimirArreglo(T[] arreglo) {
        StringBuilder sb = new StringBuilder();
        for (T elemento : arreglo) {
            sb.append(elemento).append(" ");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Arreglo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Integer[] ingresarArregloEnteros(int tamanio) {
        Integer[] arreglo = new Integer[tamanio];
        for (int i = 0; i < tamanio; i++) {
            String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo:");
            arreglo[i] = Integer.parseInt(input);
        }
        return arreglo;
    }

    public static String[] ingresarArregloCadenas(int tamanio) {
        String[] arreglo = new String[tamanio];
        for (int i = 0; i < tamanio; i++) {
            arreglo[i] = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo:");
        }
        return arreglo;
    }

    public static Boolean[] ingresarArregloBooleanos(int tamanio) {
        Boolean[] arreglo = new Boolean[tamanio];
        for (int i = 0; i < tamanio; i++) {
            String input = JOptionPane.showInputDialog(null, "Ingrese el elemento " + (i + 1) + " del arreglo (true/false):");
            arreglo[i] = Boolean.parseBoolean(input);
        }
        return arreglo;
    }
}
