package hicks.td.entities;

import hicks.td.World;
import hicks.td.util.Outfit;
import hicks.td.util.PathPoint;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Mob extends Unit
{
    private String m_mobType = "";
    private int m_mobTypeIndex;
    private int m_powerBudgetUsage;
    private int m_currentHp;
    private int m_maxHp;
    private int m_armor;
    private int m_frame;
    private int m_bounty;
    private int m_slowInstances;

    private BigDecimal m_spawnTime = Util.now();
    private Outfit m_outfit;
    private Queue<Point> m_path = new ArrayBlockingQueue<>(4);
    private Point m_previousPoint;

    public Mob(int team, int sizeRadius, int moveSpeed, String mobType, int mobTypeIndex, int powerBudgetUsage, int maxHp,
               int armor, int bounty, int slowInstances, Outfit outfit)
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
        m_spawnTime = Util.now();
        m_outfit = outfit;
    }

    public static Mob duplicateMob(Mob source)
    {
        return new Mob(source.getTeam(), source.getSizeRadius(), source.getMoveSpeed(), source.getMobType(), source.getMobTypeIndex(), source.getPowerBudgetUsage(),
                source.getMaxHp(), source.getArmor(), source.getBounty(), source.getSlowInstances(), source.getOutfit());
    }

    public String toString()
    {
        return this.getClass().getSimpleName() + " HP:" + getCurrentHp() + "(" + getMaxHp() + ")" + ",ID:" + getObjectId();
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

    public Queue<Point> createPath()
    {
        Queue<Point> path = new ArrayBlockingQueue<>(World.getMobPath().size());
        for (PathPoint point : World.getMobPath())
        {
            path.add(new Point(point.getCol() * 32 + 16, point.getRow() * 32));
        }
        return path;
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

    public void setMobType(String mobType)
    {
        m_mobType = mobType;
    }

    public int getMobTypeIndex()
    {
        return m_mobTypeIndex;
    }

    public void setMobTypeIndex(int mobTypeIndex)
    {
        m_mobTypeIndex = mobTypeIndex;
    }

    public int getPowerBudgetUsage()
    {
        return m_powerBudgetUsage;
    }

    public void setPowerBudgetUsage(int powerBudgetUsage)
    {
        m_powerBudgetUsage = powerBudgetUsage;
    }

    public BigDecimal getSpawnTime()
    {
        return m_spawnTime;
    }

    public void setSpawnTime(BigDecimal spawnTime)
    {
        m_spawnTime = spawnTime;
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

    public Outfit getOutfit()
    {
        return m_outfit;
    }

    public void setOutfit(Outfit outfit)
    {
        m_outfit = outfit;
    }

    public int getBounty()
    {
        return m_bounty;
    }

    public void setBounty(int bounty)
    {
        m_bounty = bounty;
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
