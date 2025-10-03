package service;

// Importa la clase Asiento del paquete model
import model.Asiento;
// Importa clases de SQL necesarias para la conexión y consultas
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Clase encargada del acceso a datos (DAO) para la entidad Asiento
public class AsientoDAO {
    // Conexión a la base de datos
    private Connection con;

    // Constructor que recibe una conexión para trabajar con la BD
    public AsientoDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar un asiento en la base de datos
    public void insertar(Asiento a) throws SQLException {
        // Consulta SQL para insertar un nuevo registro en la tabla "asientos"
        String sql = "INSERT INTO asientos(sala_id, numero_silla, disponible) VALUES(?,?,?)";
        // Uso de PreparedStatement para evitar inyección SQL
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Se asignan los valores de los parámetros de la consulta
            ps.setInt(1, a.getSalaId());         // sala_id
            ps.setString(2, a.getNumeroSilla()); // número de la silla
            ps.setBoolean(3, a.isDisponible());  // disponibilidad
            // Ejecuta la consulta de inserción
            ps.executeUpdate();
        }
    }

    // Método para listar todos los asientos de la tabla
    public List<Asiento> listar() throws SQLException {
        // Lista donde se almacenarán los resultados
        List<Asiento> list = new ArrayList<>();
        // Consulta SQL para traer todos los asientos
        String sql = "SELECT * FROM asientos";
        // Statement para ejecutar la consulta y obtener el ResultSet
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            // Recorre los resultados del ResultSet
            while (rs.next()) {
                // Crea un nuevo objeto Asiento con los datos de la BD y lo agrega a la lista
                list.add(new Asiento(
                        rs.getInt("id"),           // ID del asiento
                        rs.getInt("sala_id"),      // ID de la sala
                        rs.getString("numero_silla"), // Número de la silla
                        rs.getBoolean("disponible")   // Disponibilidad
                ));
            }
        }
        // Retorna la lista con todos los asientos encontrados
        return list;
    }

    // Método para actualizar un asiento existente en la base de datos
    public void actualizar(Asiento a) throws SQLException {
        // Consulta SQL de actualización
        String sql = "UPDATE asientos SET sala_id=?, numero_silla=?, disponible=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Se asignan los nuevos valores a la consulta
            ps.setInt(1, a.getSalaId());         // sala_id
            ps.setString(2, a.getNumeroSilla()); // número de silla
            ps.setBoolean(3, a.isDisponible());  // disponibilidad
            ps.setInt(4, a.getId());             // id del asiento a modificar
            // Ejecuta la actualización
            ps.executeUpdate();
        }
    }

    // Método para eliminar un asiento de la base de datos por su ID
    public void eliminar(int id) throws SQLException {
        // Consulta SQL de eliminación
        String sql = "DELETE FROM asientos WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            // Se asigna el id del asiento a eliminar
            ps.setInt(1, id);
            // Ejecuta la eliminación
            ps.executeUpdate();
        }
    }

}
