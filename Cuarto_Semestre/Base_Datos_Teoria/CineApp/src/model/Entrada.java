package model;

import java.time.LocalDateTime;

// Clase que representa una entrada (boleto) comprada por un cliente
public class Entrada {
    // Identificador único de la entrada
    private int id;
    // Documento del cliente que compra la entrada
    private int clienteDocumento;
    // Identificador de la función a la que corresponde la entrada
    private int funcionId;
    // Identificador del asiento asignado a la entrada
    private int asientoId;
    // Precio pagado por la entrada
    private double valor;
    // Fecha y hora en la que se realizó la compra
    private LocalDateTime fechaCompra;

    // Constructor vacío (permite instanciar sin inicializar datos)
    public Entrada() {}

    // Constructor con parámetros (sin incluir fecha de compra)
    public Entrada(int id, int clienteDocumento, int funcionId, int asientoId, double valor) {
        this.id = id; 
        this.clienteDocumento = clienteDocumento; 
        this.funcionId = funcionId; 
        this.asientoId = asientoId; 
        this.valor = valor;
    }

    // Métodos getter y setter para acceder y modificar los atributos

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
