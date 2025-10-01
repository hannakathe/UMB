package model;

import java.time.LocalDateTime;

public class Factura {
    private int id;
    private int clienteDocumento;
    private LocalDateTime fecha;
    private double valorTotal;
    private String datosEmpresa;

    public Factura() {}
    public Factura(int id, int clienteDocumento, double valorTotal, String datosEmpresa) {
        this.id = id; this.clienteDocumento = clienteDocumento; this.valorTotal = valorTotal; this.datosEmpresa = datosEmpresa;
    }

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
