package controller;

// Importación de la clase Asiento (modelo) y el DAO que maneja la base de datos
import model.Asiento;
import service.AsientoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de los asientos.
 * Sirve como intermediario entre la interfaz de usuario (UI) y
 * la capa de acceso a datos (DAO).
 */
public class AsientoController {
    // Objeto que permite comunicarse con la base de datos a través del DAO
    private AsientoDAO dao;

    /**
     * Constructor que inicializa el controlador con una conexión
     * a la base de datos y crea una instancia del DAO.
     * @param con conexión activa a la base de datos
     */
    public AsientoController(Connection con) {
        this.dao = new AsientoDAO(con);
    }

    /**
     * Inserta un nuevo asiento en la base de datos.
     * @param salaId identificador de la sala donde está el asiento
     * @param numeroSilla número o código del asiento (ejemplo: A1, B2)
     * @param disponible indica si el asiento está disponible o no
     * @throws Exception si ocurre un error al insertar en la BD
     */
    public void insertar(int salaId, String numeroSilla, boolean disponible) throws Exception {
        // Se crea un objeto Asiento con id = 0 (la BD genera el ID automáticamente)
        Asiento a = new Asiento(0, salaId, numeroSilla, disponible);
        dao.insertar(a); // Llama al DAO para guardar en la base de datos
    }

    /**
     * Actualiza un asiento existente en la base de datos.
     * @param id identificador del asiento a modificar
     * @param salaId identificador de la sala
     * @param numeroSilla número o código del asiento
     * @param disponible estado de disponibilidad del asiento
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void actualizar(int id, int salaId, String numeroSilla, boolean disponible) throws SQLException {
        // Se crea un objeto Asiento con los nuevos valores
        Asiento a = new Asiento(id, salaId, numeroSilla, disponible);
        dao.actualizar(a); // Llama al DAO para actualizar en la base de datos
    }

    /**
     * Elimina un asiento de la base de datos según su id.
     * @param id identificador del asiento a eliminar
     * @throws SQLException si ocurre un error en la base de datos
     */
    public void eliminar(int id) throws SQLException {
        dao.eliminar(id); // Llama al DAO para borrar el asiento
    }

    /**
     * Obtiene una lista de todos los asientos registrados en la base de datos.
     * @return lista de objetos Asiento
     * @throws Exception si ocurre un error en la consulta
     */
    public List<Asiento> listar() throws Exception {
        return dao.listar(); // Llama al DAO para recuperar todos los asientos
    }
}
