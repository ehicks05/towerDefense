package hicks.td.entities.tower;

import hicks.td.entities.projectile.Cannonball;
import hicks.td.entities.projectile.Projectile;
import hicks.td.util.Util;

import java.math.BigDecimal;

public class CannonTower extends Tower
{
    public CannonTower(int team)
    {
        setTeam(team);
        setSizeRadius(16);
        setAttackRange(256);

        setPrice(150);
        setAttackSpeed(new BigDecimal(2));
        setTimeOfLastAttack(Util.now());
        setNumberOfTargets(1);
    }

    public Projectile getProjectile()
    {
        return new Cannonball(this.getTeam());
    }
}
