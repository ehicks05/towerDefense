package hicks.td.entities;

import java.math.BigDecimal;

public class WaveSpawn
{
    private int m_mobTypeIndex;
    private BigDecimal m_spawnTime;

    public WaveSpawn(int mobTypeIndex, BigDecimal spawnTime)
    {
        m_mobTypeIndex = mobTypeIndex;
        m_spawnTime = spawnTime;
    }

    public int getMobTypeIndex()
    {
        return m_mobTypeIndex;
    }

    public void setMobTypeIndex(int mobTypeIndex)
    {
        m_mobTypeIndex = mobTypeIndex;
    }

    public BigDecimal getSpawnTime()
    {
        return m_spawnTime;
    }

    public void setSpawnTime(BigDecimal spawnTime)
    {
        m_spawnTime = spawnTime;
    }
}
