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

// Clase principal que representa la ventana de la aplicación de cine
public class CineFrame extends JFrame {
    // Conexión a la base de datos
    private Connection con;
    
    // Controladores para cada entidad del sistema
    private ClienteController clienteCtrl;
    private PeliculaController peliculaCtrl;
    // private SalaControllerHelper salaHelper; // helper para salas (usaremos SalaDAO directamente)
    private FuncionController funcionCtrl;
    private AsientoDAO asientoDAO;
    private EntradaController entradaCtrl;
    private FacturaController facturaCtrl;

    // Componentes de la interfaz de usuario
    
    // Panel de pestañas para organizar las diferentes secciones
    private JTabbedPane tabs;

    // ---------- COMPONENTES PARA CLIENTES ----------
    private JTextField txtDoc, txtNombre, txtTel;
    private JTable tblClientes;
    private DefaultTableModel modelClientes;

    // ---------- COMPONENTES PARA PELÍCULAS ----------
    private JTextField txtTitulo, txtGenero;
    private JTable tblPeliculas;
    private DefaultTableModel modelPeliculas;

    // ---------- COMPONENTES PARA SALAS ----------
    private JTextField txtTipoSala, txtCapacidad;
    private JTable tblSalas;
    private DefaultTableModel modelSalas;

    // ---------- COMPONENTES PARA FUNCIONES ----------
    private JTextField txtPeliculaIdF, txtSalaIdF, txtFechaHoraF;
    private JTable tblFunciones;
    private DefaultTableModel modelFunciones;

    // ---------- COMPONENTES PARA ASIENTOS ----------
    private JTextField txtSalaIdA, txtNumeroSillaA;
    private JTable tblAsientos;
    private DefaultTableModel modelAsientos;

    // ---------- COMPONENTES PARA ENTRADAS ----------
    private JTextField txtClienteEntrada, txtFuncionEntrada, txtAsientoEntrada, txtValorEntrada;
    private JTable tblEntradas;
    private DefaultTableModel modelEntradas;

    // ---------- COMPONENTES PARA FACTURAS ----------
    private JTextField txtClienteFactura, txtValorFactura, txtDatosEmpresa;
    private JTable tblFacturas;
    private DefaultTableModel modelFacturas;

    // ---------- COMPONENTES PARA CONSULTA RELACIONAL ----------
    private JTable tblConsultaRelacional;
    private DefaultTableModel modelConsultaRelacional;

    // Constructor principal que recibe la conexión a la base de datos
    public CineFrame(Connection con) {
        this.con = con;
        // Inicializar todos los controladores con la conexión a la base de datos
        this.clienteCtrl = new ClienteController(con);
        this.peliculaCtrl = new PeliculaController(con);
        this.funcionCtrl = new FuncionController(con);
        this.asientoDAO = new AsientoDAO(con);
        this.entradaCtrl = new EntradaController(con);
        this.facturaCtrl = new FacturaController(con);

        // Configurar propiedades básicas de la ventana
        setTitle("Cine App");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI(); // Inicializar la interfaz de usuario
    }

    // Método para inicializar todos los componentes de la interfaz de usuario
    private void initUI() {
        tabs = new JTabbedPane(); // Crear el panel de pestañas

        // ---------- PANEL GENERAL CON BOTÓN LIMPIAR ----------
        // Panel para el botón Limpiar que se muestra en la parte inferior
        JPanel pBotonGeneral = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnLimpiar = new JButton("Limpiar");
        // Agregar listener para limpiar todos los campos cuando se presione el botón
        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos(); // Llamar al método para limpiar campos
            }
        });
        pBotonGeneral.add(btnLimpiar); // Agregar botón al panel

        // Agregar el panel del botón al frame en la posición sur (abajo)
        add(pBotonGeneral, BorderLayout.SOUTH);

        // ---------- PANEL 1: CLIENTES ----------
        JPanel pClientes = new JPanel(new BorderLayout());
        // Panel del formulario para clientes con diseño de grilla
        JPanel pCliForm = new JPanel(new GridLayout(4, 2, 5, 5));
        txtDoc = new JTextField();
        txtNombre = new JTextField();
        txtTel = new JTextField();
        JButton btnAddCliente = new JButton("Agregar Cliente");
        // Agregar listener para el botón de agregar cliente usando lambda
        btnAddCliente.addActionListener(_ -> agregarCliente());
        
        // Agregar componentes al formulario de clientes
        pCliForm.add(new JLabel("Documento:"));
        pCliForm.add(txtDoc);
        pCliForm.add(new JLabel("Nombre:"));
        pCliForm.add(txtNombre);
        pCliForm.add(new JLabel("Teléfono:"));
        pCliForm.add(txtTel);
        pCliForm.add(btnAddCliente);
        pClientes.add(pCliForm, BorderLayout.NORTH); // Agregar formulario en la parte norte

        // Configurar modelo y tabla para mostrar clientes
        modelClientes = new DefaultTableModel(new Object[] { "Documento", "Nombre", "Telefono" }, 0);
        tblClientes = new JTable(modelClientes);
        pClientes.add(new JScrollPane(tblClientes), BorderLayout.CENTER); // Agregar tabla con scroll

        // Botón para listar clientes
        JButton btnListClientes = new JButton("Listar Clientes");
        btnListClientes.addActionListener(_ -> listarClientes());
        pClientes.add(btnListClientes, BorderLayout.SOUTH);

        // ---------- PANEL 2: PELÍCULAS ----------
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

        // Configurar modelo y tabla para películas
        modelPeliculas = new DefaultTableModel(new Object[] { "ID", "Título", "Género" }, 0);
        tblPeliculas = new JTable(modelPeliculas);
        pPeliculas.add(new JScrollPane(tblPeliculas), BorderLayout.CENTER);
        JButton btnListPeliculas = new JButton("Listar Películas");
        btnListPeliculas.addActionListener(_ -> listarPeliculas());
        pPeliculas.add(btnListPeliculas, BorderLayout.SOUTH);

        // ---------- PANEL 3: SALAS ----------
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

        // Configurar modelo y tabla para salas
        modelSalas = new DefaultTableModel(new Object[] { "ID", "Tipo Sala", "Capacidad" }, 0);
        tblSalas = new JTable(modelSalas);
        pSalas.add(new JScrollPane(tblSalas), BorderLayout.CENTER);
        JButton btnListSalas = new JButton("Listar Salas");
        btnListSalas.addActionListener(_ -> listarSalas());
        pSalas.add(btnListSalas, BorderLayout.SOUTH);

        // ---------- PANEL 4: FUNCIONES ----------
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

        // Configurar modelo y tabla para funciones
        modelFunciones = new DefaultTableModel(new Object[] { "ID", "PeliculaID", "SalaID", "FechaHora" }, 0);
        tblFunciones = new JTable(modelFunciones);
        pFunciones.add(new JScrollPane(tblFunciones), BorderLayout.CENTER);
        JButton btnListFunciones = new JButton("Listar Funciones");
        btnListFunciones.addActionListener(_ -> listarFunciones());
        pFunciones.add(btnListFunciones, BorderLayout.SOUTH);

        // ---------- PANEL 5: ASIENTOS ----------
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

        // Configurar modelo y tabla para asientos
        modelAsientos = new DefaultTableModel(new Object[] { "ID", "SalaID", "NoSilla", "Disponible" }, 0);
        tblAsientos = new JTable(modelAsientos);
        pAsientos.add(new JScrollPane(tblAsientos), BorderLayout.CENTER);
        JButton btnListAsientos = new JButton("Listar Asientos");
        btnListAsientos.addActionListener(_ -> listarAsientos());
        pAsientos.add(btnListAsientos, BorderLayout.SOUTH);

        // ---------- PANEL 6: ENTRADAS ----------
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

        // Configurar modelo y tabla para entradas
        modelEntradas = new DefaultTableModel(new Object[] { "ID", "ClienteDoc", "FuncionID", "AsientoID", "Valor" },
                0);
        tblEntradas = new JTable(modelEntradas);
        pEntradas.add(new JScrollPane(tblEntradas), BorderLayout.CENTER);
        JButton btnListEntradas = new JButton("Listar Entradas");
        btnListEntradas.addActionListener(_ -> listarEntradas());
        pEntradas.add(btnListEntradas, BorderLayout.SOUTH);

        // ---------- PANEL 7: FACTURAS ----------
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

        // Configurar modelo y tabla para facturas
        modelFacturas = new DefaultTableModel(new Object[] { "ID", "ClienteDoc", "ValorTotal", "DatosEmpresa" }, 0);
        tblFacturas = new JTable(modelFacturas);
        pFacturas.add(new JScrollPane(tblFacturas), BorderLayout.CENTER);
        JButton btnListFacturas = new JButton("Listar Facturas");
        btnListFacturas.addActionListener(_ -> listarFacturas());
        pFacturas.add(btnListFacturas, BorderLayout.SOUTH);
        
        // ---------- PANEL 8: CONSULTA RELACIONAL ----------
        // Panel para consulta relacional que muestra datos combinados de múltiples tablas
        JPanel pConsulta = new JPanel(new BorderLayout());

        // Panel superior con formulario para CRUD en la consulta relacional
        JPanel pConsultaForm = new JPanel(new GridLayout(4, 4, 5, 5));
        JTextField txtDocConsulta = new JTextField();
        JTextField txtNombreConsulta = new JTextField();
        JTextField txtPeliculaConsulta = new JTextField();
        JTextField txtSalaConsulta = new JTextField();
        JTextField txtAsientoConsulta = new JTextField();
        JTextField txtValorConsulta = new JTextField();
        //JTextField txtFechaFacturaConsulta = new JTextField();
        JLabel lblFechaAuto = new JLabel("Fecha auto-generada"); // Etiqueta para fecha automática
        JButton btnAddConsulta = new JButton("Agregar");
        JButton btnUpdateConsulta = new JButton("Actualizar");
        JButton btnDeleteConsulta = new JButton("Eliminar");

        // Agregar componentes al formulario de consulta
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
        pConsultaForm.add(lblFechaAuto); // Mostrar que la fecha es automática
        pConsultaForm.add(btnAddConsulta);
        pConsultaForm.add(btnUpdateConsulta);
        pConsultaForm.add(btnDeleteConsulta);

        // Definir columnas según la consulta SQL que une múltiples tablas
        modelConsultaRelacional = new DefaultTableModel(new Object[] {
                "DocumentoCliente", "NombreCliente", "Pelicula",
                "Sala", "Asiento", "ValorEntrada", "FechaFactura"
        }, 0);

        tblConsultaRelacional = new JTable(modelConsultaRelacional);
        JScrollPane scrollConsulta = new JScrollPane(tblConsultaRelacional);

        // Panel de botones para la consulta
        JPanel pConsultaBtns = new JPanel(new FlowLayout());
        //JButton btnListConsulta = new JButton("Ejecutar Consulta");
        //btnListConsulta.addActionListener(_ -> listarConsultaRelacional());
        //pConsultaBtns.add(btnListConsulta);

        // Agregar componentes al panel principal de consulta
        pConsulta.add(pConsultaForm, BorderLayout.NORTH);
        pConsulta.add(scrollConsulta, BorderLayout.CENTER);
        pConsulta.add(pConsultaBtns, BorderLayout.SOUTH);

        // Agregar tab de consulta relacional
        tabs.addTab("Consulta Relacional", pConsulta);

        // ---------- LISTENERS PARA BOTONES DE CONSULTA RELACIONAL ----------
        // Listener para agregar desde la consulta relacional
        btnAddConsulta.addActionListener(_ -> {
            agregarDesdeConsulta(txtDocConsulta, txtNombreConsulta, txtPeliculaConsulta, 
                                txtSalaConsulta, txtAsientoConsulta, txtValorConsulta);
            limpiarCamposConsulta(txtDocConsulta, txtNombreConsulta, txtPeliculaConsulta,
                                 txtSalaConsulta, txtAsientoConsulta, txtValorConsulta);
        });

        // Listener para actualizar desde la consulta relacional
        btnUpdateConsulta.addActionListener(_ -> actualizarDesdeConsulta(
                txtDocConsulta, txtNombreConsulta, txtPeliculaConsulta,
                txtSalaConsulta, txtAsientoConsulta, txtValorConsulta));

        // Listener para eliminar desde la consulta relacional
        btnDeleteConsulta.addActionListener(_ -> eliminarDesdeConsulta());

        // Listener para selección en tabla de consulta relacional
        // Cuando se selecciona una fila, carga los datos en los campos del formulario
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

        // ---------- AGREGAR TODAS LAS PESTAÑAS AL PANEL PRINCIPAL ----------
        tabs.addTab("Clientes", pClientes);
        tabs.addTab("Películas", pPeliculas);
        tabs.addTab("Salas", pSalas);
        tabs.addTab("Funciones", pFunciones);
        tabs.addTab("Asientos", pAsientos);
        tabs.addTab("Entradas", pEntradas);
        tabs.addTab("Facturas", pFacturas);

        // Agregar el panel de pestañas al centro del frame
        add(tabs, BorderLayout.CENTER);

        // ---------- CARGAR DATOS INICIALES EN LAS TABLAS ----------
        listarClientes();
        listarPeliculas();
        listarSalas();
        listarFunciones();
        listarAsientos();
        listarEntradas();
        listarFacturas();
        listarConsultaRelacional();

        // ---------- BOTONES ADICIONALES PARA OPERACIONES CRUD ----------
        
        // Botones para clientes
        JButton btnActualizarCliente = new JButton("Actualizar Cliente");
        btnActualizarCliente.addActionListener(_ -> actualizarCliente());
        JButton btnEliminarCliente = new JButton("Eliminar Cliente");
        btnEliminarCliente.addActionListener(_ -> eliminarCliente());
        
        // Botones para películas
        JButton btnActualizarPelicula = new JButton("Actualizar Película");
        btnActualizarPelicula.addActionListener(_ -> actualizarPelicula());
        JButton btnEliminarPelicula = new JButton("Eliminar Película");
        btnEliminarPelicula.addActionListener(_ -> eliminarPelicula());
        
        // Botones para salas
        JButton btnActualizarSala = new JButton("Actualizar Sala");
        btnActualizarSala.addActionListener(_ -> actualizarSala());
        JButton btnEliminarSala = new JButton("Eliminar Sala");
        btnEliminarSala.addActionListener(_ -> eliminarSala());
        
        // Botones para funciones
        JButton btnActualizarFuncion = new JButton("Actualizar Función");
        btnActualizarFuncion.addActionListener(_ -> actualizarFuncion());
        JButton btnEliminarFuncion = new JButton("Eliminar Función");
        btnEliminarFuncion.addActionListener(_ -> eliminarFuncion());
        
        // Botones para asientos
        JButton btnActualizarAsiento = new JButton("Actualizar Asiento");
        btnActualizarAsiento.addActionListener(_ -> actualizarAsiento());
        JButton btnEliminarAsiento = new JButton("Eliminar Asiento");
        btnEliminarAsiento.addActionListener(_ -> eliminarAsiento());
        
        // Botones para entradas
        JButton btnActualizarEntrada = new JButton("Actualizar Entrada");
        btnActualizarEntrada.addActionListener(_ -> actualizarEntrada());
        JButton btnEliminarEntrada = new JButton("Eliminar Entrada");
        btnEliminarEntrada.addActionListener(_ -> eliminarEntrada());
        
        // Botones para facturas
        JButton btnActualizarFactura = new JButton("Actualizar Factura");
        btnActualizarFactura.addActionListener(_ -> actualizarFactura());
        JButton btnEliminarFactura = new JButton("Eliminar Factura");
        btnEliminarFactura.addActionListener(_ -> eliminarFactura());

        // ---------- PANELES DE BOTONES PARA CADA PESTAÑA ----------
        
        // Panel de botones para clientes
        JPanel pCliBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pCliBtns.add(btnActualizarCliente);
        pCliBtns.add(btnEliminarCliente);
        pClientes.add(pCliBtns, BorderLayout.SOUTH);

        // Panel de botones para películas
        JPanel pPelBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pPelBtns.add(btnActualizarPelicula);
        pPelBtns.add(btnEliminarPelicula);
        pPeliculas.add(pPelBtns, BorderLayout.SOUTH);

        // Panel de botones para salas
        JPanel pSalaBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pSalaBtns.add(btnActualizarSala);
        pSalaBtns.add(btnEliminarSala);
        pSalas.add(pSalaBtns, BorderLayout.SOUTH);

        // Panel de botones para funciones
        JPanel pFunBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pFunBtns.add(btnActualizarFuncion);
        pFunBtns.add(btnEliminarFuncion);
        pFunciones.add(pFunBtns, BorderLayout.SOUTH);

        // Panel de botones para asientos
        JPanel pAsBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pAsBtns.add(btnActualizarAsiento);
        pAsBtns.add(btnEliminarAsiento);
        pAsientos.add(pAsBtns, BorderLayout.SOUTH);

        // Panel de botones para entradas
        JPanel pEnBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pEnBtns.add(btnActualizarEntrada);
        pEnBtns.add(btnEliminarEntrada);
        pEntradas.add(pEnBtns, BorderLayout.SOUTH);

        // Panel de botones para facturas
        JPanel pFacBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        pFacBtns.add(btnActualizarFactura);
        pFacBtns.add(btnEliminarFactura);
        pFacturas.add(pFacBtns, BorderLayout.SOUTH);

        // ---------- LISTENERS PARA SELECCIÓN EN TABLAS ----------
        // Cuando se selecciona una fila en una tabla, cargar los datos en los campos correspondientes
        
        // Listener para tabla de clientes
        tblClientes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblClientes.getSelectedRow() != -1) {
                int fila = tblClientes.getSelectedRow();
                txtDoc.setText(modelClientes.getValueAt(fila, 0).toString());
                txtNombre.setText(modelClientes.getValueAt(fila, 1).toString());
                txtTel.setText(modelClientes.getValueAt(fila, 2).toString());
            }
        });
        
        // Listener para tabla de películas
        tblPeliculas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblPeliculas.getSelectedRow() != -1) {
                int fila = tblPeliculas.getSelectedRow();
                txtTitulo.setText(modelPeliculas.getValueAt(fila, 1).toString());
                txtGenero.setText(modelPeliculas.getValueAt(fila, 2).toString());
            }
        });
        
        // Listener para tabla de salas
        tblSalas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblSalas.getSelectedRow() != -1) {
                int fila = tblSalas.getSelectedRow();
                txtTipoSala.setText(modelSalas.getValueAt(fila, 1).toString());
                txtCapacidad.setText(modelSalas.getValueAt(fila, 2).toString());
            }
        });
        
        // Listener para tabla de funciones
        tblFunciones.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblFunciones.getSelectedRow() != -1) {
                int fila = tblFunciones.getSelectedRow();
                txtPeliculaIdF.setText(modelFunciones.getValueAt(fila, 1).toString());
                txtSalaIdF.setText(modelFunciones.getValueAt(fila, 2).toString());
                txtFechaHoraF.setText(modelFunciones.getValueAt(fila, 3).toString());
            }
        });
        
        // Listener para tabla de asientos
        tblAsientos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblAsientos.getSelectedRow() != -1) {
                int fila = tblAsientos.getSelectedRow();
                txtSalaIdA.setText(modelAsientos.getValueAt(fila, 1).toString());
                txtNumeroSillaA.setText(modelAsientos.getValueAt(fila, 2).toString());
            }
        });
        
        // Listener para tabla de entradas
        tblEntradas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEntradas.getSelectedRow() != -1) {
                int fila = tblEntradas.getSelectedRow();
                txtClienteEntrada.setText(modelEntradas.getValueAt(fila, 1).toString());
                txtFuncionEntrada.setText(modelEntradas.getValueAt(fila, 2).toString());
                txtAsientoEntrada.setText(modelEntradas.getValueAt(fila, 3).toString());
                txtValorEntrada.setText(modelEntradas.getValueAt(fila, 4).toString());
            }
        });
        
        // Listener para tabla de facturas
        tblFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblFacturas.getSelectedRow() != -1) {
                int fila = tblFacturas.getSelectedRow();
                txtClienteFactura.setText(modelFacturas.getValueAt(fila, 1).toString());
                txtValorFactura.setText(modelFacturas.getValueAt(fila, 2).toString());
                txtDatosEmpresa.setText(modelFacturas.getValueAt(fila, 3).toString());
            }
        });
    }

    // ---------- MÉTODOS PARA CONSULTA RELACIONAL ----------

    // Método para agregar un registro completo desde la consulta relacional
    // Crea automáticamente todas las entidades relacionadas si no existen
    private void agregarDesdeConsulta(JTextField txtDoc, JTextField txtNombre, JTextField txtPelicula,
                                     JTextField txtSala, JTextField txtAsiento, JTextField txtValor) {
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

    // Método para actualizar un registro desde la consulta relacional
    private void actualizarDesdeConsulta(JTextField txtDoc, JTextField txtNombre, JTextField txtPelicula,
                                        JTextField txtSala, JTextField txtAsiento, JTextField txtValor) {
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

    // Método para limpiar campos del formulario de consulta relacional
    private void limpiarCamposConsulta(JTextField txtDoc, JTextField txtNombre, JTextField txtPelicula,
                                      JTextField txtSala, JTextField txtAsiento, JTextField txtValor) {
        txtDoc.setText("");
        txtNombre.setText("");
        txtPelicula.setText("");
        txtSala.setText("");
        txtAsiento.setText("");
        txtValor.setText("");
    }

    // Método para eliminar un registro desde la consulta relacional
    private void eliminarDesdeConsulta() {
        try {
            int fila = tblConsultaRelacional.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un registro para eliminar");
                return;
            }

            String documento = modelConsultaRelacional.getValueAt(fila, 0).toString();
            int doc = Integer.parseInt(documento);

            // Confirmar eliminación
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

    // ---------- MÉTODOS AUXILIARES PARA BÚSQUEDA ----------

    // Buscar película por título
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

    // Buscar sala por tipo
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

    // Buscar función por película y sala
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

    // Buscar asiento por sala y número
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

    // Método para actualizar todas las listas de la interfaz
    private void actualizarTodasLasListas() {
        listarClientes();
        listarPeliculas();
        listarSalas();
        listarFunciones();
        listarAsientos();
        listarEntradas();
        listarFacturas();
    }

    // Método para ejecutar y mostrar la consulta relacional
    private void listarConsultaRelacional() {
        try {
            modelConsultaRelacional.setRowCount(0); // limpiar tabla
            
            // Consulta SQL que une múltiples tablas para obtener información completa
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

            // Llenar la tabla con los resultados de la consulta
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

    // ---------- MÉTODOS PARA CLIENTES ----------

    // Actualizar cliente existente
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
            listarClientes(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando cliente: " + ex.getMessage());
        }
    }

    // Eliminar cliente
    private void eliminarCliente() {
        try {
            int doc = Integer.parseInt(txtDoc.getText().trim());
            clienteCtrl.eliminar(doc);
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
            listarClientes(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando cliente: " + ex.getMessage());
        }
    }

    // Agregar nuevo cliente
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
            listarClientes(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando cliente: " + ex.getMessage());
        }
    }

    // Listar todos los clientes en la tabla
    private void listarClientes() {
        try {
            List<Cliente> list = clienteCtrl.listar();
            modelClientes.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de clientes
            for (Cliente c : list) {
                modelClientes.addRow(new Object[] { c.getDocumento(), c.getNombre(), c.getTelefono() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando clientes: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS PARA PELÍCULAS ----------

    // Agregar nueva película
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
            listarPeliculas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando película: " + ex.getMessage());
        }
    }

    // Actualizar película existente
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
            listarPeliculas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando película: " + ex.getMessage());
        }
    }

    // Eliminar película
    private void eliminarPelicula() {
        try {
            int id = (int) modelPeliculas.getValueAt(tblPeliculas.getSelectedRow(), 0);
            peliculaCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Película eliminada.");
            listarPeliculas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando película: " + ex.getMessage());
        }
    }

    // Listar todas las películas en la tabla
    private void listarPeliculas() {
        try {
            List<Pelicula> list = peliculaCtrl.listar();
            modelPeliculas.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de películas
            for (Pelicula p : list) {
                modelPeliculas.addRow(new Object[] { p.getId(), p.getTitulo(), p.getGenero() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando películas: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS PARA SALAS ----------

    // Agregar nueva sala
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
            listarSalas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando sala: " + ex.getMessage());
        }
    }

    // Actualizar sala existente
    private void actualizarSala() {
        try {
            int id = (int) modelSalas.getValueAt(tblSalas.getSelectedRow(), 0);
            String tipo = txtTipoSala.getText().trim();
            int cap = Integer.parseInt(txtCapacidad.getText().trim());

            service.SalaDAO dao = new service.SalaDAO(con);
            dao.actualizar(new Sala(id, tipo, cap));
            JOptionPane.showMessageDialog(this, "Sala actualizada.");
            listarSalas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando sala: " + ex.getMessage());
        }
    }

    // Eliminar sala
    private void eliminarSala() {
        try {
            int id = (int) modelSalas.getValueAt(tblSalas.getSelectedRow(), 0);
            service.SalaDAO dao = new service.SalaDAO(con);
            dao.eliminar(id);
            JOptionPane.showMessageDialog(this, "Sala eliminada.");
            listarSalas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando sala: " + ex.getMessage());
        }
    }

    // Listar todas las salas en la tabla
    private void listarSalas() {
        try {
            service.SalaDAO dao = new service.SalaDAO(con);
            java.util.List<Sala> list = dao.listar();
            modelSalas.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de salas
            for (Sala s : list)
                modelSalas.addRow(new Object[] { s.getId(), s.getTipoSala(), s.getCapacidad() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando salas: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS PARA FUNCIONES ----------

    // Agregar nueva función
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

            // Parsear fecha y hora con el formato especificado
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
            listarFunciones(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando función: " + ex.getMessage());
        }
    }

    // Actualizar función existente
    private void actualizarFuncion() {
        try {
            int id = (int) modelFunciones.getValueAt(tblFunciones.getSelectedRow(), 0);
            int peliId = Integer.parseInt(txtPeliculaIdF.getText().trim());
            int salaId = Integer.parseInt(txtSalaIdF.getText().trim());
            LocalDateTime fechaHora = LocalDateTime.parse(txtFechaHoraF.getText().trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            funcionCtrl.actualizar(id, peliId, salaId, fechaHora);
            JOptionPane.showMessageDialog(this, "Función actualizada.");
            listarFunciones(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando función: " + ex.getMessage());
        }
    }

    // Eliminar función
    private void eliminarFuncion() {
        try {
            int id = (int) modelFunciones.getValueAt(tblFunciones.getSelectedRow(), 0);
            funcionCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Función eliminada.");
            listarFunciones(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando función: " + ex.getMessage());
        }
    }

    // Listar todas las funciones en la tabla
    private void listarFunciones() {
        try {
            List<Funcion> list = funcionCtrl.listar();
            modelFunciones.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de funciones
            for (Funcion f : list)
                modelFunciones.addRow(new Object[] { f.getId(), f.getPeliculaId(), f.getSalaId(), f.getFechaHora() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando funciones: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS PARA ASIENTOS ----------

    // Actualizar asiento existente
    private void actualizarAsiento() {
        try {
            int id = (int) modelAsientos.getValueAt(tblAsientos.getSelectedRow(), 0);
            int salaId = Integer.parseInt(txtSalaIdA.getText().trim());
            String numero = txtNumeroSillaA.getText().trim();

            asientoDAO.actualizar(new Asiento(id, salaId, numero, true));
            JOptionPane.showMessageDialog(this, "Asiento actualizado.");
            listarAsientos(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando asiento: " + ex.getMessage());
        }
    }

    // Eliminar asiento
    private void eliminarAsiento() {
        try {
            int id = (int) modelAsientos.getValueAt(tblAsientos.getSelectedRow(), 0);
            asientoDAO.eliminar(id);
            JOptionPane.showMessageDialog(this, "Asiento eliminado.");
            listarAsientos(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando asiento: " + ex.getMessage());
        }
    }

    // Agregar nuevo asiento
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
            listarAsientos(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error agregando asiento: " + ex.getMessage());
        }
    }

    // Listar todos los asientos en la tabla
    private void listarAsientos() {
        try {
            List<Asiento> list = asientoDAO.listar();
            modelAsientos.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de asientos
            for (Asiento a : list)
                modelAsientos.addRow(new Object[] { a.getId(), a.getSalaId(), a.getNumeroSilla(), a.isDisponible() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando asientos: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS PARA ENTRADAS ----------

    // Actualizar entrada existente
    private void actualizarEntrada() {
        try {
            int id = (int) modelEntradas.getValueAt(tblEntradas.getSelectedRow(), 0);
            int clienteDoc = Integer.parseInt(txtClienteEntrada.getText().trim());
            int funcionId = Integer.parseInt(txtFuncionEntrada.getText().trim());
            int asientoId = Integer.parseInt(txtAsientoEntrada.getText().trim());
            double valor = Double.parseDouble(txtValorEntrada.getText().trim());

            entradaCtrl.actualizar(id, clienteDoc, funcionId, asientoId, valor);
            JOptionPane.showMessageDialog(this, "Entrada actualizada.");
            listarEntradas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando entrada: " + ex.getMessage());
        }
    }

    // Eliminar entrada
    private void eliminarEntrada() {
        try {
            int id = (int) modelEntradas.getValueAt(tblEntradas.getSelectedRow(), 0);
            entradaCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Entrada eliminada.");
            listarEntradas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando entrada: " + ex.getMessage());
        }
    }

    // Vender nueva entrada (crear entrada)
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
            listarEntradas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error vendiendo entrada: " + ex.getMessage());
        }
    }

    // Listar todas las entradas en la tabla
    private void listarEntradas() {
        try {
            java.util.List<Entrada> list = entradaCtrl.listar();
            modelEntradas.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de entradas
            for (Entrada e : list)
                modelEntradas.addRow(new Object[] { e.getId(), e.getClienteDocumento(), e.getFuncionId(),
                        e.getAsientoId(), e.getValor() });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error listando entradas: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS PARA FACTURAS ----------

    // Actualizar factura existente
    private void actualizarFactura() {
        try {
            int id = (int) modelFacturas.getValueAt(tblFacturas.getSelectedRow(), 0);
            int clienteDoc = Integer.parseInt(txtClienteFactura.getText().trim());
            double valorTotal = Double.parseDouble(txtValorFactura.getText().trim());
            String datosEmpresa = txtDatosEmpresa.getText().trim();

            facturaCtrl.actualizar(id, clienteDoc, valorTotal, datosEmpresa);
            JOptionPane.showMessageDialog(this, "Factura actualizada.");
            listarFacturas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error actualizando factura: " + ex.getMessage());
        }
    }

    // Eliminar factura
    private void eliminarFactura() {
        try {
            int id = (int) modelFacturas.getValueAt(tblFacturas.getSelectedRow(), 0);
            facturaCtrl.eliminar(id);
            JOptionPane.showMessageDialog(this, "Factura eliminada.");
            listarFacturas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando factura: " + ex.getMessage());
        }
    }

    // Generar nueva factura
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
            listarFacturas(); // Actualizar la tabla
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error creando factura: " + ex.getMessage());
        }
    }

    // Listar todas las facturas en la tabla
    private void listarFacturas() {
        try {
            java.util.List<Factura> list = facturaCtrl.listar();
            modelFacturas.setRowCount(0); // Limpiar tabla
            // Llenar tabla con datos de facturas
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

    // ---------- MÉTODO PARA LIMPIAR CAMPOS ----------

    // Limpiar todos los campos de texto de la interfaz
    private void limpiarCampos() {
        // Limpiar campos de clientes
        txtDoc.setText("");
        txtNombre.setText("");
        txtTel.setText("");

        // Limpiar campos de películas
        txtTitulo.setText("");
        txtGenero.setText("");

        // Limpiar campos de salas
        txtTipoSala.setText("");
        txtCapacidad.setText("");

        // Limpiar campos de funciones
        txtPeliculaIdF.setText("");
        txtSalaIdF.setText("");
        txtFechaHoraF.setText("");

        // Limpiar campos de asientos
        txtSalaIdA.setText("");
        txtNumeroSillaA.setText("");

        // Limpiar campos de entradas
        txtClienteEntrada.setText("");
        txtFuncionEntrada.setText("");
        txtAsientoEntrada.setText("");
        txtValorEntrada.setText("");

        // Limpiar campos de facturas
        txtClienteFactura.setText("");
        txtValorFactura.setText("");
        txtDatosEmpresa.setText("");
    }

} // Fin de la clase CineFrame