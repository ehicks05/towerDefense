package hicks.td.entities;

import hicks.td.entities.projectile.Projectile;

public class UpgradeAttackRange extends Upgrade
{
    public UpgradeAttackRange()
    {
        setCode("AR");
        setDescription("Attack Range");
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
