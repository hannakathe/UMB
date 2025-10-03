package controller;

import model.Sala;
import service.SalaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que maneja la lógica de negocio para la entidad Sala.
 * Se encarga de recibir los datos desde la capa superior (UI) 
 * y delegar las operaciones CRUD al DAO correspondiente.
 */
public class SalaController {
    // Objeto DAO encargado de realizar las operaciones con la base de datos
    private SalaDAO dao;

    /**
     * Constructor que inicializa el controlador con una conexión a la base de datos.
     * @param con Conexión activa a la base de datos.
     */
    public SalaController(Connection con) {
        this.dao = new SalaDAO(con);
    }

    /**
     * Inserta una nueva sala en la base de datos.
     * @param tipo Tipo de sala (ej: 2D, 3D, IMAX).
     * @param cap Capacidad total de la sala.
     * @throws SQLException si ocurre un error al insertar.
     */
    public void insertar(String tipo, int cap) throws SQLException {
        // Se pasa un id = 0, asumiendo que la base de datos lo generará automáticamente
        Sala s = new Sala(0, tipo, cap);
        dao.insertar(s);
    }

    /**
     * Actualiza los datos de una sala existente.
     * @param id Identificador de la sala a actualizar.
     * @param tipo Nuevo tipo de sala.
     * @param cap Nueva capacidad de la sala.
     * @throws SQLException si ocurre un error durante la actualización.
     */
    public void actualizar(int id, String tipo, int cap) throws SQLException {
        Sala s = new Sala(id, tipo, cap);
        dao.actualizar(s);
    }

    /**
     * Elimina una sala de la base de datos por su ID.
     * @param id Identificador de la sala a eliminar.
     * @throws SQLException si ocurre un error durante la eliminación.
     */
    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    /**
     * Obtiene la lista de todas las salas almacenadas en la base de datos.
     * @return Lista de objetos Sala.
     * @throws SQLException si ocurre un error al consultar.
     */
    public List<Sala> listar() throws SQLException {
        return dao.listar();
    }
}
