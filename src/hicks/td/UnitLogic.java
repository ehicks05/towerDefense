package hicks.td;

import hicks.td.entities.Arrow;
import hicks.td.entities.Projectile;
import hicks.td.entities.Unit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;

public final class UnitLogic
{
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
        if (unit instanceof Arrow)
        {
            Arrow arrow = (Arrow) unit;
            arrow.setDistanceTravelled(arrow.getDistanceTravelled() + actualDistanceToMove.doubleValue());
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

    public static Unit getClosestVisibleEnemy(Unit callingUnit)
    {
        Unit closestEnemy = null;
        double smallestDistance = Double.MAX_VALUE;

        for (Unit unit : GameState.getUnits())
        {
            if (unit == callingUnit || unit.getTeam() == callingUnit.getTeam())
                continue;

            double distance = callingUnit.getLocation().getDistance(unit.getLocation());
            if (distance <= callingUnit.getSightRadius() && distance < smallestDistance)
            {
                closestEnemy = unit;
                smallestDistance = distance;
            }
        }

        return closestEnemy;
    }

    public static void moveAlongPath(Unit unit)
    {
        Queue<Point> path = unit.getPath();
        Point pathPoint = path.peek();

        if (pathPoint != null)
        {
            BigDecimal currentDistance = new BigDecimal(unit.getLocation().getDistance(pathPoint)).setScale(0, RoundingMode.HALF_UP);
            if (currentDistance.equals(BigDecimal.ZERO))
                path.remove();
            else
                move(unit, pathPoint);
        }
    }
}
