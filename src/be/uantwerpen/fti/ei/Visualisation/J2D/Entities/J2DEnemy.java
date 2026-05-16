package be.uantwerpen.fti.ei.Visualisation.J2D.Entities;

import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.EnemyType;
import be.uantwerpen.fti.ei.Visualisation.J2D.AssetManager;
import be.uantwerpen.fti.ei.Visualisation.J2D.Drawable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;

public final class J2DEnemy extends Enemy implements Drawable {
    public J2DEnemy(EnemyType type, Vector2 spawnPosition, List<Vector2> path, double speed, int maxHealth,
                    int rewardGold, int rewardScore, int baseDamage, int updatePriority) {
        super(type, spawnPosition, path, speed, maxHealth, rewardGold, rewardScore, baseDamage, updatePriority);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int size = sizeForType();
        int x = (int) Math.round(getX()) - size / 2;
        int y = (int) Math.round(getY()) - size / 2;

        Optional<BufferedImage> image = AssetManager.get(assetKeyForType());
        if (image.isPresent()) {
            graphics2D.drawImage(image.get(), x, y, size, size, null);
        } else {
            drawFallbackEnemy(graphics2D, x, y, size);
        }

        drawHealthBar(graphics2D, x, y - 10, size, 6);
    }

    private void drawFallbackEnemy(Graphics2D graphics2D, int x, int y, int size) {
        graphics2D.setColor(colorForType());
        graphics2D.fillOval(x, y, size, size);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawOval(x, y, size, size);
    }

    private void drawHealthBar(Graphics2D graphics2D, int x, int y, int width, int height) {
        double ratio = getHealth() / (double) getMaxHealth();
        int fillWidth = (int) Math.round(width * Math.max(0.0, Math.min(1.0, ratio)));
        graphics2D.setColor(Color.DARK_GRAY);
        graphics2D.fillRect(x, y, width, height);
        graphics2D.setColor(new Color(210, 45, 45));
        graphics2D.fillRect(x, y, fillWidth, height);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawRect(x, y, width, height);
    }

    private String assetKeyForType() {
        switch (getType()) {
            case RUNNER:
                return "enemy.runner";
            case ARMORED:
                return "enemy.armored";
            case GOBLIN:
            default:
                return "enemy.normal";
        }
    }

    private int sizeForType() {
        switch (getType()) {
            case RUNNER:
                return 30;
            case ARMORED:
                return 46;
            case GOBLIN:
            default:
                return 36;
        }
    }

    private Color colorForType() {
        switch (getType()) {
            case RUNNER:
                return new Color(230, 215, 70);
            case ARMORED:
                return new Color(120, 120, 120);
            case GOBLIN:
            default:
                return new Color(190, 65, 65);
        }
    }
}
