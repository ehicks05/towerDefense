package hicks.combat.entities;

import hicks.combat.GameLogic;

import java.math.BigDecimal;

public class Peasant extends Unit implements Builder
{
    private BigDecimal m_buildSpeed;
    private BigDecimal m_timeOfLastBuild;

    public Peasant(int team)
    {
        setTeam(team);
        setSizeRadius(40);
        setSightRadius(40);
        setMoveSpeed(50);
        setTimeOfLastMove(GameLogic.getNow());

        setHp(50);
        setArmor(0);
        setMinDamage(8);
        setMaxDamage(10);
        setAttackRange(8);

        setAttackSpeed(new BigDecimal(1.5));
        setTimeOfLastAttack(GameLogic.getNow());

        setBuildSpeed(new BigDecimal(1));
        setTimeOfLastBuild(GameLogic.getNow());
    }

    public boolean isReadyToBuild()
    {
        return GameLogic.getElapsedTime(getTimeOfLastBuild()).compareTo(getBuildSpeed()) > 0;
    }

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
}
