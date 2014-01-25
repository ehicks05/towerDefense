package hicks.td.entities.projectile;

import hicks.td.CombatLogic;
import hicks.td.GameState;
import hicks.td.UnitLogic;
import hicks.td.audio.SoundManager;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.entities.tower.Tower;
import hicks.td.util.NewtonRaphson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

public class Projectile extends Unit
{
    Tower originator;

    double m_maximumRange;
    double m_distanceTravelled;
    double m_theta;

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

    public Point getProjectileDestination(Point targetLocation)
    {
        // calculate x,y weighting
        double deltaXDouble = targetLocation.getDeltaX(getLocation());
        double deltaYDouble = targetLocation.getDeltaY(getLocation());
        BigDecimal deltaX = new BigDecimal(deltaXDouble);
        BigDecimal deltaY = new BigDecimal(deltaYDouble);

        BigDecimal distanceFromTarget = NewtonRaphson.bigSqrt(deltaX.pow(2).add(deltaY.pow(2)));

        BigDecimal factorX = deltaX.divide(distanceFromTarget, 16, RoundingMode.HALF_UP);
        BigDecimal factorY = deltaY.divide(distanceFromTarget, 16, RoundingMode.HALF_UP);

        BigDecimal projectileRange = new BigDecimal(m_maximumRange).setScale(0, RoundingMode.HALF_UP).add(new BigDecimal(10));
        BigDecimal distanceToMoveX = projectileRange.multiply(factorX);
        BigDecimal distanceToMoveY = projectileRange.multiply(factorY);

        double resultX = getLocation().getX() + distanceToMoveX.doubleValue();
        double resultY = getLocation().getY() + distanceToMoveY.doubleValue();

        return new Point(resultX, resultY);
    }

    public void performProjectileBehavior()
    {
        // check that we haven't run out of steam
        if (Math.round(m_distanceTravelled) >= m_maximumRange)
            GameState.removeUnit(this);

        Point destination = getDestination();
        UnitLogic.move(this, destination);

        // see if we have hit anyone
        for (Unit potentialVictim : new ArrayList<>(GameState.getUnits()))
        {
            if (this instanceof Glaive)
            {
                Glaive glaive = (Glaive) this;
                if (potentialVictim == glaive.getLastUnitHit()) continue;
            }

            if (potentialVictim instanceof Mob)
            {
                if (getLocation().getDistance(potentialVictim.getLocation()) <= getSizeRadius() + potentialVictim.getSizeRadius())
                    performProjectileHit((Mob) potentialVictim);
            }
        }
    }

    public void performProjectileHit(Mob victim)
    {
        SoundManager.playHitSFX();
        CombatLogic.performAttack(this, victim);
        GameState.removeUnit(this);
    }

    // ---------- Properties

    public Tower getOriginator()
    {
        return originator;
    }

    public void setOriginator(Tower originator)
    {
        this.originator = originator;
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
