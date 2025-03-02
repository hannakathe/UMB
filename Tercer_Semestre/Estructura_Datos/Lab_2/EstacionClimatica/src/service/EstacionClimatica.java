// Definimos el paquete al que pertenece esta clase, en este caso, "service".
// Esto ayuda a organizar mejor el código dentro de nuestro proyecto.
package service;

// Importamos la clase "Temperatura" que está en el paquete "model".
// Esto nos permite usar objetos de la clase Temperatura dentro de EstacionClimatica.
import model.Temperatura;

// Importamos las clases necesarias para manejar listas dinámicas.
import java.util.ArrayList;
import java.util.List;

// Definimos la clase "EstacionClimatica".
// Esta clase actúa como un servicio para gestionar un conjunto de temperaturas.
public class EstacionClimatica {

    // Lista para almacenar objetos de tipo "Temperatura".
    // Usamos una lista de tipo "ArrayList" para poder agregar y manipular múltiples registros de temperaturas.
    private List<Temperatura> temperaturas = new ArrayList<>();

    // Método para agregar una nueva temperatura a la lista.
    /* Recibe tres parámetros: el nombre del día, la temperatura máxima y la mínima.
    Luego, crea un objeto de la clase "Temperatura" y lo añade a la lista "temperaturas". */
    
    public void agregarTemperatura(String dia, double maxima, double minima) {
        temperaturas.add(new Temperatura(dia, maxima, minima));
    }

    // Método para filtrar temperaturas dentro de un rango dado.
    /* Este método recibe dos parámetros: "min" (mínimo) y "max" (máximo).
    Retorna una lista con todas las temperaturas cuya mínima es mayor o igual a "min" y cuya máxima es menor o igual a "max".*/ 

    public List<Temperatura> filtrarPorRango(double min, double max) {
        List<Temperatura> resultado = new ArrayList<>(); // Lista auxiliar para almacenar los resultados de tem entre los rangos dados. 
        
        // Recorremos todas las temperaturas almacenadas.
        for (Temperatura t : temperaturas) {
            /* Verificamos si la temperatura mínima es mayor o igual al valor "min" 
            y la temperatura máxima es menor o igual al valor "max". */ 

            if (t.getMinima() >= min && t.getMaxima() <= max) { 
                resultado.add(t); // Si cumple la condición, agregamos la temperatura a la lista "resultado".
            }
        }
        return resultado; // Retornamos la lista con las temperaturas que cumplen el criterio.
    }

    // Método para filtrar temperaturas con mínima por debajo de cero.
    // Retorna una lista de temperaturas donde la temperatura mínima es menor a 0.
    public List<Temperatura> filtrarPorMinimaBajoCero() {
        List<Temperatura> resultado = new ArrayList<>(); // Lista auxiliar para almacenar los resultados temperaturas menores a 0. 
        
        // Recorremos todas las temperaturas almacenadas.
        for (Temperatura t : temperaturas) {
            // Si la temperatura mínima es menor a 0, la agregamos a la lista.
            if (t.getMinima() < 0) {
                resultado.add(t);
            }
        }
        return resultado; // Retornamos la lista con las temperaturas bajo cero.
    }

    // Método para calcular la media de las temperaturas de un día específico.
    // Recibe como parámetro el nombre del día y devuelve el promedio entre la temperatura máxima y mínima de todas las temperaturas registradas para ese día.
    public double calcularMediaPorDia(String dia) {
        double suma = 0; // Variable para acumular la suma de los promedios.
        int contador = 0; // Contador para saber cuántas temperaturas corresponden al día indicado.

        // Recorremos todas las temperaturas almacenadas.
        for (Temperatura t : temperaturas) {
            // Comparamos el día ignorando mayúsculas y minúsculas (equalsIgnoreCase).
            if (t.getDia().equalsIgnoreCase(dia)) { 
                suma += (t.getMaxima() + t.getMinima()) / 2; // Calculamos el promedio de la temperatura máxima y mínima y lo sumamos.
                contador++; // Aumentamos el contador porque encontramos un día coincidente.
            }
        }

        // Si no se encontró ninguna temperatura para ese día, retornamos 0.
        // Si sí hay datos, calculamos el promedio total dividiendo la suma entre el contador.
        return (contador == 0) ? 0 : suma / contador; /* Pregunta si el contador es igual a 0, si es true, devulve 0, 
                                                        si es false, la suma total, segun el dia, 
                                                        pasa a dividirse entre el contador */ 
    }

    // Método para obtener todas las temperaturas almacenadas en la lista.
    // Retorna una nueva lista con las mismas temperaturas, evitando modificaciones directas en la lista original.
    public List<Temperatura> obtenerTodasLasTemperaturas() {
        return new ArrayList<>(temperaturas); // Creamos una nueva lista copiando los valores de "temperaturas".
    }
}
