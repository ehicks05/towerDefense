package hicks.td.entities.tower;

import hicks.td.entities.projectile.Arrow;
import hicks.td.entities.projectile.Projectile;
import hicks.td.util.Util;

import java.math.BigDecimal;

public class ArrowTower extends Tower
{
    public ArrowTower(int team)
    {
        setTeam(team);
        setSizeRadius(30);
        setAttackRange(256);

        setPrice(50);
        setAttackSpeed(new BigDecimal(1.5));
        setTimeOfLastAttack(Util.now());
    }

    public Projectile getProjectile()
    {
        return new Arrow(this.getTeam());
    }
}
