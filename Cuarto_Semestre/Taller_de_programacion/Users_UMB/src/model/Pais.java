package model;

public class Pais {
    private int codPais;
    private String nombrePais;

    public Pais(int codPais, String nombrePais) {
        this.codPais = codPais;
        this.nombrePais = nombrePais;
    }

    public int getCodPais() { return codPais; }
    public String getNombrePais() { return nombrePais; }
}
