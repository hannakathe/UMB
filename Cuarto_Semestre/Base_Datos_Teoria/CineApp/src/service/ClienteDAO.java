package service;

// Importa la clase Cliente del paquete model
import model.Cliente;
// Importa librerías necesarias para conexión y consultas SQL
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase DAO para gestionar la persistencia de la entidad Cliente
public class ClienteDAO {
    // Objeto Connection para manejar la conexión con la base de datos
    private Connection con;

    // Constructor que recibe la conexión ya establecida
    public ClienteDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar un cliente en la base de datos
    public void insertar(Cliente c) throws SQLException {
        // Consulta SQL para insertar datos en la tabla clientes
        String sql = "INSERT INTO clientes(documento, nombre, telefono) VALUES(?,?,?)";
        // PreparedStatement evita la inyección SQL
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Se asignan los valores a los parámetros de la consulta
            ps.setInt(1, c.getDocumento());   // Documento del cliente
            ps.setString(2, c.getNombre());   // Nombre del cliente
            ps.setString(3, c.getTelefono()); // Teléfono del cliente
            // Ejecuta la inserción
            ps.executeUpdate();
        }
    }

    // Método para obtener todos los clientes de la tabla
    public List<Cliente> listar() throws SQLException {
        // Lista que almacenará todos los clientes recuperados
        List<Cliente> list = new ArrayList<>();
        // Consulta SQL para traer todos los registros de clientes
        String sql = "SELECT * FROM clientes";
        // Statement para ejecutar la consulta y obtener resultados
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            // Itera sobre el ResultSet
            while (rs.next()) {
                // Crea objetos Cliente con los datos de cada fila
                list.add(new Cliente(
                        rs.getInt("documento"),   // Documento
                        rs.getString("nombre"),   // Nombre
                        rs.getString("telefono")  // Teléfono
                ));
            }
        }
        // Devuelve la lista de clientes
        return list;
    }

    // Método para actualizar los datos de un cliente existente
    public void actualizar(Cliente c) throws SQLException {
        // Consulta SQL de actualización
        String sql = "UPDATE clientes SET nombre=?, telefono=? WHERE documento=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Asignación de nuevos valores a los campos
            ps.setString(1, c.getNombre());   // Nuevo nombre
            ps.setString(2, c.getTelefono()); // Nuevo teléfono
            ps.setInt(3, c.getDocumento());   // Documento del cliente a actualizar
            // Ejecuta la actualización
            ps.executeUpdate();
        }
    }

    // Método para eliminar un cliente por su documento
    public void eliminar(int documento) throws SQLException {
        // Consulta SQL de eliminación
        String sql = "DELETE FROM clientes WHERE documento=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, documento); // Asigna el documento del cliente
            // Ejecuta la eliminación
            ps.executeUpdate();
        }
    }

    // Método adicional: busca un cliente en la BD por su documento
    public Cliente buscarPorDocumento(int documento) throws SQLException {
        // Consulta SQL para buscar un cliente específico
        String sql = "SELECT * FROM clientes WHERE documento = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, documento); // Se asigna el documento al parámetro
            try (ResultSet rs = ps.executeQuery()) {
                // Si existe un resultado, retorna un objeto Cliente
                if (rs.next()) {
                    return new Cliente(
                        rs.getInt("documento"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                    );
                }
                // Si no se encuentra, retorna null
                return null;
            }
        }
    }
}
