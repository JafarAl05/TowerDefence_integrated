package be.uantwerpen.fti.ei.Game.Level;

import java.util.Objects;

/**
 * Immutable doubles world coordinate.
 */
public final class Vector2 {
    private final double x;
    private final double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }// position + movement step= new positon

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }
    // to calculate direction betwee current postion and target position  (for projectile for example)

    public Vector2 multiply(double factor) {
        return new Vector2(x * factor, y * factor);
    }
    //direction * moving distance -> calculate total direction change in x and y coordinates

    public double distanceTo(Vector2 other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
        // Pythagorean theorem to calculate distance between two positions
    }

    public Vector2 normalized() {
        //we use this for movement because the length(distance) of the normalized vector becomes 1 and we can use this to make
        // things move at a constant speed (movement = normalized direction * speed * deltaTime)

        double length = Math.sqrt(x * x + y * y);
        if (length <= 0.000_001) {
            return new Vector2(0, 0);
        }
        return new Vector2(x / length, y / length);
    }

    @Override
    public boolean equals(Object object) { // to check if two vectors (represented as objects) are equal to each other
        if (this == object) { // if this object = object(so referencing to the SAME object), then obviously they are equal
            return true;
        }
        if (!(object instanceof Vector2)) {// if one object is not a vecotr2 object, then its false
            return false;
        }
        Vector2 vector2 = (Vector2) object; // if you have two different vector 2 objects, compare x and y components
        return Double.compare(vector2.x, x) == 0 && Double.compare(vector2.y, y) == 0; // i used double.compare instead of == because its cleaner

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
