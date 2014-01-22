package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class Archer extends Unit
{
    public Archer(int team)
    {
        setTeam(team);
        setSizeRadius(40);
        setSightRadius(40);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());

        setCurrentHp(50);
        setMaxHp(50);
        setArmor(0);
        setMinDamage(12);
        setMaxDamage(18);
        setAttackRange(100);

        setAttackSpeed(new BigDecimal(1.5));
        setTimeOfLastAttack(Util.now());
    }
}
