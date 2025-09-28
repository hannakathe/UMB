package models;

import java.math.BigDecimal;

public class DetalleFactura {
    private int id;
    private int facturaId;
    private int entradaId;
    private int cantidad;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    // Constructor vacío
    public DetalleFactura() {
    }

    // Constructor con parámetros
    public DetalleFactura(int id, int facturaId, int entradaId, int cantidad, BigDecimal valorUnitario) {
        this.id = id;
        this.facturaId = facturaId;
        this.entradaId = entradaId;
        this.cantidad = cantidad;
        this.valorUnitario = valorUnitario;
        calcularValorTotal();
    }

    // Getters y setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public void setFacturaId(int facturaId) {
        this.facturaId = facturaId;
    }

    public int getEntradaId() {
        return entradaId;
    }

    public void setEntradaId(int entradaId) {
        this.entradaId = entradaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularValorTotal(); // recalcular al cambiar cantidad
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
        calcularValorTotal(); // recalcular al cambiar el valor unitario
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método para calcular valor total automáticamente
    private void calcularValorTotal() {
        if (valorUnitario != null && cantidad > 0) {
            this.valorTotal = valorUnitario.multiply(BigDecimal.valueOf(cantidad));
        } else {
            this.valorTotal = BigDecimal.ZERO;
        }
    }

    @Override
    public String toString() {
        return "DetalleFactura{" +
                "id=" + id +
                ", facturaId=" + facturaId +
                ", entradaId=" + entradaId +
                ", cantidad=" + cantidad +
                ", valorUnitario=" + valorUnitario +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
