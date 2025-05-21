package ru.practicum;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class TicketSimulation {
    public static volatile int globalTick = 0;

    public static void main(String args[]) throws InterruptedException {
        BlockingQueue<Passenger> queue = new LinkedBlockingQueue<>();
        BlockingQueue<Passenger> queueA = new LinkedBlockingQueue<>();
        BlockingQueue<Passenger> queueB = new LinkedBlockingQueue<>();
        AtomicLong totalServiceTime = new AtomicLong(0);
        AtomicLong totalWaitingTime = new AtomicLong(0);

        ExecutorService executor = Executors.newFixedThreadPool(2);
       // executor.submit(new TicketWindow("Окно 1", queue, totalServiceTime, totalWaitingTime));
        //executor.submit(new TicketWindow("Окно 2", queue, totalServiceTime, totalWaitingTime));
        executor.submit(new TicketWindow("Окно 1", queueA, totalServiceTime, totalWaitingTime));
        executor.submit(new TicketWindow("Окно 2", queueB, totalServiceTime, totalWaitingTime));

//        Thread generator = new Thread(() -> {
//            int id = 1;
//            Random random = new Random();
//            double lambda = 0.9;
//            int currentTick = 0;
//
//            while (id <= 5) {
//                try {
//                    synchronized (System.out) {
//                        System.out.println("Пассажир " + id + " прибыл");
//                    }
//
//                    int serviceTime = 1900 + random.nextInt(200);
//                    Passenger passenger = new Passenger(id, serviceTime, currentTick);
//
//                    queue.put(passenger);
//                    double nextInterval = -Math.log(1 - random.nextDouble()) / lambda;
//                    nextInterval = Math.max(0.5, Math.min(2.0, nextInterval));
//                    int arrivalInterval = (int) (nextInterval * 1000);
//                    currentTick += arrivalInterval;
//                    globalTick = currentTick;
//
//                    id++;
//                } catch (InterruptedException e) {
//                    break;
//                }
//            }
//        });
        Thread generator2 = new Thread(() -> {
            int id = 1;
            Random random = new Random();
            double lambda = 0.9;
            int currentTick = 0;

            while (id <= 5) {
                try {
                    int destinationInt = random.nextInt(2);
                    String destination = (destinationInt == 0) ? "A" : "B";

                    int serviceTime = 1900 + random.nextInt(200);
                    Passenger p = new Passenger(id, serviceTime, currentTick, destination);
                    synchronized (System.out) {
                        System.out.println("Пассажир с id " + id + " прибыл " + destination);
                        if (destination.equals("A")) {
                            queueA.put(p);
                        } else {
                            queueB.put(p);
                        }
                        double nextInterval = -Math.log(1 - random.nextDouble()) / lambda;
                        int arrivalInterval = (int) (nextInterval * 1000);
                        currentTick += arrivalInterval;
                        globalTick = currentTick;
                        id++;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });

        generator2.start();
        generator2.join();

        if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
            executor.shutdown();
        }

        double totalServiceMinutes = totalServiceTime.get() / 1000.0;
        double totalWaitingMinutes = totalWaitingTime.get() / 1000.0;

        synchronized (System.out) {
            System.out.println("Общее время обслуживания всех пассажиров " + totalServiceMinutes + " мин");
            System.out.println("Общее время ожидания всех пассажиров " + totalWaitingMinutes + " мин");
        }
    }


}
