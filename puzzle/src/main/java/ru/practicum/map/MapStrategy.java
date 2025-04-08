package ru.practicum.map;

public interface MapStrategy {
    int[][] getBoard();

    int getEmptyTileCol();

    int getEmptyTileRow();
}
