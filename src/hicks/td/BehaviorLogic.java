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

            if (unit.getTeam() == 1) chooseTowerBehavior(unit);
            if (unit.getTeam() == 2) performEnemyBehavior(unit);
        }
    }

    private static void chooseTowerBehavior(Unit unit)
    {
        if (unit.getTarget() == null)
            performIdleBehavior(unit);
        if (unit.getTarget() != null)
            performHostileBehavior(unit);
    }

    private static void performIdleBehavior(Unit unit)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit);
        if (closestVisibleEnemy != null)
            unit.setTarget(closestVisibleEnemy);
    }

    private static void performHostileBehavior(Unit unit)
    {
        if (UnitLogic.isTargetInRange(unit) && unit.isReadyToAttack())
            CombatLogic.performAttack(unit);
        else
            UnitLogic.moveTowardCoordinate(unit, unit.getTarget().getLocation(), true);
    }

    private static void performEnemyBehavior(Unit unit)
    {
        UnitLogic.moveAlongPath(unit);
    }
}
