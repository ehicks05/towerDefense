package hicks.td.entities;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class ArrowTower extends Tower
{
    public ArrowTower(int team)
    {
        setTeam(team);
        setSizeRadius(30);
        setAttackRange(256);

        setAttackSpeed(new BigDecimal(1.5));
        setTimeOfLastAttack(Util.now());
    }
}
