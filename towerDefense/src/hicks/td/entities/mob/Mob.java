package hicks.td.entities.mob;

import hicks.td.World;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.entities.UnitLogic;
import hicks.td.util.MobBodyPartCollection;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private MobBodyPartCollection m_mobBodyPartCollection;
    private Queue<Point> m_path = new ArrayBlockingQueue<>(4);

    public Mob(int team, int sizeRadius, int moveSpeed, String mobType, int mobTypeIndex, int powerBudgetUsage, int maxHp, int armor,
               int bounty, int slowInstances, MobBodyPartCollection mobBodyPartCollection)
    {
        setTeam(team);
        setSizeRadius(sizeRadius);
        setMoveSpeed(moveSpeed);
        setTimeOfLastMove(Util.now());

        m_mobType = mobType;
        m_mobTypeIndex = mobTypeIndex;
        m_powerBudgetUsage = powerBudgetUsage;
        m_currentHp = maxHp;
        m_maxHp = maxHp;
        m_armor = armor;
        m_bounty = bounty;
        m_slowInstances = slowInstances;
        m_spawnTime = Util.now();
        m_mobBodyPartCollection = mobBodyPartCollection;
    }

    public static Mob duplicateMob(Mob original)
    {
        return new Mob(original.getTeam(), original.getSizeRadius(), original.getMoveSpeed(), original.getMobType(), original.getMobTypeIndex(), original.getPowerBudgetUsage(),
                original.getMaxHp(), original.getArmor(), original.getBounty(), original.getSlowInstances(), original.getMobBodyPartCollection());
    }

    public boolean isAlive()
    {
        return m_currentHp > 0;
    }

    public void performMobBehavior()
    {
        this.moveAlongPath();
        if (m_path.size() == 0)
        {
            UnitLogic.removeUnitAsTarget(this);
            World.removeUnit(this);
            World.getPlayer().removeLife();
        }
    }

    public Queue<Point> createPath()
    {
        Queue<Point> path = new ArrayBlockingQueue<>(4);
        path.add(new Point(32, 32));
        path.add(new Point(32, World.getGameMap().getHeight() - 32));
        path.add(new Point(World.getGameMap().getWidth() - 32, World.getGameMap().getHeight() - 32));
        path.add(new Point(World.getGameMap().getWidth() - 32, 32));
        return path;
    }

    public void moveAlongPath()
    {
        Queue<Point> path = this.getPath();
        Point pathPoint = path.peek();

        if (pathPoint != null)
        {
            BigDecimal currentDistance = new BigDecimal(this.getLocation().getDistance(pathPoint)).setScale(0, RoundingMode.HALF_UP);
            if (currentDistance.equals(BigDecimal.ZERO))
            {
                path.remove();
                path.add(pathPoint);
            }
            else
                UnitLogic.move(this, pathPoint);
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

    public MobBodyPartCollection getMobBodyPartCollection()
    {
        return m_mobBodyPartCollection;
    }

    public void setMobBodyPartCollection(MobBodyPartCollection mobBodyPartCollection)
    {
        m_mobBodyPartCollection = mobBodyPartCollection;
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
}
