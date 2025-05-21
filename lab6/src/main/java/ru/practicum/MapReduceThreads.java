package ru.practicum;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MapReduceThreads {

    public static void main(String[] args) throws InterruptedException {
        List<String> data = Arrays.asList(
                "apple banana orange",
                "banana orange grape",
                "orange grape apple"
        );

        List<Thread> threads = new ArrayList<>();
        Map<String, Integer> resultMap = new ConcurrentHashMap<>();

        for (String line : data) {
            WordCounter counter = new WordCounter(line, resultMap);
            Thread thread = new Thread(counter);
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Результат: " + resultMap);
    }

    static class WordCounter implements Runnable {
        private final String text;
        private final Map<String, Integer> sharedMap;

        WordCounter(String text, Map<String, Integer> sharedMap) {
            this.text = text;
            this.sharedMap = sharedMap;
        }

        @Override
        public void run() {
            Map<String, Integer> localMap = new HashMap<>();
            String[] words = text.split("\\s+");

            for (String word : words) {
                localMap.put(word, localMap.getOrDefault(word, 0) + 1);
            }

            synchronized (sharedMap) {
                localMap.forEach((word, count) ->
                        sharedMap.merge(word, count, Integer::sum)
                );
            }
        }
    }
}