package modelservice;

import java.sql.*;
import java.util.*;
import java.io.*;

public class ModelService {
    private static final String DB_URL = "jdbc:sqlite:proyecto_cine.db";

    // ----------------------
    // Database helper
    // ----------------------
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // Ejecuta script SQL de inicialización (si la BD no existe, crea tablas)
    public static void initDatabase() {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            // Intentamos crear con sentencias; si quieres ejecutar el archivo .sql, puedes leerlo
            // Aquí asumo que ya ejecutaste init_db.sql manualmente o lo puedes ejecutar una vez:
            // Para automatizar: intenta leer database/init_db.sql si existe
            File f = new File("database/init_db.sql");
            if (f.exists()) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                String[] commands = sb.toString().split(";");
                for (String cmd : commands) {
                    if (cmd.trim().isEmpty()) continue;
                    st.execute(cmd);
                }
            } else {
                // fallback: crear tablas mínimas si init_db.sql no está presente
                st.execute("PRAGMA foreign_keys = ON");
                st.execute("CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, documento TEXT UNIQUE NOT NULL, telefono TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS peliculas (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT NOT NULL, genero TEXT, valor REAL DEFAULT 0)");
                st.execute("CREATE TABLE IF NOT EXISTS salas (id INTEGER PRIMARY KEY AUTOINCREMENT, tipo_sala TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS funciones (id INTEGER PRIMARY KEY AUTOINCREMENT, sala_id INTEGER, pelicula_id INTEGER, fecha_hora TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS asientos (id INTEGER PRIMARY KEY AUTOINCREMENT, sala_id INTEGER, numero_silla TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS entradas (id INTEGER PRIMARY KEY AUTOINCREMENT, asiento_id INTEGER, funcion_id INTEGER, cliente_id INTEGER, valor REAL)");
                st.execute("CREATE TABLE IF NOT EXISTS facturas (id INTEGER PRIMARY KEY AUTOINCREMENT, cliente_id INTEGER, empresa_nombre TEXT, empresa_documento TEXT, contacto TEXT, fecha TEXT, valor_total REAL)");
                st.execute("CREATE TABLE IF NOT EXISTS detalle_factura (id INTEGER PRIMARY KEY AUTOINCREMENT, factura_id INTEGER, entrada_id INTEGER, cantidad INTEGER, valor_unitario REAL, valor_total REAL)");
            }
            st.close();
            System.out.println("BD inicializada OK");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------------
    // MODELS
    // ----------------------
    public static class Cliente {
        public int id;
        public String nombre;
        public String documento;
        public String telefono;

        public Cliente() {}
        public Cliente(int id, String nombre, String documento, String telefono) {
            this.id = id; this.nombre = nombre; this.documento = documento; this.telefono = telefono;
        }
        public String toString() { return nombre + " (" + documento + ")"; }
    }

    public static class Pelicula {
        public int id;
        public String titulo;
        public String genero;
        public double valor;
        public Pelicula() {}
        public Pelicula(int id, String titulo, String genero, double valor) {
            this.id=id; this.titulo=titulo; this.genero=genero; this.valor=valor;
        }
    }

    // Otras clases (salas, funciones, asientos, entradas, facturas, detalle_factura)
    // Puedes extender de forma análoga
    public static class Sala { public int id; public String tipo_sala; }
    public static class Funcion { public int id; public int sala_id; public int pelicula_id; public String fecha_hora; }
    public static class Asiento { public int id; public int sala_id; public String numero_silla; }
    public static class Entrada { public int id; public int asiento_id; public int funcion_id; public int cliente_id; public double valor; }
    public static class Factura { public int id; public int cliente_id; public String empresa_nombre; public String empresa_documento; public String contacto; public String fecha; public double valor_total; }
    public static class DetalleFactura { public int id; public int factura_id; public int entrada_id; public int cantidad; public double valor_unitario; public double valor_total; }

    // ----------------------
    // CRUD Clientes
    // ----------------------
    public static List<Cliente> getAllClientes() {
        List<Cliente> list = new ArrayList<>();
        String sql = "SELECT id, nombre, documento, telefono FROM clientes ORDER BY id";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Cliente getClienteById(int id) {
        String sql = "SELECT id, nombre, documento, telefono FROM clientes WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean insertCliente(Cliente cl) {
        String sql = "INSERT INTO clientes (nombre, documento, telefono) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.nombre);
            ps.setString(2, cl.documento);
            ps.setString(3, cl.telefono);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateCliente(Cliente cl) {
        String sql = "UPDATE clientes SET nombre = ?, documento = ?, telefono = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.nombre);
            ps.setString(2, cl.documento);
            ps.setString(3, cl.telefono);
            ps.setInt(4, cl.id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Peliculas
    // ----------------------
    public static List<Pelicula> getAllPeliculas() {
        List<Pelicula> list = new ArrayList<>();
        String sql = "SELECT id, titulo, genero, valor FROM peliculas ORDER BY id";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero"), rs.getDouble("valor")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertPelicula(Pelicula p) {
        String sql = "INSERT INTO peliculas (titulo, genero, valor) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.titulo);
            ps.setString(2, p.genero);
            ps.setDouble(3, p.valor);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updatePelicula(Pelicula p) {
        String sql = "UPDATE peliculas SET titulo = ?, genero = ?, valor = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.titulo);
            ps.setString(2, p.genero);
            ps.setDouble(3, p.valor);
            ps.setInt(4, p.id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deletePelicula(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // SKELETONS para las otras entidades (puedes replicar el patrón anterior)
    // ----------------------
    public static List<Sala> getAllSalas() {
        List<Sala> list = new ArrayList<>();
        String sql = "SELECT id, tipo_sala FROM salas";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Sala s = new Sala();
                s.id = rs.getInt("id");
                s.tipo_sala = rs.getString("tipo_sala");
                list.add(s);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Agrega insert/update/delete para salas, funciones, asientos, entradas, facturas, detalle_factura siguiendo el patrón anterior.
}
