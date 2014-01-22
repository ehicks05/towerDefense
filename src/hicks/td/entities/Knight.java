package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class Knight extends Unit
{
    public Knight(int team)
    {
        setTeam(team);
        setSizeRadius(50);
        setSightRadius(60);
        setMoveSpeed(100);
        setTimeOfLastMove(Util.now());

        setCurrentHp(100);
        setMaxHp(100);
        setArmor(3);
        setMinDamage(20);
        setMaxDamage(30);
        setAttackRange(10);

        setAttackSpeed(new BigDecimal(2));
        setTimeOfLastAttack(Util.now());
    }
}
