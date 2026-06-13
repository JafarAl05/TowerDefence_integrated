package be.uantwerpen.fti.ei.Game.Entities;

import be.uantwerpen.fti.ei.Game.GameState;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;

public class HUD {
    private int score;
    private int gold;
    private int lives;
    private int wave;
    private int maxWaves;
    private TowerType selectedTowerType;
    private GameState gameState;

    public void update(int score, int gold, int lives, int wave, int maxWaves, TowerType selectedTowerType, GameState gameState) {
        this.score = score;
        this.gold = gold;
        this.lives = lives;
        this.wave = wave;
        this.maxWaves = maxWaves;
        this.selectedTowerType = selectedTowerType;
        this.gameState = gameState;
    }

    public int getScore() {
        return score;
    }

    public int getGold() {
        return gold;
    }

    public int getLives() {
        return lives;
    }

    public int getWave() {
        return wave;
    }

    public int getMaxWaves() {
        return maxWaves;
    }

    public TowerType getSelectedTowerType() {
        return selectedTowerType;
    }

}
