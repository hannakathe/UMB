package service;

// Importa el modelo DetalleFactura y librerías SQL
import model.DetalleFactura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase DAO para manejar las operaciones de la tabla "detalle_factura"
public class DetalleFacturaDAO {
    // Conexión a la base de datos
    private Connection con;

    // Constructor que recibe la conexión como parámetro
    public DetalleFacturaDAO(Connection con) { 
        this.con = con; 
    }

    // Método para insertar un nuevo detalle de factura en la base de datos
    public void insertar(DetalleFactura d) throws SQLException {
        // Consulta SQL para insertar un registro en detalle_factura
        String sql = "INSERT INTO detalle_factura(factura_id, entrada_id, cantidad, valor_unitario, valor_total) VALUES(?,?,?,?,?)";
        
        // Se usa PreparedStatement para proteger contra inyecciones SQL
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Asignación de valores a cada parámetro de la consulta
            ps.setInt(1, d.getFacturaId());      // ID de la factura
            ps.setInt(2, d.getEntradaId());      // ID de la entrada
            ps.setInt(3, d.getCantidad());       // Cantidad de entradas
            ps.setDouble(4, d.getValorUnitario());// Valor unitario
            ps.setDouble(5, d.getValorTotal());  // Valor total
            // Ejecuta la consulta de inserción
            ps.executeUpdate();
        }
    }

    // Método para listar todos los detalles de factura de la tabla
    public List<DetalleFactura> listar() throws SQLException {
        // Lista que almacenará los objetos recuperados
        List<DetalleFactura> list = new ArrayList<>();
        // Consulta SQL para obtener todos los registros
        String sql = "SELECT * FROM detalle_factura";
        
        // Statement para ejecutar la consulta y obtener el ResultSet
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            // Recorre los resultados del ResultSet
            while (rs.next()) {
                // Crea un objeto DetalleFactura vacío
                DetalleFactura d = new DetalleFactura();
                // Asigna valores a sus atributos con los datos obtenidos de la BD
                d.setId(rs.getInt("id"));                     // ID del detalle
                d.setFacturaId(rs.getInt("factura_id"));       // ID de la factura
                d.setEntradaId(rs.getInt("entrada_id"));       // ID de la entrada
                d.setCantidad(rs.getInt("cantidad"));          // Cantidad de entradas
                d.setValorUnitario(rs.getDouble("valor_unitario")); // Valor unitario
                d.setValorTotal(rs.getDouble("valor_total"));  // Valor total
                // Agrega el objeto a la lista
                list.add(d);
            }
        }
        // Devuelve la lista con todos los detalles encontrados
        return list;
    }
}
