package hicks.combat;

import hicks.combat.entities.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BehaviorLogic
{
    public static void updateState()
    {
        // copy list to try to avoid concurrentModificationExceptions
        List<Unit> unitsToProcess = new ArrayList<>(GameState.getUnits());

        for (Unit unit : unitsToProcess)
        {
            if (!unit.isAlive()) continue;

//            chooseBehavior(simulationStart, map, unit);
            unit.update();
        }
    }

    private static void chooseBehavior(BigDecimal simulationStart, GameMap map, Unit unit)
    {
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
                    case 0: barracksCreation = new Knight(barracks.getTeam());
                        break;
                    case 1: barracksCreation = new Peasant(barracks.getTeam());
                        break;
                    default: barracksCreation = new Footman(barracks.getTeam());
                }

                barracksCreation.setLocation(map.getAvailableAdjacentLocation(barracks.getLocation()));
                GameState.addUnit(barracksCreation);
                barracks.setTimeOfLastBuild(GameLogic.now());

                Log.logInfo(barracks + " has created a footman " + barracksCreation);

                int unitsOnTeam1 = GameLogic.getUnitsOnTeam(GameState.getUnits(), 1);
                int unitsOnTeam2 = GameLogic.getUnitsOnTeam(GameState.getUnits(), 2);
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
            if (GameLogic.isClearOfBarracks(unit))
            {
                Unit unitCreated = new Barracks(peasant.getTeam());

                unitCreated.setLocation(map.getAvailableAdjacentLocation(peasant.getLocation()));
                GameState.addUnit(unitCreated);
                peasant.setTimeOfLastBuild(GameLogic.now());
                GameState.removeUnit(peasant);

                Log.logInfo(peasant + " has created a hicks.combat.entities.Barracks " + unitCreated);
            }
            else
                performIdleBehavior(unit, simulationStart, map);
        }
    }

    // hostile behavior is approach target, then hit target
    private static void performHostileBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        if (UnitLogic.isTargetInRange(unit) && unit.isReadyToAttack())
            CombatLogic.performAttack(unit);
        else
            UnitLogic.moveTowardCoordinate(unit, unit.getTarget().getLocation(), true);
    }

    // idle behavior is random wandering until we find an enemy
    private static void performIdleBehavior(Unit unit, BigDecimal simulationStart, GameMap map)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, GameState.getUnits());

        if (closestVisibleEnemy == null)
        {
            if (unit.getDestination() == null)
                unit.setDestination(map.getRandomPoint());

            UnitLogic.moveTowardCoordinate(unit, unit.getDestination(), false);
        }
        else
        {
            unit.setDestination(null);
            unit.setTarget(closestVisibleEnemy);
            if (unit.getTarget() != null) Log.logInfo(unit + " has targeted " + unit.getTarget() + "!");
        }
    }
}
