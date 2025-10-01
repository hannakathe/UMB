package controller;

import model.Funcion;
import service.FuncionDAO;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class FuncionController {
    private FuncionDAO dao;
    public FuncionController(Connection con) { this.dao = new FuncionDAO(con); }

    public void insertar(int peliculaId, int salaId, LocalDateTime fechaHora) throws Exception {
        Funcion f = new Funcion();
        f.setPeliculaId(peliculaId);
        f.setSalaId(salaId);
        f.setFechaHora(fechaHora);
        dao.insertar(f);
    }

    public List<Funcion> listar() throws Exception { return dao.listar(); }
}
