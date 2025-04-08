// Paquete donde se encuentra la clase
package ui;

// Importaciones de bibliotecas Swing para la interfaz gráfica
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
// Importaciones para manejar listas y las clases del modelo y servicio
import java.util.List;
import model.CantanteFamoso;
import service.ListaCantantesFamosos;

// Clase principal que hereda de JFrame (ventana principal)
public class CantantesGUI extends JFrame {
    // Instancia del servicio para manejar la lista de cantantes
    private ListaCantantesFamosos listaService = new ListaCantantesFamosos();
    
    // Componentes para la tabla
    private DefaultTableModel tableModel;  // Modelo de datos para la tabla
    private JTable table;                 // Tabla para mostrar los cantantes
    
    // Campos de texto para entrada de datos
    private JTextField campoNombre, campoDisco, campoVentas;
    
    // Datos predefinidos de cantantes (nombre, disco, ventas)
    private final Object[][] cantantesPredefinidos = {
        {"Queen", "Greatest Hits", 25000000},
        // ... (resto de datos predefinidos) //AGREGAR EL RESTO DE CANTANTES PREDEFINIDOS
    };

    // Constructor de la ventana principal
    public CantantesGUI() {
        // Configuración básica de la ventana
        setTitle("Gestión de Cantantes Famosos");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);  // Cierra la aplicación al salir
        setLocationRelativeTo(null);  // Centra la ventana
        setLayout(new BorderLayout());  // Diseño de bordes
        
        // Configuración del icono de la ventana
        ImageIcon icono = new ImageIcon("Discografia/src/resources/icon.png");
        setIconImage(icono.getImage());

        // Panel superior para entrada de datos (GridLayout de 4 filas x 2 columnas)
        JPanel panelEntrada = new JPanel(new GridLayout(4, 2, 5, 5));
        
        // Inicialización de campos de texto
        campoNombre = new JTextField();
        campoDisco = new JTextField();
        campoVentas = new JTextField();
        
        // Añade etiquetas y campos al panel
        panelEntrada.add(new JLabel("Nombre:"));
        panelEntrada.add(campoNombre);
        panelEntrada.add(new JLabel("Disco más vendido:"));
        panelEntrada.add(campoDisco);
        panelEntrada.add(new JLabel("Total de ventas:"));
        panelEntrada.add(campoVentas);
        
        // Botón para agregar cantante
        JButton botonAgregar = new JButton("Agregar");
        botonAgregar.addActionListener(e -> agregarCantante());  // Acción al hacer clic
        panelEntrada.add(botonAgregar);
        
        // Botón para agregar cantante aleatorio
        JButton botonAleatorio = new JButton("Aleatorio");
        botonAleatorio.addActionListener(e -> agregarAleatorio());
        panelEntrada.add(botonAleatorio);
        
        // Añade el panel de entrada en la parte superior de la ventana
        add(panelEntrada, BorderLayout.NORTH);

        // Configuración de la tabla
        String[] columnas = {"Nombre", "Disco Más Vendido", "Ventas"};
        tableModel = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;  // Permite editar todas las celdas
            }
        };
        
        table = new JTable(tableModel);  // Crea la tabla con el modelo
        add(new JScrollPane(table), BorderLayout.CENTER);  // Añade con scroll

        // Listener para detectar cambios en la tabla
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    actualizarCantanteDesdeFila(e.getFirstRow());  // Actualiza al editar
                }
            }
        });

        // Panel inferior para botones adicionales
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 5));
        
        // Botón para eliminar cantante(s)
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminarCantante());
        
        // Botón para ordenar por ventas
        JButton btnOrdenar = new JButton("Top Ventas");
        btnOrdenar.addActionListener(e -> mostrarOrdenados());
        
        // Botón para mostrar todos los cantantes
        JButton btnMostrar = new JButton("Mostrar Todos");
        btnMostrar.addActionListener(e -> mostrarLista());
        
        // Añade botones al panel
        panelBotones.add(btnEliminar);
        panelBotones.add(btnOrdenar);
        panelBotones.add(btnMostrar);
        
        // Añade el panel de botones en la parte inferior
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Método para agregar un nuevo cantante desde los campos de texto
    private void agregarCantante() {
        try {
            String nombre = campoNombre.getText();
            String disco = campoDisco.getText();
            int ventas = Integer.parseInt(campoVentas.getText());  // Convierte texto a número
            
            // Crea y agrega el cantante al servicio
            listaService.agregarCantante(new CantanteFamoso(nombre, disco, ventas));
            mostrarLista();    // Actualiza la tabla
            limpiarCampos();   // Limpia los campos
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Ventas debe ser un número entero.");
        }
    }

    // Método para agregar un cantante aleatorio de los predefinidos
    private void agregarAleatorio() {
        int index = (int) (Math.random() * cantantesPredefinidos.length);  // Índice aleatorio
        
        // Obtiene datos del array predefinido
        String nombre = (String) cantantesPredefinidos[index][0];
        String disco = (String) cantantesPredefinidos[index][1];
        
        // Crea cantante con ventas aleatorias (entre 10,000 y 1,000,000)
        CantanteFamoso nuevo = new CantanteFamoso(
            nombre,
            disco,
            (int)(Math.random() * 1000000 + 10000)
        );
        
        listaService.agregarCantante(nuevo);
        mostrarLista();  // Actualiza la tabla
    }

    // Método para eliminar cantante(s)
    private void eliminarCantante() {
        // Opciones para el diálogo
        String[] opciones = {"Eliminar por nombre", "Eliminar todos"};
        
        // Muestra diálogo de selección
        int seleccion = JOptionPane.showOptionDialog(this, "¿Qué deseas eliminar?",
                "Eliminar Cantante", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]);

        if (seleccion == 0) {  // Eliminar por nombre
            String nombre = JOptionPane.showInputDialog("Nombre del cantante a eliminar:");
            if (nombre != null && !nombre.isEmpty()) {
                listaService.eliminarCantante(nombre);
            }
        } else if (seleccion == 1) {  // Eliminar todos
            int confirm = JOptionPane.showConfirmDialog(this, 
                "¿Seguro que deseas eliminar todos los cantantes?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                listaService.obtenerLista().clear();  // Limpia la lista
            }
        }
        mostrarLista();  // Actualiza la tabla
    }

    // Muestra todos los cantantes en la tabla
    private void mostrarLista() {
        actualizarTabla(listaService.obtenerLista());
    }

    // Muestra cantantes ordenados por ventas (descendente)
    private void mostrarOrdenados() {
        actualizarTabla(listaService.obtenerOrdenadosPorVentas());
    }

    // Actualiza la tabla con la lista proporcionada
    private void actualizarTabla(List<CantanteFamoso> lista) {
        tableModel.setRowCount(0);  // Limpia la tabla
        for (CantanteFamoso c : lista) {
            // Añade cada cantante como nueva fila
            tableModel.addRow(new Object[]{c.getNombre(), c.getDiscoConMasVentas(), c.getTotalDeVentas()});
        }
    }

    // Actualiza un cantante cuando se edita directamente en la tabla
    private void actualizarCantanteDesdeFila(int fila) {
        if (fila >= 0 && fila < listaService.obtenerLista().size()) {
            try {
                // Obtiene nuevos valores de la tabla
                String nuevoNombre = tableModel.getValueAt(fila, 0).toString();
                String nuevoDisco = tableModel.getValueAt(fila, 1).toString();
                int nuevasVentas = Integer.parseInt(tableModel.getValueAt(fila, 2).toString());
                
                // Actualiza el cantante correspondiente
                CantanteFamoso cantante = listaService.obtenerLista().get(fila);
                cantante.setNombre(nuevoNombre);
                cantante.setDiscoConMasVentas(nuevoDisco);
                cantante.setTotalDeVentas(nuevasVentas);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Las ventas deben ser numéricas.");
            }
        }
    }

    // Limpia los campos de texto
    private void limpiarCampos() {
        campoNombre.setText("");
        campoDisco.setText("");
        campoVentas.setText("");
    }
}
