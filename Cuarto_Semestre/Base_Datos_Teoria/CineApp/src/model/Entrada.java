package model;

import java.time.LocalDateTime;

public class Entrada {
    private int id;
    private int clienteDocumento;
    private int funcionId;
    private int asientoId;
    private double valor;
    private LocalDateTime fechaCompra;

    public Entrada() {}
    public Entrada(int id, int clienteDocumento, int funcionId, int asientoId, double valor) {
        this.id = id; this.clienteDocumento = clienteDocumento; this.funcionId = funcionId; this.asientoId = asientoId; this.valor = valor;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getClienteDocumento() { return clienteDocumento; }
    public void setClienteDocumento(int clienteDocumento) { this.clienteDocumento = clienteDocumento; }
    public int getFuncionId() { return funcionId; }
    public void setFuncionId(int funcionId) { this.funcionId = funcionId; }
    public int getAsientoId() { return asientoId; }
    public void setAsientoId(int asientoId) { this.asientoId = asientoId; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public LocalDateTime getFechaCompra() { return fechaCompra; }
    public void setFechaCompra(LocalDateTime fechaCompra) { this.fechaCompra = fechaCompra; }
}
