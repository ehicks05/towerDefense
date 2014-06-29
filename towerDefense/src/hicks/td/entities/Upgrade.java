package hicks.td.entities;

import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.Tower;

public class Upgrade
{
    private String m_code;
    private String m_preReq;
    private int m_cost;

    public void applyTowerEffect(Tower tower)
    {

    }

    public void applyProjectileEffect(Projectile projectile)
    {

    }

    public String getCode()
    {
        return m_code;
    }

    public void setCode(String code)
    {
        m_code = code;
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
