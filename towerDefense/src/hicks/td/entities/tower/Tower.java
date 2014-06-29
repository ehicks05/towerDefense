package hicks.td.entities.tower;

import hicks.td.entities.Unit;
import hicks.td.entities.UnitLogic;
import hicks.td.entities.Upgrade;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.projectile.ProjectileLogic;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.util.List;

public abstract class Tower extends Unit
{
    private int             m_price;
    private int             m_attackRange;
    private BigDecimal      m_attackSpeed;
    private BigDecimal      m_timeOfLastAttack;
    private List<Mob>       m_targets;
    private int             m_kills;
    private int             m_numberOfTargets;
    private List<Upgrade>   m_upgrades;

    public abstract Projectile getProjectile();

    public void performTowerBehavior()
    {
        // set targets
        this.setTargets(UnitLogic.getClosestVisibleEnemies(this, m_attackRange, null, this.getNumberOfTargets()));

        if (this.getTargets().size() > 0) this.performHostileBehavior();
    }

    public void addUpgrade(Upgrade upgrade)
    {
        upgrade.applyTowerEffect(this);
        m_upgrades.add(upgrade);
    }

    public boolean isReadyToAttack()
    {
        return Util.getElapsedTime(m_timeOfLastAttack).compareTo(m_attackSpeed) > 0;
    }

    public void performHostileBehavior()
    {
        if (isReadyToAttack())
        {
            for (Mob target : this.getTargets())
            {
                Projectile newProjectile = this.getProjectile();
                newProjectile.setOriginator(this);
                ProjectileLogic.shootProjectile(this, newProjectile, target.getLocation());
                this.setTimeOfLastAttack(Util.now());
            }
        }
    }

    // ------------ Properties

    public int getPrice()
    {
        return m_price;
    }

    public void setPrice(int price)
    {
        m_price = price;
    }

    public int getAttackRange()
    {
        return m_attackRange;
    }

    public void setAttackRange(int attackRange)
    {
        m_attackRange = attackRange;
    }

    public BigDecimal getAttackSpeed()
    {
        return m_attackSpeed;
    }

    public void setAttackSpeed(BigDecimal attackSpeed)
    {
        m_attackSpeed = attackSpeed;
    }

    public BigDecimal getTimeOfLastAttack()
    {
        return m_timeOfLastAttack;
    }

    public void setTimeOfLastAttack(BigDecimal timeOfLastAttack)
    {
        m_timeOfLastAttack = timeOfLastAttack;
    }

    public List<Mob> getTargets()
    {
        return m_targets;
    }

    public void setTargets(List<Mob> targets)
    {
        m_targets = targets;
    }

    public int getKills()
    {
        return m_kills;
    }

    public void setKills(int kills)
    {
        m_kills = kills;
    }

    public int getNumberOfTargets()
    {
        return m_numberOfTargets;
    }

    public void setNumberOfTargets(int numberOfTargets)
    {
        m_numberOfTargets = numberOfTargets;
    }

    public List<Upgrade> getUpgrades()
    {
        return m_upgrades;
    }

    public void setUpgrades(List<Upgrade> upgrades)
    {
        m_upgrades = upgrades;
    }
}
