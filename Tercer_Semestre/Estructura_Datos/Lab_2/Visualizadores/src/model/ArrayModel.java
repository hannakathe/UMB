// Definimos el paquete donde se encuentra esta clase. 
// En este caso, la clase pertenece al paquete "model".
package model;

// Declaramos la clase "ArrayModel", que se encargará de manejar diferentes tipos de arrays.
public class ArrayModel {

    // Declaramos un array unidimensional (vector). 
    // Este almacena una lista de valores enteros (int) en una sola fila.
    private int[] unidimensional;

    // Declaramos un array bidimensional (matriz).
    // Un array bidimensional es una matriz de valores enteros organizada en filas y columnas.
    private int[][] bidimensional;

    // Declaramos un array tridimensional.
    // Este es un conjunto de matrices, es decir, una estructura de datos que almacena múltiples matrices dentro de un solo array.
    private int[][][] tridimensional;

    // MÉTODOS GETTERS Y SETTERS:
    // Estos métodos permiten acceder y modificar los valores de las variables privadas.

    // Getter para obtener el array unidimensional.
    public int[] getUnidimensional() { 
        return unidimensional; 
    }

    // Setter para asignar un nuevo valor al array unidimensional.
    public void setUnidimensional(int[] unidimensional) { 
        this.unidimensional = unidimensional; 
    }

    // Getter para obtener el array bidimensional.
    public int[][] getBidimensional() { 
        return bidimensional; 
    }

    // Setter para asignar un nuevo valor al array bidimensional.
    public void setBidimensional(int[][] bidimensional) { 
        this.bidimensional = bidimensional; 
    }

    // Getter para obtener el array tridimensional.
    public int[][][] getTridimensional() { 
        return tridimensional; 
    }

    // Setter para asignar un nuevo valor al array tridimensional.
    public void setTridimensional(int[][][] tridimensional) { 
        this.tridimensional = tridimensional; 
    }
}
