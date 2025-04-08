package ru.practicum.map;

public class SevenMapStrategy implements MapStrategy {

    @Override
    public int[][] getBoard() {
        return new int[7][7];
    }

    @Override
    public int getEmptyTileCol() {
        return 6;
    }

    @Override
    public int getEmptyTileRow() {
        return 6;
    }
}
