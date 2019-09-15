package net.ehicks.td.logic;

import net.ehicks.td.World;
import net.ehicks.td.audio.SoundEffect;
import net.ehicks.td.audio.SoundManager;
import net.ehicks.td.entities.Mob;
import net.ehicks.td.entities.Point;
import net.ehicks.td.entities.Projectile;
import net.ehicks.td.entities.Unit;
import net.ehicks.td.util.Util;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ProjectileLogic
{
    public static void shootProjectile(Unit source, Projectile projectile, Point destination)
    {
        projectile.setLocation(source.getLocation());
        projectile.setDestination(destination);

        double unitX   = source.getLocation().getX();
        double unitY   = source.getLocation().getY();
        double targetX = destination.getX();
        double targetY = destination.getY();
        projectile.setTheta(Math.atan2(targetY - unitY, targetX - unitX) + .8); // todo deal with this magic bullshit

        SoundEffect soundEffect = SoundEffect.valueOf(projectile.getFireSound());
        SoundManager.playSFX(soundEffect);
        World.addUnit(projectile);
    }

    public static void performProjectileBehavior(Projectile projectile, BigDecimal dt)
    {
        // check that we haven't run out of steam
        boolean outOfRange = Math.round(projectile.getDistanceTravelled()) >= projectile.getMaximumRange();
        if (outOfRange)
        {
            World.removeUnit(projectile);
            return;
        }

        if (projectile.getDestination() != null)
            UnitLogic.move(projectile, projectile.getDestination(), dt);
        else
        {
            projectile.onHit(null);
            return;
        }

        // see if we have hit anyone
        for (Mob mob : new ArrayList<>(Util.getMobs()))
        {
            // don't hit the guy you just hit - glaives are more fun when they bounce around
            if (projectile.getName().equals("Glaive"))
            {
                if (projectile.getLastMobHit() != null && projectile.getLastMobHit().equals(mob))
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
