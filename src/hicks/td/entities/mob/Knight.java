package hicks.td.entities.mob;

import hicks.td.util.Util;

public class Knight extends Mob
{
    public Knight(int team)
    {
        setTeam(team);
        setSizeRadius(50);
        setMoveSpeed(70);
        setTimeOfLastMove(Util.now());

        setCurrentHp(100);
        setMaxHp(100);
        setArmor(12);
        setBounty(5);
    }
}
