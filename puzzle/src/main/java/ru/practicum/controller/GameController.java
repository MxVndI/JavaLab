package ru.practicum.controller;


import ru.practicum.model.logic.GameModel;
import ru.practicum.model.tile.Tile;

public class GameController {
    private final GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public void handleTileClick(Tile tile) {
        if (isAdjacent(tile)) {
            model.swapTiles(tile);
        }
    }

    private boolean isAdjacent(Tile tile) {
        Tile empty = model.getEmptyTile();
        int rowDiff = Math.abs(tile.getRow() - empty.getRow());
        int colDiff = Math.abs(tile.getCol() - empty.getCol());
        return (rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1);
    }
}