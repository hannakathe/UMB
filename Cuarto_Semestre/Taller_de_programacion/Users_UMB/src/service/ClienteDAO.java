package service;

import model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public void agregarCliente(Cliente c) {
        String sql = "INSERT INTO Cliente(tipoID, nroID, nombres, correo, direccion, celular, cod_ciudad, cod_pais) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getTipoID());
            ps.setString(2, c.getNroID());
            ps.setString(3, c.getNombres());
            ps.setString(4, c.getCorreo());
            ps.setString(5, c.getDireccion());
            ps.setString(6, c.getCelular());
            ps.setInt(7, c.getCodCiudad());
            ps.setInt(8, c.getCodPais());
            ps.executeUpdate();
            System.out.println("Cliente agregado correctamente.");
        } catch(SQLException e) {
            System.out.println("Error al agregar: " + e.getMessage());
        }
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Cliente(
                    rs.getString("tipoID"),
                    rs.getString("nroID"),
                    rs.getString("nombres"),
                    rs.getString("correo"),
                    rs.getString("direccion"),
                    rs.getString("celular"),
                    rs.getInt("cod_ciudad"),
                    rs.getInt("cod_pais")
                ));
            }
        } catch(SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public void actualizarCliente(Cliente c) {
        String sql = "UPDATE Cliente SET nombres=?, correo=?, direccion=?, celular=?, cod_ciudad=?, cod_pais=? WHERE nroID=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getNombres());
            ps.setString(2, c.getCorreo());
            ps.setString(3, c.getDireccion());
            ps.setString(4, c.getCelular());
            ps.setInt(5, c.getCodCiudad());
            ps.setInt(6, c.getCodPais());
            ps.setString(7, c.getNroID());
            ps.executeUpdate();
            System.out.println("Cliente actualizado.");
        } catch(SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
        }
    }

    public void eliminarCliente(String nroID) {
        String sql = "DELETE FROM Cliente WHERE nroID=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nroID);
            ps.executeUpdate();
            System.out.println("Cliente eliminado.");
        } catch(SQLException e) {
            System.out.println("Error al eliminar: " + e.getMessage());
        }
    }
}
