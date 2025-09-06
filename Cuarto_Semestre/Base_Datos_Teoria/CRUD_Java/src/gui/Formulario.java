package gui;
// Define el paquete donde está esta clase (en este caso "gui").

// Se suele usar para las clases relacionadas con la interfaz gráfica.

import service.DataArticulo;
// Importa la clase de servicio que contiene los métodos CRUD para interactuar con la BD.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
// Importaciones necesarias para trabajar con Swing (interfaz gráfica),
// modelos de tabla y conexiones SQL.

public class Formulario extends JFrame {
    // La clase Formulario hereda de JFrame, por lo que representa una ventana.

    // Campos de texto para capturar los datos del artículo.
    private JTextField txtCodigo, txtNombre, txtUnidad, txtPrecio, txtCantidad, txtMarca;

    // Botones para realizar acciones CRUD y salir.
    private JButton btnNuevo, btnGrabar, btnModificar, btnEliminar, btnSalir;

    // Tabla y modelo para mostrar los artículos.
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Objeto de la capa de servicio para acceder a la BD.
    private DataArticulo data;

    // ------------------ CONSTRUCTOR ------------------
    public Formulario() {
        // Configuración básica de la ventana.
        setTitle("Gestión de Artículos"); // Título de la ventana
        setSize(850, 450); // Tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra la app al cerrar ventana
        setLocationRelativeTo(null); // Centra la ventana en pantalla
        setLayout(null); // Usa posiciones absolutas (sin layout manager)

        data = new DataArticulo(); // Inicializa la capa de servicio





        // ------------------ LABELS Y CAMPOS ------------------
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

        // ------------------ BOTONES ------------------
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

        // ------------------ TABLA ------------------
        String[] columnas = { "Código", "Nombre", "Unidad", "Precio", "Stock", "Marca" };
        // Sobrescribimos DefaultTableModel para que las celdas no sean editables
        modeloTabla = new DefaultTableModel(null, columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Ninguna celda será editable
            }
        };

        tabla = new JTable(modeloTabla);

        JScrollPane scroll = new JScrollPane(tabla); // Scroll para la tabla
        scroll.setBounds(320, 20, 500, 200);
        add(scroll);

        // ------------------ ACCIONES DE BOTONES ------------------
        btnSalir.addActionListener(_ -> System.exit(0)); // Cierra la aplicación
        btnNuevo.addActionListener(_ -> limpiarCampos()); // Limpia los campos
        btnGrabar.addActionListener(_ -> grabarArticulo()); // Inserta artículo
        btnModificar.addActionListener(_ -> modificarArticulo()); // Modifica artículo
        btnEliminar.addActionListener(_ -> eliminarArticulo()); // Elimina artículo

        cargarDatos(); // Cargar los datos desde la BD al iniciar
    }

    // ------------------ MÉTODOS AUXILIARES ------------------
    private void limpiarCampos() {
        // Limpia todos los campos de texto
        txtCodigo.setText("");
        txtNombre.setText("");
        txtUnidad.setText("");
        txtPrecio.setText("");
        txtCantidad.setText("");
        txtMarca.setText("");
    }

    private void cargarDatos() {
        // Limpia la tabla antes de recargar los datos
        modeloTabla.setRowCount(0);
        try {
            // Consulta todos los artículos desde la BD
            ResultSet rs = data.listarArticulos();
            while (rs.next()) {
                // Crea una fila con los valores del artículo
                Object[] fila = {
                        rs.getString("ART_COD"),
                        rs.getString("ART_NOM"),
                        rs.getString("ART_UNI"),
                        rs.getInt("ART_PRE"),
                        rs.getInt("ART_STK"),
                        rs.getString("ART_MARCA")
                };
                // Agrega la fila al modelo de la tabla
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void grabarArticulo() {
        try {
            // Convierte texto a números para precio y cantidad
            int precio = Integer.parseInt(txtPrecio.getText().trim());
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            // Inserta artículo usando DataArticulo
            data.insertarArticulo(
                    txtCodigo.getText().trim(),
                    txtNombre.getText().trim(),
                    txtUnidad.getText().trim(),
                    precio,
                    cantidad,
                    txtMarca.getText().trim());
            cargarDatos(); // Refresca la tabla
            limpiarCampos(); // Limpia los campos
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Precio y Cantidad deben ser números válidos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al grabar: " + e.getMessage());
        }
    }

    private void modificarArticulo() {
        try {
            String codigo = txtCodigo.getText().trim();
            if (codigo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debes ingresar un código de artículo.");
                return;
            }

            // Busca el artículo en la BD
            ResultSet rs = data.buscarArticulo(codigo);
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró un artículo con ese código.");
                return;
            }

            // Si un campo está vacío, conserva el valor actual de la BD
            String nombre = txtNombre.getText().trim().isEmpty()
                    ? rs.getString("ART_NOM")
                    : txtNombre.getText().trim();

            String unidad = txtUnidad.getText().trim().isEmpty()
                    ? rs.getString("ART_UNI")
                    : txtUnidad.getText().trim();

            int precio = txtPrecio.getText().trim().isEmpty()
                    ? rs.getInt("ART_PRE")
                    : Integer.parseInt(txtPrecio.getText().trim());

            int cantidad = txtCantidad.getText().trim().isEmpty()
                    ? rs.getInt("ART_STK")
                    : Integer.parseInt(txtCantidad.getText().trim());

            String marca = txtMarca.getText().trim().isEmpty()
                    ? rs.getString("ART_MARCA")
                    : txtMarca.getText().trim();

            // Actualiza el artículo
            data.modificarArticulo(codigo, nombre, unidad, precio, cantidad, marca);

            cargarDatos();
            JOptionPane.showMessageDialog(this, "Artículo actualizado correctamente.");

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Precio y Cantidad deben ser números válidos.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarArticulo() {
        try {
            data.eliminarArticulo(txtCodigo.getText()); // Elimina artículo según el código ingresado
            cargarDatos(); // Refresca la tabla
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
        }
    }
}


