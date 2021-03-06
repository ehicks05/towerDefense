package net.ehicks.td.entities;

import net.ehicks.td.World;
import net.ehicks.td.logic.UnitLogic;
import net.ehicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Mob extends Unit
{
    private final String m_mobType;
    private final int m_mobTypeIndex;
    private final int m_powerBudgetUsage;
    private final int m_bounty;
    private final int m_outfit;

    private int m_maxHp;
    private int m_currentHp;
    private int m_armor;
    private int m_frame;
    private BigDecimal m_secondsPerFrame = BigDecimal.ONE.divide(new BigDecimal(6), 3, RoundingMode.HALF_UP);
    private BigDecimal m_timeSinceLastFrame = BigDecimal.ZERO;
    private int m_slowInstances;

    private Queue<Point> m_path = createPath();
    private Point m_previousPoint;

    public Mob(int team, int sizeRadius, int moveSpeed, String mobType, int mobTypeIndex, int powerBudgetUsage, int maxHp,
               int armor, int bounty, int slowInstances, int outfit)
    {
        setTeam(team);
        setSizeRadius(sizeRadius);
        setMoveSpeed(moveSpeed);

        m_mobType = mobType;
        m_mobTypeIndex = mobTypeIndex;
        m_powerBudgetUsage = powerBudgetUsage;
        m_currentHp = maxHp;
        m_maxHp = maxHp;
        m_armor = armor;
        m_bounty = bounty;
        m_slowInstances = slowInstances;
        m_outfit = outfit;
    }

    public static Mob duplicateMob(Mob m)
    {
        return new Mob(m.getTeam(), m.getSizeRadius(), m.getMoveSpeed(), m.getMobType(), m.getMobTypeIndex(), m.getPowerBudgetUsage(),
                m.getMaxHp(), m.getArmor(), m.getBounty(), m.getSlowInstances(), m.getOutfit());
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + " HP:" + getCurrentHp() + "(" + getMaxHp() + ")" + ",ID:" + getObjectId();
    }

    public static Queue<Point> createPath()
    {
        Queue<Point> path = new ArrayBlockingQueue<>(World.getGameMap().getMobPath().size());
        for (PathPoint point : World.getGameMap().getMobPath())
        {
            path.add(new Point(point.getCol() * 32 + 16, point.getRow() * 32));
        }
        return path;
    }

    public boolean isAlive()
    {
        return m_currentHp > 0;
    }

    public void performMobBehavior(BigDecimal dt)
    {
        this.moveAlongPath(dt);
        if (m_path.size() == 0)
        {
            removeFromTargeting();
            World.removeUnit(this);
            World.getPlayer().removeLife();
        }

        m_timeSinceLastFrame = m_timeSinceLastFrame.add(dt);
        while (m_timeSinceLastFrame.compareTo(m_secondsPerFrame) > 0)
        {
            m_frame++;
            if (m_frame > 8)
                m_frame = 1; // start on frame index 1 because index 0 is the idle position...
            m_timeSinceLastFrame = m_timeSinceLastFrame.subtract(m_secondsPerFrame);
        }
    }

    public void removeFromTargeting()
    {
        for (Tower tower : Util.getTowers())
            if (tower.getTargets() != null && tower.getTargets().contains(this))
            {
                List<Mob> newTargetList = tower.getTargets();
                newTargetList.remove(this);
                tower.setTargets(newTargetList);
            }
    }

    public void moveAlongPath(BigDecimal dt)
    {
        Queue<Point> path = this.getPath();
        Point pathPoint = path.peek();

        if (pathPoint != null)
        {
            BigDecimal currentDistance = new BigDecimal(this.getLocation().getDistance(pathPoint)).setScale(0, RoundingMode.HALF_UP);
            if (currentDistance.equals(BigDecimal.ZERO))
            {
                setPreviousPoint(pathPoint);
                path.remove();
            }
            else
                UnitLogic.move(this, pathPoint, dt);
        }
    }

    // ------------ Properties
    public String getMobType()
    {
        return m_mobType;
    }

    public int getMobTypeIndex()
    {
        return m_mobTypeIndex;
    }

    public int getPowerBudgetUsage()
    {
        return m_powerBudgetUsage;
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

    public Queue<Point> getPath()
    {
        return m_path;
    }

    public void setPath(Queue<Point> path)
    {
        m_path = path;
    }

    public int getFrame()
    {
        return m_frame;
    }

    public void setFrame(int frame)
    {
        m_frame = frame;
    }

    public int getOutfit()
    {
        return m_outfit;
    }

    public int getBounty()
    {
        return m_bounty;
    }

    public int getSlowInstances()
    {
        return m_slowInstances;
    }

    public void setSlowInstances(int slowInstances)
    {
        m_slowInstances = slowInstances;
    }

    public Point getPreviousPoint()
    {
        return m_previousPoint;
    }

    public void setPreviousPoint(Point previousPoint)
    {
        m_previousPoint = previousPoint;
    }
}
