package be.uantwerpen.fti.ei.Visualisation.J2D;

import be.uantwerpen.fti.ei.Game.Entities.GameEntity;
import be.uantwerpen.fti.ei.Game.Game;
import be.uantwerpen.fti.ei.Game.GameState;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public final class J2DPanel extends JPanel implements ActionListener {
    private static final int FRAME_DELAY_MS = 16;
    private final Game game;
    private final Timer timer;

    public J2DPanel(Game game, int width, int height) {
        this.game = game;
        this.timer = new Timer(FRAME_DELAY_MS, this);
        setFocusable(true);
        setBackground(Color.BLACK);
        setSize(width, height);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        game.update();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        J2DTileRenderer.draw(graphics2D, game.getTileManager());
        for (GameEntity entity : game.getEntitiesInRenderOrder()) {
            if (entity instanceof Drawable) {
                ((Drawable) entity).draw(graphics2D);
            }
        }
        if (game.getHud() instanceof Drawable) {
            ((Drawable) game.getHud()).draw(graphics2D);
        }
        drawOverlayIfNeeded(graphics2D);
        graphics2D.dispose();
    }

    private void drawOverlayIfNeeded(Graphics2D graphics2D) {
        if (game.getState() == GameState.RUNNING) {
            return;
        }
        graphics2D.setColor(new Color(0, 0, 0, 170));
        graphics2D.fillRect(0, 0, getWidth(), getHeight());
        graphics2D.setColor(Color.WHITE);
        graphics2D.setFont(new Font("SansSerif", Font.BOLD, 42));
        String text = game.getState() == GameState.VICTORY ? "VICTORY" : "GAME OVER";
        int width = graphics2D.getFontMetrics().stringWidth(text);
        graphics2D.drawString(text, (getWidth() - width) / 2, getHeight() / 2);
    }
}
