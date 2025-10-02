package controller;

import model.Pelicula;
import service.PeliculaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PeliculaController {
    private PeliculaDAO dao;
    public PeliculaController(Connection con) { this.dao = new PeliculaDAO(con); }
    public void insertar(String titulo, String genero) throws Exception {
        Pelicula p = new Pelicula();
        p.setTitulo(titulo);
        p.setGenero(genero);
        dao.insertar(p);
    }
    public List<Pelicula> listar() throws Exception { return dao.listar(); }
public void actualizar(int id, String titulo, String genero) throws SQLException {
    dao.actualizar(new Pelicula(id, titulo, genero));
}

public void eliminar(int id) throws SQLException {
    dao.eliminar(id);
}

}
