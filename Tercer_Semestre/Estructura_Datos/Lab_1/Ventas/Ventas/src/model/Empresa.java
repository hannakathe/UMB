package model;

import java.util.Random;

public class Empresa {
    private final double[][] ventas;
    private static final int SEDES = 6;
    private static final int DIAS = 7;

    public Empresa() {
        ventas = new double[SEDES][DIAS];
        llenarVentas();
    }

    private void llenarVentas() {
        Random rand = new Random();
        for (int i = 0; i < SEDES; i++) {
            for (int j = 0; j < DIAS; j++) {
                ventas[i][j] = rand.nextInt(4501) + 500; // Ventas entre 500 y 5000
            }
        }
    }

    public double[][] getVentas() {
        return ventas;
    }

    public int getSedes() {
        return SEDES;
    }

    public int getDias() {
        return DIAS;
    }

    public void mostrarVentas() {
        System.out.println("Ventas por sede y día:");
        for (int i = 0; i < SEDES; i++) {
            System.out.print("Sede " + (i + 1) + ": ");
            for (int j = 0; j < DIAS; j++) {
                System.out.print(ventas[i][j] + " ");
            }
            System.out.println();
        }
    }
}

