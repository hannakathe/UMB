package service;

import model.Factura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {
    private Connection con;

    public FacturaDAO(Connection con) {
        this.con = con;
    }

    public int insertar(Factura f) throws SQLException {
        String sql = "INSERT INTO facturas(cliente_documento, valor_total, datos_empresa) VALUES(?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, f.getClienteDocumento());
            ps.setDouble(2, f.getValorTotal());
            ps.setString(3, f.getDatosEmpresa());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        }
        return -1;
    }

    public List<Factura> listar() throws SQLException {
        List<Factura> list = new ArrayList<>();
        String sql = "SELECT * FROM facturas";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Factura f = new Factura();
                f.setId(rs.getInt("id"));
                f.setClienteDocumento(rs.getInt("cliente_documento"));
                f.setValorTotal(rs.getDouble("valor_total"));
                f.setDatosEmpresa(rs.getString("datos_empresa"));
                list.add(f);
            }
        }
        return list;
    }

    public void actualizar(Factura f) throws SQLException {
        String sql = "UPDATE facturas SET cliente_doc=?, valor_total=?, datos_empresa=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, f.getClienteDocumento());
            ps.setDouble(2, f.getValorTotal());
            ps.setString(3, f.getDatosEmpresa());
            ps.setInt(4, f.getId());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM facturas WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
