package controller;

import model.Sala;
import service.SalaDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class SalaController {
    private SalaDAO dao;

    public SalaController(Connection con) {
        this.dao = new SalaDAO(con);
    }

    public void insertar(String tipo, int cap) throws SQLException {
        Sala s = new Sala(0, tipo, cap); // id 0 o autogenerado por la DB
        dao.insertar(s);
    }

    public void actualizar(int id, String tipo, int cap) throws SQLException {
        Sala s = new Sala(id, tipo, cap);
        dao.actualizar(s);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Sala> listar() throws SQLException {
        return dao.listar();
    }
}
