package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

public class Projectile extends GameEntity {
    private final Enemy target;
    private final double speed;
    private final int damage;

    public Projectile(Vector2 position, Enemy target, double speed, int damage, int updatePriority) {
        super(position, updatePriority);
        this.target = target;
        this.speed = speed;
        this.damage = damage;
    }

    public Enemy getTarget() {
        return target;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public void update(double deltaSeconds) {
        if (!target.isAlive()) {
            markDead();
            return;
        }
        Vector2 direction = target.getPosition().subtract(getPosition());
        double distance = getPosition().distanceTo(target.getPosition());
        double step = speed * deltaSeconds;
        if (distance <= step || distance <= 4.0) {
            target.takeDamage(damage);
            markDead();
        } else {
            setPosition(getPosition().add(direction.normalized().multiply(step)));
        }
    }
}
