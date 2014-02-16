package hicks.td.entities.mob;

import hicks.td.util.Util;

public class Peasant extends Mob
{
    public Peasant(int team)
    {
        setTeam(team);
        setSizeRadius(32);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());

        setCurrentHp(50);
        setMaxHp(50);
        setArmor(0);
    }
}
