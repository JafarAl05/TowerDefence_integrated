package be.uantwerpen.fti.ei.Game.Level;

import java.util.Objects;

/**
 * Immutable double-precision world coordinate.
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
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }

    public Vector2 multiply(double factor) {
        return new Vector2(x * factor, y * factor);
    }

    public double distanceTo(Vector2 other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public Vector2 normalized() {
        double length = Math.sqrt(x * x + y * y);
        if (length <= 0.000_001) {
            return new Vector2(0, 0);
        }
        return new Vector2(x / length, y / length);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Vector2)) {
            return false;
        }
        Vector2 vector2 = (Vector2) object;
        return Double.compare(vector2.x, x) == 0 && Double.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
