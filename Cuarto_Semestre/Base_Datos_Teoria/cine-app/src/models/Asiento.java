package models;
public class Asiento {
    private int id;
    private int salaId;
    private String numeroSilla;
    public Asiento(){}
    public Asiento(int id,int salaId,String numeroSilla){this.id=id;this.salaId=salaId;this.numeroSilla=numeroSilla;}
    // getters/setters
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public int getSalaId(){return salaId;}
    public void setSalaId(int salaId){this.salaId=salaId;}
    public String getNumeroSilla(){return numeroSilla;}
    public void setNumeroSilla(String numeroSilla){this.numeroSilla=numeroSilla;}
}
