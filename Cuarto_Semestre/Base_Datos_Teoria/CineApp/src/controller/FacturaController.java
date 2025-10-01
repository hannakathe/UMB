package controller;

import model.Factura;
import model.DetalleFactura;
import service.FacturaDAO;
import service.DetalleFacturaDAO;
import java.sql.Connection;
import java.util.List;

public class FacturaController {
    private FacturaDAO facturaDAO;
    private DetalleFacturaDAO detalleDAO;

    public FacturaController(Connection con) {
        facturaDAO = new FacturaDAO(con);
        detalleDAO = new DetalleFacturaDAO(con);
    }

    public int crearFactura(int clienteDoc, double valorTotal, String datosEmpresa) throws Exception {
        Factura f = new Factura();
        f.setClienteDocumento(clienteDoc);
        f.setValorTotal(valorTotal);
        f.setDatosEmpresa(datosEmpresa);
        return facturaDAO.insertar(f);
    }

    public void agregarDetalle(int facturaId, int entradaId, int cantidad, double valorUnitario) throws Exception {
        DetalleFactura d = new DetalleFactura();
        d.setFacturaId(facturaId);
        d.setEntradaId(entradaId);
        d.setCantidad(cantidad);
        d.setValorUnitario(valorUnitario);
        d.setValorTotal(cantidad * valorUnitario);
        detalleDAO.insertar(d);
    }

    public List<model.Factura> listar() throws Exception { return facturaDAO.listar(); }
}
