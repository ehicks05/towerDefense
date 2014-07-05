package hicks.td.entities;

import hicks.td.World;
import hicks.td.entities.projectile.Projectile;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    public static List<Mob> getClosestVisibleEnemies(Unit callingUnit, int attackRange)
    {
        return getClosestVisibleEnemies(callingUnit, attackRange, null);
    }

    public static List<Mob> getClosestVisibleEnemies(Unit callingUnit, int attackRange, List<Mob> exceptions)
    {
        return getClosestVisibleEnemies(callingUnit, attackRange, null, 1);

    }

    public static List<Mob> getClosestVisibleEnemies(Unit callingUnit, int attackRange, List<Mob> exceptions, int sizeToReturn)
    {
        final Point callingUnitLocation = callingUnit.getLocation();

        // filter out unwanted mobs: calling unit, same team units, exception units, and out of range units
        List<Mob> mobsToProcess = new ArrayList<>();
        for (Mob mob : new ArrayList<>(Util.getMobs()))
        {
            if (mob == callingUnit || mob.getTeam() == callingUnit.getTeam())
                continue;

            if (exceptions != null && exceptions.size() > 0 && exceptions.contains(mob))
                continue;

            double distance = new BigDecimal(mob.getLocation().getDistance(callingUnitLocation)).setScale(0, RoundingMode.HALF_UP).doubleValue();
            if (distance > attackRange)
                continue;

            mobsToProcess.add(mob);
        }

        // sort remaining mobs by distance
        Collections.sort(mobsToProcess, new Comparator<Mob>()
        {
            @Override
            public int compare(Mob o1, Mob o2)
            {
                double o1Distance = o1.getLocation().getDistance(callingUnitLocation);
                double o2Distance = o2.getLocation().getDistance(callingUnitLocation);

                if (o1Distance < o2Distance) return -1;
                if (o1Distance > o2Distance) return 1;

                return 0;
            }
        });

        List<Mob> results = mobsToProcess;

        if (results.size() > sizeToReturn)
            results = mobsToProcess.subList(0, sizeToReturn);

        return results;
    }

    public static int getUnitsOnTeam(int team)
    {
        List<Unit> units = new ArrayList<>(World.getUnits());

        for (Iterator<Unit> i = units.iterator(); i.hasNext();)
        {
            Unit unit = i.next();
            if (unit.getTeam() != team) i.remove();
        }

        return units.size();
    }
}
