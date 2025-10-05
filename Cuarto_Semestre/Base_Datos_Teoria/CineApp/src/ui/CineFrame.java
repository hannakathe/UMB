package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;

import controller.*;
import model.*;
import service.*;

// Clase principal que representa la ventana de la aplicación de cine
public class CineFrame extends JFrame {
    // Controladores para cada entidad del sistema
    private ClienteController clienteCtrl;
    private PeliculaController peliculaCtrl;
    private FuncionController funcionCtrl;
    private AsientoDAO asientoDAO;
    private EntradaController entradaCtrl;
    private FacturaController facturaCtrl;
    private SalaDAO salaDAO;
    private ImageIcon iconoSillaDisponible;
    private ImageIcon iconoSillaOcupada;
    private ImageIcon iconoSillaSeleccionada;

    // Componentes de la interfaz de usuario
    private JTabbedPane tabs;

    // ---------- COMPONENTES PARA LA PESTAÑA PRINCIPAL ----------
    private JComboBox<String> comboPeliculas;
    private JComboBox<String> comboFunciones;
    private JPanel panelAsientos;
    private JTextField txtDocumentoCliente;
    private JTextField txtNombreCliente;
    private JTextField txtTelefonoCliente;
    private JLabel lblPrecioFuncion;
    private JLabel lblPrecioTotal;
    private List<JCheckBox> asientosSeleccionados;
    
    // ---------- COMPONENTES PARA HISTORIAL ----------
    private JTable tblEntradasVendidas;
    private DefaultTableModel modelEntradasVendidas;
    private JTable tblFacturas;
    private DefaultTableModel modelFacturas;
    
    // ---------- COMPONENTES PARA EDICIÓN ----------
    private JTextField txtEditIdEntrada, txtEditDocumentoEntrada, txtEditClienteEntrada, 
                      txtEditPeliculaEntrada, txtEditSalaEntrada, txtEditAsientoEntrada, 
                      txtEditFechaEntrada, txtEditPrecioEntrada;
    private JTextField txtEditIdFactura, txtEditDocumentoFactura, txtEditClienteFactura,
                      txtEditValorFactura, txtEditFechaFactura, txtEditEmpresaFactura;

    // Variables para selección actual
    private Pelicula peliculaSeleccionada;
    private Funcion funcionSeleccionada;
    private double precioActual;
    private int cantidadAsientosSeleccionados;

    // Constructor principal
    public CineFrame(Connection con) {
        // Inicializar todos los controladores con la conexión a la base de datos
        this.clienteCtrl = new ClienteController(con);
        this.peliculaCtrl = new PeliculaController(con);
        this.funcionCtrl = new FuncionController(con);
        this.asientoDAO = new AsientoDAO(con);
        this.entradaCtrl = new EntradaController(con);
        this.facturaCtrl = new FacturaController(con);
        this.salaDAO = new SalaDAO(con);
        this.asientosSeleccionados = new ArrayList<>();

        cargarIconos();

        // Configurar propiedades básicas de la ventana
        setTitle("Sistema de Cine - Venta de Entradas");
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI(); // Inicializar la interfaz de usuario
    }

    private void cargarIconos() {
        try {
            // Cargar iconos desde la carpeta images
            iconoSillaDisponible = new ImageIcon("Cuarto_Semestre/Base_Datos_Teoria/CineApp/src/images/silla_disponible.png");
            iconoSillaOcupada = new ImageIcon("Cuarto_Semestre/Base_Datos_Teoria/CineApp/src/images/silla_ocupada.png");
            iconoSillaSeleccionada = new ImageIcon("Cuarto_Semestre/Base_Datos_Teoria/CineApp/src/images/silla_seleccion.png");
            
            // Redimensionar los iconos si es necesario
            if (iconoSillaDisponible != null) {
                Image img = iconoSillaDisponible.getImage();
                Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                iconoSillaDisponible = new ImageIcon(newImg);
            }
            if (iconoSillaOcupada != null) {
                Image img = iconoSillaOcupada.getImage();
                Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                iconoSillaOcupada = new ImageIcon(newImg);
            }
            if (iconoSillaSeleccionada != null) {
                Image img = iconoSillaSeleccionada.getImage();
                Image newImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                iconoSillaSeleccionada = new ImageIcon(newImg);
            }
        } catch (Exception e) {
            System.out.println("No se pudieron cargar los iconos: " + e.getMessage());
            iconoSillaDisponible = null;
            iconoSillaOcupada = null;
            iconoSillaSeleccionada = null;
        }
    }

    // Método para inicializar todos los componentes de la interfaz de usuario
    private void initUI() {
        tabs = new JTabbedPane();

        // ---------- PESTAÑA PRINCIPAL: VENTA DE ENTRADAS ----------
        JPanel pPrincipal = crearPanelPrincipal();

        // ---------- PESTAÑA ENTRADAS VENDIDAS ----------
        JPanel pEntradas = crearPanelEntradas();

        // ---------- PESTAÑA FACTURAS ----------
        JPanel pFacturas = crearPanelFacturas();

        // ---------- AGREGAR PESTAÑAS ----------
        tabs.addTab("Venta de Entradas", pPrincipal);
        tabs.addTab("Entradas Vendidas", pEntradas);
        tabs.addTab("Facturas", pFacturas);

        add(tabs, BorderLayout.CENTER);

        // ---------- CONFIGURAR LISTENERS ----------
        configurarListeners();
        
        // ---------- CARGAR DATOS INICIALES ----------
        cargarPeliculas();
        cargarEntradasVendidas();
        cargarFacturas();
    }

    private JPanel crearPanelPrincipal() {
        JPanel pPrincipal = new JPanel(new BorderLayout(10, 10));
        pPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Selección de película y función
        JPanel pSeleccion = new JPanel(new GridLayout(2, 2, 10, 10));
        pSeleccion.setBorder(BorderFactory.createTitledBorder("Selección de Función"));

        pSeleccion.add(new JLabel("Película:"));
        comboPeliculas = new JComboBox<>();
        comboPeliculas.addItem("-- Seleccione una película --");
        pSeleccion.add(comboPeliculas);

        pSeleccion.add(new JLabel("Función:"));
        comboFunciones = new JComboBox<>();
        comboFunciones.addItem("-- Seleccione una función --");
        pSeleccion.add(comboFunciones);

        // Panel de información de precios
        JPanel pInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        lblPrecioFuncion = new JLabel("Precio por entrada: $0.00");
        lblPrecioFuncion.setFont(new Font("Arial", Font.BOLD, 14));
        lblPrecioFuncion.setForeground(Color.BLUE);
        
        lblPrecioTotal = new JLabel("Total: $0.00");
        lblPrecioTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrecioTotal.setForeground(Color.RED);
        
        pInfo.add(lblPrecioFuncion);
        pInfo.add(lblPrecioTotal);

        // Panel central: Asientos - SOLO UNA PANTALLA
        JPanel pAsientosContainer = new JPanel(new BorderLayout());
        pAsientosContainer.setBorder(BorderFactory.createTitledBorder("Selección de Asientos"));
        
        panelAsientos = new JPanel();
        panelAsientos.setLayout(new BorderLayout());
        
        JScrollPane scrollAsientos = new JScrollPane(panelAsientos);
        scrollAsientos.setPreferredSize(new Dimension(800, 400));
        
        pAsientosContainer.add(scrollAsientos, BorderLayout.CENTER);

        // Panel inferior: Datos del cliente y botones
        JPanel pCliente = new JPanel(new GridLayout(3, 2, 10, 10));
        pCliente.setBorder(BorderFactory.createTitledBorder("Datos del Cliente"));

        txtDocumentoCliente = new JTextField();
        txtNombreCliente = new JTextField();
        txtTelefonoCliente = new JTextField();

        pCliente.add(new JLabel("Documento:"));
        pCliente.add(txtDocumentoCliente);
        pCliente.add(new JLabel("Nombre:"));
        pCliente.add(txtNombreCliente);
        pCliente.add(new JLabel("Teléfono:"));
        pCliente.add(txtTelefonoCliente);

        // Panel de botones
        JPanel pBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnVenderEntrada = new JButton("Vender Entrada y Factura");
        JButton btnLimpiar = new JButton("Limpiar");

        btnVenderEntrada.addActionListener(_ -> venderEntradaYFactura());
        btnLimpiar.addActionListener(_ -> limpiarCampos());

        pBotones.add(btnVenderEntrada);
        pBotones.add(btnLimpiar);

        // Organizar panels en la pestaña principal
        JPanel pNorth = new JPanel(new BorderLayout());
        pNorth.add(pSeleccion, BorderLayout.NORTH);
        pNorth.add(pInfo, BorderLayout.CENTER);

        pPrincipal.add(pNorth, BorderLayout.NORTH);
        pPrincipal.add(pAsientosContainer, BorderLayout.CENTER);
        
        JPanel pSouth = new JPanel(new BorderLayout());
        pSouth.add(pCliente, BorderLayout.NORTH);
        pSouth.add(pBotones, BorderLayout.SOUTH);
        pPrincipal.add(pSouth, BorderLayout.SOUTH);

        return pPrincipal;
    }

    private JPanel crearPanelEntradas() {
        JPanel pEntradas = new JPanel(new BorderLayout(10, 10));
        pEntradas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Formulario de edición
        JPanel pFormularioEntradas = new JPanel(new GridLayout(2, 8, 5, 5));
        pFormularioEntradas.setBorder(BorderFactory.createTitledBorder("Editar Entrada"));
        
        // Crear campos de texto para edición
        txtEditIdEntrada = new JTextField(); txtEditIdEntrada.setEditable(false);
        txtEditDocumentoEntrada = new JTextField();
        txtEditClienteEntrada = new JTextField();
        txtEditPeliculaEntrada = new JTextField();
        txtEditSalaEntrada = new JTextField();
        txtEditAsientoEntrada = new JTextField();
        txtEditFechaEntrada = new JTextField(); txtEditFechaEntrada.setEditable(false);
        txtEditPrecioEntrada = new JTextField();
        
        pFormularioEntradas.add(new JLabel("ID:"));
        pFormularioEntradas.add(txtEditIdEntrada);
        pFormularioEntradas.add(new JLabel("Documento:"));
        pFormularioEntradas.add(txtEditDocumentoEntrada);
        pFormularioEntradas.add(new JLabel("Cliente:"));
        pFormularioEntradas.add(txtEditClienteEntrada);
        pFormularioEntradas.add(new JLabel("Película:"));
        pFormularioEntradas.add(txtEditPeliculaEntrada);
        pFormularioEntradas.add(new JLabel("Sala:"));
        pFormularioEntradas.add(txtEditSalaEntrada);
        pFormularioEntradas.add(new JLabel("Asiento:"));
        pFormularioEntradas.add(txtEditAsientoEntrada);
        pFormularioEntradas.add(new JLabel("Fecha:"));
        pFormularioEntradas.add(txtEditFechaEntrada);
        pFormularioEntradas.add(new JLabel("Precio:"));
        pFormularioEntradas.add(txtEditPrecioEntrada);

        // Tabla de entradas
        modelEntradasVendidas = new DefaultTableModel(new Object[] {
            "ID", "Documento", "Cliente", "Película", "Sala", "Asiento", "Fecha", "Precio"
        }, 0);
        tblEntradasVendidas = new JTable(modelEntradasVendidas);
        
        // Panel de botones
        JPanel pBotonesEntradas = new JPanel(new FlowLayout());
        JButton btnImprimirTicket = new JButton("Imprimir Ticket");
        JButton btnActualizarEntradas = new JButton("Actualizar Lista");
        JButton btnEditarEntrada = new JButton("Guardar Cambios");
        JButton btnEliminarEntrada = new JButton("Eliminar Entrada");
        
        btnImprimirTicket.addActionListener(_ -> imprimirTicket());
        btnActualizarEntradas.addActionListener(_ -> cargarEntradasVendidas());
        btnEditarEntrada.addActionListener(_ -> editarEntrada());
        btnEliminarEntrada.addActionListener(_ -> eliminarEntrada());
        
        pBotonesEntradas.add(btnImprimirTicket);
        pBotonesEntradas.add(btnActualizarEntradas);
        pBotonesEntradas.add(btnEditarEntrada);
        pBotonesEntradas.add(btnEliminarEntrada);

        pEntradas.add(pFormularioEntradas, BorderLayout.NORTH);
        pEntradas.add(new JScrollPane(tblEntradasVendidas), BorderLayout.CENTER);
        pEntradas.add(pBotonesEntradas, BorderLayout.SOUTH);

        return pEntradas;
    }

    private JPanel crearPanelFacturas() {
        JPanel pFacturas = new JPanel(new BorderLayout(10, 10));
        pFacturas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Formulario de edición
        JPanel pFormularioFacturas = new JPanel(new GridLayout(2, 6, 5, 5));
        pFormularioFacturas.setBorder(BorderFactory.createTitledBorder("Editar Factura"));
        
        // Crear campos de texto para edición
        txtEditIdFactura = new JTextField(); txtEditIdFactura.setEditable(false);
        txtEditDocumentoFactura = new JTextField();
        txtEditClienteFactura = new JTextField();
        txtEditValorFactura = new JTextField();
        txtEditFechaFactura = new JTextField(); txtEditFechaFactura.setEditable(false);
        txtEditEmpresaFactura = new JTextField();
        
        pFormularioFacturas.add(new JLabel("ID:"));
        pFormularioFacturas.add(txtEditIdFactura);
        pFormularioFacturas.add(new JLabel("Documento:"));
        pFormularioFacturas.add(txtEditDocumentoFactura);
        pFormularioFacturas.add(new JLabel("Cliente:"));
        pFormularioFacturas.add(txtEditClienteFactura);
        pFormularioFacturas.add(new JLabel("Valor Total:"));
        pFormularioFacturas.add(txtEditValorFactura);
        pFormularioFacturas.add(new JLabel("Fecha:"));
        pFormularioFacturas.add(txtEditFechaFactura);
        pFormularioFacturas.add(new JLabel("Empresa:"));
        pFormularioFacturas.add(txtEditEmpresaFactura);

        // Tabla de facturas
        modelFacturas = new DefaultTableModel(new Object[] {
            "ID", "Documento", "Cliente", "Valor Total", "Fecha", "Empresa"
        }, 0);
        tblFacturas = new JTable(modelFacturas);
        
        // Panel de botones
        JPanel pBotonesFacturas = new JPanel(new FlowLayout());
        JButton btnImprimirFactura = new JButton("Imprimir Factura");
        JButton btnActualizarFacturas = new JButton("Actualizar Lista");
        JButton btnEditarFactura = new JButton("Guardar Cambios");
        JButton btnEliminarFactura = new JButton("Eliminar Factura");
        
        btnImprimirFactura.addActionListener(_ -> imprimirFactura());
        btnActualizarFacturas.addActionListener(_ -> cargarFacturas());
        btnEditarFactura.addActionListener(_ -> editarFactura());
        btnEliminarFactura.addActionListener(_ -> eliminarFactura());

        pBotonesFacturas.add(btnImprimirFactura);
        pBotonesFacturas.add(btnActualizarFacturas);
        pBotonesFacturas.add(btnEditarFactura);
        pBotonesFacturas.add(btnEliminarFactura);

        pFacturas.add(pFormularioFacturas, BorderLayout.NORTH);
        pFacturas.add(new JScrollPane(tblFacturas), BorderLayout.CENTER);
        pFacturas.add(pBotonesFacturas, BorderLayout.SOUTH);

        return pFacturas;
    }

    private void configurarListeners() {
        // Listener para selección de película
        comboPeliculas.addActionListener(_ -> {
            if (comboPeliculas.getSelectedIndex() > 0) {
                String peliculaStr = (String) comboPeliculas.getSelectedItem();
                int peliculaId = Integer.parseInt(peliculaStr.split(" - ")[0]);
                peliculaSeleccionada = buscarPeliculaPorId(peliculaId);
                cargarFuncionesPorPelicula(peliculaId);
            } else {
                peliculaSeleccionada = null;
                comboFunciones.removeAllItems();
                comboFunciones.addItem("-- Seleccione una función --");
                limpiarAsientos();
            }
        });

        // Listener para selección de función
        comboFunciones.addActionListener(_ -> {
            if (comboFunciones.getSelectedIndex() > 0) {
                String funcionStr = (String) comboFunciones.getSelectedItem();
                int funcionId = Integer.parseInt(funcionStr.split(" - ")[0]);
                funcionSeleccionada = buscarFuncionPorId(funcionId);
                if (funcionSeleccionada != null) {
                    precioActual = calcularPrecio(funcionSeleccionada.getSalaId());
                    lblPrecioFuncion.setText(String.format("Precio por entrada: $%.2f", precioActual));
                    actualizarPrecioTotal();
                    cargarAsientosDisponibles(funcionSeleccionada.getSalaId(), funcionId);
                }
            } else {
                funcionSeleccionada = null;
                lblPrecioFuncion.setText("Precio por entrada: $0.00");
                lblPrecioTotal.setText("Total: $0.00");
                limpiarAsientos();
            }
        });

        // Listener para selección en tabla de entradas
        tblEntradasVendidas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEntradasVendidas.getSelectedRow() != -1) {
                int fila = tblEntradasVendidas.getSelectedRow();
                txtEditIdEntrada.setText(modelEntradasVendidas.getValueAt(fila, 0).toString());
                txtEditDocumentoEntrada.setText(modelEntradasVendidas.getValueAt(fila, 1).toString());
                txtEditClienteEntrada.setText(modelEntradasVendidas.getValueAt(fila, 2).toString());
                txtEditPeliculaEntrada.setText(modelEntradasVendidas.getValueAt(fila, 3).toString());
                txtEditSalaEntrada.setText(modelEntradasVendidas.getValueAt(fila, 4).toString());
                txtEditAsientoEntrada.setText(modelEntradasVendidas.getValueAt(fila, 5).toString());
                txtEditFechaEntrada.setText(modelEntradasVendidas.getValueAt(fila, 6).toString());
                // Remover el símbolo $ del precio
                String precio = modelEntradasVendidas.getValueAt(fila, 7).toString().replace("$", "").replace(",", "");
                txtEditPrecioEntrada.setText(precio);
            }
        });

        // Listener para selección en tabla de facturas
        tblFacturas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblFacturas.getSelectedRow() != -1) {
                int fila = tblFacturas.getSelectedRow();
                txtEditIdFactura.setText(modelFacturas.getValueAt(fila, 0).toString());
                txtEditDocumentoFactura.setText(modelFacturas.getValueAt(fila, 1).toString());
                txtEditClienteFactura.setText(modelFacturas.getValueAt(fila, 2).toString());
                // Remover el símbolo $ del valor
                String valor = modelFacturas.getValueAt(fila, 3).toString().replace("$", "").replace(",", "");
                txtEditValorFactura.setText(valor);
                txtEditFechaFactura.setText(modelFacturas.getValueAt(fila, 4).toString());
                txtEditEmpresaFactura.setText(modelFacturas.getValueAt(fila, 5).toString());
            }
        });
    }

    // ---------- MÉTODOS PARA CARGAR DATOS ----------

    private void cargarPeliculas() {
        try {
            comboPeliculas.removeAllItems();
            comboPeliculas.addItem("-- Seleccione una película --");
            
            List<Pelicula> peliculas = peliculaCtrl.listar();
            for (Pelicula p : peliculas) {
                comboPeliculas.addItem(p.getId() + " - " + p.getTitulo() + " (" + p.getGenero() + ")");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando películas: " + ex.getMessage());
        }
    }

    private void cargarFuncionesPorPelicula(int peliculaId) {
        try {
            comboFunciones.removeAllItems();
            comboFunciones.addItem("-- Seleccione una función --");
            
            List<Funcion> funciones = funcionCtrl.listar();
            for (Funcion f : funciones) {
                if (f.getPeliculaId() == peliculaId) {
                    // Verificar si hay asientos disponibles
                    if (tieneAsientosDisponibles(f.getId(), f.getSalaId())) {
                        Sala sala = buscarSalaPorId(f.getSalaId());
                        String fechaStr = f.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                        comboFunciones.addItem(f.getId() + " - " + fechaStr + " - Sala: " + sala.getTipoSala());
                    }
                }
            }
            
            if (comboFunciones.getItemCount() == 1) {
                comboFunciones.addItem("-- No hay funciones disponibles --");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando funciones: " + ex.getMessage());
        }
    }

    private void cargarAsientosDisponibles(int salaId, int funcionId) {
        panelAsientos.removeAll();
        asientosSeleccionados.clear();
        cantidadAsientosSeleccionados = 0;
        
        try {
            List<Asiento> asientos = asientoDAO.listarPorSala(salaId);
            List<Entrada> entradasVendidas = entradaCtrl.listarPorFuncion(funcionId);
            
            // Configuración de la sala
            int asientosPorFila = 10;
            int totalFilas = (int) Math.ceil((double) asientos.size() / asientosPorFila);
            
            // Panel principal con diseño organizado
            JPanel panelPrincipal = new JPanel(new BorderLayout());
            
            // SOLO UNA PANTALLA - Centrada
            JPanel panelPantalla = new JPanel();
            panelPantalla.setBackground(Color.DARK_GRAY);
            panelPantalla.setPreferredSize(new Dimension(600, 25));
            panelPantalla.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));
            JLabel lblPantalla = new JLabel("P A N T A L L A", JLabel.CENTER);
            lblPantalla.setForeground(Color.WHITE);
            lblPantalla.setFont(new Font("Arial", Font.BOLD, 14));
            panelPantalla.add(lblPantalla);
            
            // Panel para los asientos con GridLayout organizado
            JPanel panelAsientosGrid = new JPanel(new GridLayout(totalFilas, asientosPorFila + 3, 5, 5));
            
            // Crear letras para las filas
            char letraFila = 'A';
            
            for (int fila = 0; fila < totalFilas; fila++) {
                // Agregar letra de fila a la izquierda
                JLabel lblFila = new JLabel(String.valueOf(letraFila++), JLabel.CENTER);
                lblFila.setFont(new Font("Arial", Font.BOLD, 12));
                lblFila.setForeground(Color.BLUE);
                panelAsientosGrid.add(lblFila);
                
                for (int columna = 0; columna < asientosPorFila; columna++) {
                    int indexAsiento = fila * asientosPorFila + columna;
                    if (indexAsiento >= asientos.size()) {
                        // Espacio vacío si no hay más asientos
                        panelAsientosGrid.add(new JLabel());
                        continue;
                    }
                    
                    Asiento asiento = asientos.get(indexAsiento);
                    boolean ocupado = entradasVendidas.stream()
                        .anyMatch(e -> e.getAsientoId() == asiento.getId());
                    
                    // Crear pasillo central después de la columna 4
                    if (columna == 4) {
                        JLabel pasillo = new JLabel("│", JLabel.CENTER);
                        pasillo.setForeground(Color.GRAY);
                        pasillo.setFont(new Font("Arial", Font.BOLD, 16));
                        panelAsientosGrid.add(pasillo);
                    }
                    
                    if (ocupado) {
                        // Asiento ocupado con icono - SIN FONDO DE COLOR
                        JButton btnOcupado = new JButton();
                        if (iconoSillaOcupada != null) {
                            btnOcupado.setIcon(iconoSillaOcupada);
                            btnOcupado.setText(""); // Asegurar que no tenga texto
                        } else {
                            btnOcupado.setText("🔴");
                            btnOcupado.setFont(new Font("Arial", Font.BOLD, 12));
                        }
                        btnOcupado.setBackground(null); // Sin color de fondo
                        btnOcupado.setOpaque(false); // Hacer transparente
                        btnOcupado.setBorderPainted(false); // Sin borde
                        btnOcupado.setContentAreaFilled(false); // Sin área de contenido rellena
                        btnOcupado.setEnabled(false);
                        btnOcupado.setToolTipText("Asiento " + asiento.getNumeroSilla() + " - OCUPADO");
                        btnOcupado.setPreferredSize(new Dimension(35, 35));
                        panelAsientosGrid.add(btnOcupado);
                    } else {
                        // Asiento disponible con icono - SIN FONDO DE COLOR
                        JCheckBox checkAsiento = new JCheckBox();
                        if (iconoSillaDisponible != null) {
                            checkAsiento.setIcon(iconoSillaDisponible);
                            checkAsiento.setText(""); // Asegurar que no tenga texto
                        } else {
                            checkAsiento.setText("🟢");
                            checkAsiento.setFont(new Font("Arial", Font.BOLD, 12));
                        }
                        checkAsiento.setBackground(null); // Sin color de fondo
                        checkAsiento.setOpaque(false); // Hacer transparente
                        checkAsiento.setBorderPainted(false); // Sin borde
                        checkAsiento.setContentAreaFilled(false); // Sin área de contenido rellena
                        checkAsiento.setActionCommand(String.valueOf(asiento.getId()));
                        checkAsiento.setToolTipText("Asiento " + asiento.getNumeroSilla() + " - DISPONIBLE");
                        checkAsiento.setPreferredSize(new Dimension(35, 35));
                        
                        // Listener para actualizar precio total cuando se selecciona/deselecciona
                        checkAsiento.addActionListener(e -> {
                            if (checkAsiento.isSelected()) {
                                cantidadAsientosSeleccionados++;
                                // Cambiar a icono de silla seleccionada
                                if (iconoSillaSeleccionada != null) {
                                    checkAsiento.setIcon(iconoSillaSeleccionada);
                                    checkAsiento.setText("");
                                } else {
                                    checkAsiento.setText("⚫");
                                    checkAsiento.setFont(new Font("Arial", Font.BOLD, 12));
                                }
                                // Solo borde azul cuando está seleccionado
                                checkAsiento.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                                asientosSeleccionados.add(checkAsiento);
                            } else {
                                cantidadAsientosSeleccionados--;
                                // Volver a icono de silla disponible
                                if (iconoSillaDisponible != null) {
                                    checkAsiento.setIcon(iconoSillaDisponible);
                                    checkAsiento.setText("");
                                } else {
                                    checkAsiento.setText("🟢");
                                    checkAsiento.setFont(new Font("Arial", Font.BOLD, 12));
                                }
                                // Quitar el borde cuando se deselecciona
                                checkAsiento.setBorder(null);
                                asientosSeleccionados.remove(checkAsiento);
                            }
                            actualizarPrecioTotal();
                        });
                        
                        panelAsientosGrid.add(checkAsiento);
                    }
                }
                
                // Agregar separador al final de cada fila
                JLabel separador = new JLabel("│", JLabel.CENTER);
                separador.setForeground(Color.GRAY);
                separador.setFont(new Font("Arial", Font.BOLD, 16));
                panelAsientosGrid.add(separador);
            }
            
            // Centrar la pantalla
            JPanel panelPantallaCentrada = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panelPantallaCentrada.add(panelPantalla);
            
            panelPrincipal.add(panelPantallaCentrada, BorderLayout.NORTH);
            panelPrincipal.add(panelAsientosGrid, BorderLayout.CENTER);
            
            // Agregar leyenda
            JPanel panelLeyenda = new JPanel(new FlowLayout());
            panelLeyenda.add(new JLabel("Leyenda: "));
            
            JButton btnLeyendaVerde = new JButton("Disponible");
            if (iconoSillaDisponible != null) {
                btnLeyendaVerde.setIcon(iconoSillaDisponible);
                btnLeyendaVerde.setText("");
            } else {
                btnLeyendaVerde.setText("🟢");
            }
            btnLeyendaVerde.setBackground(null);
            btnLeyendaVerde.setOpaque(false);
            btnLeyendaVerde.setBorderPainted(false);
            btnLeyendaVerde.setContentAreaFilled(false);
            btnLeyendaVerde.setEnabled(false);
            
            JButton btnLeyendaNegro = new JButton("Seleccionado");
            if (iconoSillaSeleccionada != null) {
                btnLeyendaNegro.setIcon(iconoSillaSeleccionada);
                btnLeyendaNegro.setText("");
            } else {
                btnLeyendaNegro.setText("⚫");
            }
            btnLeyendaNegro.setBackground(null);
            btnLeyendaNegro.setOpaque(false);
            btnLeyendaNegro.setBorderPainted(false);
            btnLeyendaNegro.setContentAreaFilled(false);
            btnLeyendaNegro.setEnabled(false);
            
            JButton btnLeyendaRojo = new JButton("Ocupado");
            if (iconoSillaOcupada != null) {
                btnLeyendaRojo.setIcon(iconoSillaOcupada);
                btnLeyendaRojo.setText("");
            } else {
                btnLeyendaRojo.setText("🔴");
            }
            btnLeyendaRojo.setBackground(null);
            btnLeyendaRojo.setOpaque(false);
            btnLeyendaRojo.setBorderPainted(false);
            btnLeyendaRojo.setContentAreaFilled(false);
            btnLeyendaRojo.setEnabled(false);
            
            panelLeyenda.add(btnLeyendaVerde);
            panelLeyenda.add(btnLeyendaNegro);
            panelLeyenda.add(btnLeyendaRojo);
            
            panelPrincipal.add(panelLeyenda, BorderLayout.SOUTH);
            
            // Limpiar y agregar el nuevo contenido
            panelAsientos.removeAll();
            panelAsientos.setLayout(new BorderLayout());
            panelAsientos.add(panelPrincipal, BorderLayout.CENTER);
            panelAsientos.revalidate();
            panelAsientos.repaint();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando asientos: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void cargarEntradasVendidas() {
        try {
            modelEntradasVendidas.setRowCount(0);
            
            List<Entrada> entradas = entradaCtrl.listar();
            for (Entrada entrada : entradas) {
                Cliente cliente = buscarClientePorDocumento(entrada.getClienteDocumento());
                Funcion funcion = buscarFuncionPorId(entrada.getFuncionId());
                Pelicula pelicula = buscarPeliculaPorId(funcion.getPeliculaId());
                Sala sala = buscarSalaPorId(funcion.getSalaId());
                Asiento asiento = buscarAsientoPorId(entrada.getAsientoId());
                
                // Usar la fecha y hora de la función en lugar de la fecha de venta
                modelEntradasVendidas.addRow(new Object[] {
                    entrada.getId(),
                    entrada.getClienteDocumento(),
                    cliente != null ? cliente.getNombre() : "N/A",
                    pelicula != null ? pelicula.getTitulo() : "N/A",
                    sala != null ? sala.getTipoSala() : "N/A",
                    asiento != null ? asiento.getNumeroSilla() : "N/A",
                    funcion.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    String.format("$%.2f", entrada.getValor())
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando entradas: " + ex.getMessage());
        }
    }

    private void cargarFacturas() {
        try {
            modelFacturas.setRowCount(0);
            
            List<Factura> facturas = facturaCtrl.listar();
            for (Factura factura : facturas) {
                Cliente cliente = buscarClientePorDocumento(factura.getClienteDocumento());
                
                // Verificar si la fecha es nula antes de formatear
                String fechaStr = "N/A";
                if (factura.getFecha() != null) {
                    // Usar la fecha y hora de cuando se vendió la entrada (fecha de la factura)
                    fechaStr = factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
                }
                
                modelFacturas.addRow(new Object[] {
                    factura.getId(),
                    factura.getClienteDocumento(),
                    cliente != null ? cliente.getNombre() : "N/A",
                    String.format("$%.2f", factura.getValorTotal()),
                    fechaStr,
                    factura.getDatosEmpresa()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando facturas: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS DE NEGOCIO ----------

    private void venderEntradaYFactura() {
        try {
            // Validaciones
            if (!validarDatosCliente() || !validarSeleccionFuncion() || !validarAsientosSeleccionados()) {
                return;
            }
            
            // Obtener datos
            int documento = Integer.parseInt(txtDocumentoCliente.getText().trim());
            String nombre = txtNombreCliente.getText().trim();
            String telefono = txtTelefonoCliente.getText().trim();
            
            // Crear o actualizar cliente
            Cliente clienteExistente = buscarClientePorDocumento(documento);
            if (clienteExistente == null) {
                clienteCtrl.insertar(documento, nombre, telefono);
            } else {
                clienteCtrl.actualizar(documento, nombre, telefono);
            }
            
            // Vender entradas para cada asiento seleccionado
            List<Integer> entradasIds = new ArrayList<>();
            double valorTotal = 0;
            
            for (JCheckBox checkAsiento : asientosSeleccionados) {
                int asientoId = Integer.parseInt(checkAsiento.getActionCommand());
                int entradaId = entradaCtrl.crearEntrada(documento, funcionSeleccionada.getId(), asientoId, precioActual);
                entradasIds.add(entradaId);
                valorTotal += precioActual;
            }
            
            // Generar factura automáticamente con fecha actual
            String datosEmpresa = "CINEMAX COLOMBIA - NIT: 800.123.456-1 - Tel: 601-1234567";
            
            // Usar fecha actual explícitamente para la factura
            LocalDateTime fechaActual = LocalDateTime.now();
            int facturaId = facturaCtrl.crearFactura(documento, valorTotal, datosEmpresa);
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("¡Venta completada exitosamente!\n")
                   .append("Número de factura: ").append(facturaId).append("\n")
                   .append("Entradas vendidas: ").append(entradasIds.size()).append("\n")
                   .append("Asientos: ");
            
            for (int i = 0; i < asientosSeleccionados.size(); i++) {
                JCheckBox checkAsiento = asientosSeleccionados.get(i);
                Asiento asiento = buscarAsientoPorId(Integer.parseInt(checkAsiento.getActionCommand()));
                if (asiento != null) {
                    mensaje.append(asiento.getNumeroSilla());
                    if (i < asientosSeleccionados.size() - 1) {
                        mensaje.append(", ");
                    }
                }
            }
            
            mensaje.append("\nTotal: $").append(String.format("%.2f", valorTotal))
                   .append("\nFecha de la función: ").append(funcionSeleccionada.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                   .append("\nFecha de venta: ").append(fechaActual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            JOptionPane.showMessageDialog(this, mensaje.toString());
            
            // Actualizar interfaz
            cargarAsientosDisponibles(funcionSeleccionada.getSalaId(), funcionSeleccionada.getId());
            cargarEntradasVendidas();
            cargarFacturas();
            limpiarCamposCliente();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la venta: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void imprimirTicket() {
        int fila = tblEntradasVendidas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una entrada para imprimir");
            return;
        }
        
        try {
            int entradaId = (int) modelEntradasVendidas.getValueAt(fila, 0);
            Entrada entrada = buscarEntradaPorId(entradaId);
            Cliente cliente = buscarClientePorDocumento(entrada.getClienteDocumento());
            Funcion funcion = buscarFuncionPorId(entrada.getFuncionId());
            Pelicula pelicula = buscarPeliculaPorId(funcion.getPeliculaId());
            Sala sala = buscarSalaPorId(funcion.getSalaId());
            Asiento asiento = buscarAsientoPorId(entrada.getAsientoId());
            
            String ticket = "=== TICKET DE CINE ===\n\n" +
                           "Entrada #: " + entradaId + "\n" +
                           "Cliente: " + cliente.getNombre() + " (" + cliente.getDocumento() + ")\n" +
                           "Película: " + pelicula.getTitulo() + "\n" +
                           "Sala: " + sala.getTipoSala() + "\n" +
                           "Asiento: " + asiento.getNumeroSilla() + "\n" +
                           "Función: " + funcion.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                           "Precio: $" + String.format("%.2f", entrada.getValor()) + "\n\n" +
                           "¡Disfrute de la función!";
            
            JOptionPane.showMessageDialog(this, ticket, "Ticket de Entrada", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error imprimiendo ticket: " + ex.getMessage());
        }
    }

    private void imprimirFactura() {
        int fila = tblFacturas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura para imprimir");
            return;
        }
        
        try {
            int facturaId = (int) modelFacturas.getValueAt(fila, 0);
            Factura factura = buscarFacturaPorId(facturaId);
            Cliente cliente = buscarClientePorDocumento(factura.getClienteDocumento());
            
            // Buscar las entradas asociadas a esta factura por documento del cliente
            List<Entrada> entradas = buscarEntradasPorCliente(factura.getClienteDocumento());
            
            // Manejar fecha nula
            String fechaFactura = "N/A";
            if (factura.getFecha() != null) {
                fechaFactura = factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            
            StringBuilder facturaStr = new StringBuilder();
            facturaStr.append("=== FACTURA ===\n\n")
                      .append("Factura #: ").append(facturaId).append("\n")
                      .append("Fecha de venta: ").append(fechaFactura).append("\n")
                      .append("Cliente: ").append(cliente.getNombre()).append("\n")
                      .append("Documento: ").append(cliente.getDocumento()).append("\n")
                      .append("Teléfono: ").append(cliente.getTelefono()).append("\n\n")
                      .append("DETALLES:\n");
            
            for (Entrada entrada : entradas) {
                Funcion funcion = buscarFuncionPorId(entrada.getFuncionId());
                Pelicula pelicula = buscarPeliculaPorId(funcion.getPeliculaId());
                Sala sala = buscarSalaPorId(funcion.getSalaId());
                Asiento asiento = buscarAsientoPorId(entrada.getAsientoId());
                
                facturaStr.append("- ").append(pelicula.getTitulo())
                          .append(" | Sala: ").append(sala.getTipoSala())
                          .append(" | Asiento: ").append(asiento.getNumeroSilla())
                          .append(" | Función: ").append(funcion.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                          .append(" | $").append(String.format("%.2f", entrada.getValor())).append("\n");
            }
            
            facturaStr.append("\nSUBTOTAL: $").append(String.format("%.2f", factura.getValorTotal()))
                      .append("\nIVA (19%): $").append(String.format("%.2f", factura.getValorTotal() * 0.19))
                      .append("\nTOTAL: $").append(String.format("%.2f", factura.getValorTotal() * 1.19))
                      .append("\n\n").append(factura.getDatosEmpresa())
                      .append("\n¡Gracias por su compra!");
            
            JOptionPane.showMessageDialog(this, facturaStr.toString(), "Factura", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error imprimiendo factura: " + ex.getMessage());
        }
    }

    // ---------- MÉTODOS CRUD CON AUTENTICACIÓN ----------

    private boolean autenticarAdministrador() {
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
            "Ingrese la contraseña de administrador:",
            passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Autenticación", 
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            return "ADMIN202#".equals(password);
        }
        return false;
    }

    private void editarEntrada() {
        if (txtEditIdEntrada.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una entrada para editar");
            return;
        }

        if (!autenticarAdministrador()) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta o cancelada");
            return;
        }

        try {
            int entradaId = Integer.parseInt(txtEditIdEntrada.getText());
            // Usar parseDouble directamente y manejar el formato correcto
            double nuevoValor = Double.parseDouble(txtEditPrecioEntrada.getText().replace(",", "."));
            
            // Obtener la entrada actual para mantener los otros datos
            Entrada entradaActual = buscarEntradaPorId(entradaId);
            if (entradaActual != null) {
                entradaCtrl.actualizar(entradaId, entradaActual.getClienteDocumento(), 
                    entradaActual.getFuncionId(), entradaActual.getAsientoId(), nuevoValor);
                JOptionPane.showMessageDialog(this, "Entrada actualizada correctamente");
                cargarEntradasVendidas();
                limpiarCamposEdicionEntrada();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error en formato de precio: Use punto decimal (ej: 15000.00)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error editando entrada: " + ex.getMessage());
        }
    }

    private void eliminarEntrada() {
        if (txtEditIdEntrada.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una entrada para eliminar");
            return;
        }

        if (!autenticarAdministrador()) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
            return;
        }

        try {
            int entradaId = Integer.parseInt(txtEditIdEntrada.getText());
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar esta entrada?", "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                entradaCtrl.eliminar(entradaId);
                JOptionPane.showMessageDialog(this, "Entrada eliminada correctamente");
                cargarEntradasVendidas();
                limpiarCamposEdicionEntrada();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando entrada: " + ex.getMessage());
        }
    }

    private void editarFactura() {
        if (txtEditIdFactura.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura para editar");
            return;
        }

        if (!autenticarAdministrador()) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta o cancelada");
            return;
        }

        try {
            int facturaId = Integer.parseInt(txtEditIdFactura.getText());
            // Usar parseDouble directamente y manejar el formato correcto
            double nuevoValor = Double.parseDouble(txtEditValorFactura.getText().replace(",", "."));
            String nuevaEmpresa = txtEditEmpresaFactura.getText();
            
            // Obtener la factura actual para mantener los otros datos
            Factura facturaActual = buscarFacturaPorId(facturaId);
            if (facturaActual != null) {
                facturaCtrl.actualizar(facturaId, facturaActual.getClienteDocumento(), 
                    nuevoValor, nuevaEmpresa);
                JOptionPane.showMessageDialog(this, "Factura actualizada correctamente");
                cargarFacturas();
                limpiarCamposEdicionFactura();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error en formato de valor: Use punto decimal (ej: 15000.00)");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error editando factura: " + ex.getMessage());
        }
    }

    private void eliminarFactura() {
        if (txtEditIdFactura.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura para eliminar");
            return;
        }

        if (!autenticarAdministrador()) {
            JOptionPane.showMessageDialog(this, "Contraseña incorrecta");
            return;
        }

        try {
            int facturaId = Integer.parseInt(txtEditIdFactura.getText());
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Está seguro de eliminar esta factura?", "Confirmar eliminación", 
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                facturaCtrl.eliminar(facturaId);
                JOptionPane.showMessageDialog(this, "Factura eliminada correctamente");
                cargarFacturas();
                limpiarCamposEdicionFactura();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error eliminando factura: " + ex.getMessage());
        }
    }

    private void limpiarCamposEdicionEntrada() {
        txtEditIdEntrada.setText("");
        txtEditDocumentoEntrada.setText("");
        txtEditClienteEntrada.setText("");
        txtEditPeliculaEntrada.setText("");
        txtEditSalaEntrada.setText("");
        txtEditAsientoEntrada.setText("");
        txtEditFechaEntrada.setText("");
        txtEditPrecioEntrada.setText("");
    }

    private void limpiarCamposEdicionFactura() {
        txtEditIdFactura.setText("");
        txtEditDocumentoFactura.setText("");
        txtEditClienteFactura.setText("");
        txtEditValorFactura.setText("");
        txtEditFechaFactura.setText("");
        txtEditEmpresaFactura.setText("");
    }

    // ---------- MÉTODOS AUXILIARES ----------

    private boolean validarDatosCliente() {
        if (txtDocumentoCliente.getText().trim().isEmpty() ||
            txtNombreCliente.getText().trim().isEmpty() ||
            txtTelefonoCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los datos del cliente son obligatorios");
            return false;
        }
        
        try {
            Integer.parseInt(txtDocumentoCliente.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El documento debe ser numérico");
            return false;
        }
        
        return true;
    }

    private boolean validarSeleccionFuncion() {
        if (funcionSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una función");
            return false;
        }
        return true;
    }

    private boolean validarAsientosSeleccionados() {
        if (asientosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar al menos un asiento");
            return false;
        }
        return true;
    }

    private boolean tieneAsientosDisponibles(int funcionId, int salaId) {
        try {
            List<Asiento> asientos = asientoDAO.listarPorSala(salaId);
            List<Entrada> entradas = entradaCtrl.listarPorFuncion(funcionId);
            
            int asientosOcupados = entradas.size();
            int capacidadSala = asientos.size();
            
            return asientosOcupados < capacidadSala;
        } catch (Exception e) {
            return false;
        }
    }

    private double calcularPrecio(int salaId) {
        try {
            Sala sala = buscarSalaPorId(salaId);
            if (sala != null) {
                // Precios diferentes según tipo de sala
                switch (sala.getTipoSala().toLowerCase()) {
                    case "premium": return 25000.0;
                    case "vip": return 20000.0;
                    case "3d": return 18000.0;
                    default: return 15000.0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 15000.0; // Precio por defecto
    }

    private void actualizarPrecioTotal() {
        double total = cantidadAsientosSeleccionados * precioActual;
        lblPrecioTotal.setText(String.format("Total: $%.2f", total));
    }

    private void limpiarAsientos() {
        panelAsientos.removeAll();
        asientosSeleccionados.clear();
        cantidadAsientosSeleccionados = 0;
        panelAsientos.revalidate();
        panelAsientos.repaint();
    }

    private void limpiarCamposCliente() {
        txtDocumentoCliente.setText("");
        txtNombreCliente.setText("");
        txtTelefonoCliente.setText("");
    }

    private void limpiarCampos() {
        comboPeliculas.setSelectedIndex(0);
        comboFunciones.setSelectedIndex(0);
        limpiarAsientos();
        limpiarCamposCliente();
        lblPrecioFuncion.setText("Precio por entrada: $0.00");
        lblPrecioTotal.setText("Total: $0.00");
        peliculaSeleccionada = null;
        funcionSeleccionada = null;
    }

    // ---------- MÉTODOS DE BÚSQUEDA ----------

    private Pelicula buscarPeliculaPorId(int id) {
        try {
            List<Pelicula> peliculas = peliculaCtrl.listar();
            return peliculas.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Funcion buscarFuncionPorId(int id) {
        try {
            List<Funcion> funciones = funcionCtrl.listar();
            return funciones.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Sala buscarSalaPorId(int id) {
        try {
            List<Sala> salas = salaDAO.listar();
            return salas.stream().filter(s -> s.getId() == id).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Asiento buscarAsientoPorId(int id) {
        try {
            List<Asiento> asientos = asientoDAO.listar();
            return asientos.stream().filter(a -> a.getId() == id).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Cliente buscarClientePorDocumento(int documento) {
        try {
            List<Cliente> clientes = clienteCtrl.listar();
            return clientes.stream().filter(c -> c.getDocumento() == documento).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Entrada buscarEntradaPorId(int id) {
        try {
            List<Entrada> entradas = entradaCtrl.listar();
            return entradas.stream().filter(e -> e.getId() == id).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private Factura buscarFacturaPorId(int id) {
        try {
            List<Factura> facturas = facturaCtrl.listar();
            return facturas.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Entrada> buscarEntradasPorCliente(int documento) {
        try {
            List<Entrada> entradas = entradaCtrl.listar();
            return entradas.stream()
                .filter(e -> e.getClienteDocumento() == documento)
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}