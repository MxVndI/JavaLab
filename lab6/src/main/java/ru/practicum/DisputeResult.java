package ru.practicum;

import java.util.*;

public class DisputeResult {
    private List<String> choices = new ArrayList<>(); // Список выборов участников
    private String winner; // Победитель
    private String method; // Способ перемещения

    public synchronized void addChoice(String method) {
        choices.add(method);
        if (choices.size() == 3) {
            determineWinner(); // Определить победителя
            notifyAll(); // Оповестить все потоки
        }
    }

    private void determineWinner() {
        Random rand = new Random();
        int index = rand.nextInt(3);
        winner = "Участник " + (index + 1);
        method = choices.get(index);
    }

    public synchronized void waitForResult() throws InterruptedException {
        while (choices.size() < 3) {
            wait(); // Ожидание выбора всех участников
        }
    }

    public String getWinner() { return winner; }
    public String getMethod() { return method; }
}