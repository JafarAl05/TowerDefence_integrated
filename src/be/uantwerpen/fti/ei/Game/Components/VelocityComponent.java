package be.uantwerpen.fti.ei.Game.Components;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

/**
 * Velocity data component used by the movement system.
 */
public final class VelocityComponent implements Component {
    private Vector2 velocity;

    public VelocityComponent() {
        this.velocity = new Vector2(0, 0);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
