package be.uantwerpen.fti.ei.Game;

import be.uantwerpen.fti.ei.Game.Entities.Base;
import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Entities.GameEntity;
import be.uantwerpen.fti.ei.Game.Entities.HUD;
import be.uantwerpen.fti.ei.Game.Entities.Projectile;
import be.uantwerpen.fti.ei.Game.Entities.Tower;
import be.uantwerpen.fti.ei.Game.Input.Input;
import be.uantwerpen.fti.ei.Game.Level.Level;
import be.uantwerpen.fti.ei.Game.Level.LevelRepository;
import be.uantwerpen.fti.ei.Game.Level.TileManager;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.TowerStats;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;
import be.uantwerpen.fti.ei.Game.Systems.MovementSystem;
import be.uantwerpen.fti.ei.Game.Systems.WaveManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Singleton game object. It owns all gameplay state and the main update loop.
 *
 * <p>This class contains no visualization imports and no keyboard/mouse event
 * imports. It communicates with the chosen presentation through AbstractFactory
 * and through the logic-facing Input interface.</p>
 */
public final class Game {
    private static Optional<Game> instance = Optional.empty();

    private final AbstractFactory factory;
    private final GameConfig config;
    private final Stopwatch stopwatch;
    private final MovementSystem movementSystem;
    private final WaveManager waveManager;
    private final Level level;
    private final TileManager tileManager;
    private final Input input;
    private final Base base;
    private final HUD hud;
    private final List<Enemy> enemies;
    private final List<Tower> towers;
    private final List<Projectile> projectiles;
    private final List<TowerType> towerSelectionOrder;
    private GameState state;
    private int selectedTowerIndex;
    private int gold;
    private int score;

    private Game(AbstractFactory factory, GameConfig config) {
        this.factory = factory;
        this.config = config;
        this.stopwatch = new Stopwatch();
        this.movementSystem = new MovementSystem();
        this.level = LevelRepository.getLevel(config.getSelectedLevel(), config.getTileSize());
        this.tileManager = new TileManager(level);
        this.input = factory.createInput();
        this.base = factory.createBase(level.getBasePosition(), config.getStartingLives(), 10);
        this.hud = factory.createHUD();
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.towerSelectionOrder = Collections.unmodifiableList(Arrays.asList(TowerType.BASIC, TowerType.SNIPER, TowerType.RAPID));
        this.selectedTowerIndex = 0;
        this.gold = config.getStartingGold();
        this.score = 0;
        this.state = GameState.RUNNING;
        this.waveManager = new WaveManager(factory, config, level, enemies::add);
        updateHud();
    }

    public static synchronized Game getInstance(AbstractFactory factory, GameConfig config) {
        if (!instance.isPresent()) {
            instance = Optional.of(new Game(factory, config));
        }
        return instance.get();
    }

    /** Useful when restarting from Main/test code without relying on nulls. */
    public static synchronized void resetInstance() {
        instance = Optional.empty();
    }

    public void update() {
        double deltaSeconds = stopwatch.tickSeconds();
        if (state != GameState.RUNNING) {
            updateHud();
            return;
        }

        processInput();
        waveManager.update(deltaSeconds, hasLivingEnemies());
        movementSystem.update(enemies, deltaSeconds);
        damageBaseForReachedEnemies();
        updateTowers(deltaSeconds);
        updateProjectiles(deltaSeconds);
        collectRewardsAndCleanup();
        updateGameState();
        updateHud();
    }

    public Input getInput() {
        return input;
    }

    public Level getLevel() {
        return level;
    }

    public TileManager getTileManager() {
        return tileManager;
    }

    public Base getBase() {
        return base;
    }

    public HUD getHud() {
        return hud;
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public List<Tower> getTowers() {
        return Collections.unmodifiableList(towers);
    }

    public List<Projectile> getProjectiles() {
        return Collections.unmodifiableList(projectiles);
    }

    public GameState getState() {
        return state;
    }

    public TowerType getSelectedTowerType() {
        return towerSelectionOrder.get(selectedTowerIndex);
    }

    public int getGold() {
        return gold;
    }

    public int getScore() {
        return score;
    }

    public List<GameEntity> getEntitiesInRenderOrder() {
        List<GameEntity> renderables = new ArrayList<>();
        renderables.add(base);
        renderables.addAll(towers);
        renderables.addAll(enemies);
        renderables.addAll(projectiles);
        return renderables.stream()
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }

    private void processInput() {
        if (input.consumeNextTowerRequest()) {
            selectedTowerIndex = (selectedTowerIndex + 1) % towerSelectionOrder.size();
        }
        if (input.consumePreviousTowerRequest()) {
            selectedTowerIndex = (selectedTowerIndex - 1 + towerSelectionOrder.size()) % towerSelectionOrder.size();
        }
        if (input.consumeRepairRequest()) {
            repairBase();
        }
        input.consumeBuildRequest().ifPresent(this::tryBuildTower);
    }

    private void tryBuildTower(Vector2 requestedPosition) {
        if (!tileManager.isBuildableWorldPosition(requestedPosition)) {
            return;
        }
        TowerType towerType = getSelectedTowerType();
        TowerStats stats = TowerStats.forType(towerType);
        if (gold < stats.getCost()) {
            return;
        }
        Vector2 towerPosition = tileManager.centerOfWorldTile(requestedPosition);
        Tower tower = factory.createTower(
                towerType,
                towerPosition,
                stats.getRange(),
                stats.getDamage(),
                stats.getAttacksPerSecond(),
                stats.getCost(),
                30
        );
        towers.add(tower);
        tileManager.markOccupied(requestedPosition);
        gold -= stats.getCost();
    }

    private void repairBase() {
        int repairCost = 500;
        if (gold >= repairCost && base.getHealth() < base.getMaxHealth()) {
            gold -= repairCost;
            base.repair(10);
        }
    }

    private void damageBaseForReachedEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.hasReachedBase()) {
                base.takeDamage(enemy.getBaseDamage());
                enemy.markDead();
            }
        }
    }

    private void updateTowers(double deltaSeconds) {
        for (Tower tower : towers) {
            tower.tickCooldown(deltaSeconds);
            if (!tower.canShoot()) {
                continue;
            }
            Optional<Enemy> target = tower.findTarget(enemies);
            if (target.isPresent()) {
                TowerStats stats = TowerStats.forType(tower.getType());
                projectiles.add(factory.createProjectile(tower.getPosition(), target.get(), stats.getProjectileSpeed(), tower.getDamage(), 40));
                tower.resetCooldown();
            }
        }
    }

    private void updateProjectiles(double deltaSeconds) {
        for (Projectile projectile : projectiles) {
            if (projectile.isAlive()) {
                projectile.update(deltaSeconds);
            }
        }
    }

    private void collectRewardsAndCleanup() {
        List<Enemy> defeatedEnemies = enemies.stream()
                .filter(enemy -> !enemy.isAlive() && !enemy.hasReachedBase())
                .collect(Collectors.toList());

        for (Enemy enemy : defeatedEnemies) {
            gold += enemy.getRewardGold();
            score += enemy.getRewardScore();
        }

        enemies.removeIf(enemy -> !enemy.isAlive());
        projectiles.removeIf(projectile -> !projectile.isAlive());
    }

    private void updateGameState() {
        if (!base.isAlive()) {
            state = GameState.GAME_OVER;
            return;
        }
        if (waveManager.areAllWavesStarted() && !hasLivingEnemies()) {
            state = GameState.VICTORY;
        }
    }

    private boolean hasLivingEnemies() {
        return enemies.stream().anyMatch(Enemy::isAlive);
    }

    private void updateHud() {
        hud.update(score, gold, base.getHealth(), waveManager.getCurrentWave(), config.getMaxWaves(), getSelectedTowerType(), state);
    }
}
