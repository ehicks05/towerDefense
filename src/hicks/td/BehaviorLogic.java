package hicks.td;

import hicks.td.entities.Unit;

import java.util.ArrayList;
import java.util.List;

public class BehaviorLogic
{
    public static void updateState()
    {
        // copy list to try to avoid concurrentModificationExceptions
        List<Unit> unitsToProcess = new ArrayList<>(GameState.getUnits());

        for (Unit unit : unitsToProcess)
        {
            if (!unit.isAlive()) continue;

            chooseBehavior(unit);
        }
    }

    private static void chooseBehavior(Unit unit)
    {
        if (unit.getTarget() == null)
            performIdleBehavior(unit);
        if (unit.getTarget() != null)
            performHostileBehavior(unit);
    }

    // hostile behavior is approach target, then hit target
    private static void performHostileBehavior(Unit unit)
    {
        if (UnitLogic.isTargetInRange(unit) && unit.isReadyToAttack())
            CombatLogic.performAttack(unit);
        else
            UnitLogic.moveTowardCoordinate(unit, unit.getTarget().getLocation(), true);
    }

    // idle behavior is random wandering until we find an enemy
    private static void performIdleBehavior(Unit unit)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit, GameState.getUnits());

        if (closestVisibleEnemy == null)
        {
            if (unit.getDestination() == null)
                unit.setDestination(GameState.getGameMap().getRandomPoint());

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
