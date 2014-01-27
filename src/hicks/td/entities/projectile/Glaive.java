package hicks.td.entities.projectile;

import hicks.td.GameState;
import hicks.td.UnitLogic;
import hicks.td.audio.SoundManager;
import hicks.td.entities.Unit;
import hicks.td.entities.mob.Mob;
import hicks.td.util.Util;

public class Glaive extends Projectile
{
    private int m_hitsPossible = 4;
    private int m_hitsPerformed;
    private Unit m_lastUnitHit;
    private int m_bounceRange = 300;

    public Glaive(int team)
    {
        setTeam(team);
        setSizeRadius(10);
        setMoveSpeed(300);
        setTimeOfLastMove(Util.now());

        setMinDamage(9);
        setMaxDamage(12);
        setMaximumRange(300);
    }

    public void performProjectileHit(Mob victim)
    {
        super.performProjectileHit(victim);

        if (m_hitsPerformed < m_hitsPossible)
        {
            Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(this, m_bounceRange, victim);

            if (closestVisibleEnemy != null)
            {
                Glaive glaive = new Glaive(getTeam());
                glaive.setLocation(getLocation());
                glaive.setOriginator(getOriginator());
                glaive.setHitsPerformed(getHitsPerformed() + 1);
                glaive.setLastUnitHit(victim);

                glaive.setDestination(glaive.getProjectileDestination(closestVisibleEnemy.getLocation()));

                SoundManager.playShootAxeSFX();
                GameState.addUnit(glaive);
            }
        }
    }

    // -------------- Properties

    public int getHitsPossible()
    {
        return m_hitsPossible;
    }

    public void setHitsPossible(int hitsPossible)
    {
        this.m_hitsPossible = hitsPossible;
    }

    public int getHitsPerformed()
    {
        return m_hitsPerformed;
    }

    public void setHitsPerformed(int hitsPerformed)
    {
        this.m_hitsPerformed = hitsPerformed;
    }

    public Unit getLastUnitHit()
    {
        return m_lastUnitHit;
    }

    public void setLastUnitHit(Unit lastUnitHit)
    {
        this.m_lastUnitHit = lastUnitHit;
    }

    public int getBounceRange()
    {
        return m_bounceRange;
    }

    public void setBounceRange(int bounceRange)
    {
        m_bounceRange = bounceRange;
    }
}
