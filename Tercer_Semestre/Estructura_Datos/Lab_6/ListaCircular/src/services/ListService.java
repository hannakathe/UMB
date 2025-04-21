// Declara que esta clase pertenece al paquete 'services'
package services;

// Importa las clases necesarias
import models.CircularList;
import java.util.Random;

// Clase que actúa como servicio/intermediario para operaciones con la lista circular
public class ListService {
    // Instancia de la lista circular que este servicio manejará
    private CircularList list = new CircularList();

    // Método para insertar un valor en la lista
    // Parámetro: value - valor entero a insertar
    public void insert(int value) {
        list.insert(value); // Delega la operación a la lista circular
    }

    // Método para eliminar un valor de la lista
    // Parámetro: value - valor a eliminar
    // Retorna: true si se eliminó, false si no se encontró
    public boolean delete(int value) {
        return list.delete(value); // Delega la operación
    }

    // Método para actualizar un valor en la lista
    // Parámetros:
    //   oldValue - valor a buscar
    //   newValue - nuevo valor a asignar
    // Retorna: true si se actualizó, false si no se encontró
    public boolean update(int oldValue, int newValue) {
        return list.update(oldValue, newValue); // Delega la operación
    }

    // Método para buscar un valor en la lista
    // Parámetro: value - valor a buscar
    // Retorna: posición del valor (índice) o -1 si no se encuentra
    public int search(int value) {
        return list.search(value); // Delega la operación
    }

    // Método para ordenar la lista (ascendente)
    public void sort() {
        list.sort(); // Delega la operación
    }

    // Método para vaciar la lista
    public void clear() {
        list.clear(); // Delega la operación
    }

    // Método para obtener representación textual de la lista
    // Retorna: String con formato [valor1, valor2, ...]
    public String display() {
        return list.toString(); // Usa el método toString() de la lista
    }

    // Método para llenar la lista con valores aleatorios
    // Parámetros:
    //   count - cantidad de valores a generar
    //   min - valor mínimo (inclusive)
    //   max - valor máximo (inclusive)
    public void fillRandom(int count, int min, int max) {
        Random rand = new Random(); // Generador de números aleatorios
        for (int i = 0; i < count; i++) {
            // Genera número aleatorio en el rango [min, max] e inserta
            list.insert(rand.nextInt(max - min + 1) + min);
        }
    }
}