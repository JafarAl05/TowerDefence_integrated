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
        int width = 50; // Because Java2D starts drawing from the top left and our original coordinates (vector2) point to the center of the object, we have to adjust:
        int height = 50;
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
        graphics2D.setColor(new Color(210, 210, 210));
        graphics2D.fillRect(x, y , width, height );
    }

    private void drawHealthBar(Graphics2D graphics2D, int x, int y, int width, int height) {
        double ratio = getHealth() / (double) getMaxHealth(); // calculate how full the healthbar is
        int fillWidth = (int) Math.round(width * Math.max(0.0, Math.min(1.0, ratio))); //ratio can't be negative and must be between 0 and 1
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(x, y, width, height);
        graphics2D.setColor(new Color(70, 190, 70));
        graphics2D.fillRect(x, y, fillWidth, height);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, width, height);
    }
}
