package be.uantwerpen.fti.ei.Visualisation.J2D;

import be.uantwerpen.fti.ei.Game.AbstractFactory;
import be.uantwerpen.fti.ei.Game.Entities.Base;
import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Entities.HUD;
import be.uantwerpen.fti.ei.Game.Entities.Projectile;
import be.uantwerpen.fti.ei.Game.Entities.Tower;
import be.uantwerpen.fti.ei.Game.Input.Input;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.EnemyType;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;
import be.uantwerpen.fti.ei.Visualisation.J2D.Entities.J2DBase;
import be.uantwerpen.fti.ei.Visualisation.J2D.Entities.J2DEnemy;
import be.uantwerpen.fti.ei.Visualisation.J2D.Entities.J2DHUD;
import be.uantwerpen.fti.ei.Visualisation.J2D.Entities.J2DProjectile;
import be.uantwerpen.fti.ei.Visualisation.J2D.Entities.J2DTower;
import be.uantwerpen.fti.ei.Visualisation.J2D.Input.J2DInput;

import java.util.List;

/**
 * Concrete AbstractFactory for the Java2D presentation.
 * It creates drawable subclasses but does not decide gameplay statistics.
 */
public final class J2DFactory extends AbstractFactory {
    @Override
    public Enemy createEnemy(EnemyType type, Vector2 spawnPosition, List<Vector2> path, double speed, int maxHealth,
                             int rewardGold, int rewardScore, int baseDamage, int updatePriority) {
        return new J2DEnemy(type, spawnPosition, path, speed, maxHealth, rewardGold, rewardScore, baseDamage, updatePriority);
    }

    @Override
    public Tower createTower(TowerType type, Vector2 position, double range, int damage, double attacksPerSecond, int cost, int updatePriority) {
        return new J2DTower(type, position, range, damage, attacksPerSecond, cost, updatePriority);
    }

    @Override
    public Projectile createProjectile(Vector2 position, Enemy target, double speed, int damage, int updatePriority) {
        return new J2DProjectile(position, target, speed, damage, updatePriority);
    }

    @Override
    public Base createBase(Vector2 position, int maxHealth, int updatePriority) {
        return new J2DBase(position, maxHealth, updatePriority);
    }

    @Override
    public HUD createHUD() {
        return new J2DHUD();
    }

    @Override
    public Input createInput() {
        return new J2DInput();
    }
}

//short explanation/flow:
//Main chooses J2DFactory.
//Game stores it as AbstractFactory.
//Game asks factory to create objects.
//J2DFactory returns Java2D drawable versions.
