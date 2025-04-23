package ru.practicum;

import ru.practicum.map.MapStrategy;
import ru.practicum.model.Tile;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private Tile[][] board;
    private Tile emptyTile;
    private final List<ModelListener> listeners = new ArrayList<>();

    public void start(MapStrategy strategy) {
        board = strategy.getBoard();
        findEmptyTile();
    }

    public Tile[][] getBoard() {
        return board;
    }

    public Tile getEmptyTile() {
        return emptyTile;
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void swapTiles(Tile tile) {
        int tileRow = tile.getRow();
        int tileCol = tile.getCol();
        int emptyRow = emptyTile.getRow();
        int emptyCol = emptyTile.getCol();

        // Проверяем, является ли выбранная плитка соседней с пустой
        if (!isNeighbor(tileRow, tileCol, emptyRow, emptyCol)) {
            return; // Если нет, ничего не делаем
        }

        // Меняем плитки местами в массиве
        board[emptyRow][emptyCol] = tile;
        board[tileRow][tileCol] = emptyTile;

        // Обновляем координаты плиток
        tile.setPosition(emptyRow, emptyCol);
        emptyTile.setPosition(tileRow, tileCol);

        // Обновляем пустую плитку
        emptyTile = tile;

        // Уведомляем слушателей об изменении модели
        notifyListeners();
    }

    public boolean isSolved() {
        int expected = 1;
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.getValue() != 0 && tile.getValue() != expected) {
                    return false;
                }
                expected++;
            }
        }
        return true;
    }

    public void removeListener(ModelListener listener) {
        listeners.remove(listener);
    }

    private boolean isNeighbor(int tileRow, int tileCol, int emptyRow, int emptyCol) {
        // Проверяем, является ли плитка соседней с пустой (по горизонтали или вертикали)
        return (Math.abs(tileRow - emptyRow) + Math.abs(tileCol - emptyCol) == 1);
    }

    private void findEmptyTile() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.getValue() == 0) {
                    emptyTile = tile;
                    System.out.println(emptyTile.getCol() + " sfd " + emptyTile.getRow());
                    return;
                }
            }
        }
    }

    private void notifyListeners() {
        for (ModelListener listener : listeners) {
            listener.onModelChange();
        }
    }
}