package service;

import model.Ciudad;
import model.Pais;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaisCiudadDAO {

    public List<Pais> listarPaises() {
        List<Pais> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pais";
        try (Connection conn = Conexion.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Pais(rs.getInt("cod_pais"), rs.getString("nombre_pais")));
            }
        } catch(SQLException e) {
            System.out.println("Error al listar países: " + e.getMessage());
        }
        return lista;
    }

    public List<Ciudad> listarCiudadesPorPais(int codPais) {
        List<Ciudad> lista = new ArrayList<>();
        String sql = "SELECT * FROM Ciudad WHERE cod_pais=?";
        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, codPais);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Ciudad(rs.getInt("cod_ciudad"), rs.getString("nombre_ciudad"), rs.getInt("cod_pais")));
            }
        } catch(SQLException e) {
            System.out.println("Error al listar ciudades: " + e.getMessage());
        }
        return lista;
    }
}
