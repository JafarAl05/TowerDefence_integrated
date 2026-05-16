package be.uantwerpen.fti.ei.Game.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Level definitions belong to the game logic, not to Java2D.
 *
 * <p>Legend: S = spawn, P = path, X = predefined tower spot,
 * H = base, B = blocked obstacle, . = scenery.</p>
 */
public final class LevelRepository {
    private LevelRepository() {
    }

    public static Level getLevel(int requestedLevel, int tileSize) {
        if (requestedLevel == 2) {
            return createLevelTwo(tileSize);
        }
        return createLevelOne(tileSize);
    }

    private static Level createLevelOne(int tileSize) {
        String[] layout = {
                "SPPP..X.....X...",
                ".B.P....X...B...",
                "...P.X......X...",
                ".X.P....B.......",
                "...P....X..X....",
                "...PPPPPPPPPX...",
                "....X......P....",
                ".X.....B...P..X.",
                "...........P....",
                ".....X.....P.X..",
                "....X......PPPPH",
                "...B....X....B.."
        };
        int[][] pathTiles = {
                {0, 0}, {3, 0}, {3, 5}, {11, 5}, {11, 10}, {15, 10}
        };
        return buildLevel(1, layout, pathTiles, tileSize, new Vector2(15.5 * tileSize, 10.5 * tileSize));
    }

    private static Level createLevelTwo(int tileSize) {
        String[] layout = {
                "S............B..",
                "P..............B",
                "P....X....X.....",
                "PPPPPPPP..B..X..",
                "..X....P........",
                "......XP....X...",
                ".......P........",
                "..X....PPPPPPPPP",
                ".....B....X....P",
                "...............P",
                "B..............H",
                "...........B...."
        };
        int[][] pathTiles = {
                {0, 0}, {0, 3}, {7, 3}, {7, 7}, {15, 7}, {15, 10}
        };
        return buildLevel(2, layout, pathTiles, tileSize, new Vector2(15.5 * tileSize, 10.5 * tileSize));
    }

    private static Level buildLevel(int number, String[] layout, int[][] pathTiles, int tileSize, Vector2 basePosition) {
        TileType[][] tiles = new TileType[layout.length][layout[0].length()];
        for (int row = 0; row < layout.length; row++) {
            for (int column = 0; column < layout[row].length(); column++) {
                tiles[row][column] = toTile(layout[row].charAt(column));
            }
        }

        List<Vector2> path = new ArrayList<>();
        for (int[] tile : pathTiles) {
            path.add(new Vector2((tile[0] + 0.5) * tileSize, (tile[1] + 0.5) * tileSize));
        }
        return new Level(number, tiles, path, basePosition, tileSize);
    }

    private static TileType toTile(char tile) {
        switch (tile) {
            case 'S':
                return TileType.SPAWN;
            case 'P':
                return TileType.PATH;
            case 'X':
                return TileType.BUILDABLE;
            case 'H':
                return TileType.BASE;
            case 'B':
                return TileType.BLOCKED;
            case '.':
            default:
                return TileType.SCENERY;
        }
    }
}
