package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.Components.VelocityComponent;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.EnemyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Enemy extends GameEntity {
    private final EnemyType type;
    private final VelocityComponent velocityComponent;
    private final List<Vector2> path;
    private final double speed;
    private final int maxHealth;
    private final int rewardGold;
    private final int rewardScore;
    private final int baseDamage;
    private int health;
    private int targetWaypointIndex;
    private boolean reachedBase;

    public Enemy(EnemyType type, Vector2 spawnPosition, List<Vector2> path, double speed, int maxHealth,
                 int rewardGold, int rewardScore, int baseDamage, int updatePriority) {
        super(spawnPosition, updatePriority);
        this.type = type;
        this.velocityComponent = new VelocityComponent();
        this.path = Collections.unmodifiableList(new ArrayList<>(path)); // we copy the path (list of points) and make it into an unmodifiable list
        this.speed = speed;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.rewardGold = rewardGold;
        this.rewardScore = rewardScore;
        this.baseDamage = baseDamage;
        this.targetWaypointIndex = Math.min(1, Math.max(0, path.size() - 1));// enemies spawn at index 0 and so the next index should not be less than one
        this.reachedBase = false;
    }
    // getters used by other files
    public EnemyType getType() {
        return type;
    }

    public VelocityComponent getVelocityComponent() {
        return velocityComponent;
    }

    public List<Vector2> getPath() {
        return path;
    }

    public double getSpeed() {
        return speed;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public int getRewardScore() {
        return rewardScore;
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public int getTargetWaypointIndex() {
        return targetWaypointIndex;
    }

    public void advanceWaypoint() {
        if (targetWaypointIndex < path.size() - 1) {
            targetWaypointIndex++;
        } else {
            reachedBase = true;
        }
    }

    public boolean hasReachedBase() {
        return reachedBase;
    } // game.java will deduct health from base based on this condition

    public void takeDamage(int damage) { // basically the same as Base
        health = Math.max(0, health - Math.max(0, damage));
        if (health == 0) {
            markDead();
        }
    }
}
