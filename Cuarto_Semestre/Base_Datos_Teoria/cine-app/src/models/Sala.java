package models;
public class Sala {
    private int id;
    private String tipoSala;
    public Sala() {}
    public Sala(int id, String tipoSala){this.id=id;this.tipoSala=tipoSala;}
    public Sala(String tipoSala){this.tipoSala=tipoSala;}
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getTipoSala(){return tipoSala;}
    public void setTipoSala(String tipoSala){this.tipoSala=tipoSala;}
}
