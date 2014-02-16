package hicks.td.entities.mob;

import hicks.td.GameState;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.entities.UnitLogic;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Mob extends Unit
{
    private BigDecimal m_spawnTime = Util.now();
    private int m_currentHp;
    private int m_maxHp;
    private int m_armor;
    private Queue<Point> m_path = new ArrayBlockingQueue<>(4);
    private int m_frame;

    public boolean isAlive()
    {
        return m_currentHp > 0;
    }

    public void performMobBehavior()
    {
        MobLogic.moveAlongPath(this);
        if (m_path.size() == 0)
        {
            UnitLogic.removeUnitAsTarget(this);
            GameState.removeUnit(this);
            GameState.getPlayer().removeLife();
        }
    }

    public Queue<Point> createPath()
    {
        Queue<Point> path = new ArrayBlockingQueue<>(4);
        path.add(new Point(32, 32));
        path.add(new Point(32, GameState.getGameMap().getHeight() - 32));
        path.add(new Point(GameState.getGameMap().getWidth() - 32, GameState.getGameMap().getHeight() - 32));
        path.add(new Point(GameState.getGameMap().getWidth() - 32, 32));
        return path;
    }

    // ------------ Properties
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
}
