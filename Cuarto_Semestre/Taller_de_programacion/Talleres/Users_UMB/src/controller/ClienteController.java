package controller;

import service.ClienteDAO;
import model.Cliente;
import java.util.List;

public class ClienteController {
    private ClienteDAO dao = new ClienteDAO();

    public void agregar(Cliente c) { dao.agregarCliente(c); }
    public List<Cliente> listar() { return dao.listarClientes(); }
    public void actualizar(Cliente c) { dao.actualizarCliente(c); }
    public void eliminar(String nroID) { dao.eliminarCliente(nroID); }
}
