package ru.practicum.map;

public class FiveMapStrategy implements MapStrategy {
    @Override
    public int[][] getBoard() {
        return new int[5][5];
    }

    @Override
    public int getEmptyTileCol() {
        return 4;
    }

    @Override
    public int getEmptyTileRow() {
        return 4;
    }
}
