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
 * This class connects Java2D (the visiual implementation)
 * to the game package through the AbstractFactory boundary.
 */
public  class Main {
    public static void main() {
        SwingUtilities.invokeLater(() -> {
            GameConfig config = GameConfig.load("resources/config.properties"); //we load the configurations with our GameConfig class.
            AbstractFactory factory = new J2DFactory(); //Java2D as an abstract factory
            Game game = Game.getInstance(factory, config); // singleton (one active game object)

            J2DPanel panel = new J2DPanel(game, config.getWindowWidth(), config.getWindowHeight());
            Input input = game.getInput(); // get game input to "connect " it to key/mouse inputs
            panel.addKeyListener((java.awt.event.KeyListener) input);
            panel.addMouseListener((java.awt.event.MouseListener) input);
            panel.setPreferredSize(new Dimension(config.getWindowWidth(), config.getWindowHeight())); // panel dimensions are in config file

            JFrame frame = new JFrame("Tower Defence Jafar");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);// our game map is based on fixed dimensions of the panel
            frame.add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            panel.requestFocusInWindow();
        });
    }
}
