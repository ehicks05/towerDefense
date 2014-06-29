package hicks.td.entities;

import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.Tower;

public class AttackRangeUpgrade extends Upgrade
{
    private String m_code;
    private String m_preReq;
    private int m_cost;

    AttackRangeUpgrade()
    {
        setCode("AR");
        setPreReq("");
        setCost(50);
    }

    public void applyTowerEffect(Tower tower)
    {
        tower.setAttackRange(tower.getAttackRange() + 32);
    }

    public void applyProjectileEffect(Projectile projectile)
    {

    }
}
