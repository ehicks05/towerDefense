package hicks.td;

import hicks.td.entities.*;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.Tower;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class GameState
{
    private static List<Unit>   units = new ArrayList<>();
    private static GameMap      gameMap;
    private static BigDecimal   startTime;
    private static Player       player;
    private static List<Tile>   tiles = new ArrayList<>();
    private static Spawner      spawner = new Spawner(2);

    public static List<Tower> getTowers()
    {
        List<Tower> towers = new ArrayList<>();

        for (Unit unit : new ArrayList<>(units))
            if (unit instanceof Tower) towers.add((Tower) unit);

        return towers;
    }

    public static List<Projectile> getProjectiles()
    {
        List<Projectile> projectiles = new ArrayList<>();

        for (Unit unit : new ArrayList<>(units))
            if (unit instanceof Projectile) projectiles.add((Projectile) unit);

        return projectiles;
    }

    public static List<Mob> getMobs()
    {
        List<Mob> mobs = new ArrayList<>();

        for (Unit unit : new ArrayList<>(units))
            if (unit instanceof Mob) mobs.add((Mob) unit);

        return mobs;
    }


    public static void addUnit(Unit unit)
    {
        units.add(unit);
    }

    public static void removeUnit(Unit unit)
    {
        units.remove(unit);
    }

    public static void addTileToTiles(Tile tile)
    {
        GameState.tiles.add(tile);
    }

    // ------------------------ properties

    public static List<Unit> getUnits()
    {
        return units;
    }

    public static void setUnits(List<Unit> units)
    {
        GameState.units = units;
    }

    public static GameMap getGameMap()
    {
        return gameMap;
    }

    public static void setGameMap(GameMap gameMap)
    {
        GameState.gameMap = gameMap;
    }

    public static BigDecimal getStartTime()
    {
        return startTime;
    }

    public static void setStartTime(BigDecimal startTime)
    {
        GameState.startTime = startTime;
    }

    public static Player getPlayer()
    {
        return player;
    }

    public static void setPlayer(Player player)
    {
        GameState.player = player;
    }

    public static List<Tile> getTiles()
    {
        return tiles;
    }

    public static void setTiles(List<Tile> tiles)
    {
        GameState.tiles = tiles;
    }

    public static Spawner getSpawner()
    {
        return spawner;
    }

    public static void setSpawner(Spawner spawner)
    {
        GameState.spawner = spawner;
    }
}
