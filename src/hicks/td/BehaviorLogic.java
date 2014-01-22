package hicks.td;

import hicks.td.audio.SoundManager;
import hicks.td.entities.*;
import hicks.td.util.NewtonRaphson;
import hicks.td.util.TileLoader;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public final class BehaviorLogic
{
    private static final int PLAYER_TEAM = 1;
    private static final int ENEMY_TEAM  = 2;

    public static void updateState()
    {
        // spawn units
        if (GameState.getSpawner().isReadyToBuild() && GameState.getPlayer().getRound() < 6)
        {
            Unit unit = new Footman(2);
            unit.setLocation(new Point(TileLoader.roadOffset - 16, 0));
            unit.setPath(createPath());

            if (GameState.getPlayer().getRound() > 1)
            {
                unit.setCurrentHp(unit.getCurrentHp() + 10 * GameState.getPlayer().getRound());
                unit.setMaxHp(unit.getMaxHp() + 10 * GameState.getPlayer().getRound());
            }

            GameState.addUnit(unit);
            GameState.getSpawner().setTimeOfLastBuild(Util.now());
            GameState.getSpawner().incrementUnitsCreated();

            if (GameState.getSpawner().getUnitsCreated() % 20 == 0)
            {
                GameState.getPlayer().setRound(GameState.getPlayer().getRound() + 1);
            }
        }

        // update units on the field
        for (Unit unit : new ArrayList<>(GameState.getUnits()))
        {
            if (!unit.isAlive()) continue;

            if (unit.getTeam() == PLAYER_TEAM)
            {
                if (unit instanceof Tower) chooseTowerBehavior(unit);
                if (unit instanceof Projectile) performProjectileBehavior(unit);
            }
            if (unit.getTeam() == ENEMY_TEAM) performEnemyBehavior(unit);
        }
    }

    private static Queue<Point> createPath()
    {
        Queue<Point> path = new ArrayBlockingQueue<>(2);
        path.add(new Point(TileLoader.roadOffset - 16, 0));
        path.add(new Point(TileLoader.roadOffset - 16, GameState.getGameMap().getHeight() - 1));
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
            shoot(unit);

        if (unit.getTarget() != null && !unit.isTargetInRange())
            unit.setTarget(null);
    }

    private static void shoot(Unit unit)
    {
        Projectile arrow = new Arrow(1);
        arrow.setLocation(unit.getLocation());
        arrow.setDestination(getProjectileDestination(arrow, unit.getTarget().getLocation()));

        double unitX = unit.getLocation().getX();
        double unitY = unit.getLocation().getY();
        double targetX = unit.getTarget().getLocation().getX();
        double targetY = unit.getTarget().getLocation().getY();
        arrow.setTheta(Math.atan2(targetY - unitY, targetX - unitX) + .8);

        SoundManager.playShootSFX();
        GameState.addUnit((Unit) arrow);
        unit.setTimeOfLastAttack(Util.now());
    }

    private static Point getProjectileDestination(Projectile arrow, Point targetLocation)
    {
        // calculate x,y weighting
        double deltaXDouble = targetLocation.getDeltaX(arrow.getLocation());
        double deltaYDouble = targetLocation.getDeltaY(arrow.getLocation());
        BigDecimal deltaX = new BigDecimal(deltaXDouble);
        BigDecimal deltaY = new BigDecimal(deltaYDouble);

        BigDecimal distanceFromTarget = NewtonRaphson.bigSqrt(deltaX.pow(2).add(deltaY.pow(2)));

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
            if (otherUnit.getTeam() != ENEMY_TEAM) continue;

            if (unit.getLocation().getDistance(otherUnit.getLocation()) <= unit.getSizeRadius() + otherUnit.getSizeRadius())
                performProjectileHit(unit, otherUnit);
        }
    }

    private static void performProjectileHit(Unit unit, Unit victim)
    {
        SoundManager.playHitSFX();
        CombatLogic.performAttack(unit, victim);
        GameState.removeUnit(unit);
    }

    private static void performEnemyBehavior(Unit unit)
    {
        UnitLogic.moveAlongPath(unit);
        if (unit.getPath().size() == 0)
        {
            UnitLogic.removeUnitAsTarget(unit);
            GameState.removeUnit(unit);
            GameState.getPlayer().removeLife();
        }
    }
}
