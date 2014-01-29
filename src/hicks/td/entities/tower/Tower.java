package hicks.td.entities.tower;

import hicks.td.UnitLogic;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.projectile.ProjectileLogic;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Tower extends Unit
{
    private int             m_price;
    private int             m_attackRange;
    private BigDecimal      m_attackSpeed;
    private BigDecimal      m_timeOfLastAttack;
    private Unit            m_target;
    private int             m_kills;

    public boolean isReadyToAttack()
    {
        return Util.getElapsedTime(m_timeOfLastAttack).compareTo(m_attackSpeed) > 0;
    }

    public boolean isTargetInRange()
    {
        Point targetLocation = m_target.getLocation();
        double distance = new BigDecimal(getLocation().getDistance(targetLocation)).setScale(0, RoundingMode.HALF_UP).doubleValue();
        return distance <= m_attackRange;
    }

    public abstract Projectile getProjectile();

    // ------------ Behavior

    public void lookForTarget()
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(this, m_attackRange);
        if (closestVisibleEnemy != null)
            m_target = closestVisibleEnemy;
    }

    public void performHostileBehavior()
    {
        if (isTargetInRange() && isReadyToAttack())
        {
            Projectile newProjectile = this.getProjectile();
            newProjectile.setOriginator(this);
            ProjectileLogic.shootProjectile(this, newProjectile, this.getTarget().getLocation());
            this.setTimeOfLastAttack(Util.now());
        }

        if (m_target != null && !isTargetInRange())
            m_target = null;
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

    public Unit getTarget()
    {
        return m_target;
    }

    public void setTarget(Unit target)
    {
        m_target = target;
    }

    public int getKills()
    {
        return m_kills;
    }

    public void setKills(int kills)
    {
        m_kills = kills;
    }
}
