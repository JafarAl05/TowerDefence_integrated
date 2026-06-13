package be.uantwerpen.fti.ei.Game.Level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Logic data for one level: tile map, enemy path, and base location.
 */
public final class Level {
    private final int number;
    private final TileType[][] tiles; //tiles[row][column] -> 2D array
    private final List<Vector2> path; // list of vector2 points that enemies follow
    private final Vector2 basePosition;
    private final int tileSize;

    public Level(int number, TileType[][] tiles, List<Vector2> path, Vector2 basePosition, int tileSize) {
        this.number = number;
        this.tiles = copyTiles(tiles); //encapsulation (copying it into a new reference)
        this.path = Collections.unmodifiableList(new ArrayList<>(path)); // same and making it unmodifiable
        this.basePosition = basePosition;
        this.tileSize = tileSize;
    }

    public int getTileSize() {
        return tileSize;
    }

    public TileType[][] copyTileMap() {
        return copyTiles(tiles);
    } // to prevent modification of the original reference

    public List<Vector2> getPath() {
        return path;
    }

    public Vector2 getBasePosition() {
        return basePosition;
    }

    private static TileType[][] copyTiles(TileType[][] source) { // 2D array called source
        TileType[][] result = new TileType[source.length][source[0].length]; // Tnew empty 2D array with the same size.
        for (int row = 0; row < source.length; row++) {
            System.arraycopy(source[row], 0, result[row], 0, source[row].length); // copies each row of source into the new result array
        }
        return result;
    }
}
