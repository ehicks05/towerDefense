package hicks.td.entities;

public class UpgradeDamage extends Upgrade
{
    public UpgradeDamage()
    {
        setCode("AD");
        setDescription("Attack Damage");
        setPreReq("");
        setCost(50);
    }

    public void applyTowerEffect(Tower tower)
    {

    }

    public void applyProjectileEffect(Projectile projectile)
    {
        projectile.setMinDamage(projectile.getMinDamage() + 2);
        projectile.setMaxDamage(projectile.getMaxDamage() + 2);
    }
}
