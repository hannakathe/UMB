package model;

import java.time.LocalDateTime;

public class Funcion {
    private int id;
    private int peliculaId;
    private int salaId;
    private LocalDateTime fechaHora;

    public Funcion() {}
    public Funcion(int id, int peliculaId, int salaId, LocalDateTime fechaHora) {
        this.id = id; this.peliculaId = peliculaId; this.salaId = salaId; this.fechaHora = fechaHora;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getPeliculaId() { return peliculaId; }
    public void setPeliculaId(int peliculaId) { this.peliculaId = peliculaId; }
    public int getSalaId() { return salaId; }
    public void setSalaId(int salaId) { this.salaId = salaId; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
