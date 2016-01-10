package hicks.td.logic;

import hicks.td.entities.Animation;
import hicks.td.entities.Projectile;
import hicks.td.World;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.Mob;
import hicks.td.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HitEffectLogic
{
    public static void applyHitEffect(Projectile source, Mob victim)
    {
        String code = source.getOnHitEffect();
        if (code.equals("NONE"))
        {
            if (victim != null)
                CombatLogic.performAttack(source, victim);
            World.removeUnit(source);
            SoundManager.playSFX(SoundEffect.WEAPON_HIT);
        }

        if (code.equals("BOUNCE")) bounce(source, victim);
        if (code.equals("SLOW")) slow(source, victim);
        if (code.equals("EXPLODE")) explode(source);
    }

    public static void bounce(Projectile source, Mob victim)
    {
        World.removeUnit(source);
        SoundManager.playSFX(SoundEffect.WEAPON_HIT);

        if (victim != null)
        {
            CombatLogic.performAttack(source, victim);
            // attempt to bounce to another mob
            if (source.getHitsPerformed() < source.getHitsPossible())
            {
                List<Mob> lastMobHit = Arrays.asList(victim);

                List<Mob> closestVisibleEnemies = UnitLogic.getClosestVisibleEnemies(source, source.getBounceRange(), lastMobHit, 1);
                Mob closestVisibleEnemy = null;
                if (closestVisibleEnemies.size() > 0)
                    closestVisibleEnemy = closestVisibleEnemies.get(0);

                if (closestVisibleEnemy != null)
                {
                    Projectile glaive = new Projectile(World.getProjectileByName("Glaive"));
                    glaive.applyUpgrades(source.getUpgrades());
                    glaive.setLocation(source.getLocation());
                    glaive.setOriginator(source.getOriginator());
                    glaive.setHitsPerformed(source.getHitsPerformed() + 1);
                    glaive.setLastMobHit(victim);

                    glaive.setDestination(closestVisibleEnemy.getLocation());

                    SoundManager.playSFX(SoundEffect.SHOOT_GLAIVE);
                    World.addUnit(glaive);
                }
            }
        }
    }

    public static void explode(Projectile source)
    {
        // get all mobs caught in the splash radius
        List<Mob> mobsHit = new ArrayList<>();
        for (Mob mob : new ArrayList<>(Util.getMobs()))
            if (mob.getLocation().getDistance(source.getLocation()) <= source.getSplashRadius())
                mobsHit.add(mob);

        for (Mob mob : mobsHit)
            CombatLogic.performAttack(source, mob);

        // add a visual explosion
        Animation animation = new Animation("explosion", 96, source.getLocation());
        World.addUnit(animation);

        SoundManager.playSFX(SoundEffect.CANNON_HIT);
        World.removeUnit(source);
    }

    public static void slow(Projectile source, Mob victim)
    {
        World.removeUnit(source);
        SoundManager.playSFX(SoundEffect.ICE_HIT);

        if (victim != null)
        {
            CombatLogic.performAttack(source, victim);
            if (victim.getSlowInstances() < 4)
            {
                victim.setMoveSpeed((int) (victim.getMoveSpeed() * .8));
                victim.setSlowInstances(victim.getSlowInstances() + 1);
            }
        }
    }
}
