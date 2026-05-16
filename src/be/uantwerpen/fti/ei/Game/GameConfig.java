package be.uantwerpen.fti.ei.Game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads game parameters from resources/config.properties.
 */
public final class GameConfig {
    private final Properties properties;

    private GameConfig(Properties properties) {
        this.properties = properties;
    }

    public static GameConfig load(String path) {
        Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream(path)) {
            properties.load(inputStream);
        } catch (IOException exception) {
            System.err.println("Could not load config file '" + path + "'. Using safe defaults. Reason: " + exception.getMessage());
        }
        return new GameConfig(properties);
    }

    public int getWindowWidth() {
        return getInt("window.width", 900);
    }

    public int getWindowHeight() {
        return getInt("window.height", 650);
    }

    public int getTileSize() {
        return getInt("tile.size", 50);
    }

    public int getSelectedLevel() {
        return getInt("selected.level", 1);
    }

    public int getStartingLives() {
        return getInt("starting.lives", 20);
    }

    public int getStartingGold() {
        return getInt("starting.gold", 175);
    }

    public int getMaxWaves() {
        return getInt("max.waves", 5);
    }

    public int getBaseEnemyCount() {
        return getInt("base.enemy.count", 6);
    }

    public double getSpawnIntervalSeconds() {
        return getDouble("spawn.interval.seconds", 0.85);
    }

    public String getLuaScriptPath() {
        return properties.getProperty("script.path", "scripts/waves.lua");
    }

    private int getInt(String key, int fallback) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(fallback)).trim());
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }

    private double getDouble(String key, double fallback) {
        try {
            return Double.parseDouble(properties.getProperty(key, String.valueOf(fallback)).trim());
        } catch (NumberFormatException exception) {
            return fallback;
        }
    }
}
