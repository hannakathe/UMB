package ui;

import modelservice.ModelService;
import modelservice.ModelService.Cliente;
import modelservice.ModelService.Pelicula;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UI extends JFrame {
    private JTabbedPane tabs;

    // Clientes components
    private JTable tableClientes;
    private DefaultTableModel modelClientes;

    // Peliculas components
    private JTable tablePeliculas;
    private DefaultTableModel modelPeliculas;

    public UI() {
        setTitle("Sistema Cine - CRUD");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();

        tabs.addTab("Clientes", clientesPanel());
        tabs.addTab("Películas", peliculasPanel());
        // Puedes añadir pestañas para Salas, Funciones, Asientos, Entradas, Facturas, Detalle factura

        add(tabs);
        refreshClientesTable();
        refreshPeliculasTable();
    }

    // -------------------
    // Panel Clientes
    // -------------------
    private JPanel clientesPanel() {
        JPanel p = new JPanel(new BorderLayout());
        modelClientes = new DefaultTableModel(new String[]{"ID","Nombre","Documento","Teléfono"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableClientes = new JTable(modelClientes);
        JScrollPane sp = new JScrollPane(tableClientes);
        p.add(sp, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");

        botones.add(btnAgregar); botones.add(btnEditar); botones.add(btnEliminar); botones.add(btnRefrescar);
        p.add(botones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(_ -> mostrarDialogoCliente(null));
        btnEditar.addActionListener(_ -> {
            int sel = tableClientes.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this,"Selecciona un cliente"); return; }
            int id = (Integer) tableClientes.getValueAt(sel,0);
            Cliente c = ModelService.getClienteById(id);
            mostrarDialogoCliente(c);
        });
        btnEliminar.addActionListener(_ -> {
            int sel = tableClientes.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this,"Selecciona un cliente"); return; }
            int id = (Integer) tableClientes.getValueAt(sel,0);
            int r = JOptionPane.showConfirmDialog(this,"Eliminar cliente ID "+id+"?","Confirmar", JOptionPane.YES_NO_OPTION);
            if (r==JOptionPane.YES_OPTION) {
                if (ModelService.deleteCliente(id)) {
                    JOptionPane.showMessageDialog(this,"Eliminado");
                    refreshClientesTable();
                } else JOptionPane.showMessageDialog(this,"Error al eliminar");
            }
        });
        btnRefrescar.addActionListener(_ -> refreshClientesTable());

        return p;
    }

    private void mostrarDialogoCliente(Cliente c) {
        JTextField txtNombre = new JTextField();
        JTextField txtDocumento = new JTextField();
        JTextField txtTelefono = new JTextField();
        if (c != null) {
            txtNombre.setText(c.nombre);
            txtDocumento.setText(c.documento);
            txtTelefono.setText(c.telefono);
        }
        Object[] campos = {
                "Nombre:", txtNombre,
                "Documento:", txtDocumento,
                "Teléfono:", txtTelefono
        };
        int r = JOptionPane.showConfirmDialog(this, campos, c==null?"Agregar Cliente":"Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            if (txtNombre.getText().trim().isEmpty() || txtDocumento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,"Nombre y Documento son obligatorios");
                return;
            }
            if (c == null) {
                Cliente nuevo = new Cliente();
                nuevo.nombre = txtNombre.getText().trim();
                nuevo.documento = txtDocumento.getText().trim();
                nuevo.telefono = txtTelefono.getText().trim();
                boolean ok = ModelService.insertCliente(nuevo);
                JOptionPane.showMessageDialog(this, ok ? "Cliente agregado" : "Error al agregar (¿documento duplicado?)");
            } else {
                c.nombre = txtNombre.getText().trim();
                c.documento = txtDocumento.getText().trim();
                c.telefono = txtTelefono.getText().trim();
                boolean ok = ModelService.updateCliente(c);
                JOptionPane.showMessageDialog(this, ok ? "Cliente actualizado" : "Error al actualizar");
            }
            refreshClientesTable();
        }
    }

    private void refreshClientesTable() {
        List<Cliente> list = ModelService.getAllClientes();
        modelClientes.setRowCount(0);
        for (Cliente c : list) {
            modelClientes.addRow(new Object[]{c.id, c.nombre, c.documento, c.telefono});
        }
    }

    // -------------------
    // Panel Peliculas
    // -------------------
    private JPanel peliculasPanel() {
        JPanel p = new JPanel(new BorderLayout());
        modelPeliculas = new DefaultTableModel(new String[]{"ID","Título","Género","Valor"}, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablePeliculas = new JTable(modelPeliculas);
        JScrollPane sp = new JScrollPane(tablePeliculas);
        p.add(sp, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnRefrescar = new JButton("Refrescar");
        botones.add(btnAgregar); botones.add(btnEditar); botones.add(btnEliminar); botones.add(btnRefrescar);
        p.add(botones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(_ -> mostrarDialogoPelicula(null));
        btnEditar.addActionListener(_ -> {
            int sel = tablePeliculas.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this,"Selecciona una película"); return; }
            int id = (Integer) tablePeliculas.getValueAt(sel,0);
            // obtener pelicula por id (no implementado getPeliculaById en ModelService, lo hacemos por listado)
            for (Pelicula ptemp : ModelService.getAllPeliculas()) {
                if (ptemp.id == id) { mostrarDialogoPelicula(ptemp); break; }
            }
        });
        btnEliminar.addActionListener(_ -> {
            int sel = tablePeliculas.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this,"Selecciona una película"); return; }
            int id = (Integer) tablePeliculas.getValueAt(sel,0);
            int r = JOptionPane.showConfirmDialog(this,"Eliminar película ID "+id+"?","Confirmar", JOptionPane.YES_NO_OPTION);
            if (r==JOptionPane.YES_OPTION) {
                if (ModelService.deletePelicula(id)) {
                    JOptionPane.showMessageDialog(this,"Eliminado");
                    refreshPeliculasTable();
                } else JOptionPane.showMessageDialog(this,"Error al eliminar");
            }
        });
        btnRefrescar.addActionListener(_ -> refreshPeliculasTable());

        return p;
    }

    private void mostrarDialogoPelicula(Pelicula p) {
        JTextField txtTitulo = new JTextField();
        JTextField txtGenero = new JTextField();
        JTextField txtValor = new JTextField();
        if (p != null) {
            txtTitulo.setText(p.titulo);
            txtGenero.setText(p.genero);
            txtValor.setText(Double.toString(p.valor));
        }
        Object[] campos = {
                "Título:", txtTitulo,
                "Género:", txtGenero,
                "Valor:", txtValor
        };
        int r = JOptionPane.showConfirmDialog(this, campos, p==null?"Agregar Película":"Editar Película", JOptionPane.OK_CANCEL_OPTION);
        if (r == JOptionPane.OK_OPTION) {
            if (txtTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,"Título es obligatorio");
                return;
            }
            double valor = 0;
            try { valor = Double.parseDouble(txtValor.getText().trim()); } catch (Exception ex) { valor = 0; }
            if (p == null) {
                Pelicula nuevo = new Pelicula();
                nuevo.titulo = txtTitulo.getText().trim();
                nuevo.genero = txtGenero.getText().trim();
                nuevo.valor = valor;
                boolean ok = ModelService.insertPelicula(nuevo);
                JOptionPane.showMessageDialog(this, ok ? "Película agregada" : "Error al agregar");
            } else {
                p.titulo = txtTitulo.getText().trim();
                p.genero = txtGenero.getText().trim();
                p.valor = valor;
                boolean ok = ModelService.updatePelicula(p);
                JOptionPane.showMessageDialog(this, ok ? "Película actualizada" : "Error al actualizar");
            }
            refreshPeliculasTable();
        }
    }

    private void refreshPeliculasTable() {
        List<Pelicula> list = ModelService.getAllPeliculas();
        modelPeliculas.setRowCount(0);
        for (Pelicula p : list) {
            modelPeliculas.addRow(new Object[]{p.id, p.titulo, p.genero, p.valor});
        }
    }
}
