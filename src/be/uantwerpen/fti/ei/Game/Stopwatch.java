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
        long now = System.nanoTime(); // current time (highly accurate) in nanosecondds
        double deltaSeconds = (now - previousTickNanos) / 1_000_000_000.0; // elapsed time from the time now back to the previous tick (and convert to seconds)
        previousTickNanos = now; // repeat the cycle: "now" becomes the new previous tick
        return Math.min(deltaSeconds, 0.05); // avoid huge jumps after window pauses (update uses at most 0.05 seconds)
    }
}
