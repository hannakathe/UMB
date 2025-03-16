package services;

import java.util.HashMap;
import java.util.List;

public class BinarySearchService {

    public static HashMap<Integer, Long[]> searchNumbers(List<Integer> sortedList, List<Integer> numbersToSearch) {
        HashMap<Integer, Long[]> searchResults = new HashMap<>();

        for (int number : numbersToSearch) {
            long startTime = System.nanoTime();
            int index = sortedList.indexOf(number);
            long endTime = System.nanoTime();

            long elapsedTimeMs = (endTime - startTime) / 1_000_000;
            long elapsedTimeNs = (endTime - startTime);

            searchResults.put(number, new Long[]{(long) index, elapsedTimeMs, elapsedTimeNs});
        }

        return searchResults;
    }
}
