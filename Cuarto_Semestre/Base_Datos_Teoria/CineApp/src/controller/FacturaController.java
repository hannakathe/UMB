package controller;

import model.Factura;
import model.DetalleFactura;
import service.FacturaDAO;
import service.DetalleFacturaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de las facturas.
 * Se encarga de coordinar las operaciones relacionadas con las facturas
 * y sus detalles, comunicándose con los respectivos DAO.
 */
public class FacturaController {
    // DAO para manejar la tabla de facturas
    private FacturaDAO facturaDAO;
    // DAO para manejar la tabla de detalles de factura
    private DetalleFacturaDAO detalleDAO;

    /**
     * Constructor que inicializa el controlador con una conexión
     * y crea las instancias de los DAO correspondientes.
     * @param con conexión activa a la base de datos
     */
    public FacturaController(Connection con) {
        facturaDAO = new FacturaDAO(con);
        detalleDAO = new DetalleFacturaDAO(con);
    }

    /**
     * Crea una nueva factura en la base de datos.
     * @param clienteDoc documento del cliente asociado a la factura
     * @param valorTotal valor total de la factura
     * @param datosEmpresa información de la empresa emisora
     * @return el id generado para la factura insertada
     * @throws Exception si ocurre un error durante la operación
     */
    public int crearFactura(int clienteDoc, double valorTotal, String datosEmpresa) throws Exception {
        Factura f = new Factura();
        f.setClienteDocumento(clienteDoc);
        f.setValorTotal(valorTotal);
        f.setDatosEmpresa(datosEmpresa);
        return facturaDAO.insertar(f); // Inserta la factura en la BD
    }

    /**
     * Agrega un detalle a una factura existente.
     * @param facturaId id de la factura a la que se agregará el detalle
     * @param entradaId id de la entrada asociada
     * @param cantidad número de entradas
     * @param valorUnitario precio unitario de cada entrada
     * @throws Exception si ocurre un error en la inserción
     */
    public void agregarDetalle(int facturaId, int entradaId, int cantidad, double valorUnitario) throws Exception {
        DetalleFactura d = new DetalleFactura();
        d.setFacturaId(facturaId);
        d.setEntradaId(entradaId);
        d.setCantidad(cantidad);
        d.setValorUnitario(valorUnitario);
        // Calcula el valor total del detalle como cantidad * valor unitario
        d.setValorTotal(cantidad * valorUnitario);
        detalleDAO.insertar(d); // Inserta el detalle en la BD
    }

    /**
     * Lista todas las facturas registradas en la base de datos.
     * @return lista de facturas
     * @throws Exception si ocurre un error en la consulta
     */
    public List<Factura> listar() throws Exception {
        return facturaDAO.listar();
    }

    /**
     * Actualiza los datos de una factura existente.
     * @param id identificador de la factura
     * @param clienteDoc documento del cliente
     * @param valorTotal nuevo valor total
     * @param datosEmpresa nueva información de la empresa
     * @throws SQLException si ocurre un error en la actualización
     */
    public void actualizar(int id, int clienteDoc, double valorTotal, String datosEmpresa) throws SQLException {
        facturaDAO.actualizar(new Factura(id, clienteDoc, valorTotal, datosEmpresa));
    }

    /**
     * Elimina una factura de la base de datos por su id.
     * @param id identificador de la factura a eliminar
     * @throws SQLException si ocurre un error en la BD
     */
    public void eliminar(int id) throws SQLException {
        facturaDAO.eliminar(id);
    }
}
