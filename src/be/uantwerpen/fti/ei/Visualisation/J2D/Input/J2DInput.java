package be.uantwerpen.fti.ei.Visualisation.J2D.Input;

import be.uantwerpen.fti.ei.Game.Input.Input;
import be.uantwerpen.fti.ei.Game.Level.Vector2;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;

/**
 * Java2D/Swing input implementation. The Game package only sees the Input interface.
 */
public final class J2DInput implements Input, KeyListener, MouseListener {
    private boolean nextTowerRequested;// temporary request flags: true/false
    private boolean previousTowerRequested;
    private boolean repairRequested;
    private Optional<Vector2> buildRequest; // build request is a container object: stores latest clicked position

    public J2DInput() {
        this.nextTowerRequested = false;
        this.previousTowerRequested = false;
        this.repairRequested = false;
        this.buildRequest = Optional.empty();
    }
    //we use the result boolean to store the values of the requests before we reset them (back to false)
    @Override
    public boolean consumeNextTowerRequest() {
        boolean result = nextTowerRequested;
        nextTowerRequested = false;
        return result;
    }

    @Override
    public boolean consumePreviousTowerRequest() {
        boolean result = previousTowerRequested;
        previousTowerRequested = false;
        return result;
    }

    @Override
    public boolean consumeRepairRequest() {
        boolean result = repairRequested;
        repairRequested = false;
        return result;
    }

    //here, we  use the result vector to store the values of the requests before we reset them (back to empty)
    @Override
    public Optional<Vector2> consumeBuildRequest() {
        Optional<Vector2> result = buildRequest;
        buildRequest = Optional.empty();
        return result;
    }



    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            nextTowerRequested = true;
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            previousTowerRequested = true;
        } else if (event.getKeyCode() == KeyEvent.VK_H) {
            repairRequested = true;
        }
    }
    //we dont really use following key events but we have to implement them as keylistener requires the three methods
    @Override
    public void keyTyped(KeyEvent event) {
    }
    @Override
    public void keyReleased(KeyEvent event) {
    }
    //we get the click coordinates inside the panel and in game.java we then try to build here
    @Override
    public void mouseClicked(MouseEvent event) {

        buildRequest = Optional.of(new Vector2(event.getX(), event.getY()));
    }


    @Override
    public void mousePressed(MouseEvent event) {
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }
}

