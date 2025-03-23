package ui;

import models.Employee;
import services.EmployeeBonusService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

// Clase principal que representa la interfaz gráfica para calcular bonos de empleados
public class EmployeeBonusCalculator extends JFrame {
    // Componentes de la interfaz gráfica
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    private JButton calculateBonusesButton, generateEmployeesButton, searchButton, manualEntryButton, sortBySalaryButton;
    private JLabel highestBonusLabel, lowestBonusLabel, totalBonusLabel, bonusCalculationLabel;
    private JTextField employeeCountField, searchRutField, bonusCountField;
    private List<Employee> employees;

    // Constructor de la clase
    public EmployeeBonusCalculator() {
        setTitle("Calculadora de Bonos");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon icono = new ImageIcon("Empresa/src/resources/icon.png"); 
        setIconImage(icono.getImage());

        // Modelo de la tabla para mostrar los empleados
        tableModel = new DefaultTableModel(new String[]{"RUT", "Sueldo", "Bonificación"}, 0);
        employeeTable = new JTable(tableModel);
        add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        // Panel de controles con botones y campos de texto
        JPanel controlPanel = new JPanel(new GridLayout(3, 4, 5, 5));

        controlPanel.add(new JLabel("Cantidad de empleados:"));
        employeeCountField = new JTextField("10");
        controlPanel.add(employeeCountField);

        generateEmployeesButton = new JButton("Generar Aleatorio");
        generateEmployeesButton.addActionListener(this::generateEmployees);
        controlPanel.add(generateEmployeesButton);

        manualEntryButton = new JButton("Ingresar Manualmente");
        manualEntryButton.addActionListener(this::manualEntry);
        controlPanel.add(manualEntryButton);

        calculateBonusesButton = new JButton("Calcular Bonos");
        calculateBonusesButton.addActionListener(this::calculateBonuses);
        controlPanel.add(calculateBonusesButton);

        controlPanel.add(new JLabel("Número de Bonos a Otorgar:"));
        bonusCountField = new JTextField();
        controlPanel.add(bonusCountField);

        controlPanel.add(new JLabel("Buscar RUT:"));
        searchRutField = new JTextField();
        controlPanel.add(searchRutField);

        searchButton = new JButton("Buscar");
        searchButton.addActionListener(this::searchEmployee);
        controlPanel.add(searchButton);

        sortBySalaryButton = new JButton("Ordenar por Salario");
        sortBySalaryButton.addActionListener(this::sortBySalary);
        controlPanel.add(sortBySalaryButton);

        add(controlPanel, BorderLayout.NORTH);

        // Panel de resultados para mostrar información sobre los bonos
        JPanel resultPanel = new JPanel(new GridLayout(4, 1));
        highestBonusLabel = new JLabel("Empleado con mayor bono: ");
        lowestBonusLabel = new JLabel("Empleado con menor bono: ");
        totalBonusLabel = new JLabel("Costo total de los bonos: ");
        bonusCalculationLabel = new JLabel("Los bonos se calculan como el 5% del salario de los empleados bajo la media.");
        resultPanel.add(highestBonusLabel);
        resultPanel.add(lowestBonusLabel);
        resultPanel.add(totalBonusLabel);
        resultPanel.add(bonusCalculationLabel);

        add(resultPanel, BorderLayout.SOUTH);

        // Lista para almacenar los empleados
        employees = new ArrayList<>();
    }

    // Método para generar empleados aleatorios
    private void generateEmployees(ActionEvent e) {
        tableModel.setRowCount(0);
        employees.clear();
        int count = Integer.parseInt(employeeCountField.getText());

        Random random = new Random();
        for (int i = 0; i < count; i++) {
            int rut = 100 + random.nextInt(900);
            float salary = 1600000 + random.nextFloat() * 1400000;
            employees.add(new Employee(rut, salary));
            tableModel.addRow(new Object[]{rut, formatMoney(salary), "$0"});
        }
    }

    // Método para ingresar empleados manualmente
    private void manualEntry(ActionEvent e) {
        JTextField rutField = new JTextField();
        JTextField salaryField = new JTextField();

        Object[] message = {
            "RUT (3 dígitos):", rutField,
            "Sueldo:", salaryField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Ingresar Empleado", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int rut = Integer.parseInt(rutField.getText());
                if (rut < 100 || rut > 999) {
                    JOptionPane.showMessageDialog(this, "El RUT debe ser de 3 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                float salary = Float.parseFloat(salaryField.getText());
                if (salary < 1600000) {
                    JOptionPane.showMessageDialog(this, "El sueldo debe ser mínimo de $1,600,000.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                employees.add(new Employee(rut, salary));
                tableModel.addRow(new Object[]{rut, formatMoney(salary), "$0"});
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Método para calcular los bonos de los empleados
    private void calculateBonuses(ActionEvent e) {
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Primero ingrese o genere empleados.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        float averageSalary = EmployeeBonusService.getAverageSalary(employees);
        List<Employee> lowEarners = EmployeeBonusService.getEmployeesBelowAverage(employees, averageSalary);

        if (lowEarners.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay empleados con salario por debajo de la media.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int bonusCount;
        try {
            bonusCount = Integer.parseInt(bonusCountField.getText());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un número válido de bonos a otorgar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (bonusCount > lowEarners.size()) {
            bonusCount = lowEarners.size();
        }

        lowEarners.sort(Comparator.comparing(Employee::getSalary));

        for (int i = 0; i < bonusCount; i++) {
            lowEarners.get(i).setBonus(lowEarners.get(i).getSalary() * 0.05f);
        }

        Employee highestBonus = EmployeeBonusService.getHighestBonusEmployee(employees);
        Employee lowestBonus = EmployeeBonusService.getLowestBonusEmployee(employees);
        float totalBonus = EmployeeBonusService.getTotalBonusCost(employees);

        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            tableModel.setValueAt(formatMoney(emp.getBonus()), i, 2);
        }

        highestBonusLabel.setText("Empleado con mayor bono: RUT " + highestBonus.getRut() + " - Bono: " + formatMoney(highestBonus.getBonus()));
        lowestBonusLabel.setText("Empleado con menor bono: RUT " + lowestBonus.getRut() + " - Bono: " + formatMoney(lowestBonus.getBonus()));
        totalBonusLabel.setText("Costo total de los bonos: " + formatMoney(totalBonus));

        bonusCalculationLabel.setText("Los bonos se calculan como el 5% del salario de los empleados debajo de la media: " + formatMoney(averageSalary));
    }

    // Método para ordenar los empleados por salario
    private void sortBySalary(ActionEvent e) {
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay empleados para ordenar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        employees.sort(Comparator.comparing(Employee::getSalary));

        tableModel.setRowCount(0);
        for (Employee emp : employees) {
            tableModel.addRow(new Object[]{emp.getRut(), formatMoney(emp.getSalary()), formatMoney(emp.getBonus())});
        }
    }

    // Método para buscar un empleado por su RUT
    private void searchEmployee(ActionEvent e) {
        try {
            int rut = Integer.parseInt(searchRutField.getText());
            Employee found = EmployeeBonusService.findEmployeeByRut(employees, rut);

            if (found == null) {
                JOptionPane.showMessageDialog(this, "Empleado no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(this, "RUT: " + found.getRut() + "\nSueldo: " + formatMoney(found.getSalary()) + "\nBono: " + formatMoney(found.getBonus()), "Información del Empleado", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un RUT válido (3 dígitos).", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método para formatear cantidades de dinero
    private String formatMoney(float amount) {
        return String.format("$%,.2f", amount);
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeBonusCalculator().setVisible(true));
    }
}