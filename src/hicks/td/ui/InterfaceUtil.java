package hicks.td.ui;

import hicks.td.GameState;
import hicks.td.entities.Point;
import hicks.td.entities.Unit;

import java.util.ArrayList;
import java.util.List;

public class InterfaceUtil
{
    public static boolean isValidLocation(int x, int y, int radiusOfNewBuilding)
    {
        Point attemptedBuildLocation = new Point(x, y);
        List<Unit> units = new ArrayList<>(GameState.getUnits());

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
        return (x - radius >= 0 && x + radius < GameState.getGameMap().getWidth() &&
                y - radius >= 0 && y + radius < GameState.getGameMap().getHeight());
    }
}
