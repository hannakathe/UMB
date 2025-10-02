package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import controller.*;
import model.*;
import service.*;

public class CineFrame extends JFrame {
    private Connection con;
    private ClienteController clienteCtrl;
    private PeliculaController peliculaCtrl;
    // private SalaControllerHelper salaHelper; // helper for salas (we'll use SalaDAO directly)
    private FuncionController funcionCtrl;
    private AsientoDAO asientoDAO;
    private EntradaController entradaCtrl;
    private FacturaController facturaCtrl;

    // UI components
    private JTabbedPane tabs;

    // Clientes
    private JTextField txtDoc, txtNombre, txtTel;
    private JTable tblClientes;
    private DefaultTableModel modelClientes;

    // Peliculas
    private JTextField txtTitulo, txtGenero;
    private JTable tblPeliculas;
    private DefaultTableModel modelPeliculas;

    // Salas
    private JTextField txtTipoSala, txtCapacidad;
    private JTable tblSalas;
    private DefaultTableModel modelSalas;

    // Funciones
    private JTextField txtPeliculaIdF, txtSalaIdF, txtFechaHoraF;
    private JTable tblFunciones;
    private DefaultTableModel modelFunciones;

    // Asientos
    private JTextField txtSalaIdA, txtNumeroSillaA;
    private JTable tblAsientos;
    private DefaultTableModel modelAsientos;

    // Entradas
    private JTextField txtClienteEntrada, txtFuncionEntrada, txtAsientoEntrada, txtValorEntrada;
    private JTable tblEntradas;
    private DefaultTableModel modelEntradas;

    // Facturas
    private JTextField txtClienteFactura, txtValorFactura, txtDatosEmpresa;
    private JTable tblFacturas;
    private DefaultTableModel modelFacturas;

    public CineFrame(Connection con) {
        this.con = con;
        this.clienteCtrl = new ClienteController(con);
        this.peliculaCtrl = new PeliculaController(con);
        this.funcionCtrl = new FuncionController(con);
        this.asientoDAO = new AsientoDAO(con);
        this.entradaCtrl = new EntradaController(con);
        this.facturaCtrl = new FacturaController(con);

        setTitle("Sistema de Cine - Interfaz Única");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        tabs = new JTabbedPane();

        // 1. Clientes panel
        JPanel pClientes = new JPanel(new BorderLayout());
        JPanel pCliForm = new JPanel(new GridLayout(4,2,5,5));
        txtDoc = new JTextField();
        txtNombre = new JTextField();
        txtTel = new JTextField();
        JButton btnAddCliente = new JButton("Agregar Cliente");
        btnAddCliente.addActionListener(_ -> agregarCliente());
        pCliForm.add(new JLabel("Documento:")); pCliForm.add(txtDoc);
        pCliForm.add(new JLabel("Nombre:")); pCliForm.add(txtNombre);
        pCliForm.add(new JLabel("Teléfono:")); pCliForm.add(txtTel);
        pCliForm.add(btnAddCliente);
        pClientes.add(pCliForm, BorderLayout.NORTH);

        modelClientes = new DefaultTableModel(new Object[]{"Documento","Nombre","Telefono"},0);
        tblClientes = new JTable(modelClientes);
        pClientes.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        JButton btnListClientes = new JButton("Listar Clientes");
        btnListClientes.addActionListener(_ -> listarClientes());
        pClientes.add(btnListClientes, BorderLayout.SOUTH);

        // 2. Peliculas
        JPanel pPeliculas = new JPanel(new BorderLayout());
        JPanel pPelForm = new JPanel(new GridLayout(3,2,5,5));
        txtTitulo = new JTextField();
        txtGenero = new JTextField();
        JButton btnAddPelicula = new JButton("Agregar Pelicula");
        btnAddPelicula.addActionListener(_ -> agregarPelicula());
        pPelForm.add(new JLabel("Título:")); pPelForm.add(txtTitulo);
        pPelForm.add(new JLabel("Género:")); pPelForm.add(txtGenero);
        pPelForm.add(btnAddPelicula);
        pPeliculas.add(pPelForm, BorderLayout.NORTH);

        modelPeliculas = new DefaultTableModel(new Object[]{"ID","Título","Género"},0);
        tblPeliculas = new JTable(modelPeliculas);
        pPeliculas.add(new JScrollPane(tblPeliculas), BorderLayout.CENTER);
        JButton btnListPeliculas = new JButton("Listar Películas");
        btnListPeliculas.addActionListener(_ -> listarPeliculas());
        pPeliculas.add(btnListPeliculas, BorderLayout.SOUTH);

        // 3. Salas
        JPanel pSalas = new JPanel(new BorderLayout());
        JPanel pSalaForm = new JPanel(new GridLayout(3,2,5,5));
        txtTipoSala = new JTextField();
        txtCapacidad = new JTextField();
        JButton btnAddSala = new JButton("Agregar Sala");
        btnAddSala.addActionListener(_ -> agregarSala());
        pSalaForm.add(new JLabel("Tipo Sala:")); pSalaForm.add(txtTipoSala);
        pSalaForm.add(new JLabel("Capacidad:")); pSalaForm.add(txtCapacidad);
        pSalaForm.add(btnAddSala);
        pSalas.add(pSalaForm, BorderLayout.NORTH);

        modelSalas = new DefaultTableModel(new Object[]{"ID","Tipo Sala","Capacidad"},0);
        tblSalas = new JTable(modelSalas);
        pSalas.add(new JScrollPane(tblSalas), BorderLayout.CENTER);
        JButton btnListSalas = new JButton("Listar Salas");
        btnListSalas.addActionListener(_ -> listarSalas());
        pSalas.add(btnListSalas, BorderLayout.SOUTH);

        // 4. Funciones
        JPanel pFunciones = new JPanel(new BorderLayout());
        JPanel pFunForm = new JPanel(new GridLayout(4,2,5,5));
        txtPeliculaIdF = new JTextField();
        txtSalaIdF = new JTextField();
        txtFechaHoraF = new JTextField(); // formato: yyyy-MM-dd HH:mm
        JButton btnAddFuncion = new JButton("Agregar Función");
        btnAddFuncion.addActionListener(_ -> agregarFuncion());
        pFunForm.add(new JLabel("Pelicula ID:")); pFunForm.add(txtPeliculaIdF);
        pFunForm.add(new JLabel("Sala ID:")); pFunForm.add(txtSalaIdF);
        pFunForm.add(new JLabel("FechaHora (yyyy-MM-dd HH:mm):")); pFunForm.add(txtFechaHoraF);
        pFunForm.add(btnAddFuncion);
        pFunciones.add(pFunForm, BorderLayout.NORTH);

        modelFunciones = new DefaultTableModel(new Object[]{"ID","PeliculaID","SalaID","FechaHora"},0);
        tblFunciones = new JTable(modelFunciones);
        pFunciones.add(new JScrollPane(tblFunciones), BorderLayout.CENTER);
        JButton btnListFunciones = new JButton("Listar Funciones");
        btnListFunciones.addActionListener(_ -> listarFunciones());
        pFunciones.add(btnListFunciones, BorderLayout.SOUTH);

        // 5. Asientos
        JPanel pAsientos = new JPanel(new BorderLayout());
        JPanel pAsForm = new JPanel(new GridLayout(3,2,5,5));
        txtSalaIdA = new JTextField();
        txtNumeroSillaA = new JTextField();
        JButton btnAddAsiento = new JButton("Agregar Asiento");
        btnAddAsiento.addActionListener(_ -> agregarAsiento());
        pAsForm.add(new JLabel("Sala ID:")); pAsForm.add(txtSalaIdA);
        pAsForm.add(new JLabel("No. Silla:")); pAsForm.add(txtNumeroSillaA);
        pAsForm.add(btnAddAsiento);
        pAsientos.add(pAsForm, BorderLayout.NORTH);

        modelAsientos = new DefaultTableModel(new Object[]{"ID","SalaID","NoSilla","Disponible"},0);
        tblAsientos = new JTable(modelAsientos);
        pAsientos.add(new JScrollPane(tblAsientos), BorderLayout.CENTER);
        JButton btnListAsientos = new JButton("Listar Asientos");
        btnListAsientos.addActionListener(_ -> listarAsientos());
        pAsientos.add(btnListAsientos, BorderLayout.SOUTH);

        // 6. Entradas
        JPanel pEntradas = new JPanel(new BorderLayout());
        JPanel pEnForm = new JPanel(new GridLayout(5,2,5,5));
        txtClienteEntrada = new JTextField();
        txtFuncionEntrada = new JTextField();
        txtAsientoEntrada = new JTextField();
        txtValorEntrada = new JTextField();
        JButton btnAddEntrada = new JButton("Vender Entrada");
        btnAddEntrada.addActionListener(_ -> venderEntrada());
        pEnForm.add(new JLabel("Documento Cliente:")); pEnForm.add(txtClienteEntrada);
        pEnForm.add(new JLabel("Funcion ID:")); pEnForm.add(txtFuncionEntrada);
        pEnForm.add(new JLabel("Asiento ID:")); pEnForm.add(txtAsientoEntrada);
        pEnForm.add(new JLabel("Valor:")); pEnForm.add(txtValorEntrada);
        pEnForm.add(btnAddEntrada);
        pEntradas.add(pEnForm, BorderLayout.NORTH);

        modelEntradas = new DefaultTableModel(new Object[]{"ID","ClienteDoc","FuncionID","AsientoID","Valor"},0);
        tblEntradas = new JTable(modelEntradas);
        pEntradas.add(new JScrollPane(tblEntradas), BorderLayout.CENTER);
        JButton btnListEntradas = new JButton("Listar Entradas");
        btnListEntradas.addActionListener(_ -> listarEntradas());
        pEntradas.add(btnListEntradas, BorderLayout.SOUTH);

        // 7. Facturas
        JPanel pFacturas = new JPanel(new BorderLayout());
        JPanel pFacForm = new JPanel(new GridLayout(4,2,5,5));
        txtClienteFactura = new JTextField();
        txtValorFactura = new JTextField();
        txtDatosEmpresa = new JTextField();
        JButton btnAddFactura = new JButton("Generar Factura");
        btnAddFactura.addActionListener(_ -> generarFactura());
        pFacForm.add(new JLabel("Documento Cliente:")); pFacForm.add(txtClienteFactura);
        pFacForm.add(new JLabel("Valor Total:")); pFacForm.add(txtValorFactura);
        pFacForm.add(new JLabel("Datos Empresa:")); pFacForm.add(txtDatosEmpresa);
        pFacForm.add(btnAddFactura);
        pFacturas.add(pFacForm, BorderLayout.NORTH);

        modelFacturas = new DefaultTableModel(new Object[]{"ID","ClienteDoc","ValorTotal","DatosEmpresa"},0);
        tblFacturas = new JTable(modelFacturas);
        pFacturas.add(new JScrollPane(tblFacturas), BorderLayout.CENTER);
        JButton btnListFacturas = new JButton("Listar Facturas");
        btnListFacturas.addActionListener(_ -> listarFacturas());
        pFacturas.add(btnListFacturas, BorderLayout.SOUTH);

        // Add tabs
        tabs.addTab("Clientes", pClientes);
        tabs.addTab("Películas", pPeliculas);
        tabs.addTab("Salas", pSalas);
        tabs.addTab("Funciones", pFunciones);
        tabs.addTab("Asientos", pAsientos);
        tabs.addTab("Entradas", pEntradas);
        tabs.addTab("Facturas", pFacturas);

        add(tabs, BorderLayout.CENTER);
    }

    // ---------- acciones ----------
    private void agregarCliente() {
        try {
            int doc = Integer.parseInt(txtDoc.getText());
            String nombre = txtNombre.getText();
            String tel = txtTel.getText();
            clienteCtrl.insertar(doc, nombre, tel);
            JOptionPane.showMessageDialog(this, "Cliente agregado.");
            listarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void listarClientes() {
        try {
            List<Cliente> list = clienteCtrl.listar();
            modelClientes.setRowCount(0);
            for (Cliente c : list) {
                modelClientes.addRow(new Object[]{c.getDocumento(), c.getNombre(), c.getTelefono()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando clientes: " + ex.getMessage());
        }
    }

    private void agregarPelicula() {
        try {
            String titulo = txtTitulo.getText();
            String genero = txtGenero.getText();
            peliculaCtrl.insertar(titulo, genero);
            JOptionPane.showMessageDialog(this, "Película agregada.");
            listarPeliculas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void listarPeliculas() {
        try {
            List<Pelicula> list = peliculaCtrl.listar();
            modelPeliculas.setRowCount(0);
            for (Pelicula p : list) {
                modelPeliculas.addRow(new Object[]{p.getId(), p.getTitulo(), p.getGenero()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando películas: " + ex.getMessage());
        }
    }

    private void agregarSala() {
        try {
            String tipo = txtTipoSala.getText();
            int cap = Integer.parseInt(txtCapacidad.getText());
            // Usaremos SalaDAO directamente
            service.SalaDAO dao = new service.SalaDAO(con);
            dao.insertar(new Sala(0, tipo, cap));
            JOptionPane.showMessageDialog(this, "Sala agregada.");
            listarSalas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando sala: " + ex.getMessage());
        }
    }

    private void listarSalas() {
        try {
            service.SalaDAO dao = new service.SalaDAO(con);
            java.util.List<Sala> list = dao.listar();
            modelSalas.setRowCount(0);
            for (Sala s : list) modelSalas.addRow(new Object[]{s.getId(), s.getTipoSala(), s.getCapacidad()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando salas: " + ex.getMessage());
        }
    }

    private void agregarFuncion() {
        try {
            int peliId = Integer.parseInt(txtPeliculaIdF.getText());
            int salaId = Integer.parseInt(txtSalaIdF.getText());
            String fh = txtFechaHoraF.getText();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaHora = LocalDateTime.parse(fh, fmt);
            funcionCtrl.insertar(peliId, salaId, fechaHora);
            JOptionPane.showMessageDialog(this, "Función agregada.");
            listarFunciones();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando función: " + ex.getMessage());
        }
    }

    private void listarFunciones() {
        try {
            List<Funcion> list = funcionCtrl.listar();
            modelFunciones.setRowCount(0);
            for (Funcion f : list) modelFunciones.addRow(new Object[]{f.getId(), f.getPeliculaId(), f.getSalaId(), f.getFechaHora()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando funciones: " + ex.getMessage());
        }
    }

    private void agregarAsiento() {
        try {
            int salaId = Integer.parseInt(txtSalaIdA.getText());
            String num = txtNumeroSillaA.getText();
            asientoDAO.insertar(new Asiento(0, salaId, num, true));
            JOptionPane.showMessageDialog(this, "Asiento agregado.");
            listarAsientos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando asiento: " + ex.getMessage());
        }
    }

    private void listarAsientos() {
        try {
            List<Asiento> list = asientoDAO.listar();
            modelAsientos.setRowCount(0);
            for (Asiento a : list) modelAsientos.addRow(new Object[]{a.getId(), a.getSalaId(), a.getNumeroSilla(), a.isDisponible()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando asientos: " + ex.getMessage());
        }
    }

    private void venderEntrada() {
        try {
            int doc = Integer.parseInt(txtClienteEntrada.getText());
            int funcionId = Integer.parseInt(txtFuncionEntrada.getText());
            int asientoId = Integer.parseInt(txtAsientoEntrada.getText());
            double valor = Double.parseDouble(txtValorEntrada.getText());
            int id = entradaCtrl.crearEntrada(doc, funcionId, asientoId, valor);
            JOptionPane.showMessageDialog(this, "Entrada vendida ID: " + id);
            listarEntradas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error vendiendo entrada: " + ex.getMessage());
        }
    }

    private void listarEntradas() {
        try {
            java.util.List<Entrada> list = entradaCtrl.listar();
            modelEntradas.setRowCount(0);
            for (Entrada e : list) modelEntradas.addRow(new Object[]{e.getId(), e.getClienteDocumento(), e.getFuncionId(), e.getAsientoId(), e.getValor()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando entradas: " + ex.getMessage());
        }
    }

    private void generarFactura() {
        try {
            int doc = Integer.parseInt(txtClienteFactura.getText());
            double valorTotal = Double.parseDouble(txtValorFactura.getText());
            String datos = txtDatosEmpresa.getText();
            int facturaId = facturaCtrl.crearFactura(doc, valorTotal, datos);
            JOptionPane.showMessageDialog(this, "Factura creada ID: " + facturaId);
            listarFacturas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creando factura: " + ex.getMessage());
        }
    }

    private void listarFacturas() {
        try {
            java.util.List<model.Factura> list = facturaCtrl.listar();
            modelFacturas.setRowCount(0);
            for (model.Factura f : list) modelFacturas.addRow(new Object[]{f.getId(), f.getClienteDocumento(), f.getValorTotal(), f.getDatosEmpresa()});
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando facturas: " + ex.getMessage());
        }
    }
}
