package hicks.td.entities.projectile;

import hicks.td.logic.CombatLogic;
import hicks.td.World;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.Explosion;
import hicks.td.entities.Mob;
import hicks.td.logic.UnitLogic;
import hicks.td.util.Util;

import java.util.ArrayList;
import java.util.List;

public class HitEffectLogic
{
    public static void applyHitEffect(Projectile source, Mob victim)
    {
        String code = source.getOnHitEffect();
        if (code.equals("NONE"))
        {
            CombatLogic.performAttack(source, victim);
            World.removeUnit(source);
            SoundManager.playSFX(SoundEffect.WEAPON_HIT);
        }

        if (code.equals("BOUNCE")) bounce(source, victim);
        if (code.equals("SLOW")) slow(source, victim);
        if (code.equals("EXPLODE")) explode(source, victim);
    }

    public static void bounce(Projectile source, Mob victim)
    {
        CombatLogic.performAttack(source, victim);
        World.removeUnit(source);
        SoundManager.playSFX(SoundEffect.WEAPON_HIT);

        // attempt to bounce to another mob
        if (source.getHitsPerformed() < source.getHitsPossible())
        {
            List<Mob> mobsHit = source.getMobsHit();
            mobsHit.add(victim);

            List<Mob> closestVisibleEnemies = UnitLogic.getClosestVisibleEnemies(source, source.getBounceRange(), mobsHit, 1);
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
                glaive.setMobsHit(mobsHit);

                glaive.setDestination(ProjectileLogic.getProjectileDestination(glaive, closestVisibleEnemy.getLocation()));

                SoundManager.playSFX(SoundEffect.SHOOT_GLAIVE);
                World.addUnit(glaive);
            }
        }
    }

    public static void explode(Projectile source, Mob victim)
    {
        // get all mobs caught in the splash radius
        List<Mob> mobsHit = new ArrayList<>();
        for (Mob mob : new ArrayList<>(Util.getMobs()))
            if (mob.getLocation().getDistance(source.getLocation()) <= source.getSplashRadius())
                mobsHit.add(mob);

        for (Mob mob : mobsHit)
            CombatLogic.performAttack(source, mob);

        // add a visual explosion
        Explosion explosion = new Explosion();
        explosion.setLocation(source.getLocation());
        World.addUnit(explosion);

        SoundManager.playSFX(SoundEffect.CANNON_HIT);
        World.removeUnit(source);
    }

    public static void slow(Projectile source, Mob victim)
    {
        CombatLogic.performAttack(source, victim);
        World.removeUnit(source);
        SoundManager.playSFX(SoundEffect.ICE_HIT);

        if (victim.getSlowInstances() < 4)
        {
            victim.setMoveSpeed((int) (victim.getMoveSpeed() * .8));
            victim.setSlowInstances(victim.getSlowInstances() + 1);
        }
    }
}
