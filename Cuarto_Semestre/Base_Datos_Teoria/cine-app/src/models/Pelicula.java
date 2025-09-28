package models;

import java.math.BigDecimal;

public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private BigDecimal valorBoleta;

    public Pelicula() {}
    public Pelicula(int id, String titulo, String genero, BigDecimal valorBoleta) {
        this.id = id; this.titulo = titulo; this.genero = genero; this.valorBoleta = valorBoleta;
    }
    public Pelicula(String titulo, String genero, BigDecimal valorBoleta) {
        this.titulo = titulo; this.genero = genero; this.valorBoleta = valorBoleta;
    }
    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public BigDecimal getValorBoleta() { return valorBoleta; }
    public void setValorBoleta(BigDecimal valorBoleta) { this.valorBoleta = valorBoleta; }
}
