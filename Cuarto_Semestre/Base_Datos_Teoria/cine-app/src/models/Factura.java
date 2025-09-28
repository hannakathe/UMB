package models;

import java.math.BigDecimal;
import java.sql.Date;

public class Factura {
    private int id;
    private int clienteId;
    private String empresaNombre;
    private String empresaDocumento;
    private String contacto;
    private Date fecha;
    private String tipoPago;
    private BigDecimal valorTotal;

    // Constructor vacío
    public Factura() {
    }

    // Constructor con todos los atributos
    public Factura(int id, int clienteId, String empresaNombre, String empresaDocumento,
                   String contacto, Date fecha, String tipoPago, BigDecimal valorTotal) {
        this.id = id;
        this.clienteId = clienteId;
        this.empresaNombre = empresaNombre;
        this.empresaDocumento = empresaDocumento;
        this.contacto = contacto;
        this.fecha = fecha;
        this.tipoPago = tipoPago;
        this.valorTotal = valorTotal;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }

    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }

    public String getEmpresaDocumento() {
        return empresaDocumento;
    }

    public void setEmpresaDocumento(String empresaDocumento) {
        this.empresaDocumento = empresaDocumento;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", empresaNombre='" + empresaNombre + '\'' +
                ", empresaDocumento='" + empresaDocumento + '\'' +
                ", contacto='" + contacto + '\'' +
                ", fecha=" + fecha +
                ", tipoPago='" + tipoPago + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
