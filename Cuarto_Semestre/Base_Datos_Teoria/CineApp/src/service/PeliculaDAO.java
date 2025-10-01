package service;

import model.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {
    private Connection con;
    public PeliculaDAO(Connection con) { this.con = con; }

    public void insertar(Pelicula p) throws SQLException {
        String sql = "INSERT INTO peliculas(titulo, genero) VALUES(?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero());
            ps.executeUpdate();
        }
    }

    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> list = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero")));
            }
        }
        return list;
    }
}
