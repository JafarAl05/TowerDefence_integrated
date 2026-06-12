package be.uantwerpen.fti.ei.Game.Rules;

/**
 * Pure game-rule data for enemies.
 * The Java2D factory only creates drawable enemy objects; it does not decide
 * these gameplay values. That keeps the abstract factory clean.</p>
 */
public final class EnemyStats {
    private final double baseSpeed;
    private final int baseHealth;
    private final int rewardGold;
    private final int rewardScore;
    private final int baseDamage;

    private EnemyStats(double baseSpeed, int baseHealth, int rewardGold, int rewardScore, int baseDamage) {
        this.baseSpeed = baseSpeed;
        this.baseHealth = baseHealth;
        this.rewardGold = rewardGold;
        this.rewardScore = rewardScore;
        this.baseDamage = baseDamage;
    }

    public static EnemyStats forType(EnemyType type) {
        switch (type) {
            case RUNNER:
                // Small and fast: low health, low base damage.
                return new EnemyStats(85.0, 45, 10, 90, 1);
            case ARMORED:
                // Big and slow: high health, high base damage.
                return new EnemyStats(32.0, 230, 35, 240, 4);
            case GOBLIN:
            default:
                // Normal enemy: average speed, health and damage.
                return new EnemyStats(55.0, 100, 18, 130, 2);
        }
    }

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public int getBaseHealth() {
        return baseHealth;
    }

    public int getRewardGold() {
        return rewardGold;
    }

    public int getRewardScore() {
        return rewardScore;
    }

    public int getBaseDamage() {
        return baseDamage;
    }
}
