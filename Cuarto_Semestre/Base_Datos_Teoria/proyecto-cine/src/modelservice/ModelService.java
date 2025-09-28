package modelservice;

import java.sql.*;
import java.util.*;
import java.io.*;

/**
 * ModelService: Contiene modelos + servicios (CRUD) en un solo archivo.
 * Usa SQLite: archivo de BD 'proyecto_cine.db' en la raíz del proyecto.
 */
public class ModelService {
    private static final String DB_URL = "jdbc:sqlite:proyecto_cine.db";

    // ----------------------
    // Database helper
    // ----------------------
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection()) {
            Statement st = conn.createStatement();
            File f = new File("database/init_db.sql");
            if (f.exists()) {
                StringBuilder sb = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
                // separar por ';' y ejecutar cada comando (simple)
                String[] commands = sb.toString().split(";");
                for (String cmd : commands) {
                    String t = cmd.trim();
                    if (t.isEmpty()) continue;
                    try {
                        st.execute(t);
                    } catch (SQLException ex) {
                        // algunas sentencias pueden producir errores si ya existen; las ignoramos
                    }
                }
            } else {
                // Fallback: crear tablas mínimos (en caso de no tener el SQL)
                st.execute("PRAGMA foreign_keys = ON");
                st.execute("CREATE TABLE IF NOT EXISTS clientes (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, documento TEXT UNIQUE NOT NULL, telefono TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS peliculas (id INTEGER PRIMARY KEY AUTOINCREMENT, titulo TEXT NOT NULL, genero TEXT, valor_boleta REAL DEFAULT 0)");
                st.execute("CREATE TABLE IF NOT EXISTS salas (id INTEGER PRIMARY KEY AUTOINCREMENT, tipo_sala TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS funciones (id INTEGER PRIMARY KEY AUTOINCREMENT, sala_id INTEGER, pelicula_id INTEGER, fecha_hora TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS asientos (id INTEGER PRIMARY KEY AUTOINCREMENT, sala_id INTEGER, numero_silla TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS entradas (id INTEGER PRIMARY KEY AUTOINCREMENT, asiento_id INTEGER, funcion_id INTEGER, cliente_id INTEGER, valor REAL, tipo_pago TEXT, fecha_compra TEXT)");
                st.execute("CREATE TABLE IF NOT EXISTS facturas (id INTEGER PRIMARY KEY AUTOINCREMENT, cliente_id INTEGER, empresa_nombre TEXT, empresa_documento TEXT, contacto TEXT, fecha TEXT, tipo_pago TEXT, valor_total REAL)");
                st.execute("CREATE TABLE IF NOT EXISTS detalle_factura (id INTEGER PRIMARY KEY AUTOINCREMENT, factura_id INTEGER, entrada_id INTEGER, cantidad INTEGER, valor_unitario REAL, valor_total REAL)");
            }
            st.close();
            System.out.println("BD inicializada OK (archivo: proyecto_cine.db)");
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
        public Cliente(int id, String n, String d, String t) { this.id=id; this.nombre=n; this.documento=d; this.telefono=t;}
    }

    public static class Pelicula {
        public int id;
        public String titulo;
        public String genero;
        public double valor_boleta;
        public Pelicula() {}
        public Pelicula(int id, String titulo, String genero, double valor_boleta) { this.id=id; this.titulo=titulo; this.genero=genero; this.valor_boleta=valor_boleta;}
    }

    public static class Sala { public int id; public String tipo_sala; }
    public static class Funcion { public int id; public int sala_id; public int pelicula_id; public String fecha_hora; }
    public static class Asiento { public int id; public int sala_id; public String numero_silla; }
    public static class Entrada {
        public int id; public int asiento_id; public int funcion_id; public int cliente_id; public double valor; public String tipo_pago; public String fecha_compra;
    }
    public static class Factura {
        public int id; public int cliente_id; public String empresa_nombre; public String empresa_documento; public String contacto; public String fecha; public String tipo_pago; public double valor_total;
    }
    public static class DetalleFactura {
        public int id; public int factura_id; public int entrada_id; public int cantidad; public double valor_unitario; public double valor_total;
    }

    // ----------------------
    // CRUD Clientes
    // ----------------------
    public static List<Cliente> getAllClientes() {
        List<Cliente> list = new ArrayList<>();
        String sql = "SELECT id, nombre, documento, telefono FROM clientes ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Cliente getClienteById(int id) {
        String sql = "SELECT id, nombre, documento, telefono FROM clientes WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("documento"), rs.getString("telefono"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean insertCliente(Cliente cl) {
        String sql = "INSERT INTO clientes (nombre, documento, telefono) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.nombre); ps.setString(2, cl.documento); ps.setString(3, cl.telefono);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateCliente(Cliente cl) {
        String sql = "UPDATE clientes SET nombre = ?, documento = ?, telefono = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, cl.nombre); ps.setString(2, cl.documento); ps.setString(3, cl.telefono); ps.setInt(4, cl.id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Peliculas
    // ----------------------
    public static List<Pelicula> getAllPeliculas() {
        List<Pelicula> list = new ArrayList<>();
        String sql = "SELECT id, titulo, genero, valor_boleta FROM peliculas ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) list.add(new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero"), rs.getDouble("valor_boleta")));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Pelicula getPeliculaById(int id) {
        String sql = "SELECT id, titulo, genero, valor_boleta FROM peliculas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero"), rs.getDouble("valor_boleta"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean insertPelicula(Pelicula p) {
        String sql = "INSERT INTO peliculas (titulo, genero, valor_boleta) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,p.titulo); ps.setString(2,p.genero); ps.setDouble(3,p.valor_boleta); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updatePelicula(Pelicula p) {
        String sql = "UPDATE peliculas SET titulo = ?, genero = ?, valor_boleta = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1,p.titulo); ps.setString(2,p.genero); ps.setDouble(3,p.valor_boleta); ps.setInt(4,p.id); return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deletePelicula(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Salas
    // ----------------------
    public static List<Sala> getAllSalas() {
        List<Sala> list = new ArrayList<>();
        String sql = "SELECT id, tipo_sala FROM salas ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) { Sala s = new Sala(); s.id = rs.getInt("id"); s.tipo_sala = rs.getString("tipo_sala"); list.add(s); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertSala(Sala s) {
        String sql = "INSERT INTO salas (tipo_sala) VALUES (?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1,s.tipo_sala); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateSala(Sala s) {
        String sql = "UPDATE salas SET tipo_sala = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setString(1,s.tipo_sala); ps.setInt(2,s.id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteSala(int id) {
        String sql = "DELETE FROM salas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Funciones
    // ----------------------
    public static List<Funcion> getAllFunciones() {
        List<Funcion> list = new ArrayList<>();
        String sql = "SELECT id, sala_id, pelicula_id, fecha_hora FROM funciones ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) { Funcion f = new Funcion(); f.id = rs.getInt("id"); f.sala_id = rs.getInt("sala_id"); f.pelicula_id = rs.getInt("pelicula_id"); f.fecha_hora = rs.getString("fecha_hora"); list.add(f); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertFuncion(Funcion f) {
        String sql = "INSERT INTO funciones (sala_id, pelicula_id, fecha_hora) VALUES (?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,f.sala_id); ps.setInt(2,f.pelicula_id); ps.setString(3,f.fecha_hora); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateFuncion(Funcion f) {
        String sql = "UPDATE funciones SET sala_id = ?, pelicula_id = ?, fecha_hora = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,f.sala_id); ps.setInt(2,f.pelicula_id); ps.setString(3,f.fecha_hora); ps.setInt(4,f.id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteFuncion(int id) {
        String sql = "DELETE FROM funciones WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Asientos
    // ----------------------
    public static List<Asiento> getAllAsientos() {
        List<Asiento> list = new ArrayList<>();
        String sql = "SELECT id, sala_id, numero_silla FROM asientos ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) { Asiento a = new Asiento(); a.id = rs.getInt("id"); a.sala_id = rs.getInt("sala_id"); a.numero_silla = rs.getString("numero_silla"); list.add(a); }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertAsiento(Asiento a) {
        String sql = "INSERT INTO asientos (sala_id, numero_silla) VALUES (?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,a.sala_id); ps.setString(2,a.numero_silla); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateAsiento(Asiento a) {
        String sql = "UPDATE asientos SET sala_id = ?, numero_silla = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,a.sala_id); ps.setString(2,a.numero_silla); ps.setInt(3,a.id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteAsiento(int id) {
        String sql = "DELETE FROM asientos WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Entradas
    // ----------------------
    public static List<Entrada> getAllEntradas() {
        List<Entrada> list = new ArrayList<>();
        String sql = "SELECT id, asiento_id, funcion_id, cliente_id, valor, tipo_pago, fecha_compra FROM entradas ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Entrada e = new Entrada();
                e.id = rs.getInt("id");
                e.asiento_id = rs.getInt("asiento_id");
                e.funcion_id = rs.getInt("funcion_id");
                e.cliente_id = rs.getInt("cliente_id");
                e.valor = rs.getDouble("valor");
                e.tipo_pago = rs.getString("tipo_pago");
                e.fecha_compra = rs.getString("fecha_compra");
                list.add(e);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Entrada getEntradaById(int id) {
        String sql = "SELECT id, asiento_id, funcion_id, cliente_id, valor, tipo_pago, fecha_compra FROM entradas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Entrada e = new Entrada();
                    e.id = rs.getInt("id");
                    e.asiento_id = rs.getInt("asiento_id");
                    e.funcion_id = rs.getInt("funcion_id");
                    e.cliente_id = rs.getInt("cliente_id");
                    e.valor = rs.getDouble("valor");
                    e.tipo_pago = rs.getString("tipo_pago");
                    e.fecha_compra = rs.getString("fecha_compra");
                    return e;
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return null;
    }

    public static boolean insertEntrada(Entrada e) {
        String sql = "INSERT INTO entradas (asiento_id, funcion_id, cliente_id, valor, tipo_pago, fecha_compra) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,e.asiento_id); ps.setInt(2,e.funcion_id); ps.setInt(3,e.cliente_id);
            ps.setDouble(4,e.valor); ps.setString(5,e.tipo_pago); ps.setString(6,e.fecha_compra);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public static boolean updateEntrada(Entrada e) {
        String sql = "UPDATE entradas SET asiento_id = ?, funcion_id = ?, cliente_id = ?, valor = ?, tipo_pago = ?, fecha_compra = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,e.asiento_id); ps.setInt(2,e.funcion_id); ps.setInt(3,e.cliente_id);
            ps.setDouble(4,e.valor); ps.setString(5,e.tipo_pago); ps.setString(6,e.fecha_compra); ps.setInt(7,e.id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public static boolean deleteEntrada(int id) {
        String sql = "DELETE FROM entradas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // CRUD Facturas + Detalle
    // ----------------------
    public static List<Factura> getAllFacturas() {
        List<Factura> list = new ArrayList<>();
        String sql = "SELECT id, cliente_id, empresa_nombre, empresa_documento, contacto, fecha, tipo_pago, valor_total FROM facturas ORDER BY id";
        try (Connection c = getConnection(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Factura f = new Factura();
                f.id = rs.getInt("id");
                f.cliente_id = rs.getInt("cliente_id");
                f.empresa_nombre = rs.getString("empresa_nombre");
                f.empresa_documento = rs.getString("empresa_documento");
                f.contacto = rs.getString("contacto");
                f.fecha = rs.getString("fecha");
                f.tipo_pago = rs.getString("tipo_pago");
                f.valor_total = rs.getDouble("valor_total");
                list.add(f);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static Factura getFacturaById(int id) {
        String sql = "SELECT id, cliente_id, empresa_nombre, empresa_documento, contacto, fecha, tipo_pago, valor_total FROM facturas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Factura f = new Factura();
                    f.id = rs.getInt("id");
                    f.cliente_id = rs.getInt("cliente_id");
                    f.empresa_nombre = rs.getString("empresa_nombre");
                    f.empresa_documento = rs.getString("empresa_documento");
                    f.contacto = rs.getString("contacto");
                    f.fecha = rs.getString("fecha");
                    f.tipo_pago = rs.getString("tipo_pago");
                    f.valor_total = rs.getDouble("valor_total");
                    return f;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public static boolean insertFactura(Factura f) {
        String sql = "INSERT INTO facturas (cliente_id, empresa_nombre, empresa_documento, contacto, fecha, tipo_pago, valor_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,f.cliente_id);
            ps.setString(2,f.empresa_nombre);
            ps.setString(3,f.empresa_documento);
            ps.setString(4,f.contacto);
            ps.setString(5,f.fecha);
            ps.setString(6,f.tipo_pago);
            ps.setDouble(7,f.valor_total);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean updateFactura(Factura f) {
        String sql = "UPDATE facturas SET cliente_id = ?, empresa_nombre = ?, empresa_documento = ?, contacto = ?, fecha = ?, tipo_pago = ?, valor_total = ? WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,f.cliente_id); ps.setString(2,f.empresa_nombre); ps.setString(3,f.empresa_documento); ps.setString(4,f.contacto);
            ps.setString(5,f.fecha); ps.setString(6,f.tipo_pago); ps.setDouble(7,f.valor_total); ps.setInt(8,f.id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteFactura(int id) {
        String sql = "DELETE FROM facturas WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Detalle factura
    public static List<DetalleFactura> getDetalleByFacturaId(int facturaId) {
        List<DetalleFactura> list = new ArrayList<>();
        String sql = "SELECT id, factura_id, entrada_id, cantidad, valor_unitario, valor_total FROM detalle_factura WHERE factura_id = ? ORDER BY id";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,facturaId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleFactura d = new DetalleFactura();
                    d.id = rs.getInt("id");
                    d.factura_id = rs.getInt("factura_id");
                    d.entrada_id = rs.getInt("entrada_id");
                    d.cantidad = rs.getInt("cantidad");
                    d.valor_unitario = rs.getDouble("valor_unitario");
                    d.valor_total = rs.getDouble("valor_total");
                    list.add(d);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public static boolean insertDetalleFactura(DetalleFactura d) {
        String sql = "INSERT INTO detalle_factura (factura_id, entrada_id, cantidad, valor_unitario, valor_total) VALUES (?, ?, ?, ?, ?)";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1,d.factura_id); ps.setInt(2,d.entrada_id); ps.setInt(3,d.cantidad); ps.setDouble(4,d.valor_unitario); ps.setDouble(5,d.valor_total);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteDetalle(int id) {
        String sql = "DELETE FROM detalle_factura WHERE id = ?";
        try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) { ps.setInt(1,id); return ps.executeUpdate() > 0; }
        catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ----------------------
    // Helpers: obtener listas para comboboxes (id + descripcion)
    // ----------------------
    public static Map<Integer, String> mapClientes() {
        Map<Integer, String> m = new LinkedHashMap<>();
        for (Cliente c : getAllClientes()) m.put(c.id, c.nombre + " (" + c.documento + ")");
        return m;
    }
    public static Map<Integer, String> mapPeliculas() {
        Map<Integer, String> m = new LinkedHashMap<>();
        for (Pelicula p : getAllPeliculas()) m.put(p.id, p.titulo + " [" + p.genero + "]");
        return m;
    }
    public static Map<Integer, String> mapFunciones() {
        Map<Integer, String> m = new LinkedHashMap<>();
        for (Funcion f : getAllFunciones()) {
            String desc = "ID " + f.id + " - Sala:" + f.sala_id + " Pel:" + f.pelicula_id + " " + f.fecha_hora;
            m.put(f.id, desc);
        }
        return m;
    }
    public static Map<Integer, String> mapAsientos() {
        Map<Integer, String> m = new LinkedHashMap<>();
        for (Asiento a : getAllAsientos()) m.put(a.id, "S" + a.id + " - " + a.numero_silla + " (Sala " + a.sala_id + ")");
        return m;
    }
}
