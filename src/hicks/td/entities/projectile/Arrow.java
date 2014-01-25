package hicks.td.entities.projectile;

import hicks.td.util.Util;

public class Arrow extends Projectile
{
    public Arrow(int team)
    {
        setTeam(team);
        setSizeRadius(10);
        setMoveSpeed(300);
        setTimeOfLastMove(Util.now());

        setMinDamage(12);
        setMaxDamage(18);
        setMaximumRange(800);
    }
}
