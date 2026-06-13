package be.uantwerpen.fti.ei.Game.Level;

/**
 * Logic-side tile manager. It decides where towers can be placed. (current tile state).
 * We need this because the map changes during the game (buildable-> occupied tile)
 */
public final class TileManager {
    private final TileType[][] tiles;
    private final int tileSize;

    public TileManager(Level level) {
        this.tiles = level.copyTileMap(); //editable copy of the original tile map in Levelrepository
        this.tileSize = level.getTileSize();
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

    public TileType getTile(int column, int row) {// returns the tile at a given grid (column and row) posistion
        if (!isInside(column, row)) {
            return TileType.BLOCKED; // outside the map
        }
        return tiles[row][column];
    }

    public boolean isBuildableWorldPosition(Vector2 worldPosition) {
        int column = worldToColumn(worldPosition.getX()); // we get the column/row conversion of the vector2 posisition
        int row = worldToRow(worldPosition.getY());
        return isInside(column, row) && tiles[row][column] == TileType.BUILDABLE;
        //its  true if the tile is inside the map AND the selected tile is buildable
    }

    public Vector2 centerOfWorldTile(Vector2 worldPosition) {
        int column = worldToColumn(worldPosition.getX());
        int row = worldToRow(worldPosition.getY());
        return new Vector2((column + 0.5) * tileSize, (row + 0.5) * tileSize);
        // takes a clicked position and returns the center of the tile (as vector2 coordinates)
    }

    public void markOccupied(Vector2 worldPosition) {
        int column = worldToColumn(worldPosition.getX());
        int row = worldToRow(worldPosition.getY());
        if (isInside(column, row) && tiles[row][column] == TileType.BUILDABLE) {
            tiles[row][column] = TileType.OCCUPIED;
        //called after a tower is built (converts the tile into occupied)
        }
    }

    private int worldToColumn(double worldX) {
        return (int) Math.floor(worldX / tileSize);
    } // converts X coordinate to column

    private int worldToRow(double worldY) {
        return (int) Math.floor(worldY / tileSize);
    }
    // converts Y coordinate to row

    private boolean isInside(int column, int row) {
        return row >= 0 && row < getRows() && column >= 0 && column < getColumns();
        //checks if tile coordinate exists in the map
    }
}
