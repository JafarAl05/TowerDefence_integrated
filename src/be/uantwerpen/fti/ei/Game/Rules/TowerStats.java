package be.uantwerpen.fti.ei.Game.Rules;

/** Pure game-rule data for towers. */
public final class TowerStats {
    private final double range;
    private final int damage;
    private final double attacksPerSecond;
    private final int cost;
    private final double projectileSpeed;

    private TowerStats(double range, int damage, double attacksPerSecond, int cost, double projectileSpeed) {
        this.range = range;
        this.damage = damage;
        this.attacksPerSecond = attacksPerSecond;
        this.cost = cost;
        this.projectileSpeed = projectileSpeed;
    }

    public static TowerStats forType(TowerType type) {
        switch (type) {
            case SNIPER:
                return new TowerStats(230.0, 70, 0.65, 200, 420.0);
            case RAPID:
                return new TowerStats(125.0, 10, 3.5, 100, 360.0);
            case BASIC:
            default:
                return new TowerStats(160.0, 32, 1.25, 75, 320.0);
        }
    }

    public double getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    public double getAttacksPerSecond() {
        return attacksPerSecond;
    }

    public int getCost() {
        return cost;
    }

    public double getProjectileSpeed() {
        return projectileSpeed;
    }
}
