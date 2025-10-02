package controller;

import model.Asiento;
import service.AsientoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AsientoController {
    private AsientoDAO dao;

    public AsientoController(Connection con) {
        this.dao = new AsientoDAO(con);
    }

    public void insertar(int salaId, String numeroSilla, boolean disponible) throws Exception {
        Asiento a = new Asiento(0, salaId, numeroSilla, disponible); // id = 0, se asigna en BD
        dao.insertar(a);
    }

    public void actualizar(int id, int salaId, String numeroSilla, boolean disponible) throws SQLException {
        Asiento a = new Asiento(id, salaId, numeroSilla, disponible);
        dao.actualizar(a);
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

    public List<Asiento> listar() throws Exception {
        return dao.listar();
    }
}
