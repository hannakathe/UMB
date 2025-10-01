package model;

public class Cliente {
    private int documento;
    private String nombre;
    private String telefono;

    public Cliente() {}

    public Cliente(int documento, String nombre, String telefono) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public int getDocumento() { return documento; }
    public void setDocumento(int documento) { this.documento = documento; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
