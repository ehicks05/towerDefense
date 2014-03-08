package hicks.td.entities.projectile;

import hicks.td.World;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.UnitLogic;
import hicks.td.entities.mob.Mob;
import hicks.td.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Glaive extends Projectile
{
    private int m_hitsPossible = 4;
    private int m_hitsPerformed;
    private List<Mob> m_mobsHit = new ArrayList<>();
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
        setFireSound(SoundEffect.SHOOT_GLAIVE);
    }

    public void performProjectileHit(Mob victim)
    {
        super.performProjectileHit(victim);

        // attempt to bounce to another mob
        if (m_hitsPerformed < m_hitsPossible)
        {
            List<Mob> mobsHit = this.getMobsHit();
            mobsHit.add(victim);

            List<Mob> closestVisibleEnemies = UnitLogic.getClosestVisibleEnemies(this, m_bounceRange, mobsHit, 1);
            Mob closestVisibleEnemy = null;
            if (closestVisibleEnemies.size() > 0)
                closestVisibleEnemy = closestVisibleEnemies.get(0);

            if (closestVisibleEnemy != null)
            {
                Glaive glaive = new Glaive(getTeam());
                glaive.setLocation(this.getLocation());
                glaive.setOriginator(this.getOriginator());
                glaive.setHitsPerformed(this.getHitsPerformed() + 1);
                glaive.setMobsHit(mobsHit);

                glaive.setDestination(ProjectileLogic.getProjectileDestination(glaive, closestVisibleEnemy.getLocation()));

                SoundManager.playSFX(SoundEffect.SHOOT_GLAIVE);
                World.addUnit(glaive);
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

    public List<Mob> getMobsHit()
    {
        return m_mobsHit;
    }

    public void setMobsHit(List<Mob> mobsHit)
    {
        m_mobsHit = mobsHit;
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
