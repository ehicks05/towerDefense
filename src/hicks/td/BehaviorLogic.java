package hicks.td;

import hicks.td.entities.Footman;
import hicks.td.entities.Unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BehaviorLogic
{
    public static void updateState()
    {
        // spawn units
        if (GameState.getSpawner().isReadyToBuild())
        {
            Unit unit = new Footman(2);
            unit.setLocation(new Point(MapBuilder.xOffset - 16, 0));
            unit.setPath(createPath());
            GameState.addUnit(unit);
            GameState.getSpawner().setTimeOfLastBuild(GameLogic.now());
        }

        // update units on the field
        List<Unit> unitsToProcess = new ArrayList<>(GameState.getUnits());  // copy list to try to avoid concurrentModificationExceptions

        for (Unit unit : unitsToProcess)
        {
            if (!unit.isAlive()) continue;

            if (unit.getTeam() == 1) chooseTowerBehavior(unit);
            if (unit.getTeam() == 2) performEnemyBehavior(unit);
        }
    }

    private static Queue<Point> createPath()
    {
        Queue<Point> path = new ArrayBlockingQueue<>(2);
        path.add(new Point(MapBuilder.xOffset - 16, 0));
        path.add(new Point(MapBuilder.xOffset - 16, Init.WORLD_HEIGHT - 1));
        return path;
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
            UnitLogic.moveTowardCoordinate(unit, unit.getTarget().getLocation());
    }

    private static void performEnemyBehavior(Unit unit)
    {
        UnitLogic.moveAlongPath(unit);
        if (unit.getPath().size() == 0)
        {
            GameState.removeUnit(unit);
            GameState.getPlayer().removeLife();
        }
    }
}
