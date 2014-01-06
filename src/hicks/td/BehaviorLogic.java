package hicks.td;

import hicks.td.entities.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public final class BehaviorLogic
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
            GameState.getSpawner().setTimeOfLastBuild(Util.now());
        }

        // update units on the field
        List<Unit> unitsToProcess = new ArrayList<>(GameState.getUnits());  // copy list to avoid concurrentModificationExceptions

        for (Unit unit : unitsToProcess)
        {
            if (!unit.isAlive()) continue;

            if (unit.getTeam() == 1)
            {
                if (unit instanceof Tower) chooseTowerBehavior(unit);
                if (unit instanceof Projectile) performProjectileBehavior(unit);
            }
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
        if (unit.getTarget() == null) lookForTarget(unit);
        if (unit.getTarget() != null) performHostileBehavior(unit);
    }

    private static void lookForTarget(Unit unit)
    {
        Unit closestVisibleEnemy = UnitLogic.getClosestVisibleEnemy(unit);
        if (closestVisibleEnemy != null)
            unit.setTarget(closestVisibleEnemy);
    }

    private static void performHostileBehavior(Unit unit)
    {
        if (unit.isTargetInRange() && unit.isReadyToAttack())
            shootArrow(unit);

        if (unit.getTarget() != null && !unit.isTargetInRange()) unit.setTarget(null);
    }

    private static void shootArrow(Unit unit)
    {
        Arrow arrow = new Arrow(1);
        arrow.setLocation(unit.getLocation());
//        arrow.setDestination(getProjectileDestination(arrow, unit.getTarget().getLocation()));
        arrow.setTarget(unit.getTarget());
        GameState.addUnit(arrow);

        unit.setTimeOfLastAttack(Util.now());
    }

    private static Point getProjectileDestination(Arrow arrow, Point targetLocation)
    {
        BigDecimal projectileRange = new BigDecimal(arrow.getMaximumRange()).setScale(0, RoundingMode.HALF_UP);

        // calculate x,y weighting
        BigDecimal deltaX = new BigDecimal(targetLocation.getDeltaX(arrow.getLocation()));
        BigDecimal deltaY = new BigDecimal(targetLocation.getDeltaY(arrow.getLocation()));

        BigDecimal factorX = deltaX.divide(projectileRange, 16, RoundingMode.HALF_UP);
        BigDecimal factorY = deltaY.divide(projectileRange, 16, RoundingMode.HALF_UP);

//        BigDecimal distanceToMoveX = actualDistanceToMove.multiply(factorX);
//        BigDecimal distanceToMoveY = actualDistanceToMove.multiply(factorY);

        return null;
    }

    private static void performProjectileBehavior(Unit unit)
    {
        Unit target = unit.getTarget();

        if (target != null)
        {
            Point targetLocation = target.getLocation();
            UnitLogic.move(unit, targetLocation);

            if (unit.getLocation().getDistance(targetLocation) < target.getSizeRadius())
                performProjectileHit(unit);
        }
        Arrow arrow = (Arrow) unit;
        if (arrow.getDistanceTravelled() >= arrow.getMaximumRange())
        {
            GameState.removeUnit(arrow);
        }
    }

    private static void performProjectileHit(Unit unit)
    {
        CombatLogic.performAttack(unit);
        GameState.removeUnit(unit);
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
