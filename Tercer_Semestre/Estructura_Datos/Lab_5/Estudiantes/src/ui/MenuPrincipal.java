package ui;

import model.Estudiante;
import model.Profesor;
import service.GestionEstudiantes;
import service.GestionProfesores;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

public class MenuPrincipal extends JFrame {
    private GestionEstudiantes gestionEstudiantes = new GestionEstudiantes();
    private GestionProfesores gestionProfesores = new GestionProfesores();

    private DefaultTableModel modeloTablaEstudiantes;
    private DefaultTableModel modeloTablaProfesores;
    private JTable tablaEstudiantes;
    private JTable tablaProfesores;

    private String[] nombresEjemplo = {
        "Juan", "María", "Andrés", "Luisa", "Camilo", "Valentina", "Sebastián", "Daniela",
        "Mateo", "Sara", "Tomás", "Laura", "Nicolás", "Isabella", "Santiago", "Ana", 
        "Felipe", "Carolina", "Emilio", "Juliana"
    };
    private String[] apellidosEjemplo = {
        "García", "Rodríguez", "Martínez", "López", "Hernández", "Gómez", "Díaz", "Pérez",
        "Ramírez", "Torres", "Sánchez", "Moreno", "Jiménez", "Rojas", "Castro", "Ortiz",
        "Vargas", "Guerrero", "Suárez", "Navarro"
    };
    private String[] direccionesEjemplo = {
        "Calle 45 #12-30 Bogotá", "Carrera 7 #85-20 Bogotá", "Transversal 93 #14-50 Medellín",
        "Avenida 1 #5-12 Cali", "Calle 100 #13A-25 Bogotá", "Carrera 70 #44-25 Barranquilla",
        "Diagonal 45 #32-11 Bucaramanga", "Calle 50 Sur #20-80 Envigado", 
        "Carrera 8 #9-24 Manizales", "Calle 30 #8-30 Pereira", "Carrera 15 #34-70 Cúcuta",
        "Avenida Las Vegas #50-55 Medellín", "Calle 10 #3-25 Tunja", 
        "Carrera 33 #52-60 Cartagena", "Transversal 54 #33-21 Neiva",
        "Calle 40 #20-20 Villavicencio", "Carrera 4 #45-17 Pasto",
        "Calle 60 #24-30 Ibagué", "Avenida El Dorado #99-00 Bogotá",
        "Carrera 12 #65-33 Montería"
    };
    private String[] telefonosEjemplo = {
        "3001234567", "3017654321", "3022345678", "3038765432", "3041122334",
        "3059988776", "3064433221", "3075566778", "3086677889", "3099988776",
        "3102345678", "3118765432", "3121122334", "3134455667", "3147788990",
        "3155566778", "3161239876", "3173344556", "3189988776", "3192233445"
    };
    private String[] carrerasEjemplo = {
        "Ingeniería de Sistemas", "Ingeniería Electrónica", "Ingeniería Eléctrica",
        "Ingeniería Mecánica", "Ingeniería de Software", "Ingeniería en Telecomunicaciones",
        "Ingeniería en Computación", "Ingeniería en Automatización", "Ingeniería Mecatrónica",
        "Ingeniería Matemática", "Ingeniería Física", "Ingeniería de Control",
        "Ingeniería Informática", "Ingeniería en Robótica", "Ingeniería Telemática",
        "Ingeniería de Datos", "Ingeniería en Inteligencia Artificial", "Ingeniería en Ciberseguridad"
    };
    private String[] cursosEjemplo = {
        "Cálculo Diferencial", "Cálculo Integral", "Física I", "Física II", "Química General",
        "Álgebra Lineal", "Ecuaciones Diferenciales", "Programación I", "Estructuras de Datos",
        "Bases de Datos", "Circuitos Eléctricos", "Electrónica Digital", "Sistemas Operativos",
        "Arquitectura de Computadores", "Redes de Computadoras", "Ingeniería de Software",
        "Matemáticas Discretas", "Simulación", "Probabilidad y Estadística", "Diseño de Algoritmos"
    };

    public MenuPrincipal() {
        setTitle("Gestión de Estudiantes y Profesores");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon icono = new ImageIcon("Estudiantes/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel de Estudiantes
        JPanel panelEstudiantes = new JPanel(new BorderLayout());
        modeloTablaEstudiantes = new DefaultTableModel(new String[]{"Código", "Nombre", "Apellido", "Carrera", "Notas"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tablaEstudiantes = new JTable(modeloTablaEstudiantes);
        panelEstudiantes.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JPanel panelBotonesEst = new JPanel();
        JButton btnAgregarEstudiante = new JButton("Agregar Estudiante");
        JButton btnEstudianteRandom = new JButton("Estudiante Aleatorio");
        panelBotonesEst.add(btnAgregarEstudiante);
        panelBotonesEst.add(btnEstudianteRandom);
        panelEstudiantes.add(panelBotonesEst, BorderLayout.SOUTH);
        tabbedPane.addTab("Estudiantes", panelEstudiantes);

        // Panel de Profesores
        JPanel panelProfesores = new JPanel(new BorderLayout());
        modeloTablaProfesores = new DefaultTableModel(new String[]{"Código", "Nombre", "Apellido", "Carrera", "Cursos"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return column != 0;
            }
        };
        tablaProfesores = new JTable(modeloTablaProfesores);
        panelProfesores.add(new JScrollPane(tablaProfesores), BorderLayout.CENTER);

        JPanel panelBotonesProf = new JPanel();
        JButton btnAgregarProfesor = new JButton("Agregar Profesor");
        JButton btnProfesorRandom = new JButton("Profesor Aleatorio");
        panelBotonesProf.add(btnAgregarProfesor);
        panelBotonesProf.add(btnProfesorRandom);
        panelProfesores.add(panelBotonesProf, BorderLayout.SOUTH);
        tabbedPane.addTab("Profesores", panelProfesores);

        // Botones para eliminar todo
        JButton btnEliminarEstudiantes = new JButton("Eliminar Todos los Estudiantes");
        JButton btnEliminarProfesores = new JButton("Eliminar Todos los Profesores");
        JPanel panelEliminarTodo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelEliminarTodo.add(btnEliminarEstudiantes);
        panelEliminarTodo.add(btnEliminarProfesores);
        add(panelEliminarTodo, BorderLayout.SOUTH);
        add(tabbedPane, BorderLayout.CENTER);

        // Listeners para edición en tabla
        modeloTablaEstudiantes.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                String codigo = (String) modeloTablaEstudiantes.getValueAt(row, 0);
                Estudiante est = gestionEstudiantes.consultar(codigo);
                if (est != null) {
                    est.setNombres((String) modeloTablaEstudiantes.getValueAt(row, 1));
                    est.setApellidos((String) modeloTablaEstudiantes.getValueAt(row, 2));
                    est.setCarrera((String) modeloTablaEstudiantes.getValueAt(row, 3));
                }
            }
        });

        modeloTablaProfesores.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                int row = e.getFirstRow();
                String codigo = (String) modeloTablaProfesores.getValueAt(row, 0);
                Profesor prof = gestionProfesores.consultar(codigo);
                if (prof != null) {
                    prof.setNombres((String) modeloTablaProfesores.getValueAt(row, 1));
                    prof.setApellidos((String) modeloTablaProfesores.getValueAt(row, 2));
                    prof.setCarrera((String) modeloTablaProfesores.getValueAt(row, 3));
                }
            }
        });

        // Acciones botones
        btnAgregarEstudiante.addActionListener(e -> agregarEstudianteManual());
        btnEstudianteRandom.addActionListener(e -> agregarEstudianteAleatorio());
        btnAgregarProfesor.addActionListener(e -> agregarProfesorManual());
        btnProfesorRandom.addActionListener(e -> agregarProfesorAleatorio());

        btnEliminarEstudiantes.addActionListener(e -> {
            gestionEstudiantes.obtenerTodos().clear();
            modeloTablaEstudiantes.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Todos los estudiantes han sido eliminados.");
        });

        btnEliminarProfesores.addActionListener(e -> {
            gestionProfesores.obtenerTodos().clear();
            modeloTablaProfesores.setRowCount(0);
            JOptionPane.showMessageDialog(this, "Todos los profesores han sido eliminados.");
        });


        setVisible(true);
    }

    private void agregarEstudianteManual() {
        String codigo = generarCodigo();
        String nombre = JOptionPane.showInputDialog("Nombre:");
        if (nombre == null || nombre.trim().isEmpty()) return;
    
        String apellido = JOptionPane.showInputDialog("Apellido:");
        if (apellido == null || apellido.trim().isEmpty()) return;
    
        String direccion = JOptionPane.showInputDialog("Dirección:");
        if (direccion == null || direccion.trim().isEmpty()) return;
    
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        if (telefono == null || telefono.trim().isEmpty()) return;
    
        String carrera = JOptionPane.showInputDialog("Carrera:");
        if (carrera == null || carrera.trim().isEmpty()) return;
    
        String notasTexto = JOptionPane.showInputDialog("Notas (separadas por coma):");
        if (notasTexto == null || notasTexto.trim().isEmpty()) return;
    
        List<String> partes = Arrays.asList(notasTexto.split(","));
        ArrayList<Double> notas = new ArrayList<>();
        try {
            for (String notaStr : partes) {
                notas.add(Double.parseDouble(notaStr.trim()));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Formato de notas inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        Estudiante estudiante = new Estudiante(codigo, nombre, apellido, direccion, telefono, carrera, notas);
        gestionEstudiantes.insertar(codigo, estudiante);
        modeloTablaEstudiantes.addRow(new Object[]{codigo, nombre, apellido, carrera, formatearNotas(notas)});
    }
    

    private void agregarEstudianteAleatorio() {
        String codigo = generarCodigo();
        Random rand = new Random();
        String nombre = nombresEjemplo[rand.nextInt(nombresEjemplo.length)];
        String apellido = apellidosEjemplo[rand.nextInt(apellidosEjemplo.length)];
        String direccion = direccionesEjemplo[rand.nextInt(direccionesEjemplo.length)];
        String telefono = telefonosEjemplo[rand.nextInt(telefonosEjemplo.length)];
        String carrera = carrerasEjemplo[rand.nextInt(carrerasEjemplo.length)];
        int cantidadNotas = 1 + rand.nextInt(5);
        ArrayList<Double> notas = new ArrayList<>();
        for (int i = 0; i < cantidadNotas; i++) {
            notas.add(2.5 + rand.nextDouble() * 2.5);
        }

        Estudiante estudiante = new Estudiante(codigo, nombre, apellido, direccion, telefono, carrera, notas);
        gestionEstudiantes.insertar(codigo, estudiante);
        modeloTablaEstudiantes.addRow(new Object[]{codigo, nombre, apellido, carrera, formatearNotas(notas)});
    }

    private void agregarProfesorManual() {
        String codigo = generarCodigo();
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String apellido = JOptionPane.showInputDialog("Apellido:");
        String direccion = JOptionPane.showInputDialog("Dirección:");
        String telefono = JOptionPane.showInputDialog("Teléfono:");
        String carrera = JOptionPane.showInputDialog("Carrera:");
        String cursosTexto = JOptionPane.showInputDialog("Cursos (separados por coma):");
        if (cursosTexto == null) return;
        List<String> cursos = Arrays.asList(cursosTexto.split(","));

        Profesor profesor = new Profesor(nombre, apellido, direccion, telefono, carrera, new ArrayList<>(cursos));
        gestionProfesores.insertar(codigo, profesor);
        modeloTablaProfesores.addRow(new Object[]{codigo, nombre, apellido, carrera, cursos.toString()});
    }

    private void agregarProfesorAleatorio() {
        String codigo = generarCodigo();
        Random rand = new Random();
        String nombre = nombresEjemplo[rand.nextInt(nombresEjemplo.length)];
        String apellido = apellidosEjemplo[rand.nextInt(apellidosEjemplo.length)];
        String direccion = direccionesEjemplo[rand.nextInt(direccionesEjemplo.length)];
        String telefono = telefonosEjemplo[rand.nextInt(telefonosEjemplo.length)];
        String carrera = carrerasEjemplo[rand.nextInt(carrerasEjemplo.length)];

        List<String> listaCursos = new ArrayList<>(Arrays.asList(cursosEjemplo));
        Collections.shuffle(listaCursos);
        int cantidad = 2 + rand.nextInt(3);
        List<String> cursos = listaCursos.subList(0, cantidad);

        Profesor profesor = new Profesor(nombre, apellido, direccion, telefono, carrera, new ArrayList<>(cursos));
        gestionProfesores.insertar(codigo, profesor);
        modeloTablaProfesores.addRow(new Object[]{codigo, nombre, apellido, carrera, cursos});
    }

    private String formatearNotas(List<Double> notas) {
        List<String> notasFormateadas = new ArrayList<>();
        for (Double nota : notas) {
            notasFormateadas.add(String.format("%.1f", nota));
        }
        return notasFormateadas.toString();
    }

    private String generarCodigo() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuPrincipal::new);
    }
}
