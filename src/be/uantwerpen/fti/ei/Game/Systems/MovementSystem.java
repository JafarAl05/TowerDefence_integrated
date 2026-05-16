package be.uantwerpen.fti.ei.Game.Systems;

import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Level.Vector2;

import java.util.Collection;

/**
 * ECS-style system: it processes position/velocity data of all enemies in one place.
 */
public final class MovementSystem {
    private static final double WAYPOINT_REACHED_DISTANCE = 3.0;

    public void update(Collection<Enemy> enemies, double deltaSeconds) {
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive() || enemy.hasReachedBase()) {
                continue;
            }
            Vector2 target = enemy.getPath().get(enemy.getTargetWaypointIndex());
            Vector2 direction = target.subtract(enemy.getPosition());
            double distance = enemy.getPosition().distanceTo(target);
            double movementStep = enemy.getSpeed() * deltaSeconds;

            if (distance <= movementStep || distance <= WAYPOINT_REACHED_DISTANCE) {
                enemy.setPosition(target);
                enemy.advanceWaypoint();
                enemy.getVelocityComponent().setVelocity(new Vector2(0, 0));
            } else {
                Vector2 velocity = direction.normalized().multiply(enemy.getSpeed());
                enemy.getVelocityComponent().setVelocity(velocity);
                enemy.setPosition(enemy.getPosition().add(velocity.multiply(deltaSeconds)));
            }
        }
    }
}
