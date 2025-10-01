package controller;

import model.Cliente;
import service.ClienteDAO;
import java.sql.Connection;
import java.util.List;

public class ClienteController {
    private ClienteDAO dao;
    public ClienteController(Connection con) { this.dao = new ClienteDAO(con); }

    public void insertar(int doc, String nombre, String tel) throws Exception {
        Cliente c = new Cliente(doc, nombre, tel);
        dao.insertar(c);
    }

    public List<Cliente> listar() throws Exception { return dao.listar(); }
}
