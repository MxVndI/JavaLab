package ru.practicum.map;

import ru.practicum.model.Tile;

public abstract class MapStrategy {
    public MapStrategy(int size) {
        this.size = size;
    }

    protected int size = 5;

    public Tile[][] getBoard() {
        Tile[][] board = new Tile[size][size];
        int value = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = new Tile(i, j, value++);
            }
        }
        int emptyRow = getEmptyTileRow();
        int emptyCol = getEmptyTileCol();
        board[emptyRow][emptyCol] = new Tile(emptyRow, emptyCol, 0);
        return board;
    }

    public int getEmptyTileCol() {
        return size - 1;
    }

    public int getEmptyTileRow() {
        return size - 1;
    }
}