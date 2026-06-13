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
    private static Optional<Game> instance = Optional.empty(); // instead of using Game instance = null;

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
        this.level = LevelRepository.getLevel(config.getSelectedLevel(), config.getTileSize()); //select lvl1/2 from config
        this.tileManager = new TileManager(level);
        this.input = factory.createInput();
        this.base = factory.createBase(level.getBasePosition(), config.getStartingLives(), 10);
        this.hud = factory.createHUD();
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.towerSelectionOrder = Collections.unmodifiableList(Arrays.asList(TowerType.Shotgun, TowerType.SNIPER, TowerType.SMG));
        this.selectedTowerIndex = 0;
        this.gold = config.getStartingGold();
        this.score = 0;
        this.state = GameState.RUNNING;
        this.waveManager = new WaveManager(factory, config, level, enemies::add);
        updateHud();
    }

    public static synchronized Game getInstance(AbstractFactory factory, GameConfig config) {
        if (!instance.isPresent()) {
            instance = Optional.of(new Game(factory, config)); //if there are is no game object, create one
        }
        return instance.get(); // otherwise, return the current game object.
    }

    //Useful when restarting from Main/test code without relying on nulls.
    public static synchronized void resetInstance() {
        instance = Optional.empty();
    }

   // main game loop:
    public void update() {
        double deltaSeconds = stopwatch.tickSeconds(); // fetch the delta time
        if (state != GameState.RUNNING) {
            updateHud();
            return; //stop the logic if game is not in the running state
        }

        processInput();
        waveManager.update(deltaSeconds, hasLivingEnemies()); //update the waves and enemies
        movementSystem.update(enemies, deltaSeconds);
        damageBaseForReachedEnemies(); //update the entities
        updateTowers(deltaSeconds);
        updateProjectiles(deltaSeconds);
        collectRewardsAndCleanup(); //remove dead objects
        updateGameState(); //check the game state
        updateHud();
    }
    //public getters to be used to give information to the visual side of the code (J2D)
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

    public GameState getState() {
        return state;
    }

    public TowerType getSelectedTowerType() {
        return towerSelectionOrder.get(selectedTowerIndex);
    }

    public List<GameEntity> getEntitiesInRenderOrder() { // we use this to sort the drawable objects by priority
        List<GameEntity> renderables = new ArrayList<>();
        renderables.add(base);
        renderables.addAll(towers);
        renderables.addAll(enemies);
        renderables.addAll(projectiles);
        return renderables.stream() //API STREAM
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
    }


    //private methods used in the game loop.

    // if the player wants to ..., the do ....
    private void processInput() {
        if (input.consumeNextTowerRequest()) { //switch to next option of tower (right arrow)
            selectedTowerIndex = (selectedTowerIndex + 1) % towerSelectionOrder.size();
        }
        if (input.consumePreviousTowerRequest()) { //switch to next option of tower (left arrow)
            selectedTowerIndex = (selectedTowerIndex - 1 + towerSelectionOrder.size()) % towerSelectionOrder.size();
        }
        if (input.consumeRepairRequest()) { //repair the base (H key)
            repairBase();
        }
        input.consumeBuildRequest().ifPresent(this::tryBuildTower); //build a tower(mouseclick)
    }
    //But the actual keys/mousclicks are not registrerd here -> visual side.
    // for example: the game reads consumeNextTowerRequest / consumeBuildRequest and preforms the necessary logic.
    //It does not read keyevent/mouseevent !

    private void tryBuildTower(Vector2 requestedPosition) {
        if (!tileManager.isBuildableWorldPosition(requestedPosition)) { // check if clicked tile is buildable
            return;
        }
        TowerType towerType = getSelectedTowerType(); //choose tower type based on the pressed keys
        TowerStats stats = TowerStats.forType(towerType); // match the tower type to its stats
        if (gold < stats.getCost()) { // stats of the tower type tell us the tower is too expensive
            return;
        }
        Vector2 towerPosition = tileManager.centerOfWorldTile(requestedPosition);// center the tower on the center of the selected tile
        Tower tower = factory.createTower( //create the tower with the factory and fill the fields with the tower stats.
                towerType,
                towerPosition,
                stats.getRange(),
                stats.getDamage(),
                stats.getAttacksPerSecond(),
                stats.getCost(),
                30
        );
        towers.add(tower); // add this tower to the tower list
        tileManager.markOccupied(requestedPosition); // we set the clicked tile as occupies
        gold -= stats.getCost(); // subtract the players gold with the tower cost.
    }

    private void repairBase() {
        int repairCost = 500;
        if (gold >= repairCost && base.getHealth() < base.getMaxHealth()) { //if player has enough gold and base health is not full
            gold -= repairCost;
            base.repair(10);
        }
    }

    private void damageBaseForReachedEnemies() { //if an enemy reaches the base, then the base loses health
        for (Enemy enemy : enemies) {
            if (enemy.isAlive() && enemy.hasReachedBase()) {
                base.takeDamage(enemy.getBaseDamage());  // base takes damage accroding to the enemy type (getter to get the exact damage)
                enemy.markDead(); // enemy dies once it reaches the base (kind of like kamikaze lol)
            }
        }
    }

    private void updateTowers(double deltaSeconds) { // how towers attack the enemies:
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
            // basically:
            // tower cooldown decreases
            //→ if ready, find enemy in range (API stream)
            //→ if enemy found, create projectile
            //→ reset cooldown
        }
    }

    private void updateProjectiles(double deltaSeconds) { // the projectiles that are alive update themselves
        for (Projectile projectile : projectiles) {
            if (projectile.isAlive()) {
                projectile.update(deltaSeconds);
            }
        }
    }

    private void collectRewardsAndCleanup() {
        List<Enemy> defeatedEnemies = enemies.stream() // a list of enemies that are dead and did not reach the base (so killed by a tower)
                .filter(enemy -> !enemy.isAlive() && !enemy.hasReachedBase())
                .collect(Collectors.toList());

        for (Enemy enemy : defeatedEnemies) { //gain gold/score for an enemy killed (varies on wich kind of enemy you killed)
            gold += enemy.getRewardGold();
            score += enemy.getRewardScore();
        }
        // cleanup
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
    } // check if there are living enemies

    private void updateHud() { //to update the HUD with the information gained from above methods
        hud.update(score, gold, base.getHealth(), waveManager.getCurrentWave(), config.getMaxWaves(), getSelectedTowerType(), state);
    }
}
