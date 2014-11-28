package hicks.td.entities.projectile;

import hicks.td.World;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;
import hicks.td.logic.UnitLogic;
import hicks.td.entities.Mob;
import hicks.td.util.NewtonRaphson;
import hicks.td.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class ProjectileLogic
{
    public static void shootProjectile(Unit source, Projectile projectile, Point destination)
    {
        projectile.setLocation(source.getLocation());
        projectile.setDestination(getProjectileDestination(projectile, destination));

        double unitX   = source.getLocation().getX();
        double unitY   = source.getLocation().getY();
        double targetX = destination.getX();
        double targetY = destination.getY();
        projectile.setTheta(Math.atan2(targetY - unitY, targetX - unitX) + .8); // todo deal with this magic bullshit

        SoundEffect soundEffect = SoundEffect.valueOf(projectile.getFireSound());
        SoundManager.playSFX(soundEffect);
        World.addUnit(projectile);
    }

    public static Point getProjectileDestination(Projectile projectile, Point destination)
    {
        // calculate x,y weighting
        double deltaXDouble = destination.getDeltaX(projectile.getLocation());
        double deltaYDouble = destination.getDeltaY(projectile.getLocation());
        BigDecimal deltaX = new BigDecimal(deltaXDouble);
        BigDecimal deltaY = new BigDecimal(deltaYDouble);

        BigDecimal distanceFromTarget = NewtonRaphson.bigSqrt(deltaX.pow(2).add(deltaY.pow(2)));

        BigDecimal factorX = deltaX.divide(distanceFromTarget, 16, RoundingMode.HALF_UP);
        BigDecimal factorY = deltaY.divide(distanceFromTarget, 16, RoundingMode.HALF_UP);

        BigDecimal projectileRange = new BigDecimal(projectile.getMaximumRange()).setScale(0, RoundingMode.HALF_UP).add(new BigDecimal(10));
        BigDecimal distanceToMoveX = projectileRange.multiply(factorX);
        BigDecimal distanceToMoveY = projectileRange.multiply(factorY);

        double resultX = projectile.getLocation().getX() + distanceToMoveX.doubleValue();
        double resultY = projectile.getLocation().getY() + distanceToMoveY.doubleValue();

        return new Point(resultX, resultY);
    }

    public static void performProjectileBehavior(Projectile projectile, BigDecimal dt)
    {
        // check that we haven't run out of steam
        if (Math.round(projectile.getDistanceTravelled()) >= projectile.getMaximumRange())
        {
            World.removeUnit(projectile);
            return;
        }

        UnitLogic.move(projectile, projectile.getDestination(), dt);

        // see if we have hit anyone
        for (Mob mob : new ArrayList<>(Util.getMobs()))
        {
            // don't hit the guy you just hit - glaives are more fun when they bounce around
            if (projectile.getName().equals("Glaive"))
            {
                if (projectile.getMobsHit().contains(mob))
                    continue;
            }

            double distanceToMob = projectile.getLocation().getDistance(mob.getLocation());
            if (distanceToMob <= projectile.getSizeRadius() + mob.getSizeRadius())
            {
                projectile.onHit(mob);
                return; // we just hit a mob, return so we do not hit another one this tick...
            }
        }
    }
}
