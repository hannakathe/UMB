package service;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private Connection con;
    public ClienteDAO(Connection con) { this.con = con; }

    public void insertar(Cliente c) throws SQLException {
        String sql = "INSERT INTO clientes(documento, nombre, telefono) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getDocumento());
            ps.setString(2, c.getNombre());
            ps.setString(3, c.getTelefono());
            ps.executeUpdate();
        }
    }

    public List<Cliente> listar() throws SQLException {
        List<Cliente> list = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Cliente(rs.getInt("documento"), rs.getString("nombre"), rs.getString("telefono")));
            }
        }
        return list;
    }
}
