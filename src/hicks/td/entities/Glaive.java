package hicks.td.entities;

import hicks.td.util.Util;

public class Glaive extends Projectile
{
    public Glaive(int team)
    {
        setTeam(team);
        setSizeRadius(10);
        setMoveSpeed(300);
        setTimeOfLastMove(Util.now());

        setMinDamage(12);
        setMaxDamage(18);
        setMaximumRange(600);
    }
}
