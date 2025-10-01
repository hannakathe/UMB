package controller;

import model.Pelicula;
import service.PeliculaDAO;
import java.sql.Connection;
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
}
