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

    public double getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public void update(double deltaSeconds) { // if the target (the enemy) is dead before the projectile reaches it, the projectile is marked dead (dissappearsà
        if (!target.isAlive()) {
            markDead();
            return;
        }
        Vector2 direction = target.getPosition().subtract(getPosition()); // calculate the direction from projectile-> target
        double distance = getPosition().distanceTo(target.getPosition());// the distance
        double step = speed * deltaSeconds; // v=d/t so it calculates how far the projectile must move (in one frame)
        if (distance <= step || distance <= 4.0) { // if the projectile can reach the enemy this frame or if the projectile is already  close to the target in this frame
            target.takeDamage(damage);
            markDead(); // disappear the projectile
        } else {
            setPosition(getPosition().add(direction.normalized().multiply(step))); // keep moving toward the target
        }
    }
}
