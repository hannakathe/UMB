package model;

// Clase que representa una Sala dentro del sistema de cine
public class Sala {
    // Atributo que almacena el identificador único de la sala
    private int id;
    // Tipo de sala (por ejemplo: 2D, 3D, IMAX, VIP, etc.)
    private String tipoSala;
    // Capacidad de espectadores que puede albergar la sala
    private int capacidad;

    // Constructor vacío (permite crear objetos sin valores iniciales)
    public Sala() {}

    // Constructor con parámetros para inicializar una sala con valores
    public Sala(int id, String tipoSala, int capacidad) { 
        this.id = id; 
        this.tipoSala = tipoSala; 
        this.capacidad = capacidad; 
    }

    // Métodos getter y setter para acceder y modificar los atributos privados

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTipoSala() { return tipoSala; }
    public void setTipoSala(String tipoSala) { this.tipoSala = tipoSala; }

    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}
