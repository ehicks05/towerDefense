package hicks.td.entities;

import hicks.td.Util;

import java.math.BigDecimal;

public class ArrowTower extends Unit
{
    public ArrowTower(int team)
    {
        setTeam(team);
        setSizeRadius(16);
        setSightRadius(40);

        setCurrentHp(1);
        setMaxHp(1);
        setArmor(1);
        setMinDamage(12);
        setMaxDamage(18);
        setAttackRange(40);

        setAttackSpeed(new BigDecimal(1.5));
        setTimeOfLastAttack(Util.now());
    }
}
