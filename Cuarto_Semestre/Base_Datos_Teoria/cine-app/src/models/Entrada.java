package models;
import java.math.BigDecimal;
import java.sql.Date;
public class Entrada {
    private int id;
    private int asientoId;
    private int funcionId;
    private int clienteId;
    private BigDecimal valor;
    private String tipoPago;
    private Date fechaCompra;
    // constructors + getters/setters
    public Entrada(){}


    public Entrada(int id,int asientoId,int funcionId,int clienteId,BigDecimal valor,String tipoPago,Date fechaCompra){
        this.id=id;this.asientoId=asientoId;this.funcionId=funcionId;this.clienteId=clienteId;this.valor=valor;this.tipoPago=tipoPago;this.fechaCompra=fechaCompra;
    }
    public Entrada(int asientoId,int funcionId,int clienteId,BigDecimal valor,String tipoPago,Date fechaCompra){
        this.asientoId=asientoId;this.funcionId=funcionId;this.clienteId=clienteId;this.valor=valor;this.tipoPago=tipoPago;this.fechaCompra=fechaCompra;
    }
    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public int getAsientoId(){return asientoId;}
    public void setAsientoId(int asientoId){this.asientoId=asientoId;}
    public int getFuncionId(){return funcionId;}
    public void setFuncionId(int funcionId){this.funcionId=funcionId;}
    public int getClienteId(){return clienteId;}
    public void setClienteId(int clienteId){this.clienteId=clienteId;}
    public BigDecimal getValor(){return valor;}
    public void setValor(BigDecimal valor){this.valor=valor;}
    public String getTipoPago(){return tipoPago;}
    public void setTipoPago(String tipoPago){this.tipoPago=tipoPago;}
    public Date getFechaCompra(){return fechaCompra;}
    public void setFechaCompra(Date fechaCompra){this.fechaCompra=fechaCompra;}
}