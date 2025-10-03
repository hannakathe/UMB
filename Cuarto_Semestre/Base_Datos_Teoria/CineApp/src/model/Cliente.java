package model;

// Clase que representa un cliente del cine
public class Cliente {
    // Documento de identificación del cliente
    private int documento;
    // Nombre completo del cliente
    private String nombre;
    // Número de teléfono del cliente
    private String telefono;

    // Constructor vacío (permite crear el objeto sin inicializar atributos)
    public Cliente() {}

    // Constructor con parámetros (permite crear el objeto directamente con datos)
    public Cliente(int documento, String nombre, String telefono) {
        this.documento = documento;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    // Métodos getter y setter para acceder y modificar los atributos

    // Devuelve el documento del cliente
    public int getDocumento() { return documento; }
    // Asigna el documento del cliente
    public void setDocumento(int documento) { this.documento = documento; }

    // Devuelve el nombre del cliente
    public String getNombre() { return nombre; }
    // Asigna el nombre del cliente
    public void setNombre(String nombre) { this.nombre = nombre; }

    // Devuelve el teléfono del cliente
    public String getTelefono() { return telefono; }
    // Asigna el teléfono del cliente
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
