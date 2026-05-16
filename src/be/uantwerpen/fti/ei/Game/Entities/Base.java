package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

public class Base extends GameEntity {
    private final int maxHealth;
    private int health;

    public Base(Vector2 position, int maxHealth, int updatePriority) {
        super(position, updatePriority);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - Math.max(0, damage));
        if (health == 0) {
            markDead();
        }
    }

    public void repair(int amount) {
        health = Math.min(maxHealth, health + Math.max(0, amount));
    }
}
