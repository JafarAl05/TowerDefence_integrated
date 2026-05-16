package be.uantwerpen.fti.ei;

import be.uantwerpen.fti.ei.Game.AbstractFactory;
import be.uantwerpen.fti.ei.Game.Game;
import be.uantwerpen.fti.ei.Game.GameConfig;
import be.uantwerpen.fti.ei.Game.Input.Input;
import be.uantwerpen.fti.ei.Visualisation.J2D.J2DFactory;
import be.uantwerpen.fti.ei.Visualisation.J2D.J2DPanel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Dimension;

/**
 * Application entry point. This class wires one visual implementation (Java2D)
 * to the pure game package through the AbstractFactory boundary.
 */
public  class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameConfig config = GameConfig.load("resources/config.properties");
            AbstractFactory factory = new J2DFactory();
            Game game = Game.getInstance(factory, config);

            J2DPanel panel = new J2DPanel(game, config.getWindowWidth(), config.getWindowHeight());
            Input input = game.getInput();
            panel.addKeyListener((java.awt.event.KeyListener) input);
            panel.addMouseListener((java.awt.event.MouseListener) input);
            panel.setPreferredSize(new Dimension(config.getWindowWidth(), config.getWindowHeight()));

            JFrame frame = new JFrame("Tower Defence - Clean Architecture Edition");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow();
        });
    }
}
