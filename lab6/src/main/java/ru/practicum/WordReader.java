package ru.practicum;

import java.io.*;

public class WordReader {
    private BufferedReader reader;
    private int currentIndex = 0;

    public WordReader(String filePath) throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(filePath));
    }

    public synchronized String readNextWord(int threadId) throws IOException, InterruptedException {
        while (currentIndex % 5 != threadId) {
            if (reader == null) return null; // Файл закрыт
            wait(); // Ожидание своей очереди
        }

        String word = reader.readLine();
        if (word == null) close(); // Конец файла
        currentIndex++;
        notifyAll(); // Передать ход следующему потоку
        return word;
    }

    public void close() throws IOException {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }
}