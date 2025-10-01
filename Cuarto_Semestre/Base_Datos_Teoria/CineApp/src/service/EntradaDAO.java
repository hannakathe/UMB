package service;

import model.Entrada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntradaDAO {
    private Connection con;
    public EntradaDAO(Connection con) { this.con = con; }

    public int insertar(Entrada e) throws SQLException {
        String sql = "INSERT INTO entradas(cliente_documento, funcion_id, asiento_id, valor) VALUES(?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, e.getClienteDocumento());
            ps.setInt(2, e.getFuncionId());
            ps.setInt(3, e.getAsientoId());
            ps.setDouble(4, e.getValor());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Entrada> listar() throws SQLException {
        List<Entrada> list = new ArrayList<>();
        String sql = "SELECT * FROM entradas";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Entrada e = new Entrada();
                e.setId(rs.getInt("id"));
                e.setClienteDocumento(rs.getInt("cliente_documento"));
                e.setFuncionId(rs.getInt("funcion_id"));
                e.setAsientoId(rs.getInt("asiento_id"));
                e.setValor(rs.getDouble("valor"));
                list.add(e);
            }
        }
        return list;
    }
}
