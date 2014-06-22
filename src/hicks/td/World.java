package hicks.td;

import hicks.td.entities.*;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class World
{
    private static List<Unit>    units = new ArrayList<>();
    private static GameMap       gameMap;
    private static BigDecimal    startTime;
    private static Player        player;
    private static Spawner       spawner = new Spawner(2);
    private static BufferedImage terrainImage;
    private static List<Wave> waves;

    public static void addUnit(Unit unit)
    {
        units.add(unit);
    }

    public static void removeUnit(Unit unit)
    {
        units.remove(unit);
    }

    public static void adjustStartTime(BigDecimal offset)
    {
        setStartTime(getStartTime().add(offset));
    }

    public static Wave getWave(int waveNumber)
    {
        for (Wave wave : waves)
            if (wave.getWaveNumber() == waveNumber)
                return wave;

        return null;
    }

    // ------------------------ properties

    public static List<Unit> getUnits()
    {
        return units;
    }

    public static void setUnits(List<Unit> units)
    {
        World.units = units;
    }

    public static GameMap getGameMap()
    {
        return gameMap;
    }

    public static void setGameMap(GameMap gameMap)
    {
        World.gameMap = gameMap;
    }

    public static BigDecimal getStartTime()
    {
        return startTime;
    }

    public static void setStartTime(BigDecimal startTime)
    {
        World.startTime = startTime;
    }

    public static Player getPlayer()
    {
        return player;
    }

    public static void setPlayer(Player player)
    {
        World.player = player;
    }

    public static Spawner getSpawner()
    {
        return spawner;
    }

    public static void setSpawner(Spawner spawner)
    {
        World.spawner = spawner;
    }

    public static BufferedImage getTerrainImage()
    {
        return terrainImage;
    }

    public static void setTerrainImage(BufferedImage terrainImage)
    {
        World.terrainImage = terrainImage;
    }

    public static List<Wave> getWaves()
    {
        return waves;
    }

    public static void setWaves(List<Wave> waves)
    {
        World.waves = waves;
    }
}
