package service;

// Importa el modelo Entrada y las librerías necesarias para SQL
import model.Entrada;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase DAO para manejar las operaciones de la tabla "entradas"
public class EntradaDAO {
    // Conexión a la base de datos
    private Connection con;

    // Constructor que recibe la conexión
    public EntradaDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar una nueva entrada en la base de datos
    // Retorna el ID generado automáticamente por la BD
    public int insertar(Entrada e) throws SQLException {
        // Consulta SQL de inserción
        String sql = "INSERT INTO entradas(cliente_documento, funcion_id, asiento_id, valor) VALUES(?,?,?,?)";
        
        // Se especifica Statement.RETURN_GENERATED_KEYS para recuperar el ID autogenerado
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Asignación de parámetros a la consulta
            ps.setInt(1, e.getClienteDocumento()); // Documento del cliente
            ps.setInt(2, e.getFuncionId());        // ID de la función
            ps.setInt(3, e.getAsientoId());        // ID del asiento
            ps.setDouble(4, e.getValor());         // Valor de la entrada
            // Ejecuta la inserción
            ps.executeUpdate();
            
            // Obtiene la clave primaria generada (ID de la entrada)
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next())
                    return rs.getInt(1); // Retorna el ID generado
            }
        }
        // Si no se generó ID, retorna -1
        return -1;
    }

    // Método para listar todas las entradas de la tabla
    public List<Entrada> listar() throws SQLException {
        // Lista para almacenar las entradas recuperadas
        List<Entrada> list = new ArrayList<>();
        // Consulta SQL
        String sql = "SELECT * FROM entradas";
        
        // Statement para ejecutar la consulta
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            // Recorre los resultados
            while (rs.next()) {
                // Crea un objeto Entrada vacío
                Entrada e = new Entrada();
                // Asigna los valores obtenidos de la BD
                e.setId(rs.getInt("id"));                       // ID
                e.setClienteDocumento(rs.getInt("cliente_documento")); // Documento cliente
                e.setFuncionId(rs.getInt("funcion_id"));        // ID función
                e.setAsientoId(rs.getInt("asiento_id"));        // ID asiento
                e.setValor(rs.getDouble("valor"));              // Valor
                // Agrega el objeto a la lista
                list.add(e);
            }
        }
        // Retorna la lista con todas las entradas
        return list;
    }

    // Método para actualizar los datos de una entrada ya existente
    public void actualizar(Entrada e) throws SQLException {
        // Consulta SQL de actualización
        // ⚠️ OJO: En la BD el campo podría llamarse "cliente_documento", revisa si "cliente_doc" es correcto
        String sql = "UPDATE entradas SET cliente_doc=?, funcion_id=?, asiento_id=?, valor=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Se asignan los nuevos valores
            ps.setInt(1, e.getClienteDocumento()); // Nuevo documento cliente
            ps.setInt(2, e.getFuncionId());        // Nueva función
            ps.setInt(3, e.getAsientoId());        // Nuevo asiento
            ps.setDouble(4, e.getValor());         // Nuevo valor
            ps.setInt(5, e.getId());               // ID de la entrada a actualizar
            // Ejecuta la actualización
            ps.executeUpdate();
        }
    }

    // Método para eliminar una entrada de la BD por su ID
    public void eliminar(int id) throws SQLException {
        // Consulta SQL de eliminación
        String sql = "DELETE FROM entradas WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); // Asigna el ID a eliminar
            ps.executeUpdate(); // Ejecuta la eliminación
        }
    }

}
