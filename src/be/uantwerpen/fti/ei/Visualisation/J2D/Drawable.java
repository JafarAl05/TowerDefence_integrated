package be.uantwerpen.fti.ei.Visualisation.J2D;

import java.awt.Graphics2D;

public interface Drawable {
    void draw(Graphics2D graphics2D);
}
// Any class that implements Drawable can draw itself using Graphics2D.