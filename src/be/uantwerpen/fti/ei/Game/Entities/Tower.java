package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class Tower extends GameEntity {
    private final TowerType type;
    private final double range;
    private final int damage;
    private final double attacksPerSecond;
    private final int cost;
    private double cooldownRemaining;

    public Tower(TowerType type, Vector2 position, double range, int damage, double attacksPerSecond, int cost, int updatePriority) {
        super(position, updatePriority);
        this.type = type;
        this.range = range;
        this.damage = damage;
        this.attacksPerSecond = attacksPerSecond;
        this.cost = cost;
        this.cooldownRemaining = 0.0;
    }

    public TowerType getType() {
        return type;
    }

    public double getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public boolean canShoot() {
        return cooldownRemaining <= 0.0;
    } // if the cooldown is less or equal to 0, the tower can shoot

    public void tickCooldown(double deltaSeconds) {
        cooldownRemaining = Math.max(0.0, cooldownRemaining - deltaSeconds); // this decreases the cooldown over time and prevents negative cooldown
    }

    public void resetCooldown() {
        cooldownRemaining = 1.0 / attacksPerSecond;
    } //this re(sets) the cooldown-> how much time between each shot by taking the inverse of the attacks per second

    /**
     * Java Streams API: select the closest living enemy inside range.
     */
    public Optional<Enemy> findTarget(Collection<Enemy> enemies) {
        return enemies.stream()
                .filter(Enemy::isAlive) //to ignore dead enemies
                .filter(enemy -> getPosition().distanceTo(enemy.getPosition()) <= range) //we only target enemies that are in the tower range
                .min(Comparator.comparingDouble(enemy -> getPosition().distanceTo(enemy.getPosition()))); // we select the nearest enemies (.min)
                //.min returns optional (smallest element in the stream or an empty optional if there are no enemies in the stream
    }
}
