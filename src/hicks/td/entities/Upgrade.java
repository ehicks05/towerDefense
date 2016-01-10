package hicks.td.entities;

public class Upgrade
{
    private String m_code;
    private String m_description;
    private String m_preReq;
    private int m_cost;

    public Upgrade(String code, String description, String preReq, int cost)
    {
        m_code = code;
        m_description = description;
        m_preReq = preReq;
        m_cost = cost;
    }

    public void applyTowerEffect(Tower tower)
    {
        if (m_code.equals("AR"))
            tower.setAttackRange(tower.getAttackRange() + 32);
    }

    public void applyProjectileEffect(Projectile projectile)
    {
        if (m_code.equals("AR"))
        {
            projectile.setMinDamage(projectile.getMinDamage() + 2);
            projectile.setMaxDamage(projectile.getMaxDamage() + 2);
        }
    }

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
