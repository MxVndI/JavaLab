package ru.practicum;

import java.util.*;

public class WordThread extends Thread {
    private final int threadId;
    private final WordReader wordReader;
    private final List<String> words = new ArrayList<>();

    public WordThread(int threadId, WordReader wordReader) {
        this.threadId = threadId;
        this.wordReader = wordReader;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String word = wordReader.readNextWord(threadId);
                if (word == null) break; // Конец файла
                words.add(word);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getWords() { return words; }
}