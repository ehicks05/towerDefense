package hicks.td.entities;

import hicks.td.util.MobBodyPartCollection;

public class Round
{
    private int m_roundNumber;
    private MobBodyPartCollection m_mobBodyPartCollection;

    public Round(int roundNumber, MobBodyPartCollection mobBodyPartCollection)
    {
        m_roundNumber = roundNumber;
        m_mobBodyPartCollection = mobBodyPartCollection;
    }

    public int getRoundNumber()
    {
        return m_roundNumber;
    }

    public void setRoundNumber(int roundNumber)
    {
        m_roundNumber = roundNumber;
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
