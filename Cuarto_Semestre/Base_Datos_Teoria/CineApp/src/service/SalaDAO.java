package service; // Paquete donde se encuentra la clase SalaDAO

import model.Sala; // Importa la clase Sala del paquete model
import java.sql.*; // Importa todas las clases necesarias para trabajar con SQL (Connection, Statement, ResultSet, etc.)
import java.util.ArrayList;
import java.util.List;

public class SalaDAO { // Clase DAO que maneja las operaciones CRUD sobre la tabla "salas"
    private Connection con; // Objeto Connection para manejar la conexión a la base de datos

    // Constructor que recibe una conexión a la base de datos
    public SalaDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar una nueva sala en la base de datos
    public void insertar(Sala s) throws SQLException {
        String sql = "INSERT INTO salas(tipo_sala, capacidad) VALUES(?,?)"; // Consulta SQL de inserción
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Prepara la sentencia SQL
            ps.setString(1, s.getTipoSala()); // Asigna el tipo de sala
            ps.setInt(2, s.getCapacidad()); // Asigna la capacidad de la sala
            ps.executeUpdate(); // Ejecuta la inserción en la base de datos
        }
    }

    // Método para listar todas las salas de la base de datos
    public List<Sala> listar() throws SQLException {
        List<Sala> list = new ArrayList<>(); // Lista donde se guardarán las salas obtenidas
        String sql = "SELECT * FROM salas"; // Consulta SQL para seleccionar todas las salas
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) { // Ejecuta la consulta
            while (rs.next()) { // Recorre los resultados obtenidos
                // Crea un nuevo objeto Sala con los datos de cada fila y lo agrega a la lista
                list.add(new Sala(rs.getInt("id"), rs.getString("tipo_sala"), rs.getInt("capacidad")));
            }
        }
        return list; // Retorna la lista con todas las salas
    }

    // Método para actualizar los datos de una sala existente
    public void actualizar(Sala s) throws SQLException {
        String sql = "UPDATE salas SET tipo_sala=?, capacidad=? WHERE id=?"; // Consulta SQL de actualización
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Prepara la sentencia SQL
            ps.setString(1, s.getTipoSala()); // Actualiza el tipo de sala
            ps.setInt(2, s.getCapacidad()); // Actualiza la capacidad
            ps.setInt(3, s.getId()); // Indica cuál sala actualizar (por ID)
            ps.executeUpdate(); // Ejecuta la actualización en la base de datos
        }
    }

    // Método para eliminar una sala por su ID
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM salas WHERE id=?"; // Consulta SQL de eliminación
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Prepara la sentencia SQL
            ps.setInt(1, id); // Se pasa el ID de la sala a eliminar
            ps.executeUpdate(); // Ejecuta la eliminación en la base de datos
        }
    }

    public double obtenerPrecioBase(int salaId) {
        String sql = "SELECT precio_base FROM salas WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, salaId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("precio_base");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 15000.0; // Precio por defecto
    }

}
