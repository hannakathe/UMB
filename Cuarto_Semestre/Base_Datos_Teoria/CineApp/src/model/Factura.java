package model;

import java.time.LocalDateTime;

// Clase que representa una factura generada por la compra de entradas
public class Factura {
    // Identificador único de la factura
    private int id;
    // Documento del cliente asociado a la factura
    private int clienteDocumento;
    // Fecha y hora en la que se emite la factura
    private LocalDateTime fecha;
    // Valor total a pagar en la factura
    private double valorTotal;
    // Información de la empresa emisora de la factura (nombre, NIT, etc.)
    private String datosEmpresa;

    // Constructor vacío (permite instanciar sin inicializar atributos)
    public Factura() {}

    // Constructor con parámetros (sin incluir fecha)
    public Factura(int id, int clienteDocumento, double valorTotal, String datosEmpresa) {
        this.id = id; 
        this.clienteDocumento = clienteDocumento; 
        this.valorTotal = valorTotal; 
        this.datosEmpresa = datosEmpresa;
    }

    // Métodos getter y setter para acceder y modificar los atributos

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getClienteDocumento() { return clienteDocumento; }
    public void setClienteDocumento(int clienteDocumento) { this.clienteDocumento = clienteDocumento; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getDatosEmpresa() { return datosEmpresa; }
    public void setDatosEmpresa(String datosEmpresa) { this.datosEmpresa = datosEmpresa; }
}
