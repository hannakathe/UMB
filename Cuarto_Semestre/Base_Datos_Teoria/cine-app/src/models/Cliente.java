package models;

public class Cliente {
    private int id;
    private String nombre;
    private String documento;
    private String telefono;

    public Cliente() {}
    public Cliente(int id, String nombre, String documento, String telefono) {
        this.id = id; this.nombre = nombre; this.documento = documento; this.telefono = telefono;
    }
    public Cliente(String nombre, String documento, String telefono) {
        this.nombre = nombre; this.documento = documento; this.telefono = telefono;
    }
    // getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
