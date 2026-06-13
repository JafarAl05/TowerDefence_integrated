package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.Components.PositionComponent;
import be.uantwerpen.fti.ei.Game.Level.Vector2;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared logic base class for all game entities. This class contains no visual code.
 */
public abstract class GameEntity implements Comparable<GameEntity> {
    private static final AtomicLong NEXT_ID = new AtomicLong(1); // to give each 'sub'entity an ID (useful for priorities)

    private final long id;
    private final PositionComponent positionComponent;
    private final int updatePriority; // priority of the entities (used to decide wich entitites must be drawn/ processed first/later)
    private boolean alive;

    protected GameEntity(Vector2 position, int updatePriority) { // protected constructor -> only this class and child classes can call it (super(..,..)
        this.id = NEXT_ID.getAndIncrement(); // so a next entity gets this ID +1
        this.positionComponent = new PositionComponent(position);
        this.updatePriority = updatePriority;
        this.alive = true;
    }

    public Vector2 getPosition() {
        return positionComponent.getPosition();
    }

    public double getX() {
        return positionComponent.getX();
    }

    public double getY() {
        return positionComponent.getY();
    }

    public void setPosition(Vector2 position) {
        positionComponent.setPosition(position);
    }

    public int getUpdatePriority() {
        return updatePriority;
    }

    public boolean isAlive() {
        return alive;
    }

    public void markDead() {
        alive = false;
    }

    @Override
    public int compareTo(GameEntity other) { // if priorities are the same, we compare by ID
        int priorityCompare = Integer.compare(updatePriority, other.updatePriority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return Long.compare(id, other.id);
    }
}
