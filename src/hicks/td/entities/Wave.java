package hicks.td.entities;

import hicks.td.World;

import java.math.BigDecimal;
import java.util.*;

public class Wave
{
    private int m_waveNumber;
    private int m_powerBudget;
    private BigDecimal m_timeStarted;
    private List<WaveSpawn> m_waveSpawns;

    public Wave(int waveNumber, int powerBudget)
    {
        m_waveNumber = waveNumber;
        m_powerBudget = powerBudget;
        m_waveSpawns = generateWaveSpawns();
    }

    public static List<Wave> getWaves(int n)
    {
        List<Wave> waves = new ArrayList<>();

        for (int i = 1; i < n; i++)
            waves.add(new Wave(i, i * 10));

        return waves;
    }

    private List<WaveSpawn> generateWaveSpawns()
    {
        List<WaveSpawn> waveSpawns = new ArrayList<>();

        Random rand = new Random();
        List<Mob> mobTypes = World.getMobTypes();
        Map<Integer, Integer> powerBudgetUsageMap = new HashMap<>();
        for (Mob mob : mobTypes)
            powerBudgetUsageMap.put(mob.getMobTypeIndex(), mob.getPowerBudgetUsage());

        int powerBudgeUsed = 0;
        while (powerBudgeUsed < m_powerBudget)
        {
            int maxTypeIndex = m_waveNumber + 1;

            if (maxTypeIndex >= mobTypes.size())
                maxTypeIndex = mobTypes.size() - 1;

            int mobTypeIndex = rand.nextInt(maxTypeIndex) + 1;
            powerBudgeUsed += powerBudgetUsageMap.get(mobTypeIndex);

            while (powerBudgeUsed > m_powerBudget)
            {
                powerBudgeUsed -= powerBudgetUsageMap.get(mobTypeIndex);
                mobTypeIndex--;
                powerBudgeUsed += powerBudgetUsageMap.get(mobTypeIndex);
            }

            BigDecimal spawnTime = new BigDecimal(rand.nextDouble() * 20);
            waveSpawns.add(new WaveSpawn(mobTypeIndex, spawnTime));
        }

        return waveSpawns;
    }

    public int getWaveNumber()
    {
        return m_waveNumber;
    }

    public void setWaveNumber(int waveNumber)
    {
        m_waveNumber = waveNumber;
    }

    public int getPowerBudget()
    {
        return m_powerBudget;
    }

    public void setPowerBudget(int powerBudget)
    {
        m_powerBudget = powerBudget;
    }

    public BigDecimal getTimeStarted()
    {
        return m_timeStarted;
    }

    public void setTimeStarted(BigDecimal timeStarted)
    {
        m_timeStarted = timeStarted;
    }

    public List<WaveSpawn> getWaveSpawns()
    {
        return m_waveSpawns;
    }

    public void setWaveSpawns(List<WaveSpawn> waveSpawns)
    {
        m_waveSpawns = waveSpawns;
    }
}
