package be.uantwerpen.fti.ei.Visualisation.J2D.Entities;

import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Entities.Projectile;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Visualisation.J2D.AssetManager;
import be.uantwerpen.fti.ei.Visualisation.J2D.Drawable;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class J2DProjectile extends Projectile implements Drawable {
    public J2DProjectile(Vector2 position, Enemy target, double speed, int damage, int updatePriority) {
        super(position, target, speed, damage, updatePriority);
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        int size = 48;
        int x = (int) Math.round(getX()) - size / 2;
        int y = (int) Math.round(getY()) - size / 2;
        Optional<BufferedImage> image = AssetManager.get("bullet");
        if (image.isPresent()) {
            graphics2D.drawImage(image.get(), x, y, size, size, null);
        } else {
            graphics2D.setColor(Color.YELLOW);
            graphics2D.fillOval(x, y, size, size);
            graphics2D.setColor(Color.BLACK);
            graphics2D.drawOval(x, y, size, size);
        }
    }
}
