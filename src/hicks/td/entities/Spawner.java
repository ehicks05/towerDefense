package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class Spawner extends Unit
{
    private BigDecimal m_buildSpeed;
    private BigDecimal m_timeOfLastBuild;
    private int m_unitsCreated;

    public Spawner(int team)
    {
        setTeam(team);

        setBuildSpeed(new BigDecimal(1));
        setTimeOfLastBuild(Util.now());
    }

    public boolean isReadyToBuild()
    {
        return Util.getElapsedTime(getTimeOfLastBuild()).compareTo(getBuildSpeed()) > 0;
    }

    public void incrementUnitsCreated()
    {
        m_unitsCreated++;
    }

    // -------------------- properties

    public BigDecimal getBuildSpeed()
    {
        return m_buildSpeed;
    }

    public void setBuildSpeed(BigDecimal m_buildSpeed)
    {
        this.m_buildSpeed = m_buildSpeed;
    }

    public BigDecimal getTimeOfLastBuild()
    {
        return m_timeOfLastBuild;
    }

    public void setTimeOfLastBuild(BigDecimal m_timeOfLastBuild)
    {
        this.m_timeOfLastBuild = m_timeOfLastBuild;
    }

    public int getUnitsCreated()
    {
        return m_unitsCreated;
    }

    public void setUnitsCreated(int unitsCreated)
    {
        m_unitsCreated = unitsCreated;
    }
}
