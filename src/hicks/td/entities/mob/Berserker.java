package hicks.td.entities.mob;

import hicks.td.util.Util;

public class Berserker extends Mob
{
    public Berserker(int team)
    {
        setTeam(team);
        setSizeRadius(40);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());

        setCurrentHp(60);
        setMaxHp(60);
        setArmor(1);
    }
}
