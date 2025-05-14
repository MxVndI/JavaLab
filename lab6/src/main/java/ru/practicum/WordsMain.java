package ru.practicum;

import java.util.ArrayList;
import java.util.List;

public class WordsMain {
    public static void main(String[] args) {
        try {
            WordReader reader = new WordReader("words.txt");
            List<WordThread> threads = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                threads.add(new WordThread(i, reader));
            }

            for (WordThread thread : threads) thread.start();
            for (WordThread thread : threads) thread.join();

            reader.close();

            // Вывод результатов
            for (int i = 0; i < 5; i++) {
                System.out.println("Поток " + (i + 1) + ": " + String.join(", ", threads.get(i).getWords()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}