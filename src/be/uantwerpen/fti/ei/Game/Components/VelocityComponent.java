package be.uantwerpen.fti.ei.Game.Components;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

/**
 * Velocity data component used by the movement system.
 */
public final class VelocityComponent implements Component { // just like the posistioncomponent, we also use vector2
    private Vector2 velocity;

    public VelocityComponent() {
        this.velocity = new Vector2(0, 0);
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
// velocity.x = horizontal movement speed
//velocity.y = vertical movement speed
// In Java2D screen: (0,0) is the top left of the window.