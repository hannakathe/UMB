package service; // Paquete donde se encuentra la clase PeliculaDAO

import model.Pelicula; // Importa la clase Pelicula del paquete model
import java.sql.*; // Importa todas las clases necesarias para trabajar con SQL (Connection, Statement, PreparedStatement, etc.)
import java.util.ArrayList; 
import java.util.List;

public class PeliculaDAO { // Clase DAO para manejar operaciones CRUD sobre la tabla "peliculas"
    private Connection con; // Objeto Connection para la conexión a la base de datos

    // Constructor que recibe una conexión a la base de datos
    public PeliculaDAO(Connection con) {
        this.con = con;
    }

    // Método para insertar una nueva película en la base de datos
    public void insertar(Pelicula p) throws SQLException {
        String sql = "INSERT INTO peliculas(titulo, genero) VALUES(?,?)"; // Consulta SQL de inserción
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Prepara la sentencia SQL
            ps.setString(1, p.getTitulo()); // Asigna el título de la película
            ps.setString(2, p.getGenero()); // Asigna el género de la película
            ps.executeUpdate(); // Ejecuta la inserción en la base de datos
        }
    }

    // Método para listar todas las películas de la base de datos
    public List<Pelicula> listar() throws SQLException {
        List<Pelicula> list = new ArrayList<>(); // Lista donde se guardarán las películas obtenidas
        String sql = "SELECT * FROM peliculas"; // Consulta SQL para seleccionar todas las películas
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) { // Ejecuta la consulta
            while (rs.next()) { // Recorre los resultados de la consulta
                // Crea un nuevo objeto Pelicula con los datos obtenidos de la BD y lo agrega a la lista
                list.add(new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero")));
            }
        }
        return list; // Retorna la lista con todas las películas encontradas
    }

    // Método para actualizar los datos de una película existente
    public void actualizar(Pelicula p) throws SQLException {
        String sql = "UPDATE peliculas SET titulo=?, genero=? WHERE id=?"; // Consulta SQL de actualización
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Prepara la sentencia SQL
            ps.setString(1, p.getTitulo()); // Actualiza el título de la película
            ps.setString(2, p.getGenero()); // Actualiza el género de la película
            ps.setInt(3, p.getId()); // Indica cuál película actualizar (por ID)
            ps.executeUpdate(); // Ejecuta la actualización en la base de datos
        }
    }

    // Método para eliminar una película de la base de datos usando su ID
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM peliculas WHERE id=?"; // Consulta SQL de eliminación
        try (PreparedStatement ps = con.prepareStatement(sql)) { // Prepara la sentencia SQL
            ps.setInt(1, id); // Pasa el ID de la película a eliminar
            ps.executeUpdate(); // Ejecuta la eliminación en la base de datos
        }
    }

}
