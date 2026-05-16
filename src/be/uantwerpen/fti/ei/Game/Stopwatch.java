package be.uantwerpen.fti.ei.Game;

/**
 * Simple stopwatch used to make movement time-based instead of frame-based.
 */
public final class Stopwatch {
    private long previousTickNanos;

    public Stopwatch() {
        previousTickNanos = System.nanoTime();
    }

    public double tickSeconds() {
        long now = System.nanoTime();
        double deltaSeconds = (now - previousTickNanos) / 1_000_000_000.0;
        previousTickNanos = now;
        return Math.min(deltaSeconds, 0.05); // avoid huge jumps after window pauses
    }
}
