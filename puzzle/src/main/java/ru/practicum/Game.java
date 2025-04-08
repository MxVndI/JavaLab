package ru.practicum;

import ru.practicum.map.MapStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;

public class Game {
    private int[][] board;
    private int emptyRow;
    private int playTime;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getEmptyCol() {
        return emptyCol;
    }

    public void setEmptyCol(int emptyCol) {
        this.emptyCol = emptyCol;
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public void setEmptyRow(int emptyRow) {
        this.emptyRow = emptyRow;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    private int emptyCol;
    private int size;

    public void start(MapStrategy mapStrategy) {
        this.board = mapStrategy.getBoard();
        this.size = board.length;
        this.emptyRow = mapStrategy.getEmptyTileRow();
        this.emptyCol = mapStrategy.getEmptyTileCol();
        initBoard();
        shuffleMap();
        this.playTime = LocalTime.now().getSecond();
    }

    private void initBoard() {
        int k = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = k;
                k += 1;
            }
        }
    }

    private void shuffleMap() {
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int directionX = random.nextInt(-1, 2);
            int directionY = random.nextInt(-1, 2);
            directionX = emptyCol + directionX;
            directionY = emptyRow + directionY;
            if (isValidMove(directionX, directionY)) {
                if (isAdjacent(directionX, directionY))
                swapTiles(directionX, directionY);
            }
        }
    }

    public void swapTiles(int row, int col) {
        int temp = board[row][col];
        board[row][col] = 0;
        board[emptyRow][emptyCol] = temp;
        emptyRow = row;
        emptyCol = col;
    }

    private boolean isAdjacent(int row, int col) {
        return (Math.abs(row - emptyRow) == 1 && col == emptyCol) ||
                (Math.abs(col - emptyCol) == 1 && row == emptyRow);
    }

    private boolean isValidMove(int row, int col) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    public boolean isSolved() {
        int value = 1;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (row == size - 1 && col == size - 1) {
                    if (board[row][col] != 0) return false;
                } else {
                    if (board[row][col] != value) return false;
                    value++;
                }
            }
        }
        this.playTime = LocalTime.now().getSecond() - playTime;
        System.out.println("Вы решали " + this.playTime + " секунд");
        return true;
    }
}
