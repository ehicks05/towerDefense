package hicks.td;

import hicks.td.entities.*;
import hicks.td.entities.projectile.*;
import hicks.td.entities.Tower;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class World
{
    private static String        imageDir = "ass\\img\\";
    private static List<Unit>    units = new ArrayList<>();
    private static GameMap       gameMap;
    private static BigDecimal    startTime;
    private static Player        player;
    private static BufferedImage terrainImage;
    private static List<Wave> waves;
    private static List<Upgrade> upgradeTypes;
    private static List<Tower> towerTypes;
    private static List<Projectile> projectileTypes;
    private static List<GameImage> gameImages;

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
        for (Tower tower : towerTypes)
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

    public static GameImage getGameImage(String name)
    {
        for (GameImage gameImage : gameImages)
            if (gameImage.getPath().equals(name)) return gameImage;

        return null;
    }

    // ------------------------ properties


    public static String getImageDir()
    {
        return imageDir;
    }

    public static void setImageDir(String imageDir)
    {
        World.imageDir = imageDir;
    }

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

    public static List<Upgrade> getUpgradeTypes()
    {
        return upgradeTypes;
    }

    public static void setUpgradeTypes(List<Upgrade> upgradeTypes)
    {
        World.upgradeTypes = upgradeTypes;
    }

    public static List<Tower> getTowerTypes()
    {
        return towerTypes;
    }

    public static void setTowerTypes(List<Tower> towerTypes)
    {
        World.towerTypes = towerTypes;
    }

    public static List<Projectile> getProjectileTypes()
    {
        return projectileTypes;
    }

    public static void setProjectileTypes(List<Projectile> projectileTypes)
    {
        World.projectileTypes = projectileTypes;
    }

    public static List<GameImage> getGameImages()
    {
        return gameImages;
    }

    public static void setGameImages(List<GameImage> gameImages)
    {
        World.gameImages = gameImages;
    }
}
