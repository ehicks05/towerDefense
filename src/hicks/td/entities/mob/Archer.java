package hicks.td.entities.mob;

import hicks.td.util.Util;

public class Archer extends Mob
{
    public Archer(int team)
    {
        setTeam(team);
        setSizeRadius(32);
        setMoveSpeed(55);
        setTimeOfLastMove(Util.now());

        setCurrentHp(50);
        setMaxHp(50);
        setArmor(4);
        setBounty(2);
    }
}
