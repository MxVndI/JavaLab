package ru.practicum;

public class DisputeMain {
    public static void main(String[] args) throws InterruptedException {
        DisputeResult result = new DisputeResult();
        Participant p1 = new Participant(1, result);
        Participant p2 = new Participant(2, result);
        Participant p3 = new Participant(3, result);

        p1.start(); p2.start(); p3.start();
        p1.join(); p2.join(); p3.join();

        System.out.println("Победитель: " + result.getWinner() + ", способ: " + result.getMethod());
    }
}