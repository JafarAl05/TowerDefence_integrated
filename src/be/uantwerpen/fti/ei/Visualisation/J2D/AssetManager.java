package be.uantwerpen.fti.ei.Visualisation.J2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Java2D-only image loader.
 *
 * <p>If an image is missing, the renderer falls back to Java2D shapes. That
 * keeps the game playable on another computer while still supporting sprites.</p>
 */
public final class AssetManager {
    private static final Map<String, BufferedImage> IMAGES = new HashMap<>(); // stores images using keys with the values being images
    private static boolean loaded = false; // a flag that lakes sure images are not loaded multiple times

    private AssetManager() {
    }

    public static void loadAll() {
        if (loaded) { // images are already loaded
            return;
        }
        load("bullet", "resources/images/bullet.png");  // maps a name (key) to an actual image
        load("enemy.viking", "resources/images/viking.png");
        load("enemy.ridder", "resources/images/ridder.png");
        load("enemy.spartaan", "resources/images/spartaan.png");
        load("tower.shotgun", "resources/images/shotgun.png");
        load("tower.sniper", "resources/images/sniper.png");
        load("tower.SMG", "resources/images/submachineGun.png");
        load("base", "resources/images/theWhiteHouse.png");
        loaded = true; // now all images are loaded
    }

    public static Optional<BufferedImage> get(String key) {
        loadAll();
        return Optional.ofNullable(IMAGES.get(key)); // returns image if it exists otherwise returns empty optional
    }

    private static void load(String key, String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            if (image != null) {
                IMAGES.put(key, image);
            }
        } catch (IOException exception) {
            System.err.println("Image not found: " + path + " (fallback drawing will be used)");
        }
    }
}
