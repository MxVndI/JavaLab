package ru.practicum.model.logic;

import ru.practicum.listener.ModelListener;
import ru.practicum.model.tile.Tile;
import ru.practicum.model.map.MapStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    private Tile[][] board;
    private Tile emptyTile;
    private final List<ModelListener> listeners = new ArrayList<>();
    private final List<Runnable> winListeners = new ArrayList<>();

    public void start(MapStrategy strategy) {
        board = strategy.getBoard();
        findEmptyTile();
        shuffle();
    }

    public Tile[][] getBoard() {
        return board;
    }

    public Tile getEmptyTile() {
        return emptyTile;
    }

    public void addWinListener(Runnable listener) {
        winListeners.add(listener);
    }

    public void addListener(ModelListener listener) {
        listeners.add(listener);
    }

    public void swapTiles(Tile tile) {
        int tileRow = tile.getRow();
        int tileCol = tile.getCol();
        int emptyRow = emptyTile.getRow();
        int emptyCol = emptyTile.getCol();

        if (!isNeighbor(tileRow, tileCol, emptyRow, emptyCol)) {
            return;
        }
        tile.setPosition(emptyRow, emptyCol);
        emptyTile.setPosition(tileRow, tileCol);
        Tile temp = board[emptyRow][emptyCol];
        board[emptyRow][emptyCol] = tile;
        board[tileRow][tileCol] = temp;
        notifyListeners();
        checkWin();
    }

    private void checkWin() {
        if (isSolved()) {
            winListeners.forEach(Runnable::run);
        }
    }

    private void shuffle() {
        Random random = new Random();
        int shuffleSteps = 1000;
        for (int i = 0; i < shuffleSteps; i++) {
            int emptyRow = emptyTile.getRow();
            int emptyCol = emptyTile.getCol();

            List<Tile> possibleMoves = new ArrayList<>();
            if (emptyRow > 0) {
                possibleMoves.add(board[emptyRow - 1][emptyCol]);
            }
            if (emptyRow < board.length - 1) {
                possibleMoves.add(board[emptyRow + 1][emptyCol]);
            }
            if (emptyCol > 0) {
                possibleMoves.add(board[emptyRow][emptyCol - 1]);
            }
            if (emptyCol < board[0].length - 1) {
                possibleMoves.add(board[emptyRow][emptyCol + 1]);
            }

            if (!possibleMoves.isEmpty()) {
                Tile randomTile = possibleMoves.get(random.nextInt(possibleMoves.size()));
                swapTiles(randomTile);
            }
        }
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
        return (Math.abs(tileRow - emptyRow) + Math.abs(tileCol - emptyCol) == 1);
    }

    private void findEmptyTile() {
        for (Tile[] row : board) {
            for (Tile tile : row) {
                if (tile.getValue() == 0) {
                    emptyTile = tile;
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