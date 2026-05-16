package be.uantwerpen.fti.ei.Visualisation.J2D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Java2D-only image loader. The Game package never depends on this class.
 *
 * <p>If an image is missing, the renderer falls back to Java2D shapes. That
 * keeps the game playable on another computer while still supporting sprites.</p>
 */
public final class AssetManager {
    private static final Map<String, BufferedImage> IMAGES = new HashMap<>();
    private static boolean loaded = false;

    private AssetManager() {
    }

    public static synchronized void loadAll() {
        if (loaded) {
            return;
        }
        load("bullet", "resources/images/bullet.png");
        load("enemy.runner", "resources/images/viking.png");
        load("enemy.normal", "resources/images/ridder.png");
        load("enemy.armored", "resources/images/spartaan.png");
        load("tower.basic", "resources/images/shotgun.png");
        load("tower.sniper", "resources/images/sniper.png");
        load("tower.rapid", "resources/images/submachineGun.png");
        load("base", "resources/images/theWhiteHouse.png");
        loaded = true;
    }

    public static Optional<BufferedImage> get(String key) {
        loadAll();
        return Optional.ofNullable(IMAGES.get(key));
    }

    public static Map<String, BufferedImage> loadedImages() {
        loadAll();
        return Collections.unmodifiableMap(IMAGES);
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
