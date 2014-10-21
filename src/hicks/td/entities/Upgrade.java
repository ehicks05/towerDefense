package hicks.td.entities;

import hicks.td.entities.projectile.Projectile;

public abstract class Upgrade
{
    private String m_code;
    private String m_description;
    private String m_preReq;
    private int m_cost;

    public abstract void applyTowerEffect(Tower tower);

    public abstract void applyProjectileEffect(Projectile projectile);

    public String getCode()
    {
        return m_code;
    }

    public void setCode(String code)
    {
        m_code = code;
    }

    public String getDescription()
    {
        return m_description;
    }

    public void setDescription(String description)
    {
        m_description = description;
    }

    public String getPreReq()
    {
        return m_preReq;
    }

    public void setPreReq(String preReq)
    {
        m_preReq = preReq;
    }

    public int getCost()
    {
        return m_cost;
    }

    public void setCost(int cost)
    {
        m_cost = cost;
    }
}
