package model;

// Clase que representa un asiento dentro de una sala de cine
public class Asiento {
    // Identificador único del asiento
    private int id;
    // Identificador de la sala a la que pertenece el asiento
    private int salaId;
    // Número o código que identifica la silla dentro de la sala (ej: A1, B2, etc.)
    private String numeroSilla;
    // Indica si el asiento está disponible (true) o no (false)
    private boolean disponible;

    // Constructor vacío
    public Asiento() {}

    // Constructor con parámetros para inicializar todos los atributos
    public Asiento(int id, int salaId, String numeroSilla, boolean disponible) {
        this.id = id; 
        this.salaId = salaId; 
        this.numeroSilla = numeroSilla; 
        this.disponible = disponible;
    }

    // Métodos getter y setter para acceder y modificar los atributos

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSalaId() { return salaId; }
    public void setSalaId(int salaId) { this.salaId = salaId; }

    public String getNumeroSilla() { return numeroSilla; }
    public void setNumeroSilla(String numeroSilla) { this.numeroSilla = numeroSilla; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
