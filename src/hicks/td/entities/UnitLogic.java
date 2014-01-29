package hicks.td.entities;

import hicks.td.GameState;
import hicks.td.entities.*;
import hicks.td.entities.mob.Mob;
import hicks.td.entities.projectile.Projectile;
import hicks.td.entities.tower.Tower;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

public final class UnitLogic
{
    public static Unit getUnitByObjectId(int id)
    {
        List<Unit> units = new ArrayList<>(GameState.getUnits());

        for (Unit unit : units)
            if (unit.getObjectId() == id)
                return unit;

        return null;
    }

    public static Unit removeUnitAsTarget(Unit formerTarget)
    {
        List<Unit> units = new ArrayList<>(GameState.getUnits());

        for (Unit unit : units)
        {
            if (unit instanceof Tower)
            {
                Tower tower = (Tower) unit;
                if (tower.getTarget() != null && tower.getTarget().equals(formerTarget))
                    tower.setTarget(null);
            }
        }
        return null;
    }

    public static void move(Unit unit, Point destination)
    {
        BigDecimal moveSpeed                = new BigDecimal(unit.getMoveSpeed());
        BigDecimal timeSinceLastMove        = Util.getElapsedTime(unit.getTimeOfLastMove());
        BigDecimal potentialDistanceToMove  = moveSpeed.multiply(timeSinceLastMove);
        BigDecimal currentDistance          = new BigDecimal(unit.getLocation().getDistance(destination)).setScale(0, RoundingMode.HALF_UP);

        BigDecimal desiredDistance = BigDecimal.ZERO;

        // if we are within our desired distance, stop.
        if (currentDistance.equals(desiredDistance))
        {
            unit.setDestination(null);
            return;
        }

        // prevent overshooting the destination
        BigDecimal actualDistanceToMove = trimOvershoot(potentialDistanceToMove, currentDistance, desiredDistance);
        if (unit instanceof Projectile)
        {
            Projectile projectile = (Projectile) unit;
            projectile.setDistanceTravelled(projectile.getDistanceTravelled() + actualDistanceToMove.doubleValue());
        }

        // calculate x,y weighting
        BigDecimal deltaX = new BigDecimal(destination.getDeltaX(unit.getLocation()));
        BigDecimal deltaY = new BigDecimal(destination.getDeltaY(unit.getLocation()));

        BigDecimal factorX = deltaX.divide(currentDistance, 16, RoundingMode.HALF_UP);
        BigDecimal factorY = deltaY.divide(currentDistance, 16, RoundingMode.HALF_UP);

        BigDecimal distanceToMoveX = actualDistanceToMove.multiply(factorX);
        BigDecimal distanceToMoveY = actualDistanceToMove.multiply(factorY);

        double newX = new BigDecimal(unit.getLocation().getX()).add((distanceToMoveX)).doubleValue();
        double newY = new BigDecimal(unit.getLocation().getY()).add((distanceToMoveY)).doubleValue();

        unit.setLocation(new Point(newX, newY));
        unit.setTimeOfLastMove(Util.now());
    }

    private static BigDecimal trimOvershoot(BigDecimal potentialDistanceToMove, BigDecimal currentDistance, BigDecimal desiredDistance)
    {
        BigDecimal expectedNewDistance = currentDistance.subtract(potentialDistanceToMove);
        BigDecimal actualDistanceToMove = potentialDistanceToMove;
        if (expectedNewDistance.compareTo(desiredDistance) == -1)
        {
            BigDecimal excessMovement = desiredDistance.subtract(expectedNewDistance);
            actualDistanceToMove = potentialDistanceToMove.subtract(excessMovement);
        }
        return actualDistanceToMove;
    }


    public static Mob getClosestVisibleEnemy(Unit callingUnit, int attackRange)
    {
        return getClosestVisibleEnemy(callingUnit, attackRange, null);
    }

    public static Mob getClosestVisibleEnemy(Unit callingUnit, int attackRange, List<Mob> exceptions)
    {
        Mob closestEnemy = null;
        double smallestDistance = Double.MAX_VALUE;

        for (Mob mob : new ArrayList<>(GameState.getMobs()))
        {
            if (mob == callingUnit || mob.getTeam() == callingUnit.getTeam())
                continue;

            if (exceptions != null && exceptions.size() > 0 && exceptions.contains(mob))
                continue;

            double distance = callingUnit.getLocation().getDistance(mob.getLocation());
            if (distance <= attackRange && distance < smallestDistance)
            {
                closestEnemy = mob;
                smallestDistance = distance;
            }
        }

        return closestEnemy;
    }

    public static void moveAlongPath(Mob mob)
    {
        Queue<Point> path = mob.getPath();
        Point pathPoint = path.peek();

        if (pathPoint != null)
        {
            BigDecimal currentDistance = new BigDecimal(mob.getLocation().getDistance(pathPoint)).setScale(0, RoundingMode.HALF_UP);
            if (currentDistance.equals(BigDecimal.ZERO))
                path.remove();
            else
                move(mob, pathPoint);
        }
    }

    public static int getUnitsOnTeam(int team)
    {
        List<Unit> units = new ArrayList<>(GameState.getUnits());

        for (Iterator<Unit> i = units.iterator(); i.hasNext();)
        {
            Unit unit = i.next();
            if (unit.getTeam() != team) i.remove();
        }

        return units.size();
    }
}
