package be.uantwerpen.fti.ei.Game.Components;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

/**
 * Position data component. Entities expose this component, and systems process it.
 */
public final class PositionComponent implements Component {
    private Vector2 position; // we use vector2 instead of loose x and y's

    public PositionComponent(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    } // get the current position

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    } // set the current position (for when an entity is moving for example) (can be used my movementsystem)
}
