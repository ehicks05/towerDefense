package hicks.td.entities.projectile;

import hicks.td.CombatLogic;
import hicks.td.World;
import hicks.td.audio.SoundEffect;
import hicks.td.audio.SoundManager;
import hicks.td.entities.mob.Mob;
import hicks.td.util.Util;

public class IceBolt extends Projectile
{
    public IceBolt(int team)
    {
        setTeam(team);
        setSizeRadius(12);
        setMoveSpeed(300);
        setTimeOfLastMove(Util.now());

        setMinDamage(32);
        setMaxDamage(36);
        setMaximumRange(300);
        setFireSound(SoundEffect.SHOOT_ARROW);
    }

    public void onHit(Mob victim)
    {
        CombatLogic.performAttack(this, victim);
        World.removeUnit(this);
        SoundManager.playSFX(SoundEffect.ICE_HIT);

        if (victim.getSlowInstances() < 4)
        {
            victim.setMoveSpeed((int) (victim.getMoveSpeed() * .8));
            victim.setSlowInstances(victim.getSlowInstances() + 1);
        }
    }

    // -------------- Properties

}
