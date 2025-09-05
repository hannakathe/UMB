package service;  
// Define el paquete al que pertenece esta clase (en este caso, "service").
// Sirve para organizar las clases que gestionan la lógica de negocio o servicios.

import model.Conexion;  
// Importa la clase Conexion, que se encarga de establecer la conexión con la base de datos.

import java.sql.*;  
// Importa todas las clases necesarias para trabajar con SQL en Java (Connection, Statement, ResultSet, etc.).

public class DataArticulo {
    // Esta clase contiene métodos para realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
    // sobre la tabla "ARTICULO" de la base de datos.

    

    // ------------------ MÉTODO INSERTAR C------------------
    public void insertarArticulo(String cod, String nom, String uni, int pre, int stk, String marca) throws SQLException {
        Connection con = Conexion.getConexion();
        // Consulta SQL con parámetros (?= Valores columnas de la tabla, datos puros) para evitar inyección SQL.
        String sql = "INSERT INTO ARTICULO VALUES (?,?,?,?,?,?)"; //? marcadores de posicion
        // Se prepara la consulta SQL con PreparedStatement.
        PreparedStatement ps = con.prepareStatement(sql);
        // Se asignan valores a cada parámetro de la consulta. Vinculacion con marcadores de posicion. 
        ps.setString(1, cod);     // Código del artículo
        ps.setString(2, nom);     // Nombre
        ps.setString(3, uni);     // Unidad
        ps.setInt(4, pre);        // Precio
        ps.setInt(5, stk);        // Stock
        ps.setString(6, marca);   // Marca
        // Se ejecuta la consulta de inserción.
        ps.executeUpdate();
        // Se cierran recursos.
        ps.close();
        con.close();
    }

    // ------------------ MÉTODO LISTAR R------------------
    public ResultSet listarArticulos() throws SQLException {
        // Obtiene una conexión a la base de datos usando la clase Conexion.
        Connection con = Conexion.getConexion();
        // Consulta SQL para seleccionar todos los registros de la tabla ARTICULO.
        String sql = "SELECT * FROM ARTICULO";
        // Se crea un Statement para ejecutar la consulta.
        Statement st = con.createStatement();
        // Se ejecuta la consulta y se devuelve un ResultSet con los datos.
        return st.executeQuery(sql);
    }

    // ------------------ MÉTODO MODIFICAR U------------------
    public void modificarArticulo(String cod, String nom, String uni, int pre, int stk, String marca) throws SQLException {
        Connection con = Conexion.getConexion();
        // Consulta SQL para actualizar un artículo, buscando por su código (ART_COD).
        String sql = "UPDATE ARTICULO SET ART_NOM=?, ART_UNI=?, ART_PRE=?, ART_STK=?, ART_MARCA=? WHERE ART_COD=?";
        PreparedStatement ps = con.prepareStatement(sql);
        // Se asignan los valores de actualización.
        ps.setString(1, nom);
        ps.setString(2, uni);
        ps.setInt(3, pre);
        ps.setInt(4, stk);
        ps.setString(5, marca);
        ps.setString(6, cod); // El artículo a modificar se identifica por su código.
        // Se ejecuta la actualización.
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    // ------------------ MÉTODO ELIMINAR D------------------
    public void eliminarArticulo(String cod) throws SQLException {
        Connection con = Conexion.getConexion();
        // Consulta SQL para eliminar un artículo por su código.
        String sql = "DELETE FROM ARTICULO WHERE ART_COD=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cod);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    // ------------------ MÉTODO BUSCAR ------------------
    public ResultSet buscarArticulo(String cod) throws SQLException {
        Connection con = Conexion.getConexion();
        // Consulta SQL para buscar un artículo específico por su código.
        String sql = "SELECT * FROM ARTICULO WHERE ART_COD=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cod);
        // Devuelve el resultado de la consulta (puede contener 0 o 1 artículo).
        return ps.executeQuery();
    }
}
