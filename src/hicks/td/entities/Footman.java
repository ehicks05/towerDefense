package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class Footman extends Mob
{
    public Footman(int team)
    {
        setTeam(team);
        setSizeRadius(12);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());

        setCurrentHp(60);
        setMaxHp(60);
        setArmor(1);
    }
}
