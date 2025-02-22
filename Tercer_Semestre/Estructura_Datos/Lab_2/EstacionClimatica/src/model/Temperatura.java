// Definimos el paquete al que pertenece esta clase, en este caso, "model".
// Esto significa que la clase "Temperatura" forma parte del paquete "model",
// lo que ayuda a organizar mejor nuestro código cuando trabajamos con múltiples clases.
package model;

// Definimos la clase "Temperatura". Esta clase representa la información de temperatura
// de un determinado día, incluyendo el nombre del día y las temperaturas máxima y mínima.
public class Temperatura {

    // Atributo privado "dia" que almacena el nombre del día en formato de cadena (String).
    private String dia; 

    // Atributo privado "maxima" que almacena la temperatura máxima del día en formato de número decimal (double).
    private double maxima; 

    // Atributo privado "minima" que almacena la temperatura mínima del día en formato de número decimal (double).
    private double minima; 

    // Constructor de la clase. Un constructor es un método especial que se ejecuta 
    // cuando se crea un objeto de esta clase. Su función es inicializar los atributos.
    // Recibe tres parámetros: un String (dia) y dos valores double (maxima y minima),
    // los cuales se asignan a los atributos correspondientes de la clase.
    public Temperatura(String dia, double maxima, double minima) {
        this.dia = dia;        // Asignamos el valor del parámetro "dia" al atributo de la clase "dia".
        this.maxima = maxima;  // Asignamos el valor del parámetro "maxima" al atributo de la clase "maxima".
        this.minima = minima;  // Asignamos el valor del parámetro "minima" al atributo de la clase "minima".
    }

    // Método "getDia()" que devuelve el nombre del día.
    // Se le conoce como un "getter" porque permite acceder al valor del atributo "dia".
    public String getDia() {
        return dia;  // Retorna el valor almacenado en el atributo "dia".
    }

    // Método "getMaxima()" que devuelve la temperatura máxima registrada.
    // También es un "getter" que permite acceder al valor del atributo "maxima".
    public double getMaxima() {
        return maxima;  // Retorna el valor almacenado en el atributo "maxima".
    }

    // Método "getMinima()" que devuelve la temperatura mínima registrada.
    // Es otro "getter" que permite acceder al valor del atributo "minima".
    public double getMinima() {
        return minima;  // Retorna el valor almacenado en el atributo "minima".
    }
}
