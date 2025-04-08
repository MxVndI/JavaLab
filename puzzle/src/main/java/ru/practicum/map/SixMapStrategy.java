package ru.practicum.map;

public class SixMapStrategy implements MapStrategy {

    @Override
    public int[][] getBoard() {
        return new int[6][6];
    }

    @Override
    public int getEmptyTileCol() {
        return 5;
    }

    @Override
    public int getEmptyTileRow() {
        return 5;
    }
}
