package be.uantwerpen.fti.ei.Game.Input;

import be.uantwerpen.fti.ei.Game.Level.Vector2;

import java.util.Optional;

/**
 * Logic-facing input contract. It deliberately has no dependency on Java2D/Swing.
 */
public interface Input {
    boolean consumeNextTowerRequest();

    boolean consumePreviousTowerRequest();

    boolean consumeRepairRequest();

    Optional<Vector2> consumeBuildRequest();
}
