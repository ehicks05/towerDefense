package hicks.td.entities;

public final class Player
{
    private int m_gold;
    private int m_lives;
    private int m_waveNumber;

    public Player(int gold, int lives, int waveNumber)
    {
        m_gold = gold;
        m_lives = lives;
        m_waveNumber = waveNumber;
    }

    public void addLife()
    {
        m_lives += 1;
    }
    public void removeLife()
    {
        m_lives -= 1;
    }

    public void addGold(int amount)
    {
        m_gold += amount;
    }
    public void removeGold(int amount)
    {
        m_gold -= amount;
    }

    // -------------- properties

    public int getGold()
    {
        return m_gold;
    }

    public void setGold(int gold)
    {
        this.m_gold = gold;
    }

    public int getLives()
    {
        return m_lives;
    }

    public void setLives(int lives)
    {
        this.m_lives = lives;
    }

    public int getWaveNumber()
    {
        return m_waveNumber;
    }

    public void setWaveNumber(int waveNumber)
    {
        this.m_waveNumber = waveNumber;
    }
}
