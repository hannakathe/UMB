package service;

import model.Funcion;
import java.sql.*;
//import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FuncionDAO {
    private Connection con;

    public FuncionDAO(Connection con) {
        this.con = con;
    }

    public void insertar(Funcion f) throws SQLException {
        String sql = "INSERT INTO funciones(pelicula_id, sala_id, fecha_hora) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getPeliculaId());
            ps.setInt(2, f.getSalaId());
            ps.setTimestamp(3, Timestamp.valueOf(f.getFechaHora()));
            ps.executeUpdate();
        }
    }

    public List<Funcion> listar() throws SQLException {
        List<Funcion> list = new ArrayList<>();
        String sql = "SELECT * FROM funciones";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Funcion f = new Funcion();
                f.setId(rs.getInt("id"));
                f.setPeliculaId(rs.getInt("pelicula_id"));
                f.setSalaId(rs.getInt("sala_id"));
                f.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                list.add(f);
            }
        }
        return list;
    }

    public void actualizar(Funcion f) throws SQLException {
        String sql = "UPDATE funciones SET pelicula_id=?, sala_id=?, fecha_hora=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getPeliculaId());
            ps.setInt(2, f.getSalaId());
            ps.setTimestamp(3, Timestamp.valueOf(f.getFechaHora()));
            ps.setInt(4, f.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM funciones WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
