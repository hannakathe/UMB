package otros;

import javax.swing.JOptionPane;

//Maneja menu y las instancias de las clases de Figura.java

public class CalculadoraFiguras {
    public static void iniciar() {
        boolean continuar = true; // varialble para controlar el bucle de selección de figuras

        // Menu: Bucle principal que permite al usuario seleccionar una figura y calcular su área
        while (continuar) {
            String[] opciones = {"Círculo", "Rectángulo", "Triángulo", "Cuadrado", "Pentágono", "Salir"};
            // Mostrar cuadro de diálogo con las opciones disponibles
            String seleccion = (String) JOptionPane.showInputDialog(
                    null,
                    "Seleccione la figura para calcular el área:",
                    "Calculadora de Áreas",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            // Si el usuario selecciona "Salir" o cierra el cuadro, se termina el bucle
            if (seleccion == null || seleccion.equals("Salir")) {
                continuar = false;
                break;
            }

            Figura figura = null; // Variable que almacenará la figura seleccionada
            // Dependiendo de la figura seleccionada, pedimos las dimensiones correspondientes y creamos la figura
            switch (seleccion) {
                case "Círculo":
                    double radio = pedirDouble("Ingrese el radio del círculo:");
                    figura = new Circulo(radio); //Instancia la Clase circulo (objeto basado en la clase circulo)
                    break;
                case "Rectángulo":
                    double baseR = pedirDouble("Ingrese la base del rectángulo:");
                    double alturaR = pedirDouble("Ingrese la altura del rectángulo:");
                    figura = new Rectangulo(baseR, alturaR);
                    break;
                case "Triángulo":
                    double baseT = pedirDouble("Ingrese la base del triángulo:");
                    double alturaT = pedirDouble("Ingrese la altura del triángulo:");
                    figura = new Triangulo(baseT, alturaT);
                    break;
                case "Cuadrado":
                    double ladoC = pedirDouble("Ingrese el lado del cuadrado:");
                    figura = new Cuadrado(ladoC);
                    break;
                case "Pentágono":
                    double ladoP = pedirDouble("Ingrese el lado del pentágono:");
                    double apotemaP = pedirDouble("Ingrese el apotema del pentágono:");
                    figura = new Pentagono(ladoP, apotemaP);
                    break;
            }

            // Si la entrada de valores se cancela, mostramos su área normalmente 0.0 
            if (figura != null) {
                figura.mostrarArea();
            }
        }
    }

    // Método para pedir un número de tipo double al usuario
    private static double pedirDouble(String mensaje) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, mensaje);
                // Si el usuario cancela la entrada, mostramos un mensaje y devolvemos 0
                if (input == null) {
                    JOptionPane.showMessageDialog(null, "Entrada cancelada.");
                    return 0;
                }
                return Double.parseDouble(input); // Intentamos convertir la entrada a double
            } catch (NumberFormatException e) {
                // Si ocurre un error de formato (por ejemplo, el usuario ingresa un valor no numérico), mostramos un mensaje de error
                JOptionPane.showMessageDialog(null, "Por favor, ingrese un número válido.");
            }
        }
    }
}
