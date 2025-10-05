package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
//import java.awt.event.ActionListener;
//import java.awt.event.ActionEvent;
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
    private FuncionController funcionCtrl;
    private AsientoDAO asientoDAO;
    private EntradaController entradaCtrl;
    private FacturaController facturaCtrl;
    private SalaDAO salaDAO;

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
    private ButtonGroup grupoAsientos;
    
    // ---------- COMPONENTES PARA HISTORIAL ----------
    private JTable tblEntradasVendidas;
    private DefaultTableModel modelEntradasVendidas;
    private JTable tblFacturas;
    private DefaultTableModel modelFacturas;

    // Variables para selección actual
    private Pelicula peliculaSeleccionada;
    private Funcion funcionSeleccionada;
    private double precioActual;

    // Constructor principal
    public CineFrame(Connection con) {
        this.con = con;
        // Inicializar todos los controladores con la conexión a la base de datos
        this.clienteCtrl = new ClienteController(con);
        this.peliculaCtrl = new PeliculaController(con);
        this.funcionCtrl = new FuncionController(con);
        this.asientoDAO = new AsientoDAO(con);
        this.entradaCtrl = new EntradaController(con);
        this.facturaCtrl = new FacturaController(con);
        this.salaDAO = new SalaDAO(con);

        // Configurar propiedades básicas de la ventana
        setTitle("Sistema de Cine - Venta de Entradas");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI(); // Inicializar la interfaz de usuario
    }

    // Método para inicializar todos los componentes de la interfaz de usuario
    private void initUI() {
        tabs = new JTabbedPane();

        // ---------- PESTAÑA PRINCIPAL: VENTA DE ENTRADAS ----------
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
        JPanel pInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        lblPrecioFuncion = new JLabel("Precio: $0.00");
        lblPrecioFuncion.setFont(new Font("Arial", Font.BOLD, 16));
        lblPrecioFuncion.setForeground(Color.BLUE);
        pInfo.add(lblPrecioFuncion);

        // Panel central: Asientos
        JPanel pAsientosContainer = new JPanel(new BorderLayout());
        pAsientosContainer.setBorder(BorderFactory.createTitledBorder("Selección de Asientos"));
        
        panelAsientos = new JPanel();
        panelAsientos.setLayout(new GridLayout(0, 8, 5, 5));
        grupoAsientos = new ButtonGroup();
        
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
        JButton btnVenderEntrada = new JButton("Vender Entrada");
        JButton btnGenerarFactura = new JButton("Generar Factura");
        JButton btnLimpiar = new JButton("Limpiar");

        btnVenderEntrada.addActionListener(_ -> venderEntrada());
        btnGenerarFactura.addActionListener(_ -> generarFactura());
        btnLimpiar.addActionListener(_ -> limpiarCampos());

        pBotones.add(btnVenderEntrada);
        pBotones.add(btnGenerarFactura);
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

        // ---------- PESTAÑA ENTRADAS VENDIDAS ----------
        JPanel pEntradas = new JPanel(new BorderLayout());
        pEntradas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelEntradasVendidas = new DefaultTableModel(new Object[] {
            "ID", "Documento", "Cliente", "Película", "Sala", "Asiento", "Fecha", "Precio"
        }, 0);
        tblEntradasVendidas = new JTable(modelEntradasVendidas);
        
        JPanel pBotonesEntradas = new JPanel(new FlowLayout());
        JButton btnImprimirTicket = new JButton("Imprimir Ticket");
        JButton btnActualizarEntradas = new JButton("Actualizar Lista");
        
        btnImprimirTicket.addActionListener(_ -> imprimirTicket());
        btnActualizarEntradas.addActionListener(_ -> cargarEntradasVendidas());
        
        pBotonesEntradas.add(btnImprimirTicket);
        pBotonesEntradas.add(btnActualizarEntradas);

        pEntradas.add(new JScrollPane(tblEntradasVendidas), BorderLayout.CENTER);
        pEntradas.add(pBotonesEntradas, BorderLayout.SOUTH);

        // ---------- PESTAÑA FACTURAS ----------
        JPanel pFacturas = new JPanel(new BorderLayout());
        pFacturas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        modelFacturas = new DefaultTableModel(new Object[] {
            "ID", "Documento", "Cliente", "Valor Total", "Fecha", "Empresa"
        }, 0);
        tblFacturas = new JTable(modelFacturas);
        
        JPanel pBotonesFacturas = new JPanel(new FlowLayout());
        JButton btnImprimirFactura = new JButton("Imprimir Factura");
        JButton btnActualizarFacturas = new JButton("Actualizar Lista");
        
        btnImprimirFactura.addActionListener(_ -> imprimirFactura());
        btnActualizarFacturas.addActionListener(_ -> cargarFacturas());

        pBotonesFacturas.add(btnImprimirFactura);
        pBotonesFacturas.add(btnActualizarFacturas);

        pFacturas.add(new JScrollPane(tblFacturas), BorderLayout.CENTER);
        pFacturas.add(pBotonesFacturas, BorderLayout.SOUTH);

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
                    lblPrecioFuncion.setText(String.format("Precio: $%.2f", precioActual));
                    cargarAsientosDisponibles(funcionSeleccionada.getSalaId(), funcionId);
                }
            } else {
                funcionSeleccionada = null;
                lblPrecioFuncion.setText("Precio: $0.00");
                limpiarAsientos();
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
        grupoAsientos = new ButtonGroup();
        
        try {
            List<Asiento> asientos = asientoDAO.listarPorSala(salaId);
            List<Entrada> entradasVendidas = entradaCtrl.listarPorFuncion(funcionId);
            
            for (Asiento asiento : asientos) {
                JPanel panelAsiento = new JPanel(new BorderLayout());
                panelAsiento.setPreferredSize(new Dimension(60, 60));
                
                // Verificar si el asiento está ocupado
                boolean ocupado = entradasVendidas.stream()
                    .anyMatch(e -> e.getAsientoId() == asiento.getId());
                
                if (ocupado) {
                    // Asiento ocupado - mostrar como botón deshabilitado
                    JButton btnOcupado = new JButton(asiento.getNumeroSilla());
                    btnOcupado.setBackground(Color.RED);
                    btnOcupado.setForeground(Color.WHITE);
                    btnOcupado.setEnabled(false);
                    btnOcupado.setToolTipText("Asiento ocupado");
                    panelAsiento.add(btnOcupado, BorderLayout.CENTER);
                } else {
                    // Asiento disponible - mostrar como radio button
                    JRadioButton radioAsiento = new JRadioButton(asiento.getNumeroSilla());
                    radioAsiento.setBackground(Color.GREEN);
                    radioAsiento.setActionCommand(String.valueOf(asiento.getId()));
                    grupoAsientos.add(radioAsiento);
                    panelAsiento.add(radioAsiento, BorderLayout.CENTER);
                }
                
                panelAsientos.add(panelAsiento);
            }
            
            panelAsientos.revalidate();
            panelAsientos.repaint();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error cargando asientos: " + ex.getMessage());
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
            
            // SOLUCIÓN: Verificar si la fecha es nula antes de formatear
            String fechaStr = "N/A";
            if (factura.getFecha() != null) {
                fechaStr = factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            }
            
            modelFacturas.addRow(new Object[] {
                factura.getId(),
                factura.getClienteDocumento(),
                cliente != null ? cliente.getNombre() : "N/A",
                String.format("$%.2f", factura.getValorTotal()),
                fechaStr,  // Usar la fecha formateada o "N/A"
                factura.getDatosEmpresa()
            });
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error cargando facturas: " + ex.getMessage());
    }
}

    // ---------- MÉTODOS DE NEGOCIO ----------

    private void venderEntrada() {
        try {
            // Validaciones
            if (!validarDatosCliente() || !validarSeleccionFuncion() || !validarAsientoSeleccionado()) {
                return;
            }
            
            // Obtener datos
            int documento = Integer.parseInt(txtDocumentoCliente.getText().trim());
            String nombre = txtNombreCliente.getText().trim();
            String telefono = txtTelefonoCliente.getText().trim();
            int asientoId = Integer.parseInt(grupoAsientos.getSelection().getActionCommand());
            
            // Crear o actualizar cliente
            Cliente clienteExistente = buscarClientePorDocumento(documento);
            if (clienteExistente == null) {
                clienteCtrl.insertar(documento, nombre, telefono);
            } else {
                clienteCtrl.actualizar(documento, nombre, telefono);
            }
            
            // Vender entrada
            int entradaId = entradaCtrl.crearEntrada(documento, funcionSeleccionada.getId(), asientoId, precioActual);
            
            JOptionPane.showMessageDialog(this, 
                "¡Entrada vendida exitosamente!\n" +
                "Número de entrada: " + entradaId + "\n" +
                "Asiento: " + buscarAsientoPorId(asientoId).getNumeroSilla());
            
            // Actualizar interfaz
            cargarAsientosDisponibles(funcionSeleccionada.getSalaId(), funcionSeleccionada.getId());
            cargarEntradasVendidas();
            limpiarCamposCliente();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error vendiendo entrada: " + ex.getMessage());
        }
    }

    private void generarFactura() {
        try {
            // Validaciones
            if (!validarDatosCliente() || !validarSeleccionFuncion() || !validarAsientoSeleccionado()) {
                return;
            }
            
            // Obtener datos
            int documento = Integer.parseInt(txtDocumentoCliente.getText().trim());
            String nombre = txtNombreCliente.getText().trim();
            String telefono = txtTelefonoCliente.getText().trim();
            int asientoId = Integer.parseInt(grupoAsientos.getSelection().getActionCommand());
            
            // Crear o actualizar cliente
            Cliente clienteExistente = buscarClientePorDocumento(documento);
            if (clienteExistente == null) {
                clienteCtrl.insertar(documento, nombre, telefono);
            } else {
                clienteCtrl.actualizar(documento, nombre, telefono);
            }
            
            // Vender entrada
            int entradaId = entradaCtrl.crearEntrada(documento, funcionSeleccionada.getId(), asientoId, precioActual);
            
            // Generar factura
            String datosEmpresa = "CINE XYZ - NIT: 123456789-1 - Tel: 555-0123";
            int facturaId = facturaCtrl.crearFactura(documento, precioActual, datosEmpresa);
            
            JOptionPane.showMessageDialog(this, 
                "¡Factura generada exitosamente!\n" +
                "Número de factura: " + facturaId + "\n" +
                "Número de entrada: " + entradaId);
            
            // Actualizar interfaz
            cargarAsientosDisponibles(funcionSeleccionada.getSalaId(), funcionSeleccionada.getId());
            cargarEntradasVendidas();
            cargarFacturas();
            limpiarCamposCliente();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generando factura: " + ex.getMessage());
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
            
            // Buscar la entrada asociada a esta factura
            Entrada entrada = buscarEntradaPorClienteYFecha(factura.getClienteDocumento(), factura.getFecha());
            Funcion funcion = entrada != null ? buscarFuncionPorId(entrada.getFuncionId()) : null;
            Pelicula pelicula = funcion != null ? buscarPeliculaPorId(funcion.getPeliculaId()) : null;
            Sala sala = funcion != null ? buscarSalaPorId(funcion.getSalaId()) : null;
            Asiento asiento = entrada != null ? buscarAsientoPorId(entrada.getAsientoId()) : null;
            
            String facturaStr = "=== FACTURA ===\n\n" +
                              "Factura #: " + facturaId + "\n" +
                              "Fecha: " + factura.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "\n" +
                              "Cliente: " + cliente.getNombre() + "\n" +
                              "Documento: " + cliente.getDocumento() + "\n" +
                              "Teléfono: " + cliente.getTelefono() + "\n\n" +
                              "DETALLES:\n" +
                              "Película: " + (pelicula != null ? pelicula.getTitulo() : "N/A") + "\n" +
                              "Sala: " + (sala != null ? sala.getTipoSala() : "N/A") + "\n" +
                              "Asiento: " + (asiento != null ? asiento.getNumeroSilla() : "N/A") + "\n" +
                              "Función: " + (funcion != null ? 
                                  funcion.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A") + "\n\n" +
                              "SUBTOTAL: $" + String.format("%.2f", factura.getValorTotal()) + "\n" +
                              "IVA (19%): $" + String.format("%.2f", factura.getValorTotal() * 0.19) + "\n" +
                              "TOTAL: $" + String.format("%.2f", factura.getValorTotal() * 1.19) + "\n\n" +
                              factura.getDatosEmpresa() + "\n" +
                              "¡Gracias por su compra!";
            
            JOptionPane.showMessageDialog(this, facturaStr, "Factura", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error imprimiendo factura: " + ex.getMessage());
        }
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

    private boolean validarAsientoSeleccionado() {
        if (grupoAsientos.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un asiento");
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
                // Precios base según tipo de sala
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

    private void limpiarAsientos() {
        panelAsientos.removeAll();
        grupoAsientos = new ButtonGroup();
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
        lblPrecioFuncion.setText("Precio: $0.00");
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

    private Entrada buscarEntradaPorClienteYFecha(int documento, LocalDateTime fecha) {
        try {
            List<Entrada> entradas = entradaCtrl.listar();
            return entradas.stream()
                .filter(e -> e.getClienteDocumento() == documento)
                .findFirst()
                .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }
}