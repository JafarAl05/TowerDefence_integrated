package be.uantwerpen.fti.ei.Game.Systems;

import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Level.Vector2;

import java.util.Collection;

/**
 * ECS-style system: it processes position/velocity data of all enemies in one place.
 */
public final class MovementSystem {
    private static final double WAYPOINT_REACHED_DISTANCE = 3.0; // tolerance (within 3 units of waypoint= waypoint reached)

    public void update(Collection<Enemy> enemies, double deltaSeconds) {//all enemies cuurently and time passed since last frame/update
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive() || enemy.hasReachedBase()) {
                continue; // if enemy is dead/has reached the base then they should not move anymore
            }
            Vector2 target = enemy.getPath().get(enemy.getTargetWaypointIndex());
            Vector2 direction = target.subtract(enemy.getPosition()); // calculate direction (enemy-> target)
            double distance = enemy.getPosition().distanceTo(target); // and the target is the waypoint
            double movementStep = enemy.getSpeed() * deltaSeconds; // how many units an enemy moves in each update

            if (distance <= movementStep || distance <= WAYPOINT_REACHED_DISTANCE) {// if enemy is close to the waypoint (and another step would make him skip this waypoint)
                enemy.setPosition(target);// enemies location is exactly at this waypoint now
                enemy.advanceWaypoint();// new target= currentwaypoint+1 (so the next waypoint)
                enemy.getVelocityComponent().setVelocity(new Vector2(0, 0)); //at this exact point (not moving in between waypoints) the velocity of the enemy becomes zero (for just one update cycle)
            } else { // enemy has not reached the waypoint
                Vector2 velocity = direction.normalized().multiply(enemy.getSpeed()); // calculate the velocity of the enemy
                enemy.getVelocityComponent().setVelocity(velocity); // store it in the enemies velocity component
                enemy.setPosition(enemy.getPosition().add(velocity.multiply(deltaSeconds))); // new position = old position + velocity * deltaTime
            }
        }
    }
}
// a waypoint is basically  a path point (one of a few in the path list for each enemy)