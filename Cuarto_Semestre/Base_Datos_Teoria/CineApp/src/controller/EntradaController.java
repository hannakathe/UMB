package controller;

import model.Entrada;
import service.EntradaDAO;
import service.AsientoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de las entradas.
 * Sirve como intermediario entre la capa de presentación (UI)
 * y la capa de acceso a datos (DAO).
 */
public class EntradaController {
    // DAO para manejar las operaciones sobre la tabla de entradas
    private EntradaDAO dao;
    // DAO para manejar las operaciones sobre los asientos
    private AsientoDAO asientoDao;

    /**
     * Constructor que inicializa el controlador con una conexión
     * a la base de datos y crea instancias de los DAO.
     * @param con conexión activa a la base de datos
     */
    public EntradaController(Connection con) {
        this.dao = new EntradaDAO(con);
        this.asientoDao = new AsientoDAO(con);
    }

    /**
     * Crea una nueva entrada (boleto) para un cliente.
     * Además de insertar la entrada, se marca el asiento como no disponible.
     * @param clienteDoc documento del cliente
     * @param funcionId id de la función (película en determinada sala/horario)
     * @param asientoId id del asiento elegido
     * @param valor valor de la entrada
     * @return el id generado para la entrada insertada
     * @throws Exception si ocurre un error durante la operación
     */
    public int crearEntrada(int clienteDoc, int funcionId, int asientoId, double valor) throws Exception {
        // Se construye el objeto Entrada con los datos recibidos
        Entrada e = new Entrada();
        e.setClienteDocumento(clienteDoc);
        e.setFuncionId(funcionId);
        e.setAsientoId(asientoId);
        e.setValor(valor);

        // Insertar la entrada en la base de datos
        int id = dao.insertar(e);

        // Intentar actualizar el estado del asiento a "no disponible"
        try (java.sql.PreparedStatement ps = asientoDaoCon(asientoId, asientoDao)) {
            ps.executeUpdate();
        } catch (Exception ex) {
            // En caso de error al actualizar el asiento, se ignora
            // para no bloquear la compra de la entrada
        }
        return id;
    }

    /**
     * Método auxiliar que obtiene un PreparedStatement
     * para actualizar la disponibilidad de un asiento.
     * ⚠️ Usa reflexión para acceder a la conexión interna de AsientoDAO.
     * Esto no es una buena práctica en producción, pero se usa aquí como workaround.
     */
    private java.sql.PreparedStatement asientoDaoCon(int asientoId, AsientoDAO asientoDao) throws Exception {
        // Se obtiene el campo "con" (Connection) de AsientoDAO mediante reflexión
        java.lang.reflect.Field f = AsientoDAO.class.getDeclaredField("con");
        f.setAccessible(true);
        java.sql.Connection con = (java.sql.Connection) f.get(asientoDao);

        // Consulta SQL para marcar asiento como no disponible
        String sql = "UPDATE asientos SET disponible = false WHERE id = ?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, asientoId);
        return ps;
    }

    /**
     * Lista todas las entradas registradas en la base de datos.
     * @return lista de entradas
     * @throws Exception si ocurre un error en la consulta
     */
    public List<Entrada> listar() throws Exception {
        return dao.listar();
    }

    /**
     * Actualiza una entrada existente en la base de datos.
     * @param id identificador de la entrada
     * @param clienteDoc documento del cliente
     * @param funcionId id de la función
     * @param asientoId id del asiento
     * @param valor valor de la entrada
     * @throws SQLException si ocurre un error en la BD
     */
    public void actualizar(int id, int clienteDoc, int funcionId, int asientoId, double valor) throws SQLException {
        dao.actualizar(new Entrada(id, clienteDoc, funcionId, asientoId, valor));
    }

    /**
     * Elimina una entrada de la base de datos por su id.
     * @param id identificador de la entrada a eliminar
     * @throws SQLException si ocurre un error en la BD
     */
    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }
}
