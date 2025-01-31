import generadores.NumerosPares;
import generadores.MultiplosDeTres;
import generadores.SumaMultiplos;
import otros.RecorrerMatrizInversa;
import otros.ImprimirArreglo;


import javax.swing.JOptionPane;

public class MainTA {
    public static void main(String[] args) throws Exception {
        System.out.println("Actividad de trabajo autonomo...");
        System.out.println("");

        System.out.println("---- Recorrer Matriz Inversa ----");
        System.out.println("");

        RecorrerMatrizInversa matrizInversa = new RecorrerMatrizInversa();
        matrizInversa.imprimirMatrizInversa();

        System.out.println("---- Generador numeros pares entre p y q ----");
        System.out.println("");

        NumerosPares.generadorPares();

        System.out.println("---- Generador multiplos de 3 ----");
        System.out.println("");

        MultiplosDeTres.generadorMultiplo();

        System.out.println("---- Generador multiplos de 3 ----");
        System.out.println("");

        SumaMultiplos.generarSuma();

        System.out.println("---- Ingresar y Imprimir Arreglos Genéricos ----");
        System.out.println("");

        boolean continuar = true;
        while (continuar) {
            // Permitir que el usuario elija el tipo de arreglo o salir
            String[] opciones = {"Enteros", "Cadenas", "Booleanos", "Salir"};
            String tipoArreglo = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione el tipo de arreglo que desea ingresar:",
                    "Tipo de Arreglo",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (tipoArreglo.equals("Salir")) {
                continuar = false;
            } else {
                // Obtener el tamaño del arreglo
                String tamanioInput = JOptionPane.showInputDialog(null, "Ingrese el tamaño del arreglo:");
                int tamanio = Integer.parseInt(tamanioInput);

                // Ingresar y imprimir el arreglo según el tipo seleccionado
                switch (tipoArreglo) {
                    case "Enteros":
                        Integer[] numeros = ImprimirArreglo.ingresarArregloEnteros(tamanio);
                        ImprimirArreglo.imprimirArreglo(numeros);
                        break;
                    case "Cadenas":
                        String[] cadenas = ImprimirArreglo.ingresarArregloCadenas(tamanio);
                        ImprimirArreglo.imprimirArreglo(cadenas);
                        break;
                    case "Booleanos":
                        Boolean[] booleanos = ImprimirArreglo.ingresarArregloBooleanos(tamanio);
                        ImprimirArreglo.imprimirArreglo(booleanos);
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Tipo de arreglo no soportado.");
                        break;
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Gracias por usar el programa. ¡Adiós!");
    }
}
