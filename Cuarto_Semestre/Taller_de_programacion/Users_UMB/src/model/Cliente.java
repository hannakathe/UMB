package model;

public class Cliente {
    private String tipoID;
    private String nroID;
    private String nombres;
    private String correo;
    private String direccion;
    private String celular;
    private int codCiudad;
    private int codPais;

    public Cliente(String tipoID, String nroID, String nombres, String correo, String direccion, String celular, int codCiudad, int codPais) {
        this.tipoID = tipoID;
        this.nroID = nroID;
        this.nombres = nombres;
        this.correo = correo;
        this.direccion = direccion;
        this.celular = celular;
        this.codCiudad = codCiudad;
        this.codPais = codPais;
    }

    // Getters y setters
    public String getTipoID() { return tipoID; }
    public String getNroID() { return nroID; }
    public String getNombres() { return nombres; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public String getCelular() { return celular; }
    public int getCodCiudad() { return codCiudad; }
    public int getCodPais() { return codPais; }
}
