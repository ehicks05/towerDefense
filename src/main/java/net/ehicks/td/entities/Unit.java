package net.ehicks.td.entities;

import net.ehicks.td.util.Util;

public class Unit
{
    private static int      m_seq = 0;
    private int             m_objectId;
    private long            m_creationTime = Util.now().longValue();
    private int             m_team;
    private int             m_sizeRadius;
    private int             m_moveSpeed;
    private Point           m_location;
    private Point           m_destination;

    // -------------------------------- basics

    public Unit()
    {
        setObjectId(m_seq++);
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + " (T" + m_team + ",ID:" + m_objectId + ")";
    }

    // -------------------------------- Properties

    public int getObjectId()
    {
        return m_objectId;
    }

    public void setObjectId(int objectId)
    {
        m_objectId = objectId;
    }

    public long getCreationTime()
    {
        return m_creationTime;
    }

    public void setCreationTime(long creationTime)
    {
        m_creationTime = creationTime;
    }

    public int getSizeRadius()
    {
        return m_sizeRadius;
    }

    public void setSizeRadius(int sizeRadius)
    {
        m_sizeRadius = sizeRadius;
    }

    public int getTeam()
    {
        return m_team;
    }

    public void setTeam(int team)
    {
        m_team = team;
    }

    public int getMoveSpeed()
    {
        return m_moveSpeed;
    }

    public void setMoveSpeed(int moveSpeed)
    {
        m_moveSpeed = moveSpeed;
    }

    public Point getLocation()
    {
        return m_location;
    }

    public void setLocation(Point location)
    {
        m_location = location;
    }

    public Point getDestination()
    {
        return m_destination;
    }

    public void setDestination(Point destination)
    {
        m_destination = destination;
    }
}
