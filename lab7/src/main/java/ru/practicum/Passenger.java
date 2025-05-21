package ru.practicum;

public class Passenger {
    private final int id;
    private final int serviceTime;
    private final int arrivalTick;
    private final String destination;

    public int getId() {
        return id;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getArrivalTick() {
        return arrivalTick;
    }

    public Passenger(int id, int serviceTime, int arrivalTick, String destination) {
        this.id = id;
        this.serviceTime = serviceTime;
        this.arrivalTick = arrivalTick;
        this.destination = destination;
    }
}