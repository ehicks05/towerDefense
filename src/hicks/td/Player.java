package hicks.td;

public final class Player
{
    private int m_gold;
    private int m_lives;
    private int m_round;

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

    public int getRound()
    {
        return m_round;
    }

    public void setRound(int round)
    {
        this.m_round = round;
    }
}
