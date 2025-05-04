// Define el paquete al que pertenece la clase
package models;

// Importa la clase Color de la librería AWT (Abstract Window Toolkit) de Java
import java.awt.Color;

// Declaración de la clase pública llamada Nodo
public class Nodo {
    // Atributo público que almacena el valor del nodo (entero)
    public int valor;
    
    // Atributo público que representa el nodo hijo izquierdo
    public Nodo izquierda;
    
    // Atributo público que representa el nodo hijo derecho
    public Nodo derecha;
    
    // Atributo privado que define el color del nodo (usado en árboles balanceados como Red-Black)
    private Color color;

    // Constructor de la clase Nodo que recibe un valor entero como parámetro
    public Nodo(int valor) {
        // Asigna el valor pasado como argumento al atributo 'valor'
        this.valor = valor;
        
        // Inicializa el nodo izquierdo como null (no tiene hijo izquierdo al crearse)
        this.izquierda = null;
        
        // Inicializa el nodo derecho como null (no tiene hijo derecho al crearse)
        this.derecha = null;
        
        // Asigna el color negro por defecto (común en árboles Red-Black)
        this.color = Color.BLACK; 
    }

    // ----- MÉTODOS GETTER Y SETTER -----
    
    // Getter para obtener el valor del nodo
    public int getValor() {
        return valor;
    }

    // Setter para modificar el valor del nodo
    public void setValor(int valor) {
        this.valor = valor;
    }

    // Getter para obtener el nodo hijo izquierdo
    public Nodo getIzquierda() {
        return izquierda;
    }

    // Setter para asignar un nodo hijo izquierdo
    public void setIzquierda(Nodo izquierda) {
        this.izquierda = izquierda;
    }

    // Getter para obtener el nodo hijo derecho
    public Nodo getDerecha() {
        return derecha;
    }

    // Setter para asignar un nodo hijo derecho
    public void setDerecha(Nodo derecha) {
        this.derecha = derecha;
    }

    // Getter para obtener el color del nodo
    public Color getColor() {
        return color;
    }

    // Setter para modificar el color del nodo
    public void setColor(Color color) {
        this.color = color;
    }
}