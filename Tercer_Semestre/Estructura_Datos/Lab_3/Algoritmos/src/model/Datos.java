package model;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class Datos {
    private int[] numeros;

    public Datos(int cantidad) {
        this.numeros = generarNumerosAleatoriosUnicos(cantidad);
    }

    public int[] getNumeros() {
        return numeros;
    }

    private int[] generarNumerosAleatoriosUnicos(int cantidad) {
        Set<Integer> numerosUnicos = new LinkedHashSet<>();
        Random rand = new Random();

        while (numerosUnicos.size() < cantidad) {
            int numAleatorio = rand.nextInt(cantidad * 100); // Asegura un rango más grande para evitar colisiones
            numerosUnicos.add(numAleatorio);
        }

        // Convertimos el Set a un array sin perder el orden
        int[] resultado = new int[cantidad];
        int index = 0;
        for (int num : numerosUnicos) {
            resultado[index++] = num;
        }

        return resultado;
    }
}
