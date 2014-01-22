package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class ArrowTower extends Unit implements Tower
{
    public ArrowTower(int team)
    {
        setTeam(team);
        setSizeRadius(30);
        setSightRadius(256);

        setCurrentHp(1);
        setMaxHp(1);
        setArmor(1);
        setMinDamage(12);
        setMaxDamage(18);
        setAttackRange(256);

        setAttackSpeed(new BigDecimal(1.5));
        setTimeOfLastAttack(Util.now());
    }
}
