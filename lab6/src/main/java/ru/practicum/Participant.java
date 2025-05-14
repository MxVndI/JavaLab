package ru.practicum;

import java.util.Random;

public class Participant extends Thread {
    private static final String[] METHODS = {"суша", "море", "воздух"};
    private final int id;
    private final DisputeResult result;

    public Participant(int id, DisputeResult result) {
        this.id = id;
        this.result = result;
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();
            String method = METHODS[rand.nextInt(METHODS.length)]; // Случайный выбор
            System.out.println("Участник " + id + " выбрал " + method);
            result.addChoice(method);
            result.waitForResult(); // Ожидание завершения
            if (result.getWinner().equals("Участник " + id)) {
                System.out.println("Участник " + id + " победил!");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}