package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.Components.PositionComponent;
import be.uantwerpen.fti.ei.Game.Level.Vector2;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Shared logic base class for all game entities. This class contains no visual code.
 */
public abstract class GameEntity implements Comparable<GameEntity> {
    private static final AtomicLong NEXT_ID = new AtomicLong(1);

    private final long id;
    private final PositionComponent positionComponent;
    private final int updatePriority;
    private boolean alive;

    protected GameEntity(Vector2 position, int updatePriority) {
        this.id = NEXT_ID.getAndIncrement();
        this.positionComponent = new PositionComponent(position);
        this.updatePriority = updatePriority;
        this.alive = true;
    }

    public long getId() {
        return id;
    }

    public PositionComponent getPositionComponent() {
        return positionComponent;
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
    public int compareTo(GameEntity other) {
        int priorityCompare = Integer.compare(updatePriority, other.updatePriority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        return Long.compare(id, other.id);
    }
}
