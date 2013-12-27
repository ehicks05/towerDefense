package hicks.td;

import hicks.td.entities.Unit;

import java.math.BigDecimal;
import java.util.List;

public class GameLogic
{
    public static BigDecimal now()
    {
        return new BigDecimal(System.currentTimeMillis());
    }

    public static BigDecimal getElapsedTime(BigDecimal startTime)
    {
        return GameLogic.now().subtract(startTime).divide(new BigDecimal("1000"));
    }

    public static int getUnitsOnTeam(List<Unit> units, int team)
    {
        int result = 0;
        for (Unit unit : units)
            if (unit.getTeam() == team) result++;

        return result;
    }
}
