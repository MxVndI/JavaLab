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

        if (!isNeighbor(tileRow, tileCol, emptyRow, emptyCol)) {
            return;
        }

        tile.setPosition(emptyRow, emptyCol);
        emptyTile.setPosition(tileRow, tileCol);

        Tile temp = board[emptyRow][emptyCol];
        board[emptyRow][emptyCol] = tile;
        board[tileRow][tileCol] = temp;


        notifyListeners();
        for (int i = 0; i < board.length; i++) {
            System.out.println();
            for (int j = 0; j < board[i].length; j++) {
                System.out.println(board[i][j]);
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