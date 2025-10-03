package controller;

// Importación de la clase Cliente (modelo) y el DAO que maneja la base de datos
import model.Cliente;
import service.ClienteDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de los clientes.
 * Actúa como intermediario entre la interfaz de usuario (UI)
 * y la capa de acceso a datos (DAO).
 */
public class ClienteController {
    // Objeto DAO que se encarga de la comunicación con la base de datos
    private ClienteDAO dao;

    /**
     * Constructor que inicializa el controlador con una conexión
     * y crea la instancia del DAO correspondiente.
     * @param con conexión activa a la base de datos
     */
    public ClienteController(Connection con) {
        this.dao = new ClienteDAO(con);
    }

    /**
     * Inserta un nuevo cliente en la base de datos.
     * @param doc documento de identidad del cliente
     * @param nombre nombre completo del cliente
     * @param tel número de teléfono del cliente
     * @throws Exception si ocurre un error al insertar en la BD
     */
    public void insertar(int doc, String nombre, String tel) throws Exception {
        // Se crea un objeto Cliente con los datos recibidos
        Cliente c = new Cliente(doc, nombre, tel);
        dao.insertar(c); // Llama al DAO para registrar al cliente
    }

    /**
     * Actualiza los datos de un cliente existente en la base de datos.
     * @param doc documento del cliente (identificador único)
     * @param nombre nuevo nombre del cliente
     * @param tel nuevo número de teléfono
     * @throws SQLException si ocurre un error en la BD
     */
    public void actualizar(int doc, String nombre, String tel) throws SQLException {
        Cliente c = new Cliente(doc, nombre, tel);
        dao.actualizar(c); // Llama al DAO para actualizar la información
    }

    /**
     * Elimina un cliente de la base de datos según su documento.
     * @param doc documento del cliente a eliminar
     * @throws SQLException si ocurre un error en la BD
     */
    public void eliminar(int doc) throws SQLException {
        dao.eliminar(doc); // Llama al DAO para borrar el registro
    }

    /**
     * Obtiene la lista de todos los clientes registrados en la base de datos.
     * @return lista de clientes
     * @throws Exception si ocurre un error en la consulta
     */
    public List<Cliente> listar() throws Exception {
        return dao.listar(); // Llama al DAO para recuperar los clientes
    }

    /**
     * Busca un cliente en la base de datos por su documento.
     * @param documento número de documento a buscar
     * @return el cliente encontrado o null si no existe
     * @throws SQLException si ocurre un error en la consulta
     */
    public Cliente buscarPorDocumento(int documento) throws SQLException {
        return dao.buscarPorDocumento(documento);
    }
}
