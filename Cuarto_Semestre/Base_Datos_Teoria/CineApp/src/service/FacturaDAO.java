package service;

// Importa el modelo Factura y las librerías necesarias para SQL
import model.Factura;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase DAO para manejar las operaciones de la tabla "facturas"
public class FacturaDAO {
    // Conexión a la base de datos
    private Connection con;

    // Constructor que recibe la conexión como parámetro
    public FacturaDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar una nueva factura en la base de datos
    // Retorna el ID autogenerado por la base de datos
    public int insertar(Factura f) throws SQLException {
        // Consulta SQL de inserción
        String sql = "INSERT INTO facturas(cliente_documento, valor_total, datos_empresa) VALUES(?,?,?)";
        
        // Se usa RETURN_GENERATED_KEYS para obtener la clave primaria generada automáticamente
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Se asignan los valores a los parámetros de la consulta
            ps.setInt(1, f.getClienteDocumento());   // Documento del cliente
            ps.setDouble(2, f.getValorTotal());      // Valor total de la factura
            ps.setString(3, f.getDatosEmpresa());    // Datos de la empresa emisora
            // Ejecuta la inserción
            ps.executeUpdate();
            
            // Recupera el ID generado automáticamente
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getInt(1); // Retorna el ID de la factura
            }
        }
        // Si no se genera un ID, retorna -1
        return -1;
    }

    // Método para listar todas las facturas de la base de datos
    public List<Factura> listar() throws SQLException {
        // Lista donde se guardarán las facturas recuperadas
        List<Factura> list = new ArrayList<>();
        // Consulta SQL para obtener todas las facturas
        String sql = "SELECT * FROM facturas";
        
        // Ejecuta la consulta y procesa los resultados
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                // Crea un objeto Factura y asigna los datos obtenidos
                Factura f = new Factura();
                f.setId(rs.getInt("id"));                         // ID de la factura
                f.setClienteDocumento(rs.getInt("cliente_documento")); // Documento cliente
                f.setValorTotal(rs.getDouble("valor_total"));     // Valor total
                f.setDatosEmpresa(rs.getString("datos_empresa")); // Datos empresa
                // Agrega la factura a la lista
                list.add(f);
            }
        }
        // Retorna la lista de facturas
        return list;
    }

    // Método para actualizar una factura existente
    public void actualizar(Factura f) throws SQLException {
        // Consulta SQL de actualización

        String sql = "UPDATE facturas SET cliente_doc=?, valor_total=?, datos_empresa=? WHERE id=?";
        
        // Prepara la sentencia
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Se asignan los nuevos valores
            ps.setInt(1, f.getClienteDocumento());   // Documento cliente
            ps.setDouble(2, f.getValorTotal());      // Valor total
            ps.setString(3, f.getDatosEmpresa());    // Datos empresa
            ps.setInt(4, f.getId());                 // ID de la factura
            // Ejecuta la actualización
            ps.executeUpdate();
        }
    }

    // Método para eliminar una factura por su ID
    public void eliminar(int id) throws SQLException {
        // Consulta SQL de eliminación
        String sql = "DELETE FROM facturas WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); // Se asigna el ID de la factura a eliminar
            ps.executeUpdate(); // Ejecuta la eliminación
        }
    }
}
