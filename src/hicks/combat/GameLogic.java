package hicks.combat;

import hicks.combat.entities.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic
{
    public static void updateState(BigDecimal simulationStart, GameMap map)
    {
        // copy list to try to avoid concurrentModificationExceptions
        List<Unit> unitsToProcess = new ArrayList<>(map.getExistingUnits());

        for (Unit unit : unitsToProcess)
        {
            if (!unit.isAlive()) return;

            if (!(unit instanceof Builder))
            {
                if (unit.getTarget() == null)
                    performIdleBehavior(unit, simulationStart, map);
                if (unit.getTarget() != null)
                    performHostileBehavior(unit, simulationStart, map);
            }
            if (unit instanceof Builder)
                performBuildBehavior(unit, simulationStart, map);
        }
    }

    private static void performBuildBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        if (unit instanceof Barracks)
        {
            Barracks barracks = (Barracks) unit;
            if (barracks.isReadyToBuild())
            {
                // create and add to map, a new footman... it should be in an available spot next to the barracks
                Random gen = new Random();
                int typeToBuild = gen.nextInt(5);
                Unit barracksCreation;

                switch (typeToBuild)
                {
                    case 0: barracksCreation = new Footman(barracks.getTeam());
                        break;
                    case 1: barracksCreation = new Knight(barracks.getTeam());
                        break;
//                    case 2: barracksCreation = new hicks.combat.entities.Archer(barracks.getTeam());
//                        break;
//                    case 3: barracksCreation = new hicks.combat.entities.Berserker(barracks.getTeam());
//                        break;
                    case 4: barracksCreation = new Peasant(barracks.getTeam());
                        break;
                    default: barracksCreation = new Footman(barracks.getTeam());
                }

                barracksCreation.setLocation(map.getAvailableAdjacentLocation(barracks.getLocation()));
                map.addUnitToExistingUnits(barracksCreation);
                barracks.setTimeOfLastBuild(GameLogic.now());

                Log.logInfo(simulationStart, barracks + " has created a footman " + barracksCreation);

                int unitsOnTeam1 = getUnitsOnTeam(map.getExistingUnits(), 1);
                int unitsOnTeam2 = getUnitsOnTeam(map.getExistingUnits(), 2);
                int winningTeam = unitsOnTeam1 > unitsOnTeam2 ? 1 : 2;

                if (barracks.getTeam() == winningTeam)
                    barracks.setBuildSpeed(new BigDecimal(1));
                else
                    barracks.setBuildSpeed(new BigDecimal(1));
            }
        }

        if (unit instanceof Peasant)
        {
            Peasant peasant = (Peasant) unit;
            if (clearOfBarracks(unit, map))
            {
                Unit unitCreated = new Barracks(peasant.getTeam());

                unitCreated.setLocation(map.getAvailableAdjacentLocation(peasant.getLocation()));
                map.addUnitToExistingUnits(unitCreated);
                peasant.setTimeOfLastBuild(GameLogic.now());
                map.getExistingUnits().remove(peasant);

                Log.logInfo(simulationStart, peasant + " has created a hicks.combat.entities.Barracks " + unitCreated);
            }
            else
                performIdleBehavior(unit, simulationStart, map);
        }
    }

    private static boolean clearOfBarracks(Unit unit, GameMap map)
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

    private static void performHostileBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        if (unit.isTargetInRange() && unit.isReadyToAttack())
            CombatLogic.performAttack(unit, map, simulationStart);
        else
            UnitLogic.moveTowardCoordinate(unit, unit.getTarget().getLocation(), true);
    }

    private static void performIdleBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, map.getExistingUnits());

        if (closestVisibleEnemy == null)
        {
            if (unit.getDestination() == null)
                unit.setDestination(map.getRandomPointOnMap());

            UnitLogic.moveTowardCoordinate(unit, unit.getDestination(), false);
        }
        else
        {
            unit.setDestination(null);
            unit.setTarget(closestVisibleEnemy);
            if (unit.getTarget() != null) Log.logInfo(simulationStart, unit + " has targeted " + unit.getTarget() + "!");
        }
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

    public static BigDecimal now()
    {
        return new BigDecimal(System.nanoTime());
    }

    public static int getUnitsOnTeam(List<Unit> units, int team)
    {
        int result = 0;
        for (Unit unit : units)
            if (unit.getTeam() == team) result++;

        return result;
    }
}
