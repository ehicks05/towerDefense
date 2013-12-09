package hicks.combat;

import hicks.combat.entities.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic
{
    public static BigDecimal now()
    {
        return new BigDecimal(System.nanoTime());
    }

    public static BigDecimal getElapsedTime(BigDecimal startTime)
    {
        return GameLogic.now().subtract(startTime).divide(new BigDecimal("1000000000"));
    }

    public static List<Integer> teamsLeft(List<Unit> units)
    {
        List<Integer> teamsAlive = new ArrayList<>();
        for (Unit unit : units)
            if (unit.isAlive() && !teamsAlive.contains(unit.getTeam()))
                teamsAlive.add(unit.getTeam());

        return teamsAlive;
    }

    public static int getUnitsOnTeam(List<Unit> units, int team)
    {
        int result = 0;
        for (Unit unit : units)
            if (unit.getTeam() == team) result++;

        return result;
    }

    public static int getTeamWithMostUnits(List<Unit> units)
    {
        int unitsOnTeam1 = GameLogic.getUnitsOnTeam(units, 1);
        int unitsOnTeam2 = GameLogic.getUnitsOnTeam(units, 2);
        return unitsOnTeam1 > unitsOnTeam2 ? 1 : 2;
    }

    public static boolean isClearOfBarracks(Unit unit, GameMap map)
    {
        boolean isClearOfBarracks = true;
        List<Unit> barracks = new ArrayList<>(getListOfBarracks(map));
        for (Unit barrack : barracks)
        {
            if (unit.getLocation().getDistance(barrack.getLocation()) < 100) isClearOfBarracks = false;
        }
        return isClearOfBarracks;
    }

    private static List<Unit> getListOfBarracks(GameMap map)
    {
        List<Unit> units = map.getExistingUnits();
        List<Unit> barracks = new ArrayList<>();
        for (Unit unit : units)
        {
            if (unit instanceof Barracks) barracks.add(unit);
        }

        return barracks;
    }
}
