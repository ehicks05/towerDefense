import java.math.BigDecimal;

public class Barracks extends Unit
{
    private BigDecimal m_buildSpeed;
    private BigDecimal m_timeOfLastBuild;

    public Barracks(int team)
    {
        setTeam(team);
        setSizeRadius(150);
        setSightRadius(60);

        setHp(600);
        setArmor(5);

        setBuildSpeed(new BigDecimal(10));
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
