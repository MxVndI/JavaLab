package ru.practicum;

import ru.practicum.map.MapStrategy;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Random;

public class Game {
    private int[][] board;
    private int emptyRow;
    private long playTime;
    private int emptyCol;
    private int size;

    public int[][] getBoard() {
        return board;
    }

    public void start(MapStrategy mapStrategy) {
        this.board = mapStrategy.getBoard();
        this.size = board.length;
        this.emptyRow = mapStrategy.getEmptyTileRow();
        this.emptyCol = mapStrategy.getEmptyTileCol();
        initBoard();
        shuffleMap();
        this.playTime = Instant.now().getEpochSecond();
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
        int newCordX = 0, newCordY = 0;
        for (int i = 0; i < 10000; i++) {
            int direction = random.nextInt(1, 5);
            switch (direction) {
                case 1:
                    newCordX = emptyCol + 1;
                    newCordY = emptyRow;
                    break;
                case 2:
                    newCordX = emptyCol - 1;
                    newCordY = emptyRow;
                    break;
                case 3:
                    newCordX = emptyCol;
                    newCordY = emptyRow + 1;
                    break;
                case 4:
                    newCordX = emptyCol;
                    newCordY = emptyRow - 1;
                    break;
            }
            swapTiles(newCordY, newCordX);
        }
    }

    public void swapTiles(int row, int col) {
        if (isValidMove(row, col) && isAdjacent(row, col)) {
            int temp = board[row][col];
            board[row][col] = 0;
            board[emptyRow][emptyCol] = temp;
            emptyRow = row;
            emptyCol = col;
        }
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
        this.playTime = Instant.now().getEpochSecond() - playTime;
        System.out.println("Вы решали " + this.playTime + " секунд");
        saveResultToFile();
        return true;
    }

    private void saveResultToFile() {
        try (FileWriter writer = new FileWriter("bestResult.txt", true);
             FileReader reader = new FileReader("bestResult.txt")) {
            long bestResult = Long.valueOf(reader.read());
            if (playTime < bestResult) {
                writer.write(String.valueOf(playTime));
                writer.flush();
            } else {
                writer.write(String.valueOf(bestResult));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
