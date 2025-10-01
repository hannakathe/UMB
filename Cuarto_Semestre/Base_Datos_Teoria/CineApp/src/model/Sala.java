package model;

public class Sala {
    private int id;
    private String tipoSala;
    private int capacidad;

    public Sala() {}
    public Sala(int id, String tipoSala, int capacidad) { this.id = id; this.tipoSala = tipoSala; this.capacidad = capacidad; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTipoSala() { return tipoSala; }
    public void setTipoSala(String tipoSala) { this.tipoSala = tipoSala; }
    public int getCapacidad() { return capacidad; }
    public void setCapacidad(int capacidad) { this.capacidad = capacidad; }
}
