package model;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;

    public Pelicula() {}
    public Pelicula(int id, String titulo, String genero) { this.id = id; this.titulo = titulo; this.genero = genero; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
}
