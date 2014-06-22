package hicks.td.entities.tower;

import hicks.td.entities.projectile.IceBolt;
import hicks.td.entities.projectile.Projectile;
import hicks.td.util.Util;

import java.math.BigDecimal;

public class IceTower extends Tower
{
    public IceTower(int team)
    {
        setTeam(team);
        setSizeRadius(16);
        setAttackRange(224);

        setPrice(150);
        setAttackSpeed(new BigDecimal(1.7));
        setTimeOfLastAttack(Util.now());
        setNumberOfTargets(1);
    }

    public Projectile getProjectile()
    {
        return new IceBolt(this.getTeam());
    }
}
