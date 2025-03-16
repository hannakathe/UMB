package models;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BinarySearchModel {
    private List<Integer> sortedNumbers;

    public BinarySearchModel(int[] sortedArray) {
        this.sortedNumbers = Arrays.stream(sortedArray).boxed().collect(Collectors.toList());
    }

    public int getIndexOf(int number) {
        return sortedNumbers.indexOf(number);
    }

    public List<Integer> getSortedNumbers() {
        return sortedNumbers;
    }
}
