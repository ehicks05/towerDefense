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
        arrow.setDestination(getProjectileDestination(arrow, unit.getTarget().getLocation()));
        GameState.addUnit(arrow);

        unit.setTimeOfLastAttack(Util.now());
    }

    private static Point getProjectileDestination(Arrow arrow, Point targetLocation)
    {
        // calculate x,y weighting
        double deltaXDouble = targetLocation.getDeltaX(arrow.getLocation());
        double deltaYDouble = targetLocation.getDeltaY(arrow.getLocation());
        BigDecimal deltaX = new BigDecimal(deltaXDouble);
        BigDecimal deltaY = new BigDecimal(deltaYDouble);

        BigDecimal distanceFromTarget = bigSqrt(deltaX.pow(2).add(deltaY.pow(2)));

        BigDecimal factorX = deltaX.divide(distanceFromTarget, 16, RoundingMode.HALF_UP);
        BigDecimal factorY = deltaY.divide(distanceFromTarget, 16, RoundingMode.HALF_UP);

        BigDecimal projectileRange = new BigDecimal(arrow.getMaximumRange()).setScale(0, RoundingMode.HALF_UP).add(new BigDecimal(10));
        BigDecimal distanceToMoveX = projectileRange.multiply(factorX);
        BigDecimal distanceToMoveY = projectileRange.multiply(factorY);

        double resultX = arrow.getLocation().getX() + distanceToMoveX.doubleValue();
        double resultY = arrow.getLocation().getY() + distanceToMoveY.doubleValue();

        return new Point(resultX, resultY);
    }

    private static void performProjectileBehavior(Unit unit)
    {
        // check that we haven't run out of steam
        Arrow arrow = (Arrow) unit;
        if (Math.round(arrow.getDistanceTravelled()) >= arrow.getMaximumRange())
            GameState.removeUnit(arrow);

        Point destination = unit.getDestination();
        UnitLogic.move(unit, destination);

        // see if we have hit anyone
        for (Unit otherUnit : new ArrayList<>(GameState.getUnits()))
        {
            if (otherUnit.getTeam() != 2) continue;

            if (unit.getLocation().getDistance(otherUnit.getLocation()) <= otherUnit.getSizeRadius())
                performProjectileHit(unit, otherUnit);
        }
    }

    private static void performProjectileHit(Unit unit, Unit unitThatGotHit)
    {
        CombatLogic.performAttack(unit, unitThatGotHit);
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

    // ------------------

    private static final BigDecimal SQRT_DIG = new BigDecimal(150);
    private static final BigDecimal SQRT_PRE = new BigDecimal(10).pow(SQRT_DIG.intValue());

    /**
     * Private utility method used to compute the square root of a BigDecimal.
     */
    private static BigDecimal sqrtNewtonRaphson  (BigDecimal c, BigDecimal xn, BigDecimal precision){
        BigDecimal fx = xn.pow(2).add(c.negate());
        BigDecimal fpx = xn.multiply(new BigDecimal(2));
        BigDecimal xn1 = fx.divide(fpx,2*SQRT_DIG.intValue(),RoundingMode.HALF_DOWN);
        xn1 = xn.add(xn1.negate());
        BigDecimal currentSquare = xn1.pow(2);
        BigDecimal currentPrecision = currentSquare.subtract(c);
        currentPrecision = currentPrecision.abs();
        if (currentPrecision.compareTo(precision) <= -1){
            return xn1;
        }
        return sqrtNewtonRaphson(c, xn1, precision);
    }

    /**
     * Uses Newton Raphson to compute the square root of a BigDecimal.
     */
    public static BigDecimal bigSqrt(BigDecimal c){
        return sqrtNewtonRaphson(c,new BigDecimal(1),new BigDecimal(1).divide(SQRT_PRE));
    }
}
