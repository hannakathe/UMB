package model;

public class Asiento {
    private int id;
    private int salaId;
    private String numeroSilla;
    private boolean disponible;

    public Asiento() {}
    public Asiento(int id, int salaId, String numeroSilla, boolean disponible) {
        this.id = id; this.salaId = salaId; this.numeroSilla = numeroSilla; this.disponible = disponible;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getSalaId() { return salaId; }
    public void setSalaId(int salaId) { this.salaId = salaId; }
    public String getNumeroSilla() { return numeroSilla; }
    public void setNumeroSilla(String numeroSilla) { this.numeroSilla = numeroSilla; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
