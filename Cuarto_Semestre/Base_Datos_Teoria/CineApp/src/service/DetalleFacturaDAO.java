package service;

import model.DetalleFactura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetalleFacturaDAO {
    private Connection con;
    public DetalleFacturaDAO(Connection con) { this.con = con; }

    public void insertar(DetalleFactura d) throws SQLException {
        String sql = "INSERT INTO detalle_factura(factura_id, entrada_id, cantidad, valor_unitario, valor_total) VALUES(?,?,?,?,?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, d.getFacturaId());
            ps.setInt(2, d.getEntradaId());
            ps.setInt(3, d.getCantidad());
            ps.setDouble(4, d.getValorUnitario());
            ps.setDouble(5, d.getValorTotal());
            ps.executeUpdate();
        }
    }

    public List<DetalleFactura> listar() throws SQLException {
        List<DetalleFactura> list = new ArrayList<>();
        String sql = "SELECT * FROM detalle_factura";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                DetalleFactura d = new DetalleFactura();
                d.setId(rs.getInt("id"));
                d.setFacturaId(rs.getInt("factura_id"));
                d.setEntradaId(rs.getInt("entrada_id"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setValorUnitario(rs.getDouble("valor_unitario"));
                d.setValorTotal(rs.getDouble("valor_total"));
                list.add(d);
            }
        }
        return list;
    }
}
