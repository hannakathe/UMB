package model;

// Clase que representa una película dentro del sistema de cine
public class Pelicula {
    // Identificador único de la película
    private int id;
    // Título de la película
    private String titulo;
    // Género de la película (ejemplo: acción, comedia, drama, etc.)
    private String genero;

    // Constructor vacío (permite crear un objeto sin inicializar atributos)
    public Pelicula() {}

    // Constructor con parámetros (permite inicializar todos los atributos de la película)
    public Pelicula(int id, String titulo, String genero) { 
        this.id = id; 
        this.titulo = titulo; 
        this.genero = genero; 
    }

    // Métodos getter y setter para acceder y modificar los atributos

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}
