package ru.practicum;
import java.util.concurrent.BlockingQueue;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicInteger;

public class TicketWindow implements Runnable {
    private final String name;
    private final BlockingQueue<Passenger> queue;
    private final AtomicLong totalServiceTime;
    private final AtomicLong totalWaitingTime;
    private int lastServiceEndTick = 0;

    public TicketWindow(String name, BlockingQueue<Passenger> queue, AtomicLong totalServiceTime, AtomicLong totalWaitingTime) {
        this.name = name;
        this.queue = queue;
        this.totalServiceTime = totalServiceTime;
        this.totalWaitingTime = totalWaitingTime;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                Passenger passenger = queue.take();

                int startServiceTick = Math.max(passenger.getArrivalTick(), lastServiceEndTick);
                int waitingTime = startServiceTick - passenger.getArrivalTick();

                if (waitingTime < 0) waitingTime = 0;

                totalWaitingTime.addAndGet(waitingTime);

                synchronized (System.out) {
                    System.out.println(passenger.getId() + " имя " + name + " ждал " + waitingTime/1000.0);
                }

                int serviceTime = passenger.getServiceTime();

                lastServiceEndTick = startServiceTick + serviceTime;
                totalServiceTime.addAndGet(serviceTime);

                synchronized (System.out) {
                    System.out.println(passenger.getId() + " - id пассажира, имя = " + name + " обслуживался " + serviceTime/1000.0 + " мин");
                }
            }

        } catch (InterruptedException e) {
            System.out.println("[" + name + "] Работа завершена.");
            Thread.currentThread().interrupt();
        }
    }
}