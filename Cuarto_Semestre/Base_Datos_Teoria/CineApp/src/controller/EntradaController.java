package controller;

import model.Entrada;
import service.EntradaDAO;
import service.AsientoDAO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EntradaController {
    private EntradaDAO dao;
    private AsientoDAO asientoDao;

    public EntradaController(Connection con) {
        this.dao = new EntradaDAO(con);
        this.asientoDao = new AsientoDAO(con);
    }

    public int crearEntrada(int clienteDoc, int funcionId, int asientoId, double valor) throws Exception {
        Entrada e = new Entrada();
        e.setClienteDocumento(clienteDoc);
        e.setFuncionId(funcionId);
        e.setAsientoId(asientoId);
        e.setValor(valor);
        int id = dao.insertar(e);
        // marcar asiento como no disponible (simple): actualizar disponible = false
        try (java.sql.PreparedStatement ps = asientoDaoCon(asientoId, asientoDao)) {
            // helper below
            ps.executeUpdate();
        } catch (Exception ex) {
            // No bloquear por ahora
        }
        return id;
    }

    // helper: obtain PreparedStatement to update asiento (used only here)
    private java.sql.PreparedStatement asientoDaoCon(int asientoId, AsientoDAO asientoDao) throws Exception {
        java.lang.reflect.Field f = AsientoDAO.class.getDeclaredField("con");
        f.setAccessible(true);
        java.sql.Connection con = (java.sql.Connection) f.get(asientoDao);
        String sql = "UPDATE asientos SET disponible = false WHERE id = ?";
        java.sql.PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, asientoId);
        return ps;
    }

    public List<model.Entrada> listar() throws Exception {
        return dao.listar();
    }

    public void actualizar(int id, int clienteDoc, int funcionId, int asientoId, double valor) throws SQLException {
        dao.actualizar(new Entrada(id, clienteDoc, funcionId, asientoId, valor));
    }

    public void eliminar(int id) throws SQLException {
        dao.eliminar(id);
    }

}
