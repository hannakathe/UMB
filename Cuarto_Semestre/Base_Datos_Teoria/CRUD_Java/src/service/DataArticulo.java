package service;

import model.Conexion;

import java.sql.*;

public class DataArticulo {

    public ResultSet listarArticulos() throws SQLException {
        Connection con = Conexion.getConexion();
        String sql = "SELECT * FROM ARTICULO";
        Statement st = con.createStatement();
        return st.executeQuery(sql);
    }

    public void insertarArticulo(String cod, String nom, String uni, int pre, int stk, String marca) throws SQLException {
        Connection con = Conexion.getConexion();
        String sql = "INSERT INTO ARTICULO VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cod);
        ps.setString(2, nom);
        ps.setString(3, uni);
        ps.setInt(4, pre);
        ps.setInt(5, stk);
        ps.setString(6, marca);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public void modificarArticulo(String cod, String nom, String uni, int pre, int stk, String marca) throws SQLException {
        Connection con = Conexion.getConexion();
        String sql = "UPDATE ARTICULO SET ART_NOM=?, ART_UNI=?, ART_PRE=?, ART_STK=?, ART_MARCA=? WHERE ART_COD=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, nom);
        ps.setString(2, uni);
        ps.setInt(3, pre);
        ps.setInt(4, stk);
        ps.setString(5, marca);
        ps.setString(6, cod);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public void eliminarArticulo(String cod) throws SQLException {
        Connection con = Conexion.getConexion();
        String sql = "DELETE FROM ARTICULO WHERE ART_COD=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cod);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    public ResultSet buscarArticulo(String cod) throws SQLException {
    Connection con = Conexion.getConexion();
    String sql = "SELECT * FROM ARTICULO WHERE ART_COD=?";
    PreparedStatement ps = con.prepareStatement(sql);
    ps.setString(1, cod);
    return ps.executeQuery();
}

}


