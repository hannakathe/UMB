package model;

// Clase que representa el detalle de una factura (cada línea o concepto facturado)
public class DetalleFactura {
    // Identificador único del detalle
    private int id;
    // ID de la factura a la que pertenece este detalle
    private int facturaId;
    // ID de la entrada asociada a este detalle
    private int entradaId;
    // Cantidad de entradas compradas
    private int cantidad;
    // Precio unitario de cada entrada
    private double valorUnitario;
    // Valor total del detalle (cantidad * valorUnitario)
    private double valorTotal;

    // Constructor vacío (permite crear el objeto sin inicializar atributos)
    public DetalleFactura() {}

    // Constructor con parámetros (se calcula automáticamente el valor total)
    public DetalleFactura(int facturaId, int entradaId, int cantidad, double valorUnitario) {
        this.facturaId = facturaId; 
        this.entradaId = entradaId; 
        this.cantidad = cantidad; 
        this.valorUnitario = valorUnitario; 
        this.valorTotal = cantidad * valorUnitario; // cálculo automático
    }

    // Métodos getter y setter para acceder y modificar los atributos

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
