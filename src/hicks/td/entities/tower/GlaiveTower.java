package hicks.td.entities.tower;

import hicks.td.util.Util;

import java.math.BigDecimal;

public class GlaiveTower extends Tower
{
    public GlaiveTower(int team)
    {
        setTeam(team);
        setSizeRadius(30);
        setAttackRange(160);

        setPrice(100);
        setAttackSpeed(new BigDecimal(1));
        setTimeOfLastAttack(Util.now());
    }
}
