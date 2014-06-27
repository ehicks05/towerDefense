package hicks.td.entities.projectile;

import hicks.td.CombatLogic;
import hicks.td.World;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.Unit;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.tower.Tower;

import java.util.Random;

public abstract class Projectile extends Unit
{
    private Tower m_originator;
    private SoundEffect m_fireSound;

    private double m_maximumRange;
    private double m_distanceTravelled;
    private double m_theta;

    private int m_minDamage;
    private int m_maxDamage;

    public int getAttackDamage()
    {
        Random random       = new Random();
        int damageRange     = m_maxDamage - m_minDamage + 1;
        int randomPortion   = random.nextInt(damageRange);

        return m_minDamage + randomPortion;
    }

    // ---------- Behavior

    public void onHit(Mob victim)
    {
        CombatLogic.performAttack(this, victim);
        World.removeUnit(this);
        SoundManager.playSFX(SoundEffect.WEAPON_HIT);
    }

    // ---------- Properties

    public Tower getOriginator()
    {
        return m_originator;
    }

    public void setOriginator(Tower originator)
    {
        this.m_originator = originator;
    }

    public SoundEffect getFireSound()
    {
        return m_fireSound;
    }

    public void setFireSound(SoundEffect fireSound)
    {
        m_fireSound = fireSound;
    }

    public double getMaximumRange()
    {
        return m_maximumRange;
    }

    public void setMaximumRange(double maximumRange)
    {
        m_maximumRange = maximumRange;
    }

    public double getDistanceTravelled()
    {
        return m_distanceTravelled;
    }

    public void setDistanceTravelled(double distanceTravelled)
    {
        m_distanceTravelled = distanceTravelled;
    }

    public double getTheta()
    {
        return m_theta;
    }

    public void setTheta(double theta)
    {
        m_theta = theta;
    }

    public void setMinDamage(int minDamage)
    {
        m_minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage)
    {
        m_maxDamage = maxDamage;
    }

    public int getMinDamage()
    {
        return m_minDamage;
    }

    public int getMaxDamage()
    {
        return m_maxDamage;
    }
}