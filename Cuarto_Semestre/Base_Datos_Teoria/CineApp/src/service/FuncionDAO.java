package service; // El paquete donde se encuentra esta clase

import model.Funcion; // Importa la clase Funcion del paquete model
import java.sql.*; // Importa todas las clases necesarias para trabajar con SQL (Connection, Statement, ResultSet, etc.)
//import java.time.LocalDateTime; // Importación comentada, posiblemente usada en algún momento
import java.util.ArrayList; 
import java.util.List;

public class FuncionDAO { // Clase DAO (Data Access Object) para manejar operaciones CRUD con la tabla "funciones"
    private Connection con; // Objeto Connection para conectarse a la base de datos

    // Constructor que recibe la conexión a la base de datos
    public FuncionDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar una nueva función en la base de datos
    public void insertar(Funcion f) throws SQLException {
        String sql = "INSERT INTO funciones(pelicula_id, sala_id, fecha_hora) VALUES(?,?,?)"; // Consulta SQL de inserción
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Se prepara el statement
            ps.setInt(1, f.getPeliculaId()); // Se asigna el ID de la película
            ps.setInt(2, f.getSalaId()); // Se asigna el ID de la sala
            ps.setTimestamp(3, Timestamp.valueOf(f.getFechaHora())); // Se asigna la fecha y hora
            ps.executeUpdate(); // Ejecuta la consulta de inserción
        }
    }

    // Método para listar todas las funciones almacenadas en la base de datos
    public List<Funcion> listar() throws SQLException {
        List<Funcion> list = new ArrayList<>(); // Lista donde se guardarán las funciones obtenidas
        String sql = "SELECT * FROM funciones"; // Consulta SQL de selección
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) { // Ejecuta la consulta
            while (rs.next()) { // Recorre los resultados
                Funcion f = new Funcion(); // Se crea un objeto Funcion por cada fila
                f.setId(rs.getInt("id")); // Se obtiene el ID de la función
                f.setPeliculaId(rs.getInt("pelicula_id")); // Se obtiene el ID de la película
                f.setSalaId(rs.getInt("sala_id")); // Se obtiene el ID de la sala
                f.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime()); // Convierte el timestamp a LocalDateTime
                list.add(f); // Agrega el objeto a la lista
            }
        }
        return list; // Retorna la lista con todas las funciones
    }

    // Método para actualizar los datos de una función existente
    public void actualizar(Funcion f) throws SQLException {
        String sql = "UPDATE funciones SET pelicula_id=?, sala_id=?, fecha_hora=? WHERE id=?"; // Consulta de actualización
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Se prepara el statement
            ps.setInt(1, f.getPeliculaId()); // Actualiza el ID de la película
            ps.setInt(2, f.getSalaId()); // Actualiza el ID de la sala
            ps.setTimestamp(3, Timestamp.valueOf(f.getFechaHora())); // Actualiza la fecha y hora
            ps.setInt(4, f.getId()); // Indica cuál registro actualizar (por ID)
            ps.executeUpdate(); // Ejecuta la actualización
        }
    }

    // Método para eliminar una función por su ID
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM funciones WHERE id=?"; // Consulta de eliminación
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Se prepara el statement
            ps.setInt(1, id); // Se pasa el ID de la función a eliminar
            ps.executeUpdate(); // Ejecuta la eliminación
        }
    }
    // En FuncionDAO.java - Agregar este método
public boolean tieneEntradasVendidas(int funcionId) throws SQLException {
    String sql = "SELECT COUNT(*) FROM entradas WHERE funcion_id = ?";
    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
        pstmt.setInt(1, funcionId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    }
    return false;
}

}
