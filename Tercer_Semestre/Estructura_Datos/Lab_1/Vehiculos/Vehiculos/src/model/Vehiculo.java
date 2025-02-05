package model;

public class Vehiculo {
    private String marca;
    private double[] precios;

    public Vehiculo(String marca) {
        this.marca = marca;
        this.precios = new double[5];
    }

    public String getMarca() {
        return marca;
    }

    public void setPrecio(int year, double precio) {
        if (precio > 0) {
            this.precios[year - 2019] = precio;
        } else {
            throw new IllegalArgumentException("Solo se permiten valores positivos.");
        }
    }

    public double getPrecio(int year) {
        return this.precios[year - 2019];
    }

    public double[] getPrecios() {
        return precios;
    }
}

