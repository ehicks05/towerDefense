package hicks.td.entities;

import hicks.td.Util;
import hicks.td.Point;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Unit
{
    private static int      m_seq = 0;
    private int             m_objectId;
    private int             m_team;
    private int             m_sizeRadius;
    private int             m_sightRadius;
    private int             m_moveSpeed;
    private BigDecimal      m_timeOfLastMove;
    private boolean         m_moving;
    private Point           m_location;
    private Point           m_destination;
    private Queue<Point>    m_path = new ArrayBlockingQueue<>(30);

    private int             m_currentHp;
    private int             m_maxHp;
    private int             m_armor;
    private int             m_minDamage;
    private int             m_maxDamage;
    private int             m_attackRange;
    private BigDecimal      m_attackSpeed;
    private BigDecimal      m_timeOfLastAttack;
    private Unit            m_target;
    private int             m_kills;

    public Unit()
    {
        setObjectId(m_seq++);
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + " (T" + m_team + ",ID:" + m_objectId + ")";
    }

    public boolean isReadyToAttack()
    {
        return Util.getElapsedTime(m_timeOfLastAttack).compareTo(m_attackSpeed) > 0;
    }

    public boolean isAlive()
    {
        return m_currentHp > 0;
    }

    // -------- Properties

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

    public int getSightRadius()
    {
        return m_sightRadius;
    }

    public void setSightRadius(int sightRadius)
    {
        m_sightRadius = sightRadius;
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

    public Queue<Point> getPath()
    {
        return m_path;
    }

    public void setPath(Queue<Point> path)
    {
        m_path = path;
    }

    public int getCurrentHp()
    {
        return m_currentHp;
    }

    public void setCurrentHp(int currentHp)
    {
        m_currentHp = currentHp;
    }

    public int getMaxHp()
    {
        return m_maxHp;
    }

    public void setMaxHp(int maxHp)
    {
        m_maxHp = maxHp;
    }

    public int getArmor()
    {
        return m_armor;
    }

    public void setArmor(int armor)
    {
        m_armor = armor;
    }

    public void setMinDamage(int minDamage)
    {
        m_minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage)
    {
        m_maxDamage = maxDamage;
    }

    public int getAttackRange()
    {
        return m_attackRange;
    }

    public void setAttackRange(int attackRange)
    {
        m_attackRange = attackRange;
    }

    public BigDecimal getAttackSpeed()
    {
        return m_attackSpeed;
    }

    public void setAttackSpeed(BigDecimal attackSpeed)
    {
        m_attackSpeed = attackSpeed;
    }

    public BigDecimal getTimeOfLastAttack()
    {
        return m_timeOfLastAttack;
    }

    public void setTimeOfLastAttack(BigDecimal timeOfLastAttack)
    {
        m_timeOfLastAttack = timeOfLastAttack;
    }

    public Unit getTarget()
    {
        return m_target;
    }

    public void setTarget(Unit target)
    {
        m_target = target;
    }

    public int getKills()
    {
        return m_kills;
    }

    public void setKills(int kills)
    {
        m_kills = kills;
    }

    public int getMinDamage()
    {
        return m_minDamage;
    }

    public int getMaxDamage()
    {
        return m_maxDamage;
    }
}
