package net.ehicks.td.entities;

import java.math.BigDecimal;

public class WaveSpawn
{
    private final int m_mobTypeIndex;
    private final BigDecimal m_spawnTime;

    public WaveSpawn(int mobTypeIndex, BigDecimal spawnTime)
    {
        m_mobTypeIndex = mobTypeIndex;
        m_spawnTime = spawnTime;
    }

    public int getMobTypeIndex()
    {
        return m_mobTypeIndex;
    }

    public BigDecimal getSpawnTime()
    {
        return m_spawnTime;
    }
}
