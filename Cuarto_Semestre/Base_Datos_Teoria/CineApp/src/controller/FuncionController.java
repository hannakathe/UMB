package controller;

import model.Funcion;
import service.FuncionDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class FuncionController {
    private FuncionDAO dao;

    public FuncionController(Connection con) {
        this.dao = new FuncionDAO(con);
    }

    public void insertar(int peliculaId, int salaId, LocalDateTime fechaHora) throws Exception {
        Funcion f = new Funcion();
        f.setPeliculaId(peliculaId);
        f.setSalaId(salaId);
        f.setFechaHora(fechaHora);
        dao.insertar(f);
    }

    public List<Funcion> listar() throws Exception {
        return dao.listar();
    }

    public void actualizar(int id, int peliId, int salaId, LocalDateTime fechaHora) throws SQLException {
        dao.actualizar(new Funcion(id, peliId, salaId, fechaHora));
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

}
