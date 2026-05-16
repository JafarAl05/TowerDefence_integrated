package be.uantwerpen.fti.ei.Game.Systems;

import be.uantwerpen.fti.ei.Game.AbstractFactory;
import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.GameConfig;
import be.uantwerpen.fti.ei.Game.Level.Level;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.EnemyStats;
import be.uantwerpen.fti.ei.Game.Rules.EnemyType;
import be.uantwerpen.fti.ei.Game.Scripting.LuaWaveScript;

import java.util.List;
import java.util.function.Consumer;

/**
 * Creates waves and enemies. All wave/gameplay calculations happen here, not in J2DFactory.
 */
public final class WaveManager {
    private final AbstractFactory factory;
    private final GameConfig config;
    private final Level level;
    private final Consumer<Enemy> enemyConsumer;
    private final LuaWaveScript luaWaveScript;
    private int currentWave;
    private int enemiesToSpawn;
    private double spawnTimer;
    private boolean waveActive;
    private boolean allWavesStarted;

    public WaveManager(AbstractFactory factory, GameConfig config, Level level, Consumer<Enemy> enemyConsumer) {
        this.factory = factory;
        this.config = config;
        this.level = level;
        this.enemyConsumer = enemyConsumer;
        this.luaWaveScript = new LuaWaveScript(config.getLuaScriptPath());
        this.currentWave = 0;
        this.enemiesToSpawn = 0;
        this.spawnTimer = 0.0;
        this.waveActive = false;
        this.allWavesStarted = false;
    }

    public void update(double deltaSeconds, boolean enemiesStillAlive) {
        if (allWavesStarted) {
            return;
        }
        if (!waveActive && !enemiesStillAlive) {
            startNextWave();
        }
        if (waveActive) {
            spawnTimer -= deltaSeconds;
            if (spawnTimer <= 0.0 && enemiesToSpawn > 0) {
                spawnEnemy();
                enemiesToSpawn--;
                spawnTimer = config.getSpawnIntervalSeconds();
            }
            if (enemiesToSpawn == 0) {
                waveActive = false;
                if (currentWave >= config.getMaxWaves()) {
                    allWavesStarted = true;
                }
            }
        }
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public boolean areAllWavesStarted() {
        return allWavesStarted;
    }

    private void startNextWave() {
        currentWave++;
        if (currentWave > config.getMaxWaves()) {
            allWavesStarted = true;
            return;
        }
        enemiesToSpawn = config.getBaseEnemyCount() + (currentWave - 1) * 2;
        spawnTimer = 0.0;
        waveActive = true;
    }

    private void spawnEnemy() {
        EnemyType type = chooseEnemyType();
        EnemyStats stats = EnemyStats.forType(type);
        List<Vector2> path = level.getPath();
        Vector2 spawn = path.get(0);
        double speed = stats.getBaseSpeed() * luaWaveScript.speedMultiplier(currentWave);
        int health = (int) Math.round(stats.getBaseHealth() * luaWaveScript.healthMultiplier(currentWave));
        int rewardGold = (int) Math.round(stats.getRewardGold() * luaWaveScript.rewardMultiplier(currentWave));
        int rewardScore = (int) Math.round(stats.getRewardScore() * luaWaveScript.rewardMultiplier(currentWave));

        enemyConsumer.accept(factory.createEnemy(
                type,
                spawn,
                path,
                speed,
                health,
                rewardGold,
                rewardScore,
                stats.getBaseDamage(),
                20
        ));
    }

    private EnemyType chooseEnemyType() {
        if (currentWave >= 4 && enemiesToSpawn % 4 == 0) {
            return EnemyType.ARMORED;
        }
        if (currentWave >= 2 && enemiesToSpawn % 3 == 0) {
            return EnemyType.RUNNER;
        }
        return EnemyType.GOBLIN;
    }
}
