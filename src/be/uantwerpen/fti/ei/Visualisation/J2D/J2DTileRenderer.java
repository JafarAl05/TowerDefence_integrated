package be.uantwerpen.fti.ei.Visualisation.J2D;

import be.uantwerpen.fti.ei.Game.Level.TileManager;
import be.uantwerpen.fti.ei.Game.Level.TileType;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

/** Draws a logic-side TileManager. The tile data itself remains in the Game package. */
public final class J2DTileRenderer {
    private J2DTileRenderer() {
    }

    public static void draw(Graphics2D graphics2D, TileManager tileManager) {
        int tileSize = tileManager.getTileSize();
        for (int row = 0; row < tileManager.getRows(); row++) {
            for (int column = 0; column < tileManager.getColumns(); column++) {
                drawTile(graphics2D, tileManager.getTile(column, row), column * tileSize, row * tileSize, tileSize);
            }
        }
    }

    private static void drawTile(Graphics2D graphics2D, TileType tileType, int x, int y, int size) {
        switch (tileType) {
            case PATH:
            case SPAWN:
                graphics2D.setColor(new Color(188, 160, 110));
                break;
            case BUILDABLE:
                graphics2D.setColor(new Color(80, 155, 82));
                break;
            case OCCUPIED:
                graphics2D.setColor(new Color(65, 110, 70));
                break;
            case BASE:
                graphics2D.setColor(new Color(120, 120, 170));
                break;
            case BLOCKED:
                graphics2D.setColor(new Color(80, 85, 90));
                break;
            case SCENERY:
            default:
                graphics2D.setColor(new Color(48, 128, 67));
                break;
        }
        graphics2D.fillRect(x, y, size, size);
        graphics2D.setColor(new Color(0, 0, 0, 45));
        graphics2D.drawRect(x, y, size, size);

        if (tileType == TileType.BUILDABLE) {
            drawBuildSpotMarker(graphics2D, x, y, size);
        } else if (tileType == TileType.OCCUPIED) {
            drawOccupiedMarker(graphics2D, x, y, size);
        } else if (tileType == TileType.BLOCKED) {
            graphics2D.setColor(new Color(40, 40, 40, 90));
            graphics2D.fillOval(x + size / 4, y + size / 4, size / 2, size / 2);
        }
    }

    private static void drawBuildSpotMarker(Graphics2D graphics2D, int x, int y, int size) {
        Stroke oldStroke = graphics2D.getStroke();
        graphics2D.setColor(new Color(255, 255, 255, 160));
        graphics2D.setStroke(new BasicStroke(2.0f));
        int margin = 8;
        graphics2D.drawRoundRect(x + margin, y + margin, size - margin * 2, size - margin * 2, 12, 12);
        graphics2D.drawLine(x + size / 2, y + margin + 5, x + size / 2, y + size - margin - 5);
        graphics2D.drawLine(x + margin + 5, y + size / 2, x + size - margin - 5, y + size / 2);
        graphics2D.setStroke(oldStroke);
    }

    private static void drawOccupiedMarker(Graphics2D graphics2D, int x, int y, int size) {
        graphics2D.setColor(new Color(0, 0, 0, 80));
        graphics2D.fillRoundRect(x + 6, y + 6, size - 12, size - 12, 10, 10);
    }
}
