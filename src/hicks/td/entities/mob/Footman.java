package hicks.td.entities.mob;

import hicks.td.util.Util;

public class Footman extends Mob
{
    public Footman(int team)
    {
        setTeam(team);
        setSizeRadius(32);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());

        setCurrentHp(60);
        setMaxHp(60);
        setArmor(8);
    }
}
