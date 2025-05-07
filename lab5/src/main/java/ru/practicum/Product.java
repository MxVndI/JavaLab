package ru.practicum;

public class Product {
    private String type;
    private String brand;
    private Integer size;
    private String color;
    private double price;

    public Product(String type, String brand, Integer size, String color, double price) {
        this.type = type;
        this.brand = brand;
        this.size = size;
        this.color = color;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("%s [%s] | %s | %s | %.2f руб", type, brand, color, size != null ? size : "-", price);
    }
}