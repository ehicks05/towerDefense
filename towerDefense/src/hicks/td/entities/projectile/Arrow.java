package hicks.td.entities.projectile;

import hicks.td.audio.SoundEffect;
import hicks.td.entities.Upgrade;
import hicks.td.util.Util;

import java.util.List;

public class Arrow extends Projectile
{
    public Arrow(int team, List<Upgrade> upgrades)
    {
        setTeam(team);
        setSizeRadius(12);
        setMoveSpeed(300);
        setTimeOfLastMove(Util.now());

        setMinDamage(12);
        setMaxDamage(18);
        setMaximumRange(800);
        setFireSound(SoundEffect.SHOOT_ARROW);

        applyUpgrades(upgrades);
    }
}
