package hicks.td.entities;

import hicks.td.Util;

public class Arrow extends Unit implements Projectile
{
    double m_maximumRange;
    double m_distanceTravelled;

    public Arrow(int team)
    {
        setTeam(team);
        setSizeRadius(20);
        setSightRadius(1);
        setMoveSpeed(200);
        setTimeOfLastMove(Util.now());

        setCurrentHp(1);
        setMaxHp(1);
        setMinDamage(12);
        setMaxDamage(18);
        setMaximumRange(800);
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
}
