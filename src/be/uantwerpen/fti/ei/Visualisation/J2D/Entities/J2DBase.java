package be.uantwerpen.fti.ei.Visualisation.J2D.Entities;

import be.uantwerpen.fti.ei.Game.Entities.Base;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Visualisation.J2D.AssetManager;
import be.uantwerpen.fti.ei.Visualisation.J2D.Drawable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class J2DBase extends Base implements Drawable {
    public J2DBase(Vector2 position, int maxHealth, int updatePriority) {
        super(position, maxHealth, updatePriority);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int width = 58;
        int height = 58;
        int x = (int) Math.round(getX()) - width / 2;
        int y = (int) Math.round(getY()) - height / 2;

        Optional<BufferedImage> image = AssetManager.get("base");
        if (image.isPresent()) {
            graphics2D.drawImage(image.get(), x, y, width, height, null);
        } else {
            drawFallbackBase(graphics2D, x, y, width, height);
        }

        drawHealthBar(graphics2D, x - 5, y - 14, width + 10, 8);
    }

    private void drawFallbackBase(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.setColor(new Color(210, 210, 240));
        graphics2D.fillRect(x, y + 16, width, height - 16);
        graphics2D.setColor(new Color(150, 150, 190));
        graphics2D.fillPolygon(new int[]{x - 6, x + width / 2, x + width + 6}, new int[]{y + 18, y - 8, y + 18}, 3);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y + 16, width, height - 16);
    }

    private void drawHealthBar(Graphics2D graphics2D, int x, int y, int width, int height) {
        double ratio = getHealth() / (double) getMaxHealth();
        int fillWidth = (int) Math.round(width * Math.max(0.0, Math.min(1.0, ratio)));
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(x, y, width, height);
        graphics2D.setColor(new Color(70, 190, 70));
        graphics2D.fillRect(x, y, fillWidth, height);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, width, height);
    }
}
