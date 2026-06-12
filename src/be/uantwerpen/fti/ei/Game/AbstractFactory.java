package be.uantwerpen.fti.ei.Game;

import be.uantwerpen.fti.ei.Game.Entities.Base;
import be.uantwerpen.fti.ei.Game.Entities.Enemy;
import be.uantwerpen.fti.ei.Game.Entities.HUD;
import be.uantwerpen.fti.ei.Game.Entities.Projectile;
import be.uantwerpen.fti.ei.Game.Entities.Tower;
import be.uantwerpen.fti.ei.Game.Input.Input;
import be.uantwerpen.fti.ei.Game.Level.Vector2;
import be.uantwerpen.fti.ei.Game.Rules.EnemyType;
import be.uantwerpen.fti.ei.Game.Rules.TowerType;

import java.util.List;

/**
 * Abstract factory boundary between the game logic and a concrete visualization
 *
 * <p>The Game package only depends on this abstract type and on logic-level entity
 * types. It never imports Java2D, images, or keyboard event classes.
 * A visual package, such as Visualisation.J2D, provides a concrete factory that
 * returns drawable subclasses of these logic entities.</p>
 *
 * <p>Important design rule: gameplay values such as damage, health, speed, range
 * and cost are passed into the factory by the game logic. The visual factory does
 * not decide game rules; it only decides the concrete implementation family.</p>
 */
public abstract class AbstractFactory {
    public abstract Enemy createEnemy(
            EnemyType type,
            Vector2 spawnPosition,
            List<Vector2> path,
            double speed,
            int maxHealth,
            int rewardGold,
            int rewardScore,
            int baseDamage,
            int updatePriority
    );

    public abstract Tower createTower(
            TowerType type,
            Vector2 position,
            double range,
            int damage,
            double attacksPerSecond,
            int cost,
            int updatePriority
    );

    public abstract Projectile createProjectile(
            Vector2 position,
            Enemy target,
            double speed,
            int damage,
            int updatePriority
    );

    public abstract Base createBase(Vector2 position, int maxHealth, int updatePriority);

    public abstract HUD createHUD();

    public abstract Input createInput();
}
