package models;

// Clase que representa un modelo de datos para un sistema de gestión de cine
public class CinemaxModel {
    // Atributos de la clase
    private int semana;         // Número de la semana
    private int sala;           // Número de la sala
    private String pelicula;    // Nombre de la película
    private int espectadores;   // Cantidad de espectadores

    // Constructor de la clase
    public CinemaxModel(int semana, int sala, String pelicula, int espectadores) {
        this.semana = semana;
        this.sala = sala;
        this.pelicula = pelicula;
        this.espectadores = espectadores;
    }

    // Método para obtener el número de la semana
    public int getSemana() {
        return semana;
    }

    // Método para obtener el número de la sala
    public int getSala() {
        return sala;
    }

    // Método para obtener el nombre de la película
    public String getPelicula() {
        return pelicula;
    }

    // Método para obtener la cantidad de espectadores
    public int getEspectadores() {
        return espectadores;
    }
}