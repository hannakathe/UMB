package service;

import model.Sala;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalaDAO {
    private Connection con;
    public SalaDAO(Connection con) { this.con = con; }

    public void insertar(Sala s) throws SQLException {
        String sql = "INSERT INTO salas(tipo_sala, capacidad) VALUES(?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getTipoSala());
            ps.setInt(2, s.getCapacidad());
            ps.executeUpdate();
        }
    }

    public List<Sala> listar() throws SQLException {
        List<Sala> list = new ArrayList<>();
        String sql = "SELECT * FROM salas";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Sala(rs.getInt("id"), rs.getString("tipo_sala"), rs.getInt("capacidad")));
            }
        }
        return list;
    }
}
