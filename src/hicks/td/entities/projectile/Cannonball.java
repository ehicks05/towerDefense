package hicks.td.entities.projectile;

import hicks.td.CombatLogic;
import hicks.td.GameState;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.Explosion;
import hicks.td.entities.Unit;
import hicks.td.entities.UnitLogic;
import hicks.td.entities.mob.Mob;
import hicks.td.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Cannonball extends Projectile
{
    private int m_splashRadius;

    public Cannonball(int team)
    {
        setTeam(team);
        setSizeRadius(10);
        setMoveSpeed(600);
        setTimeOfLastMove(Util.now());

        setMinDamage(24);
        setMaxDamage(30);
        setMaximumRange(600);
        setFireSound(SoundEffect.CANNON_FIRE);
        setSplashRadius(64);
    }

    public void performProjectileHit(Mob victim)
    {
        // get all mobs caught in the splash radius
        List<Mob> mobsHit = new ArrayList<>();
        for (Mob mob : new ArrayList<>(Util.getMobs()))
        {
            if (mob.getLocation().getDistance(this.getLocation()) <= this.getSplashRadius())
                mobsHit.add(mob);
        }

        for (Mob mob : mobsHit)
        {
            CombatLogic.performAttack(this, mob);
        }

        // add a visual explosion
        Explosion explosion = new Explosion();
        explosion.setLocation(this.getLocation());
        GameState.addUnit(explosion);

        SoundManager.playSFX(SoundEffect.CANNON_HIT);
        GameState.removeUnit(this);

    }

    public int getSplashRadius()
    {
        return m_splashRadius;
    }

    public void setSplashRadius(int splashRadius)
    {
        m_splashRadius = splashRadius;
    }
}