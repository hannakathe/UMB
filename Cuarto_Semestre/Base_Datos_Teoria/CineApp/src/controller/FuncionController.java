package controller;

import model.Funcion;
import service.FuncionDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador para gestionar la lógica de negocio de las funciones de cine.
 * Se encarga de coordinar la creación, consulta, actualización y eliminación
 * de funciones, actuando como intermediario entre la UI y la capa DAO.
 */
public class FuncionController {
    // DAO para manejar las operaciones sobre la tabla de funciones
    private FuncionDAO dao;

    /**
     * Constructor que inicializa el controlador con una conexión
     * a la base de datos y crea una instancia de FuncionDAO.
     * @param con conexión activa a la base de datos
     */
    public FuncionController(Connection con) {
        this.dao = new FuncionDAO(con);
    }

    /**
     * Inserta una nueva función en la base de datos.
     * @param peliculaId id de la película que se proyectará
     * @param salaId id de la sala donde se dará la función
     * @param fechaHora fecha y hora de la proyección
     * @throws Exception si ocurre un error en la inserción
     */
    public void insertar(int peliculaId, int salaId, LocalDateTime fechaHora) throws Exception {
        Funcion f = new Funcion();
        f.setPeliculaId(peliculaId);
        f.setSalaId(salaId);
        f.setFechaHora(fechaHora);
        dao.insertar(f); // Inserta la función en la base de datos
    }

    /**
     * Lista todas las funciones registradas en la base de datos.
     * @return lista de funciones
     * @throws Exception si ocurre un error en la consulta
     */
    public List<Funcion> listar() throws Exception {
        return dao.listar();
    }

    /**
     * Actualiza los datos de una función existente.
     * @param id identificador de la función
     * @param peliId nuevo id de la película
     * @param salaId nuevo id de la sala
     * @param fechaHora nueva fecha y hora
     * @throws SQLException si ocurre un error en la actualización
     */
    public void actualizar(int id, int peliId, int salaId, LocalDateTime fechaHora) throws SQLException {
        dao.actualizar(new Funcion(id, peliId, salaId, fechaHora));
    }

    /**
     * Elimina una función de la base de datos por su id.
     * @param id identificador de la función a eliminar
     * @throws SQLException si ocurre un error en la eliminación
     */
    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    // MÉTODOS NUEVOS PARA LA GESTIÓN DE FUNCIONES
    public void crearFuncion(int peliculaId, int salaId, LocalDateTime fechaHora) throws Exception {
        insertar(peliculaId, salaId, fechaHora);
    }

    public boolean tieneEntradasVendidas(int funcionId) throws SQLException {
        return dao.tieneEntradasVendidas(funcionId);
    }
}