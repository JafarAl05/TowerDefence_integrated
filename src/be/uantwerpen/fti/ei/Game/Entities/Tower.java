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

    public double getAttacksPerSecond() {
        return attacksPerSecond;
    }

    public int getCost() {
        return cost;
    }

    public boolean canShoot() {
        return cooldownRemaining <= 0.0;
    }

    public void tickCooldown(double deltaSeconds) {
        cooldownRemaining = Math.max(0.0, cooldownRemaining - deltaSeconds);
    }

    public void resetCooldown() {
        cooldownRemaining = 1.0 / attacksPerSecond;
    }

    /**
     * Java Streams API usage: select the closest living enemy inside range.
     */
    public Optional<Enemy> findTarget(Collection<Enemy> enemies) {
        return enemies.stream()
                .filter(Enemy::isAlive)
                .filter(enemy -> getPosition().distanceTo(enemy.getPosition()) <= range)
                .min(Comparator.comparingDouble(enemy -> getPosition().distanceTo(enemy.getPosition())));
    }
}
