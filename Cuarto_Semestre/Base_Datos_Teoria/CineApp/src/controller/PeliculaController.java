package controller;

import model.Pelicula;
import service.PeliculaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que maneja la lógica de negocio para la entidad Pelicula.
 * Se encarga de recibir los datos desde la interfaz o la capa superior
 * y delegar las operaciones en el DAO correspondiente.
 */
public class PeliculaController {
    // Objeto de acceso a datos (DAO) encargado de interactuar con la base de datos
    private PeliculaDAO dao;

    /**
     * Constructor que inicializa el controlador con una conexión a la base de datos.
     * @param con Conexión activa a la base de datos.
     */
    public PeliculaController(Connection con) { 
        this.dao = new PeliculaDAO(con); 
    }

    /**
     * Inserta una nueva película en la base de datos.
     * @param titulo Título de la película.
     * @param genero Género de la película.
     * @throws Exception si ocurre un error al insertar.
     */
    public void insertar(String titulo, String genero) throws Exception {
        Pelicula p = new Pelicula();
        p.setTitulo(titulo);
        p.setGenero(genero);
        dao.insertar(p); // delega la inserción al DAO
    }

    /**
     * Obtiene la lista de todas las películas almacenadas en la base de datos.
     * @return Lista de objetos Pelicula.
     * @throws Exception si ocurre un error al consultar.
     */
    public List<Pelicula> listar() throws Exception { 
        return dao.listar(); 
    }

    /**
     * Actualiza los datos de una película existente.
     * @param id Identificador de la película a actualizar.
     * @param titulo Nuevo título.
     * @param genero Nuevo género.
     * @throws SQLException si ocurre un error durante la actualización.
     */
    public void actualizar(int id, String titulo, String genero) throws SQLException {
        dao.actualizar(new Pelicula(id, titulo, genero));
    }

    /**
     * Elimina una película de la base de datos por su ID.
     * @param id Identificador de la película a eliminar.
     * @throws SQLException si ocurre un error durante la eliminación.
     */
    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }
}
