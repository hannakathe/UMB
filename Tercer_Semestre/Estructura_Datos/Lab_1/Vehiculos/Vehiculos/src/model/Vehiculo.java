package model;

public class Vehiculo {
    // Declaración de los atributos de la clase Vehiculo
    private String marca;  // Atributo para almacenar la marca del vehículo
    private double[] precios;  // Atributo para almacenar los precios en un arreglo

    // Constructor de la clase Vehiculo. Inicializa el atributo marca y un arreglo de precios.
    public Vehiculo(String marca) {
        this.marca = marca;  // Inicializa la marca del vehículo con el valor pasado como parámetro
        this.precios = new double[5];  // Crea un arreglo de 5 elementos para los precios (uno por cada año desde 2019)
    }

    // Método getter para obtener la marca del vehículo
    public String getMarca() {
        return marca;  // Devuelve el valor del atributo marca
    }

    // Método setter para establecer el precio de un año específico
    public void setPrecio(int year, double precio) {
        if (precio > 0) {  // Verifica que el precio sea positivo
            this.precios[year - 2019] = precio;  // Asigna el precio al índice correspondiente en el arreglo de precios
        } else {
            // Si el precio no es positivo, lanza una excepción indicando que solo se permiten valores positivos
            throw new IllegalArgumentException("Solo se permiten valores positivos.");
        }
    }

    // Método getter para obtener el precio de un año específico
    public double getPrecio(int year) {
        return this.precios[year - 2019];  // Devuelve el precio del año solicitado
    }

    // Método getter para obtener todos los precios
    public double[] getPrecios() {
        return precios;  // Devuelve el arreglo completo de precios
    }
}
