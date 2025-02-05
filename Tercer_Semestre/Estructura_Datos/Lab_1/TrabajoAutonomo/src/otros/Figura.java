package otros;

// Importamos JOptionPane para mostrar cuadros de mensaje
import javax.swing.JOptionPane;

// Clase abstracta Figura que sirve como base para todas las figuras geométricas
public abstract class Figura {
    protected String nombre; // Nombre de la figura (Círculo, Rectángulo, etc.)
    protected double dimension1; // Primera dimensión (radio, base, lado, etc.)
    protected double dimension2; // Segunda dimensión (altura, apotema, etc.)

    // Constructor que inicializa el nombre y las dimensiones de la figura
    public Figura(String nombre, double dimension1, double dimension2) {
        this.nombre = nombre;
        this.dimension1 = dimension1;
        this.dimension2 = dimension2;
    }

    // Método abstracto que debe implementarse en cada subclase para calcular el área
    public abstract double calcularArea();

    // Método para mostrar el área de la figura en un cuadro de mensaje
    public void mostrarArea() {
        JOptionPane.showMessageDialog(null, "El área de " + nombre + " es: " + calcularArea());
    }
}

// Clase Circulo que hereda de Figura y representa un círculo
class Circulo extends Figura {
    public Circulo(double radio) {
        super("Círculo", radio, 0); // El radio es dimension1, dimension2 no se usa en el círculo
    }

    // Implementación del cálculo del área del círculo
    @Override
    public double calcularArea() {
        return Math.PI * dimension1 * dimension1; // Área = π * radio^2
    }
}

// Clase Rectangulo que hereda de Figura y representa un rectángulo
class Rectangulo extends Figura {
    public Rectangulo(double base, double altura) {
        super("Rectángulo", base, altura); // El rectángulo necesita base y altura
    }

    // Implementación del cálculo del área del rectángulo
    @Override
    public double calcularArea() {
        return dimension1 * dimension2; // Área = base * altura
    }
}

// Clase Triangulo que hereda de Figura y representa un triángulo
class Triangulo extends Figura {
    public Triangulo(double base, double altura) {
        super("Triángulo", base, altura); // El triángulo necesita base y altura
    }

    // Implementación del cálculo del área del triángulo
    @Override
    public double calcularArea() {
        return (dimension1 * dimension2) / 2; // Área = (base * altura) / 2
    }
}

// Clase Cuadrado que hereda de Figura y representa un cuadrado
class Cuadrado extends Figura {
    public Cuadrado(double lado) {
        super("Cuadrado", lado, lado); // El cuadrado tiene todos sus lados iguales, por lo que dimension1 y dimension2 son iguales
    }

    // Implementación del cálculo del área del cuadrado
    @Override
    public double calcularArea() {
        return dimension1 * dimension2; // Área = lado * lado
    }
}

// Clase Pentagono que hereda de Figura y representa un pentágono
class Pentagono extends Figura {
    public Pentagono(double lado, double apotema) {
        super("Pentágono", lado, apotema); // El pentágono necesita el lado y el apotema (la distancia desde el centro hasta un vértice)
    }

    // Implementación del cálculo del área del pentágono
    @Override
    public double calcularArea() {
        return (5 * dimension1 * dimension2) / 2; // Área = (5 * lado * apotema) / 2
    }
}
