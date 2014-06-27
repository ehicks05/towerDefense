package hicks.td.entities.mob;

import hicks.td.entities.Point;
import hicks.td.entities.UnitLogic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;

public class MobLogic
{
    public static void moveAlongPath(Mob mob)
    {
        Queue<Point> path = mob.getPath();
        Point pathPoint = path.peek();

        if (pathPoint != null)
        {
            BigDecimal currentDistance = new BigDecimal(mob.getLocation().getDistance(pathPoint)).setScale(0, RoundingMode.HALF_UP);
            if (currentDistance.equals(BigDecimal.ZERO))
            {
                path.remove();
                path.add(pathPoint);
            }
            else
                UnitLogic.move(mob, pathPoint);
        }
    }
}
