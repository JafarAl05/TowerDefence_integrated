package be.uantwerpen.fti.ei.Game.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Pure logic data for one level: tile map, enemy path, and base location.
 */
public final class Level {
    private final int number;
    private final TileType[][] tiles;
    private final List<Vector2> path;
    private final Vector2 basePosition;
    private final int tileSize;

    public Level(int number, TileType[][] tiles, List<Vector2> path, Vector2 basePosition, int tileSize) {
        this.number = number;
        this.tiles = copyTiles(tiles);
        this.path = Collections.unmodifiableList(new ArrayList<>(path));
        this.basePosition = basePosition;
        this.tileSize = tileSize;
    }

    public int getNumber() {
        return number;
    }

    public int getColumns() {
        return tiles[0].length;
    }

    public int getRows() {
        return tiles.length;
    }

    public int getTileSize() {
        return tileSize;
    }

    public TileType getTile(int column, int row) {
        if (row < 0 || row >= getRows() || column < 0 || column >= getColumns()) {
            return TileType.BLOCKED;
        }
        return tiles[row][column];
    }

    public TileType[][] copyTileMap() {
        return copyTiles(tiles);
    }

    public List<Vector2> getPath() {
        return path;
    }

    public Vector2 getBasePosition() {
        return basePosition;
    }

    private static TileType[][] copyTiles(TileType[][] source) {
        TileType[][] result = new TileType[source.length][source[0].length];
        for (int row = 0; row < source.length; row++) {
            System.arraycopy(source[row], 0, result[row], 0, source[row].length);
        }
        return result;
    }
}
