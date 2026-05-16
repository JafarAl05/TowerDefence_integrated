package be.uantwerpen.fti.ei.Game.Components;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

/**
 * Position data component. Entities expose this component, and systems process it.
 */
public final class PositionComponent implements Component {
    private Vector2 position;

    public PositionComponent(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
