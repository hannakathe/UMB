package model;

import java.time.LocalDateTime;

// Clase que representa una función de cine (proyección de una película en una sala en un horario específico)
public class Funcion {
    // Identificador único de la función
    private int id;
    // Identificador de la película que se proyecta en la función
    private int peliculaId;
    // Identificador de la sala donde se realiza la función
    private int salaId;
    // Fecha y hora programada para la función
    private LocalDateTime fechaHora;

    // Constructor vacío
    public Funcion() {}

    // Constructor con parámetros (inicializa todos los atributos)
    public Funcion(int id, int peliculaId, int salaId, LocalDateTime fechaHora) {
        this.id = id; 
        this.peliculaId = peliculaId; 
        this.salaId = salaId; 
        this.fechaHora = fechaHora;
    }

    // Métodos getter y setter para acceder y modificar los atributos

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPeliculaId() { return peliculaId; }
    public void setPeliculaId(int peliculaId) { this.peliculaId = peliculaId; }

    public int getSalaId() { return salaId; }
    public void setSalaId(int salaId) { this.salaId = salaId; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
