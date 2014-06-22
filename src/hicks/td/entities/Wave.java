package hicks.td.entities;

import hicks.td.util.MobBodyPartCollection;

public class Wave
{
    private int m_waveNumber;
    private int m_powerBudget;
    private MobBodyPartCollection m_mobBodyPartCollection;

    public Wave(int waveNumber, MobBodyPartCollection mobBodyPartCollection)
    {
        m_waveNumber = waveNumber;
        m_mobBodyPartCollection = mobBodyPartCollection;
    }

    public int getWaveNumber()
    {
        return m_waveNumber;
    }

    public void setWaveNumber(int waveNumber)
    {
        m_waveNumber = waveNumber;
    }

    public int getPowerBudget()
    {
        return m_powerBudget;
    }

    public void setPowerBudget(int powerBudget)
    {
        m_powerBudget = powerBudget;
    }

    public MobBodyPartCollection getMobBodyPartCollection()
    {
        return m_mobBodyPartCollection;
    }

    public void setMobBodyPartCollection(MobBodyPartCollection mobBodyPartCollection)
    {
        m_mobBodyPartCollection = mobBodyPartCollection;
    }
}
