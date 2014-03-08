package hicks.td.ui;

import hicks.td.World;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;

import java.util.ArrayList;
import java.util.List;

public class InterfaceUtil
{
    public static int snapToGrid(int input)
    {
        int snappedNumber;

        if (input % 32 >= 16)
            snappedNumber = input + input % 32;
        else
            snappedNumber = input - input % 32;

        return snappedNumber;
    }

    public static boolean isValidLocation(int x, int y, int radiusOfNewBuilding)
    {
        Point attemptedBuildLocation = new Point(x, y);
        List<Unit> units = new ArrayList<>(World.getUnits());

        // check against existing units
        for (Unit unit : units)
        {
            Point unitLocation = unit.getLocation();
            double distance = attemptedBuildLocation.getDistance(unitLocation);
            if (distance < unit.getSizeRadius() + radiusOfNewBuilding)
                return false;
        }

        // check against edge of map
        if (!isObjectInBounds(x, y, radiusOfNewBuilding)) return false;

        // check against terrain
        // todo

        return true;
    }

    private static boolean isObjectInBounds(int x, int y, int radius)
    {
        return (x - radius >= 0 && x + radius < World.getGameMap().getWidth() &&
                y - radius >= 0 && y + radius < World.getGameMap().getHeight());
    }
}
