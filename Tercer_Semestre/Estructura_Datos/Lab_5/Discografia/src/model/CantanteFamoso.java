// Define el paquete al que pertenece la clase (organización de código)
package model;

// Declaración de la clase pública 'CantanteFamoso'
public class CantanteFamoso {
    // Atributos privados de la clase (encapsulamiento)
    private String nombre;               // Almacena el nombre del cantante
    private String discoConMasVentas;    // Almacena el nombre del disco más vendido
    private int totalDeVentas;           // Almacena el número total de ventas del disco

    // Constructor de la clase (inicializa el objeto cuando se crea)
    public CantanteFamoso(String nombre, String discoConMasVentas, int totalDeVentas) {
        this.nombre = nombre;                     // Asigna el nombre recibido al atributo
        this.discoConMasVentas = discoConMasVentas; // Asigna el disco recibido al atributo
        this.totalDeVentas = totalDeVentas;       // Asigna las ventas recibidas al atributo
    }

    // --- MÉTODOS GETTER Y SETTER ---
    // (Permiten acceder y modificar los atributos privados de forma controlada)

    // Getter para 'nombre' (devuelve el valor actual)
    public String getNombre() {
        return nombre;
    }

    // Setter para 'nombre' (modifica el valor)
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Getter para 'discoConMasVentas'
    public String getDiscoConMasVentas() {
        return discoConMasVentas;
    }

    // Setter para 'discoConMasVentas'
    public void setDiscoConMasVentas(String discoConMasVentas) {
        this.discoConMasVentas = discoConMasVentas;
    }

    // Getter para 'totalDeVentas'
    public int getTotalDeVentas() {
        return totalDeVentas;
    }

    // Setter para 'totalDeVentas'
    public void setTotalDeVentas(int totalDeVentas) {
        this.totalDeVentas = totalDeVentas;
    }

    // Sobrescribe el método toString() de la clase Object
    // (Proporciona una representación en String del objeto)
    @Override
    public String toString() {
        // Ejemplo de salida: "Shakira - Laundry Service (20 ventas)"
        return nombre + " - " + discoConMasVentas + " (" + totalDeVentas + " ventas)";
    }
}