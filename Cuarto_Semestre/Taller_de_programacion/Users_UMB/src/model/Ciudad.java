package model;

public class Ciudad {
    private int codCiudad;
    private String nombreCiudad;
    private int codPais;

    public Ciudad(int codCiudad, String nombreCiudad, int codPais) {
        this.codCiudad = codCiudad;
        this.nombreCiudad = nombreCiudad;
        this.codPais = codPais;
    }

    public int getCodCiudad() { return codCiudad; }
    public String getNombreCiudad() { return nombreCiudad; }
    public int getCodPais() { return codPais; }
}
