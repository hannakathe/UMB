package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
    // private SalaControllerHelper salaHelper; // helper for salas (we'll use
    // SalaDAO directly)
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

    // Consulta Relacional
    private JTable tblConsultaRelacional;
    private DefaultTableModel modelConsultaRelacional;

    public CineFrame(Connection con) {
        this.con = con;
        this.clienteCtrl = new ClienteController(con);
        this.peliculaCtrl = new PeliculaController(con);
        this.funcionCtrl = new FuncionController(con);
        this.asientoDAO = new AsientoDAO(con);
        this.entradaCtrl = new EntradaController(con);
        this.facturaCtrl = new FacturaController(con);

        setTitle("Cine App");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        tabs = new JTabbedPane();

        // limpiar campos

        // Panel general para el botón Limpiar
        JPanel pBotonGeneral = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });
        pBotonGeneral.add(btnLimpiar);

        // Lo agregamos al JFrame, debajo de los tabs
        add(pBotonGeneral, BorderLayout.SOUTH);

        // 1. Clientes panel
        JPanel pClientes = new JPanel(new BorderLayout());
        JPanel pCliForm = new JPanel(new GridLayout(4, 2, 5, 5));
        txtDoc = new JTextField();
        txtNombre = new JTextField();
        txtTel = new JTextField();
        JButton btnAddCliente = new JButton("Agregar Cliente");
        btnAddCliente.addActionListener(_ -> agregarCliente());
        pCliForm.add(new JLabel("Documento:"));
        pCliForm.add(txtDoc);
        pCliForm.add(new JLabel("Nombre:"));
        pCliForm.add(txtNombre);
        pCliForm.add(new JLabel("Teléfono:"));
        pCliForm.add(txtTel);
        pCliForm.add(btnAddCliente);
        pClientes.add(pCliForm, BorderLayout.NORTH);

        modelClientes = new DefaultTableModel(new Object[] { "Documento", "Nombre", "Telefono" }, 0);
        tblClientes = new JTable(modelClientes);
        pClientes.add(new JScrollPane(tblClientes), BorderLayout.CENTER);

        JButton btnListClientes = new JButton("Listar Clientes");
        btnListClientes.addActionListener(_ -> listarClientes());
        pClientes.add(btnListClientes, BorderLayout.SOUTH);

        // CONSULTA GENERAL TODO
        // (Se mueve la inicialización de pConsulta más abajo, después de su declaración)

        // 2. Peliculas
        JPanel pPeliculas = new JPanel(new BorderLayout());
        JPanel pPelForm = new JPanel(new GridLayout(3, 2, 5, 5));
        txtTitulo = new JTextField();
        txtGenero = new JTextField();
        JButton btnAddPelicula = new JButton("Agregar Pelicula");
        btnAddPelicula.addActionListener(_ -> agregarPelicula());
        pPelForm.add(new JLabel("Título:"));
        pPelForm.add(txtTitulo);
        pPelForm.add(new JLabel("Género:"));
        pPelForm.add(txtGenero);
        pPelForm.add(btnAddPelicula);
        pPeliculas.add(pPelForm, BorderLayout.NORTH);

        modelPeliculas = new DefaultTableModel(new Object[] { "ID", "Título", "Género" }, 0);
        tblPeliculas = new JTable(modelPeliculas);
        pPeliculas.add(new JScrollPane(tblPeliculas), BorderLayout.CENTER);
        JButton btnListPeliculas = new JButton("Listar Películas");
        btnListPeliculas.addActionListener(_ -> listarPeliculas());
        pPeliculas.add(btnListPeliculas, BorderLayout.SOUTH);

        // 3. Salas
        JPanel pSalas = new JPanel(new BorderLayout());
        JPanel pSalaForm = new JPanel(new GridLayout(3, 2, 5, 5));
        txtTipoSala = new JTextField();
        txtCapacidad = new JTextField();
        JButton btnAddSala = new JButton("Agregar Sala");
        btnAddSala.addActionListener(_ -> agregarSala());
        pSalaForm.add(new JLabel("Tipo Sala:"));
        pSalaForm.add(txtTipoSala);
        pSalaForm.add(new JLabel("Capacidad:"));
        pSalaForm.add(txtCapacidad);
        pSalaForm.add(btnAddSala);
        pSalas.add(pSalaForm, BorderLayout.NORTH);

        modelSalas = new DefaultTableModel(new Object[] { "ID", "Tipo Sala", "Capacidad" }, 0);
        tblSalas = new JTable(modelSalas);
        pSalas.add(new JScrollPane(tblSalas), BorderLayout.CENTER);
        JButton btnListSalas = new JButton("Listar Salas");
        btnListSalas.addActionListener(_ -> listarSalas());
        pSalas.add(btnListSalas, BorderLayout.SOUTH);

        // 4. Funciones
        JPanel pFunciones = new JPanel(new BorderLayout());
        JPanel pFunForm = new JPanel(new GridLayout(4, 2, 5, 5));
        txtPeliculaIdF = new JTextField();
        txtSalaIdF = new JTextField();
        txtFechaHoraF = new JTextField(); // formato: yyyy-MM-dd HH:mm
        JButton btnAddFuncion = new JButton("Agregar Función");
        btnAddFuncion.addActionListener(_ -> agregarFuncion());
        pFunForm.add(new JLabel("Pelicula ID:"));
        pFunForm.add(txtPeliculaIdF);
        pFunForm.add(new JLabel("Sala ID:"));
        pFunForm.add(txtSalaIdF);
        pFunForm.add(new JLabel("FechaHora (yyyy-MM-dd HH:mm):"));
        pFunForm.add(txtFechaHoraF);
        pFunForm.add(btnAddFuncion);
        pFunciones.add(pFunForm, BorderLayout.NORTH);

        modelFunciones = new DefaultTableModel(new Object[] { "ID", "PeliculaID", "SalaID", "FechaHora" }, 0);
        tblFunciones = new JTable(modelFunciones);
        pFunciones.add(new JScrollPane(tblFunciones), BorderLayout.CENTER);
        JButton btnListFunciones = new JButton("Listar Funciones");
        btnListFunciones.addActionListener(_ -> listarFunciones());
        pFunciones.add(btnListFunciones, BorderLayout.SOUTH);

        // 5. Asientos
        JPanel pAsientos = new JPanel(new BorderLayout());
        JPanel pAsForm = new JPanel(new GridLayout(3, 2, 5, 5));
        txtSalaIdA = new JTextField();
        txtNumeroSillaA = new JTextField();
        JButton btnAddAsiento = new JButton("Agregar Asiento");
        btnAddAsiento.addActionListener(_ -> agregarAsiento());
        pAsForm.add(new JLabel("Sala ID:"));
        pAsForm.add(txtSalaIdA);
        pAsForm.add(new JLabel("No. Silla:"));
        pAsForm.add(txtNumeroSillaA);
        pAsForm.add(btnAddAsiento);
        pAsientos.add(pAsForm, BorderLayout.NORTH);

        modelAsientos = new DefaultTableModel(new Object[] { "ID", "SalaID", "NoSilla", "Disponible" }, 0);
        tblAsientos = new JTable(modelAsientos);
        pAsientos.add(new JScrollPane(tblAsientos), BorderLayout.CENTER);
        JButton btnListAsientos = new JButton("Listar Asientos");
        btnListAsientos.addActionListener(_ -> listarAsientos());
        pAsientos.add(btnListAsientos, BorderLayout.SOUTH);

        // 6. Entradas
        JPanel pEntradas = new JPanel(new BorderLayout());
        JPanel pEnForm = new JPanel(new GridLayout(5, 2, 5, 5));
        txtClienteEntrada = new JTextField();
        txtFuncionEntrada = new JTextField();
        txtAsientoEntrada = new JTextField();
        txtValorEntrada = new JTextField();
        JButton btnAddEntrada = new JButton("Vender Entrada");
        btnAddEntrada.addActionListener(_ -> venderEntrada());
        pEnForm.add(new JLabel("Documento Cliente:"));
        pEnForm.add(txtClienteEntrada);
        pEnForm.add(new JLabel("Funcion ID:"));
        pEnForm.add(txtFuncionEntrada);
        pEnForm.add(new JLabel("Asiento ID:"));
        pEnForm.add(txtAsientoEntrada);
        pEnForm.add(new JLabel("Valor:"));
        pEnForm.add(txtValorEntrada);
        pEnForm.add(btnAddEntrada);
        pEntradas.add(pEnForm, BorderLayout.NORTH);

        modelEntradas = new DefaultTableModel(new Object[] { "ID", "ClienteDoc", "FuncionID", "AsientoID", "Valor" },
                0);
        tblEntradas = new JTable(modelEntradas);
        pEntradas.add(new JScrollPane(tblEntradas), BorderLayout.CENTER);
        JButton btnListEntradas = new JButton("Listar Entradas");
        btnListEntradas.addActionListener(_ -> listarEntradas());
        pEntradas.add(btnListEntradas, BorderLayout.SOUTH);

        // 7. Facturas
        JPanel pFacturas = new JPanel(new BorderLayout());
        JPanel pFacForm = new JPanel(new GridLayout(4, 2, 5, 5));
        txtClienteFactura = new JTextField();
        txtValorFactura = new JTextField();
        txtDatosEmpresa = new JTextField();
        JButton btnAddFactura = new JButton("Generar Factura");
        btnAddFactura.addActionListener(_ -> generarFactura());
        pFacForm.add(new JLabel("Documento Cliente:"));
        pFacForm.add(txtClienteFactura);
        pFacForm.add(new JLabel("Valor Total:"));
        pFacForm.add(txtValorFactura);
        pFacForm.add(new JLabel("Datos Empresa:"));
        pFacForm.add(txtDatosEmpresa);
        pFacForm.add(btnAddFactura);
        pFacturas.add(pFacForm, BorderLayout.NORTH);

        modelFacturas = new DefaultTableModel(new Object[] { "ID", "ClienteDoc", "ValorTotal", "DatosEmpresa" }, 0);
        tblFacturas = new JTable(modelFacturas);
        pFacturas.add(new JScrollPane(tblFacturas), BorderLayout.CENTER);
        JButton btnListFacturas = new JButton("Listar Facturas");
        btnListFacturas.addActionListener(_ -> listarFacturas());
        pFacturas.add(btnListFacturas, BorderLayout.SOUTH);
        // consulta relacional tabs
        
        // consulta relacional
        // 8. Consulta Relacional
        // En el método initUI(), modifica la sección de Consulta Relacional:

// 8. Consulta Relacional
JPanel pConsulta = new JPanel(new BorderLayout());

// Panel superior con formulario para CRUD
JPanel pConsultaForm = new JPanel(new GridLayout(4, 4, 5, 5));
JTextField txtDocConsulta = new JTextField();
JTextField txtNombreConsulta = new JTextField();
JTextField txtPeliculaConsulta = new JTextField();
JTextField txtSalaConsulta = new JTextField();
JTextField txtAsientoConsulta = new JTextField();
JTextField txtValorConsulta = new JTextField();
//JTextField txtFechaFacturaConsulta = new JTextField();
JLabel lblFechaAuto = new JLabel("Fecha auto-generada");
JButton btnAddConsulta = new JButton("Agregar");
JButton btnUpdateConsulta = new JButton("Actualizar");
JButton btnDeleteConsulta = new JButton("Eliminar");

pConsultaForm.add(new JLabel("Documento:"));
pConsultaForm.add(txtDocConsulta);
pConsultaForm.add(new JLabel("Nombre:"));
pConsultaForm.add(txtNombreConsulta);
pConsultaForm.add(new JLabel("Película:"));
pConsultaForm.add(txtPeliculaConsulta);
pConsultaForm.add(new JLabel("Sala:"));
pConsultaForm.add(txtSalaConsulta);
pConsultaForm.add(new JLabel("Asiento:"));
pConsultaForm.add(txtAsientoConsulta);
pConsultaForm.add(new JLabel("Valor:"));
pConsultaForm.add(txtValorConsulta);
pConsultaForm.add(new JLabel("Fecha Factura:"));
//pConsultaForm.add(txtFechaFacturaConsulta);
pConsultaForm.add(lblFechaAuto);
pConsultaForm.add(btnAddConsulta);
pConsultaForm.add(btnUpdateConsulta);
pConsultaForm.add(btnDeleteConsulta);

// Definir columnas según tu consulta SQL
modelConsultaRelacional = new DefaultTableModel(new Object[] {
        "DocumentoCliente", "NombreCliente", "Pelicula",
        "Sala", "Asiento", "ValorEntrada", "FechaFactura"
}, 0);

tblConsultaRelacional = new JTable(modelConsultaRelacional);
JScrollPane scrollConsulta = new JScrollPane(tblConsultaRelacional);

// Panel de botones
JPanel pConsultaBtns = new JPanel(new FlowLayout());
//JButton btnListConsulta = new JButton("Ejecutar Consulta");
//btnListConsulta.addActionListener(_ -> listarConsultaRelacional());

//pConsultaBtns.add(btnListConsulta);

// Agregar componentes al panel principal
pConsulta.add(pConsultaForm, BorderLayout.NORTH);
pConsulta.add(scrollConsulta, BorderLayout.CENTER);
pConsulta.add(pConsultaBtns, BorderLayout.SOUTH);

// Agregar tab
tabs.addTab("Consulta Relacional", pConsulta);

// Listeners para los botones de CRUD
// Modifica los listeners para no pasar el parámetro de fecha
btnAddConsulta.addActionListener(_ -> {
    agregarDesdeConsulta(txtDocConsulta, txtNombreConsulta, txtPeliculaConsulta, 
                        txtSalaConsulta, txtAsientoConsulta, txtValorConsulta);
    limpiarCamposConsulta(txtDocConsulta, txtNombreConsulta, txtPeliculaConsulta,
                         txtSalaConsulta, txtAsientoConsulta, txtValorConsulta);
});

btnUpdateConsulta.addActionListener(_ -> actualizarDesdeConsulta(
        txtDocConsulta, txtNombreConsulta, txtPeliculaConsulta,
        txtSalaConsulta, txtAsientoConsulta, txtValorConsulta));

btnDeleteConsulta.addActionListener(_ -> eliminarDesdeConsulta());

// Listener para selección en tabla
tblConsultaRelacional.getSelectionModel().addListSelectionListener(e -> {
    if (!e.getValueIsAdjusting() && tblConsultaRelacional.getSelectedRow() != -1) {
        int fila = tblConsultaRelacional.getSelectedRow();
        txtDocConsulta.setText(modelConsultaRelacional.getValueAt(fila, 0).toString());
        txtNombreConsulta.setText(modelConsultaRelacional.getValueAt(fila, 1).toString());
        txtPeliculaConsulta.setText(modelConsultaRelacional.getValueAt(fila, 2).toString());
        txtSalaConsulta.setText(modelConsultaRelacional.getValueAt(fila, 3).toString());
        txtAsientoConsulta.setText(modelConsultaRelacional.getValueAt(fila, 4).toString());
        txtValorConsulta.setText(modelConsultaRelacional.getValueAt(fila, 5).toString());
        // No cargamos la fecha porque ahora es automática
    }
});

        // Add tabs
        tabs.addTab("Clientes", pClientes);
        tabs.addTab("Películas", pPeliculas);
        tabs.addTab("Salas", pSalas);
        tabs.addTab("Funciones", pFunciones);
        tabs.addTab("Asientos", pAsientos);
        tabs.addTab("Entradas", pEntradas);
        tabs.addTab("Facturas", pFacturas);

        add(tabs, BorderLayout.CENTER);

        listarClientes();
        listarPeliculas();
        listarSalas();
        listarFunciones();
        listarAsientos();
        listarEntradas();
        listarFacturas();
        listarConsultaRelacional();

        // ----------- button actions ----------
        // clientes

        JButton btnActualizarCliente = new JButton("Actualizar Cliente");
        btnActualizarCliente.addActionListener(_ -> actualizarCliente());

        JButton btnEliminarCliente = new JButton("Eliminar Cliente");
        btnEliminarCliente.addActionListener(_ -> eliminarCliente());
        // peliculas
        JButton btnActualizarPelicula = new JButton("Actualizar Película");
        btnActualizarPelicula.addActionListener(_ -> actualizarPelicula());

        JButton btnEliminarPelicula = new JButton("Eliminar Película");
        btnEliminarPelicula.addActionListener(_ -> eliminarPelicula());
        // salas
        JButton btnActualizarSala = new JButton("Actualizar Sala");
        btnActualizarSala.addActionListener(_ -> actualizarSala());

        JButton btnEliminarSala = new JButton("Eliminar Sala");
        btnEliminarSala.addActionListener(_ -> eliminarSala());
        // funciones
        JButton btnActualizarFuncion = new JButton("Actualizar Función");
        btnActualizarFuncion.addActionListener(_ -> actualizarFuncion());

        JButton btnEliminarFuncion = new JButton("Eliminar Función");
        btnEliminarFuncion.addActionListener(_ -> eliminarFuncion());

        // asientos
        JButton btnActualizarAsiento = new JButton("Actualizar Asiento");
        btnActualizarAsiento.addActionListener(_ -> actualizarAsiento());

        JButton btnEliminarAsiento = new JButton("Eliminar Asiento");
        btnEliminarAsiento.addActionListener(_ -> eliminarAsiento());
        // entradas
        JButton btnActualizarEntrada = new JButton("Actualizar Entrada");
        btnActualizarEntrada.addActionListener(_ -> actualizarEntrada());

        JButton btnEliminarEntrada = new JButton("Eliminar Entrada");
        btnEliminarEntrada.addActionListener(_ -> eliminarEntrada());
        // facturas
        JButton btnActualizarFactura = new JButton("Actualizar Factura");
        btnActualizarFactura.addActionListener(_ -> actualizarFactura());

        JButton btnEliminarFactura = new JButton("Eliminar Factura");
        btnEliminarFactura.addActionListener(_ -> eliminarFactura());

        // CLIENTES
        JPanel pCliBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pCliBtns.add(btnActualizarCliente);
        pCliBtns.add(btnEliminarCliente);
        pClientes.add(pCliBtns, BorderLayout.SOUTH);

        // PELÍCULAS
        JPanel pPelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pPelBtns.add(btnActualizarPelicula);
        pPelBtns.add(btnEliminarPelicula);
        pPeliculas.add(pPelBtns, BorderLayout.SOUTH);

        // SALAS
        JPanel pSalaBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pSalaBtns.add(btnActualizarSala);
        pSalaBtns.add(btnEliminarSala);
        pSalas.add(pSalaBtns, BorderLayout.SOUTH);

        // FUNCIONES
        JPanel pFunBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pFunBtns.add(btnActualizarFuncion);
        pFunBtns.add(btnEliminarFuncion);
        pFunciones.add(pFunBtns, BorderLayout.SOUTH);

        // ASIENTOS
        JPanel pAsBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pAsBtns.add(btnActualizarAsiento);
        pAsBtns.add(btnEliminarAsiento);
        pAsientos.add(pAsBtns, BorderLayout.SOUTH);

        // ENTRADAS
        JPanel pEnBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pEnBtns.add(btnActualizarEntrada);
        pEnBtns.add(btnEliminarEntrada);
        pEntradas.add(pEnBtns, BorderLayout.SOUTH);

        // FACTURAS
        JPanel pFacBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pFacBtns.add(btnActualizarFactura);
        pFacBtns.add(btnEliminarFactura);
        pFacturas.add(pFacBtns, BorderLayout.SOUTH);

        // -----------Listeners ----------
        // clientes
        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblClientes.getSelectedRow() != -1) {
                int fila = tblClientes.getSelectedRow();
                txtDoc.setText(modelClientes.getValueAt(fila, 0).toString());
                txtNombre.setText(modelClientes.getValueAt(fila, 1).toString());
                txtTel.setText(modelClientes.getValueAt(fila, 2).toString());
            }
        });
        // peliculas
        tblPeliculas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPeliculas.getSelectedRow() != -1) {
                int fila = tblPeliculas.getSelectedRow();
                txtTitulo.setText(modelPeliculas.getValueAt(fila, 1).toString());
                txtGenero.setText(modelPeliculas.getValueAt(fila, 2).toString());
            }
        });
        // salas
        tblSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblSalas.getSelectedRow() != -1) {
                int fila = tblSalas.getSelectedRow();
                txtTipoSala.setText(modelSalas.getValueAt(fila, 1).toString());
                txtCapacidad.setText(modelSalas.getValueAt(fila, 2).toString());
            }
        });
        // funciones
        tblFunciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblFunciones.getSelectedRow() != -1) {
                int fila = tblFunciones.getSelectedRow();
                txtPeliculaIdF.setText(modelFunciones.getValueAt(fila, 1).toString());
                txtSalaIdF.setText(modelFunciones.getValueAt(fila, 2).toString());
                txtFechaHoraF.setText(modelFunciones.getValueAt(fila, 3).toString());
            }
        });
        // asientos
        tblAsientos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblAsientos.getSelectedRow() != -1) {
                int fila = tblAsientos.getSelectedRow();
                txtSalaIdA.setText(modelAsientos.getValueAt(fila, 1).toString());
                txtNumeroSillaA.setText(modelAsientos.getValueAt(fila, 2).toString());
            }
        });
        // entradas
        tblEntradas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEntradas.getSelectedRow() != -1) {
                int fila = tblEntradas.getSelectedRow();
                txtClienteEntrada.setText(modelEntradas.getValueAt(fila, 1).toString());
                txtFuncionEntrada.setText(modelEntradas.getValueAt(fila, 2).toString());
                txtAsientoEntrada.setText(modelEntradas.getValueAt(fila, 3).toString());
                txtValorEntrada.setText(modelEntradas.getValueAt(fila, 4).toString());
            }
        });
        // facturas
        tblFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblFacturas.getSelectedRow() != -1) {
                int fila = tblFacturas.getSelectedRow();
                txtClienteFactura.setText(modelFacturas.getValueAt(fila, 1).toString());
                txtValorFactura.setText(modelFacturas.getValueAt(fila, 2).toString());
                txtDatosEmpresa.setText(modelFacturas.getValueAt(fila, 3).toString());
            }
        });
    }

    // ---------- acciones ----------
    // consulta relacional
    // ---------- CRUD para Consulta Relacional ----------

private void agregarDesdeConsulta(JTextField txtDoc, JTextField txtNombre, JTextField txtPelicula,
                                 JTextField txtSala, JTextField txtAsiento, JTextField txtValor) {
    // Eliminamos el parámetro JTextField txtFechaFactura
    try {
        // Validar campos obligatorios
        if (txtDoc.getText().trim().isEmpty() || txtNombre.getText().trim().isEmpty() ||
            txtPelicula.getText().trim().isEmpty() || txtValor.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Documento, Nombre, Película y Valor son obligatorios.");
            return;
        }

        // 1. Crear/actualizar cliente directamente
        int documento;
        try {
            documento = Integer.parseInt(txtDoc.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Documento debe ser numérico");
            return;
        }
        
        // Intentar crear el cliente directamente
        // Si ya existe, la base de datos lanzará una excepción por clave duplicada
        try {
            clienteCtrl.insertar(documento, txtNombre.getText().trim(), "000-0000000");
        } catch (Exception e) {
            // Si falla por duplicado, actualizar el cliente existente
            clienteCtrl.actualizar(documento, txtNombre.getText().trim(), "000-0000000");
        }

        // 2. Buscar o crear película
        String tituloPelicula = txtPelicula.getText().trim();
        Pelicula peliculaExistente = buscarPeliculaPorTitulo(tituloPelicula);
        int peliculaId;
        
        if (peliculaExistente == null) {
            // Crear nueva película
            peliculaCtrl.insertar(tituloPelicula, "Genero Default");
            // Obtener el ID de la película recién creada
            List<Pelicula> peliculas = peliculaCtrl.listar();
            peliculaId = peliculas.get(peliculas.size() - 1).getId();
            JOptionPane.showMessageDialog(this, "Película creada automáticamente");
        } else {
            peliculaId = peliculaExistente.getId();
        }

        // 3. Buscar o crear sala
        String tipoSala = txtSala.getText().trim();
        Sala salaExistente = buscarSalaPorTipo(tipoSala);
        int salaId;
        
        if (salaExistente == null && !tipoSala.isEmpty()) {
            // Crear nueva sala
            SalaDAO salaDAO = new SalaDAO(con);
            salaDAO.insertar(new Sala(0, tipoSala, 50)); // Capacidad default 50
            List<Sala> salas = salaDAO.listar();
            salaId = salas.get(salas.size() - 1).getId();
            JOptionPane.showMessageDialog(this, "Sala creada automáticamente");
        } else if (salaExistente != null) {
            salaId = salaExistente.getId();
        } else {
            JOptionPane.showMessageDialog(this, "Se requiere una sala válida");
            return;
        }

        // 4. Crear función si no existe (usando fecha/hora actual)
        Funcion funcionExistente = buscarFuncion(peliculaId, salaId);
        int funcionId;
        
        if (funcionExistente == null) {
            // Crear nueva función con fecha/hora actual
            funcionCtrl.insertar(peliculaId, salaId, LocalDateTime.now());
            List<Funcion> funciones = funcionCtrl.listar();
            funcionId = funciones.get(funciones.size() - 1).getId();
            JOptionPane.showMessageDialog(this, "Función creada automáticamente");
        } else {
            funcionId = funcionExistente.getId();
        }

        // 5. Buscar o crear asiento
        String numeroAsiento = txtAsiento.getText().trim();
        Asiento asientoExistente = buscarAsiento(salaId, numeroAsiento);
        int asientoId;
        
        if (asientoExistente == null && !numeroAsiento.isEmpty()) {
            // Crear nuevo asiento
            asientoDAO.insertar(new Asiento(0, salaId, numeroAsiento, true));
            List<Asiento> asientos = asientoDAO.listar();
            asientoId = asientos.get(asientos.size() - 1).getId();
            JOptionPane.showMessageDialog(this, "Asiento creado automáticamente");
        } else if (asientoExistente != null) {
            asientoId = asientoExistente.getId();
        } else {
            JOptionPane.showMessageDialog(this, "Se requiere un asiento válido");
            return;
        }

        // 6. Crear entrada
        double valor = Double.parseDouble(txtValor.getText().trim());
        int entradaId = entradaCtrl.crearEntrada(documento, funcionId, asientoId, valor);

        // 7. Crear factura automáticamente con fecha actual
        String datosEmpresa = "Cine XYZ - " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        int facturaId = facturaCtrl.crearFactura(documento, valor, datosEmpresa);

        JOptionPane.showMessageDialog(this, "Registro completo creado.\nEntrada ID: " + entradaId + "\nFactura ID: " + facturaId);
        
        // Actualizar todas las listas
        actualizarTodasLasListas();
        listarConsultaRelacional();
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error agregando registro: " + ex.getMessage());
        ex.printStackTrace();
    }
}

private void actualizarDesdeConsulta(JTextField txtDoc, JTextField txtNombre, JTextField txtPelicula,
                                    JTextField txtSala, JTextField txtAsiento, JTextField txtValor) {
    // Eliminamos el parámetro de fecha
    try {
        int fila = tblConsultaRelacional.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para actualizar");
            return;
        }

        String documentoOriginal = modelConsultaRelacional.getValueAt(fila, 0).toString();
        int documento = Integer.parseInt(txtDoc.getText().trim());

        // Actualizar cliente
        clienteCtrl.actualizar(documento, txtNombre.getText().trim(), "000-0000000");

        // Buscar la entrada relacionada y actualizarla
        List<Entrada> entradas = entradaCtrl.listar();
        for (Entrada entrada : entradas) {
            if (String.valueOf(entrada.getClienteDocumento()).equals(documentoOriginal)) {
                // Actualizar valor de la entrada
                double nuevoValor = Double.parseDouble(txtValor.getText().trim());
                entradaCtrl.actualizar(entrada.getId(), documento, entrada.getFuncionId(), 
                                     entrada.getAsientoId(), nuevoValor);
                
                // Actualizar factura con nueva fecha
                List<Factura> facturas = facturaCtrl.listar();
                for (Factura factura : facturas) {
                    if (factura.getClienteDocumento() == documento) {
                        String nuevosDatosEmpresa = "Cine XYZ - Actualizado: " + 
                                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                        facturaCtrl.actualizar(factura.getId(), documento, nuevoValor, nuevosDatosEmpresa);
                        break;
                    }
                }
                break;
            }
        }

        JOptionPane.showMessageDialog(this, "Registro actualizado correctamente");
        actualizarTodasLasListas();
        listarConsultaRelacional();
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error actualizando registro: " + ex.getMessage());
    }
}
private void limpiarCamposConsulta(JTextField txtDoc, JTextField txtNombre, JTextField txtPelicula,
                                  JTextField txtSala, JTextField txtAsiento, JTextField txtValor) {
    // Eliminamos el parámetro de fecha
    txtDoc.setText("");
    txtNombre.setText("");
    txtPelicula.setText("");
    txtSala.setText("");
    txtAsiento.setText("");
    txtValor.setText("");
}



private void eliminarDesdeConsulta() {
    try {
        int fila = tblConsultaRelacional.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar");
            return;
        }

        String documento = modelConsultaRelacional.getValueAt(fila, 0).toString();
        int doc = Integer.parseInt(documento);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de eliminar todas las entradas del cliente " + documento + "?",
            "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Eliminar entradas del cliente
            List<Entrada> entradas = entradaCtrl.listar();
            for (Entrada entrada : entradas) {
                if (entrada.getClienteDocumento() == doc) {
                    entradaCtrl.eliminar(entrada.getId());
                }
            }

            // También puedes eliminar el cliente si lo deseas
            // clienteCtrl.eliminar(doc);

            JOptionPane.showMessageDialog(this, "Entradas del cliente eliminadas");
            actualizarTodasLasListas();
            listarConsultaRelacional();
        }
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error eliminando registro: " + ex.getMessage());
    }
}

// Métodos auxiliares para búsqueda
private Pelicula buscarPeliculaPorTitulo(String titulo) {
    try {
        List<Pelicula> peliculas = peliculaCtrl.listar();
        for (Pelicula p : peliculas) {
            if (p.getTitulo().equalsIgnoreCase(titulo)) {
                return p;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

private Sala buscarSalaPorTipo(String tipo) {
    try {
        SalaDAO salaDAO = new SalaDAO(con);
        List<Sala> salas = salaDAO.listar();
        for (Sala s : salas) {
            if (s.getTipoSala().equalsIgnoreCase(tipo)) {
                return s;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

private Funcion buscarFuncion(int peliculaId, int salaId) {
    try {
        List<Funcion> funciones = funcionCtrl.listar();
        for (Funcion f : funciones) {
            if (f.getPeliculaId() == peliculaId && f.getSalaId() == salaId) {
                return f;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

private Asiento buscarAsiento(int salaId, String numeroAsiento) {
    try {
        List<Asiento> asientos = asientoDAO.listar();
        for (Asiento a : asientos) {
            if (a.getSalaId() == salaId && a.getNumeroSilla().equals(numeroAsiento)) {
                return a;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

// Método para actualizar todas las listas
private void actualizarTodasLasListas() {
    listarClientes();
    listarPeliculas();
    listarSalas();
    listarFunciones();
    listarAsientos();
    listarEntradas();
    listarFacturas();
}
    private void listarConsultaRelacional() {
        try {
            modelConsultaRelacional.setRowCount(0); // limpiar tabla
            String sql = "SELECT " +
                    "c.documento AS DocumentoCliente, " +
                    "c.nombre AS NombreCliente, " +
                    "p.titulo AS Pelicula, " +
                    "s.tipo_sala AS Sala, " +
                    "a.numero_silla AS Asiento, " +
                    "e.valor AS ValorEntrada, " +
                    "f.fecha AS FechaFactura " +
                    "FROM entradas e " +
                    "JOIN clientes c ON e.cliente_documento = c.documento " +
                    "JOIN funciones fn ON e.funcion_id = fn.id " +
                    "JOIN peliculas p ON fn.pelicula_id = p.id " +
                    "JOIN salas s ON fn.sala_id = s.id " +
                    "JOIN asientos a ON e.asiento_id = a.id " +
                    "JOIN facturas f ON f.cliente_documento = c.documento";

            var stmt = con.createStatement();
            var rs = stmt.executeQuery(sql);

            while (rs.next()) {
                modelConsultaRelacional.addRow(new Object[] {
                        rs.getString("DocumentoCliente"),
                        rs.getString("NombreCliente"),
                        rs.getString("Pelicula"),
                        rs.getString("Sala"),
                        rs.getString("Asiento"),
                        rs.getDouble("ValorEntrada"),
                        rs.getString("FechaFactura")
                });
            }

            rs.close();
            stmt.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error ejecutando consulta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // clientes
    private void actualizarCliente() {
        try {
            int doc = Integer.parseInt(txtDoc.getText().trim());
            String nombre = txtNombre.getText().trim();
            String tel = txtTel.getText().trim();

            if (nombre.isEmpty() || tel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre y teléfono son obligatorios.");
                return;
            }

            clienteCtrl.actualizar(doc, nombre, tel);
            JOptionPane.showMessageDialog(this, "Cliente actualizado.");
            listarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando cliente: " + ex.getMessage());
        }
    }

    private void eliminarCliente() {
        try {
            int doc = Integer.parseInt(txtDoc.getText().trim());
            clienteCtrl.eliminar(doc);
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            listarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando cliente: " + ex.getMessage());
        }
    }

    private void agregarCliente() {
        try {
            String docStr = txtDoc.getText().trim();
            String nombre = txtNombre.getText().trim();
            String tel = txtTel.getText().trim();

            if (docStr.isEmpty() || nombre.isEmpty() || tel.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            int doc;
            try {
                doc = Integer.parseInt(docStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Documento inválido");
                return;
            }

            clienteCtrl.insertar(doc, nombre, tel);
            JOptionPane.showMessageDialog(this, "Cliente agregado.");
            listarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando cliente: " + ex.getMessage());
        }
    }

    private void listarClientes() {
        try {
            List<Cliente> list = clienteCtrl.listar();
            modelClientes.setRowCount(0);
            for (Cliente c : list) {
                modelClientes.addRow(new Object[] { c.getDocumento(), c.getNombre(), c.getTelefono() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando clientes: " + ex.getMessage());
        }
    }

    private void agregarPelicula() {
        try {
            String titulo = txtTitulo.getText().trim();
            String genero = txtGenero.getText().trim();

            if (titulo.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            peliculaCtrl.insertar(titulo, genero);
            JOptionPane.showMessageDialog(this, "Película agregada.");
            listarPeliculas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando película: " + ex.getMessage());
        }
    }

    // peliculas
    private void actualizarPelicula() {
        try {
            int id = (int) modelPeliculas.getValueAt(tblPeliculas.getSelectedRow(), 0);
            String titulo = txtTitulo.getText().trim();
            String genero = txtGenero.getText().trim();

            if (titulo.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            peliculaCtrl.actualizar(id, titulo, genero);
            JOptionPane.showMessageDialog(this, "Película actualizada.");
            listarPeliculas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando película: " + ex.getMessage());
        }
    }

    private void eliminarPelicula() {
        try {
            int id = (int) modelPeliculas.getValueAt(tblPeliculas.getSelectedRow(), 0);
            peliculaCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Película eliminada.");
            listarPeliculas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando película: " + ex.getMessage());
        }
    }

    private void listarPeliculas() {
        try {
            List<Pelicula> list = peliculaCtrl.listar();
            modelPeliculas.setRowCount(0);
            for (Pelicula p : list) {
                modelPeliculas.addRow(new Object[] { p.getId(), p.getTitulo(), p.getGenero() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando películas: " + ex.getMessage());
        }
    }

    private void agregarSala() {
        try {
            String tipo = txtTipoSala.getText().trim();
            String capStr = txtCapacidad.getText().trim();

            if (tipo.isEmpty() || capStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            int cap;
            try {
                cap = Integer.parseInt(capStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Capacidad inválida");
                return;
            }

            service.SalaDAO dao = new service.SalaDAO(con);
            dao.insertar(new Sala(0, tipo, cap));
            JOptionPane.showMessageDialog(this, "Sala agregada.");
            listarSalas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando sala: " + ex.getMessage());
        }
    }

    // salas
    private void actualizarSala() {
        try {
            int id = (int) modelSalas.getValueAt(tblSalas.getSelectedRow(), 0);
            String tipo = txtTipoSala.getText().trim();
            int cap = Integer.parseInt(txtCapacidad.getText().trim());

            service.SalaDAO dao = new service.SalaDAO(con);
            dao.actualizar(new Sala(id, tipo, cap));
            JOptionPane.showMessageDialog(this, "Sala actualizada.");
            listarSalas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando sala: " + ex.getMessage());
        }
    }

    private void eliminarSala() {
        try {
            int id = (int) modelSalas.getValueAt(tblSalas.getSelectedRow(), 0);
            service.SalaDAO dao = new service.SalaDAO(con);
            dao.eliminar(id);
            JOptionPane.showMessageDialog(this, "Sala eliminada.");
            listarSalas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando sala: " + ex.getMessage());
        }
    }

    private void listarSalas() {
        try {
            service.SalaDAO dao = new service.SalaDAO(con);
            java.util.List<Sala> list = dao.listar();
            modelSalas.setRowCount(0);
            for (Sala s : list)
                modelSalas.addRow(new Object[] { s.getId(), s.getTipoSala(), s.getCapacidad() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando salas: " + ex.getMessage());
        }
    }

    private void agregarFuncion() {
        try {
            String peliIdStr = txtPeliculaIdF.getText().trim();
            String salaIdStr = txtSalaIdF.getText().trim();
            String fh = txtFechaHoraF.getText().trim();

            if (peliIdStr.isEmpty() || salaIdStr.isEmpty() || fh.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            int peliId, salaId;
            try {
                peliId = Integer.parseInt(peliIdStr);
                salaId = Integer.parseInt(salaIdStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de película o sala inválido");
                return;
            }

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fechaHora;
            try {
                fechaHora = LocalDateTime.parse(fh, fmt);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "FechaHora con formato incorrecto");
                return;
            }

            funcionCtrl.insertar(peliId, salaId, fechaHora);
            JOptionPane.showMessageDialog(this, "Función agregada.");
            listarFunciones();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando función: " + ex.getMessage());
        }
    }

    // funciones
    private void actualizarFuncion() {
        try {
            int id = (int) modelFunciones.getValueAt(tblFunciones.getSelectedRow(), 0);
            int peliId = Integer.parseInt(txtPeliculaIdF.getText().trim());
            int salaId = Integer.parseInt(txtSalaIdF.getText().trim());
            LocalDateTime fechaHora = LocalDateTime.parse(txtFechaHoraF.getText().trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            funcionCtrl.actualizar(id, peliId, salaId, fechaHora);
            JOptionPane.showMessageDialog(this, "Función actualizada.");
            listarFunciones();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando función: " + ex.getMessage());
        }
    }

    private void eliminarFuncion() {
        try {
            int id = (int) modelFunciones.getValueAt(tblFunciones.getSelectedRow(), 0);
            funcionCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Función eliminada.");
            listarFunciones();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando función: " + ex.getMessage());
        }
    }

    private void listarFunciones() {
        try {
            List<Funcion> list = funcionCtrl.listar();
            modelFunciones.setRowCount(0);
            for (Funcion f : list)
                modelFunciones.addRow(new Object[] { f.getId(), f.getPeliculaId(), f.getSalaId(), f.getFechaHora() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando funciones: " + ex.getMessage());
        }
    }

    // asientos
    private void actualizarAsiento() {
        try {
            int id = (int) modelAsientos.getValueAt(tblAsientos.getSelectedRow(), 0);
            int salaId = Integer.parseInt(txtSalaIdA.getText().trim());
            String numero = txtNumeroSillaA.getText().trim();

            asientoDAO.actualizar(new Asiento(id, salaId, numero, true));
            JOptionPane.showMessageDialog(this, "Asiento actualizado.");
            listarAsientos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando asiento: " + ex.getMessage());
        }
    }

    private void eliminarAsiento() {
        try {
            int id = (int) modelAsientos.getValueAt(tblAsientos.getSelectedRow(), 0);
            asientoDAO.eliminar(id);
            JOptionPane.showMessageDialog(this, "Asiento eliminado.");
            listarAsientos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando asiento: " + ex.getMessage());
        }
    }

    private void agregarAsiento() {
        try {
            String textoSala = txtSalaIdA.getText().trim();
            String textoNum = txtNumeroSillaA.getText().trim();

            if (textoSala.isEmpty() || textoNum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            int salaId;
            try {
                salaId = Integer.parseInt(textoSala);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de sala inválido");
                return;
            }

            // número de silla puede ser string si es algo como "A1"
            String num = textoNum;

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
            for (Asiento a : list)
                modelAsientos.addRow(new Object[] { a.getId(), a.getSalaId(), a.getNumeroSilla(), a.isDisponible() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando asientos: " + ex.getMessage());
        }
    }

    // entradas
    private void actualizarEntrada() {
        try {
            int id = (int) modelEntradas.getValueAt(tblEntradas.getSelectedRow(), 0);
            int clienteDoc = Integer.parseInt(txtClienteEntrada.getText().trim());
            int funcionId = Integer.parseInt(txtFuncionEntrada.getText().trim());
            int asientoId = Integer.parseInt(txtAsientoEntrada.getText().trim());
            double valor = Double.parseDouble(txtValorEntrada.getText().trim());

            entradaCtrl.actualizar(id, clienteDoc, funcionId, asientoId, valor);
            JOptionPane.showMessageDialog(this, "Entrada actualizada.");
            listarEntradas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando entrada: " + ex.getMessage());
        }
    }

    private void eliminarEntrada() {
        try {
            int id = (int) modelEntradas.getValueAt(tblEntradas.getSelectedRow(), 0);
            entradaCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Entrada eliminada.");
            listarEntradas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando entrada: " + ex.getMessage());
        }
    }

    private void venderEntrada() {
        try {
            String docStr = txtClienteEntrada.getText().trim();
            String funcionStr = txtFuncionEntrada.getText().trim();
            String asientoStr = txtAsientoEntrada.getText().trim();
            String valorStr = txtValorEntrada.getText().trim();

            if (docStr.isEmpty() || funcionStr.isEmpty() || asientoStr.isEmpty() || valorStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            int doc, funcionId, asientoId;
            double valor;
            try {
                doc = Integer.parseInt(docStr);
                funcionId = Integer.parseInt(funcionStr);
                asientoId = Integer.parseInt(asientoStr);
                valor = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Datos numéricos inválidos");
                return;
            }

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
            for (Entrada e : list)
                modelEntradas.addRow(new Object[] { e.getId(), e.getClienteDocumento(), e.getFuncionId(),
                        e.getAsientoId(), e.getValor() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando entradas: " + ex.getMessage());
        }
    }

    // facturas
    private void actualizarFactura() {
        try {
            int id = (int) modelFacturas.getValueAt(tblFacturas.getSelectedRow(), 0);
            int clienteDoc = Integer.parseInt(txtClienteFactura.getText().trim());
            double valorTotal = Double.parseDouble(txtValorFactura.getText().trim());
            String datosEmpresa = txtDatosEmpresa.getText().trim();

            facturaCtrl.actualizar(id, clienteDoc, valorTotal, datosEmpresa);
            JOptionPane.showMessageDialog(this, "Factura actualizada.");
            listarFacturas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando factura: " + ex.getMessage());
        }
    }

    private void eliminarFactura() {
        try {
            int id = (int) modelFacturas.getValueAt(tblFacturas.getSelectedRow(), 0);
            facturaCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Factura eliminada.");
            listarFacturas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando factura: " + ex.getMessage());
        }
    }

    private void generarFactura() {
        try {
            String docStr = txtClienteFactura.getText().trim();
            String valorStr = txtValorFactura.getText().trim();
            String datos = txtDatosEmpresa.getText().trim();

            if (docStr.isEmpty() || valorStr.isEmpty() || datos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
                return;
            }

            int doc;
            double valorTotal;
            try {
                doc = Integer.parseInt(docStr);
                valorTotal = Double.parseDouble(valorStr);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Documento o valor inválido");
                return;
            }

            int facturaId = facturaCtrl.crearFactura(doc, valorTotal, datos);
            JOptionPane.showMessageDialog(this, "Factura creada ID: " + facturaId);
            listarFacturas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creando factura: " + ex.getMessage());
        }
    }

    private void listarFacturas() {
        try {
            java.util.List<Factura> list = facturaCtrl.listar();
            modelFacturas.setRowCount(0);
            for (Factura f : list) {
                modelFacturas.addRow(new Object[] {
                        f.getId(),
                        f.getClienteDocumento(),
                        f.getValorTotal(),
                        f.getDatosEmpresa()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando facturas: " + ex.getMessage());
        }
    }

    // limpiar campos
    private void limpiarCampos() {
        // Clientes
        txtDoc.setText("");
        txtNombre.setText("");
        txtTel.setText("");

        // Películas
        txtTitulo.setText("");
        txtGenero.setText("");

        // Salas
        txtTipoSala.setText("");
        txtCapacidad.setText("");

        // Funciones
        txtPeliculaIdF.setText("");
        txtSalaIdF.setText("");
        txtFechaHoraF.setText("");

        // Asientos
        txtSalaIdA.setText("");
        txtNumeroSillaA.setText("");

        // Entradas
        txtClienteEntrada.setText("");
        txtFuncionEntrada.setText("");
        txtAsientoEntrada.setText("");
        txtValorEntrada.setText("");

        // Facturas
        txtClienteFactura.setText("");
        txtValorFactura.setText("");
        txtDatosEmpresa.setText("");
    }

}
