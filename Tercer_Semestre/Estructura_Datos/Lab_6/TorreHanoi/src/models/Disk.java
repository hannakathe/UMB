// Declaración del paquete al que pertenece la clase
package models;

// Definición de la clase Disk (Disco)
public class Disk {
    // Atributo privado que almacena el tamaño del disco
    private int size;

    // Constructor de la clase Disk que recibe el tamaño como parámetro
    public Disk(int size) {
        this.size = size; // Asigna el valor del parámetro 'size' al atributo 'size' de la clase
    }

    // Método getter para obtener el tamaño del disco
    public int getSize() {
        return size; // Retorna el valor del atributo 'size'
    }

    // Sobrescritura del método toString() para representar el disco como una cadena de caracteres
    @Override
    public String toString() {
        // Retorna una cadena compuesta por el carácter '■' repetido 'size' veces
        // Ejemplo: si size=3, retorna "■■■"
        return "■".repeat(size);
    }
}