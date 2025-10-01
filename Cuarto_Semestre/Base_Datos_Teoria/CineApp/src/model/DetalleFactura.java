package model;

public class DetalleFactura {
    private int id;
    private int facturaId;
    private int entradaId;
    private int cantidad;
    private double valorUnitario;
    private double valorTotal;

    public DetalleFactura() {}
    public DetalleFactura(int facturaId, int entradaId, int cantidad, double valorUnitario) {
        this.facturaId = facturaId; this.entradaId = entradaId; this.cantidad = cantidad; this.valorUnitario = valorUnitario; 
        this.valorTotal = cantidad * valorUnitario;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFacturaId() { return facturaId; }
    public void setFacturaId(int facturaId) { this.facturaId = facturaId; }
    public int getEntradaId() { return entradaId; }
    public void setEntradaId(int entradaId) { this.entradaId = entradaId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getValorUnitario() { return valorUnitario; }
    public void setValorUnitario(double valorUnitario) { this.valorUnitario = valorUnitario; }
    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }
}
