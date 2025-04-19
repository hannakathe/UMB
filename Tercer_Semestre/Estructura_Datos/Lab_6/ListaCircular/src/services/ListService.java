package services;

import models.CircularList;
import java.util.Random;

public class ListService {
    private CircularList list = new CircularList();

    public void insert(int value) {
        list.insert(value);
    }

    public boolean delete(int value) {
        return list.delete(value);
    }

    public boolean update(int oldValue, int newValue) {
        return list.update(oldValue, newValue);
    }

    public int search(int value) {
        return list.search(value);
    }

    public void sort() {
        list.sort();
    }

    public void clear() {
        list.clear();
    }

    public String display() {
        return list.toString();
    }

    public void fillRandom(int count, int min, int max) {
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            list.insert(rand.nextInt(max - min + 1) + min);
        }
    }
}
