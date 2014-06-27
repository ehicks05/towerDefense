package hicks.td.entities;

import java.math.BigDecimal;

public class Unit
{
    private static int      m_seq = 0;
    private int             m_objectId;
    private int             m_team;
    private int             m_sizeRadius;
    private int             m_moveSpeed;
    private BigDecimal      m_timeOfLastMove;
    private boolean         m_moving;
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

    public BigDecimal getTimeOfLastMove()
    {
        return m_timeOfLastMove;
    }

    public void setTimeOfLastMove(BigDecimal timeOfLastMove)
    {
        m_timeOfLastMove = timeOfLastMove;
    }

    public boolean isMoving()
    {
        return m_moving;
    }

    public void setMoving(boolean moving)
    {
        m_moving = moving;
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