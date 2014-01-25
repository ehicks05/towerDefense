package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class Peasant extends Mob
{
    public Peasant(int team)
    {
        setTeam(team);
        setSizeRadius(24);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());

        setCurrentHp(50);
        setMaxHp(50);
        setArmor(0);
    }
}
