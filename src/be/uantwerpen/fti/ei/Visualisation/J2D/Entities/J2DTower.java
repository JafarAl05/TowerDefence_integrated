package be.uantwerpen.fti.ei.Visualisation.J2D.Entities;

import be.uantwerpen.fti.ei.Game.Entities.Tower;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;
import be.uantwerpen.fti.ei.Visualisation.J2D.AssetManager;
import be.uantwerpen.fti.ei.Visualisation.J2D.Drawable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class J2DTower extends Tower implements Drawable {
    public J2DTower(TowerType type, Vector2 position, double range, int damage, double attacksPerSecond, int cost, int updatePriority) {
        super(type, position, range, damage, attacksPerSecond, cost, updatePriority);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int size = 42;
        int x = (int) Math.round(getX()) - size / 2;
        int y = (int) Math.round(getY()) - size / 2;

        graphics2D.setColor(new Color(0, 0, 0, 28)); // draws a transparant circle (range around the rower)= tower range
        int rangeDiameter = (int) Math.round(getRange() * 2.0); // range is the radius and so the diameter=range*2
        graphics2D.fillOval((int) Math.round(getX() - getRange()), (int) Math.round(getY() - getRange()), rangeDiameter, rangeDiameter);

        Optional<BufferedImage> image = AssetManager.get(assetKeyForType());
        if (image.isPresent()) {
            graphics2D.drawImage(image.get(), x, y, size, size, null);
        } else {
            drawFallbackTower(graphics2D, x, y, size);
        }
    }

    private void drawFallbackTower(Graphics2D graphics2D, int x, int y, int size) {
        graphics2D.setColor(colorForType());
        graphics2D.fillRect(x, y, size, size);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, size, size);
    }

    private String assetKeyForType() {
        switch (getType()) {
            case SNIPER:
                return "tower.sniper";
            case SMG:
                return "tower.SMG";
            case Shotgun:
            default:
                return "tower.shotgun";
        }
    }

    private Color colorForType() {
        switch (getType()) {
            case SNIPER:
                return new Color(70, 100, 190);
            case SMG:
                return new Color(225, 150, 45);
            case Shotgun:
            default:
                return new Color(80, 80, 155);
        }
    }
}
