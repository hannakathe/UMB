package service;

import model.Empresa;

public class EmpresaService {
    private Empresa empresa;

    public EmpresaService(Empresa empresa) {
        this.empresa = empresa;
    }

    public double promedioSemanal() {
        double total = 0;
        double[][] ventas = empresa.getVentas();
        for (int i = 0; i < empresa.getSedes(); i++) {
            for (int j = 0; j < empresa.getDias(); j++) {
                total += ventas[i][j];
            }
        }
        return total / (empresa.getSedes() * empresa.getDias());
    }

    public double[] promedioPorSede() {
        double[][] ventas = empresa.getVentas();
        double[] promedios = new double[empresa.getSedes()];
        for (int i = 0; i < empresa.getSedes(); i++) {
            double suma = 0;
            for (int j = 0; j < empresa.getDias(); j++) {
                suma += ventas[i][j];
            }
            promedios[i] = suma / empresa.getDias();
        }
        return promedios;
    }

    public void diasSobrePromedio() {
        double[][] ventas = empresa.getVentas();
        double[] promedios = promedioPorSede();
        System.out.println("\nDías con ventas sobre el promedio por sede:");
        for (int i = 0; i < empresa.getSedes(); i++) {
            System.out.print("Sede " + (i + 1) + ": ");
            for (int j = 0; j < empresa.getDias(); j++) {
                if (ventas[i][j] > promedios[i]) {
                    System.out.print((j + 1) + " ");
                }
            }
            System.out.println();
        }
    }

    public void aumentarVentasBajas() {
        double[][] ventas = empresa.getVentas();
        double[] promedios = promedioPorSede();
        for (int i = 0; i < empresa.getSedes(); i++) {
            for (int j = 0; j < empresa.getDias(); j++) {
                if (ventas[i][j] < promedios[i]) {
                    ventas[i][j] *= 1.15; // Aumento del 15%
                }
            }
        }
    }
}

