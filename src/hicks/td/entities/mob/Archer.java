package hicks.td.entities.mob;

import hicks.td.entities.Unit;
import hicks.td.util.Util;

import java.math.BigDecimal;

public class Archer extends Unit
{
    public Archer(int team)
    {
        setTeam(team);
        setSizeRadius(40);
        setMoveSpeed(50);
        setTimeOfLastMove(Util.now());
    }
}
