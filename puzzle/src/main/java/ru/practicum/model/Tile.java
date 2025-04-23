package ru.practicum.model;

public class Tile {
    private int row;
    private int col;
    private final int value;

    public Tile(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "row=" + row +
                ", col=" + col +
                ", value=" + value +
                '}';
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
