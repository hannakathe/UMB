// Importación de las clases necesarias
package services;  // Paquete donde se encuentra la clase Empresa.
import model.Sede;  // Importa la clase 'Sede' del paquete 'model' para trabajar con las sedes de la empresa.
import java.util.ArrayList;  // Importa la clase ArrayList para almacenar la lista de sedes.
import java.util.List;  // Importa la interfaz List para definir una lista de sedes.

public class Empresa {
    // Atributo de la clase que contiene la lista de sedes.
    private List<Sede> sedes;

    // Constructor de la clase Empresa que recibe el número de sedes.
    public Empresa(int numeroDeSedes) {
        // Inicializa la lista de sedes como un ArrayList vacío.
        this.sedes = new ArrayList<>();
        // Crea e inicializa las sedes según el número recibido.
        for (int i = 0; i < numeroDeSedes; i++) {
            // Añade una nueva sede a la lista, con un nombre basado en el índice.
            sedes.add(new Sede("Sede " + (i + 1)));
        }
    }

    // Método getter que retorna la lista de sedes.
    public List<Sede> getSedes() {
        return sedes;
    }

    // Método que muestra las ventas que están por encima del promedio de cada sede.
    public void mostrarVentasSobrePromedio() {
        // Itera sobre todas las sedes de la empresa.
        for (Sede sede : sedes) {
            // Calcula el promedio de ventas de la sede.
            double promedio = sede.calcularPromedio();
            System.out.println("");  // Salto de línea para mejorar la legibilidad.
            // Imprime el nombre de la sede y un título para las ventas sobre el promedio.
            System.out.println("Ventas sobre el promedio para " + sede.getNombre() + ":");
            System.out.println("");  // Salto de línea para mejorar la legibilidad.

            // Itera sobre todas las ventas de la sede.
            for (int i = 0; i < sede.getVentas().size(); i++) {
                // Si la venta de ese día es mayor que el promedio, la imprime.
                if (sede.getVentas().get(i) > promedio) {
                    System.out.println("Día " + (i + 1) + ": " + sede.getVentas().get(i));
                }
            }
        }
    }

    // Método que aumenta las ventas por debajo del promedio en un porcentaje dado.
    public void aumentarVentasDebajoPromedio(double porcentaje) {
        // Itera sobre todas las sedes de la empresa.
        for (Sede sede : sedes) {
            // Llama al método de la clase 'Sede' que aumenta las ventas por debajo del promedio.
            sede.aumentarVentasDebajoPromedio(porcentaje);
        }
    }

    // Método que calcula el promedio general de ventas para toda la empresa.
    public double calcularPromedioGeneral() {
        double sumaTotal = 0;  // Acumulador de las ventas totales.
        int cantidadTotal = 0;  // Acumulador de la cantidad total de ventas.

        // Itera sobre todas las sedes de la empresa.
        for (Sede sede : sedes) {
            // Suma las ventas de la sede, multiplicadas por la cantidad de días (ventas).
            sumaTotal += sede.calcularPromedio() * sede.getVentas().size();
            // Suma la cantidad total de días con ventas en esa sede.
            cantidadTotal += sede.getVentas().size();
        }

        // Retorna el promedio total de ventas de toda la empresa.
        return sumaTotal / cantidadTotal;
    }
}
