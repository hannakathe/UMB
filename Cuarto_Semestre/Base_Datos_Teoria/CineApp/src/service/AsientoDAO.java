package service;

import model.Asiento;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsientoDAO {
    private Connection con;

    public AsientoDAO(Connection con) {
        this.con = con;
    }

    public void insertar(Asiento a) throws SQLException {
        String sql = "INSERT INTO asientos(sala_id, numero_silla, disponible) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getSalaId());
            ps.setString(2, a.getNumeroSilla());
            ps.setBoolean(3, a.isDisponible());
            ps.executeUpdate();
        }
    }

    public List<Asiento> listar() throws SQLException {
        List<Asiento> list = new ArrayList<>();
        String sql = "SELECT * FROM asientos";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Asiento(rs.getInt("id"), rs.getInt("sala_id"), rs.getString("numero_silla"),
                        rs.getBoolean("disponible")));
            }
        }
        return list;
    }

    public void actualizar(Asiento a) throws SQLException {
        String sql = "UPDATE asientos SET sala_id=?, numero_silla=?, disponible=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getSalaId());
            ps.setString(2, a.getNumeroSilla());
            ps.setBoolean(3, a.isDisponible());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM asientos WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
