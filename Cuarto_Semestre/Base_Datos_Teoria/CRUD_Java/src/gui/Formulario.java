package gui;

import service.DataArticulo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Formulario extends JFrame {

    private JTextField txtCodigo, txtNombre, txtUnidad, txtPrecio, txtCantidad, txtMarca;
    private JButton btnNuevo, btnGrabar, btnModificar, btnEliminar, btnSalir;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private DataArticulo data;

    public Formulario() {
        setTitle("Gestión de Artículos");
        setSize(850, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        data = new DataArticulo();

        // Labels y TextFields
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setBounds(20, 20, 100, 25);
        add(lblCodigo);

        txtCodigo = new JTextField();
        txtCodigo.setBounds(130, 20, 150, 25);
        add(txtCodigo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 60, 100, 25);
        add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 60, 150, 25);
        add(txtNombre);

        JLabel lblUnidad = new JLabel("Unidad:");
        lblUnidad.setBounds(20, 100, 100, 25);
        add(lblUnidad);

        txtUnidad = new JTextField();
        txtUnidad.setBounds(130, 100, 150, 25);
        add(txtUnidad);

        JLabel lblPrecio = new JLabel("Precio:");
        lblPrecio.setBounds(20, 140, 100, 25);
        add(lblPrecio);

        txtPrecio = new JTextField();
        txtPrecio.setBounds(130, 140, 150, 25);
        add(txtPrecio);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(20, 180, 100, 25);
        add(lblCantidad);

        txtCantidad = new JTextField();
        txtCantidad.setBounds(130, 180, 150, 25);
        add(txtCantidad);

        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setBounds(20, 220, 100, 25);
        add(lblMarca);

        txtMarca = new JTextField();
        txtMarca.setBounds(130, 220, 150, 25);
        add(txtMarca);

        // Botones
        btnNuevo = new JButton("Nuevo");
        btnNuevo.setBounds(20, 270, 100, 30);
        add(btnNuevo);

        btnGrabar = new JButton("Grabar");
        btnGrabar.setBounds(130, 270, 100, 30);
        add(btnGrabar);

        btnModificar = new JButton("Modificar");
        btnModificar.setBounds(240, 270, 100, 30);
        add(btnModificar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(350, 270, 100, 30);
        add(btnEliminar);

        btnSalir = new JButton("Salir");
        btnSalir.setBounds(460, 270, 100, 30);
        add(btnSalir);

        // Tabla
        String[] columnas = {"Código", "Nombre", "Unidad", "Precio", "Stock", "Marca"};
        modeloTabla = new DefaultTableModel(null, columnas);
        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBounds(320, 20, 500, 200);
        add(scroll);

        // Acciones
        btnSalir.addActionListener(_ -> System.exit(0));
        btnNuevo.addActionListener(_ -> limpiarCampos());
        btnGrabar.addActionListener(_ -> grabarArticulo());
        btnModificar.addActionListener(_ -> modificarArticulo());
        btnEliminar.addActionListener(_ -> eliminarArticulo());

        cargarDatos();
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtUnidad.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtMarca.setText("");
    }

    private void cargarDatos() {
        modeloTabla.setRowCount(0);
        try {
            ResultSet rs = data.listarArticulos();
            while (rs.next()) {
                Object[] fila = {
                        rs.getString("ART_COD"),
                        rs.getString("ART_NOM"),
                        rs.getString("ART_UNI"),
                        rs.getInt("ART_PRE"),
                        rs.getInt("ART_STK"),
                        rs.getString("ART_MARCA")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void grabarArticulo() {
        try {
            data.insertarArticulo(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    txtUnidad.getText(),
                    Integer.parseInt(txtPrecio.getText()),
                    Integer.parseInt(txtCantidad.getText()),
                    txtMarca.getText()
            );
            cargarDatos();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al grabar: " + e.getMessage());
        }
    }

    private void modificarArticulo() {
        try {
            data.modificarArticulo(
                    txtCodigo.getText(),
                    txtNombre.getText(),
                    txtUnidad.getText(),
                    Integer.parseInt(txtPrecio.getText()),
                    Integer.parseInt(txtCantidad.getText()),
                    txtMarca.getText()
            );
            cargarDatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarArticulo() {
        try {
            data.eliminarArticulo(txtCodigo.getText());
            cargarDatos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }
}
