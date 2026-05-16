package be.uantwerpen.fti.ei.Game.Level;

/**
 * Logic-side tile manager. It decides where towers can be placed.
 * The visual package only reads this data to draw it.
 */
public final class TileManager {
    private final TileType[][] tiles;
    private final int tileSize;

    public TileManager(Level level) {
        this.tiles = level.copyTileMap();
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

    public TileType getTile(int column, int row) {
        if (!isInside(column, row)) {
            return TileType.BLOCKED;
        }
        return tiles[row][column];
    }

    public boolean isBuildableWorldPosition(Vector2 worldPosition) {
        int column = worldToColumn(worldPosition.getX());
        int row = worldToRow(worldPosition.getY());
        return isInside(column, row) && tiles[row][column] == TileType.BUILDABLE;
    }

    public Vector2 centerOfWorldTile(Vector2 worldPosition) {
        int column = worldToColumn(worldPosition.getX());
        int row = worldToRow(worldPosition.getY());
        return new Vector2((column + 0.5) * tileSize, (row + 0.5) * tileSize);
    }

    public void markOccupied(Vector2 worldPosition) {
        int column = worldToColumn(worldPosition.getX());
        int row = worldToRow(worldPosition.getY());
        if (isInside(column, row) && tiles[row][column] == TileType.BUILDABLE) {
            tiles[row][column] = TileType.OCCUPIED;
        }
    }

    private int worldToColumn(double worldX) {
        return (int) Math.floor(worldX / tileSize);
    }

    private int worldToRow(double worldY) {
        return (int) Math.floor(worldY / tileSize);
    }

    private boolean isInside(int column, int row) {
        return row >= 0 && row < getRows() && column >= 0 && column < getColumns();
    }
}
