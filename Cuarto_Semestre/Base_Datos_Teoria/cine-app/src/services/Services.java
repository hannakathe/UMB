package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// -------------------- CLIENTES --------------------
class ClienteService {
    public void create(String nombre, String documento, String telefono) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, documento, telefono) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, documento);
            ps.setString(3, telefono);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                clientes.add(new String[]{
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("documento"),
                        rs.getString("telefono")
                });
            }
        }
        return clientes;
    }

    public void update(int id, String nombre, String documento, String telefono) throws SQLException {
        String sql = "UPDATE clientes SET nombre=?, documento=?, telefono=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, documento);
            ps.setString(3, telefono);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- PELÍCULAS --------------------
class PeliculaService {
    public void create(String titulo, String genero, double valorBoleta) throws SQLException {
        String sql = "INSERT INTO peliculas (titulo, genero, valor_boleta) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, genero);
            ps.setDouble(3, valorBoleta);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> peliculas = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                peliculas.add(new String[]{
                        rs.getString("id"),
                        rs.getString("titulo"),
                        rs.getString("genero"),
                        rs.getString("valor_boleta")
                });
            }
        }
        return peliculas;
    }

    public void update(int id, String titulo, String genero, double valorBoleta) throws SQLException {
        String sql = "UPDATE peliculas SET titulo=?, genero=?, valor_boleta=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, titulo);
            ps.setString(2, genero);
            ps.setDouble(3, valorBoleta);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM peliculas WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- SALAS --------------------
class SalaService {
    public void create(String tipoSala) throws SQLException {
        String sql = "INSERT INTO salas (tipo_sala) VALUES (?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipoSala);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> salas = new ArrayList<>();
        String sql = "SELECT * FROM salas";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                salas.add(new String[]{
                        rs.getString("id"),
                        rs.getString("tipo_sala")
                });
            }
        }
        return salas;
    }

    public void update(int id, String tipoSala) throws SQLException {
        String sql = "UPDATE salas SET tipo_sala=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tipoSala);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM salas WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- FUNCIONES --------------------
class FuncionService {
    public void create(int salaId, int peliculaId, String fechaHora) throws SQLException {
        String sql = "INSERT INTO funciones (sala_id, pelicula_id, fecha_hora) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setInt(2, peliculaId);
            ps.setString(3, fechaHora);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> funciones = new ArrayList<>();
        String sql = "SELECT * FROM funciones";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                funciones.add(new String[]{
                        rs.getString("id"),
                        rs.getString("sala_id"),
                        rs.getString("pelicula_id"),
                        rs.getString("fecha_hora")
                });
            }
        }
        return funciones;
    }

    public void update(int id, int salaId, int peliculaId, String fechaHora) throws SQLException {
        String sql = "UPDATE funciones SET sala_id=?, pelicula_id=?, fecha_hora=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setInt(2, peliculaId);
            ps.setString(3, fechaHora);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM funciones WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- ASIENTOS --------------------
class AsientoService {
    public void create(int salaId, String numeroSilla) throws SQLException {
        String sql = "INSERT INTO asientos (sala_id, numero_silla) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setString(2, numeroSilla);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> asientos = new ArrayList<>();
        String sql = "SELECT * FROM asientos";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                asientos.add(new String[]{
                        rs.getString("id"),
                        rs.getString("sala_id"),
                        rs.getString("numero_silla")
                });
            }
        }
        return asientos;
    }

    public void update(int id, int salaId, String numeroSilla) throws SQLException {
        String sql = "UPDATE asientos SET sala_id=?, numero_silla=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, salaId);
            ps.setString(2, numeroSilla);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM asientos WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- ENTRADAS --------------------
class EntradaService {
    public void create(int asientoId, int funcionId, int clienteId, double valor,
                       String tipoPago, String fechaCompra) throws SQLException {
        String sql = "INSERT INTO entradas (asiento_id, funcion_id, cliente_id, valor, tipo_pago, fecha_compra) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asientoId);
            ps.setInt(2, funcionId);
            ps.setInt(3, clienteId);
            ps.setDouble(4, valor);
            ps.setString(5, tipoPago);
            ps.setString(6, fechaCompra);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> entradas = new ArrayList<>();
        String sql = "SELECT * FROM entradas";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                entradas.add(new String[]{
                        rs.getString("id"),
                        rs.getString("asiento_id"),
                        rs.getString("funcion_id"),
                        rs.getString("cliente_id"),
                        rs.getString("valor"),
                        rs.getString("tipo_pago"),
                        rs.getString("fecha_compra")
                });
            }
        }
        return entradas;
    }

    public void update(int id, int asientoId, int funcionId, int clienteId, double valor,
                       String tipoPago, String fechaCompra) throws SQLException {
        String sql = "UPDATE entradas SET asiento_id=?, funcion_id=?, cliente_id=?, valor=?, tipo_pago=?, fecha_compra=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, asientoId);
            ps.setInt(2, funcionId);
            ps.setInt(3, clienteId);
            ps.setDouble(4, valor);
            ps.setString(5, tipoPago);
            ps.setString(6, fechaCompra);
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM entradas WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- FACTURAS --------------------
class FacturaService {
    public void create(int clienteId, String empresaNombre, String empresaDocumento,
                       String contacto, String fecha, String tipoPago, double valorTotal) throws SQLException {
        String sql = "INSERT INTO facturas (cliente_id, empresa_nombre, empresa_documento, contacto, fecha, tipo_pago, valor_total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setString(2, empresaNombre);
            ps.setString(3, empresaDocumento);
            ps.setString(4, contacto);
            ps.setString(5, fecha);
            ps.setString(6, tipoPago);
            ps.setDouble(7, valorTotal);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                facturas.add(new String[]{
                        rs.getString("id"),
                        rs.getString("cliente_id"),
                        rs.getString("empresa_nombre"),
                        rs.getString("empresa_documento"),
                        rs.getString("contacto"),
                        rs.getString("fecha"),
                        rs.getString("tipo_pago"),
                        rs.getString("valor_total")
                });
            }
        }
        return facturas;
    }

    public void update(int id, int clienteId, String empresaNombre, String empresaDocumento,
                       String contacto, String fecha, String tipoPago, double valorTotal) throws SQLException {
        String sql = "UPDATE facturas SET cliente_id=?, empresa_nombre=?, empresa_documento=?, contacto=?, fecha=?, tipo_pago=?, valor_total=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setString(2, empresaNombre);
            ps.setString(3, empresaDocumento);
            ps.setString(4, contacto);
            ps.setString(5, fecha);
            ps.setString(6, tipoPago);
            ps.setDouble(7, valorTotal);
            ps.setInt(8, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM facturas WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}

// -------------------- DETALLE FACTURA --------------------
class DetalleFacturaService {
    public void create(int facturaId, int entradaId, int cantidad, double valorUnitario, double valorTotal) throws SQLException {
        String sql = "INSERT INTO detalle_factura (factura_id, entrada_id, cantidad, valor_unitario, valor_total) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facturaId);
            ps.setInt(2, entradaId);
            ps.setInt(3, cantidad);
            ps.setDouble(4, valorUnitario);
            ps.setDouble(5, valorTotal);
            ps.executeUpdate();
        }
    }

    public List<String[]> getAll() throws SQLException {
        List<String[]> detalles = new ArrayList<>();
        String sql = "SELECT * FROM detalle_factura";
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                detalles.add(new String[]{
                        rs.getString("id"),
                        rs.getString("factura_id"),
                        rs.getString("entrada_id"),
                        rs.getString("cantidad"),
                        rs.getString("valor_unitario"),
                        rs.getString("valor_total")
                });
            }
        }
        return detalles;
    }

    public void update(int id, int facturaId, int entradaId, int cantidad, double valorUnitario, double valorTotal) throws SQLException {
        String sql = "UPDATE detalle_factura SET factura_id=?, entrada_id=?, cantidad=?, valor_unitario=?, valor_total=? WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, facturaId);
            ps.setInt(2, entradaId);
            ps.setInt(3, cantidad);
            ps.setDouble(4, valorUnitario);
            ps.setDouble(5, valorTotal);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM detalle_factura WHERE id=?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
