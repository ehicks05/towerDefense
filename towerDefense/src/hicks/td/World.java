package hicks.td;

import hicks.td.entities.*;
import hicks.td.entities.projectile.*;
import hicks.td.entities.tower.Tower;

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
    private static BufferedImage terrainImage;
    private static List<Wave> waves;
    private static List<Upgrade> upgrades;
    private static List<Tower> towers;
    private static List<Projectile> projectiles;

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

    public static Tower getTowerByName(String name)
    {
        for (Tower tower : towers)
            if (tower.getName().equals(name))
                return new Tower(tower);

        return null;
    }

    public static Projectile getProjectileByName(String name)
    {
        if (name.equals("Arrow")) return new Arrow(1, null);
        if (name.equals("Cannonball")) return new Cannonball(1, null);
        if (name.equals("Glaive")) return new Glaive(1, null);
        if (name.equals("IceBolt")) return new IceBolt(1, null);

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

    public static List<Upgrade> getUpgrades()
    {
        return upgrades;
    }

    public static void setUpgrades(List<Upgrade> upgrades)
    {
        World.upgrades = upgrades;
    }

    public static List<Tower> getTowers()
    {
        return towers;
    }

    public static void setTowers(List<Tower> towers)
    {
        World.towers = towers;
    }

    public static List<Projectile> getProjectiles()
    {
        return projectiles;
    }

    public static void setProjectiles(List<Projectile> projectiles)
    {
        World.projectiles = projectiles;
    }
}
